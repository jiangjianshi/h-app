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
import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePictureDto;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDtoAsz;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetailAsz;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;

/**
 * ClassName: HouseUtil date: 2016年4月27日 下午12:13:52 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class HouseAszUtil {

	private static final Log logger = LogFactory.getLog(HouseAszUtil.class);

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
	 * @param HousePublishDtoAsz
	 * @return
	 */
	public static HouseDetailAsz getHouseDetail(HouseDetailAsz asz, HousePublishDtoAsz dtoAsz) {
		
		if (dtoAsz == null) {
			return null;
		}
		if (asz == null) {
			asz = new HouseDetailAsz();
		}

		asz.setApartmentId(dtoAsz.getApartmentId());
		asz.setApartmentCode(dtoAsz.getApartmentCode());
		asz.setHouseId(dtoAsz.getHouseId());
		asz.setRentType(dtoAsz.getRentType());
		asz.setRoomNo(dtoAsz.getRoomNo());
		asz.setRentStatus(dtoAsz.getRentStatus());
		asz.setRentPrice(dtoAsz.getRentPrice());
		asz.setCustomerPerson(dtoAsz.getCustomerPerson());
		asz.setResidentialName(dtoAsz.getResidentialName());
		asz.setCityCode(dtoAsz.getCityCode());
		asz.setCityName(dtoAsz.getCityName());
		asz.setAreaCode(dtoAsz.getAreaCode());
		asz.setAddress(dtoAsz.getAddress());
		asz.setAreaName(dtoAsz.getAreaName());
		asz.setAddress(dtoAsz.getAddress());
		asz.setBusinessCircleMulti(dtoAsz.getBusinessCircleMulti());
		asz.setLng(dtoAsz.getLng());
		asz.setLat(dtoAsz.getLat());
		asz.setPropertyUse(dtoAsz.getPropertyUse());
		asz.setFloor(dtoAsz.getFloor());
		asz.setGroudFloors(dtoAsz.getGroudFloors());
		asz.setRooms(dtoAsz.getRooms());
		asz.setLivings(dtoAsz.getLivings());
		asz.setBathRooms(dtoAsz.getBathRooms());
		asz.setBuildArea(dtoAsz.getBuildArea());
		asz.setTotalArea(dtoAsz.getTotalArea());
		asz.setOrientation(dtoAsz.getOrientation());
		asz.setFitmentType(dtoAsz.getFitmentType());
		asz.setHouseRoomFeature(dtoAsz.getHouseRoomFeature());
		asz.setHouseConfiuTation(dtoAsz.getHouseConfiuTation());
		asz.setRemark(dtoAsz.getRemark());
		asz.setImgList(dtoAsz.getImgList());
		asz.setAgentUname(dtoAsz.getAgentUname());
		asz.setAgentUphone(dtoAsz.getAgentUphone());
		asz.setAgentPost(dtoAsz.getAgentPost());
		asz.setAgentDepartment(dtoAsz.getAgentDepartment());
		asz.setCreateTime(dtoAsz.getCreateTime());
		asz.setUpdateTime(dtoAsz.getUpdateTime());
		asz.setOnlineStatus(dtoAsz.getOnlineStatus());
		
		//每次数据更新，需重新收集
		asz.setTransferFlag(AszConstants.HouseDetail.TRANSFER_FLAG_NO);
		return asz;
	}


}
