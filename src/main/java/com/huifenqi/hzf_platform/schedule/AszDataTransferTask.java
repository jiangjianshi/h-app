package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetailAsz;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailAszRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;

@Service
public class AszDataTransferTask {

	@Resource
	private HouseDetailAszRepository aszHouseDetailRepository;

	@Resource
	private HouseDao houseDao;
	@Resource
	private RedisCacheManager redisCacheManager;

	@Resource
	private AszDataTransferService aszDataTransferService;

	@Resource
	private HouseRecommendRepository houseRecommendRepository;

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private RoomBaseRepository roomBaseRepository;

	@Resource
	private TaskConfiguration taskConfiguration;

	private static Logger logger = LoggerFactory.getLogger(AszDataTransferTask.class);

	@Scheduled(cron = "${hfq.asz.transfer.house.task}") // 每五分钟执行一次
	public void transferData() throws Exception {

		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行爱上租房源处理定时任务");
			return;			
		}
		long startTime = System.currentTimeMillis();
		List<HouseDetailAsz> aszHouseDetailList = aszHouseDetailRepository.findByTransferFlag();
		if (CollectionUtils.isEmpty(aszHouseDetailList)) {
			long endTime = System.currentTimeMillis();
			logger.info("没有待同步的爱上租房源,任务结束。本次耗时：{}ms", endTime - startTime);
			return;
		}
		for (HouseDetailAsz asz : aszHouseDetailList) {

			String rentType = asz.getRentType();// 枚举值 ENTIRE.整套出租 SHARE.单间出租
			if (rentType.equals("ENTIRE")) {
				try {
					logger.info("爱上租整租，发布房源");
					aszDataTransferService.publishHouse(asz);
				} catch (Exception e) {
					logger.error("爱上租发布房源失败");
				}
			} else if (rentType.equals("SHARE")) {
				try {
					logger.info("爱上租分租，发布房源和房间");
					aszDataTransferService.pulbicHouseAndRoom(asz);
				} catch (Exception e) {
					logger.error("爱上租发布房源失败");
				}
			}
		}

		long endTime = System.currentTimeMillis();
		logger.info("爱上租转化房源结束，本次任务耗时：{}ms", endTime - startTime);
	}

}
