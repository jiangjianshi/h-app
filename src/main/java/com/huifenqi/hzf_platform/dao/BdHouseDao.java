/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.BdHousePictureRepository;
import com.huifenqi.hzf_platform.utils.BdHouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class BdHouseDao {

	private static final Log logger = LogFactory.getLog(BdHouseDao.class);

	@Autowired
	private BdHouseDetailRepository bdHouseDetailRepository;

	@Autowired
	private BdHousePictureRepository bdHousePictureRepository;

	/**
	 * 添加房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
	public void addBdHousePublishDto(BdHousePublishDto bdHouseDto) {
		try{
			// 添加房源详细信息
			BdHouseDetail bdHouseDetail = bdHouseDetailRepository.findBySellIdandAppId(bdHouseDto.getOutHouseId(), bdHouseDto.getAppId());
			bdHouseDetail = BdHouseUtil.getBdHouseDetail(bdHouseDetail,bdHouseDto);
			bdHouseDetailRepository.save(bdHouseDetail);
					
			// 添加房源图片
			String appSellId = bdHouseDto.getAppId() + bdHouseDto.getOutHouseId();
			List<BdHousePicture> housePictureList = BdHouseUtil.getPicInfo(bdHouseDto.getPicUrlList(), appSellId);
			if (CollectionUtils.isNotEmpty(housePictureList)) {
				bdHousePictureRepository.save(housePictureList);
			}			
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源保存失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布房源失败");
		}
		
		
	}
	
	/**
	 * 更新房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 * @throws Exception
	 */
	public void roomSingleModify(BdHousePublishDto houseDto, String outHouseId,String appId) throws Exception {

		// 查询房源基础信息
		BdHouseDetail houseBase = bdHouseDetailRepository.findBySellIdandAppIdandState(outHouseId,appId);
		if (houseBase == null) {
			logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
			throw new Exception(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
		}

		// 更新房源基础信息houseDto
//		BdHouseDetail  newBdHouseDetail = BdHouseUtil.getBdHouseDetail(houseDto);
		houseBase = BdHouseUtil.updateHouseBase(houseBase, houseDto);
		if (houseBase == null) {
			logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
			throw new Exception(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
		}
		bdHouseDetailRepository.save(houseBase);
	}
	
	
	/**
	 * 查询上架房源信息
	 * 
	 * @param houseDto
	 * @return sellId appId
	 * @throws Exception
	 */
	public BdHouseDetail findBySellIdandAppIdandState( String outHouseId,String appId) throws Exception {
		return bdHouseDetailRepository.findBySellIdandAppIdandState(outHouseId,appId);
	}

	/**
	 * 查询上架和下架房源信息
	 * 
	 * @param houseDto
	 * @return sellId appId
	 * @throws Exception
	 */
	public BdHouseDetail findBySellIdandAppId( String outHouseId,String appId) throws Exception {
		return bdHouseDetailRepository.findBySellIdandAppId(outHouseId,appId);
	}
	
	/**
	 * 房源上、下架修改
	 *
	 * @param outHouseId
	 * @param appId
	 * @param memo
	 * @param status
	 * @throws Exception
	 */
	public void houseModify(String outHouseId, String appId, String memo, String status, int transferFlag)
			throws Exception {
		// 更新房源上下架状态
		bdHouseDetailRepository.setStatusAndMemo(outHouseId, appId, memo, status, transferFlag);
	}
}
