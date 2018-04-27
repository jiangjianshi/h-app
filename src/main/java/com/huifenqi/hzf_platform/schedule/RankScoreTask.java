package com.huifenqi.hzf_platform.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.AgencyDefaultScore;
import com.huifenqi.hzf_platform.context.entity.house.HouseApproveRecord;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseRankScore;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyDefaultScoreRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseApproveRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRankScoreRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.utils.DateUtil;

@Service
public class RankScoreTask {

	private static String START_TIME_KEY = "rank_score_start_time_key";
	// private static String KEY_SELL_ID_PREFIX = "RoomSellId:";
	// private static final long expireTime = 1800 * 1000;// 半小时
	private static Logger logger = LoggerFactory.getLogger(RankScoreTask.class);

	@Resource
	private HouseBaseRepository houseBaseRepository;
	@Resource
	private HouseDetailRepository houseDetailRepository;
	@Resource
	private HouseApproveRecordRepository houseApproveRecordRepository;
	@Resource
	private AgencyDefaultScoreRepository agencyDefaultScoreRepository;
	@Resource
	private HouseRankScoreRepository houseRankScoreRepository;
	@Resource
	private HousePictureRepository housePictureRepository;
	@Resource
	private RoomBaseRepository roomBaseRepository;
	@Resource
	private RedisCacheManager redisCacheManager;

	@Value("${hzf.hzfplatform.schedule}")
	private boolean scheduleStatus;

	@Scheduled(cron = "${hfq.rank.score.task}") // 每2分钟执行一次
	public void calculateRankScore() {
		 if (!scheduleStatus) {
			 logger.info("定时任务未开启，不能执行计算rank分定时任务");
			 return;
		 }

		Date startTime = new Date(); // 任务开始执行时间
		long stTime = startTime.getTime();
		logger.info("计算rank分任务开始，本次执行开始时间为：{}", DateUtil.formatDateTime(startTime));
		String lastRunTime = redisCacheManager.getValue(START_TIME_KEY);
		if (StringUtils.isNotEmpty(lastRunTime)) {
			startTime.setTime(Long.parseLong(lastRunTime));
			logger.info("上次执行时间为：{}", DateUtil.formatDateTime(startTime));
		} else {
			Date initTime = DateUtils.addDays(startTime, -1); // 如果无上次执行时间的记录，则按当前时间减去?天
			startTime.setTime(initTime.getTime());
		}
		// 记录下次查询数据开始时间
		redisCacheManager.putValue(START_TIME_KEY, String.valueOf(new Date().getTime()));
		int totalPage = 1;
		for (int i = 0; i < totalPage; i++) {
			Pageable pageable = new PageRequest(i, 100);// 分页查询
			Page<HouseDetail> pageList = houseDetailRepository.findByTimeRegion(pageable, startTime);
			totalPage = pageList.getTotalPages();
			if (totalPage > 0) {
				logger.info("开始计算第{}页房源Rank分，共{}页。", i + 1, totalPage);
				List<HouseDetail> detailList = pageList.getContent();
				for (HouseDetail detail : detailList) {
					try {
						HouseRankScore houseRank = calHouseRankScore(detail);
						saveOrUpdateRankScore(houseRank);
						logger.info("houseRank={}", houseRank.toString());
					} catch (Exception e) {
						logger.error("计算房源rank分失败，sellId={}", detail.getSellId(), e);
					}

				}
				logger.info("房源第{}页完毕，共{}页", i + 1, totalPage);
			} else {
				logger.info("Rank Score 没有待处理的房源。");
			}
		}

		// 计算房间评分
		totalPage = 1;
		for (int i = 0; i < totalPage; i++) {
			Pageable pageR = new PageRequest(i, 100);// 分页查询
			Page<RoomBase> roomPageList = roomBaseRepository.findByTimeRegion(pageR, startTime);
			totalPage = roomPageList.getTotalPages();
			if (totalPage > 0) {
				logger.info("开始计算第{}页房间Rank分，共{}页。", i + 1, totalPage);
				List<RoomBase> roomBaseList = roomPageList.getContent();
				for (RoomBase room : roomBaseList) {
					try {
						HouseRankScore roomRank = calRoomRankScore(room);
						saveOrUpdateRankScore(roomRank);
						logger.info("roomRank={}", roomRank.toString());
					} catch (Exception e) {
						logger.error("计算房间rank分失败，roomId={}", room.getId(), e);
					}

				}
				logger.info("房间第{}页完毕，共{}页", i + 1, totalPage);
			} else {
				logger.info("Rank Score 没有待处理的房间。");
			}
		}
		logger.info("计算Rank Score任务结束，耗时：{}ms", System.currentTimeMillis() - stTime);
	}

