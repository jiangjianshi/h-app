/** 
 * Project Name: hzf_platform 
 * File Name: HouseUtil.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年4月27日下午12:13:52 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.context.BdConstantsEnum;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePictureDto;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDtoQft;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;

/**
 * ClassName: HouseUtil date: 2016年4月27日 下午12:13:52 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class BdHouseQftUtil {

	private static final Log logger = LogFactory.getLog(BdHouseQftUtil.class);

	/**
	 * 获取房间详情
	 * 
	 * @param appSellId
	 *            appID+房源ID
	 * @return List<>
	 */
	public static List<BdHousePicture> getPicInfo(String picsJson, String appSellId) {
		try {
			List<BdHousePicture> bdHousePictureList = new ArrayList<>();
			List<BdHousePictureDto> bdHousePictureDtoList = new ArrayList<>();
			Gson gson = GsonUtils.getInstace();
			bdHousePictureDtoList = gson.fromJson(picsJson, new TypeToken<List<BdHousePictureDto>>() {
			}.getType());
            boolean isFirstPic=true;
			for (BdHousePictureDto bdHousePictureDto : bdHousePictureDtoList) {
				BdHousePicture bdHousePicture = new BdHousePicture();
				bdHousePicture.setPicDesc(bdHousePictureDto.getPicDesc());
				bdHousePicture.setPicDetailnum(Integer.parseInt(bdHousePictureDto.getDetailNum()));
				bdHousePicture.setPicWebPath(bdHousePictureDto.getPicUrl());
				bdHousePicture.setSellId(appSellId);
				if (isFirstPic) {
					bdHousePicture.setIsDefault(1); // 1：首图；0：非首图
                    isFirstPic=false;
				} else {
					bdHousePicture.setIsDefault(0);

				}
				// bdHousePicture.setPicType();
				// bdHousePicture.setRoomId(sellId);

				bdHousePicture.setIsDelete(Constants.Common.STATE_IS_DELETE_NO);// 1代表删除；0代表有效
				bdHousePicture.setCreateTime(new Date());
				bdHousePicture.setUpdateTime(new Date());
				bdHousePictureList.add(bdHousePicture);
			}
			return bdHousePictureList;
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
		}

	}

	/**
	 * 从房源DTO获取房源详细信息
	 * 
	 * @param BdHousePublishDtoQft
	 * @return
	 */
	public static BdHouseDetailQft getBdHouseDetail(BdHouseDetailQft BdHouseDetailQft,
			BdHousePublishDtoQft BdHousePublishDtoQft) {
		
		if (BdHousePublishDtoQft == null) {
			return null;
		}
		if (BdHouseDetailQft == null) {
			BdHouseDetailQft = new BdHouseDetailQft();
		}
		BdHouseDetailQft.setxCode(BdHousePublishDtoQft.getxCoord());
		BdHouseDetailQft.setyCode(BdHousePublishDtoQft.getyCoord());
		BdHouseDetailQft.setDistrictName(BdHousePublishDtoQft.getDistrictName());
		BdHouseDetailQft.setMonthRent(BdHousePublishDtoQft.getMonthRent());
		BdHouseDetailQft.setAgentPhone(BdHousePublishDtoQft.getAgentPhone());
		BdHouseDetailQft.setAgentName(BdHousePublishDtoQft.getAgentName());
		BdHouseDetailQft.setBedRoomNum(BdHousePublishDtoQft.getBedRoomNum());
		BdHouseDetailQft.setLivingRoomNum(BdHousePublishDtoQft.getLivingRoomNum());
		BdHouseDetailQft.setToiletNum(BdHousePublishDtoQft.getToiletNum());
		BdHouseDetailQft.setRentRoomArea(BdHousePublishDtoQft.getRentRoomArea());
		BdHouseDetailQft.setHouseFloor(BdHousePublishDtoQft.getHouseFloor());
		BdHouseDetailQft.setTotalFloor(BdHousePublishDtoQft.getTotalFloor());
		BdHouseDetailQft.setFaceToType(BdHousePublishDtoQft.getFaceToType());
		BdHouseDetailQft.setBuildType(BdHousePublishDtoQft.getBuildType());
		BdHouseDetailQft.setBuildYear(BdHousePublishDtoQft.getBuildYear());
		BdHouseDetailQft.setRentStartDate(BdHousePublishDtoQft.getRentStartDate());
		if (BdHousePublishDtoQft.getRentType() == Constants.BdHouseDetailQft.RENT_TYPE_SHARE) {
			BdHouseDetailQft.setBedRoomType(BdHousePublishDtoQft.getBedRoomType());
		}
		BdHouseDetailQft.setAppId(BdHousePublishDtoQft.getAppId());
		BdHouseDetailQft.setOutHouseId(BdHousePublishDtoQft.getOutHouseId());
		BdHouseDetailQft.setPicUrlList(BdHousePublishDtoQft.getPicUrlList());
		BdHouseDetailQft.setRentType(BdHousePublishDtoQft.getRentType());
		BdHouseDetailQft.setRoomName(BdHousePublishDtoQft.getRoomName());
		BdHouseDetailQft.setRoomCode(BdHousePublishDtoQft.getRoomCode());
		BdHouseDetailQft.setFeatureTag(BdHousePublishDtoQft.getFeatureTag());
		BdHouseDetailQft.setDetailPoint(BdHousePublishDtoQft.getDetailPoint());
		BdHouseDetailQft.setServicePoint(BdHousePublishDtoQft.getServicePoint());
		BdHouseDetailQft.setShortRent(BdHousePublishDtoQft.getShortRent());
		BdHouseDetailQft.setProvice(BdHousePublishDtoQft.getProvince());
		BdHouseDetailQft.setCityName(BdHousePublishDtoQft.getCityName());
		BdHouseDetailQft.setCountyName(BdHousePublishDtoQft.getCountyName());
		BdHouseDetailQft.setStreet(BdHousePublishDtoQft.getStreet());
		BdHouseDetailQft.setAddress(BdHousePublishDtoQft.getAddress());
		BdHouseDetailQft.setSubwayLine(BdHousePublishDtoQft.getSubwayLine());
		BdHouseDetailQft.setSubwayStation(BdHousePublishDtoQft.getSubwayStation());
		BdHouseDetailQft.setOrderPhone(BdHousePublishDtoQft.getOrderPhone());
		BdHouseDetailQft.setVideoUrl(BdHousePublishDtoQft.getVideoUrl());
		BdHouseDetailQft.setSupplyHeating(BdHousePublishDtoQft.getSupplyHeating());
		BdHouseDetailQft.setGreenRatio(BdHousePublishDtoQft.getGreenRatio());
		BdHouseDetailQft.setAgentName(BdHousePublishDtoQft.getAgentName());
		BdHouseDetailQft.setAreaName(BdHousePublishDtoQft.getAreaName());
		BdHouseDetailQft.setHouseDesc(BdHousePublishDtoQft.getHouseDesc());
		BdHouseDetailQft.setLiveBedTotile(BdHousePublishDtoQft.getLiveBedTotile());
		BdHouseDetailQft.setCompanyId(BdHousePublishDtoQft.getCompanyId());
		BdHouseDetailQft.setCompanyName(BdHousePublishDtoQft.getCompanyName());
		BdHouseDetailQft.setFitmentState(BdHousePublishDtoQft.getFitmentState());
		Date date = new Date();
		BdHouseDetailQft.setCreateTime(date);
		BdHouseDetailQft.setUpdateTime(date);
		BdHouseDetailQft.setState(BdConstantsEnum.StatusEnum.PUT_AWAY.getIndex());// 设置房源状态上架
		return BdHouseDetailQft;
	}

	/**
	 * 从房源DTO获取房源详细信息
	 * 
	 * @param BdHousePublishDtoQft
	 * @return
	 */
	public static BdHouseDetailQft getBdUpdateHouseDetail(BdHousePublishDtoQft BdHousePublishDtoQft) {
		if (BdHousePublishDtoQft == null) {
			return null;
		}
		BdHouseDetailQft BdHouseDetailQft = new BdHouseDetailQft();
		// BdHouseDetailQft.setxCode(BdHousePublishDtoQft.getxCode());
		// BdHouseDetailQft.setyCode(BdHousePublishDtoQft.getyCode());
		// BdHouseDetailQft.setDistrictName(BdHousePublishDtoQft.getDistrictName());
		BdHouseDetailQft.setMonthRent(BdHousePublishDtoQft.getMonthRent());
		BdHouseDetailQft.setAgentPhone(BdHousePublishDtoQft.getAgentPhone());
		// BdHouseDetailQft.setAgentName(BdHousePublishDtoQft.getAgentName());
		BdHouseDetailQft.setBedRoomNum(BdHousePublishDtoQft.getBedRoomNum());
		BdHouseDetailQft.setLivingRoomNum(BdHousePublishDtoQft.getLivingRoomNum());
		BdHouseDetailQft.setToiletNum(BdHousePublishDtoQft.getToiletNum());
		BdHouseDetailQft.setRentRoomArea(BdHousePublishDtoQft.getRentRoomArea());
		BdHouseDetailQft.setHouseFloor(BdHousePublishDtoQft.getHouseFloor());
		BdHouseDetailQft.setTotalFloor(BdHousePublishDtoQft.getTotalFloor());
		BdHouseDetailQft.setFaceToType(BdHousePublishDtoQft.getFaceToType());
		BdHouseDetailQft.setBuildType(BdHousePublishDtoQft.getBuildType());
		BdHouseDetailQft.setBuildYear(BdHousePublishDtoQft.getBuildYear());
		BdHouseDetailQft.setRentStartDate(BdHousePublishDtoQft.getRentStartDate());
		BdHouseDetailQft.setBedRoomType(BdHousePublishDtoQft.getBedRoomType());
		BdHouseDetailQft.setAppId(BdHousePublishDtoQft.getAppId());
		BdHouseDetailQft.setOutHouseId(BdHousePublishDtoQft.getOutHouseId());
		// BdHouseDetailQft.setPicUrlList(BdHousePublishDtoQft.getPicUrlList());
		BdHouseDetailQft.setRentType(BdHousePublishDtoQft.getRentType());
		BdHouseDetailQft.setRoomName(BdHousePublishDtoQft.getRoomName());
		BdHouseDetailQft.setRoomCode(BdHousePublishDtoQft.getRoomCode());
		BdHouseDetailQft.setFeatureTag(BdHousePublishDtoQft.getFeatureTag());
		BdHouseDetailQft.setDetailPoint(BdHousePublishDtoQft.getDetailPoint());
		BdHouseDetailQft.setServicePoint(BdHousePublishDtoQft.getServicePoint());
		BdHouseDetailQft.setShortRent(BdHousePublishDtoQft.getShortRent());
		// BdHouseDetailQft.setProvice(BdHousePublishDtoQft.getProvice());
		// BdHouseDetailQft.setCityName(BdHousePublishDtoQft.getCityName());
		// BdHouseDetailQft.setCountyName(BdHousePublishDtoQft.getCountyName());
		// BdHouseDetailQft.setStreet(BdHousePublishDtoQft.getStreet());
		// BdHouseDetailQft.setAddress(BdHousePublishDtoQft.getAddress());
		// BdHouseDetailQft.setSubwayLine(BdHousePublishDtoQft.getSubwayLine());
		// BdHouseDetailQft.setSubwayStation(BdHousePublishDtoQft.getSubwayStation());
		BdHouseDetailQft.setOrderPhone(BdHousePublishDtoQft.getOrderPhone());
		// BdHouseDetailQft.setVideoUrl(BdHousePublishDtoQft.getVideoUrl());
		BdHouseDetailQft.setSupplyHeating(BdHousePublishDtoQft.getSupplyHeating());
		BdHouseDetailQft.setGreenRatio(BdHousePublishDtoQft.getGreenRatio());
		BdHouseDetailQft.setAgentName(BdHousePublishDtoQft.getAgentName());
		// BdHouseDetailQft.setAreaName(BdHousePublishDtoQft.getAreaName());
		Date date = new Date();
		// BdHouseDetailQft.setCreateTime(date);
		BdHouseDetailQft.setUpdateTime(date);

		return BdHouseDetailQft;
	}

	/**
	 * 更新房源基础信息
	 * 
	 * @param oldHouseBase
	 * @return
	 */
	public static BdHouseDetailQft updateHouseBase(BdHouseDetailQft oldHouseBase, BdHousePublishDtoQft newHouseBase) {
		if (oldHouseBase == null) {
			return null;
		}
		if (newHouseBase == null) {
			return null;
		}
		if(newHouseBase.getMonthRent() != null && !"".equals(newHouseBase.getMonthRent())){
			oldHouseBase.setMonthRent(newHouseBase.getMonthRent());
		}
		if(newHouseBase.getAgentPhone()!=null && !"".equals(newHouseBase.getAgentPhone())){
			oldHouseBase.setAgentPhone(newHouseBase.getAgentPhone());
		}
		if(newHouseBase.getBedRoomNum() != null && !"".equals(newHouseBase.getBedRoomNum())){
			oldHouseBase.setBedRoomNum(newHouseBase.getBedRoomNum());
		}
		if(newHouseBase.getLivingRoomNum() != null && !"".equals(newHouseBase.getLivingRoomNum())){
			oldHouseBase.setLivingRoomNum(newHouseBase.getLivingRoomNum());
		}
		if(newHouseBase.getToiletNum()!= null && !"".equals(newHouseBase.getToiletNum())){
			oldHouseBase.setToiletNum(newHouseBase.getToiletNum());
		}
		if(newHouseBase.getRentRoomArea()!= null && !"".equals(newHouseBase.getRentRoomArea())){
			oldHouseBase.setRentRoomArea(newHouseBase.getRentRoomArea());
		}
		if(newHouseBase.getHouseFloor()!= null && !"".equals(newHouseBase.getHouseFloor())){
			oldHouseBase.setHouseFloor(newHouseBase.getHouseFloor());
		}
		if(newHouseBase.getTotalFloor()!= null && !"".equals(newHouseBase.getTotalFloor())){
			oldHouseBase.setTotalFloor(newHouseBase.getTotalFloor());
		}
		if(newHouseBase.getFaceToType()!= null && !"".equals(newHouseBase.getFaceToType())){
			oldHouseBase.setFaceToType(newHouseBase.getFaceToType());
		}
		if(newHouseBase.getBuildType()!= null && !"".equals(newHouseBase.getBuildType())){
			oldHouseBase.setBuildType(newHouseBase.getBuildType());
		}
		if(newHouseBase.getBuildYear()!= null && !"".equals(newHouseBase.getBuildYear())){
			oldHouseBase.setBuildYear(newHouseBase.getBuildYear());
		}
		if(newHouseBase.getRentStartDate()!= null && !"".equals(newHouseBase.getRentStartDate())){
			oldHouseBase.setRentStartDate(newHouseBase.getRentStartDate());
		}
		if(newHouseBase.getBedRoomType()!= null && !"".equals(newHouseBase.getBedRoomType())){
			oldHouseBase.setBedRoomType(newHouseBase.getBedRoomType());
		}
		if(newHouseBase.getAppId()!= null && !"".equals(newHouseBase.getAppId())){
			oldHouseBase.setAppId(newHouseBase.getAppId());
		}
		if(newHouseBase.getOutHouseId()!= null && !"".equals(newHouseBase.getOutHouseId())){
			oldHouseBase.setOutHouseId(newHouseBase.getOutHouseId());
		}
		if(newHouseBase.getRentType()!= null && !"".equals(newHouseBase.getRentType())){
			oldHouseBase.setRentType(newHouseBase.getRentType());
		}
		if(newHouseBase.getRoomName()!= null && !"".equals(newHouseBase.getRoomName())){
			oldHouseBase.setRoomName(newHouseBase.getRoomName());
		}
		if(newHouseBase.getRoomCode()!= null && !"".equals(newHouseBase.getRoomCode())){
			oldHouseBase.setRoomCode(newHouseBase.getRoomCode());
		}
		if(newHouseBase.getFeatureTag()!= null && !"".equals(newHouseBase.getFeatureTag())){
			oldHouseBase.setFeatureTag(newHouseBase.getFeatureTag());
		}
		if(newHouseBase.getDetailPoint()!= null && !"".equals(newHouseBase.getDetailPoint())){
			oldHouseBase.setDetailPoint(newHouseBase.getDetailPoint());
		}
		if(newHouseBase.getServicePoint()!= null && !"".equals(newHouseBase.getServicePoint())){
			oldHouseBase.setServicePoint(newHouseBase.getServicePoint());
		}
		if(newHouseBase.getShortRent()!= null && !"".equals(newHouseBase.getShortRent())){
			oldHouseBase.setShortRent(newHouseBase.getShortRent());
		}
		if(newHouseBase.getOrderPhone()!= null && !"".equals(newHouseBase.getOrderPhone())){
			oldHouseBase.setOrderPhone(newHouseBase.getOrderPhone());
		}
		if(newHouseBase.getSupplyHeating()!= null && !"".equals(newHouseBase.getSupplyHeating())){
			oldHouseBase.setSupplyHeating(newHouseBase.getSupplyHeating());
		}
		if(newHouseBase.getGreenRatio()!= null && !"".equals(newHouseBase.getGreenRatio())){
			oldHouseBase.setGreenRatio(newHouseBase.getGreenRatio());
		}
		if(newHouseBase.getAgentName()!= null && !"".equals(newHouseBase.getAgentName())){
			oldHouseBase.setAgentName(newHouseBase.getAgentName());
		}
		if(newHouseBase.getHouseDesc()!= null && !"".equals(newHouseBase.getHouseDesc())){
			oldHouseBase.setHouseDesc(newHouseBase.getHouseDesc());
		}

		if (newHouseBase.getLiveBedTotile() != null && !"".equals(newHouseBase.getLiveBedTotile())) {
			oldHouseBase.setLiveBedTotile(newHouseBase.getLiveBedTotile());
		}
		if (newHouseBase.getCompanyId() != null && !"".equals(newHouseBase.getCompanyId())) {
			oldHouseBase.setCompanyId(newHouseBase.getCompanyId());
		}
		if (newHouseBase.getCompanyName() != null && !"".equals(newHouseBase.getCompanyName())) {
			oldHouseBase.setCompanyName(newHouseBase.getCompanyName());
		}
		if (newHouseBase.getFitmentState() != null && !"".equals(newHouseBase.getFitmentState())) {
			oldHouseBase.setFitmentState(newHouseBase.getFitmentState());
		}
		if (newHouseBase.getPicUrlList() != null && !"".equals(newHouseBase.getPicUrlList())) {
			oldHouseBase.setPicUrlList(newHouseBase.getPicUrlList());
		}
		if (newHouseBase.getxCoord() != null && !"".equals(newHouseBase.getxCoord())) {
			oldHouseBase.setxCode(newHouseBase.getxCoord());
		}
		if (newHouseBase.getyCoord() != null && !"".equals(newHouseBase.getyCoord())) {
			oldHouseBase.setyCode(newHouseBase.getyCoord());
		}
		if (newHouseBase.getDistrictName() != null && !"".equals(newHouseBase.getDistrictName())) {
			oldHouseBase.setDistrictName(newHouseBase.getDistrictName());
		}
		if (newHouseBase.getCityName() != null && !"".equals(newHouseBase.getCityName())) {
			oldHouseBase.setCityName(newHouseBase.getCityName());
		}
		if (newHouseBase.getCountyName() != null && !"".equals(newHouseBase.getCountyName())) {
			oldHouseBase.setCountyName(newHouseBase.getCountyName());
		}
		if (newHouseBase.getAddress() != null && !"".equals(newHouseBase.getAddress())) {
			oldHouseBase.setAddress(newHouseBase.getAddress());
		}


		// oldHouseBase.setAgentName(newHouseBase.getAgentName());
		// oldHouseBase.setProvice(newHouseBase.getProvice());
		// oldHouseBase.setStreet(newHouseBase.getStreet());
		// oldHouseBase.setSubwayLine(newHouseBase.getSubwayLine());
		// oldHouseBase.setSubwayStation(newHouseBase.getSubwayStation());
		// oldHouseBase.setVideoUrl(newHouseBase.getVideoUrl());
		// oldHouseBase.setAreaName(newHouseBase.getAreaName());
		Date date = new Date();
		// oldHouseBase.setCreateTime(date);
		oldHouseBase.setUpdateTime(date);
		//2017年06月23日19:59:45  jjs
		oldHouseBase.setTransferFlag(0);
		return oldHouseBase;
	}
}
