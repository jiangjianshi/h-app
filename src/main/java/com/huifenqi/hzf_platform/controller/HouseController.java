/** 
 * Project Name: hzf_platform 
 * File Name: HouseController.java 
 * Package Name: com.huifenqi.hzf_platform.controller 
 * Date: 2016年4月26日下午4:38:39 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.HouseRequestHandler;
import com.huifenqi.hzf_platform.handler.UserRequestHandler;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;

/**
 * ClassName: HouseController date: 2016年4月26日 下午4:38:39 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@RestController
public class HouseController {

	@Autowired
	private HouseRequestHandler houseRequestHandler;
	
	@Autowired
	private UserRequestHandler userRequestHandler;

	@Autowired
	private Configuration configuration;

	/**
	 * 搜索房源
	 */
	@RequestMapping(value = "/search")
	public Responses searchHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.searchHouse(request);
	}
	
	/**
	 * 条件筛选 搜索房源
	 */
	@RequestMapping(value = "/search/searchHouseList")
	public Responses searchHouseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.searchHouseList(request);
	}
	
	/**
	 * 计算下架房源数量
	 */
	@RequestMapping(value = "/search/companyOffCount")
	public void companyOffCount(HttpServletRequest request) throws Exception {
		 houseRequestHandler.companyOffCount(request);
	}

	/**
	 * SAAS店铺页搜索房源
	 * update by arison
	 */
	@RequestMapping(value = "/sass/stallsearch")
	public Responses saasSearchHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> params = new HashMap<>();
		String source = request.getHeader("referer");
		// String source = RequestUtils.getParameterString(request, "source");
		if(StringUtil.isBlank(source))
		{
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"来源页面错误");
		}
		String url= configuration.hfqHzfSassServiceHost.trim()+"/v3/house/microstore/reqagencydetail/";
		String agencyUrl=source.substring(source.indexOf("//")+2,source.lastIndexOf("/"));
		params.put("agencyUrl",agencyUrl);
		String postRet=null;
		String agencyId=null;
		try {
			postRet = HttpUtil.post(url, params);
			ApiResult postResult = GsonUtils.jsonToBean(postRet,ApiResult.class);
			JsonObject jo=postResult.result;
			agencyId= jo.get("agencyId").getAsString();
			if(StringUtil.isBlank(agencyId)){
				throw new Exception();
			}
		}catch (Exception e){
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"请求sass失败");
		}
		return houseRequestHandler.saasSearchHouse(request,agencyId);
	}


	/**
	 * SAAS店铺页信息查询接口
	 * update by arison
	 */
	@RequestMapping(value = "/sass/getStallPage")
	public Responses sassGetStallPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, String> params = new HashMap<>();
		String source = request.getHeader("referer");
		// String source = RequestUtils.getParameterString(request, "source");
		String url= configuration.hfqHzfSassServiceHost.trim()+"/v3/house/microstore/reqagencydetail/";
		//String agencyUrl="shop.huizhaofang.com";
		String agencyUrl=source.substring(source.indexOf("//")+2,source.lastIndexOf("/"));
		params.put("agencyUrl",agencyUrl);
		String postRet=null;
		JsonObject jo=null;
		try {
			postRet = HttpUtil.post(url, params);
			ApiResult postResult = GsonUtils.jsonToBean(postRet,ApiResult.class);
			jo=postResult.result;
			if(jo==null || jo.isJsonNull())
			{
				throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"请求sass失败");
			}
		}catch (Exception e){
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"请求sass失败");
		}
		Responses responses = new Responses(jo);
		return responses;
	}

	/**
	 * 查询房源详情
	 */
	@RequestMapping(value = "/search/getHouseDetail", method = RequestMethod.POST)
	public Responses getHouseInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getHouseDetail(request);
	}

	/**
	 * 发布房源接口
	 */
	@RequestMapping(value = "/house/feedHouse", method = RequestMethod.POST)
	public Responses feedHouse(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.feedHouse(request);
	}

	/**
	 * 发布房间接口
	 */
	@RequestMapping(value = "/house/feedRoom", method = RequestMethod.POST)
	public Responses feedRoom(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.feedRoom(request);
	}

	/**
	 * 修改房源接口
	 */
	@RequestMapping(value = "/house/updateHouse", method = RequestMethod.POST)
	public Responses updateHouse(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");

		return houseRequestHandler.updateHouse(request);
	}

	/**
	 * 修改房间接口
	 */
	@RequestMapping(value = "/house/updateRoom", method = RequestMethod.POST)
	public Responses updateRoom(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.updateRoom(request);
	}

	/**
	 * 修改房源状态接口
	 */
	@RequestMapping(value = "/house/updateHouseStatus", method = RequestMethod.POST)
	public Responses updateHouseStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.updateHouseStatus(request);
	}

	/**
	 * 修改房间状态接口
	 */
	@RequestMapping(value = "/house/updateRoomStatus", method = RequestMethod.POST)
	public Responses updateRoomStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.updateRoomStatus(request);
	}

	/**
	 * 删除房源
	 */
	@RequestMapping(value = "/house/delHouse", method = RequestMethod.POST)
	public Responses delHouse(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.delHouse(request);
	}

	/**
	 * 删除房间
	 */
	@RequestMapping(value = "/house/delRoom", method = RequestMethod.POST)
	public Responses delRoom(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.delRoom(request);
	}

	/**
	 * 获取投诉列表
	 */
	@RequestMapping(value = "/house/getComplaintList")
	public Responses getComplaintList(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getComplaintList(request);
	}

	/**
	 * 检查投诉是否存在
	 */
	@RequestMapping(value = "/house/checkComplaint")
	public Responses checkComplaint(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.checkComplaint(request);
	}

	/**
	 * 保存投诉
	 */
	@RequestMapping(value = "/house/saveComplaint", method = RequestMethod.POST)
	public Responses saveComplaint(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.saveComplaint(request);
	}

	/**
	 * 获取热点公寓列表
	 */
	@RequestMapping(value = "/house/getHotApartment")
	public Responses getHotApartment(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getHotApartment(request);
	}

	/**
	 * 获取推荐房源
	 */
	@RequestMapping(value = "/house/getRecommendHouse")
	public Responses getRecommendHouse(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getRecommendHouse(request);
	}

	/**
	 * 拨打电话统计
	 */
	@RequestMapping(value = "/house/staticphonecall", method = RequestMethod.POST)
	public Responses staticPhoneCall(HttpServletRequest request, @RequestParam(required = true) String phone,
			@RequestParam(required = true) String sellId,
			@RequestParam(name = "roomId", required = false, defaultValue = "0") long roomId,
			HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.staticPhoneCall(phone, sellId, roomId);
	}

	/**
	 * 设置热门公寓
	 */
	@RequestMapping(value = "/house/addhotapartment", method = RequestMethod.POST)
	public Responses addHotApartment(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.addHotApartment(request);
	}

	/**
	 * 删除热门公寓
	 */
	@RequestMapping(value = "/house/delhotapartment", method = RequestMethod.POST)
	public Responses delHotApartment(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.delHotApartment(request);
	}

	/**
	 * 清除城市所有热门公寓
	 */
	@RequestMapping(value = "/house/clearhotapartment", method = RequestMethod.POST)
	public Responses clearHotApartment(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.clearHotApartment(request);
	}

	/**
	 * 设置推荐房源
	 */
	@RequestMapping(value = "/house/addrecommendhouse", method = RequestMethod.POST)
	public Responses addRecommendHouse(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");

		return houseRequestHandler.addRecommendHouse(request);
	}

	/**
	 * 取消设置推荐房源
	 */
	@RequestMapping(value = "/house/delrecommendhouse", method = RequestMethod.POST)
	public Responses delRecommendHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.delRecommendHouse(request);
	}

	/**
	 * 清除城市所有推荐房源
	 */
	@RequestMapping(value = "/house/clearrecommendhouse", method = RequestMethod.POST)
	public Responses clearRecommendHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");

		return houseRequestHandler.clearRecommendHouse(request);
	}

	/**
	 * 获取服务式公寓列表
	 */
	@RequestMapping(value = "/house/getServiceApartment")
	public Responses getServiceApartmentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getServiceApartmentList(request);
	}
	
	/**
	 * 添加用户订制数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/house/addOrderCustom", method = RequestMethod.POST)
	public Responses addOrderCustom(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		// 校验当前用户是否注册过，如果注册过 执行保存用户订制数据；否则，限制性注册 然后再执行保存用户订制数据
		Responses userRes = userRequestHandler.queryUserId(request);
		Map<String, String> userMap = (Map<String, String>) userRes.getBody();
		if (StringUtil.isNotEmpty(userMap.get("userId"))) {
			return houseRequestHandler.addOrderCustom(request);
		} else {
			Responses res = userRequestHandler.newUserRegister(request, response);
			Map<String, String> returnMap = (Map<String, String>) res.getBody();
			if ("0".equals(returnMap.get("errorCode"))) {
				return houseRequestHandler.addOrderCustom(request);
			} else {
				return res;
			}
		}
	}
	
	/**
	 * 获取用户订制详情
	 */
	@RequestMapping(value = "/house/getOrderCustomByUserId")
	public Responses getOrderCustomByUserId(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return houseRequestHandler.getOrderCustomByUserId(request);
	}

	/**
	 * 获取全局配置
	 */
	@RequestMapping(value = "/house/globalconf")
	public Responses getGlobalConf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseRequestHandler.getGlobalConf(request);
	}

	/**
	 * 用户数据上报功能
	 */
	@RequestMapping(value = "/house/statistics")
	public Responses webStatics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseRequestHandler.webStatics(request);
	}

	/**
	 * 用户数据上报功能
	 */
	@RequestMapping(value = "/house/testScheduler")
	public Responses testScheduler(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseRequestHandler.testScheduler(request);
	}

	/**
	 * 用户数据上报功能
	 */
	@RequestMapping(value = "/house/testReportScheduler")
	public Responses testReportScheduler(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return houseRequestHandler.testReportScheduler(request);
	}
}
