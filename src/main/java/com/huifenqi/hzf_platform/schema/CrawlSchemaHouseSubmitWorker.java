/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseSubmitWork.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:13:02 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClassName: CrawlSchemaHouseSubmitWorker date: 2017年5月1日 下午13:13:02 Description:
 * 提供要处理的房源
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class CrawlSchemaHouseSubmitWorker implements Runnable {

	private static Log logger = LogFactory.getLog(CrawlSchemaHouseSubmitWorker.class);

	private LinkedBlockingQueue<CrawlHouseDetail> crawlQueue;

	private ConcurrentSkipListSet<String> processingCrawlHouses;


	private CrawlHouseDetailRepository crawlHouseDetailRepository;

	public CrawlSchemaHouseSubmitWorker(LinkedBlockingQueue<CrawlHouseDetail> crawlQueue,
			CrawlHouseDetailRepository crawlHouseDetailRepository,
			ConcurrentSkipListSet<String> processingCrawlHouses) {
		this.crawlQueue = crawlQueue;
		this.crawlHouseDetailRepository = crawlHouseDetailRepository;
		this.processingCrawlHouses = processingCrawlHouses;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(String.format("提取处理爬虫房源的线程出错：%s", e.toString()));
			}
		}
	}

	/**
	 * 工作方法
	 * 
	 * @throws InterruptedException
	 */
	private void doWork() throws InterruptedException {
		// 当前还有要等待处理的数据,就暂时不取新的数据
		if (crawlQueue.isEmpty()) {
			List<String> tempList = new ArrayList<>();
			tempList.addAll(processingCrawlHouses);
			// 每次取100条。
			List<CrawlHouseDetail> crawlList = null;
			if (tempList.isEmpty()) {
				crawlList = crawlHouseDetailRepository.findFirst100ByIsRunAndIsDelete(CrawlConstants.CrawlHouseDetail.IS_RUN_NO,CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
			} else {
				crawlList = crawlHouseDetailRepository.findFirst100ByDepartmentIdNotInAndIsRunAndIsDeleteOrderByCrawlTimeAsc(
						tempList, CrawlConstants.CrawlHouseDetail.IS_RUN_NO,
						CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
			}
			if (crawlList != null && !crawlList.isEmpty()) {
				for (CrawlHouseDetail crawlHouseDetail : crawlList) {
					// 将图片数据加入到队列中
					if (!crawlQueue.offer(crawlHouseDetail)) {
						// 停止往队列里面放数据
						logger.info(LogUtils.getCommLog(String.format("将爬虫数据(%s)加入到待处理队列失败.", crawlHouseDetail.getDepartmentId())));
						break;
					}
					processingCrawlHouses.add(crawlHouseDetail.getDepartmentId());
					logger.info(LogUtils.getCommLog(String.format("将爬虫数据公寓ID(%s)加入到待处理队列.", crawlHouseDetail.getDepartmentId())));
				}
			} else {
				// 没有查到数据，则休眠1分钟
				TimeUnit.SECONDS.sleep(30);
			}
		} else {
			logger.info(LogUtils.getCommLog(String.format("爬虫待处理队列不为空，休眠等待下次查询.")));
			// 休眠1分钟
			TimeUnit.SECONDS.sleep(10);
		}
	}
}
