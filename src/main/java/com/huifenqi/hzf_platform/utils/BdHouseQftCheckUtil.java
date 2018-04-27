package com.huifenqi.hzf_platform.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.huifenqi.hzf_platform.context.BdConstantsEnum;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDtoQft;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;

/**
 * @Description: 百度接口房源字段校验util
 * @Author chenshuai
 * @Date 2017/4/14 0014 17:43
 */
public class BdHouseQftCheckUtil {

    public static final String DEFAULT_ENUM_STR = "-1";
    public static final int DEFAULT_ENUM_INT = -1;
    public static final int DEFAULT_INT = 0;
    private static final boolean IS_MUST_ENUM = true; // 字段必填
    private static final boolean NOT_MUST_ENUM = false; // 字段非必填

	public static BdHousePublishDtoQft getHousePublishDtoUpdate(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

        Map<String, String> map = BdRequestUtils.handlerDataToMap(request);

		BdHousePublishDtoQft housePublishDto = new BdHousePublishDtoQft();

        checkUpdate(request, housePublishDto, map);

        return housePublishDto;
    }

	public static BdHousePublishDtoQft getHousePublishDtoStatus(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

        Map<String, String> map = BdRequestUtils.handlerDataToMap(request);

		BdHousePublishDtoQft housePublishDto = new BdHousePublishDtoQft();

        // 分配给合作公寓的接入ID
        String appIdKey = "appId";
        String appId = BdRequestUtils.getParameterString(map, appIdKey);
        housePublishDto.setAppId(appId);

        // 合作公寓的房屋ID
        String outHouseIdKey = "houseId";
        String outHouseId = BdRequestUtils.getParameterString(map, outHouseIdKey);
        housePublishDto.setOutHouseId(outHouseId);

        // ==========================

        /**
         * 改动原因
         */
        String memoKey = "memo";
        String memo = BdRequestUtils.getParameterString(map, memoKey);
        housePublishDto.setMemo(memo);

        /**
         * 房源状态，4000上架  5000下架
         */
        String statusKey = "status";
        String status = BdRequestUtils.getParameterString(map, statusKey);
        checkEnumValue(BdConstantsEnum.StatusEnum.checkIndexExist(status, IS_MUST_ENUM), statusKey);
        housePublishDto.setStatus(status);

        return housePublishDto;
    }


