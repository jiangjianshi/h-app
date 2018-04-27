/**
 * Project Name: hzf_platform
 * File Name: HouseRequestHandler.java
 * Package Name: com.huifenqi.hzf_platform.handler
 * Date: 2016年4月26日下午4:40:45
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved.
 *
 */
package com.huifenqi.hzf_platform.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.MapHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.map.CommuteMapConfigDto;
import com.huifenqi.hzf_platform.context.dto.response.map.CommuteMapConfigInfo;
import com.huifenqi.hzf_platform.context.dto.response.map.CommuteMapConfigQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.map.MapHouseInfo;
import com.huifenqi.hzf_platform.context.dto.response.map.MapHouseQueryDto;
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.context.entity.location.CommuteMapConfig;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.MapHouseDao;
import com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository;
import com.huifenqi.hzf_platform.dao.repository.location.CommuteMapConfigRepository;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.ResponseUtils;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: HouseRequestHandler date: 2017年10月09日 下午4:40:45 Description:
 *
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Service
public class MapHouseRequestHandler {

	private static final Log logger = LogFactory.getLog(MapHouseRequestHandler.class);

	@Autowired
	private MapHouseDao mapHouseDao;

	@Autowired
	private AgencyManageRepository agencyManageRepository;
	
	@Autowired
	private CommuteMapConfigRepository commuteMapConfigRepository;

	@Autowired
	private Configuration configuration;

	public static final String FOOTMARK = "footmark";

	
	
	/**
	 * 查询所有通勤配置
	 * 
	 * @return
	 */
	public Responses reqCommuteConfig() {
		Responses result = new Responses();
		List<CommuteMapConfig> configList = commuteMapConfigRepository.findAllCommuteMapConfig();
		if (configList == null || configList.isEmpty()) {
			return result;
		}
	    		
		/*通勤配置分组**/
		Map<String, List<CommuteMapConfigInfo>> configInfoMap = new HashMap<>();
		//默认出行方式
		Map<String, Long> defaultTypeMap = new HashMap<>();
		
		for (CommuteMapConfig config : configList) {
			List<CommuteMapConfigInfo> tempList = configInfoMap.get(config.getTravelType());
			
			//默认出行方式index初始化
			if(config.getIsDefault()==Constants.CommuteMapConfig.COMMUTE_ID_ISDEFAULT){
				defaultTypeMap.put(config.getTravelType(), config.getId()%5);
			}
			/*如果取不到数据,那么直接new一个空的ArrayList**/
			if(tempList == null){
				tempList = new ArrayList<>();
				tempList.add(new CommuteMapConfigInfo(config.getId() , config.getTimeName(), config.getScale()));
				configInfoMap.put(config.getTravelType(), tempList);
				
			}else{  
				/*某个sku之前已经存放过了,则直接追加数据到原来的List里**/
				tempList.add(new CommuteMapConfigInfo(config.getId() , config.getTimeName(), config.getScale()));
			}
		}
		
		//返回对象
		CommuteMapConfigQueryDto queryDto = new CommuteMapConfigQueryDto();
	
    	/*遍历map,封装结果**/
	    for(String key : configInfoMap.keySet()){
	    		CommuteMapConfigDto dto = new CommuteMapConfigDto();
	    		dto.setTravelType(key);
	    		
	    		//赋值出行方式名称
	    		if(key.equals(Constants.CommuteMapConfig.TYPE_BUS_CODE)){	    		
	    			dto.setTravelTypeName(Constants.CommuteMapConfig.TYPE_BUS_NAME);
	    		}if(key.equals(Constants.CommuteMapConfig.TYPE_DRIVER_CODE)){	    		
	    			dto.setTravelTypeName(Constants.CommuteMapConfig.TYPE_DRIVER_NAME);
	    		}if(key.equals(Constants.CommuteMapConfig.TYPE_RIDING_CODE)){	    		
	    			dto.setTravelTypeName(Constants.CommuteMapConfig.TYPE_RIDING_NAME);
	    		}if(key.equals(Constants.CommuteMapConfig.TYPE_WALK_CODE)){	    		
	    			dto.setTravelTypeName(Constants.CommuteMapConfig.TYPE_WALK_NAME);
	    		}
	    		
	    		//赋值默认出行方式index
	    		for(String typeKe:defaultTypeMap.keySet()){//默认显示出行方式index
	    			if(typeKe.equals(key)){
	    				dto.setDefaultCommuteIndex(defaultTypeMap.get(typeKe));
	    			}
	    		}
	    		dto.setCommuteConfig(configInfoMap.get(key));
	    		queryDto.addDto(dto);
	    }
		
		result.setBody(queryDto);
		ResponseUtils.fillRow(result.getMeta(), configList.size(), configList.size());
		return result;
	}
	
