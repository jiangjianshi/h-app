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

import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDtoAsz;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetailAsz;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailAszRepository;
import com.huifenqi.hzf_platform.utils.HouseAszUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;

/**
 * ClassName: HouseAszDao date: 2017年09月15日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class HouseAszDao {

	private static final Log logger = LogFactory.getLog(HouseAszDao.class);

	@Autowired
	private HouseDetailAszRepository houseDetailAszRepository;

	/**
	 * 添加房源信息
	 * 
	 * @param houseDto
	 * @return sellId
	 */
	public void addBdHousePublishDtoQft(HousePublishDtoAsz houseDtoAsz) {
		try{
			// 添加房源详细信息
			HouseDetailAsz houseDetailAsz = houseDetailAszRepository
					.findByApartmentCode(houseDtoAsz.getApartmentCode());

			// 转换成db对象
			houseDetailAsz = HouseAszUtil.getHouseDetail(houseDetailAsz, houseDtoAsz);

			// 保存房源
			houseDetailAszRepository.save(houseDetailAsz);
					
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("爱上租房源保存失败:" +houseDtoAsz.getApartmentId()+ e.getMessage()));
			logger.error(LogUtils.getCommLog("爱上租房源保存失败,房源数据详情:" +houseDtoAsz.toString()+ e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "发布爱上租房源失败");
		}

	}
}
