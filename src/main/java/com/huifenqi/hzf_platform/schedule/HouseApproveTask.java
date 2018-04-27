package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;

@Service
public class HouseApproveTask {

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private HouseDetailRepository houseDetailRepository;

	@Resource
	private RoomBaseRepository roomBaseRepository;

	@Resource
	private HouseApproveService houseApproveService;

	@Resource
	private TaskConfiguration taskConfiguration;

	private static Logger logger = LoggerFactory.getLogger(HouseApproveTask.class);

	@Scheduled(cron = "${hfq.filter.error.data.task}") // 每五分钟执行一次
	public void filterHouseErrorData() {

		if (taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行房源程序审核定时任务");
			return;
		}

		// 审核房源
		long startTime = System.currentTimeMillis();
		Pageable pageable = new PageRequest(0, 1000);// 分页查询
		Page<HouseDetail> pageList = houseDetailRepository.findFirstUnapprove(pageable);// 查询未审核，未被删除，待出租，已收集 允许solr查 允许审核
		List<HouseDetail> detailList = pageList.getContent();
		if (CollectionUtils.isNotEmpty(detailList)) {
			for (HouseDetail hd : detailList) {
				HouseBase hb = houseBaseRepository.findBySellId(hd.getSellId());
				if(hb != null){
					try {
							houseApproveService.checkHouseErrorData(hd, hb);
					} catch (Exception e) {
						logger.error("房源数据审核失败，sellId={}", hd.getSellId(), e);
					} 
				}
			}
		} else {
			logger.info("没有待审核的房源.");
		}

		// 审核房间
		Pageable pageRoom = new PageRequest(0, 1000);// 分页查询
		Page<RoomBase> roomPageList = roomBaseRepository.findFirstUnapprove(pageRoom);// 查询未审核，未被删除，待出租，已收集
		List<RoomBase> rbList = roomPageList.getContent();
		if (CollectionUtils.isNotEmpty(rbList)) {
			for (RoomBase rb : rbList) {
				// 判断分租房间是否有错误数据select * from t_house_detail a where a.f_approve_status = 0
				if(rb != null){
				try {
						houseApproveService.checkRoomErrorData(rb);
					} catch (Exception e) {
						logger.error("房间数据审核失败，sellId={}, roomId={}", rb.getSellId(), rb.getId(), e);
					}
				}
			}
		} else {
			logger.info("没有待审核的房间.");
		}
		logger.error("本次数据审核任务结束，耗时：{}ms", System.currentTimeMillis() - startTime);
	}

}
