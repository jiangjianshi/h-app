/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseDataCollectWorker.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:14:47 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.SchemaConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.*;
import com.huifenqi.hzf_platform.context.entity.location.Area;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.District;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.dao.repository.house.*;
import com.huifenqi.hzf_platform.dao.repository.location.AreaRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository;
import com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository;
import com.huifenqi.hzf_platform.service.BaiduService;
import com.huifenqi.hzf_platform.service.FileService;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClassName: SchemaHouseDataCollectWorker date: 2017年4月15日 下午6:14:47
 * Description: 收集房源信息
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
public class BdSchemaHouseDataCollectWorker implements Runnable {

	private static Log logger = LogFactory.getLog(BdSchemaHouseDataCollectWorker.class);

	private LinkedBlockingQueue<BdHousePicture> queue;

	private BdHousePictureRepository bdHousePictureRepository;

	private FileService fileService;

	public BdSchemaHouseDataCollectWorker(LinkedBlockingQueue<BdHousePicture> queue, BdHousePictureRepository bdHousePictureRepository, FileService fileService) {
		this.queue = queue;
		this.bdHousePictureRepository = bdHousePictureRepository;
		this.fileService = fileService;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(LogUtils.getCommLog(String.format("收集待处理房源图片的线程出错:%s", e.toString())));
			}
		}
	}

	/**
	 * 工作方法
	 */
	private void doWork() throws Exception {
		BdHousePicture bdHousePicture = queue.take();
		try {
			try {
                // 收集图片信息
                collectionBdPicData(bdHousePicture);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("处理房间的图片时(%s)出错:%s", bdHousePicture.getSellId(), e.toString())));
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("处理房间的图片时(%s)出错:%s", bdHousePicture.getSellId(), e.toString())));
		} finally {
			// 从正在处理的集合中移除该房间
			logger.info(LogUtils.getCommLog(String.format("房间的图片(%s)的数据收集结束.", bdHousePicture.getSellId())));
		}
	}

	/**
	 * 模拟收集百度图片数据
	 * @param bdHousePicture
	 */
	private void collectionBdPicData(BdHousePicture bdHousePicture) throws Exception {
//			try {
//				//如果本地没有信息,则说明没收集过,进行收集
//				if (StringUtil.isBlank(bdHousePicture.getPicRootPath())) {
//					logger.info(LogUtils.getCommLog(String.format("待收集图片网络路径: (%s).", bdHousePicture.getPicWebPath())));
//					//调用系统图片下载方法将网络图片保存到本地服务器
//					String localUrl = fileService.downFile(bdHousePicture.getPicWebPath());
//					//设置本地图片路径并更新图片数据信息。
//					bdHousePicture.setPicRootPath(localUrl);;
//					bdHousePicture.setUpdateTime(new Date());
//					bdHousePicture.setIsRun(Constants.HouseBase.IS_RUN_YES);
//					bdHousePictureRepository.save(bdHousePicture);
//				}else {
//					//如果本地已经有信息,则说明已经收集过,不再进行收集
//				}
//			} catch (Exception e) {
//				logger.error(LogUtils.getCommLog(String.format("房间(%s)的图片(%s)下载失败:%s",bdHousePicture.getSellId(), bdHousePicture.getId(), ExceptionUtils.getStackTrace(e))));
//			}
		}

}
