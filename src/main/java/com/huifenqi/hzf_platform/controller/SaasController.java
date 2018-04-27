package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.SaasRequestHandler;

@RestController
public class SaasController {
	
	@Autowired
	private SaasRequestHandler saasRequestHandler;

	/**
	 * SaaS房源置顶设置接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saas/isTopHouse")
	public Responses isTopHouse(HttpServletRequest request) throws Exception {
		return saasRequestHandler.isTopHouse(request);
	}

	/**
	 * SaaS设置房源发布类型
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saas/setPubType")
	public Responses setPubType(HttpServletRequest request) throws Exception {
		return saasRequestHandler.setPubType(request);
	}
}
