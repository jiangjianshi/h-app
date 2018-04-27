/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.MapHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.map.HouseMapSolrResult;
import com.huifenqi.hzf_platform.context.dto.response.map.MapHouseInfo;
import com.huifenqi.hzf_platform.context.dto.response.map.MapHouseQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.map.RoomMapSolrResult;
import com.huifenqi.hzf_platform.context.entity.location.Area;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.CommunityBase;
import com.huifenqi.hzf_platform.context.entity.location.District;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.map.MapHouseSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.house.solr.map.MapRoomSolrRepository;
import com.huifenqi.hzf_platform.dao.repository.location.AreaRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CommunityBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Repository
public class MapHouseDao {

	private static final Log logger = LogFactory.getLog(MapHouseDao.class);

	@Autowired
	private HouseBaseRepository houseBaseRepository;

	@Autowired
	private RoomBaseRepository roomBaseRepository;

	@Autowired
	private MapHouseSolrRepository houseSolrRepository;
	
	@Autowired
	private MapRoomSolrRepository roomSolrRepository;

	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CommunityBaseRepository communityBaseRepository;

	/**
	 * 条件筛选 地图找房
	 * 
	 * @return
	 * @throws Exception 
	 */
	public MapHouseQueryDto getHouseResultDto(MapHouseSearchDto houseSearchDto, List<String> agencyIdList) throws Exception {
		
		MapHouseQueryDto mapHouseQueryDto = new MapHouseQueryDto();
		int entireRent = houseSearchDto.getEntireRent();
		List<MapHouseInfo> groupList = new ArrayList<>();
		
		boolean flag = false;//是否展示
		City city = cityRepository.findCityById(houseSearchDto.getCityId());
		if(city != null){
			if(city.getIsmap()==Constants.MapHouse.CITY_MAP_OPEN){//该城市展示地图行政区、商圈下房源数量
				flag = true;
			}
		}
		
		//地图找房-安卓设置 默认范围     通勤找房不设置
		if(houseSearchDto.getChannel().equals(Constants.MapHouse.CHANNEL_AND) && houseSearchDto.getCommuteId() <= Constants.CommuteMapConfig.COMMUTE_ID_INIT){
			if(houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_DIS || houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_BIZ){
				houseSearchDto.setDistance(Constants.MapHouse.DISTANCE_MAX);
			}else{
				houseSearchDto.setDistance(Constants.MapHouse.DISTANCE_MIN);
			}
		}
		
		if (entireRent == Constants.HouseDetail.RENT_TYPE_ENTIRE) { // 整租
			//地图找房查询solr
			GroupResult<HouseMapSolrResult> houseFieldGroup  = houseSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);
			
			if (houseFieldGroup == null) {
				logger.error(LogUtils.getCommLog("地图找房搜索房源结果为空"));
				return null;
			}
			
			//获取整租分租结果
			List<GroupEntry<HouseMapSolrResult>> houseGroupList = houseFieldGroup.getGroupEntries().getContent();
			
			if(houseGroupList != null){ 
				//遍历solr结果
				for(GroupEntry<HouseMapSolrResult> houseMap : houseGroupList ){
					MapHouseInfo mapInfo = new MapHouseInfo();
					mapInfo.setName(houseMap.getGroupValue());
					mapInfo.setNumber(houseMap.getResult().getTotalElements());
					mapInfo.setPrice(houseMap.getResult().getContent().get(0).getPrice()/100);
					groupList.add(mapInfo);
				}
			}
			
		}if (entireRent == Constants.HouseDetail.RENT_TYPE_SHARE) { // 合租
			//地图找房查询solr
			GroupResult<RoomMapSolrResult> roomFieldGroup = roomSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);
			
