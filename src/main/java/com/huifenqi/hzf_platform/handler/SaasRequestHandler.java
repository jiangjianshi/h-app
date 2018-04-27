/** 
 * Project Name: hzf_platform 
 * File Name: HouseRequestHandler.java 
 * Package Name: com.huifenqi.hzf_platform.handler 
 * Date: 2016年4月26日下午4:40:45 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.dto.request.house.SaasHousePublishDto;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.dao.HouseDao;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.SaasHouseCheckUtil;

/**
 * ClassName: HouseRequestHandler date: 2016年4月26日 下午4:40:45 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Service
public class SaasRequestHandler {

	private static final Log logger = LogFactory.getLog(SaasRequestHandler.class);

	@Autowired
	private HouseDao houseDao;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;

	/**
	 * SaaS房源置顶设置接口
	 * 
	 * @return
	 */
	@Transactional
	public Responses isTopHouse(HttpServletRequest request) throws Exception {
		SaasHousePublishDto saasHousePublishDto = null;
		try {
			// 验证和获取更新数据
			saasHousePublishDto = SaasHouseCheckUtil.getHousePublishDtoStatus(request);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("置顶参数解析失败" + e.getMessage()));
			return new Responses(ErrorMsgCode.ERROR_MSG_INVALID_PARAMETER, e.getMessage());
		}

		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(saasHousePublishDto.getAppId());
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", saasHousePublishDto.getAppId())));
			return new Responses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}

		try {
			houseDao.isTopHouse(saasHousePublishDto);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源置顶设置失败" + e.getMessage()));
			return new Responses(ErrorMsgCode.ERROR_MSG_INVALID_ISTOP_HOUSE_FAIL, "房源置顶设置失败");
		}

		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("code", "0");
		returnMap.put("msg", "房源置顶成功");
		responses.setBody(returnMap);

		return responses;
	}

	/**
	 * SaaS设置房源发布类型
	 * 
	 * @return
	 */
	@Transactional
	public Responses setPubType(HttpServletRequest request) throws Exception {
		SaasHousePublishDto saasHousePublishDto = null;
		try {
			// 验证和获取更新数据
			saasHousePublishDto = SaasHouseCheckUtil.getHousePublishDtoPubType(request);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源发布类型参数解析失败" + e.getMessage()));
			return new Responses(ErrorMsgCode.ERROR_MSG_INVALID_PARAMETER, e.getMessage());
		}

		// 验证是否具备访问权限
		PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(saasHousePublishDto.getAppId());
		if (platformCustomer == null) {
			logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", saasHousePublishDto.getAppId())));
			return new Responses(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "未找到平台用户");
		}

		try {
			houseDao.setPubType(saasHousePublishDto);
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("房源发布类型设置失败" + e.getMessage()));
			return new Responses(ErrorMsgCode.ERROR_MSG_INVALID_ISTOP_HOUSE_FAIL, "房源发布类型设置失败");
		}

		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("code", "0");
		returnMap.put("msg", "房源发布类型设置成功");
		responses.setBody(returnMap);

		return responses;
	}
}
