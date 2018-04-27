/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;


import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Repository
public class CrawlHouseDao {

	private static final Log logger = LogFactory.getLog(CrawlHouseDao.class);

	@Autowired
	private CrawlHouseDetailRepository crawlHouseDetailRepository;
	/**
	 * 添加房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
//	public void addCrawlHousePublishDto(BasicDBObject bdbObj) {
//		try{
//			
//			
//			CrawlHouseDetail crawlHouseDetail= CrawlHouseUtil.getCrawlHouseDetail(bdbObj);
//			CrawlHouseDetail newCrawlHouseDetail = crawlHouseDetailRepository.findByDepartmentId(crawlHouseDetail.getDepartmentId());
//			
//			if(newCrawlHouseDetail != null){
//				newCrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_NO);// 设置房源收集状态
//				crawlHouseDetailRepository.save(newCrawlHouseDetail);
//			}else{
//				//同步远程mongodb数据到本地
//				crawlHouseDetailRepository.save(crawlHouseDetail);
//			}
//
//		}catch (Exception e) {
//			logger.error(LogUtils.getCommLog("同步远程mongodb数据到本地失败" + e.getMessage()));
//			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "同步远程mongodb数据到本地失败");
//		}	
//	}
	
	public void addCrawlHousePublishDto(List<CrawlHouseDetail> insertList) {
		try{
			//同步远程mongodb数据到本地
			crawlHouseDetailRepository.save(insertList);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("同步远程mongodb数据到本地失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "同步远程mongodb数据到本地失败");
		}	
	}
	
	public void updateCrawlHousePublishDto(List<CrawlHouseDetail> updateList) {
		try{
			//同步远程mongodb数据到本地
			crawlHouseDetailRepository.save(updateList);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("同步远程mongodb数据到本地失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "同步远程mongodb数据到本地失败");
		}	
	}
	
	
	public int setIsDeleteByAppId(int isDelete,int appId) {
		try{
			//批量下架
			return crawlHouseDetailRepository.setIsDeleteByAppId(isDelete,appId);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("爬虫数据批量下架失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "爬虫数据批量下架失败");
		}	
	}
	
	public CrawlHouseDetail findByDepartmentId(String departmentId) {
		CrawlHouseDetail newCrawlHouseDetail = null;
		try{
			 newCrawlHouseDetail = crawlHouseDetailRepository.findByDepartmentId(departmentId);	
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("同步远程mongodb数据到本地失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "同步远程mongodb数据到本地失败");
		}
		return newCrawlHouseDetail;	
	}
	
	public List<String>  findFirstAllByAppId(int appId) {
		List<String>  oldList = new ArrayList<>();
		try{
			oldList = crawlHouseDetailRepository.findFirstAllByAppId(appId);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("批量查询所有爬虫数据失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "批量查询所有爬虫数据失败");
		}
		return oldList;	
	}
	
	public List<CrawlHouseDetail>  findAllByAppId(int appId) {
		List<CrawlHouseDetail>  oldHouseDetailList = new ArrayList<>();
		try{
			oldHouseDetailList = crawlHouseDetailRepository.findAllByAppId(appId);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("批量查询所有爬虫数据失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "批量查询所有爬虫数据失败");
		}
		return oldHouseDetailList;	
	}
}
