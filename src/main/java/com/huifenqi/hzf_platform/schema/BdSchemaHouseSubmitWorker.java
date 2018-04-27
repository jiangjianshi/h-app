/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaHouseSubmitWork.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月4日下午6:13:02 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import com.huifenqi.hzf_platform.dao.repository.house.BdHousePictureRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: SchemaHouseSubmitWork date: 2017年4月14日 下午13:13:02 Description:
 * 提供要处理的房源
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
public class BdSchemaHouseSubmitWorker implements Runnable {

	private static Log logger = LogFactory.getLog(BdSchemaHouseSubmitWorker.class);

	private LinkedBlockingQueue<BdHousePicture> queue;


	private BdHousePictureRepository bdHousePictureRepository;

	public BdSchemaHouseSubmitWorker(LinkedBlockingQueue<BdHousePicture> queue,
									 BdHousePictureRepository bdHousePictureRepository) {
		this.queue = queue;
		this.bdHousePictureRepository = bdHousePictureRepository;
	}

	@Override
	public void run() {
		while (true) {
			try {
				doWork();
			} catch (Throwable e) {
				logger.error(String.format("提处理房源图片的线程出错：%s", e.toString()));
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
			//当队列为空时从数据库中查询未保存过的图片，查询前100个未处理的图处。
			List<BdHousePicture> housePictures=bdHousePictureRepository.findFirst100AllByIsRun(Constants.HouseBase.IS_RUN_NO);
			if (housePictures != null && !housePictures.isEmpty()) {
				for (BdHousePicture bdHousePicture : housePictures) {
					//将图片数据加入到队列中
					if (!queue.offer(bdHousePicture)) {
					    // 停止往队列里面放数据
						logger.info(LogUtils.getCommLog(String.format("将图片(%s)加入到待处理队列失败.", bdHousePicture.getId())));
						break;
					}
					logger.info(LogUtils.getCommLog(String.format("将图片(%s)加入到待处理队列.", bdHousePicture.toString())));
				}
			} else {
				// 没有查到数据，则休眠1分钟
				TimeUnit.SECONDS.sleep(30);
			}
		} else {
			logger.info(LogUtils.getCommLog(String.format("待处理队列不为空，休眠等待下次查询.")));
			// 休眠1分钟
			TimeUnit.SECONDS.sleep(30);
		}
	}
}
