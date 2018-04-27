/**
 * Project Name: hzf_platform
 * File Name: HouseRequestHandler.java
 * Package Name: com.huifenqi.hzf_platform.handler
 * Date: 2016年4月26日下午4:40:45
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved.
 *
 */
package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.IdealRentHouseSearchDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.IdealRentHouseDao;
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
public class IdealRentHouseRequestHandler {

	private static final Log logger = LogFactory.getLog(IdealRentHouseRequestHandler.class);

	@Autowired
	private IdealRentHouseDao idealRentHouseDao;

	
	@Autowired
	private Configuration configuration;

	
	
	/**
	 * 理想生活圈
	 * 
	 * @return
	 */
	public Responses searchIdealRentHouse(HttpServletRequest request) throws Exception {
		
		//封装请求参数
		IdealRentHouseSearchDto idealRentHouseSearchDto = getIdealRentHouseSearchDto(request);
		
		//获取理想生活圈数据
		return searchMapHouseList(idealRentHouseSearchDto);
	}

	private IdealRentHouseSearchDto getIdealRentHouseSearchDto(HttpServletRequest request) throws Exception {

		if (request == null) {
			return null;
		}

		IdealRentHouseSearchDto idealRentHouseSearchDto = new IdealRentHouseSearchDto();

		int appId = RequestUtils.getParameterInt(request, "appId");
		idealRentHouseSearchDto.setAppId(appId);

		long cityId = RequestUtils.getParameterLong(request, "cityId", 0);
		if (cityId != 0) {
			idealRentHouseSearchDto.setCityId(cityId);
		}

		String keyword = RequestUtils.getParameterString(request, "q", StringUtil.EMPTY);
		idealRentHouseSearchDto.setKeyword(keyword);
	
		String price = RequestUtils.getParameterString(request, "price", StringUtil.EMPTY);
		checkRegionNumber(price, "price");
		idealRentHouseSearchDto.setPrice(price);
		
		//整租1居室
		String cBedRoomNums = RequestUtils.getParameterString(request, "cBedRoomNums", StringUtil.EMPTY);
		idealRentHouseSearchDto.setcBedRoomNums(cBedRoomNums);

	

		String location = RequestUtils.getParameterString(request, "location", StringUtil.EMPTY);
		checkPosition(location, "location");
		idealRentHouseSearchDto.setLocation(location);

		String distance = RequestUtils.getParameterString(request, "distance", StringUtil.EMPTY);
		//checkRegionNumber(distance, "distance");
		idealRentHouseSearchDto.setDistance(distance);

	

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
		idealRentHouseSearchDto.setEntireRent(entireRent);

		
		return idealRentHouseSearchDto;

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
	private Responses searchMapHouseList(IdealRentHouseSearchDto idealRentHouseSearchDto) throws Exception {
		
		//搜索房源
		logger.info(LogUtils.getCommLog("理想生活圈 ,houseSearchDto:" + idealRentHouseSearchDto));
		HouseSearchResultDto houseSearchResultDto = idealRentHouseDao.getHouseResultDto(idealRentHouseSearchDto);

		if (houseSearchResultDto == null) {
			logger.error(LogUtils.getCommLog("搜索房源结果不存在"));
			return new Responses(ErrorMsgCode.ERROR_MSG_SEARCH_HOUSE_FAIL, "搜索房源失败");
		}
		
		Responses responses = new Responses(houseSearchResultDto);
		List<HouseSearchResultInfo> searchHouses = houseSearchResultDto.getSearchHouses();

		if (CollectionUtils.isNotEmpty(searchHouses)) {
			for (HouseSearchResultInfo info : searchHouses) {
				// 推荐房源列表页显示小图 ，2017年06月23日19:07:25 jjs
				if (StringUtils.isNotEmpty(info.getPic())) {
					String pic = info.getPic() + "?x-oss-process=image/resize,h_240";
					info.setPic(pic);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(searchHouses)) {
			ResponseUtils.fillRow(responses.getMeta(), searchHouses.size(), searchHouses.size());
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
