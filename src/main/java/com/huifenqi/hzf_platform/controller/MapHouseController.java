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
import com.huifenqi.hzf_platform.handler.MapHouseRequestHandler;


/**
 * ClassName: HouseController date: 2017年10月09日 下午4:38:39 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@RestController
public class MapHouseController {

	@Autowired
	private MapHouseRequestHandler mapHouseRequestHandler;
	
	
	/**
	 * 地图/通勤 找房
	 * 
	 */
	@RequestMapping(value = "/house/map/searchMapHouse")
	public Responses searchHouseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapHouseRequestHandler.searchMapHouse(request);
	}

	/**
	 * 查询通勤配置
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/house/map/getCommuteConfig")
	public Responses getCommuteConfig(HttpServletRequest request) {
		return mapHouseRequestHandler.reqCommuteConfig();
	}
	
}
