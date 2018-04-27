/** 
 * Project Name: hzf_platform 
 * File Name: HouseUtil.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年4月27日下午12:13:52 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.ComplaintSaveDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDto;
import com.huifenqi.hzf_platform.context.dto.request.house.RoomPublishDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.ComplaintInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.ComplaintQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.HouseSearchResultInfo;
import com.huifenqi.hzf_platform.context.dto.response.house.ImgLink;
import com.huifenqi.hzf_platform.context.dto.response.house.RoomQueryDto;
import com.huifenqi.hzf_platform.context.dto.response.house.SettingVo;
import com.huifenqi.hzf_platform.context.entity.house.Apartment;
import com.huifenqi.hzf_platform.context.entity.house.Complaint;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.HzfHousesSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;
import com.huifenqi.hzf_platform.context.entity.location.Subway;
import com.huifenqi.hzf_platform.context.enums.HouseTagsEnum;
import com.huifenqi.hzf_platform.context.enums.bd.RoomTypeEnum;
import com.huifenqi.hzf_platform.context.enums.bd.SettingsEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;

/**
 * ClassName: HouseUtil date: 2016年4月27日 下午12:13:52 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseUtil {

	private static final Log logger = LogFactory.getLog(HouseUtil.class);

	public static final String[] CHINESE_NUMBER = { "0", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };

	public static final String PREFIX = "HF";

	private static int sequenceId = 0;

	/**
	 * 获取房源销售Id
	 * 
	 * @return
	 */
	// public static synchronized String getHouseSellId() {
	// StringBuilder msgId = new StringBuilder();
	// msgId.append(PREFIX);
	// msgId.append(DateUtil.format("yyyyMMdd", new Date()));
	//
	// sequenceId = sequenceId >= 10000 ? 1 : sequenceId;
	// msgId.append(String.format("%04d", sequenceId++));
	//
	// msgId.append(MsgUtils.generateNoncestr(3));
	// return msgId.toString();
	// }

	// public static synchronized String getHouseSellId() {
	// StringBuilder msgId = new StringBuilder();
	// msgId.append(PREFIX);
	// String data = DateUtil.format("yyyyMMddHHMMSSFFF", new Date());
	// msgId.append(Integer.toHexString(Integer.valueOf(data.substring(2, 8))));
	// msgId.append(Integer.toHexString(Integer.valueOf(data.substring(8,
	// 17))));
	//
	// // sequenceId = sequenceId >= 10000 ? 1 : sequenceId;
	// // msgId.append(String.format("%04d", sequenceId++));
	//
	// try {
	// Thread.sleep(1);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// msgId.append(MsgUtils.generateNoncestr(1));
	//
	// return msgId.toString().toUpperCase();
	// }

	public static synchronized String getHouseSellId() {
		StringBuilder msgId = new StringBuilder();
		Random random = new Random();
		msgId.append(PREFIX);

		sequenceId = sequenceId >= 10 ? 0 : sequenceId;
		msgId.append(sequenceId++);

		int hashCode = UUID.randomUUID().toString().hashCode();
		if (hashCode < 0) {
			hashCode = -hashCode;
		}
		msgId.append(String.format("%010d", hashCode));
		msgId.append(String.format("%d", random.nextInt(10)));

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msgId.toString().toUpperCase();
	}

	/**
	 * 获取房源设置字符串列表
	 * 
	 * @param settingStr
	 * @return
	 */
	public static List<String> getHouseSettingSingleStringList(String settingStr) {
		List<String> settingStringList = new ArrayList<String>();
		if (StringUtil.isEmpty(settingStr)) {
			return settingStringList;
		}
		StringTokenizer st = new StringTokenizer(settingStr, StringUtil.COMMA);
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			settingStringList.add(token);
		}
		return settingStringList;
	}

	/**
	 * 返回单个设置key和num
	 * 
	 * @param singleSettingStr
	 * @return
	 */
	public static Pair<String, String> getHouseSettingKeyAndNum(String singleSettingStr) {
		if (StringUtil.isEmpty(singleSettingStr)) {
			return null;
		}
		String[] split = StringUtil.split(singleSettingStr, StringUtil.COLON);
		if (split == null || split.length != 2) {
			return null;
		}
		String settingKey = split[0];
		String settingNumValue = split[1];
		Pair<String, String> pair = Pair.of(settingKey, settingNumValue);
		return pair;
	}

	/**
	 * 返回单个设置type和code
	 * 
	 * @param settingKey
	 * @return
	 */
	public static Pair<Integer, Integer> getHouseSettingTypeAndCode(String settingKey) {
		if (settingKey == null) {
			return null;
		}
		// int settingType = Constants.HouseSetting.getSettingType(settingKey);
		// int settingCode = Constants.HouseSetting.getSettingCode(settingKey);
		int settingType = SettingsEnum.getSettingType(settingKey);
		int settingCode = SettingsEnum.getSettingCode(settingKey);
		return Pair.of(settingType, settingCode);
	}

	/**
	 * 返回地铁坐标字符串
	 * 
	 * @param subway
	 * @return
	 */
	public static String getPosition(Subway subway) {
		if (subway == null) {
			return null;
		}
		String longitude = subway.getLongitude();
		String latitude = subway.getLatitude();
		return getPosition(longitude, latitude);
	}

	/**
	 * 获取房源坐标字符串
	 * 
	 * @param houseDetail
	 * @return
	 */
	public static String getPosition(HouseDetail houseDetail) {
		if (houseDetail == null) {
			return null;
		}
		String longitude = houseDetail.getBaiduLongitude();
		String latitude = houseDetail.getBaiduLatitude();
		return getPosition(longitude, latitude);
	}

	/**
	 * 获取坐标字符串
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static String getPosition(String longitude, String latitude) {
		if (StringUtil.isEmpty(longitude)) {
			return null;
		}
		if (StringUtil.isEmpty(latitude)) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(longitude);
		builder.append(StringUtil.COMMA);
		builder.append(latitude);
		return builder.toString();
	}

	/**
	 * 获取房源描述，如“一室一厅”
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static String getRoomDesc(int bedroomNum, int livingroomNum) {
		StringBuilder builder = new StringBuilder();

		int numberLength = CHINESE_NUMBER.length;

		if (bedroomNum >= 0 && bedroomNum < numberLength) {
			builder.append(bedroomNum);
		} else {
			builder.append("?");
		}
		builder.append("室");

		if (livingroomNum >= 0 && livingroomNum < numberLength) {
			builder.append(livingroomNum);
		} else {
			builder.append("?");
		}
		builder.append("厅");

		return builder.toString();
	}

	/**
	 * 获取面积显示字符串
	 * 
	 * @param area
	 * @return
	 */
	public static String getFloorStr(String floorNo, String floorTotal) {
		StringBuilder builder = new StringBuilder();

		if (floorNo.equals("0") && floorTotal.equals("0")) {
			builder.append("");
		} else if (floorNo.equals("0") && !floorTotal.equals("0")) {
			builder.append("共").append(floorNo).append("层");
		} else if (!floorNo.equals("0") && floorTotal.equals("0")) {
			builder.append(floorNo).append("层");
		} else {
			if (isTopFloor(floorNo, floorTotal)) {
				builder.append("顶层");
			} else {
				builder.append(floorNo);
			}
			builder.append(StringUtil.SLASH);
			builder.append(floorTotal);
			builder.append("层");
		}
		/*
		 * if (!floorNo.equals("0") || !floorTotal.equals("0")) {// 楼层或者总楼层不为0
		 * if (isTopFloor(floorNo, floorTotal)) { builder.append("顶层"); } else {
		 * builder.append(floorNo); // builder.append("层"); }
		 * builder.append(StringUtil.SLASH); builder.append(floorTotal); //
		 * builder.append("层"); } else {// 楼层或者总楼层其中一项为0 if
		 * (floorNo.equals("0")) { builder.append(StringUtil.QFT_HYPHEN); } else
		 * { builder.append(floorNo); }
		 * 
		 * if (floorTotal.equals("0")) { builder.append(StringUtil.SLASH);
		 * builder.append(StringUtil.QFT_HYPHEN); } else {
		 * builder.append(StringUtil.SLASH); builder.append(floorTotal); } }
		 */
		return builder.toString();
	}

	/**
	 * 是否为顶层
	 * 
	 * @param floorNo
	 * @param floorTotal
	 * @return
	 */
	public static boolean isTopFloor(String floorNo, String floorTotal) {
		boolean isTop = false;
		if (StringUtil.isNotBlank(floorNo) && floorNo.equalsIgnoreCase(floorTotal)) {
			isTop = true;
		}
		return isTop;
	}

	/**
	 * 获取朝向字符串
	 * 
	 * @param orientations
	 * @return
	 */
	public static String getOrientationsStr(int orientations) {
		String orientationsStr = Constants.HouseBase.getOrentationName(orientations);
		return orientationsStr;
	}

	/**
	 * 获取房间类型字符串
	 * 
	 * @param orientations
	 * @return
	 */
	public static String getRoomTypeStr(int roomType) {
		// String orientationsStr = Constants.RoomBase.getRoomType(roomType);
		String orientationsStr = RoomTypeEnum.getRoomTypeDesc(roomType);
		return orientationsStr;
	}

	/**
	 * 获取面积显示字符串
	 * 
	 * @param area
	 * @return
	 */
	public static String getAreaStr(double area) {
		BigDecimal decimalData = new BigDecimal(area).setScale(0, BigDecimal.ROUND_HALF_UP);
		return decimalData.toString();
	}

	/**
	 * 获取押金形式字段
	 * 
	 * @param depositMonth
	 * @param periodMonth
	 * @return
	 */
	public static String getPaytypeStr(int depositMonth, int periodMonth) {

		StringBuilder builder = new StringBuilder();

		int numberLength = CHINESE_NUMBER.length;

		if (periodMonth > 0 && periodMonth < numberLength) {

			if (depositMonth > 0 && depositMonth < numberLength) {
				builder.append("押");
				builder.append(HouseUtil.CHINESE_NUMBER[depositMonth]);
			}

			builder.append("付");
			builder.append(HouseUtil.CHINESE_NUMBER[periodMonth]);
		}

		return builder.toString();
	}

	/**
	 * 获取保险显示字符串
	 * 
	 * @param insurance
	 * @return
	 */
	public static String getInsuranceStr(int insurance) {
		String str = null;
		switch (insurance) {
		case Constants.HouseBase.INSURANCE_EXIST:
			str = "有";
			break;
		case Constants.HouseBase.INSURANCE_NOT_EXIST:
			str = "无";
			break;

		default:
			str = "未知";
			break;
		}
		return str;
	}

	// /**
	// * 获取房源列表标题
	// *
	// * @param houseDetail
	// * @return
	// */
	// public static String getHouseListTitle(HouseDetail houseDetail) {
	// String bizName = houseDetail.getBizName();
	// String communityName = houseDetail.getCommunityName();
	// int bedroomNum = houseDetail.getBedroomNum();
	// int orientations = houseDetail.getOrientations();
	// int houseType = houseDetail.getHouseType();
	// String focusCode = houseDetail.getFocusCode();
	//
	// String houseListTitle = getHouseListTitle(bizName, communityName,
	// bedroomNum, orientations, houseType,
	// focusCode);
	//
	// return houseListTitle;
	// }

	/**
	 * 获取房源列表标题
	 * 
	 * @param houseSolrResult
	 * @return
	 */
	public static String getHouseListTitle(HouseSolrResult houseSolrResult) {
		String companyName = houseSolrResult.getCompanyName();
		String bizName = houseSolrResult.getBizName();
		String communityName = houseSolrResult.getCommunityName();
		int bedroomNum = houseSolrResult.getBedroomNums();
		int orientations = houseSolrResult.getOrientations();
		int houseType = houseSolrResult.getHouseType();
		String focusCode = houseSolrResult.getFocusCode();
		String source = houseSolrResult.getSource();
		String houseListTitle = getHouseListTitle(source, companyName, bizName, communityName, bedroomNum, orientations,
				houseType, focusCode);
		return houseListTitle;
	}

	/**
	 * 获取房源列表标题
	 * 
	 * @param houseSolrResult
	 * @return
	 */
	public static String getHouseListTitle(RoomSolrResult roomSolrResult) {
		String companyName = roomSolrResult.getCompanyName();
		String bizName = roomSolrResult.getBizName();
		String communityName = roomSolrResult.getCommunityName();
		int bedroomNum = roomSolrResult.getBedroomNums();
		int orientations = roomSolrResult.getOrientations();
		// int roomType = roomSolrResult.getRoomType();
		String source = roomSolrResult.getSource();
		String houseListTitle = getHouseListTitleRoom(source, companyName, bizName, communityName, bedroomNum,
				orientations);
		return houseListTitle;
	}

	/**
	 * 获取房源列表标题
	 *
	 * @param houseSolrResult
	 * @return
	 */
	public static String getHzfHousesListTitle(HzfHousesSolrResult roomSolrResult) {
		String companyName = roomSolrResult.getCompanyName();
		String bizName = roomSolrResult.getBizName();
		String communityName = roomSolrResult.getCommunityName();
		int bedroomNum = roomSolrResult.getBedroomNums();
		int orientations = roomSolrResult.getOrientations();
		// int roomType = roomSolrResult.getRoomType();
		String source = roomSolrResult.getSource();
		String houseListTitle = getHouseListTitleRoom(source, companyName, bizName, communityName, bedroomNum,
				orientations);
		return houseListTitle;
	}

	/**
	 * 获取房源列表标题
	 * 
	 * @param communityName
	 * @param bedroomNum
	 * @param orientations
	 * @return
	 */
	public static String getHouseListTitle(String source, String companyName, String bizName, String communityName,
			int bedroomNum, int orientations, int houseType, String foucsCode) {
		StringBuilder stringBuilder = new StringBuilder();
		/*
		 * if (StringUtil.isNotEmpty(companyName)) { //if
		 * (!source.equals(StringUtil.QFT)) { stringBuilder.append(companyName);
		 * //} }
		 */

		if (StringUtil.isNotEmpty(bizName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(bizName);
		}

		if (StringUtil.isNotEmpty(communityName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(communityName);
		}

		if (houseType > 0) {// 集中式房源
			/*
			 * if (StringUtil.isNotEmpty(communityName)) {
			 * stringBuilder.append(StringUtil.DOT2); }
			 */
			stringBuilder.append(foucsCode);
		} else {
			if (bedroomNum > 0) {
				/*
				 * if (StringUtil.isNotEmpty(communityName)) {
				 * stringBuilder.append(StringUtil.DOT2); }
				 */
				stringBuilder.append(bedroomNum);
				stringBuilder.append("居");
			}
		}

		String stringReturn = stringBuilder.toString();
		if (stringReturn.startsWith(StringUtil.DOT2)) {
			stringReturn = stringReturn.substring(1);
		}
		return stringReturn;
	}

	/**
	 * 获取房源列表标题
	 * 
	 * @param communityName
	 * @param bedroomNum
	 * @param orientations
	 * @return
	 */
	public static String getHouseListTitleRoom(String source, String companyName, String bizName, String communityName,
			int bedroomNum, int orientations) {
		StringBuilder stringBuilder = new StringBuilder();

		/*
		 * if (StringUtil.isNotEmpty(companyName)) {
		 * stringBuilder.append(companyName); }
		 */

		if (StringUtil.isNotEmpty(bizName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(bizName);
		}

		if (StringUtil.isNotEmpty(communityName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(communityName);
		}

		if (bedroomNum > 0) {
			stringBuilder.append(bedroomNum);
			stringBuilder.append("居");
		}

		if (orientations > 0) {
			String orientationsStr = HouseUtil.getOrientationsStr(orientations);
			logger.debug("log by arissss   :" + "-".equals(orientationsStr));
			if (StringUtil.isNoneBlank(orientationsStr)) {
				stringBuilder.append(StringUtil.DOT2);
				stringBuilder.append(orientationsStr);
				stringBuilder.append("卧");
			}
		}
		// if (roomType > 0) {
		// stringBuilder.append(StringUtil.HYPHEN);
		// if (roomType == Constants.RoomBase.ROOM_TYPE_OPTIMIZED) {// 优化间展示次卧
		// roomType = Constants.RoomBase.ROOM_TYPE_SECONDARY;
		// }
		// String roomTypeStr = HouseUtil.getRoomTypeStr(roomType);
		// stringBuilder.append(roomTypeStr);
		// }
		String stringReturn = stringBuilder.toString();
		if (stringReturn.startsWith(StringUtil.DOT2)) {
			stringReturn = stringReturn.substring(1);
		}
		return stringReturn;
	}

	/**
	 * 获取房源详情标题
	 * 
	 * @param houseDetail
	 * @return
	 */
	// public static String getHouseDetailTitle(HouseDetail houseDetail,
	// RoomBase roomBase) {
	//
	// if (houseDetail == null) {
	// return StringUtil.EMPTY;
	// }
	// String bizName = houseDetail.getBizName();
	//
	// String communityName = houseDetail.getCommunityName();
	//
	// int bedroomNum = houseDetail.getBedroomNum();
	//
	// int livingroomNum = houseDetail.getLivingroomNum();
	//
	// int toiletNum = houseDetail.getToiletNum();
	//
	// int roomType = 0;
	// if (roomBase != null) {
	// roomType = roomBase.getRoomType();
	// }
	//
	// int orientations = houseDetail.getOrientations();
	//
	// return getHouseDetailTitle(bizName, communityName, bedroomNum, roomType,
	// orientations);
	// }

	/**
	 * 获取房源详情标题
	 * 
	 * @param houseDetail
	 * @return
	 */
	public static String getHouseDetailTitle(HouseSolrResult houseSolrResult, List<String> agencyIdList,
			String cyName) {

		if (houseSolrResult == null) {
			return StringUtil.EMPTY;
		}
		String companyName = "";
		if (agencyIdList.contains(houseSolrResult.getCompanyId())) {
			companyName = cyName;
		}

		String bizName = houseSolrResult.getBizName();

		String communityName = houseSolrResult.getCommunityName();

		int bedroomNum = houseSolrResult.getBedroomNums();

		int entireRent = houseSolrResult.getEntireRent();

		int roomType = 0;

		int orientations = houseSolrResult.getOrientations();

		int houseType = houseSolrResult.getHouseType();

		String focusCode = houseSolrResult.getFocusCode();

		String source = houseSolrResult.getSource();
		return getHouseDetailTitle(source, companyName, bizName, communityName, bedroomNum, entireRent, roomType,
				orientations, houseType, focusCode);
	}

	/**
	 * 获取房源详情标题
	 * 
	 * @param houseDetail
	 * @return
	 */
	public static String getHouseDetailTitle(RoomSolrResult roomSolrResult, List<String> agencyIdList, String cyName) {

		if (roomSolrResult == null) {
			return StringUtil.EMPTY;
		}
		String companyName = "";
		if (agencyIdList.contains(roomSolrResult.getCompanyId())) {
			companyName = cyName;
		}

		String bizName = roomSolrResult.getBizName();

		String communityName = roomSolrResult.getCommunityName();

		int bedroomNum = roomSolrResult.getBedroomNums();

		int entireRent = roomSolrResult.getEntireRent();

		int roomType = roomSolrResult.getRoomType();

		int orientations = roomSolrResult.getOrientations();

		String source = roomSolrResult.getSource();

		return getHouseDetailTitle(source, companyName, bizName, communityName, bedroomNum, entireRent, roomType,
				orientations);
	}

	/**
	 * 获取房源详情标题
	 * 
	 * @param communityName
	 * @param bedroomNum
	 * @param livingroomNum
	 * @param toiletNum
	 * @param roomName
	 * @param orientations
	 * @return
	 */
	public static String getHouseDetailTitle(String source, String companyName, String bizName, String communityName,
			int bedroomNum, int entireRent, int roomType, int orientations) {
		StringBuilder stringBuilder = new StringBuilder();
		if (StringUtil.isNotEmpty(companyName)) {
			// if (!source.equals(StringUtil.QFT)) {
			stringBuilder.append(companyName);
			// }
		}

		if (StringUtil.isNotEmpty(bizName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(bizName);
		}

		if (StringUtil.isNotEmpty(communityName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(communityName);
		}

		if (bedroomNum > 0) {
			stringBuilder.append(bedroomNum);
			stringBuilder.append("居");
		}

		if (entireRent == 0) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append("合租");
		} else if (entireRent == 1) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append("整租");
		}

		// stringBuilder.append(livingroomNum);
		// stringBuilder.append("厅");
		//
		// stringBuilder.append(toiletNum);
		// stringBuilder.append("卫");

		// if (roomType > 0) {
		// stringBuilder.append(StringUtil.HYPHEN);
		// if (roomType == Constants.RoomBase.ROOM_TYPE_OPTIMIZED) {// 优化间展示次卧
		// roomType = Constants.RoomBase.ROOM_TYPE_SECONDARY;
		// }
		// String roomTypeStr = HouseUtil.getRoomTypeStr(roomType);
		// stringBuilder.append(roomTypeStr);
		// }

		if (roomType > 0 && orientations != 11111) {
			stringBuilder.append(StringUtil.DOT2);
			String orientationsStr = HouseUtil.getOrientationsStr(orientations);
			if (StringUtil.isNoneBlank(orientationsStr)) {
				stringBuilder.append(orientationsStr);
				stringBuilder.append("卧");
			}
		}

		String stringReturn = stringBuilder.toString();
		if (stringReturn.startsWith(StringUtil.DOT2)) {
			stringReturn = stringReturn.substring(1);
		}

		return stringReturn;
	}

	/**
	 * 获取整租房源详情标题
	 * 
	 * @param communityName
	 * @param bedroomNum
	 * @param livingroomNum
	 * @param toiletNum
	 * @param roomName
	 * @param orientations
	 * @return
	 */
	public static String getHouseDetailTitle(String source, String companyName, String bizName, String communityName,
			int bedroomNum, int entireRent, int roomType, int orientations, int houseType, String focusCode) {
		StringBuilder stringBuilder = new StringBuilder();
		// if (StringUtil.isNotEmpty(companyName)) {
		// //if (!source.equals(StringUtil.QFT)) {
		// stringBuilder.append(companyName);
		// //}
		// }

		if (StringUtil.isNotEmpty(bizName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(bizName);
		}

		if (StringUtil.isNotEmpty(communityName)) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append(communityName);
		}

		if (houseType > 0) {// 集中式房源
			stringBuilder.append(focusCode);
		} else {
			if (bedroomNum > 0) {
				stringBuilder.append(bedroomNum);
				stringBuilder.append("居");
			}
		}

		if (entireRent == 0) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append("合租");
		} else if (entireRent == 1) {
			stringBuilder.append(StringUtil.DOT2);
			stringBuilder.append("整租");
		}

		// stringBuilder.append(livingroomNum);
		// stringBuilder.append("厅");
		//
		// stringBuilder.append(toiletNum);
		// stringBuilder.append("卫");

		// if (roomType > 0) {
		// stringBuilder.append(StringUtil.HYPHEN);
		// if (roomType == Constants.RoomBase.ROOM_TYPE_OPTIMIZED) {// 优化间展示次卧
		// roomType = Constants.RoomBase.ROOM_TYPE_SECONDARY;
		// }
		// String roomTypeStr = HouseUtil.getRoomTypeStr(roomType);
		// stringBuilder.append(roomTypeStr);
		// }

		if (roomType > 0 && orientations != 11111) {
			stringBuilder.append(StringUtil.DOT2);
			String orientationsStr = HouseUtil.getOrientationsStr(orientations);
			if (StringUtil.isNoneBlank(orientationsStr)) {
				stringBuilder.append(orientationsStr);
				stringBuilder.append("卧");
			}
		}

		String stringReturn = stringBuilder.toString();
		if (stringReturn.startsWith(StringUtil.DOT2)) {
			stringReturn = stringReturn.substring(1);
		}

		return stringReturn;
	}

	/**
	 * 获取房间详情标题
	 * 
	 * @param houseDetail
	 * @return
	 */
	public static String getRoomTitle(RoomBase roomBase) {

		int status = roomBase.getStatus();
		String roomName = roomBase.getRoomName();
		int orientations = roomBase.getOrientations();

		return getRoomTitle(status, roomName, orientations);
	}

	/**
	 * 获取房间标题
	 * 
	 * @param status
	 * @param roomName
	 * @param orientation
	 * @return
	 */
	public static String getRoomTitle(int status, String roomName, int orientation) {

		StringBuilder stringBuilder = new StringBuilder();

		if (status == Constants.HouseBase.STATUS_RENT) {
			stringBuilder.append(StringUtil.LEFT_SQUARE_BRACKET);
			stringBuilder.append(Constants.HouseBase.getSimplifiedStatusDesc(status));
			stringBuilder.append(StringUtil.RIGHT_SQUARE_BRACKET);
		}

		if (StringUtil.isNotEmpty(roomName)) {
			stringBuilder.append(roomName);
		}

		stringBuilder.append(StringUtil.HYPHEN);

		String orientationsStr = HouseUtil.getOrientationsStr(orientation);
		stringBuilder.append(orientationsStr);

		return stringBuilder.toString();
	}

	/**
	 * 获取房源状态
	 * 
	 * @param publishStatus
	 * @return
	 */
	public static int getStatus(int publishStatus) {
		int status = Constants.HouseBase.STATUS_DRAFT;
		if (publishStatus == Constants.HouseBase.PUBLISH_STATUS_NOT_ON_SALE) { // 未上架
			status = Constants.HouseBase.STATUS_DRAFT;
		} else if (publishStatus == Constants.HouseBase.PUBLISH_STATUS_PRE_RENT) { // 待出租
			status = Constants.HouseBase.STATUS_NEW;
		} else if (publishStatus == Constants.HouseBase.PUBLISH_STATUS_RENT) { // 已出租
			status = Constants.HouseBase.STATUS_RENT;
		}
		return status;
	}

	/**
	 * 获取简化地铁描述字符串
	 * 
	 * @param subway
	 * @return
	 */
	public static String getSimplifiedSubway(String subway) {
		if (StringUtil.isEmpty(subway)) {
			return StringUtil.EMPTY;
		}
		String simplifiedSubway = StringUtil.EMPTY;
		int index = subway.indexOf(StringUtil.COMMA);
		if (index >= 0) {
			simplifiedSubway = subway.substring(0, index);
		}

		if (StringUtil.isEmpty(simplifiedSubway)) {
			return simplifiedSubway;
		}
		// int left = simplifiedSubway.indexOf(StringUtil.LEFT_PARENTHESIS);
		// int right = simplifiedSubway.indexOf(StringUtil.RIGHT_PARENTHESIS);
		// if (left >= 0 && right >= 0) {
		// simplifiedSubway = simplifiedSubway.substring(0, left) +
		// simplifiedSubway.substring(right + 1);
		// }
		return simplifiedSubway;

		// String simplifiedSubway = StringUtil.EMPTY;
		// int left = subway.indexOf(StringUtil.LEFT_PARENTHESIS);
		// int right = subway.indexOf(StringUtil.RIGHT_PARENTHESIS);
		// if (left >= 0 && right >= 0) {
		// simplifiedSubway = subway.substring(0, left) + subway.substring(right
		// + 1);
		// } else {
		// simplifiedSubway = subway;
		// }
		//
		// return simplifiedSubway;
	}

	/**
	 * 获取发布时间字符串
	 * 
	 * @param pubDate
	 * @return
	 */
	public static String getPubDesc(Date pubDate) {
		String pubDesc = StringUtil.EMPTY;
		if (pubDate == null) {
			return pubDesc;
		}
		Date date = new Date();
		if (pubDate.after(date)) {
			return pubDesc;
		}

		// 定义时间显示字符串
		int diffHourFromCurrent = DateUtil.getDiffHourFromCurrent(pubDate);
		if (diffHourFromCurrent < 1) {
			pubDesc = "刚刚发布";
		} else if (diffHourFromCurrent < 24) {
			pubDesc = String.format("%d小时前发布", diffHourFromCurrent);
		} else if (diffHourFromCurrent < 24 * 7) {
			pubDesc = String.format("%d天前发布", diffHourFromCurrent / 24);
		} else {
			pubDesc = DateUtil.formatDate(pubDate) + "发布";
		}

		return pubDesc;

	}

	/**
	 * 获取装饰类型描述
	 * 
	 * @param decoration
	 * @return
	 */
	public static String getDecorationStr(int decoration) {
		String decorationName = Constants.HouseBase.getDecorationName(decoration);
		return decorationName;
	}

	/**
	 * 获取租房类型描述
	 * 
	 * @param entireRent
	 * @return
	 */
	public static String getEntireRentStr(int entireRent) {
		String rentTypeName = Constants.HouseDetail.getRentTypeName(entireRent);
		return rentTypeName;
	}

	/**
	 * 获取主要房源配置JSONArray
	 * 
	 * @param houseSettingList
	 * @return
	 */
	public static List<SettingVo> getHouseSettingSortList(List<HouseSetting> houseSettingList) {
		if (CollectionUtils.isEmpty(houseSettingList)) {
			return null;
		}
		List<HouseSetting> settingList = HouseUtil.getPrimaryHouseSettingList(houseSettingList);
		if (CollectionUtils.isEmpty(settingList)) {
			return null;
		}
		List<SettingVo> sortList = new ArrayList<SettingVo>();
		for (HouseSetting setting : houseSettingList) {
			int categoryType = setting.getCategoryType();
			int settingCode = setting.getSettingCode();
			int settingNum = setting.getSettingNum();
			
			SettingsEnum obj = SettingsEnum.getEnumObject(categoryType, settingCode);
			if (obj != null) {
				SettingVo vo = new SettingVo();
				vo.setName(obj.name());
				vo.setDesc(obj.getDesc());
				vo.setSort(obj.getSort());
				vo.setCount(settingNum);
				sortList.add(vo);
			}
		}
		// 对 sortList 进行排序
		Collections.sort(sortList, new Comparator<SettingVo>() {
			@Override
			public int compare(SettingVo o1, SettingVo o2) {
				Integer s1 = o1.getSort();
				Integer s2 = o2.getSort();
				return s1.compareTo(s2);
			}
		});
		return sortList;
	}


	/**
	 * 获取主要房源设置列表
	 * 
	 * @param houseSettingList
	 * @return
	 */
	public static List<HouseSetting> getPrimaryHouseSettingList(List<HouseSetting> houseSettingList) {
		// return getHouseSettingList(houseSettingList,
		// Constants.HouseSetting.getPrimarySettingTypes());
		return getHouseSettingList(houseSettingList, SettingsEnum.getPrimarySettingTypes());
	}

	/**
	 * 获取次要房源设置列表
	 * 
	 * @param houseSettingList
	 * @return
	 */
	public static List<HouseSetting> getSecondaryHouseSettingList(List<HouseSetting> houseSettingList) {
		// return getHouseSettingList(houseSettingList,
		// Constants.HouseSetting.getSecondarySettingTypes());
		return getHouseSettingList(houseSettingList, SettingsEnum.getSecondarySettingTypes());
	}

	/**
	 * 获取指定类型房源设置列表
	 * 
	 * @param houseSettingList
	 * @param categoryType
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(List<HouseSetting> houseSettingList,
			List<Integer> categoryTypeList) {
		if (CollectionUtils.isEmpty(houseSettingList)) {
			return null;
		}
		if (CollectionUtils.isEmpty(categoryTypeList)) {
			return null;
		}
		List<HouseSetting> list = new ArrayList<HouseSetting>();
		for (HouseSetting setting : houseSettingList) {
			int categoryType = setting.getCategoryType();
			if (categoryTypeList.contains(categoryType)) {
				list.add(setting);
			}
		}
		return list;
	}

	/**
	 * 获取配置列表
	 * 
	 * @param settingCodes
	 * @param categoryTypes
	 * @param settingNums
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(List<Integer> settingCodes, List<Integer> categoryTypes,
			List<Integer> settingNums) {
		if (CollectionUtils.isEmpty(settingCodes)) {
			return null;
		}
		if (settingCodes.size() != settingNums.size()) {// 列表长度一致
			logger.error(
					LogUtils.getCommLog("房屋配置信息有误, settingCodes:" + settingCodes + ", categoryTypes:" + categoryTypes));
			return null;
		}
		List<HouseSetting> list = new ArrayList<HouseSetting>();
		for (int i = 0; i < settingCodes.size(); i++) {
			Integer code = settingCodes.get(i);
			Integer type = categoryTypes.get(i);
			Integer num = settingNums.get(i);
			if (code != null && type != null) {
				HouseSetting houseSetting = new HouseSetting();
				houseSetting.setCategoryType(type);
				houseSetting.setSettingCode(code);
				houseSetting.setSettingNum(num);
				list.add(houseSetting);
			} else {
				logger.error(LogUtils.getCommLog("配置参数存在空值, code:" + code + ", type:" + type));
			}
		}
		return list;
	}

	/**
	 * 获取单个房间查询结果
	 * 
	 * @param roomBase
	 * @return
	 */
	public static RoomQueryDto getRoomQueryDto(RoomBase roomBase, List<HousePicture> houseRoomPictureList) {
		if (roomBase == null) {
			return null;
		}

		RoomQueryDto roomQueryDto = new RoomQueryDto();

		String sellId = roomBase.getSellId();
		roomQueryDto.setSellId(sellId);

		long roomId = roomBase.getId();
		roomQueryDto.setRoomId(roomId);

		int roomType = roomBase.getRoomType();
		// roomQueryDto.setRoomType(Constants.RoomBase.getRoomType(roomType));
		roomQueryDto.setRoomType(RoomTypeEnum.getRoomTypeDesc(roomType));

		int monthRent = roomBase.getMonthRent();
		roomQueryDto.setRentPriceMonth(monthRent);

		// 朝向
		int orientations = roomBase.getOrientations();
		String orientationsStr = HouseUtil.getOrientationsStr(orientations);
		roomQueryDto.setOrientations(orientationsStr);

		// 面积
		roomQueryDto.setArea(HouseUtil.getAreaStr(roomBase.getArea()));

		List<String> imgsStrList = getImgsList(houseRoomPictureList);

		// 图片
		roomQueryDto.setImgs(imgsStrList);

		Date pubDate = roomBase.getPubDate();
		String pubDateStr = DateUtil.formatDate(pubDate);
		roomQueryDto.setPubDate(pubDateStr);

		// 状态
		int status = roomBase.getStatus();
		roomQueryDto.setStatus(status);
		roomQueryDto.setStatusDesc(Constants.HouseBase.getSimplifiedStatusDesc(status));

		// 房间标题
		String title = HouseUtil.getRoomTitle(roomBase);
		roomQueryDto.setTitle(title);

		// 押几付几
		int depositMonth = roomBase.getDepositMonth();
		int periodMonth = roomBase.getPeriodMonth();
		String paytypeStr = HouseUtil.getPaytypeStr(depositMonth, periodMonth);
		roomQueryDto.setPayType(paytypeStr);

		// 独立卫生间
		int toilet = roomBase.getToilet();
		roomQueryDto.setToilet(toilet);

		// 独立阳台
		int balcony = roomBase.getBalcony();
		roomQueryDto.setBalcony(balcony);

		return roomQueryDto;

	}

	/**
	 * 获取图片列表信息
	 * 
	 * @param houseRoomPictureList
	 * @return
	 */
	public static List<String> getImgsList(List<HousePicture> houseRoomPictureList) {
		if (CollectionUtils.isEmpty(houseRoomPictureList)) {
			return null;
		}
		List<String> imgList = new ArrayList<String>();
		for (HousePicture housePicture : houseRoomPictureList) {
			String picRootPath = housePicture.getPicRootPath();
			imgList.add(picRootPath);
		}
		return imgList;
	}

	/**
	 * 从房源DTO获取房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseBase getHouseBase(HousePublishDto housePublishDto) {
		return getHouseBase(housePublishDto, HouseUtil.getHouseSellId());
	}

	/**
	 * 从房源DTO获取房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseBase getCrawlHouseBase(HousePublishDto housePublishDto) {
		return getCrawlHouseBase(housePublishDto, HouseUtil.getHouseSellId());
	}

	/**
	 * 从房源DTO获取房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseBase getHouseBase(HousePublishDto housePublishDto, String sellId) {
		if (housePublishDto == null) {
			return null;
		}
		HouseBase houseBase = new HouseBase();
		houseBase.setSellId(sellId);
		houseBase.setIsSale(Constants.HouseBase.IS_SALE_NO);
		houseBase.setExt400(StringUtils.EMPTY);
		houseBase.setComment(StringUtil.EMPTY); // 房源描述写入Detail表
		houseBase.setCheckinTime(housePublishDto.getCheckInTime());

		int price = housePublishDto.getPrice();
		houseBase.setMonthRent(price);
		houseBase.setDayRent(price / 30); // 日租金为月租金除30

		houseBase.setServiceFee(housePublishDto.getBonus());
		houseBase.setDepositFee(housePublishDto.getDeposit()); // 押金
		houseBase.setDepositMonth(housePublishDto.getDepositMonth());
		houseBase.setPeriodMonth(housePublishDto.getPeriodMonth());

		houseBase.setCompanyId(housePublishDto.getCompanyId());
		houseBase.setCompanyName(housePublishDto.getCompanyName());
		houseBase.setAgencyId(housePublishDto.getAgencyId());
		houseBase.setAgencyPhone(housePublishDto.getAgencyPhone());
		houseBase.setAgencyName(housePublishDto.getAgencyName());
		houseBase.setAgencyIntroduce(housePublishDto.getAgencyIntroduce());
		houseBase.setAgencyGender(housePublishDto.getAgencyGender());
		houseBase.setAgencyAvatar(housePublishDto.getAgencyAvatar());
		houseBase.setApprovedId(0); // 审核房源人员Id(后续审核接口赋值 )

		Date date = new Date();
		houseBase.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		houseBase.setCreateTime(date);
		houseBase.setUpdateTime(date);

		// TODO 确定发布时间
		houseBase.setPubDate(date);
		houseBase.setFirstPubDate(date);

		int publishStatus = housePublishDto.getStatus();
		int status = HouseUtil.getStatus(publishStatus);
		houseBase.setStatus(status);

		houseBase.setHasKey(housePublishDto.getHasKey());

		return houseBase;
	}

	/**
	 * 从房源DTO获取房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseBase getCrawlHouseBase(HousePublishDto housePublishDto, String sellId) {
		if (housePublishDto == null) {
			return null;
		}
		HouseBase houseBase = new HouseBase();
		houseBase.setSellId(sellId);
		houseBase.setIsSale(Constants.HouseBase.IS_SALE_NO);
		houseBase.setExt400(StringUtils.EMPTY);
		houseBase.setComment(StringUtil.EMPTY); // 房源描述写入Detail表
		houseBase.setCheckinTime(housePublishDto.getCheckInTime());

		int price = housePublishDto.getPrice();
		houseBase.setMonthRent(price);
		houseBase.setDayRent(price / 30); // 日租金为月租金除30

		houseBase.setServiceFee(housePublishDto.getBonus());
		houseBase.setDepositFee(housePublishDto.getDeposit()); // 押金
		houseBase.setDepositMonth(housePublishDto.getDepositMonth());
		houseBase.setPeriodMonth(housePublishDto.getPeriodMonth());

		houseBase.setCompanyId(housePublishDto.getCompanyId());
		houseBase.setCompanyName(housePublishDto.getCompanyName());
		houseBase.setAgencyId(housePublishDto.getAgencyId());
		houseBase.setAgencyPhone(housePublishDto.getAgencyPhone());
		houseBase.setAgencyName(housePublishDto.getAgencyName());
		houseBase.setAgencyIntroduce(housePublishDto.getAgencyIntroduce());
		houseBase.setAgencyGender(housePublishDto.getAgencyGender());
		houseBase.setAgencyAvatar(housePublishDto.getAgencyAvatar());
		houseBase.setApprovedId(0); // 审核房源人员Id(后续审核接口赋值 )

		Date date = new Date();
		houseBase.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		houseBase.setCreateTime(date);
		houseBase.setUpdateTime(date);

		// TODO 确定发布时间
		houseBase.setPubDate(date);
		houseBase.setFirstPubDate(date);

		int publishStatus = housePublishDto.getStatus();
		int status = HouseUtil.getStatus(publishStatus);
		houseBase.setStatus(status);

		houseBase.setHasKey(housePublishDto.getHasKey());
		houseBase.setSourceFlag(Constants.HouseBase.SOURCE_FALG_CRAWL);
		return houseBase;
	}

	/**
	 * 更新房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseBase updateHouseBase(HouseBase oldHouseBase, HouseBase newHouseBase) {
		if (oldHouseBase == null) {
			return null;
		}
		if (newHouseBase == null) {
			return null;
		}

		oldHouseBase.setIsSale(newHouseBase.getIsSale());
		oldHouseBase.setExt400(newHouseBase.getExt400());
		oldHouseBase.setComment(newHouseBase.getComment());
		oldHouseBase.setCheckinTime(newHouseBase.getCheckinTime());

		oldHouseBase.setMonthRent(newHouseBase.getMonthRent());
		oldHouseBase.setDayRent(newHouseBase.getDayRent());

		oldHouseBase.setServiceFee(newHouseBase.getServiceFee());
		oldHouseBase.setDepositFee(newHouseBase.getDepositFee());
		oldHouseBase.setDepositMonth(newHouseBase.getDepositMonth());
		oldHouseBase.setPeriodMonth(newHouseBase.getPeriodMonth());

		oldHouseBase.setCompanyId(newHouseBase.getCompanyId());
		oldHouseBase.setCompanyName(newHouseBase.getCompanyName());
		oldHouseBase.setAgencyId(newHouseBase.getAgencyId());
		oldHouseBase.setAgencyPhone(newHouseBase.getAgencyPhone());
		oldHouseBase.setAgencyName(newHouseBase.getAgencyName());
		oldHouseBase.setAgencyIntroduce(newHouseBase.getAgencyIntroduce());
		oldHouseBase.setAgencyGender(newHouseBase.getAgencyGender());
		oldHouseBase.setAgencyAvatar(newHouseBase.getAgencyAvatar());

		// oldHouseBase.setApprovedId(newHouseBase.getApprovedId());

		oldHouseBase.setUpdateTime(newHouseBase.getUpdateTime());

		oldHouseBase.setPubDate(newHouseBase.getPubDate());

		oldHouseBase.setStatus(newHouseBase.getStatus());

		oldHouseBase.setHasKey(newHouseBase.getHasKey());

		return oldHouseBase;
	}

	/**
	 * 从房间DTO获取房间基础信息
	 * 
	 * @param roomPublishDto
	 * @return
	 */
	public static RoomBase getRoomBase(RoomPublishDto roomPublishDto) {
		if (roomPublishDto == null) {
			return null;
		}

		String sellId = roomPublishDto.getSellId();

		RoomBase roomBase = new RoomBase();
		roomBase.setSellId(sellId);

		int price = roomPublishDto.getPrice();
		roomBase.setMonthRent(price);
		roomBase.setDayRent(price / 30); // 日租金为月租金除30

		roomBase.setHasKey(roomPublishDto.getHasKey());

		roomBase.setServiceFee(roomPublishDto.getBonus());
		roomBase.setDepositFee(roomPublishDto.getDeposit()); // 押金
		roomBase.setDepositMonth(roomPublishDto.getDepositMonth());
		roomBase.setPeriodMonth(roomPublishDto.getPeriodMonth());

		roomBase.setApprovedId(0); // 审核房源人员Id(后续审核接口赋值 )

		// 保存经纪人电话
		roomBase.setAgencyPhone(roomPublishDto.getAgencyPhone());

		int publishStatus = roomPublishDto.getStatus();
		int status = HouseUtil.getStatus(publishStatus);
		roomBase.setStatus(status);

		roomBase.setArea(roomPublishDto.getArea());

		roomBase.setOrientations(roomPublishDto.getOrientation());

		roomBase.setToilet(roomPublishDto.getToilet());
		roomBase.setBalcony(roomPublishDto.getBalcony());
		// roomBase.setInsurance(roomPublishDto.getInsurance());

		roomBase.setCheckinTime(roomPublishDto.getCheckInTime());

		roomBase.setDecoration(roomPublishDto.getFitmentType());
		roomBase.setComment(roomPublishDto.getDesc());
		roomBase.setRoomName(roomPublishDto.getRoomName());
		roomBase.setRoomTag(StringUtil.EMPTY); // 自定义标签(后台生成)
		roomBase.setRoomType(roomPublishDto.getRoomType()); // 房间类型
		roomBase.setPubType(roomPublishDto.getPubType()); // 发布类型
		Date date = new Date();
		roomBase.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		roomBase.setCreateTime(date);
		roomBase.setUpdateTime(date);

		// TODO 确定发布时间
		roomBase.setPubDate(date);
		roomBase.setFirstPubDate(date);

		return roomBase;
	}

	/**
	 * 更新房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static RoomBase updateRoomBase(RoomBase oldRoomBase, RoomBase newRoomBase) {
		if (oldRoomBase == null) {
			return null;
		}
		if (newRoomBase == null) {
			return null;
		}

		oldRoomBase.setMonthRent(newRoomBase.getMonthRent());
		oldRoomBase.setDayRent(newRoomBase.getDayRent());

		oldRoomBase.setHasKey(newRoomBase.getHasKey());

		oldRoomBase.setServiceFee(newRoomBase.getServiceFee());
		oldRoomBase.setDepositFee(newRoomBase.getDepositFee());
		oldRoomBase.setDepositMonth(newRoomBase.getDepositMonth());
		oldRoomBase.setPeriodMonth(newRoomBase.getPeriodMonth());

		// oldRoomBase.setApprovedId(newRoomBase.getApprovedId());

		oldRoomBase.setStatus(newRoomBase.getStatus());

		oldRoomBase.setArea(newRoomBase.getArea());

		oldRoomBase.setOrientations(newRoomBase.getOrientations());

		oldRoomBase.setToilet(newRoomBase.getToilet());
		oldRoomBase.setBalcony(newRoomBase.getBalcony());
		// oldRoomBase.setInsurance(newRoomBase.getInsurance());

		oldRoomBase.setCheckinTime(newRoomBase.getCheckinTime());

		oldRoomBase.setDecoration(newRoomBase.getDecoration());
		oldRoomBase.setComment(newRoomBase.getComment());
		oldRoomBase.setRoomName(newRoomBase.getRoomName());
		oldRoomBase.setRoomTag(newRoomBase.getRoomTag());
		// oldRoomBase.setIsTop(newRoomBase.getIsTop());
		oldRoomBase.setUpdateTime(newRoomBase.getUpdateTime());

		oldRoomBase.setPubDate(newRoomBase.getPubDate());
		oldRoomBase.setRoomType(newRoomBase.getRoomType());
		// 保存经纪人电话 2017-07-24 13:03:57
		oldRoomBase.setAgencyPhone(newRoomBase.getAgencyPhone());
		oldRoomBase.setPubType(newRoomBase.getPubType());// saas发布类型变更
		// 修改房间时将isApprove 改为未审核，已下架的房源除外
		if (oldRoomBase.getStatus() != Constants.HouseBase.STATUS_RENT) {
			oldRoomBase.setApproveStatus(Constants.HouseDetail.NOT_APPROVE);
		}
		return oldRoomBase;
	}

	/**
	 * 从房源DTO获取房源详细信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseDetail getHouseDetail(HousePublishDto housePublishDto, HouseBase houseBase) {
		if (housePublishDto == null || houseBase == null) {
			return null;
		}
		HouseDetail houseDetail = new HouseDetail();
		houseDetail.setSellId(houseBase.getSellId());

		houseDetail.setBuildingNo(housePublishDto.getBuildingNo()); // 楼栋编号
		houseDetail.setUnitNo(housePublishDto.getUnitNo()); // 单元号
		houseDetail.setFlowNo(housePublishDto.getFlowNo());
		houseDetail.setFlowTotal(housePublishDto.getFlowTotal());
		houseDetail.setHouseNo(housePublishDto.getHouseNo()); // 门牌号
		houseDetail.setBuildingName(housePublishDto.getBuildingName()); // 楼栋名

		houseDetail.setArea(housePublishDto.getArea());

		// 朝向(存Id)

		houseDetail.setOrientations(housePublishDto.getOrientation());

		houseDetail.setBedroomNum(housePublishDto.getBedroomNum());
		houseDetail.setLivingroomNum(housePublishDto.getLivingroomNum());
		houseDetail.setKitchenNum(housePublishDto.getKitchenNum()); // 厨房数量
		houseDetail.setToiletNum(housePublishDto.getToiletNum()); // 卫生间数量
		houseDetail.setBalconyNum(housePublishDto.getBalconyNum()); // 阳台数量

		// houseDetail.setProvince(StringUtil.EMPTY); // 省份(后台抓取)
		// houseDetail.setCity(StringUtil.EMPTY); // 城市(后台抓取)
		// houseDetail.setDistrict(StringUtil.EMPTY); // 行政区(后台抓取)
		// houseDetail.setBizName(housePublishDto.getBizName()); // 商圈

		houseDetail.setHouseFunction(0); // TODO 房源用途
		// houseDetail.setAddress(StringUtil.EMPTY); // 地址(后台抓取拼接)
		houseDetail.setCommunityName(housePublishDto.getCommunityName());

		// houseDetail.setSubway(StringUtil.EMPTY);// 城市(后台抓取)
		// houseDetail.setSubwayDistance(0);// 城市(后台抓取)
		// houseDetail.setBusStations(StringUtil.EMPTY);// 城市(后台抓取)
		// houseDetail.setSurround(StringUtil.EMPTY);// 城市(后台抓取)

		// houseDetail.setCityId(0);// 城市(后台抓取)
		// houseDetail.setDistrictId(0);// 城市(后台抓取)
		// houseDetail.setBizId(0);// 城市(后台抓取)
		// houseDetail.setSubwayLineId(StringUtil.EMPTY);// 城市(后台抓取)
		// houseDetail.setSubwayStationId(StringUtil.EMPTY);// 城市(后台抓取)

		houseDetail.setBaiduLatitude(housePublishDto.getPositionX());
		houseDetail.setBaiduLongitude(housePublishDto.getPositionY());

		houseDetail.setBuildingType(housePublishDto.getBuildingType());

		String buildingYearValue = housePublishDto.getBuildingYear();
		int buildingYear = StringUtil.parseInt(buildingYearValue, 0);
		houseDetail.setBuildingYear((short) buildingYear);

		// houseDetail.setToilet(housePublishDto.getToilet());
		// houseDetail.setBalcony(housePublishDto.getBalcony());
		// houseDetail.setInsurance(housePublishDto.getInsurance());

		houseDetail.setDecoration(housePublishDto.getFitmentType());
		houseDetail.setEntireRent(housePublishDto.getEntireRent());
		houseDetail.setComment(housePublishDto.getDesc()); // 房源描述重复
		// houseDetail.setHouseTag(StringUtil.EMPTY); // 自定义标签(后台生成)
		houseDetail.setSource(housePublishDto.getSource()); // 来源(后台抓取)

		houseDetail.setFocusCode(housePublishDto.getFocusCode());// 集中式房源编号
		houseDetail.setHouseType(housePublishDto.getHouseType());// 房源类型
		houseDetail.setIsRun(Constants.HouseBase.IS_RUN_NO); // 数据是否采集
		houseDetail.setPubType(housePublishDto.getPubType()); // saas发布类型
		Date date = new Date();
		houseDetail.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		houseDetail.setCreateTime(date);
		houseDetail.setUpdateTime(date);

		return houseDetail;
	}

	/**
	 * 更新房源基础信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static HouseDetail updateHouseDetail(HouseDetail oldHouseDetail, HouseDetail newHouseDetail, int status) {
		if (oldHouseDetail == null) {
			return null;
		}
		if (newHouseDetail == null) {
			return null;
		}

		oldHouseDetail.setBuildingNo(newHouseDetail.getBuildingNo());
		oldHouseDetail.setUnitNo(newHouseDetail.getUnitNo());
		oldHouseDetail.setFlowNo(newHouseDetail.getFlowNo());
		oldHouseDetail.setFlowTotal(newHouseDetail.getFlowTotal());
		oldHouseDetail.setHouseNo(newHouseDetail.getHouseNo());
		oldHouseDetail.setBuildingName(newHouseDetail.getBuildingName());

		oldHouseDetail.setArea(newHouseDetail.getArea());

		oldHouseDetail.setOrientations(newHouseDetail.getOrientations());

		oldHouseDetail.setBedroomNum(newHouseDetail.getBedroomNum());
		oldHouseDetail.setLivingroomNum(newHouseDetail.getLivingroomNum());
		oldHouseDetail.setKitchenNum(newHouseDetail.getKitchenNum());
		oldHouseDetail.setToiletNum(newHouseDetail.getToiletNum());
		oldHouseDetail.setBalconyNum(newHouseDetail.getBalconyNum());

		// oldHouseDetail.setProvince(newHouseDetail.getProvince());
		// oldHouseDetail.setCity(newHouseDetail.getCity());
		// oldHouseDetail.setDistrict(newHouseDetail.getDistrict());
		// oldHouseDetail.setBizName(newHouseDetail.getBizName());

		oldHouseDetail.setHouseFunction(newHouseDetail.getHouseFunction());
		// oldHouseDetail.setAddress(newHouseDetail.getAddress());
		oldHouseDetail.setCommunityName(newHouseDetail.getCommunityName());

		// oldHouseDetail.setSubway(newHouseDetail.getSubway());
		// oldHouseDetail.setSubwayDistance(newHouseDetail.getSubwayDistance());
		// oldHouseDetail.setBusStations(newHouseDetail.getBusStations());
		// oldHouseDetail.setSurround(newHouseDetail.getSurround());

		// oldHouseDetail.setCityId(newHouseDetail.getCityId());
		// oldHouseDetail.setDistrictId(newHouseDetail.getDistrictId());
		// oldHouseDetail.setBizId(newHouseDetail.getBizId());
		// oldHouseDetail.setSubwayLineId(newHouseDetail.getSubwayLineId());
		// oldHouseDetail.setSubwayStationId(newHouseDetail.getSubwayStationId());

		oldHouseDetail.setBaiduLatitude(newHouseDetail.getBaiduLatitude());
		oldHouseDetail.setBaiduLongitude(newHouseDetail.getBaiduLongitude());

		oldHouseDetail.setBuildingType(newHouseDetail.getBuildingType());

		oldHouseDetail.setBuildingYear(newHouseDetail.getBuildingYear());

		// oldHouseDetail.setToilet(newHouseDetail.getToilet());
		// oldHouseDetail.setBalcony(newHouseDetail.getBalcony());
		// oldHouseDetail.setInsurance(newHouseDetail.getInsurance());

		oldHouseDetail.setDecoration(newHouseDetail.getDecoration());
		oldHouseDetail.setEntireRent(newHouseDetail.getEntireRent());
		oldHouseDetail.setComment(newHouseDetail.getComment());
		// oldHouseDetail.setHouseTag(newHouseDetail.getHouseTag());

		// oldHouseDetail.setSource(newHouseDetail.getSource());

		oldHouseDetail.setUpdateTime(newHouseDetail.getUpdateTime());
		oldHouseDetail.setIsTop(newHouseDetail.getIsTop());
		oldHouseDetail.setFocusCode(newHouseDetail.getFocusCode());
		oldHouseDetail.setHouseType(newHouseDetail.getHouseType());
		// 更新信息后，需要重新进行数据采集
		oldHouseDetail.setIsRun(Constants.HouseDetail.HOUSE_UNRUN);
		// saas发布类型更新
		oldHouseDetail.setPubType(newHouseDetail.getPubType());
		// 更新房源时改为未审核,下架状态除外
		if (status != Constants.HouseBase.PUBLISH_STATUS_RENT) {
			oldHouseDetail.setApproveStatus(Constants.HouseDetail.NOT_APPROVE);
		}
		return oldHouseDetail;
	}

	/**
	 * 从房源DTO获取房源设置信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(HousePublishDto housePublishDto, HouseBase houseBase) {
		if (housePublishDto == null) {
			return null;
		}
		if (houseBase == null) {
			return null;
		}
		String settings = housePublishDto.getSettings();
		String settingsAddon = housePublishDto.getSettingsAddon();
		String sellId = houseBase.getSellId();
		return getHouseSettingList(settings, settingsAddon, sellId, null);
	}

	/**
	 * 从房源DTO获取房源设置信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(RoomPublishDto roomPublishDto, RoomBase roomBase) {
		if (roomPublishDto == null) {
			return null;
		}
		if (roomBase == null) {
			return null;
		}
		String settings = roomPublishDto.getSettings();
		String settingsAddon = roomPublishDto.getSettingsAddon();
		String sellId = roomBase.getSellId();
		long roomId = roomBase.getId();
		return getHouseSettingList(settings, settingsAddon, sellId, roomId);
	}

	/**
	 * 从房源DTO获取房源设置信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(String settings, String addonSettings, String sellId,
			Long roomId) {
		if (StringUtil.isEmpty(sellId)) {
			return null;
		}

		List<HouseSetting> houseSettingList = new ArrayList<HouseSetting>();

		// 主要配置
		if (StringUtil.isNotEmpty(settings)) {
			List<HouseSetting> mainSettingList = getHouseSettingList(settings, sellId, roomId,
					Constants.HouseSetting.CATEGORY_PRIMARY);
			if (CollectionUtils.isNotEmpty(mainSettingList)) {
				houseSettingList.addAll(mainSettingList);
			}
		}

		// 次要配置
		if (StringUtil.isNotEmpty(addonSettings)) {
			List<HouseSetting> addonSettingList = getHouseSettingList(addonSettings, sellId, roomId,
					Constants.HouseSetting.CATEGORY_SECONDARY);
			if (CollectionUtils.isNotEmpty(addonSettingList)) {
				houseSettingList.addAll(addonSettingList);
			}
		}

		return houseSettingList;
	}

	/**
	 * 从房源DTO获取房源图片
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static List<HousePicture> getHousePictureList(HousePublishDto housePublishDto, HouseBase houseBase) {
		if (housePublishDto == null || houseBase == null) {
			return null;
		}

		String imgs = housePublishDto.getImgs();
		String sellId = houseBase.getSellId();

		return getHousePictureList(imgs, sellId, null);

	}

	/**
	 * 从房源DTO获取房源图片
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static List<HousePicture> getHousePictureList(RoomPublishDto roomPublishDto, RoomBase roomBase) {
		if (roomPublishDto == null || roomBase == null) {
			return null;
		}

		String imgs = roomPublishDto.getImgs();

		String sellId = roomBase.getSellId();

		long roomId = roomBase.getId();

		return getHousePictureList(imgs, sellId, roomId);

	}

	/**
	 * 根据设置字符串获取房源设置列表
	 * 
	 * @param settingStr
	 * @return
	 */
	public static List<HouseSetting> getHouseSettingList(String settingStr, String sellId, Long roomId,
			int settingCategory) {

		if (StringUtil.isEmpty(settingStr)) {
			return null;
		}

		if (StringUtil.isEmpty(sellId)) {
			return null;
		}

		List<HouseSetting> houseSettingList = new ArrayList<HouseSetting>();

		StringTokenizer st = new StringTokenizer(settingStr, StringUtil.COMMA);
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			HouseSetting houseSetting = getHouseSetting(token, sellId, roomId, settingCategory);
			if (houseSetting != null) {
				houseSettingList.add(houseSetting);
			}
		}

		return houseSettingList;
	}

	/**
	 * 根据设置字符串获取房源设置列表
	 * 
	 * @param settingStr
	 * @return
	 */
	public static List<HousePicture> getHousePictureList(String pictureStr, String sellId, Long roomId) {

		if (StringUtil.isEmpty(pictureStr)) {
			return null;
		}

		if (StringUtil.isEmpty(sellId)) {
			return null;
		}

		List<HousePicture> housePictureList = new ArrayList<HousePicture>();

		StringTokenizer st = new StringTokenizer(pictureStr, StringUtil.COMMA);
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			HousePicture housePicture = getHousePicture(token, sellId, roomId);
			if (housePicture != null) {
				housePictureList.add(housePicture);
			}
		}

		return housePictureList;
	}

	/**
	 * 获取单个房源设置
	 * 
	 * @param settingStr
	 * @param houseBase
	 * @return
	 */
	public static HouseSetting getHouseSetting(String settingStr, String sellId, Long roomId, int categoryType) {

		if (StringUtil.isEmpty(settingStr)) {
			return null;
		}

		String[] split = StringUtil.split(settingStr, StringUtil.COLON);
		if (split == null || split.length != 2) {
			return null;
		}
		String settingKey = split[0];
		String settingNumValue = split[1];

		HouseSetting houseSetting = new HouseSetting();

		// int settingType = Constants.HouseSetting.getSettingType(settingKey);
		int settingType = SettingsEnum.getSettingType(settingKey);
		if (settingType == Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED) {
			logger.error(LogUtils.getCommLog("不识别的房源设置类型:" + settingKey));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_INVALID_HOUSE_SETTING_TYPE, "不识别的房源设置类型");
		}
		houseSetting.setCategoryType(settingType);

		// int settingCode = Constants.HouseSetting.getSettingCode(settingKey);
		int settingCode = SettingsEnum.getSettingCode(settingKey);
		if (settingCode == Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED) {
			logger.error(LogUtils.getCommLog("不识别的房源设置编号:" + settingKey));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_INVALID_HOUSE_SETTING_TYPE, "不识别的房源设置类型");
		}
		houseSetting.setSettingCode(settingCode); // 物品Id

		int settingNum = StringUtil.parseInt(settingNumValue, 0);

		houseSetting.setSellId(sellId);

		if (roomId != null) {
			houseSetting.setRoomId(roomId);
		}

		houseSetting.setSettingNum(settingNum);

		houseSetting.setSettingPosition(0); // 配置位置(暂时不用)

		String settingName = StringUtil.EMPTY;
		houseSetting.setSettingName(settingName); // 设置名称(后台抓取)

		houseSetting.setSettingPrice(0); // 价格(后台抓取)
		houseSetting.setSettingCost(0); // 成本(后台抓取)
		houseSetting.setIsCompleted(Constants.HouseSetting.IS_COMPLETE_NO);

		Date date = new Date();
		houseSetting.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		houseSetting.setCreateTime(date);
		houseSetting.setUpdateTime(date);

		return houseSetting;
	}

	/**
	 * 获取单个房源图片
	 * 
	 * @param settingStr
	 * @param houseBase
	 * @return
	 */
	public static HousePicture getHousePicture(String pictureStr, String sellId, Long roomId) {

		if (StringUtil.isEmpty(pictureStr)) {
			return null;
		}

		HousePicture housePicture = new HousePicture();

		housePicture.setSellId(sellId);

		if (roomId != null) {
			housePicture.setRoomId(roomId);
		}

		housePicture.setPicWebPath(pictureStr);

		housePicture.setPicRootPath(StringUtil.EMPTY);

		Date date = new Date();
		housePicture.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		housePicture.setCreateTime(date);
		housePicture.setUpdateTime(date);

		return housePicture;

	}

	/**
	 * 分析新旧房源设置
	 * 
	 * @param oldHouseSettings
	 * @param newHouseSettings
	 * @param addSettings
	 * @param updateSettings
	 * @param deleteSettings
	 */
	public static void analyzeHouseSettings(List<HouseSetting> oldHouseSettings, List<HouseSetting> newHouseSettings,
			List<HouseSetting> addSettings, List<HouseSetting> updateSettings, List<HouseSetting> deleteSettings) {

		if (CollectionUtils.isEmpty(newHouseSettings)) { // 新设置为空
			if (CollectionUtils.isEmpty(oldHouseSettings)) { // 原有设置为空
				return;
			} else {
				deleteSettings.addAll(oldHouseSettings);
			}
		} else { // 新设置不为空

			if (CollectionUtils.isEmpty(oldHouseSettings)) { // 原有设置空
				addSettings.addAll(newHouseSettings);
			} else {

				for (HouseSetting newSetting : newHouseSettings) {
					int categoryType = newSetting.getCategoryType();
					int settingCode = newSetting.getSettingCode();
					HouseSetting oldSetting = getHouseSettingFromList(oldHouseSettings, categoryType, settingCode);
					if (oldSetting == null) { // 新增
						addSettings.add(newSetting);
					}
				}

				for (HouseSetting oldSetting : oldHouseSettings) {
					int categoryType = oldSetting.getCategoryType();
					int settingCode = oldSetting.getSettingCode();
					HouseSetting newSetting = getHouseSettingFromList(newHouseSettings, categoryType, settingCode);
					if (newSetting != null) { // 修改
						boolean isChanged = isSettingChanged(oldSetting, newSetting);
						if (isChanged) {
							oldSetting = updateHouseSetting(oldSetting, newSetting);
							updateSettings.add(oldSetting);
						}
					} else { // 删除
						deleteSettings.add(oldSetting);
					}
				}
			}
		}

	}

	/**
	 * 从列表中查询设置
	 * 
	 * @param houseSettings
	 * @param categoryType
	 * @param settingCode
	 * @return
	 */
	public static HouseSetting getHouseSettingFromList(List<HouseSetting> houseSettings, int categoryType,
			int settingCode) {
		if (CollectionUtils.isEmpty(houseSettings)) {
			return null;
		}
		for (HouseSetting setting : houseSettings) {
			int queryCategoryType = setting.getCategoryType();
			int querySettingCode = setting.getSettingCode();
			if (categoryType == queryCategoryType && settingCode == querySettingCode) {
				return setting;
			}
		}
		return null;
	}

	/**
	 * 设置是否变更
	 * 
	 * @param oldSetting
	 * @param newSetting
	 * @return
	 */
	public static boolean isSettingChanged(HouseSetting oldSetting, HouseSetting newSetting) {

		int oldNum = oldSetting.getSettingNum();
		int newNum = newSetting.getSettingNum();
		boolean isNumEquals = oldNum == newNum;
		if (!isNumEquals) {
			return true;
		}
		return false;
	}

	/**
	 * 更新设置
	 * 
	 * @param oldSetting
	 * @param newSetting
	 * @return
	 */
	public static HouseSetting updateHouseSetting(HouseSetting oldSetting, HouseSetting newSetting) {

		oldSetting.setSettingNum(newSetting.getSettingNum());
		oldSetting.setUpdateTime(new Date());

		return oldSetting;

	}

	/**
	 * 分析新旧房源图片
	 * 
	 * @param oldHousePics
	 * @param newHousePics
	 * @param addPics
	 * @param updatePics
	 * @param deletePics
	 */
	public static void analyzeHousePics(List<HousePicture> oldHousePics, List<HousePicture> newHousePics,
			List<HousePicture> addPics, List<HousePicture> deletePics, boolean isShare) {

		if (CollectionUtils.isEmpty(newHousePics)) { // 新图片空
			if (CollectionUtils.isEmpty(oldHousePics)) { // 原有图片空
				return;
			} else {
				deletePics.addAll(oldHousePics);
			}
		} else { // 新图片不为空
			if (CollectionUtils.isEmpty(oldHousePics)) { // 原有图片空
				addPics.addAll(newHousePics);
			} else {

				for (HousePicture newPic : newHousePics) {
					String path = newPic.getPicWebPath();
					HousePicture oldPic = getHousePictureFromList(oldHousePics, path);
					if (oldPic == null) { // 新增
						addPics.add(newPic);
					}
				}

				boolean isFirstPic = true;
				for (HousePicture oldPic : oldHousePics) {
					String path = oldPic.getPicWebPath();
					HousePicture newPic = getHousePictureFromList(newHousePics, path);
					if (newPic != null) { // 修改
						// do nothing
					} else { // 删除
						deletePics.add(oldPic);
						// 如果首图被删除，重新设一张首图
						boolean flag = (!isShare) && isFirstPic && oldPic.getIsDefault() == 1 && CollectionUtils.isNotEmpty(addPics);
						if (flag) {
							addPics.get(0).setIsDefault(1);
							isFirstPic = false;
						}
					}
				}
			}
		}
	}

	public static HousePicture getHousePictureFromList(List<HousePicture> housePics, String path) {
		if (CollectionUtils.isEmpty(housePics)) {
			return null;
		}
		for (HousePicture pic : housePics) {
			String picPath = pic.getPicWebPath();
			if (StringUtil.isStringEquals(picPath, path)) {
				return pic;
			}
		}
		return null;
	}

	/**
	 * 从投诉DTO获取投诉信息
	 * 
	 * @param housePublishDto
	 * @return
	 */
	public static Complaint getComplaint(ComplaintSaveDto complaintSaveDto) {
		if (complaintSaveDto == null) {
			return null;
		}

		Complaint complaint = new Complaint();

		complaint.setSellId(complaintSaveDto.getSellId());
		complaint.setRoomId(complaintSaveDto.getRoomId());
		complaint.setUid(complaintSaveDto.getUid());
		complaint.setComment(complaintSaveDto.getComment());
		complaint.setComplaint(complaintSaveDto.getComplaint());

		Date date = new Date();
		complaint.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);
		complaint.setCreateTime(date);
		complaint.setUpdateTime(date);

		return complaint;
	}

	/**
	 * 获取投诉查询返回对象
	 * 
	 * @param complaintList
	 * @return
	 */
	public static ComplaintQueryDto getComplaintQueryDto(List<Complaint> complaintList) {
		ComplaintQueryDto complaintQueryDto = new ComplaintQueryDto();

		List<ComplaintInfo> complaintInfoList = new ArrayList<ComplaintInfo>();
		if (CollectionUtils.isNotEmpty(complaintList)) {
			for (Complaint complaint : complaintList) {
				ComplaintInfo complaintQueryInfo = getComplaintInfo(complaint);
				if (complaintQueryInfo != null) {
					complaintInfoList.add(complaintQueryInfo);
				}
			}
		}
		complaintQueryDto.setComplaints(complaintInfoList);

		return complaintQueryDto;
	}

	/**
	 * 获取单个投诉信息
	 * 
	 * @param complaint
	 * @return
	 */
	public static ComplaintInfo getComplaintInfo(Complaint complaint) {
		if (complaint == null) {
			return null;
		}
		ComplaintInfo complaintQueryInfo = new ComplaintInfo();

		complaintQueryInfo.setComplaintId(complaint.getId());
		complaintQueryInfo.setSellId(complaint.getSellId());
		complaintQueryInfo.setRoomId(complaint.getRoomId());
		complaintQueryInfo.setUid(complaint.getUid());

		int complaintType = complaint.getComplaint();
		String complaintTypeName = Constants.Complaint.getComplaintType(complaintType);
		complaintQueryInfo.setComplaint(complaintTypeName);

		String comment = complaint.getComment();
		comment = comment == null ? StringUtil.EMPTY : comment;
		complaintQueryInfo.setComment(comment);

		complaintQueryInfo.setCreateDate(complaint.getCreateTime());
		complaintQueryInfo.setUpdateDate(complaint.getUpdateTime());

		return complaintQueryInfo;
	}

	/**
	 * 获取公寓查询返回对象
	 * 
	 * @param complaintList
	 * @return
	 */
	public static ApartmentQueryDto getApartmentQueryDto(List<Apartment> apartmentList) {
		ApartmentQueryDto apartmentQueryDto = new ApartmentQueryDto();

		List<ApartmentInfo> apartmentInfoList = new ArrayList<ApartmentInfo>();
		if (CollectionUtils.isNotEmpty(apartmentList)) {
			for (Apartment apartment : apartmentList) {
				ApartmentInfo apartmentQueryInfo = getApartmentInfo(apartment);
				if (apartmentQueryInfo != null) {
					apartmentInfoList.add(apartmentQueryInfo);
				}
			}
		}
		apartmentQueryDto.setApartments(apartmentInfoList);

		return apartmentQueryDto;
	}

	/**
	 * 获取单个公寓信息
	 * 
	 * @param complaint
	 * @return
	 */
	public static ApartmentInfo getApartmentInfo(Apartment apartment) {
		if (apartment == null) {
			return null;
		}
		ApartmentInfo apartmentInfo = new ApartmentInfo();
		apartmentInfo.setApartmentName(apartment.getName());
		apartmentInfo.setApartmentPic(apartment.getPicRootPath());

		int type = apartment.getType();
		String typeDesc = Constants.ApartmentStatus.getTypeDesc(type);
		apartmentInfo.setApartmentType(typeDesc);

		return apartmentInfo;
	}

	public static List<HouseSearchResultInfo> getHouseSearchResultInfoListByHouse(List<HouseSolrResult> solrResultList,
			List<String> collectIdList) {
		if (CollectionUtils.isEmpty(solrResultList)) {
			return null;
		}

		List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
		for (HouseSolrResult result : solrResultList) {
			HouseSearchResultInfo info = getHouseSearchResultInfo(result, collectIdList);
			if (info != null) {
				infoList.add(info);
			}
		}
		return infoList;
	}

	public static List<HouseSearchResultInfo> getHouseSearchResultInfoListByHouse(
			List<HouseSolrResult> solrResultList) {
		if (CollectionUtils.isEmpty(solrResultList)) {
			return null;
		}

		List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
		for (HouseSolrResult result : solrResultList) {
			HouseSearchResultInfo info = getHouseSearchResultInfo(result);
			if (info != null) {
				infoList.add(info);
			}
		}
		return infoList;
	}

	public static HouseSearchResultInfo getHouseSearchResultInfo(HouseSolrResult solrResult) {
		if (solrResult == null) {
			return null;
		}
		HouseSearchResultInfo info = new HouseSearchResultInfo();

		info.setSellId(solrResult.getSellId());
		info.setRoomId(0); // 房源查询结果,roomId为0
		info.setTitle(HouseUtil.getHouseListTitle(solrResult)); // 设置标题
		info.setPrice(solrResult.getPrice());
		info.setArea(getAreaStr((float) solrResult.getArea()));
		info.setHouseArea(getAreaStr((float) solrResult.getArea()));// 房源面积

		String flowNo = solrResult.getFlowNo();
		String flowTotal = solrResult.getFlowTotal();
		info.setFloorNo(flowNo);
		info.setFloorTotal(flowTotal);
		info.setFloor(HouseUtil.getFloorStr(flowNo, flowTotal)); // 设置楼层

		// String subway = solrResult.getSubway();
		// info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 设置地铁

		String subway = solrResult.getSubway();
		String bizName = solrResult.getBizName();
		if (!"".equals(subway) && subway != null) {
			info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 默认显示设置地铁
		} else {
			if (!"".equals(bizName) && bizName != null) {
				info.setSubway("位于  " + solrResult.getBizName());// 无地铁显示商圈
			} else {
				info.setSubway("");// 无商圈显示空
			}
		}

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getHouseTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			int count = 0;
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
					count++;
				}
				if (count == 4) {
					break;
				}
			}
		}
		info.setHouseTag(houseTag);

		List<String> imgs = solrResult.getImgs();
		String pic = StringUtil.EMPTY;
		if (CollectionUtils.isNotEmpty(imgs)) {
			pic = imgs.get(0); // TODO 设置图片
		}
		info.setPic(pic);

		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDesc = HouseUtil.getPubDesc(pubDate);
		info.setPubDesc(pubDesc); // 设置发布时间描述

		String pubStr = DateUtil.formatDateTime(pubDate);
		info.setPubDate(pubStr); // 设置发布时间

		info.setCommunityName(solrResult.getCommunityName());
		info.setSubTitle(StringUtil.EMPTY); // TODO 设置副标题
		info.setAddress(solrResult.getAddress());
		info.setLivingroomNums(solrResult.getLivingroomNums());
		info.setBedroomNums(solrResult.getBedroomNums());

		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		info.setDecoration(decorationName); // 装饰

		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		info.setEntireRent(entireRent);
		info.setEntireRentDesc(rentTypeName);

		double distance = solrResult.getDistance();
		distance = distance * 1000; // 千米转米
		int displayDistance = (int) distance;
		info.setDistance(displayDistance); // 设置距离

		// 2017-06-14 13:56:48 jjs
		String orieName = HouseUtil.getOrientationsStr(solrResult.getOrientations());
		info.setOrientationName(orieName);

		info.setIsTop(solrResult.getIsTop());// 是否置顶
		info.setCompanyId(solrResult.getCompanyId());// 公寓ID

		info.setRtName("");// 房间类型名称

		// 房源已出租标识：status不等于1；approveStatus不等于1或3
		int status = solrResult.getStatus();
		int approveStatus = solrResult.getApproveStatus();
		if (status != 1 || approveStatus != 1 || approveStatus != 3) {
			info.setRentFlag(1);
		} else {
			info.setRentFlag(0);
		}
		info.setSource(solrResult.getSource());
		return info;

	}

	public static HouseSearchResultInfo getHouseSearchResultInfo(HouseSolrResult solrResult,
			List<String> collectIdList) {
		if (solrResult == null) {
			return null;
		}
		HouseSearchResultInfo info = new HouseSearchResultInfo();

		info.setSellId(solrResult.getSellId());
		info.setRoomId(0); // 房源查询结果,roomId为0
		info.setTitle(HouseUtil.getHouseListTitle(solrResult)); // 设置标题
		info.setPrice(solrResult.getPrice());
		info.setArea(getAreaStr((float) solrResult.getArea()));
		info.setHouseArea(getAreaStr((float) solrResult.getArea()));// 房源面积

		String flowNo = solrResult.getFlowNo();
		String flowTotal = solrResult.getFlowTotal();
		info.setFloorNo(flowNo);
		info.setFloorTotal(flowTotal);
		info.setFloor(HouseUtil.getFloorStr(flowNo, flowTotal)); // 设置楼层

		// String subway = solrResult.getSubway();
		// info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 设置地铁

		String subway = solrResult.getSubway();
		String bizName = solrResult.getBizName();
		if (!"".equals(subway) && subway != null) {
			info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 默认显示设置地铁
		} else {
			if (!"".equals(bizName) && bizName != null) {
				info.setSubway("位于  " + solrResult.getBizName());// 无地铁显示商圈
			} else {
				info.setSubway("");// 无商圈显示空
			}
		}

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getHouseTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			int count = 0;
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
					count++;
				}
				if (count == 4) {
					break;
				}
			}
		}
		info.setHouseTag(houseTag);

		List<String> imgs = solrResult.getImgs();
		String pic = StringUtil.EMPTY;
		if (CollectionUtils.isNotEmpty(imgs)) {
			pic = imgs.get(0); // TODO 设置图片
		}
		info.setPic(pic);

		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDesc = HouseUtil.getPubDesc(pubDate);
		info.setPubDesc(pubDesc); // 设置发布时间描述

		String pubStr = DateUtil.formatDateTime(pubDate);
		info.setPubDate(pubStr); // 设置发布时间

		info.setCommunityName(solrResult.getCommunityName());
		info.setSubTitle(StringUtil.EMPTY); // TODO 设置副标题
		info.setAddress(solrResult.getAddress());
		info.setLivingroomNums(solrResult.getLivingroomNums());
		info.setBedroomNums(solrResult.getBedroomNums());

		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		info.setDecoration(decorationName); // 装饰

		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		info.setEntireRent(entireRent);
		info.setEntireRentDesc(rentTypeName);

		double distance = solrResult.getDistance();
		distance = distance * 1000; // 千米转米
		int displayDistance = (int) distance;
		info.setDistance(displayDistance); // 设置距离

		// 2017-06-14 13:56:48 jjs
		String orieName = HouseUtil.getOrientationsStr(solrResult.getOrientations());
		info.setOrientationName(orieName);

		info.setIsTop(solrResult.getIsTop());// 是否置顶
		info.setCompanyId(solrResult.getCompanyId());// 公寓ID

		info.setRtName("");// 房间类型名称

		// 添加收藏标识
		if (CollectionUtils.isNotEmpty(collectIdList) && collectIdList.contains(solrResult.getSellId())) {
			info.setCollectFlag(1);
		} else {
			info.setCollectFlag(0);
		}
		// 房源已出租标识：status不等于1；approveStatus不等于1或3
		int status = solrResult.getStatus();
		int approveStatus = solrResult.getApproveStatus();
		if (status != 1 || approveStatus != 1 || approveStatus != 3) {
			info.setRentFlag(1);
		} else {
			info.setRentFlag(0);
		}
		info.setSource(solrResult.getSource());
		return info;

	}

	public static List<HouseSearchResultInfo> getHouseSearchResultInfoListByRoom(List<RoomSolrResult> solrResultList,
			List<String> collectIdList) {
		if (CollectionUtils.isEmpty(solrResultList)) {
			return null;
		}

		List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
		for (RoomSolrResult result : solrResultList) {
			HouseSearchResultInfo info = getHouseSearchResultInfo(result, collectIdList);
			if (info != null) {
				infoList.add(info);
			}
		}
		return infoList;
	}

	public static List<HouseSearchResultInfo> getHouseSearchResultInfoListByRoom(List<RoomSolrResult> solrResultList) {
		if (CollectionUtils.isEmpty(solrResultList)) {
			return null;
		}

		List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
		for (RoomSolrResult result : solrResultList) {
			HouseSearchResultInfo info = getHouseSearchResultInfo(result);
			if (info != null) {
				infoList.add(info);
			}
		}
		return infoList;
	}

	public static HouseSearchResultInfo getHouseSearchResultInfo(RoomSolrResult solrResult) {
		if (solrResult == null) {
			return null;
		}
		HouseSearchResultInfo info = new HouseSearchResultInfo();

		info.setSellId(solrResult.getSellId());
		info.setRoomId(solrResult.getId());
		info.setTitle(HouseUtil.getHouseListTitle(solrResult)); // 设置标题
		info.setPrice(solrResult.getPrice());
		info.setArea(getAreaStr((float) solrResult.getArea()));

		String flowNo = solrResult.getFlowNo();
		String flowTotal = solrResult.getFlowTotal();
		info.setFloorNo(flowNo);
		info.setFloorTotal(flowTotal);
		info.setFloor(HouseUtil.getFloorStr(flowNo, flowTotal)); // 设置楼层

		// String subway = solrResult.getSubway();
		// info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 设置地铁

		String subway = solrResult.getSubway();
		String bizName = solrResult.getBizName();
		if (!"".equals(subway) && subway != null) {
			info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 默认显示设置地铁
		} else {
			if (!"".equals(bizName) && bizName != null) {
				info.setSubway("位于  " + solrResult.getBizName());// 无地铁显示商圈
			} else {
				info.setSubway("");// 无商圈显示空
			}
		}

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getRoomTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			int count = 0;
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
					count++;
				}
				if (count == 4) {
					break;
				}
			}
		}
		info.setHouseTag(houseTag);

		List<String> imgs = solrResult.getImgs();
		String pic = StringUtil.EMPTY;
		if (CollectionUtils.isNotEmpty(imgs)) {
			pic = imgs.get(0); // TODO 设置图片
		}
		info.setPic(pic);

		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDesc = HouseUtil.getPubDesc(pubDate);
		info.setPubDesc(pubDesc); // 设置发布时间描述

		String pubStr = DateUtil.formatDateTime(pubDate);
		info.setPubDate(pubStr); // 设置发布时间

		info.setCommunityName(solrResult.getCommunityName());
		info.setSubTitle(StringUtil.EMPTY); // TODO 设置副标题
		info.setAddress(solrResult.getAddress());
		info.setLivingroomNums(solrResult.getLivingroomNums());
		info.setBedroomNums(solrResult.getBedroomNums());

		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		info.setDecoration(decorationName); // 装饰

		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		info.setEntireRent(entireRent);
		info.setEntireRentDesc(rentTypeName);
		info.setDistance(0); // TODO 设置距离
		// 2017-06-14 14:09:59 jjs
		String orieName = HouseUtil.getOrientationsStr(solrResult.getOrientations());
		info.setOrientationName(orieName);

		info.setIsTop(solrResult.getIsTop());// 是否置顶
		info.setHouseArea(getAreaStr((float) solrResult.getHouseArea()));// 房源面积

		if ("优化间".equals(solrResult.getRtName())) {
			info.setRtName("次卧");// 房间类型名称
		} else if ("其他".equals(solrResult.getRtName())) {
			info.setRtName("");// 房间类型名称
		} else {
			info.setRtName(solrResult.getRtName());// 房间类型名称
		}

		info.setCompanyId(solrResult.getCompanyId());// 公寓ID
		// 房源已出租标识：status不等于1；approveStatus不等于1或3
		int status = solrResult.getStatus();
		int approveStatus = solrResult.getRoomApproveStatus();
		if (status == 1 && approveStatus == 1 || approveStatus == 3) {
			info.setRentFlag(0);
		} else {
			info.setRentFlag(1);
		}
		info.setSource(solrResult.getSource());
		return info;

	}

	public static HouseSearchResultInfo getHouseSearchResultInfo(RoomSolrResult solrResult,
			List<String> collectIdList) {
		if (solrResult == null) {
			return null;
		}
		HouseSearchResultInfo info = new HouseSearchResultInfo();

		info.setSellId(solrResult.getSellId());
		info.setRoomId(solrResult.getId());
		info.setTitle(HouseUtil.getHouseListTitle(solrResult)); // 设置标题
		info.setPrice(solrResult.getPrice());
		info.setArea(getAreaStr((float) solrResult.getArea()));

		String flowNo = solrResult.getFlowNo();
		String flowTotal = solrResult.getFlowTotal();
		info.setFloorNo(flowNo);
		info.setFloorTotal(flowTotal);
		info.setFloor(HouseUtil.getFloorStr(flowNo, flowTotal)); // 设置楼层

		// String subway = solrResult.getSubway();
		// info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 设置地铁

		String subway = solrResult.getSubway();
		String bizName = solrResult.getBizName();
		if (!"".equals(subway) && subway != null) {
			info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 默认显示设置地铁
		} else {
			if (!"".equals(bizName) && bizName != null) {
				info.setSubway("位于  " + solrResult.getBizName());// 无地铁显示商圈
			} else {
				info.setSubway("");// 无商圈显示空
			}
		}

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getRoomTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			int count = 0;
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
					count++;
				}
				if (count == 4) {
					break;
				}
			}
		}
		info.setHouseTag(houseTag);

		List<String> imgs = solrResult.getImgs();
		String pic = StringUtil.EMPTY;
		if (CollectionUtils.isNotEmpty(imgs)) {
			pic = imgs.get(0); // TODO 设置图片
		}
		info.setPic(pic);

		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDesc = HouseUtil.getPubDesc(pubDate);
		info.setPubDesc(pubDesc); // 设置发布时间描述

		String pubStr = DateUtil.formatDateTime(pubDate);
		info.setPubDate(pubStr); // 设置发布时间

		info.setCommunityName(solrResult.getCommunityName());
		info.setSubTitle(StringUtil.EMPTY); // TODO 设置副标题
		info.setAddress(solrResult.getAddress());
		info.setLivingroomNums(solrResult.getLivingroomNums());
		info.setBedroomNums(solrResult.getBedroomNums());

		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		info.setDecoration(decorationName); // 装饰

		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		info.setEntireRent(entireRent);
		info.setEntireRentDesc(rentTypeName);
		info.setDistance(0); // TODO 设置距离
		// 2017-06-14 14:09:59 jjs
		String orieName = HouseUtil.getOrientationsStr(solrResult.getOrientations());
		info.setOrientationName(orieName);

		info.setIsTop(solrResult.getIsTop());// 是否置顶
		info.setHouseArea(getAreaStr((float) solrResult.getHouseArea()));// 房源面积

		if ("优化间".equals(solrResult.getRtName())) {
			info.setRtName("次卧");// 房间类型名称
		} else if ("其他".equals(solrResult.getRtName())) {
			info.setRtName("");// 房间类型名称
		} else {
			info.setRtName(solrResult.getRtName());// 房间类型名称
		}

		info.setCompanyId(solrResult.getCompanyId());// 公寓ID
		// 添加收藏标识
		if (CollectionUtils.isNotEmpty(collectIdList) && collectIdList.contains(solrResult.getSellId())) {
			info.setCollectFlag(1);
		} else {
			info.setCollectFlag(0);
		}
		// 房源已出租标识：status不等于1；approveStatus不等于1或3
		int status = solrResult.getStatus();
		int approveStatus = solrResult.getRoomApproveStatus();
		if (status == 1 && approveStatus == 1 || approveStatus == 3) {
			info.setRentFlag(0);
		} else {
			info.setRentFlag(1);
		}
		info.setSource(solrResult.getSource());
		return info;

	}

	public static List<HouseSearchResultInfo> getHzfHousesSearchResultInfo(List<HzfHousesSolrResult> solrResultList,
			List<String> collectIdList) {
		if (CollectionUtils.isEmpty(solrResultList)) {
			return null;
		}

		List<HouseSearchResultInfo> infoList = new ArrayList<HouseSearchResultInfo>();
		for (HzfHousesSolrResult result : solrResultList) {
			HouseSearchResultInfo info = getHzfHousesSearchResultInfo(result, collectIdList);
			if (info != null) {
				infoList.add(info);
			}
		}
		return infoList;
	}

	public static HouseSearchResultInfo getHzfHousesSearchResultInfo(HzfHousesSolrResult solrResult,
			List<String> collectIdList) {
		if (solrResult == null) {
			return null;
		}
		HouseSearchResultInfo info = new HouseSearchResultInfo();

		info.setSellId(solrResult.getSellId());
		info.setRoomId(solrResult.getRoomId());
		info.setTitle(HouseUtil.getHzfHousesListTitle(solrResult)); // 设置标题
		info.setPrice(solrResult.getPrice());
		info.setArea(getAreaStr((float) solrResult.getArea()));
		String flowNo = solrResult.getFlowNo();
		String flowTotal = solrResult.getFlowTotal();
		info.setFloorNo(flowNo);
		info.setFloorTotal(flowTotal);
		info.setFloor(HouseUtil.getFloorStr(flowNo, flowTotal)); // 设置楼层

		// String subway = solrResult.getSubway();
		// info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 设置地铁

		String subway = solrResult.getSubway();
		String bizName = solrResult.getBizName();
		if (!"".equals(subway) && subway != null) {
			info.setSubway(HouseUtil.getSimplifiedSubway(subway)); // 默认显示设置地铁
		} else {
			if (!"".equals(bizName) && bizName != null) {
				info.setSubway("位于  " + solrResult.getBizName());// 无地铁显示商圈
			} else {
				info.setSubway("");// 无商圈显示空
			}
		}

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getHouseTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			int count = 0;
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
					count++;
				}
				if (count == 4) {
					break;
				}
			}
		}
		info.setHouseTag(houseTag);

		List<String> imgs = solrResult.getImgs();
		String pic = StringUtil.EMPTY;
		if (CollectionUtils.isNotEmpty(imgs)) {
			pic = imgs.get(0); // TODO 设置图片
		}
		info.setPic(pic);

		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDesc = HouseUtil.getPubDesc(pubDate);
		info.setPubDesc(pubDesc); // 设置发布时间描述

		String pubStr = DateUtil.formatDateTime(pubDate);
		info.setPubDate(pubStr); // 设置发布时间

		info.setCommunityName(solrResult.getCommunityName());
		info.setSubTitle(StringUtil.EMPTY); // TODO 设置副标题
		info.setAddress(solrResult.getAddress());
		info.setLivingroomNums(solrResult.getLivingroomNums());
		info.setBedroomNums(solrResult.getBedroomNums());

		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		info.setDecoration(decorationName); // 装饰

		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		info.setEntireRent(entireRent);
		info.setEntireRentDesc(rentTypeName);
		info.setDistance(0); // TODO 设置距离
		// 2017-06-14 14:09:59 jjs
		String orieName = HouseUtil.getOrientationsStr(solrResult.getOrientations());
		info.setOrientationName(orieName);

		info.setIsTop(solrResult.getIsTop());// 是否置顶
		info.setHouseArea(getAreaStr((float) solrResult.getHouseArea()));// 房源面积

		if ("优化间".equals(solrResult.getRtName())) {
			info.setRtName("次卧");// 房间类型名称
		} else if ("其他".equals(solrResult.getRtName())) {
			info.setRtName("");// 房间类型名称
		} else {
			info.setRtName(solrResult.getRtName());// 房间类型名称
		}

		info.setCompanyId(solrResult.getCompanyId());// 公寓ID
		// 添加收藏标识
		if (collectIdList.contains(solrResult.getSellId())) {
			info.setCollectFlag(1);
		} else {
			info.setCollectFlag(0);
		}
		// 房源已出租标识：status不等于1；approveStatus不等于1或3
		int status = solrResult.getStatus();
		int approveStatus = solrResult.getApproveStatus();
		if (status == 1 && approveStatus == 1 || approveStatus == 3) {
			info.setRentFlag(0);
		} else {
			info.setRentFlag(1);
		}
		info.setSource(solrResult.getSource());
		return info;
	}

	/**
	 * 获取房源详情
	 * 
	 * @param roomSolrResult
	 * @return
	 */
	public static HouseQueryDto getHouseQueryDto(HouseSolrResult solrResult, int communityHouseCount,
			int companyHouseCount, int companyCityCount, int collectFlag, List<String> agencyIdList, String companyName,
			String imageCss) {
		if (solrResult == null) {
			return null;
		}
		HouseQueryDto houseQueryDto = new HouseQueryDto();

		houseQueryDto.setPrice(solrResult.getPrice());
		houseQueryDto.setBonus(0);
		houseQueryDto.setHasKey(0);

		houseQueryDto.setAgencyId(0);

		// TODO 经纪公司相关信息
		houseQueryDto.setCompanyId("0");
		houseQueryDto.setCompanyName(StringUtil.EMPTY);
		houseQueryDto.setCompanyCredit(StringUtil.EMPTY);
		houseQueryDto.setAgencyPhone(solrResult.getAgencyPhone());
		houseQueryDto.setAgencyExt(StringUtil.EMPTY);
		houseQueryDto.setAgencyName(StringUtil.EMPTY);
		houseQueryDto.setAgencyIntroduce(StringUtil.EMPTY);
		houseQueryDto.setAgencyGender(0);

		// 房源状态
		int status = solrResult.getStatus();
		houseQueryDto.setStatus(status);
		houseQueryDto.setStatusDesc(Constants.HouseBase.getSimplifiedStatusDesc(status));

		Date checkinTime = SolrUtil.getSolrDate(solrResult.getCheckIn());
		String checkInStr = DateUtil.formatDate(checkinTime);
		checkInStr = StringUtil.defaultString(checkInStr);
		houseQueryDto.setCheckIn(checkInStr);

		// 押金形式
		int depositMonth = solrResult.getDepositMonth();
		int periodMonth = solrResult.getPeriodMonth();
		houseQueryDto.setDepositMonth(depositMonth);
		houseQueryDto.setPeriodMonth(periodMonth);
		String payType = HouseUtil.getPaytypeStr(depositMonth, periodMonth);
		houseQueryDto.setPayType(payType);

		// 收藏信息(暂不使用)
		houseQueryDto.setMark(0);

		// 房源详情标题
		houseQueryDto.setTitle(getHouseDetailTitle(solrResult, agencyIdList, companyName));

		// 发布时间
		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDateStr = DateUtil.formatDate(pubDate);
		pubDateStr = StringUtil.defaultString(pubDateStr);
		houseQueryDto.setPubDate(pubDateStr);

		// 最近更新时间
		Date updateTime = SolrUtil.getSolrDate(solrResult.getUpdateDate());
		String updateTimeStr = DateUtil.formatDate(updateTime);
		updateTimeStr = StringUtil.defaultString(updateTimeStr);
		houseQueryDto.setLastModifyDate(updateTimeStr);

		// 经纬度
		String baiduLongitude = solrResult.getBaiduLongitude(); // 经度
		String baiduLatitude = solrResult.getBaiduLatitude(); // 纬度
		String position = HouseUtil.getPosition(baiduLongitude, baiduLatitude);
		houseQueryDto.setPosition(position);
		houseQueryDto.setPositionX(baiduLatitude);
		houseQueryDto.setPositionY(baiduLongitude);

		// 城市Id
		houseQueryDto.setCityId(solrResult.getCityId());

		// 地铁,公交,周边
		if (StringUtil.isBlank(solrResult.getSubway())) {
			houseQueryDto.setSubway("暂无地铁线路");
		} else {
			houseQueryDto.setSubway(solrResult.getSubway() + ";");
		}
		houseQueryDto.setSubwayDistance(solrResult.getSubwayDistance());
		if (StringUtil.isBlank(solrResult.getBusStations())) {
			houseQueryDto.setBusStations("暂无公交线路");
		} else {
			houseQueryDto.setBusStations(solrResult.getBusStations());
		}
		houseQueryDto.setSurround(solrResult.getSurround());

		// 地址
		houseQueryDto.setCommunityName(solrResult.getCommunityName());
		houseQueryDto.setAddress(solrResult.getAddress());

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getHouseTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
				}
			}
		}
		houseQueryDto.setHouseTag(houseTag);

		// 房源描述(几室几厅)
		int bedroomNum = solrResult.getBedroomNums();
		int livingroomNum = solrResult.getLivingroomNums();
		String roomDesc = HouseUtil.getRoomDesc(bedroomNum, livingroomNum);
		houseQueryDto.setRoom(roomDesc);

		// 面积
		houseQueryDto.setArea(HouseUtil.getAreaStr(solrResult.getArea()));

		// 房源面积
		if (solrResult.getArea() != 0) {
			houseQueryDto.setHouseArea(HouseUtil.getAreaStr(solrResult.getArea()));
		}

		// 楼层
		String floorNo = solrResult.getFlowNo();
		String floorTotal = solrResult.getFlowTotal();
		houseQueryDto.setFloor(HouseUtil.getFloorStr(floorNo, floorTotal));

		// 朝向
		int orientations = solrResult.getOrientations();
		String orientationsStr = HouseUtil.getOrientationsStr(orientations);
		houseQueryDto.setOrentation(orientationsStr);

		// 建筑类型
		houseQueryDto.setBuildingType(StringUtil.EMPTY);

		// 装修类型
		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		houseQueryDto.setFitmentType(decorationName);

		// 建筑时间
		int buildingYear = solrResult.getBuildingYear();
		houseQueryDto.setBuildingYear(String.valueOf(buildingYear));

		// 独立卫生间
		int toilet = solrResult.getToilet();
		houseQueryDto.setToilet(toilet);

		// 独立阳台
		int balcony = solrResult.getBalcony();
		houseQueryDto.setBalcony(balcony);

		// 保险
		int insurance = solrResult.getInsurance();
		String insuranceStr = HouseUtil.getInsuranceStr(insurance);
		houseQueryDto.setInsurance(insuranceStr);

		// 整租/合租
		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		houseQueryDto.setEntireRent(entireRent);
		houseQueryDto.setEntireRentDesc(rentTypeName);

		// TODO buildType
		houseQueryDto.setBuildType(StringUtil.EMPTY);

		// 房源描述
		String comment = solrResult.getComment();
		if (StringUtils.isEmpty(comment) || "无".equals(comment.trim())) {
			comment = Constants.Common.houseDesc;
		}
		houseQueryDto.setDesc(comment);

		// 来源
		String source = solrResult.getSource();
		houseQueryDto.setSource(source);

		// 设置
		List<Integer> settingCodes = solrResult.getSettingCodes();
		List<Integer> categoryTypes = solrResult.getCategoryTypes();
		List<Integer> settingNums = solrResult.getSettingNums();
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(settingCodes, categoryTypes, settingNums);
		List<SettingVo> settingsSortList = HouseUtil.getHouseSettingSortList(houseSettingList);
		houseQueryDto.setSettings(settingsSortList);

		// 新增同小区房源总数、公寓下所有房源总数、公寓下所有城市总数
		houseQueryDto.setCommunityHouseCount(communityHouseCount);
		houseQueryDto.setCompanyHouseCount(companyHouseCount);
		houseQueryDto.setCompanyCityCount(companyCityCount);

		// 查询该房源/房间 针对当前用户是否被收藏
		houseQueryDto.setCollectFlag(collectFlag);

		// 图片
		// 竖图压缩+水印
		String erectParams2 = "/resize,m_lfit,h_600,w_800/auto-orient,1/quality,q_90" + imageCss;
		// String erectParams2
		// ="/resize,m_lfit,h_600,w_800/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";

		// 原图水印
		// String bigParams
		// ="?x-oss-process=image/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";
		String bigParams = "?x-oss-process=image/auto-orient,1/quality,q_90" + imageCss;

		// 横图压缩+水印
		// String transversePrams =
		// "?x-oss-process=image/resize,m_fixed,h_600,w_800/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";
		String transversePrams = "?x-oss-process=image/resize,m_fixed,h_600,w_800/auto-orient,1/quality,q_90"
				+ imageCss;

		List<String> imgs = solrResult.getImgs();
		List<ImgLink> imgLinks = new ArrayList<ImgLink>();
		if (CollectionUtils.isNotEmpty(imgs)) {
			for (String imgUrl : imgs) {
				ImgLink link = new ImgLink();
				if (StringUtils.isNotBlank(imgUrl)) {
					if (imgUrl.contains("(")) {
						int oldHeigh = Integer.valueOf(imgUrl.substring(imgUrl.indexOf("(") + 1, imgUrl.indexOf(",")));
						int x = oldHeigh / 4;
						int w = Integer.valueOf(imgUrl.substring(imgUrl.indexOf(",") + 1, imgUrl.indexOf(")")));
						w = w / 100 * 99;//oss存储图片crop存在误差
						int h = w / 4 * 3;
						String erectParams1 = "?x-oss-process=image/crop,x_0,y_" + x + ",w_" + w + ",h_" + h;
						link.setBigImgUrl(imgUrl + bigParams);
						link.setSmallImgUrl(imgUrl + erectParams1 + erectParams2);
					} else {
						link.setBigImgUrl(imgUrl + bigParams);
						link.setSmallImgUrl(imgUrl + transversePrams);
					}
					imgLinks.add(link);
				}
			}
		}else{//临时解决ios房源详情无图
            ImgLink link = new ImgLink();
            link.setBigImgUrl(Constants.imageUtil.HOUUSE_DETAIL_IMG_DEFALUE);
            link.setSmallImgUrl(Constants.imageUtil.HOUUSE_DETAIL_IMG_DEFALUE);
            imgLinks.add(link);
		}
		
		
		houseQueryDto.setImgs(imgLinks);
		// add by arison 20170821
		if (StringUtil.isNotBlank(solrResult.getCompanyId()) && !"0".equals(solrResult.getCompanyId())) {
			houseQueryDto.setCompanyId(solrResult.getCompanyId());
		}
		houseQueryDto.setCompanyName(solrResult.getCompanyName());
		// 获取所有房间
		List<RoomQueryDto> referHouse = getReferHouse(solrResult, null);
		houseQueryDto.setReferHouse(referHouse);
		// 支持月付 1:支持月付；0:不支持月付
		houseQueryDto.setIsMonthlyPay(solrResult.getIsPayMonth());

		return houseQueryDto;
	}

	/**
	 * 获取房间列表
	 * 
	 * @param solrResult
	 * @return
	 */
	public static List<RoomQueryDto> getReferHouse(HouseSolrResult solrResult, Long excludeRoomId) {
		if (solrResult == null) {
			return null;
		}
		List<Integer> roomIds = solrResult.getRoomIds();
		if (CollectionUtils.isEmpty(roomIds)) {
			return null;
		}

		List<String> roomNames = solrResult.getRoomNames();
		if (CollectionUtils.isEmpty(roomNames)) {
			return null;
		}

		List<Integer> roomTypes = solrResult.getRoomTypes();
		if (CollectionUtils.isEmpty(roomTypes)) {
			return null;
		}

		List<Integer> roomPrices = solrResult.getRoomPrices();
		if (CollectionUtils.isEmpty(roomPrices)) {
			return null;
		}

		List<Integer> roomOrientations = solrResult.getRoomOrientations();
		if (CollectionUtils.isEmpty(roomOrientations)) {
			return null;
		}

		List<Double> roomAreas = solrResult.getRoomAreas();
		if (CollectionUtils.isEmpty(roomAreas)) {
			return null;
		}

		List<String> roomPubDates = solrResult.getRoomPubDates();
		if (CollectionUtils.isEmpty(roomPubDates)) {
			return null;
		}

		List<Integer> roomStatus = solrResult.getRoomStatus();
		if (CollectionUtils.isEmpty(roomStatus)) {
			return null;
		}

		List<Integer> roomDepositMonths = solrResult.getRoomDepositMonths();
		if (CollectionUtils.isEmpty(roomDepositMonths)) {
			return null;
		}

		List<Integer> roomPeriodMonths = solrResult.getRoomPeriodMonths();
		if (CollectionUtils.isEmpty(roomPeriodMonths)) {
			return null;
		}

		List<Integer> roomToilets = solrResult.getRoomToilets();
		if (CollectionUtils.isEmpty(roomToilets)) {
			return null;
		}

		List<Integer> roomBalconys = solrResult.getRoomBalconys();
		if (CollectionUtils.isEmpty(roomBalconys)) {
			return null;
		}

		List<Integer> roomApproveStatusList = solrResult.getRoomApproveStatus();
		if (CollectionUtils.isEmpty(roomApproveStatusList)) {
			return null;
		}

		String sellId = solrResult.getSellId();

		int idSize = roomIds.size();

		List<RoomQueryDto> roomQueryDtoList = new ArrayList<RoomQueryDto>();

		try {
			for (int i = 0; i < idSize; i++) {

				// 程序或者图片审核失败，不展示该套内房间
				Integer roomApproveStatus = roomApproveStatusList.get(i);
				if (roomApproveStatus != 1 && roomApproveStatus != 3) {
					continue;
				}

				RoomQueryDto roomQueryDto = new RoomQueryDto();
				roomQueryDto.setSellId(sellId);

				Integer roomId = roomIds.get(i);
				if (roomId == null) {
					continue;
				} else {
					roomQueryDto.setRoomId(roomId);
					if (excludeRoomId != null) {
						long excludeRoomIdValue = excludeRoomId;
						int roomIdValue = roomId;
						if (excludeRoomIdValue == roomIdValue) { // 当前房间不包含在其他房间内
							continue;
						}
					}
				}

				String name = roomNames.get(i);
				Integer type = roomTypes.get(i);
				if (type != null) {
					// roomQueryDto.setRoomType(Constants.RoomBase.getRoomType(type));
					roomQueryDto.setRoomType(RoomTypeEnum.getRoomTypeDesc(type));
				}

				Integer price = roomPrices.get(i);
				if (price != null) {
					roomQueryDto.setRentPriceMonth(price);
				}

				Integer orientation = roomOrientations.get(i);
				if (orientation != null) {
					String orientationsStr = HouseUtil.getOrientationsStr(orientation);
					roomQueryDto.setOrientations(orientationsStr);
				}

				Double area = roomAreas.get(i);
				if (area != null) {
					double areaValue = area;
					String areaStr = getAreaStr((float) areaValue);
					roomQueryDto.setArea(areaStr);
				}

				String pubdate = roomPubDates.get(i);
				if (pubdate != null) {
					Date solrDate = SolrUtil.getSolrDate(pubdate);
					String pubdateStr = DateUtil.formatDate(solrDate);
					roomQueryDto.setPubDate(pubdateStr);
				}

				Integer status = roomStatus.get(i);
				if (status != null) {
					roomQueryDto.setStatus(status);
					roomQueryDto.setStatusDesc(Constants.HouseBase.getSimplifiedStatusDesc(status));
				}

				if (orientation != null && status != null) {
					String roomTitle = getRoomTitle(status, name, orientation);
					roomQueryDto.setTitle(roomTitle);
				}

				Integer depositMonth = roomDepositMonths.get(i);
				Integer periodMonth = roomPeriodMonths.get(i);
				if (depositMonth != null && depositMonth != null) {
					String paytypeStr = HouseUtil.getPaytypeStr(depositMonth, periodMonth);
					roomQueryDto.setPayType(paytypeStr);
				}

				Integer toilet = roomToilets.get(i);
				if (toilet != null) {
					roomQueryDto.setToilet(toilet);
				}

				Integer balcony = roomBalconys.get(i);
				if (balcony != null) {
					roomQueryDto.setBalcony(balcony);
				}

				roomQueryDtoList.add(roomQueryDto);

			}
		} catch (IndexOutOfBoundsException e) {
			logger.error(LogUtils.getCommLog("HouseSolrResult房间数据异常" + e.getMessage()));
		}

		return roomQueryDtoList;

	}

	/**
	 * 获取房间详情
	 * 
	 * @param solrResult
	 * @return
	 */
	public static HouseQueryDto getHouseQueryDto(RoomSolrResult solrResult, int communityHouseCount,
			int companyHouseCount, int companyCityCount, int collectFlag, List<String> agencyIdList, String companyName,
			String imageCss) {
		if (solrResult == null) {
			return null;
		}
		HouseQueryDto houseQueryDto = new HouseQueryDto();

		houseQueryDto.setPrice(solrResult.getPrice());
		houseQueryDto.setBonus(0);
		houseQueryDto.setHasKey(0);

		houseQueryDto.setAgencyId(0);

		// TODO 经纪公司相关信息
		houseQueryDto.setCompanyId("0");
		houseQueryDto.setCompanyName(StringUtil.EMPTY);
		houseQueryDto.setCompanyCredit(StringUtil.EMPTY);
		houseQueryDto.setAgencyPhone(solrResult.getAgencyPhone());
		houseQueryDto.setAgencyExt(StringUtil.EMPTY);
		houseQueryDto.setAgencyName(StringUtil.EMPTY);
		houseQueryDto.setAgencyIntroduce(StringUtil.EMPTY);
		houseQueryDto.setAgencyGender(0);

		// 房源状态
		int status = solrResult.getStatus();
		houseQueryDto.setStatus(status);
		houseQueryDto.setStatusDesc(Constants.HouseBase.getSimplifiedStatusDesc(status));

		Date checkinTime = SolrUtil.getSolrDate(solrResult.getCheckIn());
		String checkInStr = DateUtil.formatDate(checkinTime);
		checkInStr = StringUtil.defaultString(checkInStr);
		houseQueryDto.setCheckIn(checkInStr);

		// 押金形式
		int depositMonth = solrResult.getDepositMonth();
		int periodMonth = solrResult.getPeriodMonth();
		houseQueryDto.setDepositMonth(depositMonth);
		houseQueryDto.setPeriodMonth(periodMonth);
		String payType = HouseUtil.getPaytypeStr(depositMonth, periodMonth);
		houseQueryDto.setPayType(payType);

		// 收藏信息(暂不使用)
		houseQueryDto.setMark(0);

		// 房源详情标题
		houseQueryDto.setTitle(getHouseDetailTitle(solrResult, agencyIdList, companyName));

		// 发布时间
		Date pubDate = SolrUtil.getSolrDate(solrResult.getPubDate());
		String pubDateStr = DateUtil.formatDate(pubDate);
		pubDateStr = StringUtil.defaultString(pubDateStr);
		houseQueryDto.setPubDate(pubDateStr);

		// 最近更新时间
		Date updateTime = SolrUtil.getSolrDate(solrResult.getUpdateDate());
		String updateTimeStr = DateUtil.formatDate(updateTime);
		updateTimeStr = StringUtil.defaultString(updateTimeStr);
		houseQueryDto.setLastModifyDate(updateTimeStr);

		// 经纬度
		String baiduLongitude = solrResult.getBaiduLongitude(); // 经度
		String baiduLatitude = solrResult.getBaiduLatitude(); // 纬度
		String position = HouseUtil.getPosition(baiduLongitude, baiduLatitude);
		houseQueryDto.setPosition(position);
		houseQueryDto.setPositionX(baiduLatitude);
		houseQueryDto.setPositionY(baiduLongitude);

		// 城市Id
		houseQueryDto.setCityId(solrResult.getCityId());

		// 地铁,公交,周边
		if (StringUtil.isBlank(solrResult.getSubway())) {
			houseQueryDto.setSubway("暂无地铁线路");
		} else {
			houseQueryDto.setSubway(solrResult.getSubway() + ";");
		}
		houseQueryDto.setSubwayDistance(solrResult.getSubwayDistance());
		if (StringUtil.isBlank(solrResult.getBusStations())) {
			houseQueryDto.setBusStations("暂无公交线路");
		} else {
			houseQueryDto.setBusStations(solrResult.getBusStations());
		}
		houseQueryDto.setSurround(solrResult.getSurround());

		// 地址
		houseQueryDto.setCommunityName(solrResult.getCommunityName());
		houseQueryDto.setAddress(solrResult.getAddress());

		// 组装房源标签，以字符串形式返回，“,”分隔（排序顺序：支持月付、独立卫生间、独立阳台、近地铁、南向、押一付一）
		String houseTag = "";
		String houseTagTemp = solrResult.getRoomTag();
		if (StringUtil.isNotEmpty(houseTagTemp)) {
			String[] tagArray = new String[] { "12", "3", "4", "6", "2", "9" };
			String[] tagArray2 = houseTagTemp.split(",");
			List<String> tag2List = new ArrayList<String>();
			for (int i = 0; i < tagArray2.length; i++) {
				tag2List.add(tagArray2[i]);
			}
			for (int i = 0; i < tagArray.length; i++) {
				if (tag2List.contains(tagArray[i])) {
					if (StringUtil.isEmpty(houseTag)) {
						houseTag = HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					} else {
						houseTag = houseTag + "," + HouseTagsEnum.getDesc(Integer.parseInt(tagArray[i]));
					}
				}
			}
		}
		houseQueryDto.setHouseTag(houseTag);

		// 房源描述(几室几厅)
		int bedroomNum = solrResult.getBedroomNums();
		int livingroomNum = solrResult.getLivingroomNums();
		String roomDesc = HouseUtil.getRoomDesc(bedroomNum, livingroomNum);
		houseQueryDto.setRoom(roomDesc);

		// 面积
		houseQueryDto.setArea(HouseUtil.getAreaStr(solrResult.getArea()));

		// 房源面积
		if (solrResult.getHouseArea() != 0) {
			houseQueryDto.setHouseArea(HouseUtil.getAreaStr(solrResult.getHouseArea()));
		}

		// 楼层
		String floorNo = solrResult.getFlowNo();
		String floorTotal = solrResult.getFlowTotal();
		houseQueryDto.setFloor(HouseUtil.getFloorStr(floorNo, floorTotal));

		// 朝向
		int orientations = solrResult.getOrientations();
		String orientationsStr = HouseUtil.getOrientationsStr(orientations);
		houseQueryDto.setOrentation(orientationsStr);

		// 建筑类型
		houseQueryDto.setBuildingType(StringUtil.EMPTY);

		// 装修类型
		int decoration = solrResult.getDecoration();
		String decorationName = HouseUtil.getDecorationStr(decoration);
		houseQueryDto.setFitmentType(decorationName);

		// 建筑时间
		houseQueryDto.setBuildingYear(StringUtil.EMPTY);

		// 独立卫生间
		int toilet = solrResult.getToilet();
		houseQueryDto.setToilet(toilet);

		// 独立阳台
		int balcony = solrResult.getBalcony();
		houseQueryDto.setBalcony(balcony);

		// 保险
		int insurance = solrResult.getInsurance();
		String insuranceStr = HouseUtil.getInsuranceStr(insurance);
		houseQueryDto.setInsurance(insuranceStr);

		// 整租/合租
		int entireRent = solrResult.getEntireRent();
		String rentTypeName = HouseUtil.getEntireRentStr(entireRent);
		houseQueryDto.setEntireRent(entireRent);
		houseQueryDto.setEntireRentDesc(rentTypeName);

		// TODO buildType
		houseQueryDto.setBuildType(StringUtil.EMPTY);

		// 房源描述
		String comment = solrResult.getComment();
		if (StringUtils.isEmpty(comment) || "无".equals(comment.trim())) {
			comment = Constants.Common.houseDesc;
		}
		houseQueryDto.setDesc(comment);

		// 来源
		String source = solrResult.getSource();
		houseQueryDto.setSource(source);

		// 设置
		List<Integer> settingCodes = solrResult.getSettingCodes();
		List<Integer> categoryTypes = solrResult.getCategoryTypes();
		List<Integer> settingNums = solrResult.getSettingNums();
		List<HouseSetting> houseSettingList = HouseUtil.getHouseSettingList(settingCodes, categoryTypes, settingNums);
		List<SettingVo> settingsSortList = HouseUtil.getHouseSettingSortList(houseSettingList);
		houseQueryDto.setSettings(settingsSortList);

		// 新增同小区房源总数、公寓下所有房源总数、公寓下所有城市总数
		houseQueryDto.setCommunityHouseCount(communityHouseCount);
		houseQueryDto.setCompanyHouseCount(companyHouseCount);
		houseQueryDto.setCompanyCityCount(companyCityCount);

		// 查询该房源/房间 针对当前用户是否被收藏
		houseQueryDto.setCollectFlag(collectFlag);

		// 图片
		// 竖图压缩+水印
		String erectParams2 = "/resize,m_lfit,h_600,w_800/auto-orient,1/quality,q_90" + imageCss;
		// String
		// erectParams2="/resize,m_lfit,h_600,w_800/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";

		// 原图水印
		// String
		// bigParams="?x-oss-process=image/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";
		String bigParams = "?x-oss-process=image/auto-orient,1/quality,q_90" + imageCss;

		// 横图压缩+水印
		// String
		// transversePrams="?x-oss-process=image/resize,m_fixed,h_600,w_800/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8zMCA=,t_90,g_se,x_30,y_30";
		String transversePrams = "?x-oss-process=image/resize,m_fixed,h_600,w_800/auto-orient,1/quality,q_90"
				+ imageCss;

		List<String> imgs = solrResult.getImgs();
		List<ImgLink> imgLinks = new ArrayList<ImgLink>();
		if (CollectionUtils.isNotEmpty(imgs)) {
			for (String imgUrl : imgs) {
				ImgLink link = new ImgLink();
				if (StringUtils.isNotBlank(imgUrl)) {
					if (imgUrl.contains("(")) {
						int oldHeigh = Integer.valueOf(imgUrl.substring(imgUrl.indexOf("(") + 1, imgUrl.indexOf(",")));
						int x = oldHeigh / 4;
						int w = Integer.valueOf(imgUrl.substring(imgUrl.indexOf(",") + 1, imgUrl.indexOf(")")));
						w = w / 100 * 99;//oss存储图片crop存在误差
						int h = w / 4 * 3;
						String erectParams1 = "?x-oss-process=image/crop,x_0,y_" + x + ",w_" + w + ",h_" + h;
						link.setBigImgUrl(imgUrl + bigParams);
						link.setSmallImgUrl(imgUrl + erectParams1 + erectParams2);
					} else {
						link.setBigImgUrl(imgUrl + bigParams);
						link.setSmallImgUrl(imgUrl + transversePrams);
					}
					imgLinks.add(link);
				}
			}
		}else{//临时解决ios房源详情无图
            ImgLink link = new ImgLink();
            link.setBigImgUrl(Constants.imageUtil.HOUUSE_DETAIL_IMG_DEFALUE);
            link.setSmallImgUrl(Constants.imageUtil.HOUUSE_DETAIL_IMG_DEFALUE);
            imgLinks.add(link);
        }

		houseQueryDto.setImgs(imgLinks);

		int roomType = solrResult.getRoomType();
		houseQueryDto.setRoomType(RoomTypeEnum.getRoomTypeDesc(roomType));

		houseQueryDto.setRoomName(solrResult.getRoomName());
		// add by arison 20170821
		if (StringUtil.isNotBlank(solrResult.getCompanyId()) && !"0".equals(solrResult.getCompanyId())) {
			houseQueryDto.setCompanyId(solrResult.getCompanyId());
		}
		houseQueryDto.setCompanyName(solrResult.getCompanyName());
		// 支持月付 1:支持月付；0:不支持月付
		houseQueryDto.setIsMonthlyPay(solrResult.getRisPayMonth());
		return houseQueryDto;
	}

	// public static void main(String[] args) {
	// Set set = new HashSet();
	// for (int i = 1; i <= 6000; i++) {
	// String s = HouseUtil.getHouseSellId();
	// set.add(s);
	// // System.out.println(s);
	// }
	// System.out.println(set.size());
	// }
}