	public static BdHousePublishDtoQft getHousePublishDtoAdd(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

        Map<String, String> map = BdRequestUtils.handlerDataToMap(request);

		BdHousePublishDtoQft housePublishDto = new BdHousePublishDtoQft();

        checkCommons(request, housePublishDto, map);

        // 图片list
        String picUrlListKey = "picUrlList";
		String picUrlList = BdRequestUtils.getParameterString(map, picUrlListKey, StringUtil.EMPTY);
		checkPicUrlSize(picUrlList, picUrlListKey);
        housePublishDto.setPicUrlList(picUrlList);

        // 房屋所在省
        String provinceKey = "province";
		String province = BdRequestUtils.getParameterString(map, provinceKey, StringUtil.EMPTY);
        housePublishDto.setProvince(province);

        // 房屋所在城市
        String cityNameKey = "cityName";
        String cityName = BdRequestUtils.getParameterString(map, cityNameKey);
        housePublishDto.setCityName(cityName);

        // 房屋所在区县
        String countyNameKey = "countyName";
        String countyName = BdRequestUtils.getParameterString(map, countyNameKey);
        housePublishDto.setCountyName(countyName);

        //房屋所在商圈(否)
        String areaNameKey = "areaName";
        String areaName = BdRequestUtils.getParameterString(map, areaNameKey, StringUtil.EMPTY);
        housePublishDto.setAreaName(areaName);

        // 小区名称
        String districtNameKey = "districtName";
        String districtName = BdRequestUtils.getParameterString(map, districtNameKey);
        checkStringMaxLength(districtName, districtNameKey, Constants.HouseDetail.HOUSE_COMMUNITY_NAME_LENGTH_MAX);
        housePublishDto.setDistrictName(districtName);

        // 房屋所在小区详细地址 北京市昌平区蓝天家园
        String streetKey = "street";
		String street = BdRequestUtils.getParameterString(map, streetKey, StringUtil.EMPTY);
        housePublishDto.setStreet(street);

        // 出租房屋门牌地址 1号楼3单元208室
        String addressKey = "address";
        String address = BdRequestUtils.getParameterString(map, addressKey);
        housePublishDto.setAddress(address);

        // 房屋附近地铁站所在线路名称 1号线(否)
        String subwayLineKey = "subwayLine";
        String subwayLine = BdRequestUtils.getParameterString(map, subwayLineKey, StringUtil.EMPTY);
        housePublishDto.setSubwayLine(subwayLine);

        // 房屋附近地铁站名 北京南站(否)
        String subwayStationKey = "subwayStation";
        String subwayStation = BdRequestUtils.getParameterString(map, subwayStationKey, StringUtil.EMPTY);
        housePublishDto.setSubwayStation(subwayStation);

        // 房间描述
        String houseDescKey = "houseDesc";
		String houseDesc = BdRequestUtils.getParameterString(map, houseDescKey, StringUtil.EMPTY);
        housePublishDto.setHouseDesc(houseDesc);

        //坐标经度
        String xCoordKey = "xCoord";
        String xCoord = BdRequestUtils.getParameterString(map, xCoordKey);
		// checkPositionValue(xCoord, xCoordKey);
        checkLongitude(xCoord, xCoordKey);
        housePublishDto.setxCoord(xCoord);
        
        //坐标纬度
        String yCoordKey = "yCoord";
        String yCoord = BdRequestUtils.getParameterString(map, yCoordKey);
		// checkPositionValue(yCoord, yCoordKey);
        checkLatitude(yCoord, yCoordKey);
        housePublishDto.setyCoord(yCoord);

        // 合作公寓系统房屋视频url(否)
        String videoUrlKey = "videoUrl";
        String videoUrl = BdRequestUtils.getParameterString(map, videoUrlKey, StringUtil.EMPTY);
		housePublishDto.setVideoUrl(videoUrl);
        
        
		// 公司ID(否)
		String liveBedTotileKey = "liveBedTotile";
		String liveBedTotile = BdRequestUtils.getParameterString(map, liveBedTotileKey, StringUtil.EMPTY);
		housePublishDto.setLiveBedTotile(liveBedTotile);

        // 公司ID(否)
        String companyIdKey = "companyId";
        String companyId = BdRequestUtils.getParameterString(map, companyIdKey, StringUtil.EMPTY);
		housePublishDto.setCompanyId(companyId);
        
        // 公司名称(否)
        String companyNameKey = "companyName";
        String companyName = BdRequestUtils.getParameterString(map, companyNameKey, StringUtil.EMPTY);
		housePublishDto.setCompanyName(companyName);

        // 装修状态(否)
		String fitmentStateKey = "fitmentState";
		String fitmentState = BdRequestUtils.getParameterString(map, fitmentStateKey, StringUtil.EMPTY);
		housePublishDto.setFitmentState(fitmentState);

        return housePublishDto;
    }