	/**
	 * 地图/通勤 找房
	 * 
	 * @return
	 */
	public Responses searchMapHouse(HttpServletRequest request) throws Exception {
		
		//封装请求参数
		MapHouseSearchDto mapHouseSearchDto = getMapHouseSearchDto(request);
		
		//请求solr获取地图找房数据
		return searchMapHouseList(mapHouseSearchDto);
	}

	private MapHouseSearchDto getMapHouseSearchDto(HttpServletRequest request) throws Exception {

		if (request == null) {
			return null;
		}

		MapHouseSearchDto mapHouseSearchDto = new MapHouseSearchDto();

		int appId = RequestUtils.getParameterInt(request, "appId");
		mapHouseSearchDto.setAppId(appId);

		String companyId = RequestUtils.getParameterString(request, "companyId", StringUtil.EMPTY);
		mapHouseSearchDto.setCompanyId(companyId);

		long cityId = RequestUtils.getParameterLong(request, "cityId", 0);
		if (cityId != 0) {
			mapHouseSearchDto.setCityId(cityId);
		}

		String keyword = RequestUtils.getParameterString(request, "q", StringUtil.EMPTY);
		mapHouseSearchDto.setKeyword(keyword);

		long disId = RequestUtils.getParameterLong(request, "disId", 0);
		mapHouseSearchDto.setDistrictId(disId);

		long bizId = RequestUtils.getParameterLong(request, "bizId", 0);
		mapHouseSearchDto.setBizId(bizId);

		long lineId = RequestUtils.getParameterLong(request, "lineId", 0);
		mapHouseSearchDto.setLineId(lineId);

		long stationId = RequestUtils.getParameterLong(request, "stationId", 0);
		mapHouseSearchDto.setStationId(stationId);

		String price = RequestUtils.getParameterString(request, "price", StringUtil.EMPTY);
		checkRegionNumber(price, "price");
		mapHouseSearchDto.setPrice(price);

		int orientation = RequestUtils.getParameterInt(request, "orientation", 0);
		if (orientation != 0) {
			checkOrientation(orientation, "orientation");
		}
		mapHouseSearchDto.setOrientation(orientation);

		String area = RequestUtils.getParameterString(request, "area", StringUtil.EMPTY);
		checkRegionNumber(area, "area");
		mapHouseSearchDto.setArea(area);

		String location = RequestUtils.getParameterString(request, "location", StringUtil.EMPTY);
		checkPosition(location, "location");
		mapHouseSearchDto.setLocation(location);

		String distance = RequestUtils.getParameterString(request, "distance", StringUtil.EMPTY);
		//checkRegionNumber(distance, "distance");
		mapHouseSearchDto.setDistance(distance);

		String orderType = RequestUtils.getParameterString(request, "orderType", StringUtil.EMPTY);
		checkOrderType(orderType, "orderType");
		mapHouseSearchDto.setOrderType(orderType);

		String order = RequestUtils.getParameterString(request, "order", StringUtil.EMPTY);
		checkOrder(order, "order");
		mapHouseSearchDto.setOrder(order);

//		int pageNum = RequestUtils.getParameterInt(request, "pageNum", 1);
//		checkNonNegativeNumber(pageNum, "pageNum");
//		mapHouseSearchDto.setPageNum(pageNum);
//
//		int pageSize = RequestUtils.getParameterInt(request, "pageSize", 10);
//		checkNonNegativeNumber(pageSize, "pageSize");
//		mapHouseSearchDto.setPageSize(pageSize);

		// 整租/分租,可选，默认全部
		int entireRent = RequestUtils.getParameterInt(request, "entireRent", Constants.HouseDetail.RENT_TYPE_ALL);
		checkEntireRent(entireRent, "entireRent");
		mapHouseSearchDto.setEntireRent(entireRent);

		String bedroomNums = RequestUtils.getParameterString(request, "bedroomNums", StringUtil.EMPTY);
		checkRegionNumber(bedroomNums, "bedroomNums");
		mapHouseSearchDto.setBedroomNum(bedroomNums);

		String houseTag = RequestUtils.getParameterString(request, "houseTag", StringUtil.EMPTY);
		checkSeparatedNumber(houseTag, "houseTag");
		mapHouseSearchDto.setHouseTag(houseTag);

		String orientationStr = RequestUtils.getParameterString(request, "orientationStr", StringUtil.EMPTY);
		checkSeparatedNumber(orientationStr, "orientationStr");
		mapHouseSearchDto.setOrientationStr(orientationStr);

		String eBedRoomNums = RequestUtils.getParameterString(request, "eBedRoomNums", "0");
		String sBedRoomNums = RequestUtils.getParameterString(request, "sBedRoomNums", "0");
		if (!"0".equals(eBedRoomNums) && !"0".equals(sBedRoomNums)) {
			mapHouseSearchDto.seteBedRoomNums(eBedRoomNums);
			mapHouseSearchDto.setsBedRoomNums(sBedRoomNums);
			mapHouseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ALL);
		} else if (!"0".equals(eBedRoomNums)) {
			mapHouseSearchDto.seteBedRoomNums(eBedRoomNums);
			mapHouseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_ENTIRE);
		} else if (!"0".equals(sBedRoomNums)) {
			mapHouseSearchDto.setsBedRoomNums(sBedRoomNums);
			mapHouseSearchDto.setEntireRent(Constants.HouseDetail.RENT_TYPE_SHARE);
		}

		String payType = RequestUtils.getParameterString(request, "payType", StringUtil.EMPTY);
		mapHouseSearchDto.setPayType(payType);

		String sellerId = RequestUtils.getParameterString(request, "sellerId", StringUtil.EMPTY);
		if (StringUtil.isNotBlank(sellerId)) {
			mapHouseSearchDto.setSellerId(sellerId);
		}
		int roomerId = RequestUtils.getParameterInt(request, "roomerId", 0);
		mapHouseSearchDto.setRoomerId(roomerId);
		
		int level = RequestUtils.getParameterInt(request, "level");
		checkLevel(level, "level");
		mapHouseSearchDto.setLevel(level);
		
		String travelType = RequestUtils.getParameterString(request, "travelType", StringUtil.EMPTY);
		mapHouseSearchDto.setTravelType(travelType);
		
		String channel = RequestUtils.getParameterString(request, "channel", StringUtil.EMPTY);
		mapHouseSearchDto.setChannel(channel);

		int commuteId = RequestUtils.getParameterInt(request, "commuteId",0);
		mapHouseSearchDto.setCommuteId(commuteId);
		
		String companyType = RequestUtils.getParameterString(request, "companyType", StringUtil.EMPTY);
		mapHouseSearchDto.setCompanyType(companyType);
		
		return mapHouseSearchDto;

	}


	/**
	 * 校验区域字段
	 *
	 * @param regionNumber
	 * @param keyName
	 */
	private void checkRegionNumber(String regionNumber, String keyName) {
		if (StringUtil.isNotBlank(regionNumber)) {
			boolean valid = RulesVerifyUtil.verifyNumberRegion(regionNumber);
			if (!valid) {
				throw new InvalidParameterException("参数格式不正确:" + keyName);
			}
		}
	}

	/**
	 * 校验坐标字段
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkPosition(String position, String keyName) {
		if (StringUtil.isNotBlank(position)) {
			boolean valid = RulesVerifyUtil.verifyPosition(position);
			if (!valid) {
				throw new InvalidParameterException("参数格式不正确:" + keyName);
			}
		}
	}

	/**
	 * 校验排序类型字段
	 *
	 * @param regionNumber
	 * @param keyName
	 */
	private void checkOrderType(String orderType, String keyName) {
		if (StringUtil.isNotBlank(orderType)) {
			if (!Constants.Search.containsOrderType(orderType)) {
				throw new InvalidParameterException("参数异常:" + keyName);
			}
		}
	}

	/**
	 * 校验排序字段
	 *
	 * @param regionNumber
	 * @param keyName
	 */
	private void checkOrder(String order, String keyName) {
		if (StringUtil.isNotBlank(order)) {
			if (!Constants.Search.containsOrder(order)) {
				throw new InvalidParameterException("参数异常:" + keyName);
			}
		}
	}

	/**
	 * 校验逗号分隔数字
	 *
	 * @param regionNumber
	 * @param keyName
	 */
	private void checkSeparatedNumber(String regionNumber, String keyName) {
		if (StringUtil.isNotBlank(regionNumber)) {
			boolean valid = RulesVerifyUtil.verifySeparatedNumber(regionNumber);
			if (!valid) {
				throw new InvalidParameterException("参数格式不正确:" + keyName);
			}
		}
	}




	/**
	 * 抛出BaseException
	 *
	 * @param e
	 */
	private void rethrowBaseException(Exception e) {
		if (e instanceof BaseException) {
			throw (BaseException) e;
		}
	}

	/**
	 * 校验坐标值
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkPositionValue(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			boolean valid = RulesVerifyUtil.verifyPositionValue(positionValue);
			if (!valid) {
				throw new InvalidParameterException("参数格式不正确:" + keyName);
			}
		}
	}

	/**
	 * 校验纬度
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkLatitude(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			Double latitude = StringUtil.parseDouble(positionValue);
			boolean valid = latitude != null && latitude >= Constants.Common.LATITUDE_MIN
					&& latitude <= Constants.Common.LATITUDE_MAX;
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName);
			}
		}
	}

	/**
	 * 校验经度
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkLongitude(String positionValue, String keyName) {
		if (StringUtil.isNotBlank(positionValue)) {
			Double longitude = StringUtil.parseDouble(positionValue);
			boolean valid = longitude != null && longitude >= Constants.Common.LONGITUDE_MIN
					&& longitude <= Constants.Common.LONGITUDE_MAX;
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName);
			}
		}
	}

	/**
	 * 校验字符串长度
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkStringMaxLength(String string, String keyName, int maxLength) {
		if (StringUtil.isNotBlank(string)) {
			boolean valid = RulesVerifyUtil.verifyCsMaxLength(string, maxLength);
			if (!valid) {
				throw new InvalidParameterException("参数不合法:" + keyName + ",字符串过长");
			}
		}
	}

	/**
	 * 校验设置
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkSetting(String settingValue, String keyName) {
		if (StringUtil.isNotBlank(settingValue)) {
			boolean valid = RulesVerifyUtil.verifySetting(settingValue);
			String msg = "参数异常:" + keyName;
			if (!valid) {
				throw new InvalidParameterException(msg);
			}
			List<String> houseSettingSingleStringList = HouseUtil.getHouseSettingSingleStringList(settingValue);
			if (CollectionUtils.isNotEmpty(houseSettingSingleStringList)) {
				for (String singleString : houseSettingSingleStringList) {
					Pair<String, String> keyAndNum = HouseUtil.getHouseSettingKeyAndNum(singleString);
					if (keyAndNum == null) {
						throw new InvalidParameterException(msg);
					}
					String key = keyAndNum.getLeft();
					Pair<Integer, Integer> typeAndCode = HouseUtil.getHouseSettingTypeAndCode(key);
					if (typeAndCode == null) {
						throw new InvalidParameterException(msg);
					}
					Integer type = typeAndCode.getLeft();
					if (type == null || type == Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED) {
						throw new InvalidParameterException(msg);
					}
					Integer code = typeAndCode.getRight();
					if (code == null || code == Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED) {
						throw new InvalidParameterException(msg);
					}
					String num = keyAndNum.getRight();
					boolean isNumValid = RulesVerifyUtil.verifyNumber(num);
					if (!isNumValid) {
						throw new InvalidParameterException(msg);
					}
				}
			}
		}
	}

	/**
	 * 校验图片
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkImgs(String imgs, String keyName) {
		if (StringUtil.isNotBlank(imgs)) {
			String[] split = imgs.split(StringUtil.COMMA);
			int IMG_MAX = Constants.HousePicture.HOUSE_IMGS_MAX;
			if (split == null || split.length == 0) {
				throw new InvalidParameterException("参数异常:" + keyName);
			} else if (split.length > IMG_MAX) {
				throw new InvalidParameterException("参数异常:" + keyName + ",图片数量过多");
			}
		}
	}

	/**
	 * 校验房源/房间状态
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkStatus(int status, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_NOT_ON_SALE);
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_PRE_RENT);
		validKeys.add(Constants.HouseBase.PUBLISH_STATUS_RENT);
		checkIfValid(status, validKeys, keyName);
	}

	/**
	 * 校验数字非负
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkNonNegativeNumber(long number, String keyName) {
		if (number < 0) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}

	/**
	 * 校验数字非负
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkNonNegativeNumber(double number, String keyName) {
		if (number < 0) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}

	/**
	 * 校验面积
	 *
	 * @param position
	 * @param keyName
	 */
	private void checkArea(float number, String keyName, int minArea) {
		if (number < minArea) {
			throw new InvalidParameterException("参数异常:" + keyName + ",面积过小");
		}
	}

	/**
	 * 校验电话
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkPhone(String phone, String keyName) {
		if (StringUtil.isNotBlank(phone)) {
			// boolean valid = RulesVerifyUtil.verifyPhone(phone);
			boolean valid = RulesVerifyUtil.verifyCsMaxLength(phone, Constants.HouseBase.HOUSE_PHONE_LENGTH_MAX); // 暂时只校验长度
			if (!valid) {
				throw new InvalidParameterException("参数异常:" + keyName);
			}
		}
	}

	/**
	 * 校验是否有钥匙
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkHaskey(int haskey, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.HAS_KEY_NO);
		validKeys.add(Constants.HouseBase.HAS_KEY_YES);
		checkIfValid(haskey, validKeys, keyName);
	}

	/**
	 * 校验性别
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkGender(int gender, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.GENDER_MALE);
		validKeys.add(Constants.HouseBase.GENDER_FEMALE);
		validKeys.add(Constants.HouseBase.GENDER_INIT);
		checkIfValid(gender, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkOrientation(int orientation, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.ORIENTATION_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_WEST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_SOUTH_EAST);
		validKeys.add(Constants.HouseBase.ORIENTATION_NORTH_SOUTH);
		validKeys.add(Constants.HouseBase.ORIENTATION_EAST_WEST);
		checkIfValid(orientation, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkBuildingType(int buildingType, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_TOWER);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB_AND_TOWER);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_SINGLE);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_ROW);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_OVERLAY);
		validKeys.add(Constants.HouseBase.BUILDING_TYPE_INIT);
		checkIfValid(buildingType, validKeys, keyName);
	}

	/**
	 * 校验朝向
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkDecoration(int decoration, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.DECORATION_FINE);
		validKeys.add(Constants.HouseBase.DECORATION_BRIEF);
		validKeys.add(Constants.HouseBase.DECORATION_ROUGH);
		validKeys.add(Constants.HouseBase.DECORATION_OLD);
		validKeys.add(Constants.HouseBase.DECORATION_LUXURY);
		validKeys.add(Constants.HouseBase.DECORATION_MEDIUM);
		validKeys.add(Constants.HouseBase.DECORATION_NORMAL);
		validKeys.add(Constants.HouseBase.DECORATION_INIT);
		checkIfValid(decoration, validKeys, keyName);
	}

	/**
	 * 校验是否独立
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkIndependent(int independent, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.INDEPENDENT);
		validKeys.add(Constants.HouseBase.NOT_INDEPENDENT);
		checkIfValid(independent, validKeys, keyName);
	}

	/**
	 * 校验家财险
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkInsurance(int insurance, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseBase.INSURANCE_EXIST);
		validKeys.add(Constants.HouseBase.INSURANCE_NOT_EXIST);
		checkIfValid(insurance, validKeys, keyName);
	}

	/**
	 * 校验入住时间
	 *
	 * @param date
	 */
	private void checkCheckIn(Date date, String keyName) {
		if (date != null) {
			Date curDate = new Date();
			if (date.before(curDate) && !DateUtil.isSameDay(date, curDate)) {
				throw new InvalidParameterException("参数异常:" + keyName + ",入住时间不能早于今天");
			}
		}
	}

	/**
	 * 校验建筑年份
	 *
	 * @param date
	 */
	private void checkBuildingYear(String buildingYear, String keyName) {
		if (StringUtil.isNotBlank(buildingYear)) {
			Date year = DateUtil.parseYear(buildingYear);
			if (year == null) {
				throw new InvalidParameterException("参数异常:" + keyName + ",年份解析失败");
			}
		}
	}

	/**
	 * 校验整分租
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkEntireRent(int entireRent, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.HouseDetail.RENT_TYPE_SHARE);
		validKeys.add(Constants.HouseDetail.RENT_TYPE_ENTIRE);
		validKeys.add(Constants.HouseDetail.RENT_TYPE_BOTH);
		validKeys.add(Constants.HouseDetail.RENT_TYPE_ALL);
		checkIfValid(entireRent, validKeys, keyName);
	}
	
	/**
	 * 校验整分租
	 *
	 * @param haskey
	 * @param keyName
	 */
	private void checkLevel(int level, String keyName) {
		List<Integer> validKeys = new ArrayList<Integer>();
		validKeys.add(Constants.MapHouse.LEVEL_BIZ);
		validKeys.add(Constants.MapHouse.LEVEL_DIS);
		validKeys.add(Constants.MapHouse.LEVEL_COMMUNITY);
		validKeys.add(Constants.MapHouse.LEVEL_COMPANY);
		checkIfValid(level, validKeys, keyName);
	}

	/**
	 * 校验参数是否有效
	 *
	 * @param value
	 * @param validValues
	 * @param keyName
	 */
	private void checkIfValid(int value, List<Integer> validValues, String keyName) {
		if (CollectionUtils.isEmpty(validValues)) {
			return;
		}
		if (!validValues.contains(value)) {
			throw new InvalidParameterException("参数异常:" + keyName);
		}
	}


	/**
	 * @Title: searchMapHouseList
	 * @Description: 条件搜索房源数据
	 * @return Responses
	 * @author changmingwei
	 * @throws Exception 
	 * @dateTime 2017年10月09日 下午4:07:37
	 */
	private Responses searchMapHouseList(MapHouseSearchDto mapHouseSearchDto) throws Exception {
		
		List<String> agencyIdList = new ArrayList<String>();
		// 公司筛选条件为全部
		if(mapHouseSearchDto.getCompanyId().equals(Constants.HouseDetail.COMPANY_TYPE_ALL)  ||  StringUtil.isNotBlank(mapHouseSearchDto.getCompanyType())){
			//获取品牌公寓
			List<Agency> agencyList = agencyManageRepository.findAgenciesByCityId(mapHouseSearchDto.getCityId());
			if (CollectionUtils.isNotEmpty(agencyList)) {
				for (Agency agency : agencyList) {
					if (!agencyIdList.contains(agency.getCompanyId())) {
						agencyIdList.add(agency.getCompanyId());
					}
				}
			}
		}
		
		//通勤找房计算周边范围
		if(mapHouseSearchDto.getCommuteId() > Constants.CommuteMapConfig.COMMUTE_ID_INIT){
			CommuteMapConfig commuteMapConfig = commuteMapConfigRepository.findCommuteMapConfigById(mapHouseSearchDto.getCommuteId());
			if(commuteMapConfig !=null ){
				 
				BigDecimal time = new BigDecimal(commuteMapConfig.getTime());//出行时间/分钟
				BigDecimal speed = new BigDecimal(commuteMapConfig.getTravelTypeSpeed());//速度/每小时
				BigDecimal  minute= new BigDecimal(Constants.CommuteMapConfig.MINUTE_60);//60分钟

				BigDecimal  disance= time.multiply(speed).divide(minute,0, BigDecimal.ROUND_HALF_EVEN);//保存0位小数
				mapHouseSearchDto.setDistance(disance.toString());
			}
		}
		
		//搜索房源
		logger.info(LogUtils.getCommLog("地图/通勤找房 请求参数 ,houseSearchDto:" + mapHouseSearchDto));
		MapHouseQueryDto mapHouseQueryDto = mapHouseDao.getHouseResultDto(mapHouseSearchDto, agencyIdList);
		if (mapHouseQueryDto == null) {
			logger.error(LogUtils.getCommLog("地图/通勤找房 结果不存在"));
			return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "地图/通勤找房 失败");
		}
		
		Responses responses = new Responses(mapHouseQueryDto);
		List<MapHouseInfo> mapHouseInfoList = mapHouseQueryDto.getMapList();
		if (CollectionUtils.isNotEmpty(mapHouseInfoList)) {
			ResponseUtils.fillRow(responses.getMeta(), mapHouseInfoList.size(), mapHouseInfoList.size());
		}
		return responses;
	}


	/**
	 * @Title: getGlobalConf
	 * @Description: 获取全局配置
	 * @return Responses
	 * @author 李强
	 * @dateTime 2017年8月29日 上午10:44:59
	 */
	public Responses getGlobalConf(HttpServletRequest request) throws Exception {
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("agreementPath", configuration.agreementPath);
		returnMap.put("privateAgreementPath", configuration.privateAgreementPath);
		responses.setBody(returnMap);
		return responses;
	}
}
