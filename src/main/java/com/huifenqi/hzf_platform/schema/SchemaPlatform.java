/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaPlatform.java 
 * Package Name: com.huifenqi.hzf_platform.schema 
 * Date: 2016年5月5日下午6:14:21 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.OssConfiguration;
import com.huifenqi.hzf_platform.configuration.SchemaConfiguration;
import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.ThirdSysDao;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository;
import com.huifenqi.hzf_platform.dao.repository.house.BdHousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.CrawlHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseOperateRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.location.AreaRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository;
import com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository;
import com.huifenqi.hzf_platform.handler.ThirdSysHouseRequestHandler;
import com.huifenqi.hzf_platform.service.BaiduService;
import com.huifenqi.hzf_platform.service.FileService;

/**
 * ClassName: SchemaPlatform date: 2016年5月5日 下午6:14:21 update date: 2017年5月22日
 * 下午6:14:21 Description:schema管理平台
 * 
 * @author xiaozhan,changmingwei
 * @version
 * @since JDK 1.8
 */
@Component
public class SchemaPlatform {

	private LinkedBlockingQueue<HouseDetail> queue;

	private LinkedBlockingQueue<BdHousePicture> housePicQueue;

	private LinkedBlockingQueue<CrawlHouseDetail> crawlQueue;

	private LinkedBlockingQueue<CrawlHouseDetail> crawlDeleteQueue;

	private BdHousePictureRepository bdHousePictureRepository;

	private ConcurrentSkipListSet<String> processingHouses;

	private ConcurrentSkipListSet<String> processingCrawlDeleteHouses;

	private ConcurrentSkipListSet<String> processingCrawlHouses;

	private HouseDetailRepository houseDetailRepository;

	private HousePictureRepository housePictureRepository;

	private CrawlHouseDetailRepository crawlHouseDetailRepository;

	private BaiduService baiduService;

	private FileService fileService;

	private ExecutorService executors;

	private SchemaConfiguration schemaConfiguration;

	private SubwayRepository subwayRepository;

	private CityRepository cityRepository;

	private AreaRepository areaRepository;

	private DistrictRepository districtRepository;

	private HouseBaseRepository houseBaseRepository;

	private RoomBaseRepository roomBaseRepository;

	private RedisCacheManager redisCacheManager;

	private HouseDao houseDao;

	private ThirdSysDao thirdSysDao;

	private List<HouseDetail> listHouseDetail;

	private OssConfiguration ossConfig;

	private TaskConfiguration taskConfiguration;

	private CompanyPayMonthRepository companyPayMonthRepository;

	private ThirdSysHouseRequestHandler thirdSysHouseRequestHandler;

	private HouseOperateRecordRepository operateRecordRepository;

	public SchemaPlatform() {

	}

