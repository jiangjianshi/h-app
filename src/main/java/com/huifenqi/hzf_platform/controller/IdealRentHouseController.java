/** 
 * Project Name: hzf_platform 
 * File Name: HouseController.java 
 * Package Name: com.huifenqi.hzf_platform.controller 
 * Date: 2016年4月26日下午4:38:39 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.IdealRentHouseRequestHandler;


/**
 * ClassName: IdealRentHouseController date: 2017年11月11日 下午4:38:39 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@RestController
public class IdealRentHouseController {

	@Autowired
	private IdealRentHouseRequestHandler idealRentHouseRequestHandler;
	
	
	/**
	 * 理想租住圈
	 * 
	 */
	@RequestMapping(value = "/house/ideal/searchIdealRentHouse")
	public Responses searchIdealRentHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return idealRentHouseRequestHandler.searchIdealRentHouse(request);
	}

	
}
