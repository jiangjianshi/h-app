package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.cache.MongoCrawlManager;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.dao.CrawlHouseDao;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.CrawlHouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.mongodb.BasicDBObject;

/**
 * ClassName: HouseRequestHandler date: 2016年4月26日 下午4:40:45 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Component
public class CrawlHouseRequestHandler {

	private static final Log logger = LogFactory.getLog(CrawlHouseRequestHandler.class);

	@Autowired
	private CrawlHouseDao crawlHouseDao;
	
//	@Autowired
//	private HouseDao houseDao;

	@Autowired
	private MongoCrawlManager mongoCrawlManager;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;

	/**
	 * 同步远程mongodb数据到本地
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public BdResponses initMongodbData(HttpServletRequest request) {
		List<CrawlHouseDetail> updateList = new ArrayList<>();
		List<CrawlHouseDetail> insertList = new ArrayList<>();
		List<CrawlHouseDetail> newList = new ArrayList<>();
		List<String> newStrList = new ArrayList<>();
		List<String> newStrCopyList = new ArrayList<>();
		List<String> oldStrList = new ArrayList<>();
		List<CrawlHouseDetail> oldHouseDetailList = new ArrayList<>();
		String appId = null;
		String dataTable = null;
		int crawlType = 0;
		int limit = 0;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
			crawlType = RequestUtils.getParameterInt(request, "crawlType");
			dataTable = RequestUtils.getParameterString(request, "dataTable");
			limit = RequestUtils.getParameterInt(request, "limit");
		} catch (Exception e) {
		}
		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}
		
		//验证爬虫类型
		if (crawlType <= 0) {
			logger.error(LogUtils.getCommLog(String.format("未找到爬虫数据类型,crawlType:%s", crawlType)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "爬虫数据类型不能为空");
		}
		
		//验证爬虫表名
		if (dataTable == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到爬虫数据表名,crawlType:%s", crawlType)));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "爬虫数据表名不能为空");
		}
		

		//读取mongodb爬虫数据到消息队列
		mongoCrawlManager.startMongoDBConn();
		LinkedBlockingQueue<BasicDBObject> bdojQueue = mongoCrawlManager.readColData(crawlType,dataTable,limit);
		mongoCrawlManager.startMongoDBConn();
		
		//获取已经上次同步完成的爬虫房源	
		oldStrList = crawlHouseDao.findFirstAllByAppId(crawlType);
		oldHouseDetailList = crawlHouseDao.findAllByAppId(crawlType);

		//批量下架
		int rows = crawlHouseDao.setIsDeleteByAppId(CrawlConstants.CrawlHouseDetail.IS_DELETE_YES,crawlType);

		if(rows < 0){
			logger.error(LogUtils.getCommLog(String.format("批量下架房源失败")));
			return new BdResponses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "批量下架房源失败");
		}
		
		//处理数据
		if (bdojQueue.size() > 0) {		
			logger.error(LogUtils.getCommLog(String.format("读取到爬虫数据条数为:%s", bdojQueue.size())));
			BasicDBObject bdbObj;
			try {
				boolean done = false;  
	            while (!done) {  
	                //取出队首元素，如果队列为空，则阻塞  
					logger.info("队列剩余数据条数为："+bdojQueue.size());
					if(bdojQueue.size() == 0){
						done = true;
						break;
					}
					bdbObj = bdojQueue.take();	
					
					if (bdbObj != null) {
						//获取爬虫房源对象
						CrawlHouseDetail crawlHouseDetail = CrawlHouseUtil.getCrawlHouseDetailByCrawlType(bdbObj,crawlType);
						newList.add(crawlHouseDetail);//获取最新爬虫数据
						newStrList.add(crawlHouseDetail.getDepartmentId());//获取最新公寓ID
						newStrCopyList.add(crawlHouseDetail.getDepartmentId());//获取最新公寓ID
//						//CrawlHouseDetail newCrawlHouseDetail = crawlHouseDao.findByDepartmentId(crawlHouseDetail.getDepartmentId());
					}
	            } 
				logger.info("爬虫数据分组开始");
				
	            newStrList.removeAll(oldStrList);//获取集合差集,需要的房源信息
	            newStrCopyList.removeAll(newStrList);//获取集合差集,需要更新的房源信息
	            
	            if(!newList.isEmpty()){
	            	//获取新增房源结合
		            for(CrawlHouseDetail crawlHouseDetail : newList){
		            	
		            	if(newStrList.contains(crawlHouseDetail.getDepartmentId())){
			            		//同步远程mongodb数据到本地							
		    					insertList.add(crawlHouseDetail);
		    				}
		            }
	            }
	            
	            if(!newStrCopyList.isEmpty()){
	            	for(CrawlHouseDetail crawlHouseDetail : oldHouseDetailList){
		            	if(newStrCopyList.contains(crawlHouseDetail.getDepartmentId())){	
		            		//设置初始值
		            		crawlHouseDetail = CrawlHouseUtil.getCrawlHouseDetailFlag(crawlHouseDetail,crawlType);
		    				updateList.add(crawlHouseDetail);
		            	}
		            }
	            }
                
	            logger.info("爬虫数据分组，更新爬虫数据条数为："+updateList.size());
	            logger.info("爬虫数据分组，新增爬虫数据条数为："+insertList.size());
            	logger.info(LogUtils.getCommLog(String.format("同步远程mongodb数据到本地开始")));
	            if(!updateList.isEmpty()){
					crawlHouseDao.addCrawlHousePublishDto(updateList);
		            logger.info(LogUtils.getCommLog(String.format("同步远程mongodb数据到本地，执行update完成")));
	            }	
	            if(!insertList.isEmpty()){
					crawlHouseDao.addCrawlHousePublishDto(insertList);
		            logger.info(LogUtils.getCommLog(String.format("同步远程mongodb数据到本地，执行insert完成")));
	            }

	            logger.info(LogUtils.getCommLog(String.format("同步远程mongodb数据到本地完成")));
			} catch (InterruptedException e) {
				e.printStackTrace();
				BdResponses bdResponses = new BdResponses();
				bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
				bdResponses.setMsg(e.toString());
			}
		}

		BdResponses bdResponses = new BdResponses();
		bdResponses.setCode(ErrorMsgCode.ERROR_MSG_OK);
		bdResponses.setMsg("同步远程mongodb数据到本地成功");

		return bdResponses;
	}
}