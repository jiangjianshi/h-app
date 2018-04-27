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
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetail;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;

@Service
public class DataTransferTask {

	@Resource
	private BdHouseDetailRepository bdHouseDetailRepository;

	@Resource
	private HouseDao houseDao;

	@Resource
	private RedisCacheManager redisCacheManager;

	@Resource
	private DataTransferService dataTransferService;

	@Resource
	private HouseRecommendRepository houseRecommendRepository;

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private RoomBaseRepository roomBaseRepository;

	@Resource
	private TaskConfiguration taskConfiguration;

	private static Logger logger = LoggerFactory.getLogger(DataTransferTask.class);

	@Scheduled(cron = "${hfq.baidu.api.house.task}") // 每十五分钟执行一次
	public void transferData() throws Exception {

		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行扩展房源处理定时任务");
			return;
		}
		long startTime = System.currentTimeMillis();
		List<BdHouseDetail> bdHouseDetailList = bdHouseDetailRepository.findByTimeRegion();
		if (CollectionUtils.isEmpty(bdHouseDetailList)) {
			long endTime = System.currentTimeMillis();
			logger.info("没有待同步的房源,任务结束。本次耗时：{}ms", endTime - startTime);
			return;
		}
		for (BdHouseDetail hd : bdHouseDetailList) {

			int rentType = hd.getRentType();// 枚举值 1.整套出租 2.单间出租
			if (rentType == 1) {
				try {
					logger.info("整租，发布房源");
					dataTransferService.publishHouse(hd);
				} catch (Exception e) {
					logger.error("发布房源失败");
				}
			} else if (rentType == 2) {
				try {
					logger.info("分租，发布房源和房间");
					dataTransferService.pulbicHouseAndRoom(hd);
				} catch (Exception e) {
					logger.error("发布房源失败");
				}
			}
		}

		long endTime = System.currentTimeMillis();
		logger.info("转化房源结束，本次任务耗时：{}ms", endTime - startTime);
	}

	/**
	 * 清理推荐房源，房源下架则不展示
	 * 
	 * @throws Exception
	 */
//	@Scheduled(cron = "${hfq.recommend.house.task}")
//	@Transactional
//	public void recommendLease() {
//		long startTime = System.currentTimeMillis();
//		int delCount = 0;
//		List<HouseRecommend> hrList = houseRecommendRepository.findAllValidRecommend();
//		if (!CollectionUtils.isEmpty(hrList)) {
//			Iterator<HouseRecommend> it = hrList.iterator();
//			while (it.hasNext()) {
//				HouseRecommend hr = it.next();
//				long roomId = hr.getRoomId();
//				if (roomId == 0) {
//					HouseBase hb = houseBaseRepository.findBySellId(hr.getSellId());
//					if (hb == null || hb.getStatus() == Constants.HouseBase.STATUS_RENT) {
//						logger.info("推荐房源已下架或已出租️，sellId = {}， roomId = {}", hr.getSellId(), roomId);
//						houseRecommendRepository.setIsDeleteById(hr.getId(), Constants.Common.STATE_IS_DELETE_YES);
//						delCount += 1;
//					}
//				} else {
//					RoomBase rb = roomBaseRepository.findBySellIdAndRoomId(hr.getSellId(), hr.getRoomId());
//					if (rb == null || rb.getStatus() == Constants.HouseBase.STATUS_RENT) {
//						logger.info("推荐房源已下架或已出租️，sellId = {}， roomId = {}", hr.getSellId(), roomId);
//						houseRecommendRepository.setIsDeleteById(hr.getId(), Constants.Common.STATE_IS_DELETE_YES);
//						delCount += 1;
//					}
//				}
//				
//
//			}
//			if (delCount > 0) {
//				try {
//					redisCacheManager.delete("hzf_platform-city.house.recommend");
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error("删除推荐房源缓存失败。", e);
//				}
//			}
//		}
//		long endTime = System.currentTimeMillis();
//		logger.info("推荐房源清理结束，共{}条，本次任务耗时：{}ms", delCount, endTime - startTime);
//	}

}
