/** 
 * Project Name: hzf_platform 
 * File Name: HouseSubController.java 
 * Package Name: com.huifenqi.hzf_platform.controller 
 * Date: 2017年8月8日 上午11:12:39 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.HouseRequestHandler;
import com.huifenqi.hzf_platform.handler.HouseSubRequestHandler;

/**
 * ClassName: HouseSubController date: 2017年8月8日 上午11:12:39 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@RestController
public class HouseSubController {

	@Autowired
	private HouseRequestHandler houseRequestHandler;

	@Autowired
	private HouseSubRequestHandler houseSubRequestHandler;

	/**
	 * @Title: getBizNameList
	 * @Description: “热门搜索” 展示本城市房源最多的商圈名TOP12
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月8日 下午2:02:41
	 */
	@RequestMapping(value = "/house/getBizNameList", method = RequestMethod.POST)
	public Responses getBizNameList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseSubRequestHandler.getBizNameList(request);
	}

	/**
	 * @Title: addFootmarkHistory
	 * @Description: 添加浏览房源足迹记录
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月9日 下午12:01:33
	 */
	@RequestMapping(value = "/house/addFootmarkHistory", method = RequestMethod.POST)
	public Responses addFootmarkHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseSubRequestHandler.addFootmarkHistory(request);
	}

	/**
	 * @Title: deleteFootmarkHistory
	 * @Description: 删除浏览房源足迹记录
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月22日 下午5:50:49
	 */
	@RequestMapping(value = "/house/deleteFootmarkHistory", method = RequestMethod.POST)
	public Responses deleteFootmarkHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseSubRequestHandler.deleteFootmarkHistory(request);
	}

	/**
	 * @Title: getFootmarkHistoryList
	 * @Description: 获取浏览房源足迹列表
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午10:56:21
	 */
	@RequestMapping(value = "/house/getFootmarkHistoryList", method = RequestMethod.POST)
	public Responses getFootmarkHistoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getFootmarkHistoryList(request);
	}

	/**
	 * @Title: getSlideshowUrl
	 * @Description: 获取主页(会找房和会分期)的轮播图
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午10:10:36
	 */
	@RequestMapping(value = "/house/getSlideshowUrl")
	public Responses getSlideshowUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseSubRequestHandler.getSlideshowUrl();
	}

	/**
	 * 获取收藏房源列表
	 * 
	 * @author arison
	 * @dateTime 2017年8月17日 上午10:32:32
	 */
	@RequestMapping(value = "/house/getCollectHouses")
	public Responses getCollectHouses(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getCollectHouseList(request);
	}

	/**
	 * 修改收藏房源状态
	 * 
	 * @author arison
	 * @dateTime 2017年8月17日 上午10:32:36
	 */
	@RequestMapping(value = "/house/updateCollectHouseState")
	public Responses updateCollectHouseState(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.updateCollectHouse(request);
	}

	/**
	 * @Title: operateAgency
	 * @Description: 操作品牌公寓数据
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月14日 下午2:53:58
	 */
	@RequestMapping(value = "/house/operateAgency")
	public Responses operateAgency(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseSubRequestHandler.operateAgency(request);
	}

	/**
	 * @Title: getAgencyActivity
	 * @Description: 通过品牌公寓ID获取对应的活动详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月28日 下午2:23:28
	 */
	@RequestMapping(value = "/house/getActivityAgency", method = RequestMethod.POST)
	public Responses getActivityAgency(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseSubRequestHandler.getActivityAgency(request);

	}

	/**
	 * *获取租房宝典的类别列表*
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/house/getCustomRenting")
	public Responses getCustomRenting(HttpServletRequest request) {
		return houseSubRequestHandler.getCustomRentingList(request);
	}
	
	
}