	@Autowired
	private SchemaPlatform(SchemaConfiguration schemaConfiguration, HouseDetailRepository houseDetailRepository,
			HousePictureRepository housePictureRepository, BaiduService baiduService, FileService fileService,
			SubwayRepository subwayRepository, CityRepository cityRepository, AreaRepository areaRepository,
			DistrictRepository districtRepository, HouseBaseRepository houseBaseRepository,
			RoomBaseRepository roomBaseRepository, BdHousePictureRepository bdHousePictureRepository,
			RedisCacheManager redisCacheManager, CrawlHouseDetailRepository crawlHouseDetailRepository,
			HouseDao houseDao, ThirdSysDao thirdSysDao, OssConfiguration ossConfig, TaskConfiguration taskConfiguration,
			CompanyPayMonthRepository companyPayMonthRepository,
			ThirdSysHouseRequestHandler thirdSysHouseRequestHandler,
			HouseOperateRecordRepository operateRecordRepository) {
		this.houseDetailRepository = houseDetailRepository;
		this.housePictureRepository = housePictureRepository;
		this.baiduService = baiduService;
		this.fileService = fileService;
		this.schemaConfiguration = schemaConfiguration;
		this.subwayRepository = subwayRepository;
		this.cityRepository = cityRepository;
		this.areaRepository = areaRepository;
		this.districtRepository = districtRepository;
		this.houseBaseRepository = houseBaseRepository;
		this.roomBaseRepository = roomBaseRepository;
		this.bdHousePictureRepository = bdHousePictureRepository;
		this.redisCacheManager = redisCacheManager;
		this.crawlHouseDetailRepository = crawlHouseDetailRepository;
		this.houseDao = houseDao;
		this.thirdSysDao = thirdSysDao;
		this.ossConfig = ossConfig;
		this.taskConfiguration = taskConfiguration;
		this.companyPayMonthRepository = companyPayMonthRepository;
		this.thirdSysHouseRequestHandler = thirdSysHouseRequestHandler;
		this.operateRecordRepository = operateRecordRepository;
		if (!taskConfiguration.isScheduleStatus()) {
			return;
		}
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		queue = new LinkedBlockingQueue<>(1024);
		housePicQueue = new LinkedBlockingQueue<>(1024);
		crawlQueue = new LinkedBlockingQueue<>(1024);
		crawlDeleteQueue = new LinkedBlockingQueue<>(1024);

		processingHouses = new ConcurrentSkipListSet<>();
		processingCrawlHouses = new ConcurrentSkipListSet<>();
		processingCrawlDeleteHouses = new ConcurrentSkipListSet<>();
		listHouseDetail = new ArrayList<>();
		int collecWorkNums = schemaConfiguration.getCollectWorkerNum();
		if (collecWorkNums == 0) {
			collecWorkNums = Runtime.getRuntime().availableProcessors() + 1;
		}
		int submitWorkNums = 1;
		executors = Executors.newFixedThreadPool(collecWorkNums + submitWorkNums);

		// 处理房源信息
		for (int i = 0; i < submitWorkNums; i++) {
			executors.submit(new SchemaHouseDataCollectWorker(queue, processingHouses, houseDetailRepository,
					housePictureRepository, baiduService, fileService, schemaConfiguration, subwayRepository,
					cityRepository, areaRepository, districtRepository, houseBaseRepository, roomBaseRepository,
					redisCacheManager, listHouseDetail, ossConfig, companyPayMonthRepository));
		}
		// 收集房源信息
		for (int i = 0; i < submitWorkNums; i++) {
			executors.submit(new SchemaHouseSubmitWorker(queue, processingHouses, houseDetailRepository));
		}

		// 模拟百度图片处理线程 
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(new BdSchemaHouseDataCollectWorker(housePicQueue, bdHousePictureRepository, fileService));
		//		}
		//		// 模拟百度收集待处理图片
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(new BdSchemaHouseSubmitWorker(housePicQueue, bdHousePictureRepository));
		//		}
		// 查询待处理爬虫数据
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(
		//					new CrawlSchemaHouseSubmitWorker(crawlQueue, crawlHouseDetailRepository, processingCrawlHouses));
		//		}

		// 处理爬虫数据发布房源
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(new CrawlSchemaHouseDataCollectWorker(crawlQueue, crawlHouseDetailRepository, houseDao,
		//					processingCrawlHouses));
		//		}

		// 查询待下架爬虫数据
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(new CrawlSchemaDeleteHouseSubmitWorker(crawlDeleteQueue, crawlHouseDetailRepository,
		//					processingCrawlDeleteHouses));
		//		}

		// 处理待下架爬虫数据
		//		for (int i = 0; i < submitWorkNums; i++) {
		//			executors.submit(new CrawlSchemaDeleteHouseDataCollectWorker(crawlDeleteQueue, crawlHouseDetailRepository,
		//					houseDao, processingCrawlDeleteHouses, houseBaseRepository, roomBaseRepository));
		//		}

		//处理房源操作历史数据
		for (int i = 0; i < submitWorkNums; i++) {
			executors.submit(new RedisHistorySchemaDeleteHouseDataCollerWorker(redisCacheManager, houseDao, thirdSysDao,
					houseDetailRepository, houseBaseRepository, roomBaseRepository, thirdSysHouseRequestHandler));
		}

		//保存操作记录
		for (int i = 0; i < submitWorkNums; i++) {
			executors.submit(new RedisSaveHouseOptRecordWorker(operateRecordRepository, redisCacheManager, houseDetailRepository, houseBaseRepository, roomBaseRepository));
		}

	}
}
