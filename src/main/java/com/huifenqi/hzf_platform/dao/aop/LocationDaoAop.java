/** 
 * Project Name: hzf_platform_project 
 * File Name: CityInfoVersionDaoAop.java 
 * Package Name: com.huifenqi.hzf_platform.dao.aop 
 * Date: 2016年5月21日下午5:42:53 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.aop;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.entity.location.Area;
import com.huifenqi.hzf_platform.context.entity.location.City;
import com.huifenqi.hzf_platform.context.entity.location.CityInfoVersion;
import com.huifenqi.hzf_platform.context.entity.location.CommunityBase;
import com.huifenqi.hzf_platform.context.entity.location.CommuteMapConfig;
import com.huifenqi.hzf_platform.context.entity.location.District;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;

/**
 * ClassName: LocationDaoAop date: 2016年5月21日 下午5:42:53 Description:
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Aspect
@Component
public class LocationDaoAop {

	@Autowired
	private RedisCacheManager redisCacheManager;

	private static Log logger = LogFactory.getLog(LocationDaoAop.class);

	private static final String CITY_INFO_VERSION_KEY = "city.info.version";

	private static final String CITY_SUBWAY_INFO_KEY = "city.subway.info";

	private static final String CITY_DISTRICT_INFO_KEY = "city.district.info";

	private static final String CITY_AREA_INFO_KEY = "city.area.info";
	
	private static final String CITY_COMMUNITY_INFO_KEY = "city.community.info";
	
	private static final String CITY_COMMUTE_MAP_CONFIG_KEY = "commute.map.config.info";

	private static final String CITY_LIST_INFO = "city.list.info";

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CityInfoVersionRepository.findInfoVersionBy(..)) && args(cityId, infoType)")
	public CityInfoVersion findInfoVersionBy(ProceedingJoinPoint pjp, long cityId, int infoType) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_INFO_VERSION_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(cityId).append("-").append(infoType);
		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, CityInfoVersion.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市信息版本%s失败", hashKey.toString())));
		}

		CityInfoVersion version = (CityInfoVersion) pjp.proceed();

		if (version != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(version));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市版本信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(version)))));
			}
		}

		return version;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository.findSubWaysByCityId(..)) && args(cityId)")
	public List<Subway> findSubWaysByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_SUBWAY_INFO_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Subway>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取地铁信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<Subway> subways = (List<Subway>) pjp.proceed();

		if (subways != null && !subways.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(subways));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市地铁信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(subways)))));
			}
		}

		return subways;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.DistrictRepository.findDistrictsByCityId(..)) && args(cityId)")
	public List<District> findDistrictsByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_DISTRICT_INFO_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<District>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市区域信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<District> districts = (List<District>) pjp.proceed();

		if (districts != null && !districts.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(districts));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市区域信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(districts)))));
			}
		}

		return districts;
	}
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CommunityBaseRepository.findCommunityByCityId(..)) && args(cityId)")
	public List<CommunityBase> findCommunityByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_COMMUNITY_INFO_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CommunityBase>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市商圈信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<CommunityBase> communityList = (List<CommunityBase>) pjp.proceed();

		if (communityList != null && !communityList.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(communityList));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市商圈信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(communityList)))));
			}
		}

		return communityList;
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CommuteMapConfigRepository.findCommuteMapConfigById(..)) && args(id)")
	public CommuteMapConfig findCommuteMapConfigById(ProceedingJoinPoint pjp, long id) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_COMMUTE_MAP_CONFIG_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");
		hashKey.append(id);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CommunityBase>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取通勤配置信息%s失败", hashKey.toString())));
		}

		CommuteMapConfig config = (CommuteMapConfig) pjp.proceed();

		if (config != null ) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(config));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存通勤配置信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(config)))));
			}
		}

		return config;
	}
	
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CommuteMapConfigRepository.findAllCommuteMapConfig(..))")
	public List<CommuteMapConfig> findAllCommuteMapConfig(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_COMMUTE_MAP_CONFIG_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CommuteMapConfig>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取通勤配置信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<CommuteMapConfig> configList = (List<CommuteMapConfig>) pjp.proceed();

		if (configList != null && !configList.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(configList));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存通勤配置信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(configList)))));
			}
		}

		return configList;
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.findAreasByCityId(..)) && args(cityId)")
	public List<Area> findAreasByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_AREA_INFO_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Area>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市商圈信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<Area> areas = (List<Area>) pjp.proceed();

		if (areas != null && !areas.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(areas));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市商圈信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(areas)))));
			}
		}

		return areas;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.findHotAreaByCityId(..)) && args(cityId)")
	public List<Area> findHotAreaByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_AREA_INFO_KEY);
		String hashKey = getCityAreaAllHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Area>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市热门商圈信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<Area> areas = (List<Area>) pjp.proceed();

		if (areas != null && !areas.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(areas));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市热门商圈信息(%s=%s)到缓存失败", hashKey,
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(areas)))));
			}
		}

		return areas;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.findHotAreaByCityId(..)) && args(cityId,pageable)")
	public List<Area> findHotAreaByCityId(ProceedingJoinPoint pjp, long cityId, Pageable pageable) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_AREA_INFO_KEY);
		String hashKey = getCityAreaLimitHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Area>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取分页城市热门商圈信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<Area> areas = (List<Area>) pjp.proceed();

		if (areas != null && !areas.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(areas));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(
						String.format("保存分页城市热门商圈信息(%s=%s)到缓存失败", hashKey, GsonUtils.getInstace().toJson(areas))));
			}
		}

		return areas;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CityRepository.findAllCities(..))")
	public List<City> findAllCities(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_LIST_INFO);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<City>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<City> cities = (List<City>) pjp.proceed();

		if (cities != null && !cities.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(cities));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(cities)))));
			}
		}
		return cities;
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CityRepository.findCityByStatus(..))")
	public List<City> findCityByStatus(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_LIST_INFO);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<City>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<City> cities = (List<City>) pjp.proceed();

		if (cities != null && !cities.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(cities));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(cities)))));
			}
		}
		return cities;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CityRepository.findCityByName(..)) && args(name)")
	public City findCityByName(ProceedingJoinPoint pjp, String name) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_LIST_INFO);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(name);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, City.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市信息%s失败", hashKey.toString())));
		}

		City city = (City) pjp.proceed();

		if (city != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(city));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(city)))));
			}
		}
		return city;
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.CityRepository.findCityById(..)) && args(cityId)")
	public City findCityById(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_LIST_INFO);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, City.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取城市信息%s失败", hashKey.toString())));
		}

		City city = (City) pjp.proceed();

		if (city != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(city));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存城市信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(city)))));
			}
		}
		return city;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.SubwayRepository.findSubWayByStationId(..)) && args(stationId)")
	public Subway findSubWayByStationId(ProceedingJoinPoint pjp, long stationId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_SUBWAY_INFO_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("stationId-");
		hashKey.append(stationId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<Subway>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取地铁信息%s失败", hashKey.toString())));
		}

		Subway subway = (Subway) pjp.proceed();

		if (subway != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(subway));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存地铁信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(subway)))));
			}
		}

		return subway;
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.setHotAreaByIdAndCityId(..))", returning = "count")
	public void afterSetHotArea(JoinPoint joinPoint, int count) {
		if (count == 0) {
			return;
		}
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 2) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}
		Object object = args[1];
		if (!(object instanceof Long)) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}
		long cityId = (long) object;

		removeCityHotArea(cityId);

	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.unsetHotAreaByIdAndCityId(..))", returning = "count")
	public void afterUnsetHotArea(JoinPoint joinPoint, int count) {
		if (count == 0) {
			return;
		}
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 2) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}
		Object object = args[1];
		if (!(object instanceof Long)) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}
		long cityId = (long) object;

		removeCityHotArea(cityId);
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.location.AreaRepository.unsetHotAreaByCityId(..))", returning = "count")
	public void afterDeleteCityAllHotArea(JoinPoint joinPoint, int count) {
		if (count == 0) {
			return;
		}
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 1) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}
		Object object = args[0];
		if (!(object instanceof Long)) {
			logger.error(LogUtils.getCommLog(String.format("cityId获取失败")));
			return;
		}

		long cityId = (long) object;

		removeCityHotArea(cityId);
	}

	private void removeCityHotArea(long cityId) {
		String key = RedisUtils.getInstance().getKey(CITY_AREA_INFO_KEY);

		String allHashKey = getCityAreaAllHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, allHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除热门商圈(%s,%s)失败", key, allHashKey)));
		}

		String limitHashKey = getCityAreaLimitHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, limitHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除热门商圈(%s,%s)失败", key, limitHashKey)));
		}
	}

	private String getCityAreaAllHashKey(long cityId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("hot-all-");
		hashKey.append(cityId);
		return hashKey.toString();
	}

	private String getCityAreaLimitHashKey(long cityId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("hot-limit-");
		hashKey.append(cityId);
		return hashKey.toString();
	}
}
