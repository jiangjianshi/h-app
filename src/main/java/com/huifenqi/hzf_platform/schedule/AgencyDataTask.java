package com.huifenqi.hzf_platform.schedule;

import java.util.ArrayList;
import java.util.Date;
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
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.dao.MapHouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;

@Service
public class AgencyDataTask {

	@Resource
	private TaskConfiguration taskConfiguration;
	
	@Autowired
	private HouseDetailRepository houseDetailRepository;
	
	@Autowired
	private AgencyManageRepository agencyManageRepository;
	
	@Autowired
	private MapHouseDao mapHouseDao;

	private static Logger logger = LoggerFactory.getLogger(AgencyDataTask.class);

	/**
	 * @Title: manageAgencyData
	 * @Description: 删除中介公司数据中没有房源的数据
	 * @return void
	 * @author 叶东明
	 * @dateTime 2017年9月14日 下午5:51:10
	 */
	@Scheduled(cron = "${hfq.manage.agency.task}") // 每天凌晨5点执行一次
	public void manageAgencyData() throws Exception {
		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行中介公司数据处理定时任务");
			return;
		}
		long startTime = System.currentTimeMillis();
		List<Agency> agencyList = agencyManageRepository.findAllAgency();
		
		// =====================新增品牌公寓=====================
		// 查询品牌公寓所有数据
		List<String> checkList = new ArrayList<String>();
		List<String> checkCompanyIdList = new ArrayList<String>();
		for (Agency agency : agencyList) {
			String agencyId = agency.getCompanyId();
			long cityId = agency.getCityId();
			if (!checkList.contains(agencyId + "-" + cityId)) {
				checkList.add(agencyId + "-" + cityId);
			}
			if (!checkCompanyIdList.contains(agencyId + "-00")) {
				checkCompanyIdList.add(agencyId + "-00");
			}
		}
		// 查询所有包含房间的品牌公寓列表
		List<Long> cityIdList0 = new ArrayList<Long>();
		for (Agency agency : agencyList) {
			long cityId = agency.getCityId();
			if (!cityIdList0.contains(cityId)) {
				List<MapHouseInfo> newList = mapHouseDao.getCompanyHouseCount(cityId);
				if (!CollectionUtils.isEmpty(newList)) {
					for (MapHouseInfo mapHouseInfo : newList) {
						String agencyId = mapHouseInfo.getName();
						String checkString = agencyId + "-" + cityId;
						String checkCompanyIdString = agencyId + "-00";
						if (checkCompanyIdList.contains(checkCompanyIdString)) {// 保证当前公寓属于品牌公寓
							if (!checkList.contains(checkString)) {// 然后新增当前城市下的品牌公寓
								List<Agency> agencyListTemp = agencyManageRepository.getAgencyByCompanyId(agencyId);
								if (!CollectionUtils.isEmpty(agencyListTemp)) {
									Agency agencyInfo = new Agency();
									Date createTime = new Date();
									agencyInfo.setCityId(cityId);
									agencyInfo.setCompanyId(agencyId);
									agencyInfo.setCompanyName(agencyListTemp.get(0).getCompanyName());
									agencyInfo.setPicRootPath(agencyListTemp.get(0).getPicRootPath());
									agencyInfo.setPicWebPath(agencyListTemp.get(0).getPicWebPath());
									agencyInfo.setCreateTime(createTime);
									agencyInfo.setUpdateTime(createTime);
									agencyManageRepository.save(agencyInfo);
								}
							}
						}
					}
				}
				cityIdList0.add(cityId);
			}
		}
		
		/*// 查询所有包含房源的品牌公寓列表
		List<QueryDetailVo> houseIsNotEmpty = houseDetailRepository.getHouseIsNotEmptyList();
		if (!CollectionUtils.isEmpty(houseIsNotEmpty)) {
			for (QueryDetailVo queryDetailVo : houseIsNotEmpty) {
				String agencyId0 = queryDetailVo.getCompanyId();
				long cityId0 = queryDetailVo.getCityId();
				String checkString = agencyId0 + "-" + cityId0;
				List<Agency> agencyInfoList = agencyManageRepository.getAgencyByCompanyId(agencyId0);
				if (!checkList.contains(checkString)) {
					if (!CollectionUtils.isEmpty(agencyInfoList)) {
						Agency agency = new Agency();
						Date createTime = new Date();
						agency.setCityId(cityId0);
						agency.setCompanyId(agencyInfoList.get(0).getCompanyId());
						agency.setCompanyName(agencyInfoList.get(0).getCompanyName());
						agency.setPicRootPath(agencyInfoList.get(0).getPicRootPath());
						agency.setPicWebPath(agencyInfoList.get(0).getPicWebPath());
						agency.setCreateTime(createTime);
						agency.setUpdateTime(createTime);
						agencyManageRepository.save(agency);
					}
				}
			}
		}*/
		
		// =====================删除品牌公寓=====================
		for (Agency agency : agencyList) {
			long roomNumber = agency.getRoomCount();
			String agencyId = agency.getCompanyId();
			long cityId = agency.getCityId();
			if (roomNumber == 0) {
				if (!"-1".equals(agencyId) && cityId != -1) {// 排除固定选项
					agencyManageRepository.deleteByAgencyIdAndCityId(agencyId, cityId);
				}
			}
		}
		
		/*if (!CollectionUtils.isEmpty(agencyList)) {
			for (Agency agency : agencyList) {
				String agencyId = agency.getCompanyId();
				long cityId = agency.getCityId();
				QueryDetailVo houseIsEmpty = houseDetailRepository.getHouseIsEmptyList(agencyId, cityId);
				if (houseIsEmpty != null) {
					String companyId = houseIsEmpty.getCompanyId();
					if (houseIsEmpty != null) {
						long houseNum = houseIsEmpty.getHouseNum();
						if (houseNum == 0) {
							if (!"-1".equals(companyId) && cityId != -1) {// 排除固定选项
								agencyManageRepository.deleteByAgencyIdAndCityId(agencyId, cityId);
							}
						}
					}
				}
			}
		}*/
		
		long endTime = System.currentTimeMillis();
		logger.info("中介公司数据处理结束，本次任务耗时：{}ms", endTime - startTime);
	}

}
