package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.dto.response.map.MapHouseInfo;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.dao.MapHouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;

@Service
public class AgencyRoomCountTask {

	@Resource
	private TaskConfiguration taskConfiguration;
	
	@Autowired
	private MapHouseDao mapHouseDao;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private AgencyManageRepository agencyManageRepository;

	private static Logger logger = LoggerFactory.getLogger(AgencyRoomCountTask.class);

	/**
	 * @Title: agencyRoomCount
	 * @Description: 统计某城市的品牌公寓下的房间量
	 * @return void
	 * @author 叶东明
	 * @dateTime 2017年10月14日 下午5:14:20
	 */
	@Scheduled(cron = "${hfq.agency.room.count.task}") // 每天凌晨6点执行一次
	public void agencyRoomCount() throws Exception {
		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行房间量数据统计定时任务");
			return;
		}
		long startTime = System.currentTimeMillis();
		
		// 查询所有上线城市列表
		List<City> cityList = cityRepository.findOpenCityList();
		if (!CollectionUtils.isEmpty(cityList)) {
			for (City city : cityList) {
				long cityId = city.getCityId();
				// 遍历城市列表，统计某城市的品牌公寓下的房间量，插入到Agency品牌公寓表中
				List<MapHouseInfo> newList = mapHouseDao.getCompanyHouseCount(cityId);
				if (!CollectionUtils.isEmpty(newList)) {
					for (MapHouseInfo mapHouseInfo : newList) {
						String companyId = mapHouseInfo.getName();
						long number = mapHouseInfo.getNumber();
						agencyManageRepository.updateByAgencyIdAndCityId(companyId, cityId, number);
					}
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("中介公司数据处理结束，本次任务耗时：{}ms", endTime - startTime);
	}

}