	private static void checkCommons(HttpServletRequest request, BdHousePublishDtoQft housePublishDto,
			Map<String, String> map) throws Exception {

        // 分配给合作公寓的接入ID
        String appIdKey = "appId";
        String appId = BdRequestUtils.getParameterString(map, appIdKey);
        housePublishDto.setAppId(appId);

        // 合作公寓的房屋ID
        String outHouseIdKey = "outHouseId";
        String outHouseId = BdRequestUtils.getParameterString(map, outHouseIdKey);
        checkStringMaxLength(outHouseId, outHouseIdKey, Constants.HouseDetail.HOUSE_HOUSEID_LENGTH_MAX);
        housePublishDto.setOutHouseId(outHouseId);


        // 出租方式
        String rentTypeKey = "rentType";
        int rentType = BdRequestUtils.getParameterInt(map, rentTypeKey);
        checkEnumValue(BdConstantsEnum.RentTypeEnum.checkIndexExist(rentType, IS_MUST_ENUM), rentTypeKey);
        housePublishDto.setRentType(rentType);

		// 居室数量
        String bedRoomNumKey = "bedRoomNum";
		int bedRoomNum = BdRequestUtils.getParameterInt(map, bedRoomNumKey, 0);
        checkNonNegativeNumber(bedRoomNum, bedRoomNumKey);
        housePublishDto.setBedRoomNum(bedRoomNum);

        // 起居室数量
        String livingRoomNumKey = "livingRoomNum";
		int livingRoomNum = BdRequestUtils.getParameterInt(map, livingRoomNumKey, 0);
        checkNonNegativeNumber(livingRoomNum, livingRoomNumKey);
        housePublishDto.setLivingRoomNum(livingRoomNum);

        // 卫生间数量
        String toiletNumKey = "toiletNum";
		int toiletNum = BdRequestUtils.getParameterInt(map, toiletNumKey, 0);
        checkNonNegativeNumber(toiletNum, toiletNumKey);
        housePublishDto.setToiletNum(toiletNum);

        // 面积
        String rentRoomAreaKey = "rentRoomArea";
        float rentRoomArea = BdRequestUtils.getParameterFloat(map, rentRoomAreaKey);
		// checkNonNegativeNumber(rentRoomArea, rentRoomAreaKey);
        //checkArea(rentRoomArea, rentRoomAreaKey, Constants.HouseDetail.HOUSE_AREA_MIN);
        housePublishDto.setRentRoomArea(rentRoomArea);

        if(rentType == Constants.BdHouseDetail.RENT_TYPE_SHARE){
            // 出租屋类型
            String bedRoomTypeKey = "bedRoomType";
			String bedRoomType = BdRequestUtils.getParameterString(map, bedRoomTypeKey, "0");
			// checkEnumValue(BdConstantsEnum.BedRoomTypeEnum.checkIndexExist(bedRoomType,
			// IS_MUST_ENUM), bedRoomTypeKey);
            housePublishDto.setBedRoomType(bedRoomType);
        }


        // 房间名称(否)
        String roomNameKey = "roomName";
        String roomName = BdRequestUtils.getParameterString(map, roomNameKey, StringUtil.EMPTY);
        checkStringMaxLength(roomName, roomNameKey, Constants.HouseDetail.HOUSE_ROOMNAME_LENGTH_MAX);
        housePublishDto.setRoomName(roomName);

        // 房间编码(否)
        String roomCodeKey = "roomCode";
        String roomCode = BdRequestUtils.getParameterString(map, roomCodeKey, StringUtil.EMPTY);
        checkStringMaxLength(roomCode, roomCodeKey, Constants.HouseDetail.HOUSE_ROOMCODE_LENGTH_MAX);
        housePublishDto.setRoomCode(roomCode);

        // 朝向
        String faceToTypeKey = "faceToType";
		String faceToType = BdRequestUtils.getParameterString(map, faceToTypeKey, StringUtil.EMPTY);
		// checkEnumValue(BdConstantsEnum.FaceToTypeEnum.checkIndexExist(faceToType,
		// IS_MUST_ENUM), faceToTypeKey);
        housePublishDto.setFaceToType(faceToType);

        // 总楼层数量
        String totalFloorKey = "totalFloor";
		int totalFloor = BdRequestUtils.getParameterInt(map, totalFloorKey, 0);
        checkNonNegativeNumber(totalFloor, totalFloorKey);
        housePublishDto.setTotalFloor(totalFloor);

        // 所在楼层，物理楼层
        String houseFloorKey = "houseFloor";
		int houseFloor = BdRequestUtils.getParameterInt(map, houseFloorKey, 0);
        checkNonNegativeNumber(houseFloor, houseFloorKey);
        housePublishDto.setHouseFloor(houseFloor);

        // 房间标签，枚举值，可多选，以逗号分隔
        String featureTagKey = "featureTag";
		String featureTag = BdRequestUtils.getParameterString(map, featureTagKey, StringUtil.EMPTY);
		// checkEnumValue(BdConstantsEnum.FeatureTagEnum.checkIndexExist(featureTag,
		// IS_MUST_ENUM), featureTagKey);
        housePublishDto.setFeatureTag(featureTag);

        // 房屋配置，枚举值，可多选，以逗号分隔
        String detailPointKey = "detailPoint";
        String detailPoint = BdRequestUtils.getParameterString(map, detailPointKey);
		// checkEnumValue(BdConstantsEnum.DetailPointEnum.checkIndexExist(detailPoint,
		// IS_MUST_ENUM), detailPointKey);
        housePublishDto.setDetailPoint(detailPoint);

        // 公寓配套服务，枚举值，可多选，以逗号分隔(否)
        String servicePointKey = "servicePoint";
        String servicePoint = BdRequestUtils.getParameterString(map, servicePointKey, StringUtil.EMPTY);
        checkEnumValue(BdConstantsEnum.ServicePointEnum.checkIndexExist(servicePoint, NOT_MUST_ENUM), servicePointKey);
        housePublishDto.setServicePoint(servicePoint);

        // 租金月租金，单位为分
        String monthRentKey = "monthRent";
		int monthRent = BdRequestUtils.getParameterInt(map, monthRentKey, 0);
		// checkNonNegativeNumber(monthRent, monthRentKey);
        housePublishDto.setMonthRent(monthRent);

        //入住时间
        String rentStartDateKey = "rentStartDate";
        Date rentStartDate = BdRequestUtils.getParameterDate(map, rentStartDateKey);
        checkCheckIn(rentStartDate, rentStartDateKey);
        housePublishDto.setRentStartDate(rentStartDate);

        // 是否支持短租，枚举值0不支持  1支持
        String shortRentKey = "shortRent";
		int shortRent = BdRequestUtils.getParameterInt(map, shortRentKey, 0);
		// checkEnumValue(BdConstantsEnum.ShortRentEnum.checkIndexExist(shortRent,
		// IS_MUST_ENUM), shortRentKey);
        housePublishDto.setShortRent(shortRent);

        //经纪人电话
		String agentPhone = BdRequestUtils.getParameterString(map, "agentPhone", StringUtil.EMPTY);
        //checkPhone(agentPhone, "agentPhone");
        housePublishDto.setAgentPhone(agentPhone);

        // 预约短信接收到的手机号(否)
        String orderPhoneKey = "orderPhone";
        String orderPhone = BdRequestUtils.getParameterString(map, orderPhoneKey, StringUtil.EMPTY);
        housePublishDto.setOrderPhone(orderPhone);

        //经纪人姓名
        String agentNameKey = "agentName";
		String agentName = BdRequestUtils.getParameterString(map, agentNameKey, StringUtil.EMPTY);
		// checkStringMaxLength(agentName, agentNameKey,
		// Constants.HouseBase.HOUSE_AGENCY_NAME_LENGTH_MAX);
        housePublishDto.setAgentName(agentName);

        // 建筑时间(否)
        String buildYearKey = "buildYear";
        int buildYear = BdRequestUtils.getParameterInt(map, buildYearKey, DEFAULT_INT);
        checkNonNegativeNumber(buildYear, buildYearKey);
        housePublishDto.setBuildYear(buildYear);

        // 小区供暖方式，1集中供暖 2自供暖 3无供暖
        String supplyHeatingKey = "supplyHeating";
		int supplyHeating = BdRequestUtils.getParameterInt(map, supplyHeatingKey, 0);
		// checkEnumValue(BdConstantsEnum.SupplyHeatingEnum.checkIndexExist(supplyHeating,
		// IS_MUST_ENUM), supplyHeatingKey);
        housePublishDto.setSupplyHeating(supplyHeating);

        // 小区绿化率，不带百分号(否)
        String greenRatioKey = "greenRatio";
        int greenRatio = BdRequestUtils.getParameterInt(map, greenRatioKey, DEFAULT_INT);
        checkNonNegativeNumber(greenRatio, greenRatioKey);
        housePublishDto.setGreenRatio(greenRatio);

        // 建筑类型(否)
        String buildTypeKey = "buildType";
        int buildType = BdRequestUtils.getParameterInt(map, buildTypeKey, DEFAULT_ENUM_INT);
        checkEnumValue(BdConstantsEnum.BuildTypeEnum.checkIndexExist(buildType, NOT_MUST_ENUM), buildTypeKey);
        housePublishDto.setBuildType(buildType);

    }

