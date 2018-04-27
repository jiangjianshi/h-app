/** 
 * Project Name: hzf_platform_project 
 * File Name: SubwayController.java 
 * Package Name: com.huifenqi.hzf_platform.controller 
 * Date: 2016年4月27日下午5:31:48 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.LocationRequestHandler;

/**
 * ClassName: LocationController date: 2017年5月22日 下午5:31:48 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@RestController
public class LocationController {

	@Autowired
	private LocationRequestHandler locationRequestHandler;
	
	/**
	 * 获取城市地铁坐标接口
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/subway/initsubway")
	public Responses initSubway(long cityId, long version, HttpServletRequest request) {
		return locationRequestHandler.initSubway(cityId, version);
	}
	
	/**
	 * 获取城市商圈接口
	 * 
	 * @param cityName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loaction/zone/initzone")
	public void initzone(String cityName, String subdistrict, HttpServletRequest request) throws Exception {
		locationRequestHandler.initZone(cityName, subdistrict);
	}
	
	
	/**
	 * 初始化行政区中心坐标
	 * 
	 * @param cityName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loaction/dis/initDisCenter")
	public void initDisCenter(String cityName, String subdistrict, HttpServletRequest request) throws Exception {
		locationRequestHandler.initDisCenter(cityName, subdistrict);
	}
	
	
	/**
	 * 初始化行政区中心坐标
	 * 
	 * @param cityName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loaction/community/initCommunityCenter")
	public void initCommunityCenter(int cityId, HttpServletRequest request) throws Exception {
		locationRequestHandler.initCommunityCenter(cityId);
	}

	/**
	 * 初始化行政区中心坐标
	 * 
	 * @param cityName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loaction/community/initCommunityCenterTest")
	public Responses initCommunityCenterTest(String cityName,String disName,String comName, HttpServletRequest request) throws Exception {
		return locationRequestHandler.initCommunityCenterTest(cityName,disName,comName);
	}
	/**
	 * 查询城市的所有地铁信息
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/subway/getcitysubways")
	public Responses reqCitySubways(long cityId, long version, HttpServletRequest request) {
		return locationRequestHandler.reqCitySubways(cityId, version);
	}
	
	/**
	 * 获取全部城市地铁
	 * @param version
	 * @return
	 */
	@RequestMapping("/loaction/subway/getAllCitysSubways")
	public Responses reqCitySubways(Integer version, HttpServletRequest request) {
		return locationRequestHandler.reqAllCitysSubways(version);
	}
	
	/**
	 * 查询城市的所有商圈信息
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/businesscircle/getcitybizcircle")
	public Responses reqCityBizCircle(long cityId, long version, HttpServletRequest request) {
		return locationRequestHandler.reqCityBizCircle(cityId, version);
	}
	
	/**
	 * 获取所有城市商圈
	 * @param version
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/businesscircle/getAllCitysBizcircles")
	public Responses getAllCitysBizcircles(Integer version, HttpServletRequest request) {
		return locationRequestHandler.reqAllCitysBizcircles(version);
	}
	
	
	/**
	 * 查询城市的热点商圈信息
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/businesscircle/gethotcitybizcircle")
	public Responses reqCityHotBizCircle(long cityId, HttpServletRequest request) {
		return locationRequestHandler.reqCityHotBizCircle(cityId);
	}

	/**
	 * 查询所有的城市
	 * 
	 * @param cityId
	 * @param request
	 * @return
	 */
	@RequestMapping("/loaction/city/getallcity")
	public Responses reqAllCities(HttpServletRequest request) {
		return locationRequestHandler.reqAllCities();
	}

	/**
	 * 设置热门商圈
	 */
	@RequestMapping(value = "/loaction/businesscircle/addhotcitybizcircle", method = RequestMethod.POST)
	public Responses addHotCityBizCircle(HttpServletRequest request) throws Exception {
		return locationRequestHandler.addHotCityBizCircle(request);
	}

	/**
	 * 取消设置热门商圈
	 */
	@RequestMapping(value = "/loaction/businesscircle/delhotcitybizcircle", method = RequestMethod.POST)
	public Responses delHotCityBizCircle(HttpServletRequest request) throws Exception {
		return locationRequestHandler.delHotCityBizCircle(request);
	}

	/**
	 * 清除城市所有热门商圈
	 */
	@RequestMapping(value = "/loaction/businesscircle/clearhotcitybizcircle", method = RequestMethod.POST)
	public Responses clearHotCityBizCircle(HttpServletRequest request) throws Exception {
		return locationRequestHandler.clearHotCityBizCircle(request);
	}

	/**
	 * 查询城市id
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/location/city/getcityid")
	public Responses reqCityByName(HttpServletRequest request)throws Exception  {
		return locationRequestHandler.reqCityById(request);
	}

}
