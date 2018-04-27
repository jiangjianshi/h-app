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
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetail;
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
public class BdHouseUtil {

	private static final Log logger = LogFactory.getLog(BdHouseUtil.class);

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
	 * @param bdHousePublishDto
	 * @return
	 */
	public static BdHouseDetail getBdHouseDetail(BdHouseDetail bdHouseDetail,BdHousePublishDto bdHousePublishDto) {
		
		if (bdHousePublishDto == null) {
			return null;
		}if(bdHouseDetail == null){
			bdHouseDetail = new BdHouseDetail();
		}
		bdHouseDetail.setxCode(bdHousePublishDto.getxCoord());
		bdHouseDetail.setyCode(bdHousePublishDto.getyCoord());
		bdHouseDetail.setDistrictName(bdHousePublishDto.getDistrictName());
		bdHouseDetail.setMonthRent(bdHousePublishDto.getMonthRent());
		bdHouseDetail.setAgentPhone(bdHousePublishDto.getAgentPhone());
		bdHouseDetail.setAgentName(bdHousePublishDto.getAgentName());
		bdHouseDetail.setBedRoomNum(bdHousePublishDto.getBedRoomNum());
		bdHouseDetail.setLivingRoomNum(bdHousePublishDto.getLivingRoomNum());
		bdHouseDetail.setToiletNum(bdHousePublishDto.getToiletNum());
		bdHouseDetail.setRentRoomArea(bdHousePublishDto.getRentRoomArea());
		bdHouseDetail.setHouseFloor(bdHousePublishDto.getHouseFloor());
		bdHouseDetail.setTotalFloor(bdHousePublishDto.getTotalFloor());
		bdHouseDetail.setFaceToType(bdHousePublishDto.getFaceToType());
		bdHouseDetail.setBuildType(bdHousePublishDto.getBuildType());
		bdHouseDetail.setBuildYear(bdHousePublishDto.getBuildYear());
		bdHouseDetail.setRentStartDate(bdHousePublishDto.getRentStartDate());
		if(bdHousePublishDto.getRentType() == Constants.BdHouseDetail.RENT_TYPE_SHARE){
			bdHouseDetail.setBedRoomType(bdHousePublishDto.getBedRoomType());
		}
		bdHouseDetail.setAppId(bdHousePublishDto.getAppId());
		bdHouseDetail.setOutHouseId(bdHousePublishDto.getOutHouseId());
		bdHouseDetail.setPicUrlList(bdHousePublishDto.getPicUrlList());
		bdHouseDetail.setRentType(bdHousePublishDto.getRentType());
		bdHouseDetail.setRoomName(bdHousePublishDto.getRoomName());
		bdHouseDetail.setRoomCode(bdHousePublishDto.getRoomCode());
		bdHouseDetail.setFeatureTag(bdHousePublishDto.getFeatureTag());
		bdHouseDetail.setDetailPoint(bdHousePublishDto.getDetailPoint());
		bdHouseDetail.setServicePoint(bdHousePublishDto.getServicePoint());
		bdHouseDetail.setShortRent(bdHousePublishDto.getShortRent());
		bdHouseDetail.setProvice(bdHousePublishDto.getProvince());
		bdHouseDetail.setCityName(bdHousePublishDto.getCityName());
		bdHouseDetail.setCountyName(bdHousePublishDto.getCountyName());
		bdHouseDetail.setStreet(bdHousePublishDto.getStreet());
		bdHouseDetail.setAddress(bdHousePublishDto.getAddress());
		bdHouseDetail.setSubwayLine(bdHousePublishDto.getSubwayLine());
		bdHouseDetail.setSubwayStation(bdHousePublishDto.getSubwayStation());
		bdHouseDetail.setOrderPhone(bdHousePublishDto.getOrderPhone());
		bdHouseDetail.setVideoUrl(bdHousePublishDto.getVideoUrl());
		bdHouseDetail.setSupplyHeating(bdHousePublishDto.getSupplyHeating());
		bdHouseDetail.setGreenRatio(bdHousePublishDto.getGreenRatio());
		bdHouseDetail.setAgentName(bdHousePublishDto.getAgentName());
		bdHouseDetail.setAreaName(bdHousePublishDto.getAreaName());
		bdHouseDetail.setHouseDesc(bdHousePublishDto.getHouseDesc());
		bdHouseDetail.setCompanyId(bdHousePublishDto.getCompanyId());
		bdHouseDetail.setCompanyName(bdHousePublishDto.getCompanyName());
		bdHouseDetail.setIsSaas(bdHousePublishDto.getIsSaas());//saas公司标识
		
		Date date = new Date();
		bdHouseDetail.setCreateTime(date);
		bdHouseDetail.setUpdateTime(date);
		bdHouseDetail.setState(BdConstantsEnum.StatusEnum.PUT_AWAY.getIndex());// 设置房源状态上架
		return bdHouseDetail;
	}