	private static void checkUpdate(HttpServletRequest request, BdHousePublishDtoQft housePublishDto,
			Map<String, String> map) throws Exception {
		

        // 分配给合作公寓的接入ID
        String appIdKey = "appId";
        String appId = BdRequestUtils.getParameterString(map, appIdKey);
        housePublishDto.setAppId(appId);

        // 合作公寓的房屋ID
        String outHouseIdKey = "outHouseId";
        String outHouseId = BdRequestUtils.getParameterString(map, outHouseIdKey);
        housePublishDto.setOutHouseId(outHouseId);

		// 图片list
		String picUrlListKey = "picUrlList";
		if (!checkParameterIsNull(map, picUrlListKey)) {
			String picUrlList = BdRequestUtils.getParameterString(map, picUrlListKey, StringUtil.EMPTY);
			checkPicUrlSize(picUrlList, picUrlListKey);
			housePublishDto.setPicUrlList(picUrlList);
		}
        // 出租方式
        String rentTypeKey = "rentType";
        if (!checkParameterIsNull(map, rentTypeKey)) {
            int rentType = BdRequestUtils.getParameterInt(map, rentTypeKey);
            checkEnumValue(BdConstantsEnum.RentTypeEnum.checkIndexExist(rentType, IS_MUST_ENUM), rentTypeKey);
            housePublishDto.setRentType(rentType);
        }

        // 居室数量
        String bedRoomNumKey = "bedRoomNum";
        if (!checkParameterIsNull(map, bedRoomNumKey)) {
            int bedRoomNum = BdRequestUtils.getParameterInt(map, bedRoomNumKey);
            checkNonNegativeNumber(bedRoomNum, bedRoomNumKey);
            housePublishDto.setBedRoomNum(bedRoomNum);
        }

        // 起居室数量
        String livingRoomNumKey = "livingRoomNum";
        if (!checkParameterIsNull(map, livingRoomNumKey)) {
            int livingRoomNum = BdRequestUtils.getParameterInt(map, livingRoomNumKey);
            checkNonNegativeNumber(livingRoomNum, livingRoomNumKey);
            housePublishDto.setLivingRoomNum(livingRoomNum);
        }

        // 卫生间数量
        String toiletNumKey = "toiletNum";
        if (!checkParameterIsNull(map, toiletNumKey)) {
            int toiletNum = BdRequestUtils.getParameterInt(map, toiletNumKey);
            checkNonNegativeNumber(toiletNum, toiletNumKey);
            housePublishDto.setToiletNum(toiletNum);
        }

        // 面积
        String rentRoomAreaKey = "rentRoomArea";
        if (!checkParameterIsNull(map, rentRoomAreaKey)) {
            float rentRoomArea = BdRequestUtils.getParameterFloat(map, rentRoomAreaKey);
			// checkNonNegativeNumber(rentRoomArea, rentRoomAreaKey);
			// checkArea(rentRoomArea, rentRoomAreaKey,
			// Constants.HouseDetail.HOUSE_AREA_MIN);
            housePublishDto.setRentRoomArea(rentRoomArea);
        }

        // 出租屋类型
        String bedRoomTypeKey = "bedRoomType";
        if (!checkParameterIsNull(map, bedRoomTypeKey)) {
			String bedRoomType = BdRequestUtils.getParameterString(map, bedRoomTypeKey);
			// checkEnumValue(BdConstantsEnum.BedRoomTypeEnum.checkIndexExist(bedRoomType,
			// IS_MUST_ENUM), bedRoomTypeKey);
            housePublishDto.setBedRoomType(bedRoomType);
        }

        // 房间名称(否)
        String roomNameKey = "roomName";
        if (!checkParameterIsNull(map, roomNameKey)) {
            String roomName = BdRequestUtils.getParameterString(map, roomNameKey);
            checkStringMaxLength(roomName, roomNameKey, Constants.HouseDetail.HOUSE_ROOMNAME_LENGTH_MAX);
            housePublishDto.setRoomName(roomName);
        }

        // 房间编码(否)
        String roomCodeKey = "roomCode";
        if (!checkParameterIsNull(map, roomCodeKey)) {
            String roomCode = BdRequestUtils.getParameterString(map, roomCodeKey);
            checkStringMaxLength(roomCode, roomCodeKey, Constants.HouseDetail.HOUSE_ROOMCODE_LENGTH_MAX);
            housePublishDto.setRoomCode(roomCode);
        }

        // 朝向
        String faceToTypeKey = "faceToType";
        if (!checkParameterIsNull(map, faceToTypeKey)) {
			String faceToType = BdRequestUtils.getParameterString(map, faceToTypeKey);
			// checkEnumValue(BdConstantsEnum.FaceToTypeEnum.checkIndexExist(faceToType,
			// IS_MUST_ENUM), faceToTypeKey);
            housePublishDto.setFaceToType(faceToType);
        }

        // 总楼层数量
        String totalFloorKey = "totalFloor";
        if (!checkParameterIsNull(map, totalFloorKey)) {
            int totalFloor = BdRequestUtils.getParameterInt(map, totalFloorKey);
            checkNonNegativeNumber(totalFloor, totalFloorKey);
            housePublishDto.setTotalFloor(totalFloor);
        }

        // 所在楼层，物理楼层
        String houseFloorKey = "houseFloor";
        if (!checkParameterIsNull(map, houseFloorKey)) {
            int houseFloor = BdRequestUtils.getParameterInt(map, houseFloorKey);
            checkNonNegativeNumber(houseFloor, houseFloorKey);
            housePublishDto.setHouseFloor(houseFloor);
        }

        // 房间标签，枚举值，可多选，以逗号分隔
        String featureTagKey = "featureTag";
        if (!checkParameterIsNull(map, featureTagKey)) {
            String featureTag = BdRequestUtils.getParameterString(map, featureTagKey);
            checkEnumValue(BdConstantsEnum.FeatureTagEnum.checkIndexExist(featureTag, IS_MUST_ENUM), featureTagKey);
            housePublishDto.setFeatureTag(featureTag);
        }

        // 房屋配置，枚举值，可多选，以逗号分隔
        String detailPointKey = "detailPoint";
        if (!checkParameterIsNull(map, detailPointKey)) {
            String detailPoint = BdRequestUtils.getParameterString(map, detailPointKey);
			// checkEnumValue(BdConstantsEnum.DetailPointEnum.checkIndexExist(detailPoint,
			// IS_MUST_ENUM), detailPointKey);
            housePublishDto.setDetailPoint(detailPoint);
        }

        // 公寓配套服务，枚举值，可多选，以逗号分隔(否)
        String servicePointKey = "servicePoint";
        if (!checkParameterIsNull(map, servicePointKey)) {
            String servicePoint = BdRequestUtils.getParameterString(map, servicePointKey);
            checkEnumValue(BdConstantsEnum.ServicePointEnum.checkIndexExist(servicePoint, NOT_MUST_ENUM), servicePointKey);
            housePublishDto.setServicePoint(servicePoint);
        }

        // 租金月租金，单位为分
        String monthRentKey = "monthRent";
        if (!checkParameterIsNull(map, monthRentKey)) {
            int monthRent = BdRequestUtils.getParameterInt(map, monthRentKey);
            checkNonNegativeNumber(monthRent, monthRentKey);
            housePublishDto.setMonthRent(monthRent);
        }

        //入住时间
        String rentStartDateKey = "rentStartDate";
        if (!checkParameterIsNull(map, rentStartDateKey)) {
            Date rentStartDate = BdRequestUtils.getParameterDate(map, rentStartDateKey);
            //checkCheckIn(rentStartDate, rentStartDateKey);
            housePublishDto.setRentStartDate(rentStartDate);
        }

        // 是否支持短租，枚举值0不支持  1支持
        String shortRentKey = "shortRent";
        if (!checkParameterIsNull(map, shortRentKey)) {
            int shortRent = BdRequestUtils.getParameterInt(map, shortRentKey);
            checkEnumValue(BdConstantsEnum.ShortRentEnum.checkIndexExist(shortRent, IS_MUST_ENUM), shortRentKey);
            housePublishDto.setShortRent(shortRent);
        }
        
        // 城市
        String cityNameKey = "cityName";
        if (!checkParameterIsNull(map, cityNameKey)) {
            String cityName =BdRequestUtils.getParameterString(map, cityNameKey);
            housePublishDto.setCityName(cityName);
        }
        
        // 行政区
        String countyNameKey = "countyName";
        if (!checkParameterIsNull(map, countyNameKey)) {
            String countyName =BdRequestUtils.getParameterString(map, countyNameKey);
            housePublishDto.setCountyName(countyName);
        }
        
        // 小区名称
        String districtNameKey = "districtName";
        if (!checkParameterIsNull(map, districtNameKey)) {
            String districtName =BdRequestUtils.getParameterString(map, districtNameKey);
            housePublishDto.setDistrictName(districtName);
        }
        
		// 门牌地址
		String addressKey = "address";
		if (!checkParameterIsNull(map, addressKey)) {
			String address = BdRequestUtils.getParameterString(map, addressKey);
			housePublishDto.setAddress(address);
		}

		// 经度
		String xCoordKey = "xCoord";
		if (!checkParameterIsNull(map, xCoordKey)) {
			String xCoord = BdRequestUtils.getParameterString(map, xCoordKey);
			housePublishDto.setxCoord(xCoord);
		}

		// 纬度
		String yCoordKey = "yCoord";
		if (!checkParameterIsNull(map, yCoordKey)) {
			String yCoord = BdRequestUtils.getParameterString(map, yCoordKey);
			housePublishDto.setyCoord(yCoord);
		}


        //经纪人电话
        String agentPhoneKey = "agentPhone";
        if (!checkParameterIsNull(map, agentPhoneKey)) {
            String agentPhone = BdRequestUtils.getParameterString(map, agentPhoneKey);
            //checkPhone(agentPhone, agentPhoneKey);
            housePublishDto.setAgentPhone(agentPhone);
        }

        // 预约短信接收到的手机号(否)
        String orderPhoneKey = "orderPhone";
        if (!checkParameterIsNull(map, orderPhoneKey)) {
            String orderPhone = BdRequestUtils.getParameterString(map, orderPhoneKey);
            //checkOrderPhone(orderPhone, orderPhoneKey);
            housePublishDto.setOrderPhone(orderPhone);
        }

        //经纪人姓名
        String agentNameKey = "agentName";
        if (!checkParameterIsNull(map, agentNameKey)) {
            String agentName = BdRequestUtils.getParameterString(map, agentNameKey);
            checkStringMaxLength(agentName, agentNameKey, Constants.HouseBase.HOUSE_AGENCY_NAME_LENGTH_MAX);
            housePublishDto.setAgentName(agentName);
        }

        // 建筑时间(否)
        String buildYearKey = "buildYear";
        if (!checkParameterIsNull(map, buildYearKey)) {
            int buildYear = BdRequestUtils.getParameterInt(map, buildYearKey, DEFAULT_INT);
            checkNonNegativeNumber(buildYear, buildYearKey);
            housePublishDto.setBuildYear(buildYear);
        }

        // 小区供暖方式，1集中供暖 2自供暖 3无供暖
        String supplyHeatingKey = "supplyHeating";
        if (!checkParameterIsNull(map, supplyHeatingKey)) {
            int supplyHeating = BdRequestUtils.getParameterInt(map, supplyHeatingKey);
            checkEnumValue(BdConstantsEnum.SupplyHeatingEnum.checkIndexExist(supplyHeating, IS_MUST_ENUM), supplyHeatingKey);
            housePublishDto.setSupplyHeating(supplyHeating);
        }

        // 小区绿化率，不带百分号(否)
        String greenRatioKey = "greenRatio";
        if (!checkParameterIsNull(map, greenRatioKey)) {
            int greenRatio = BdRequestUtils.getParameterInt(map, greenRatioKey, DEFAULT_INT);
            checkNonNegativeNumber(greenRatio, greenRatioKey);
            housePublishDto.setGreenRatio(greenRatio);
        }

        // 建筑类型(否)
        String buildTypeKey = "buildType";
        if (!checkParameterIsNull(map, buildTypeKey)) {
            int buildType = BdRequestUtils.getParameterInt(map, buildTypeKey, DEFAULT_ENUM_INT);
            checkEnumValue(BdConstantsEnum.BuildTypeEnum.checkIndexExist(buildType, NOT_MUST_ENUM), buildTypeKey);
            housePublishDto.setBuildType(buildType);
        }
       // 房源描述(否)
        String houseDescKey = "houseDesc";
        if (!checkParameterIsNull(map, houseDescKey)) {
        	String houseDesc = BdRequestUtils.getParameterString(map, houseDescKey);
            housePublishDto.setHouseDesc(houseDesc);
        }

		// 公司ID(否)
		String liveBedTotileKey = "liveBedTotile";
		if (!checkParameterIsNull(map, liveBedTotileKey)) {
			String liveBedTotile = BdRequestUtils.getParameterString(map, liveBedTotileKey);
			housePublishDto.setLiveBedTotile(liveBedTotile);
		}
       
		// 公司ID(否)
		String companyIdKey = "companyId";
		if (!checkParameterIsNull(map, companyIdKey)) {
			String companyId = BdRequestUtils.getParameterString(map, companyIdKey);
			housePublishDto.setCompanyId(companyId);
		}

		// 公司名称(否)

		String companyNameKey = "companyName";
		if (!checkParameterIsNull(map, companyNameKey)) {
			String companyName = BdRequestUtils.getParameterString(map, companyNameKey);
			housePublishDto.setCompanyName(companyName);
		}

		// 装修状态(否)
		String fitmentStateKey = "fitmentState";
		if (!checkParameterIsNull(map, fitmentStateKey)) {
			String fitmentState = BdRequestUtils.getParameterString(map, fitmentStateKey);
			housePublishDto.setFitmentState(fitmentState);
		}

    }

