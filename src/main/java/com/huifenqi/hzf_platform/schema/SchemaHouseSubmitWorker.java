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

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClassName: SchemaHouseSubmitWork date: 2016年5月4日 下午6:13:02 Description:
 * 提供要处理的房源
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class SchemaHouseSubmitWorker implements Runnable {

	private static Log logger = LogFactory.getLog(SchemaHouseSubmitWorker.class);

	private LinkedBlockingQueue<HouseDetail> queue;

	private ConcurrentSkipListSet<String> processingHouses;


	private HouseDetailRepository houseDetailRepository;

	public SchemaHouseSubmitWorker(LinkedBlockingQueue<HouseDetail> queue,
			ConcurrentSkipListSet<String> processingHouses, HouseDetailRepository houseDetailRepository) {
		this.queue = queue;
		this.processingHouses = processingHouses;
		this.houseDetailRepository = houseDetailRepository;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(String.format("提供待处理房源的线程出错：%s", e.toString()));
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
		if (queue.isEmpty()) {
			List<String> temphouses = new ArrayList<>();
			temphouses.addAll(processingHouses);
			// 每次取100条
			List<HouseDetail> houses = null;
			if (temphouses.isEmpty()) {
				houses = houseDetailRepository.findFirst100ByIsRunAndIsDeleteOrderByCreateTimeAsc(
						Constants.HouseDetail.HOUSE_UNRUN, Constants.Common.STATE_IS_DELETE_NO);
			} else {
				houses = houseDetailRepository.findFirst100BySellIdNotInAndIsRunAndIsDeleteOrderByCreateTimeAsc(
						temphouses, Constants.HouseDetail.HOUSE_UNRUN, Constants.Common.STATE_IS_DELETE_NO);
			}
			if (houses != null && !houses.isEmpty()) {
				for (HouseDetail house : houses) {
					if (!queue.offer(house)) {
						// 停止往队列里面放数据
						logger.info(LogUtils.getCommLog(String.format("将房间(%s)加入到待处理队列失败.", house.getSellId())));
						break;
					}
					processingHouses.add(house.getSellId());
					logger.info(LogUtils.getCommLog(String.format("将房间(%s)加入到待处理队列.", house.getSellId())));
				}
			} else {
				// 没有查到数据，则休眠1分钟
				TimeUnit.SECONDS.sleep(60);
			}
		} else {
			logger.info(LogUtils.getCommLog(String.format("待处理队列不为空，休眠等待下次查询.")));
			// 休眠1分钟
			TimeUnit.SECONDS.sleep(60);
		}
	}
}
