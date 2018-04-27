/** 
 * Project Name: hzf_platform_project 
 * File Name: LocationRequestHandler.java 
 * Package Name: com.huifenqi.hzf_platform.handler 
 * Date: 2016年4月27日下午7:39:11 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.QueryConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.CoordinateDto;
import com.huifenqi.hzf_platform.context.dto.response.location.AllCityBizCircleDto;
import com.huifenqi.hzf_platform.context.dto.response.location.AllCitySubwayDto;
import com.huifenqi.hzf_platform.context.dto.response.location.AreaInfo;
import com.huifenqi.hzf_platform.context.dto.response.location.BusinessCircle;
import com.huifenqi.hzf_platform.context.dto.response.location.BussinessCircleQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.location.CircleAreaQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.location.CityBizCircleDto;
import com.huifenqi.hzf_platform.context.dto.response.location.CityInfo;
import com.huifenqi.hzf_platform.context.dto.response.location.CityQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.location.CitySubwayDto;
import com.huifenqi.hzf_platform.context.dto.response.location.SubwayLineDto;
import com.huifenqi.hzf_platform.context.dto.response.location.SubwayQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.location.SubwayStationDto;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;
import com.huifenqi.hzf_platform.context.entity.location.Area;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.CityInfoVersion;
import com.huifenqi.hzf_platform.context.entity.location.CommunityBase;
import com.huifenqi.hzf_platform.context.entity.location.District;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.context.enums.CityInfoTypeEnum;
import com.huifenqi.hzf_platform.context.enums.UpdateFlagEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.repository.location.AreaRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityInfoVersionRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CityRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CommunityBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository;
import com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository;
import com.huifenqi.hzf_platform.service.BaiduService;
import com.huifenqi.hzf_platform.service.GaoDeService;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.ResponseUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: LocationRequestHandler date: 2016年4月27日 下午7:39:11 Description:
 * 位置相关业务逻辑
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class LocationRequestHandler {

	private static Log logger = LogFactory.getLog(LocationRequestHandler.class);

	@Autowired
	private SubwayRepository subwayRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CityInfoVersionRepository cityInfoVersionRepository;

	@Autowired
	private QueryConfiguration queryConfiguration;

	@Autowired
	private BaiduService baiduService;

	@Autowired
	private GaoDeService gaodeService;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@Autowired
	private CommunityBaseRepository communityBaseRepository;
	
	private static String CITY_SUB_BIZ = "city_sub_biz";

	/**
	 * 地铁坐标初始化
	 * 
	 * @return
	 */
	@Transactional
	public Responses initSubway(long cityId, long version) {
		Responses result = new Responses();
		logger.info("初始化城市信息开始");
		// 获取所有地铁线路
		List<Subway> subwayList = subwayRepository.findSubWaysByCityId(cityId);
		List<Subway> subInit = new ArrayList<>();
		try {
			// 删除redis缓存
			redisCacheManager.delete("hzf_platform-city.subway.info");

			if (CollectionUtils.isNotEmpty(subwayList)) {
				for (Subway subway : subwayList) {
					List<String> keywords = new ArrayList<>();
					keywords.add("地铁站");
					List<String> tags = new ArrayList<>();
					// tags.add("交通设施/地铁站");
					Map<String, String> sort = new HashMap<>();
					sort.put("industry_type", "life");
					sort.put("sort_name", "distance");
					sort.put("sort_rule", "1");
					JsonArray jsonArray = null;
					JsonObject jsonObject = null;

					if (StringUtil.isEmpty(subway.getLatitude()) || StringUtil.isEmpty(subway.getLongitude())) {// 坐标为空
						// 获取城市列表
						City city = cityRepository.findCityById(cityId);
						if (city != null) {
							// 从百度获取地址站坐标
							jsonObject = baiduService.getCoordinateByAddress(
									subway.getStation() + " 地铁" + subway.getSubwayLine(), city.getName());
						}

						if (jsonObject == null) {
							logger.info("该地铁站点出未查询到结果--" + subway.getStation());
							continue;
						}
						// 查询该站的地铁线路
						String lat = jsonObject.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
						String lng = jsonObject.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();

						// 更新地铁坐标信息
						logger.info(LogUtils.getCommLog(String.format("地铁(%s)的(%s)数据有问题,百度最新经度(%s)最新纬度(%s)",
								subway.getSubwayLine(), subway.getStation(), lat, lng)));
						subInit.add(subway);
						// 更新地铁线路坐标地址
						subwayRepository.setLatAndLngById(lat, lng, subway.getId());
					} else {// 坐标不为空

						jsonArray = baiduService.radiusPlaceSearch(keywords, tags, "3000", subway.getLatitude(),
								subway.getLongitude(), "1", sort);
						// 没有查到数据
						if (jsonArray == null || jsonArray.size() == 0) {
							continue;
						}
						JsonObject subwayDetail = jsonArray.get(0).getAsJsonObject();

						// 查询该站的地铁线路
						String lat = subwayDetail.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
						String lng = subwayDetail.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();
						String station = subwayDetail.getAsJsonPrimitive("name").getAsString();
						String uid = subwayDetail.getAsJsonPrimitive("uid").getAsString();
						if (!station.equals(subway.getStation())) {
							logger.info("数据有问题城市ID:" + subway.getCityId() + " " + subway.getSubwayLine()
									+ " 站点名称:" + subway.getStation()
									+ " 百度站点:" + station);
						} else {
							// 判断是否最新地铁坐标
							if (!lat.equals(subway.getLatitude()) || !lng.equals(subway.getLongitude())) {
								logger.info(
										LogUtils.getCommLog(String.format("地铁(%s)的(%s)数据有问题,百度站点(%s)最新纬度(%s)最新经度(%s)",
												subway.getSubwayLine(), subway.getStation(), station, lat, lng)));
								
								logger.info(LogUtils.getCommLog(String.format("地铁(%s)的(%s)数据有问题,百度站点(%s)最新uid:(%s)",
										subway.getSubwayLine(), subway.getStation(), station, uid)));
								subInit.add(subway);
								subway.setLongitude(lng);
								subway.setLatitude(lat);
							}
						}
						subway.setUid(uid);
						subwayRepository.save(subway);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setBody(subInit);
		ResponseUtils.fillRow(result.getMeta(), subInit.size(), subInit.size());
		logger.info("初始化城市信息结束");
		return result;
	}

	/**
	 * 商圈信息初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void initZone(String cityName, String subdistrict) throws Exception {
		logger.info("初始化商圈开始:" + cityName);

		JsonArray jsonArray = gaodeService.getCoordinateByCity(cityName, subdistrict);
		City city = null;
		District dt = null;
		String districtName = null;
		city = cityRepository.findCityByName(cityName);
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject district = jsonArray.get(i).getAsJsonObject();
			JsonArray jsonArrayZone = jsonArray.get(i).getAsJsonObject().getAsJsonArray("districts");
			districtName = district.getAsJsonPrimitive("name").getAsString();

			dt = districtRepository.findDistrictByNameAndCityId(districtName, city.getCityId());
			if (dt == null) {
				logger.info("未找到该行政区" + districtName);
				// throw new Exception("商圈初始化失败" + districtName);
			}

			if (jsonArrayZone != null) {
				for (int j = 0; j < jsonArrayZone.size(); j++) {
					JsonObject zone = jsonArrayZone.get(j).getAsJsonObject();
					String zoneName = zone.getAsJsonPrimitive("name").getAsString();
					String center = zone.getAsJsonPrimitive("center").getAsString();
					Area initArea = new Area();
					initArea.setCityId(city.getCityId());
					initArea.setDistrictId(dt.getDistrictId());
					initArea.setName(zoneName);
					initArea.setCenter(center);
					initArea.setSort(j);
					initArea.setStatus(1);
					areaRepository.save(initArea);
				}
			}
		}
		logger.info("初始化商圈结束:" + cityName);

	}

	/**
	 * 小区坐标初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void initCommunityCenter(int cityId) throws Exception {
		logger.info("初始小区坐标开始");

		List<CommunityBase> comunityList = communityBaseRepository.findCommunityByCityIdAndFlag(cityId,
				Constants.CommunityStatus.OPEN_NOT_FLAG);
		List<CommunityBase> newComunityList = new ArrayList<>();
		for (CommunityBase c : comunityList) {
			CoordinateDto coord = baiduService.getCoordinateByAddress(c.getCityName(), c.getDistrictName(),
					c.getCommunityName());
			if (coord != null) {
				c.setCenter(coord.getLng() + "," + coord.getLat());
				c.setPrecise(coord.getPrecise());
				c.setConfidence(coord.getConfidence());
				c.setLevel(coord.getLevel());
				c.setFlag(Constants.CommunityStatus.OPEN_YES_FLAG);
				newComunityList.add(c);
			}
			logger.info("待收集小区总数:" + comunityList.size() + "已收集:" + newComunityList.size());
		}
		communityBaseRepository.save(newComunityList);

		logger.info("初始小区坐标结束");

	}

	/**
	 * 小区坐标初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Responses initCommunityCenterTest(String cityName, String disName, String comName) throws Exception {
		logger.info("初始小区坐标开始");
		Responses result = new Responses();
		CoordinateDto coord = baiduService.getCoordinateByAddress(cityName, disName, comName);
		result.setBody(coord.toString());
		logger.info("初始小区坐标结束");
		return result;

	}

	/**
	 * 商圈信息初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public void initDisCenter(String cityName, String subdistrict) throws Exception {
		logger.info("初始化行政区坐标开始:" + cityName);

		// 从高德获取全国省市区
		JsonArray jsonArray = gaodeService.getCoordinateByCity(cityName, subdistrict);
		City city = null;
		District dt = null;
		String name = null;

		// 获取城市信息
		city = cityRepository.findCityByName(cityName);

		if (subdistrict.equals("1")) {// 直辖市 北京
			for (int i = 0; i < jsonArray.size(); i++) {
				// 解析Json
				JsonObject district = jsonArray.get(i).getAsJsonObject();
				if (district != null) {
					// 获取行政区名称
					name = district.getAsJsonPrimitive("name").getAsString();
					dt = districtRepository.findDistrictByNameAndCityId(name, city.getCityId());
					if (dt != null) {
						logger.info("未找到该行政区" + name);
						String center = district.getAsJsonPrimitive("center").getAsString();
						if (dt.getName().equals(name)) {
							dt.setCenter(center);
							districtRepository.save(dt);
						}
					}

				}
			}
		} else {
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonArray jsonArrayZone = jsonArray.get(i).getAsJsonObject().getAsJsonArray("districts");
				if (jsonArrayZone != null) {
					for (int j = 0; j < jsonArrayZone.size(); j++) {

						JsonObject zone = jsonArrayZone.get(j).getAsJsonObject();
						name = zone.getAsJsonPrimitive("name").getAsString();
						logger.info("找到该行政区" + name);
						dt = districtRepository.findDistrictByNameAndCityId(name, city.getCityId());
						if (dt != null) {
							// throw new Exception("商圈初始化失败" + districtName);
							String center = zone.getAsJsonPrimitive("center").getAsString();
							if (dt.getName().equals(name)) {
								dt.setCenter(center);
								districtRepository.save(dt);
							}
						}

					}
				}
			}
		}
		logger.info("初始化行政区坐标结束:" + cityName);
	}

	/**
	 * 查询城市的所有地铁信息
	 * 
	 * @return
	 */
	public Responses reqCitySubways(long cityId, long version) {

		Responses result = new Responses();

		long cVersion = 0;
		List<Subway> subways = subwayRepository.findSubWaysByCityId(cityId);
		if (CollectionUtils.isNotEmpty(subways)) {
			// 提取地铁线包含的站点
			Map<String, SubwayLineDto> subwayMap = new HashMap<>();
			SubwayQueryDto subwayQueryDto = new SubwayQueryDto();
			// 添加全部地铁线路
			SubwayLineDto subwayLine0 = new SubwayLineDto();
			subwayLine0.setLineId(0);
			subwayLine0.setLineName("全部");
			subwayQueryDto.addSubway(subwayLine0);
			// 循环遍历地铁线路
			for (Subway subway : subways) {
				SubwayLineDto subwayLine = subwayMap.get(subway.getSubwayLine());
				if (subwayLine == null) {
					subwayLine = new SubwayLineDto();
					subwayLine.setLineId(subway.getSubwayLineId());
					subwayLine.setLineName(subway.getSubwayLine());
					subwayMap.put(subway.getSubwayLine(), subwayLine);
					subwayQueryDto.addSubway(subwayLine);

					// 地铁列表添加全部 2017年06月26日15:38:06 jjs
					SubwayStationDto subwayAll = new SubwayStationDto();
					subwayAll.setStationName("全部");
					subwayAll.setStationId(0);
					subwayLine.addSubwayStation(subwayAll);
				}
				SubwayStationDto subwayStationDto = new SubwayStationDto();
				subwayStationDto.setStationName(subway.getStation());
				subwayStationDto.setStationId(subway.getId());
				subwayLine.addSubwayStation(subwayStationDto);
			}
			subwayQueryDto.setVersion(cVersion);
			subwayQueryDto.setUpdateFag(Constants.CityInfo.NEED_UPDATE_FLAG);
			result.setBody(subwayQueryDto);
			ResponseUtils.fillRow(result.getMeta(), subways.size(), subways.size());
		}

		return result;
	}
	
	/**
	 * 获取全部开通城市的地铁
	 * @param cityId
	 * @param version
	 * @return
	 */
	public Responses reqAllCitysSubways(Integer version) {
		
		Responses result = new Responses();//创建返回对象
		AllCitySubwayDto allCitySubway = new AllCitySubwayDto();
		
		CityInfoVersion ver = cityInfoVersionRepository.findByInfoType(CityInfoTypeEnum.subway.getCode());
		if(version >= ver.getVersion()){
			allCitySubway.setVersion(ver.getVersion());
			allCitySubway.setUpdateFag(UpdateFlagEnum.NOT_UPDATE.getCode());
			result.setBody(allCitySubway);
			return result;
		}
		
		String cacheKey =  getKey(CityInfoTypeEnum.subway.name(), ver.getVersion());
		String cacheResult = redisCacheManager.getValue(cacheKey);
		if (!StringUtils.isEmpty(cacheResult)) {
			allCitySubway = GsonUtils.getInstace().fromJson(cacheResult, new TypeToken<AllCitySubwayDto>() {}.getType());
		} else {
			List<Long> cityIdList = cityRepository.findOpenedHasSubwayCityIdList();
			for (Long cityId : cityIdList) {
				List<Subway> subways = subwayRepository.findSubWaysByCityId(cityId);
				if (CollectionUtils.isNotEmpty(subways)) {
					// 提取地铁线包含的站点
					Map<String, SubwayLineDto> subwayMap = new HashMap<>();
					CitySubwayDto citySubwayDto = new CitySubwayDto();
					citySubwayDto.setCityId(cityId);
					// 添加全部地铁线路
					SubwayLineDto subwayLine0 = new SubwayLineDto();
					subwayLine0.setLineId(0);
					subwayLine0.setLineName("全部");
					citySubwayDto.addSubways(subwayLine0);
					// 循环遍历地铁线路
					for (Subway subway : subways) {
						SubwayLineDto subwayLine = subwayMap.get(subway.getSubwayLine());
						if (subwayLine == null) {
							subwayLine = new SubwayLineDto();
							subwayLine.setLineId(subway.getSubwayLineId());
							subwayLine.setLineName(subway.getSubwayLine());
							subwayMap.put(subway.getSubwayLine(), subwayLine);
							citySubwayDto.addSubways(subwayLine);

							SubwayStationDto subwayAll = new SubwayStationDto();
							subwayAll.setStationName("全部");
							subwayAll.setStationId(0);
							subwayLine.addSubwayStation(subwayAll);
						}
						SubwayStationDto subwayStationDto = new SubwayStationDto();
						subwayStationDto.setStationName(subway.getStation());
						subwayStationDto.setStationId(subway.getId());
						subwayLine.addSubwayStation(subwayStationDto);
					}

					allCitySubway.addCitysSubway(citySubwayDto);
				}
			}
			allCitySubway.setVersion(ver.getVersion());
			allCitySubway.setUpdateFag(UpdateFlagEnum.UPDATE.getCode());

			redisCacheManager.putValue(cacheKey, GsonUtils.getInstace().toJson(allCitySubway));//放入缓存
		}
		result.setBody(allCitySubway);
		return result;
	}
	

	/**
	 * 查询城市的所有商圈信息
	 * 
	 * @param cityId
	 * @return
	 */
	public Responses reqCityBizCircle(long cityId, long version) {
		Responses result = new Responses();
		long cVersion = 0;
		List<District> districts = districtRepository.findDistrictsByCityId(cityId);
		if (districts == null || districts.isEmpty()) {
			return result;
		}

		// 为了便于更新缓存，改为根据cityId查询
		List<Area> areas = areaRepository.findAreasByCityId(cityId);

		BussinessCircleQueryDto bCircleQueryDto = new BussinessCircleQueryDto();

		// 添加全部行政区选项 2017-08-29 15:03:37 YDM
		BusinessCircle bCircle0 = new BusinessCircle();
		bCircle0.setDistrictId(0);
		bCircle0.setDistrictName("全部");
		bCircleQueryDto.addBusinessCircle(bCircle0);
		// 遍历全部行政区，并添加行政区下的地铁
		for (District district : districts) {
			BusinessCircle bCircle = new BusinessCircle();
			bCircle.setDistrictId(district.getDistrictId());
			bCircle.setDistrictName(district.getName());
			bCircle.addAreaInfo(0, "全部");// 增加全部 2017-06-14 16:01:38 jjs
			if (areas == null) {
				bCircleQueryDto.addBusinessCircle(bCircle);
				continue;
			}
			for (Area area : areas) {
				if (area.getDistrictId() == district.getDistrictId()) {
					bCircle.addAreaInfo(area.getAreaId(), area.getName());
				}
			}
			bCircleQueryDto.addBusinessCircle(bCircle);
		}
		bCircleQueryDto.setVersion(cVersion);
		bCircleQueryDto.setUpdateFag(Constants.CityInfo.NEED_UPDATE_FLAG);

		result.setBody(bCircleQueryDto);
		if (CollectionUtils.isNotEmpty(areas)) {
			ResponseUtils.fillRow(result.getMeta(), areas.size(), areas.size());
		}

		return result;
	}
	
	/**
	 * 获取所有城市商圈
	 * @param version
	 * @return
	 */
	public Responses reqAllCitysBizcircles(Integer version){
		
		Responses result = new Responses();
		AllCityBizCircleDto citysBizCircle = new AllCityBizCircleDto();
		
		CityInfoVersion ver = cityInfoVersionRepository.findByInfoType(CityInfoTypeEnum.bizCircle.getCode());
		if(version >= ver.getVersion()){
			citysBizCircle.setVersion(ver.getVersion());
			citysBizCircle.setUpdateFlag(UpdateFlagEnum.NOT_UPDATE.getCode());
			result.setBody(citysBizCircle);
			return result;
		}
		String cacheKey =  getKey(CityInfoTypeEnum.bizCircle.name(), ver.getVersion());
		String cacheResult = redisCacheManager.getValue(cacheKey);
		if(!StringUtils.isEmpty(cacheResult)){//如果不在缓存，则重新加载
			citysBizCircle = GsonUtils.getInstace().fromJson(cacheResult, new TypeToken<AllCityBizCircleDto>() {}.getType());
		}else{
			List<Long> cityIdList = cityRepository.findOpenedHasSubwayCityIdList();
			for (Long cityId : cityIdList) {
				
				List<District> districts = districtRepository.findDistrictsByCityId(cityId);
				// 为了便于更新缓存，改为根据cityId查询
				List<Area> cityareas = areaRepository.findAreasByCityId(cityId);
				
				Multimap<Long ,Area> multimap = ArrayListMultimap.create();
				for (Area area : cityareas) {
					multimap.put(area.getDistrictId(), area);
				}
				
				CityBizCircleDto bCircleQueryDto = new CityBizCircleDto();
				bCircleQueryDto.setCityId(cityId);
				// 添加全部行政区选项 2017-08-29 15:03:37 YDM
				BusinessCircle bCircle0 = new BusinessCircle();
				bCircle0.setDistrictId(0);
				bCircle0.setDistrictName("全部");
				bCircleQueryDto.addBusinessCircle(bCircle0);
				// 遍历全部行政区，并添加行政区下的商圈
				for (District district : districts) {
					BusinessCircle bCircle = new BusinessCircle();
					bCircle.setDistrictId(district.getDistrictId());
					bCircle.setDistrictName(district.getName());
					bCircle.addAreaInfo(0, "全部");// 增加全部 2017-06-14 16:01:38 jjs
					for (Area area : multimap.get(district.getDistrictId())) {
						bCircle.addAreaInfo(area.getAreaId(), area.getName());
					}
					bCircleQueryDto.addBusinessCircle(bCircle);
				}
				citysBizCircle.addCitysBizCircles(bCircleQueryDto);
			}
			
			citysBizCircle.setVersion(ver.getVersion());
			citysBizCircle.setUpdateFlag(UpdateFlagEnum.UPDATE.getCode());
			
			redisCacheManager.putValue(cacheKey, GsonUtils.getInstace().toJson(citysBizCircle));//放入缓存
		}
		result.setBody(citysBizCircle);
		return result;
	}
	
	/**
	 * 获取缓存的key
	 * @param type
	 * @param version
	 * @return
	 */
	private String getKey(String type, Integer version) {
		return CITY_SUB_BIZ + ":" + type + "_" + version;
	}
	
	/**
	 * 查询城市的热点商圈信息
	 * 
	 * @param cityId
	 * @return
	 */
	public Responses reqCityHotBizCircle(long cityId) {
		Responses result = new Responses();

		List<Area> areas = null;
		int size = queryConfiguration.getHotAreaLimit();
		if (size == Constants.Common.QUERY_LIMIT_ALL) {
			areas = areaRepository.findHotAreaByCityId(cityId);
		} else {
			PageRequest pageRequest = new PageRequest(0, size);
			areas = areaRepository.findHotAreaByCityId(cityId, pageRequest);
		}

		CircleAreaQueryDto areaQueryDto = new CircleAreaQueryDto();

		if (CollectionUtils.isNotEmpty(areas)) {
			for (Area area : areas) {
				AreaInfo areaInfo = new AreaInfo();
				areaInfo.setAreaId(area.getAreaId());
				areaInfo.setAreaName(area.getName());
				areaQueryDto.addCicleArea(areaInfo);
			}

			ResponseUtils.fillRow(result.getMeta(), areas.size(), areas.size());
		}

		result.setBody(areaQueryDto);
		return result;
	}

	/**
	 * 查询所有的城市
	 * 
	 * @return
	 */
	public Responses reqAllCities() {
		// Responses result = new Responses();
		// CityQueryDto cityQueryDto = new CityQueryDto();
		// int n = 0;
		// String cityName = null;
		// // 北上广深优先排序
		// List<City> steadyCities = cityRepository.findSteadyCity();
		// if (CollectionUtils.isNotEmpty(steadyCities)) {
		// n = steadyCities.size();
		// for (City city : steadyCities) {
		// if (city.getName().indexOf("市") != -1) {
		// cityName = city.getName().replaceAll("市", "");
		// } else {
		// cityName = city.getName();
		// }
		// cityQueryDto.addCity(city.getCityId(), cityName, city.getCenter());
		// }
		// }
		// // 获取已开通的城市列表
		// List<City> cities = cityRepository.findCityByStatus();
		// List<Long> cityIdList = new ArrayList<Long>();
		// if (CollectionUtils.isNotEmpty(cities)) {
		// for (City city : cities) {
		// cityIdList.add(city.getCityId());
		// }
		// }
		// // 除北上广深之外的城市按房源量从大到小排序
		// List<QueryDetailVo> citiesVo =
		// houseDetailRepository.getCityInfoList();
		// if (CollectionUtils.isNotEmpty(citiesVo) &&
		// CollectionUtils.isNotEmpty(cities)) {
		// for (QueryDetailVo queryDetailVo : citiesVo) {
		// if (cityIdList.contains(queryDetailVo.getCityId())) {
		// if (queryDetailVo.getCityName().indexOf("市") != -1) {
		// cityName = queryDetailVo.getCityName().replaceAll("市", "");
		// } else {
		// cityName = queryDetailVo.getCityName();
		// }
		// cityQueryDto.addCity(queryDetailVo.getCityId(), cityName,
		// queryDetailVo.getCenter());
		// n++;
		// }
		// }
		// }
		// result.setBody(cityQueryDto);
		// ResponseUtils.fillRow(result.getMeta(), n, n);
		// return result;
		Responses result = new Responses();
		List<City> cities = cityRepository.findOpenCityList();
		if (cities == null || cities.isEmpty()) {
			return result;
		}
		String cityName = null;
		CityQueryDto cityDto = new CityQueryDto();
		for (City city : cities) {
			if (city.getName().indexOf("市") != -1) {
				cityName = city.getName().replaceAll("市", "");
			} else {
				cityName = city.getName();
			}
			cityDto.addCity(city.getCityId(), cityName, city.getCenter(), city.getHasSubway());
		}
		result.setBody(cityDto);
		ResponseUtils.fillRow(result.getMeta(), cities.size(), cities.size());
		return result;
	}

	/**
	 * 查询的城市id
	 *
	 * @return
	 */
	public Responses reqCityById(HttpServletRequest request) throws Exception {
		Responses result = new Responses();
		String cityName = RequestUtils.getParameterString(request, "cityName");

		if (cityName.indexOf("市") != -1) {
			cityName = cityName.replaceAll("市", "");
		}

		List<City> cities = cityRepository.findCityByNameLike("%" + cityName + "%");
		if (cities == null || cities.isEmpty()) {
		    throw new BaseException(ErrorMsgCode.ERROR_MSG_QUERY_AREA_HOUSE_FAIL, "根据城市获取ID失败");
		}
		City city = cities.get(0);
		CityInfo cityQueryDto = new CityInfo(city.getCityId(), city.getName(), city.getCenter(), city.getHasSubway());
		result.setBody(cityQueryDto);
		ResponseUtils.fillRow(result.getMeta(), 1, 1);
		return result;
	}

	/**
	 * 设置热门商圈
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Responses addHotCityBizCircle(HttpServletRequest request) throws Exception {

		long areaId = RequestUtils.getParameterLong(request, "areaId");

		Area area = areaRepository.findOne(areaId);
		if (area == null) {
			logger.error(LogUtils.getCommLog("area查询失败, areaId:" + areaId));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_QUERY_AREA_HOUSE_FAIL, "商圈查询失败");
		}

		long cityId = area.getCityId();

		areaRepository.setHotAreaByIdAndCityId(areaId, cityId);

		Responses result = new Responses();
		return result;
	}

	/**
	 * 取消设置热门商圈
	 * 
	 * @return
	 */
	@Transactional
	public Responses delHotCityBizCircle(HttpServletRequest request) throws Exception {

		long areaId = RequestUtils.getParameterLong(request, "areaId");

		Area area = areaRepository.findOne(areaId);
		if (area == null) {
			logger.error(LogUtils.getCommLog("area查询失败, areaId:" + areaId));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_QUERY_AREA_HOUSE_FAIL, "商圈查询失败");
		}

		long cityId = area.getCityId();

		areaRepository.unsetHotAreaByIdAndCityId(areaId, cityId);

		Responses result = new Responses();
		return result;
	}

	/**
	 * 清除城市所有热门商圈
	 * 
	 * @return
	 */
	@Transactional
	public Responses clearHotCityBizCircle(HttpServletRequest request) throws Exception {

		long cityId = RequestUtils.getParameterLong(request, "cityId");

		areaRepository.unsetHotAreaByCityId(cityId);

		Responses result = new Responses();
		return result;
	}
}
