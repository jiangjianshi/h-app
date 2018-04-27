/** 
* Project Name: hzf_platform_project 
* File Name: HouseDaoAop.java 
* Package Name: com.huifenqi.hzf_platform.dao.aop 
* Date: 2016年6月4日上午10:42:04 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.aop;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.entity.house.Apartment;
import com.huifenqi.hzf_platform.context.entity.house.HouseCollection;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;
import com.huifenqi.hzf_platform.context.entity.house.HouseToken;
import com.huifenqi.hzf_platform.context.entity.house.SlideShowUrl;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: HouseDaoAop date: 2016年6月4日 上午10:42:04 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Aspect
@Component
public class HouseDaoAop {

	private static Log logger = LogFactory.getLog(HouseDaoAop.class);

	@Autowired
	private RedisCacheManager redisCacheManager;
	
	@Autowired
	private HouseDao houseDao;

	private static final String CITY_RECOMMEND_HOUSE_KEY = "city.house.recommend";

	private static final String CITY_APARTMENT_KEY = "city.house.apartment";

	private static final String HOUSE_TOKEN = "house.token";
	
	private static final String SLIDE_SHOW_URL_KEY = "slide_show_url";
	
	private static final String SLIDE_SHOW_URL_HASHKEY = "all_slide_show_url";
	
	private static final String USER_HOUSE_COLLECTION_KEY = "user.house.collection";

	private static final String HUSE_TOP_100 = "house_top_100:";
	private static final String HUSE_TOP_10 = "house_top_10:";


	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository.findHouseRecommends(..)) && args(cityId)")
	public List<HouseRecommend> findHouseRecommendByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_RECOMMEND_HOUSE_KEY);
		String hashKey = getCityAllHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<HouseRecommend>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取推荐房源信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<HouseRecommend> houseRecommends = (List<HouseRecommend>) pjp.proceed();

		if (houseRecommends != null && !houseRecommends.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(houseRecommends));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存推荐房源信息(%s=%s)到缓存失败", hashKey,
						GsonUtils.getInstace().toJson(houseRecommends))));
			}
		}

		return houseRecommends;
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository.findHouseRecommends(..)) && args(cityId,pageable)")
	public List<HouseRecommend> findHouseRecommendByCityId(ProceedingJoinPoint pjp, long cityId, Pageable pageable)
			throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_RECOMMEND_HOUSE_KEY);
		String hashKey = getCityLimitHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<HouseRecommend>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取分页推荐房源信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<HouseRecommend> houseRecommends = (List<HouseRecommend>) pjp.proceed();

		if (houseRecommends != null && !houseRecommends.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(houseRecommends));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存分页推荐房源信息(%s=%s)到缓存失败", hashKey,
						GsonUtils.getInstace().toJson(houseRecommends))));
			}
		}

		return houseRecommends;
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository.save(..))", returning = "retObj")
	public void afterSaveHouseRecommend(JoinPoint joinPoint, Object retObj) {
		HouseRecommend houseRecommend = (HouseRecommend) retObj;
		long cityId = houseRecommend.getCityId();

		removeCityHouseRecommend(cityId);

	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository.setIsDeleteByIdAndCityId(..))", returning = "count")
	public void afterDeleteHouseRecommend(JoinPoint joinPoint, int count) {
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

		removeCityHouseRecommend(cityId);
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseRecommendRepository.setIsDeleteByCityId(..))", returning = "count")
	public void afterDeleteCityAllHouseRecommend(JoinPoint joinPoint, int count) {
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

		removeCityHouseRecommend(cityId);
	}

	private void removeCityHouseRecommend(long cityId) {
		String key = RedisUtils.getInstance().getKey(CITY_RECOMMEND_HOUSE_KEY);

		String allHashKey = getCityAllHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, allHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除推荐房源信息(%s,%s)失败", key, allHashKey)));
		}

		String limitHashKey = getCityLimitHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, limitHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除推荐房源信息(%s,%s)失败", key, limitHashKey)));
		}
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository.findHotApartments(..)) && args(cityId)")
	public List<Apartment> findHotApartmentsByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_APARTMENT_KEY);
		String hashKey = getCityAllHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Apartment>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取热门公寓信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<Apartment> hotApartments = (List<Apartment>) pjp.proceed();

		if (hotApartments != null && !hotApartments.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(hotApartments));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(
						String.format("保存热门公寓信息(%s=%s)到缓存失败", hashKey, GsonUtils.getInstace().toJson(hotApartments))));
			}
		}

		return hotApartments;
	}

	

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository.findHotApartments(..)) && args(cityId,pageable)")
	public List<Apartment> findHotApartmentsByCityId(ProceedingJoinPoint pjp, long cityId, Pageable pageable)
			throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_APARTMENT_KEY);
		String hashKey = getCityLimitHashKey(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Apartment>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取分页热门公寓信息%s失败", hashKey)));
		}

		@SuppressWarnings("unchecked")
		List<Apartment> hotApartments = (List<Apartment>) pjp.proceed();

		if (hotApartments != null && !hotApartments.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(hotApartments));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存分页热门公寓信息(%s=%s)到缓存失败", hashKey,
						GsonUtils.getInstace().toJson(hotApartments))));
			}
		}

		return hotApartments;
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository.save(..))", returning = "retObj")
	public void afterSaveApartment(JoinPoint joinPoint, Object retObj) {
		Apartment apartment = (Apartment) retObj;
		long cityId = apartment.getCityId();

		removeCityApartment(cityId);

	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository.setIsDeleteByIdAndCityId(..))", returning = "count")
	public void afterDeleteApartment(JoinPoint joinPoint, int count) {
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

		removeCityApartment(cityId);
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.ApartmentRepository.setIsDeleteByCityId(..))", returning = "count")
	public void afterDeleteCityAllApartment(JoinPoint joinPoint, int count) {
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

		removeCityApartment(cityId);
	}

	private void removeCityApartment(long cityId) {
		String key = RedisUtils.getInstance().getKey(CITY_APARTMENT_KEY);

		String allHashKey = getCityAllHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, allHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除热门公寓信息(%s,%s)失败", key, allHashKey)));
		}

		String limitHashKey = getCityLimitHashKey(cityId);
		try {
			redisCacheManager.deleteHash(key, limitHashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除热门公寓信息(%s,%s)失败", key, limitHashKey)));
		}
	}

	private String getCityAllHashKey(long cityId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");
		hashKey.append(cityId);
		return hashKey.toString();
	}

	private String getCityLimitHashKey(long cityId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("limit-");
		hashKey.append(cityId);
		return hashKey.toString();
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseTokenRepository.findTokenBySellIdAndRoomId(..)) && args(sellId,roomId)")
	public String findTokenBySellIdAndRoomId(ProceedingJoinPoint pjp, String sellId, long roomId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(HOUSE_TOKEN);
		String hashKey = getTokenHashKey(sellId, roomId);

		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取token信息%s失败", hashKey)));
		}

		// 查询token
		String token = (String) pjp.proceed();

		if (token != null) {
			try {
				redisCacheManager.putHash(key, hashKey, token);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存token信息(%s=%s)到缓存失败", hashKey, token)));
			}
		}

		return token;
	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseTokenRepository.save(..))", returning = "retObj")
	public void afterSaveHouseToken(JoinPoint joinPoint, Object retObj) {
		HouseToken houseToken = (HouseToken) retObj;
		String sellId = houseToken.getSellId();
		long roomId = houseToken.getRoomId();
		String token = houseToken.getToken();

		// 保存token
		String key = RedisUtils.getInstance().getKey(HOUSE_TOKEN);
		String hashKey = getTokenHashKey(sellId, roomId);
		try {
			redisCacheManager.putHash(key, hashKey, token);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("保存token信息(%s=%s)到缓存失败", hashKey, token)));
		}

	}

	@AfterReturning(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.HouseTokenRepository.setIsDeleteBySellIdAndRoomId(..))", returning = "count")
	public void afterDeleteHouseToken(JoinPoint joinPoint, int count) {
		if (count == 0) {
			return;
		}
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 2) {
			logger.error(LogUtils.getCommLog(String.format("setIsDeleteBySellIdAndRoomId参数获取失败")));
			return;
		}
		Object object = args[0];
		if (!(object instanceof String)) {
			logger.error(LogUtils.getCommLog(String.format("sellId获取失败")));
			return;
		}
		String sellId = (String) object;

		Object object1 = args[1];
		if (!(object1 instanceof Long)) {
			logger.error(LogUtils.getCommLog(String.format("roomId获取失败")));
			return;
		}
		long roomId = (long) object1;

		// 删除token
		String key = RedisUtils.getInstance().getKey(HOUSE_TOKEN);
		String hashKey = getTokenHashKey(sellId, roomId);
		try {
			redisCacheManager.deleteHash(key, hashKey);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("删除缓存token信息(%s,%s)失败", key, hashKey)));
		}

	}

	private String getTokenHashKey(String sellId, long roomId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(sellId);
		hashKey.append(StringUtil.HYPHEN);
		hashKey.append(roomId);
		return hashKey.toString();
	}
	
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.platform.SlideShowUrlRepository.getSlideShowUrlList(..))")
	public List<SlideShowUrl> getSlideShowUrlList(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(SLIDE_SHOW_URL_KEY);
		String hashKey = SLIDE_SHOW_URL_HASHKEY;
		try {
			String result = redisCacheManager.getHash(key, hashKey);
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<SlideShowUrl>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取轮播图信息%s失败", hashKey)));
		}
		@SuppressWarnings("unchecked")
		List<SlideShowUrl> slideShowUrlList = (List<SlideShowUrl>) pjp.proceed();
		if (slideShowUrlList != null && !slideShowUrlList.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey, GsonUtils.getInstace().toJson(slideShowUrlList));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(
						String.format("保存轮播图信息(%s=%s)到缓存失败", hashKey, GsonUtils.getInstace().toJson(slideShowUrlList))));
			}
		}
		return slideShowUrlList;
	}
	
	
	private String getHouseCollectionHashKey(long userId,String sellId, int roomId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(userId);
		hashKey.append(StringUtil.HYPHEN);
		hashKey.append(sellId);
		hashKey.append(StringUtil.HYPHEN);
		hashKey.append(roomId);
		return hashKey.toString();
	}

	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.HouseDao.getNewHouseResultDto(..)) && args(houseSearchDto,agencyIdList)")
	public HouseSearchResultDto getNewHouseResultDto(HouseSearchDto houseSearchDto, List<String> agencyIdList)
			throws Throwable {
		String key = RedisUtils.getInstance().getKey(HUSE_TOP_100);
		String cityKey = houseSearchDto.getCityId()+":";
		String newKey = getHouseListHashKey(key,cityKey,houseSearchDto.toString()+agencyIdList.toString());
		HouseSearchResultDto  houseSearchResultDto = new HouseSearchResultDto();
		try {
			String result = redisCacheManager.getValue(newKey);
			if (result != null) {
				 houseSearchResultDto.setSearchHouses(GsonUtils.getInstace().fromJson(result, new TypeToken<List<HouseSearchResultInfo>>() {
				}.getType()));
				 return houseSearchResultDto;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取top前100房源列表信息%s失败", newKey)));
		}
		
		houseSearchResultDto= houseDao.getNewAopHouseResultDto(houseSearchDto, agencyIdList); 
		List<HouseSearchResultInfo> infoList = houseSearchResultDto.getSearchHouses();

		if (infoList != null && !infoList.isEmpty()) {
			try {
				
				redisCacheManager.putValue(newKey,GsonUtils.getInstace().toJson(infoList),Constants.HouseListConfig.REDIS_TIME_OUT);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存top前100房源列表(%s=%s)到缓存失败", newKey,
						GsonUtils.getInstace().toJson(infoList))));
			}
		}

		return houseSearchResultDto;
	}
		
	private String getHouseListHashKey(String key ,String cityKey,String  paramkey) {
		StringBuilder hashKey = new StringBuilder();
		String shakey = DigestUtils.sha256Hex(paramkey.trim());
		hashKey.append(key);
		hashKey.append(cityKey);
		hashKey.append(shakey);
		return hashKey.toString();
	}
}