	/**
	 * 从房源DTO获取房源详细信息
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static BdHouseDetail getBdUpdateHouseDetail(BdHousePublishDto bdHousePublishDto) {
		if (bdHousePublishDto == null) {
			return null;
		}
		BdHouseDetail bdHouseDetail = new BdHouseDetail();
		// bdHouseDetail.setxCode(bdHousePublishDto.getxCode());
		// bdHouseDetail.setyCode(bdHousePublishDto.getyCode());
		// bdHouseDetail.setDistrictName(bdHousePublishDto.getDistrictName());
		bdHouseDetail.setMonthRent(bdHousePublishDto.getMonthRent());
		bdHouseDetail.setAgentPhone(bdHousePublishDto.getAgentPhone());
		// bdHouseDetail.setAgentName(bdHousePublishDto.getAgentName());
		bdHouseDetail.setBedRoomNum(bdHousePublishDto.getBedRoomNum());
		bdHouseDetail.setLivingRoomNum(bdHousePublishDto.getLivingRoomNum());
		bdHouseDetail.setToiletNum(bdHousePublishDto.getToiletNum());
		bdHouseDetail.setRentRoomArea(bdHousePublishDto.getRentRoomArea());
		bdHouseDetail.setHouseFloor(bdHousePublishDto.getHouseFloor());
		bdHouseDetail.setTotalFloor(bdHousePublishDto.getTotalFloor());
		bdHouseDetail.setFaceToType(bdHousePublishDto.getFaceToType());
		bdHouseDetail.setBuildType(bdHousePublishDto.getBuildType());
		bdHouseDetail.setBuildYear(bdHousePublishDto.getBuildYear());
		bdHouseDetail.setRentStartDate(bdHousePublishDto.getRentStartDate());
		bdHouseDetail.setBedRoomType(bdHousePublishDto.getBedRoomType());
		bdHouseDetail.setAppId(bdHousePublishDto.getAppId());
		bdHouseDetail.setOutHouseId(bdHousePublishDto.getOutHouseId());
		// bdHouseDetail.setPicUrlList(bdHousePublishDto.getPicUrlList());
		bdHouseDetail.setRentType(bdHousePublishDto.getRentType());
		bdHouseDetail.setRoomName(bdHousePublishDto.getRoomName());
		bdHouseDetail.setRoomCode(bdHousePublishDto.getRoomCode());
		bdHouseDetail.setFeatureTag(bdHousePublishDto.getFeatureTag());
		bdHouseDetail.setDetailPoint(bdHousePublishDto.getDetailPoint());
		bdHouseDetail.setServicePoint(bdHousePublishDto.getServicePoint());
		bdHouseDetail.setShortRent(bdHousePublishDto.getShortRent());
		// bdHouseDetail.setProvice(bdHousePublishDto.getProvice());
		// bdHouseDetail.setCityName(bdHousePublishDto.getCityName());
		// bdHouseDetail.setCountyName(bdHousePublishDto.getCountyName());
		// bdHouseDetail.setStreet(bdHousePublishDto.getStreet());
		// bdHouseDetail.setAddress(bdHousePublishDto.getAddress());
		// bdHouseDetail.setSubwayLine(bdHousePublishDto.getSubwayLine());
		// bdHouseDetail.setSubwayStation(bdHousePublishDto.getSubwayStation());
		bdHouseDetail.setOrderPhone(bdHousePublishDto.getOrderPhone());
		// bdHouseDetail.setVideoUrl(bdHousePublishDto.getVideoUrl());
		bdHouseDetail.setSupplyHeating(bdHousePublishDto.getSupplyHeating());
		bdHouseDetail.setGreenRatio(bdHousePublishDto.getGreenRatio());
		bdHouseDetail.setAgentName(bdHousePublishDto.getAgentName());
		// bdHouseDetail.setAreaName(bdHousePublishDto.getAreaName());
		Date date = new Date();
		// bdHouseDetail.setCreateTime(date);
		bdHouseDetail.setUpdateTime(date);

		return bdHouseDetail;
	}

	/**
	 * 更新房源基础信息
	 * 
	 * @param oldHouseBase
	 * @return
	 */
	public static BdHouseDetail updateHouseBase(BdHouseDetail oldHouseBase, BdHousePublishDto newHouseBase) {
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

		if (newHouseBase.getCompanyId() != null && !"".equals(newHouseBase.getCompanyId())) {
			oldHouseBase.setCompanyId(newHouseBase.getCompanyId());
		}
		if (newHouseBase.getCompanyName() != null && !"".equals(newHouseBase.getCompanyName())) {
			oldHouseBase.setCompanyName(newHouseBase.getCompanyName());
		}


		// oldHouseBase.setxCode(newHouseBase.getxCode());
		// oldHouseBase.setyCode(newHouseBase.getyCode());
		// oldHouseBase.setDistrictName(newHouseBase.getDistrictName());
		// oldHouseBase.setAgentName(newHouseBase.getAgentName());
		// oldHouseBase.setPicUrlList(newHouseBase.getPicUrlList());
		// oldHouseBase.setProvice(newHouseBase.getProvice());
		// oldHouseBase.setCityName(newHouseBase.getCityName());
		// oldHouseBase.setCountyName(newHouseBase.getCountyName());
		// oldHouseBase.setStreet(newHouseBase.getStreet());
		// oldHouseBase.setAddress(newHouseBase.getAddress());
		// oldHouseBase.setSubwayLine(newHouseBase.getSubwayLine());
		// oldHouseBase.setSubwayStation(newHouseBase.getSubwayStation());
		// oldHouseBase.setVideoUrl(newHouseBase.getVideoUrl());
		// oldHouseBase.setAreaName(newHouseBase.getAreaName());
		Date date = new Date();
		// oldHouseBase.setCreateTime(date);
		oldHouseBase.setUpdateTime(date);
		//2017年06月23日19:59:45  jjs
		oldHouseBase.setTransferFlag(0);
		oldHouseBase.setIsSaas(newHouseBase.getIsSaas());
		return oldHouseBase;
	}
}