    /**
     * 验证属性字段是否为null   是：true  不是：false
     *
     * @param map
     * @param keyName
     */
    private static boolean checkParameterIsNull(Map<String,String> map, String keyName) {
        String value = map.get(keyName);
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return false;
    }

    /**
     * 验证枚举
     */
    private static void checkEnumValue(boolean isExist, String keyName) {
        if (!isExist) {
            throw new InvalidParameterException("参数不合法:" + keyName);
        }
    }

    private static void checkPicUrlSize(String picUrlList, String keyName) {
        JsonArray jsonArray = null;
        try {
			if (StringUtil.isNotBlank(picUrlList)) {
				jsonArray = new Gson().fromJson(picUrlList, JsonArray.class);
			}
        } catch (JsonSyntaxException e) {
            throw new InvalidParameterException("参数不合法:" + keyName);
        }
		if (jsonArray != null) {
			if (jsonArray.size() > 20) {
				throw new InvalidParameterException("参数不合法:" + keyName + ",图片数量大于20张");
			}
        }

    }

    /**
     * 校验坐标值
     *
     * @param positionValue
     * @param keyName
     */
    private static void checkPositionValue(String positionValue, String keyName) {
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
     * @param positionValue
     * @param keyName
     */
    private static void checkLatitude(String positionValue, String keyName) {
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
     * @param positionValue
     * @param keyName
     */
    private static void checkLongitude(String positionValue, String keyName) {
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
     * @param string
     * @param keyName
     */
    private static void checkStringMaxLength(String string, String keyName, int maxLength) {
        if (StringUtil.isNotBlank(string)) {
            boolean valid = RulesVerifyUtil.verifyCsMaxLength(string, maxLength);
            if (!valid) {
                throw new InvalidParameterException("参数不合法:" + keyName + ",字符串过长");
            }
        }
    }

    /**
     * 校验数字非负
     *
     * @param number
     * @param keyName
     */
    private static void checkNonNegativeNumber(long number, String keyName) {
        if (number < 0) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }
    }

    /**
     * 校验数字非负
     *
     * @param number
     * @param keyName
     */
    private static void checkNonNegativeNumber(double number, String keyName) {
        if (number < 0) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }
    }

    /**
     * 校验面积
     *
     * @param number
     * @param keyName
     */
    private static void checkArea(float number, String keyName, int minArea) {
        if (number < minArea) {
            throw new InvalidParameterException("参数异常:" + keyName + ",面积过小");
        }
    }

    /**
     * 校验电话
     *
     * @param phone
     * @param keyName
     */
    private static void checkPhone(String phone, String keyName) {
        if (StringUtil.isNotBlank(phone)) {
            // boolean valid = RulesVerifyUtil.verifyPhone(phone);
            boolean valid = RulesVerifyUtil.verifyCsMaxLength(phone, Constants.HouseBase.HOUSE_PHONE_LENGTH_MAX); // 暂时只校验长度
            if (!valid) {
                throw new InvalidParameterException("参数异常:" + keyName);
            }
        }
    }

    /**
     * 校验电话
     *
     * @param phone
     * @param keyName
     */
    private static void checkOrderPhone(String phone, String keyName) {
        if (StringUtil.isNotBlank(phone)) {
             boolean valid = RulesVerifyUtil.verifyPhone(phone);
            //boolean valid = RulesVerifyUtil.verifyCsMaxLength(phone, Constants.HouseBase.HOUSE_PHONE_LENGTH_MAX); // 暂时只校验长度
            if (!valid) {
                throw new InvalidParameterException("参数异常:" + keyName);
            }
        }
    }

    /**
     * 校验朝向
     *
     * @param orientation
     * @param keyName
     */
    private static void checkOrientation(int orientation, String keyName) {
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
     * @param buildingType
     * @param keyName
     */
    private static void checkBuildingType(int buildingType, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_TOWER);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SLAB_AND_TOWER);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_SINGLE);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_ROW);
        validKeys.add(Constants.HouseBase.BUILDING_TYPE_OVERLAY);
        checkIfValid(buildingType, validKeys, keyName);
    }


    /**
     * 校验入住时间
     *
     * @param date
     */
    private static void checkCheckIn(Date date, String keyName) {
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
     * @param buildingYear
     */
    private static void checkBuildingYear(String buildingYear, String keyName) {
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
     * @param entireRent
     * @param keyName
     */
    private static void checkEntireRent(int entireRent, String keyName) {
        List<Integer> validKeys = new ArrayList<Integer>();
        validKeys.add(Constants.HouseDetail.RENT_TYPE_SHARE);
        validKeys.add(Constants.HouseDetail.RENT_TYPE_ENTIRE);
        validKeys.add(Constants.HouseDetail.RENT_TYPE_BOTH);
        checkIfValid(entireRent, validKeys, keyName);
    }

    /**
     * 校验参数是否有效
     *
     * @param value
     * @param validValues
     * @param keyName
     */
    private static void checkIfValid(int value, List<Integer> validValues, String keyName) {
        if (CollectionUtils.isEmpty(validValues)) {
            return;
        }
        if (!validValues.contains(value)) {
            throw new InvalidParameterException("参数异常:" + keyName);
        }
    }
}