	/**
	 * 计算房源得分
	 * 
	 * @param detail
	 */
	private HouseRankScore calHouseRankScore(HouseDetail detail) {
		double agencyScore = 4, picScore = 0, infoScore = 0, subwayScore = 0;// 初始化分数变量：来源，图片，基本信息，地铁
		// 来源得分
		AgencyDefaultScore agency = agencyDefaultScoreRepository.findBySourceName(detail.getSource());
		if (agency != null) {
			agencyScore = agency.getScore();
		}
		// 图片得分
		picScore = calPicScore(detail.getSellId(), 0, agencyScore);

		// 信息完整度得分
		int price = houseBaseRepository.selectMonthRentBySellId(detail.getSellId());
		infoScore = calBaseInfoScore(price, detail.getArea(), detail);

		// 地铁得分
		if (detail.getSubwayDistance() > 0) {
			subwayScore = 10;
		}

		HouseRankScore partRank = new HouseRankScore();
		partRank.setBaseInfoScore(infoScore);
		partRank.setSubwayScore(subwayScore);
		partRank.setPicScore(picScore);
		partRank.setSourceScore(agencyScore);
		partRank.setRandomScore(0.0);
		partRank.setSellId(detail.getSellId());
		partRank.setRoomId(0L);
		addRandomScore(partRank);
		return partRank;
	}

	/**
	 * 计算房间得分
	 * 
	 * @param detail
	 */
	private HouseRankScore calRoomRankScore(RoomBase room) {

		HouseDetail detail = houseDetailRepository.findBySellId(room.getSellId());
		double agencyScore = 4, picScore = 0, infoScore = 0, subwayScore = 0;// 初始化分数变量：来源，图片，基本信息，地铁
		if (detail == null) {
			logger.error("为查询到房源详情");
		} else {
			// 来源得分
			AgencyDefaultScore agency = agencyDefaultScoreRepository.findBySourceName(detail.getSource());
			if (agency != null) {
				agencyScore = agency.getrScore();
			}
			// 图片得分
			picScore = calPicScore(room.getSellId(), room.getId(), agencyScore);

			// 信息完整度得分
			int price = room.getMonthRent();
			infoScore = calBaseInfoScore(price, room.getArea(), detail);

			// 地铁得分
			if (detail.getSubwayDistance() > 0) {
				subwayScore = 10;
			}

		}

		HouseRankScore partRank = new HouseRankScore();
		partRank.setBaseInfoScore(infoScore);
		partRank.setSubwayScore(subwayScore);
		partRank.setPicScore(picScore);
		partRank.setSourceScore(agencyScore);
		partRank.setRandomScore(0.0);
		partRank.setSellId(room.getSellId());
		partRank.setRoomId(room.getId());
		// 同房源多个房间时添加随机分
		addRandomScore(partRank);
		return partRank;
	}