			if (roomFieldGroup == null) {
				logger.error(LogUtils.getCommLog("地图找房搜索房源结果为空"));
				return null;
			}
			List<GroupEntry<RoomMapSolrResult>> roomGroupList = roomFieldGroup.getGroupEntries().getContent();
			if(roomGroupList != null){ 
				//遍历roomSolr结果
				for(GroupEntry<RoomMapSolrResult> roomMap : roomGroupList ){
					MapHouseInfo mapInfo = new MapHouseInfo();
					mapInfo.setName(roomMap.getGroupValue());
					mapInfo.setNumber(roomMap.getResult().getTotalElements());
					mapInfo.setPrice(roomMap.getResult().getContent().get(0).getPrice()/100);
					groupList.add(mapInfo);
				}
			}
	
		}if (entireRent == Constants.HouseDetail.RENT_TYPE_ALL) { // 全部
			//地图找房查询solr-整租		
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
			GroupResult<HouseMapSolrResult> houseFieldGroup  = houseSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);
			
			//地图找房查询solr-分租
			houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
			GroupResult<RoomMapSolrResult> roomFieldGroup = roomSolrRepository.getAllByMultiCondition(houseSearchDto, agencyIdList);
			
			if (houseFieldGroup == null && roomFieldGroup == null) {
				logger.error(LogUtils.getCommLog("地图找房搜索房源结果为空"));
				return null;
			}
			List<GroupEntry<HouseMapSolrResult>> houseGroupList = houseFieldGroup.getGroupEntries().getContent();
			if(houseGroupList != null){ 
				//遍历solr结果
				for(GroupEntry<HouseMapSolrResult> houseMap : houseGroupList ){
					MapHouseInfo mapInfo = new MapHouseInfo();
					mapInfo.setName(houseMap.getGroupValue());
					mapInfo.setNumber(houseMap.getResult().getTotalElements());
					mapInfo.setPrice(houseMap.getResult().getContent().get(0).getPrice()/100);
					groupList.add(mapInfo);
				}
			}
			
			//分租
			List<GroupEntry<RoomMapSolrResult>> roomGroupList = roomFieldGroup.getGroupEntries().getContent();
			if(roomGroupList != null){
				//遍历solr结果
				for(GroupEntry<RoomMapSolrResult> roomMap : roomGroupList ){
					MapHouseInfo mapInfo = new MapHouseInfo();
					mapInfo.setName(roomMap.getGroupValue());
					mapInfo.setNumber(roomMap.getResult().getTotalElements());
					mapInfo.setPrice(roomMap.getResult().getContent().get(0).getPrice()/100);
					groupList.add(mapInfo);
				}
			}
		}
		
		List<MapHouseInfo> mapList = new ArrayList<MapHouseInfo>();
		
		if(houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_DIS){//行政区
			List<District> districtList = districtRepository.findDistrictsByCityId(houseSearchDto.getCityId());
			if(!groupList.isEmpty()){
				for(MapHouseInfo group :groupList){
					for(District district : districtList){
						if(group.getName().equals(String.valueOf(district.getDistrictId()))){
							MapHouseInfo info = new MapHouseInfo();
							info.setName(district.getName());
							if(flag){//是否展示行政区下房源数量
								info.setNumber(group.getNumber());
							}
							info.setLevel(houseSearchDto.getLevel());
							info.setCenter(district.getCenter());
							mapList.add(info);
						}
					}
					
				}
			}
		}
		
		if(houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_BIZ){//商圈
			//List<Area> areaList = areaRepository.findAreasByDistrictId(houseSearchDto.getDistrictId());
			List<Area> areaList = areaRepository.findAreasByCityId(houseSearchDto.getCityId());
			if(!groupList.isEmpty()){
				for(MapHouseInfo group :groupList){
					for(Area area : areaList){
						if(group.getName().equals(String.valueOf(area.getAreaId()))){
							MapHouseInfo info = new MapHouseInfo();
							info.setName(area.getName());
							if(flag){//是否展示商圈下房源数量
								info.setNumber(group.getNumber());
							}
							info.setLevel(houseSearchDto.getLevel());
							info.setCenter(area.getCenter());
							mapList.add(info);
						}
					}
					
				}
			}
		}
		
		if(houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_COMMUNITY){//小区
			List<CommunityBase> communityList = communityBaseRepository.findCommunityByCityId(houseSearchDto.getCityId());
			if(!groupList.isEmpty()){
				for(MapHouseInfo group :groupList){
					for(CommunityBase communityBase : communityList){
						if(group.getName().equals(String.valueOf(communityBase.getCommunityName()))){
							MapHouseInfo info = new MapHouseInfo();
							info.setName(communityBase.getCommunityName().trim());
							info.setNumber(group.getNumber());
							info.setLevel(houseSearchDto.getLevel());
							info.setCenter(communityBase.getCenter());
							info.setPrice(group.getPrice());
							mapList.add(info);
						}
					}
					
				}
			}
		}
		
		if(houseSearchDto.getLevel() == Constants.MapHouse.LEVEL_COMPANY){//品牌公寓
			if(!groupList.isEmpty()){
				for(MapHouseInfo group :groupList){
							MapHouseInfo info = new MapHouseInfo();
							info.setName(group.getName());
							info.setNumber(group.getNumber());
							info.setLevel(houseSearchDto.getLevel());
							mapList.add(info);
						}
				}
		}
		
		if(entireRent == Constants.HouseDetail.RENT_TYPE_ALL){//全部的时候需要合并结果
			//合并
			List<MapHouseInfo> newList = new ArrayList<MapHouseInfo>();    
		    for (MapHouseInfo mapHouseInfo : mapList) {    
		        boolean state = false;    
		        for (MapHouseInfo newMapHouseInfo : newList) {    
		            if(newMapHouseInfo.getName().equals(mapHouseInfo.getName())){    
		                long number = (long)newMapHouseInfo.getNumber();    
		                number += mapHouseInfo.getNumber();    
		                newMapHouseInfo.setNumber(number);    
		                state = true;  
		              //房间和房源取最小价格
			            //int result = newMapHouseInfo.getPrice().compareTo(mapHouseInfo.getPrice());
			            if(newMapHouseInfo.getPrice() >= mapHouseInfo.getPrice()){// result大于0，则t1>t2； result等于0，则t1=t2；
			            	newMapHouseInfo.setPrice(mapHouseInfo.getPrice());
			            }else{ //result小于0，则t1<t2
			            	newMapHouseInfo.setPrice(newMapHouseInfo.getPrice());
			            } 
		            }  
 
		        }    
		        if(!state){    
		        	newList.add(mapHouseInfo);    
		        }    
		    } 
		    mapHouseQueryDto.setMapList(newList);
			return mapHouseQueryDto;
		}
		
		
		mapHouseQueryDto.setMapList(mapList);
		return mapHouseQueryDto;
	}

	/**
	 * 品牌公寓房源分组
	 * 
	 * @return
	 * @throws Exception 
	 */	
	public List<MapHouseInfo> getCompanyHouseCount(long cityId) throws Exception {
		
		List<MapHouseInfo> groupList = new ArrayList<>();
		MapHouseSearchDto houseSearchDto = new MapHouseSearchDto();
		
		if(cityId <= 0){
			return null;
		}
		
		//设置查询默认参数
		houseSearchDto.setCityId(cityId);
		houseSearchDto.setLevel(Constants.MapHouse.LEVEL_COMPANY);
		
		//地图找房查询solr-整租		
		houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
		GroupResult<HouseMapSolrResult> houseFieldGroup  = houseSolrRepository.getAllByMultiCondition(houseSearchDto, null);
		
		//地图找房查询solr-分租
		houseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
		GroupResult<RoomMapSolrResult> roomFieldGroup = roomSolrRepository.getAllByMultiCondition(houseSearchDto, null);
		
		if (houseFieldGroup == null && roomFieldGroup == null) {
			logger.error(LogUtils.getCommLog("品牌公寓房源分组结果为空"));
			return null;
		}
		List<GroupEntry<HouseMapSolrResult>> houseGroupList = houseFieldGroup.getGroupEntries().getContent();
		if(houseGroupList != null){ 
			//遍历solr结果
			for(GroupEntry<HouseMapSolrResult> houseMap : houseGroupList ){
				MapHouseInfo mapInfo = new MapHouseInfo();
				mapInfo.setName(houseMap.getGroupValue());
				mapInfo.setNumber(houseMap.getResult().getTotalElements());
				groupList.add(mapInfo);
			}
		}
		
		//分租
		List<GroupEntry<RoomMapSolrResult>> roomGroupList = roomFieldGroup.getGroupEntries().getContent();
		if(roomGroupList != null){
			//遍历solr结果
			for(GroupEntry<RoomMapSolrResult> roomMap : roomGroupList ){
				MapHouseInfo mapInfo = new MapHouseInfo();
				mapInfo.setName(roomMap.getGroupValue());
				mapInfo.setNumber(roomMap.getResult().getTotalElements());
				groupList.add(mapInfo);
			}
		}
		
		
		//分租整租需要合并结果
		List<MapHouseInfo> newList = new ArrayList<MapHouseInfo>();    
	    for (MapHouseInfo mapHouseInfo : groupList) {    
	        boolean state = false;    
	        for (MapHouseInfo newMapHouseInfo : newList) {    
	            if(newMapHouseInfo.getName().equals(mapHouseInfo.getName())){    
	                long number = (long)newMapHouseInfo.getNumber();    
	                number += mapHouseInfo.getNumber();    
	                newMapHouseInfo.setNumber(number);    
	                state = true;    
	            }    
	        }    
	        if(!state){    
	        	newList.add(mapHouseInfo);    
	        }    
	    } 
		return newList;
	}
	

	/**
	 * 查询房源是否存在
	 * 
	 * @param sellId
	 * @return
	 */
	public boolean isHouseExist(String sellId) {

		int count = houseBaseRepository.countBySellId(sellId);

		return count > 0;
	}

	/**
	 * 查询房间是否存在
	 * 
	 * @param roomId
	 * @return
	 */
	public boolean isRoomExist(long roomId) {

		int count = roomBaseRepository.countByRoomId(roomId);

		return count > 0;
	}
	
}
