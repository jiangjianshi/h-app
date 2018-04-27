/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseDataCollectWorker.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:14:47 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: SchemaHouseDataCollectWorker date: 2017年4月15日 下午6:14:47
 * Description: 收集房源信息
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class CrawlSchemaDeleteHouseDataCollectWorker implements Runnable {

	private static Log logger = LogFactory.getLog(CrawlSchemaDeleteHouseDataCollectWorker.class);

	private LinkedBlockingQueue<CrawlHouseDetail> queue;
	
	private ConcurrentSkipListSet<String> processingCrawlDeleteHouses;

	private CrawlHouseDetailRepository crawlHouseDetailRepository;
	
	private HouseDao houseDao;
	
	private HouseBaseRepository houseBaseRepository;

	private RoomBaseRepository roomBaseRepository;

	public CrawlSchemaDeleteHouseDataCollectWorker(LinkedBlockingQueue<CrawlHouseDetail> queue, CrawlHouseDetailRepository crawlHouseDetailRepository,
			HouseDao houseDao, ConcurrentSkipListSet<String> processingCrawlDeleteHouses,HouseBaseRepository houseBaseRepository,RoomBaseRepository roomBaseRepository) {
		this.queue = queue;
		this.crawlHouseDetailRepository = crawlHouseDetailRepository;
		this.houseDao = houseDao;
		this.processingCrawlDeleteHouses = processingCrawlDeleteHouses;
		this.houseBaseRepository = houseBaseRepository;
		this.roomBaseRepository = roomBaseRepository;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(LogUtils.getCommLog(String.format("收集待处理下架爬虫房源的线程出错:%s", e.toString())));
			}
		}
	}

	/**
	 * 工作方法
	 */
	private void doWork() throws Exception {
		CrawlHouseDetail crawlHouseDetail = queue.take();
		try {
			try {
				// 下架爬虫信息
                deleteCrawlHouseData(crawlHouseDetail);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("处理下架爬虫数据时(%s)出错:%s", crawlHouseDetail.getDepartmentId(), e.toString())));
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("处理下架爬虫数据(%s)出错:%s", crawlHouseDetail.getDepartmentId(), e.toString())));
		} finally {
			// 从正在处理的集合中移除该房间
			processingCrawlDeleteHouses.remove(crawlHouseDetail.getDepartmentId());
			// 从正在处理的集合中移除该房间
			logger.info(LogUtils.getCommLog(String.format("处理下架爬虫数据(%s)收集结束.", crawlHouseDetail.getDepartmentId())));
		}
	}

	
	/**
	 * 下架爬虫数据
	 * @param bdHousePicture
	 */
	@Transactional
	private void deleteCrawlHouseData(CrawlHouseDetail crawlHouseDetail) throws Exception {
			try {
				//如果本地没有信息,则说明没收集过,进行收集
				if (crawlHouseDetail != null) {					
					logger.info(LogUtils.getCommLog(String.format("爬虫数据下架，待处理下架爬虫数据编码为: (%s)", crawlHouseDetail.getDepartmentId())));
					
					try {
						houseDao.crawlDelateHouse(crawlHouseDetail);
					} catch (Exception e) {
						logger.error(LogUtils.getCommLog("下架爬虫数据状态失败" + e.getMessage()));
						throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "下架爬虫数据状态失败");
					}
				}else {
					//如果本地已经有信息,则说明已经收集过,不再进行收集
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("爬虫数据ID(%s)下载失败:%s",crawlHouseDetail.getId(), ExceptionUtils.getStackTrace(e))));
			}
		}
}

