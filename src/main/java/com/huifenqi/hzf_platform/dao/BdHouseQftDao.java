/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDtoQft;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.BdHouseDetailQftRepository;
import com.huifenqi.hzf_platform.dao.repository.house.BdHousePictureRepository;
import com.huifenqi.hzf_platform.utils.BdHouseQftUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class BdHouseQftDao {

	private static final Log logger = LogFactory.getLog(BdHouseQftDao.class);

	@Autowired
	private BdHouseDetailQftRepository BdHouseDetailQftRepository;

	@Autowired
	private BdHousePictureRepository bdHousePictureRepository;

	/**
	 * 添加房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
	public void addBdHousePublishDtoQft(BdHousePublishDtoQft bdHouseDto) {
		try{
			// 添加房源详细信息
			BdHouseDetailQft BdHouseDetailQft = BdHouseDetailQftRepository
					.findBySellIdandAppId(bdHouseDto.getOutHouseId(), bdHouseDto.getAppId());
			BdHouseDetailQft = BdHouseQftUtil.getBdHouseDetail(BdHouseDetailQft, bdHouseDto);
			BdHouseDetailQftRepository.save(BdHouseDetailQft);
					
			// 添加房源图片
			// String appSellId = bdHouseDto.getAppId() +
			// bdHouseDto.getOutHouseId();
			// List<BdHousePicture> housePictureList =
			// BdHouseQftUtil.getPicInfo(bdHouseDto.getPicUrlList(), appSellId);
			// if (CollectionUtils.isNotEmpty(housePictureList)) {
			// bdHousePictureRepository.save(housePictureList);
			// }
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
	public void roomSingleModify(BdHousePublishDtoQft houseDto, String outHouseId, String appId) throws Exception {

		// 查询房源基础信息
		BdHouseDetailQft houseBase = BdHouseDetailQftRepository.findBySellIdandAppIdandState(outHouseId, appId);
		if (houseBase == null) {
			logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
			throw new Exception(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
		}

		// 更新房源基础信息houseDto
		// BdHouseDetailQft newBdHouseDetailQft =
		// BdHouseQftUtil.getBdHouseDetailQft(houseDto);
		houseBase = BdHouseQftUtil.updateHouseBase(houseBase, houseDto);
		if (houseBase == null) {
			logger.error(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
			throw new Exception(String.format("百度房源更新失败, outHouseId:%s, appId:%s", outHouseId, appId));
		}
		BdHouseDetailQftRepository.save(houseBase);
	}
	
	
	/**
	 * 查询上架房源信息
	 * 
	 * @param houseDto
	 * @return sellId appId
	 * @throws Exception
	 */
	public BdHouseDetailQft findBySellIdandAppIdandState(String outHouseId, String appId) throws Exception {
		return BdHouseDetailQftRepository.findBySellIdandAppIdandState(outHouseId, appId);
	}

	/**
	 * 查询上架和下架房源信息
	 * 
	 * @param houseDto
	 * @return sellId appId
	 * @throws Exception
	 */
	public BdHouseDetailQft findBySellIdandAppId(String outHouseId, String appId) throws Exception {
		return BdHouseDetailQftRepository.findBySellIdandAppId(outHouseId, appId);
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
		BdHouseDetailQftRepository.setStatusAndMemo(outHouseId, appId, memo, status, transferFlag);
	}
}
