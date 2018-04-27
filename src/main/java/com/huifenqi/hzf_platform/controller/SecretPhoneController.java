package com.huifenqi.hzf_platform.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.SecretPhoneHandler;

@RestController
@RequestMapping("/phone")
public class SecretPhoneController {

	@Resource
	private SecretPhoneHandler secretPhoneHandler;
	@Resource
	private RedisCacheManager redisCacheManager;
	
	private static String secretNoPrefix = "secretNo:";//redis key 前缀
	
	@RequestMapping(value = "/getSectetPhone", method = RequestMethod.POST)
	public Responses getUnbingSectetPhone(HttpServletRequest req) {
		
		
		String sellId = req.getParameter("sellId");
		String roomId = req.getParameter("roomId");
		String agencyPhone = req.getParameter("agencyPhone");
		String secretNo = redisCacheManager.getValue(getKey(sellId, roomId, agencyPhone));
		if (StringUtils.isEmpty(secretNo)) {
			secretNo = secretPhoneHandler.getUnbingSectetPhone(sellId, roomId, agencyPhone);
			redisCacheManager.putValue(getKey(sellId, roomId, agencyPhone), secretNo, 15000);
		}
		Responses responses = new Responses();
		ImmutableMap<String, String> returnMap = ImmutableMap.of("secretNo", secretNo);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * 拼装key
	 * @param sellId
	 * @param roomId
	 * @param agencyPhone
	 * @return
	 */
	private String getKey(String sellId, String roomId, String agencyPhone) {
		return secretNoPrefix + sellId + roomId + agencyPhone;
	}

}
