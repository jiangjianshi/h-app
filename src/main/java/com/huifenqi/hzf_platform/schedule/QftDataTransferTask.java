package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.ScheduleConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.CoordinateDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.context.entity.house.HouseDicitem;
import com.huifenqi.hzf_platform.context.entity.house.HouseTypeMapping;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailQftRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDicitemRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseTypeMappingRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.service.BaiduService;
import com.huifenqi.hzf_platform.utils.StringUtil;

@Service
public class QftDataTransferTask {

	@Resource
	private BdHouseDetailQftRepository bdHouseDetailQftRepository;

	@Resource
	private HouseDao houseDao;

	@Resource
	private QftDataTransferService qftDataTransferService;

	@Resource
	private HouseRecommendRepository houseRecommendRepository;

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private RoomBaseRepository roomBaseRepository;

	@Resource
	private HouseTypeMappingRepository houseTypeMappingRepository;

	@Resource
	private HouseDicitemRepository houseDicitemRepository;

	@Resource
	private BaiduService baiduService;

	@Resource
	private TaskConfiguration taskConfiguration;
	private static Logger logger = LoggerFactory.getLogger(QftDataTransferTask.class);

	@Scheduled(cron = "${hfq.qft.transfer.house.task}") // 每十五分钟执行一次
	public void transferData() {

		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行全房通扩展房源处理定时任务");
			return;
		}

		long startTime = System.currentTimeMillis();
		List<BdHouseDetailQft> qftHouseDetailList = bdHouseDetailQftRepository.findFirst1000ByTransferFlagOrderByUpdateTimeDesc(0);
		if (CollectionUtils.isEmpty(qftHouseDetailList)) {
			long endTime = System.currentTimeMillis();
			logger.info("qft没有待同步的房源,任务结束。本次耗时：{}ms", endTime - startTime);
			return;
		}

		HouseTypeMapping typeMapping = null;
		HouseDicitem houseDicitem = null;
		for (BdHouseDetailQft hd : qftHouseDetailList) {
			// 判断是否有户型的对应关系

			if (StringUtil.isNotEmpty(hd.getLiveBedTotile())) {
				typeMapping = houseTypeMappingRepository.findByHouseTypeDesc(hd.getLiveBedTotile());
				if (typeMapping == null) {
					logger.info("未查到户型对应关系:{}", hd.getLiveBedTotile());
					continue;
				} else {
					hd.setBedRoomNum(typeMapping.getBedroomNums());
					hd.setLivingRoomNum(typeMapping.getLivingroomNums());
					hd.setToiletNum(typeMapping.getToiletNums());
				}
			}

			if (hd.getRentType() == ScheduleConstants.QftUtil.RENT_TYPE_SHARE) {// 分租查询房间类型
				if (!"0".equals(hd.getBedRoomType())) {
					// 根据房间类型名称查看编码
					houseDicitem = houseDicitemRepository.findHouseDicitemByDicName(hd.getBedRoomType());
					if (houseDicitem == null) {
						logger.info("未查到房间类型对应数据:{}", hd.getBedRoomType());
						continue;
					} else {
						hd.setBedRoomType(houseDicitem.getDicValue());

					}
				}
			}

			// 判断是否有坐标
			if ("0".equals(hd.getxCode()) || "0".equals(hd.getyCode())) {
				CoordinateDto coord = baiduService.getCoordinateByAddressQft(hd);
				if (coord == null) {
					qftDataTransferService.updateHouseTrandferFlag(hd.getOutHouseId(), hd.getAppId(),
							ScheduleConstants.QftUtil.IS_FLAG_FAIL);
					continue;
				} else {
					if (coord.getPrecise() != 1) {
						qftDataTransferService.updateHouseXY(hd.getOutHouseId(), hd.getAppId(),
								ScheduleConstants.QftUtil.IS_FLAG_FAIL, coord.getLng(), coord.getLat(),
								coord.getPrecise(), coord.getConfidence(), coord.getLevel()); // 将transferFlag=
																								// -1
																								// 失败
						continue;
					} else {
						logger.info("返回坐标为: {}", coord.toString());
						qftDataTransferService.updateHouseXY(hd.getOutHouseId(), hd.getAppId(),
								ScheduleConstants.QftUtil.IS_FLAG_SUCCESS, coord.getLng(), coord.getLat(),
								coord.getPrecise(), coord.getConfidence(), coord.getLevel()); // 将transferFlag=2
																								// 坐标查询成功
						hd.setxCode(coord.getLng());
						hd.setyCode(coord.getLat());
					}

				}
			}

			int rentType = hd.getRentType();// 枚举值 1.整套出租 2.单间出租
			if (rentType == 1) {
				try {
					try {
						logger.info("整租，发布房源");
						qftDataTransferService.publishHouse(hd);
					} catch (Exception e) {
						logger.error("发布房源失败");
						bdHouseDetailQftRepository.updateHouseTrandferFlag(hd.getOutHouseId(), hd.getAppId(),
								ScheduleConstants.QftUtil.IS_FLAG_UPDATE_FAIL); // 将transferFlag=-2
					}
				} catch (Exception e) {
					logger.error("发布房源失败", e);
				}

			} else if (rentType == 2) {
				try {
					try {
						logger.info("分租，发布房源和房间");
						qftDataTransferService.pulbicHouseAndRoom(hd);
					} catch (Exception e) {
						logger.error("发布房源失败");
						bdHouseDetailQftRepository.updateHouseTrandferFlag(hd.getOutHouseId(), hd.getAppId(),
								ScheduleConstants.QftUtil.IS_FLAG_UPDATE_FAIL); // 将transferFlag=-2
					}
				} catch (Exception e) {
					logger.error("发布房源失败", e);
				}

			}
		}
		long endTime = System.currentTimeMillis();
		logger.error("qft转化房源结束，本次任务耗时：{}ms", endTime - startTime);
	}

}