	/**
	 * 计算图片得分
	 * 
	 * @return
	 */
	private double calPicScore(String sellId, long roomId, double agencyScore) {
		double picScore = 0;
		HouseApproveRecord approve = houseApproveRecordRepository.findBySellIdAndRoomId(sellId, roomId);
		if (approve != null) {
			picScore = approve.getImageScore();
		} else {// 如果没有审核，则计算默认分
			List<HousePicture> picList = housePictureRepository.findVaildBySellIdAndRoomId(sellId, roomId,
					Constants.Common.STATE_IS_DELETE_NO);
			Map<String, String> repeatMap = new HashMap<String, String>();
			for (HousePicture pic : picList) {// 图片去重
				if (!StringUtils.isEmpty(pic.getPicWebPath())) {
					repeatMap.put(pic.getPicWebPath(), pic.getPicWebPath());
				}
			}
			int picCnt = repeatMap.size();
			if (picCnt > 0 && picCnt < 5) {
				picScore = 4;
			} else if (picCnt >= 5 && picCnt < 8) {
				picScore = 7;
			} else if (picCnt >= 8) {
				picScore = 10;
			}
			// 图片总得分=来源分的60%加图片数量得分的40%
			if (picScore > 0) {
				picScore = agencyScore * 0.6 + picScore * 0.4;
			}
		}

		return picScore;
	}

	/**
	 * 计算信息完整度得分
	 * 
	 * @return
	 */
	private double calBaseInfoScore(int price, float area, HouseDetail detail) {
		double infoScore = 0;
		if (price > 0) {
			infoScore = 5;
		}
		if (detail.getLivingroomNum() > 0 || detail.getBedroomNum() > 0) {
			infoScore += 2;
		}
		if (area > 0) {
			infoScore += 2;
		}
		try {
			if (StringUtils.isNotEmpty(detail.getFlowNo()) && Integer.parseInt(detail.getFlowNo()) > 0) {
				infoScore += 1;
			}
		} catch (Exception e) {
			logger.error("数据转换异常，楼层信息格式错误。 flowNo={}", detail.getFlowNo(), e);
		}

		return infoScore;
	}

	/**
	 * 保存更新rank分
	 * 
	 * @param rankScore
	 */
	private void saveOrUpdateRankScore(HouseRankScore rankScore) {
		HouseRankScore oldRankScore = houseRankScoreRepository.findBySellIdAndRoomId(rankScore.getSellId(),
				rankScore.getRoomId());
		if (oldRankScore == null) {
			rankScore.setCreationDate(new Date());
			rankScore.setLastChangeDate(new Date());
			houseRankScoreRepository.save(rankScore);
		} else {
			oldRankScore.setSourceScore(rankScore.getSourceScore());
			oldRankScore.setPicScore(rankScore.getPicScore());
			oldRankScore.setSubwayScore(rankScore.getSubwayScore());
			oldRankScore.setBaseInfoScore(rankScore.getBaseInfoScore());
			oldRankScore.setRandomScore(rankScore.getRandomScore());
			oldRankScore.setLastChangeDate(new Date());
			houseRankScoreRepository.save(oldRankScore);
		}
	}

	/**
	 * 添加随机分数
	 * 
	 * @param rankScore
	 */
	private void addRandomScore(HouseRankScore rankScore) {
		// String cacheSellId = null;
		// try {
		// cacheSellId = redisCacheManager.getValue(KEY_SELL_ID_PREFIX +
		// rankScore.getSellId());
		// } catch (Exception e) {
		// logger.error("查询Redis失败。", e);
		// }
		// if (StringUtils.isNotEmpty(cacheSellId)) {
		// double randomNum = (new Random().nextInt(9) + 1) / 100.0;
		// rankScore.setRandomScore(randomNum);
		// } else {
		// int count =
		// roomBaseRepository.sellCountBySellId(rankScore.getSellId());
		// if (count > 1) {
		// // 分值0.01-0.09
		// double randomNum = (new Random().nextInt(9) + 1) / 100.0;
		// rankScore.setRandomScore(randomNum);
		// try {
		// redisCacheManager.putValue(KEY_SELL_ID_PREFIX +
		// rankScore.getSellId(), rankScore.getSellId(),
		// expireTime);
		// } catch (Exception e) {
		// logger.error("Redis存储失败。", e);
		// }
		// }
		// }

		double randomNum = (new Random().nextInt(99) + 1) / 1000.0;
		rankScore.setRandomScore(randomNum);
	}

}
