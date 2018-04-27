/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduService.java 
 * Package Name: com.huifenqi.hzf_platform.service 
 * Date: 2016年4月29日下午3:58:58 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.configuration.BaiduConfiguration;
import com.huifenqi.hzf_platform.context.dto.request.house.CoordinateDto;
import com.huifenqi.hzf_platform.context.entity.house.BdHouseDetailQft;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: BaiduService date: 2016年4月29日 下午3:58:58 Description: 百度API服务
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class BaiduService {

	private static final Log logger = LogFactory.getLog(BaiduService.class);

	@Autowired
	private BaiduConfiguration baiduConfiguration;

	// 百度的API地址
	private static final String BAIDU_API_DOMAIN = "http://api.map.baidu.com";

	// Geocoding API 是一类简单的HTTP接口，用于提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
	private static final String GET_CODER_API = "/geocoder/v2/";

	// 周边搜索接口
	private static final String PLACE_RADIUS_SEARCH_API = "/place/v2/search";

	// 地址详情接口
	private static final String PLACE_DETAIL_API = "/place/v2/detail";

	/**
	 * 根据经纬度获取地址的相关信息
	 * 
	 * @param lat
	 * @param lng
	 * @throws Exception
	 */
	public JsonObject getAddressByLocation(String lat, String lng) throws Exception {
		StringBuilder location = new StringBuilder();
		location.append(lat).append(",").append(lng);
		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", baiduConfiguration.getAk());
		params.put("location", location.toString());
		try {
			params.put("sn", createSignature(GET_CODER_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, GET_CODER_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonObject("result");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("根据经纬度获取地址失败:%s", e.toString())));
			throw new Exception("根据经纬度获取地址失败");
		}

	}

	/**
	 * 根据地址获取经纬度信息
	 * 
	 * @param address
	 * @param city
	 * @throws Exception
	 */
	public JsonObject getCoordinateByAddress(String address, String city) throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", baiduConfiguration.getAk());
		params.put("address", address);
		params.put("city", city);
		try {
			params.put("sn", createSignature(GET_CODER_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, GET_CODER_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				return null;
				// throw new
				// Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonObject("result");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("根据经纬度获取地址失败:%s", e.toString())));
			throw new Exception("根据经纬度获取地址失败");
		}
	}

	/**
	 * 根据地址获取经纬度信息-数据校验全房通
	 * 
	 * @param address
	 * @param city
	 * @throws Exception
	 */
	public CoordinateDto getCoordinateByAddressQft(BdHouseDetailQft qft) {
		CoordinateDto coordinateDto = new CoordinateDto();
		StringBuilder stringBuilder = new StringBuilder();
		String cityName = qft.getCityName();// 城市
		String countyName = qft.getCountyName();// 行政区
		String districtName = qft.getDistrictName();// 小区名
		String build = qft.getAddress().substring(0, qft.getAddress().indexOf(","));// 楼栋

		if ("无".equals(cityName)) {
			cityName = "";
		}

		if ("无".equals(districtName)) {
			districtName = "";
		}
		if ("无".equals(countyName)) {
			countyName = "";
		}

		// 参数校验
		if (StringUtil.isEmpty(districtName) || StringUtil.isEmpty(cityName)) {// 小区名或者城市为空时，返回空坐标
			return null;
		}

//		if (StringUtil.isEmpty(cityName) && StringUtil.isEmpty(countyName)) {// 城市和行政区都为空，返回空坐标
//			return null;
//		}

		// 参数地址拼接
		stringBuilder.append(countyName);
		stringBuilder.append(districtName);
		stringBuilder.append(build);

		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", baiduConfiguration.getAk());
		params.put("address", stringBuilder.toString());
		params.put("city", cityName);
		try {
			params.put("sn", createSignature(GET_CODER_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, GET_CODER_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status == 0) {
				JsonObject res = reJto.getAsJsonObject("result");
				String lng = res.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();
				String lat = res.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
				int precise = res.getAsJsonPrimitive("precise").getAsInt();
				int confidence = res.getAsJsonPrimitive("confidence").getAsInt();
				String level = res.getAsJsonPrimitive("level").getAsString();
				coordinateDto.setLng(lng);
				coordinateDto.setLat(lat);
				coordinateDto.setPrecise(precise);
				coordinateDto.setConfidence(confidence);
				coordinateDto.setLevel(level);
			} else {
				logger.error(LogUtils.getCommLog(String.format("根据全房通地址获取经纬度失败，房源编号:%s", qft.getOutHouseId())));
				return null;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("根据全房通地址获取经纬度失败:%s", e.toString())));
			return null;
		}
		return coordinateDto;

	}
	
	
	/**
	 * 根据地址获取经纬度信息
	 * 
	 * @param address
	 * @param city
	 * @throws Exception
	 */
	public CoordinateDto getCoordinateByAddress(String cityName,String districtName,String communityName) {
		CoordinateDto coordinateDto = new CoordinateDto();
		StringBuilder stringBuilder = new StringBuilder();

		// 参数校验
		if (StringUtil.isEmpty(communityName)) {// 物业地址为空，返回空坐标
			return null;
		}

		if (StringUtil.isEmpty(cityName) && StringUtil.isEmpty(districtName)) {// 城市和行政区都为空，返回空坐标
			return null;
		}

		// 参数地址拼接
		stringBuilder.append(districtName);
		stringBuilder.append(communityName);

		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", baiduConfiguration.getAk());
		params.put("address", stringBuilder.toString());
		params.put("city", cityName);
		try {
			params.put("sn", createSignature(GET_CODER_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, GET_CODER_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status == 0) {
				JsonObject res = reJto.getAsJsonObject("result");
				String lng = res.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();
				String lat = res.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
				int precise = res.getAsJsonPrimitive("precise").getAsInt();
				int confidence = res.getAsJsonPrimitive("confidence").getAsInt();
				String level = res.getAsJsonPrimitive("level").getAsString();
				coordinateDto.setLng(lng);
				coordinateDto.setLat(lat);
				coordinateDto.setPrecise(precise);
				coordinateDto.setConfidence(confidence);
				coordinateDto.setLevel(level);
			} else {
				logger.error(LogUtils.getCommLog(String.format("根据地址获取经纬度失败，小区名称:%s", communityName)));
				return null;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("根据地址获取经纬度失败:%s", e.toString())));
			return null;
		}
		return coordinateDto;

	}

	/**
	 * 搜索周边的信息
	 * 
	 * @param keywords
	 * @param tags
	 * @param radius
	 * @param lat
	 * @param lng
	 * @param pageSize
	 * @param sort
	 * @throws Exception
	 */
	public JsonArray radiusPlaceSearch(List<String> keywords, List<String> tags, String radius, String lat, String lng,
			String pageSize, Map<String, String> sort) throws Exception {
		try {

			// 关键字
			if (keywords == null || keywords.isEmpty()) {
				throw new Exception("关键词不能为空.");
			}
			Map<String, String> params = new LinkedHashMap<>();
			StringBuilder keyword = new StringBuilder();
			for (String kw : keywords) {
				if (keyword.length() > 0) {
					keyword.append("$");
				}
				keyword.append(kw);
			}
			params.put("query", keyword.toString());
			// 标签
			StringBuilder tag = new StringBuilder();
			if (tags != null && !tags.isEmpty()) {
				for (String tg : tags) {
					if (tag.length() > 0) {
						tag.append(",");
					}
					tag.append(tg);
				}
				params.put("tag", tag.toString());
			}

			// 是否返回POI信息
			params.put("scope", "2");

			// 每页的条数
			if (pageSize != null) {
				params.put("page_size", pageSize);
			}

			// 过滤排序
			if (sort != null && !sort.isEmpty()) {
				StringBuilder filter = new StringBuilder();
				for (String sk : sort.keySet()) {
					if (filter.length() > 0) {
						filter.append("|");
					}
					filter.append(sk).append(":").append(sort.get(sk));
				}
				params.put("filter", filter.toString());
			}

			params.put("ak", baiduConfiguration.getAk());

			StringBuilder location = new StringBuilder();
			location.append(lat).append(",").append(lng);
			params.put("location", location.toString());
			if (!StringUtil.isBlank(radius)) {
				params.put("radius", radius);
			}
			params.put("output", "json");
			params.put("timestamp", String.valueOf(System.currentTimeMillis()));
			params.put("sn", createSignature(PLACE_RADIUS_SEARCH_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, PLACE_RADIUS_SEARCH_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonArray("results");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("地址搜索失败(%s):%s", keywords, e.toString())));
			throw new Exception("地址搜索失败");
		}

	}

	/**
	 * 搜索区域内的信息
	 * 
	 * @param keywords
	 * @param tags
	 * @param radius
	 * @param lat
	 * @param lng
	 * @param pageSize
	 * @param sort
	 * @throws Exception
	 */
	public JsonArray regionPlaceSearch(List<String> keywords, List<String> tags, String region, String cityLimit,
			String pageSize, Map<String, String> sort) throws Exception {
		try {

			// 关键字
			if (keywords == null || keywords.isEmpty()) {
				throw new Exception("关键词不能为空.");
			}
			Map<String, String> params = new LinkedHashMap<>();
			StringBuilder keyword = new StringBuilder();
			for (String kw : keywords) {
				if (keyword.length() > 0) {
					keyword.append("$");
				}
				keyword.append(kw);
			}
			params.put("query", keyword.toString());
			// 标签
			StringBuilder tag = new StringBuilder();
			if (tags != null && !tags.isEmpty()) {
				for (String tg : tags) {
					if (tag.length() > 0) {
						tag.append(",");
					}
					tag.append(tg);
				}
				params.put("tag", tag.toString());
			}

			// 是否返回POI信息
			params.put("scope", "2");

			// 每页的条数
			if (pageSize != null) {
				params.put("page_size", pageSize);
			}

			// 过滤排序
			if (sort != null && !sort.isEmpty()) {
				StringBuilder filter = new StringBuilder();
				for (String sk : sort.keySet()) {
					if (filter.length() > 0) {
						filter.append("|");
					}
					filter.append(sk).append(":").append(sort.get(sk));
				}
				params.put("filter", filter.toString());
			}

			params.put("ak", baiduConfiguration.getAk());
			params.put("region", region);
			params.put("city_limit", cityLimit);

			params.put("output", "json");
			params.put("timestamp", String.valueOf(System.currentTimeMillis()));
			params.put("sn", createSignature(PLACE_RADIUS_SEARCH_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, PLACE_RADIUS_SEARCH_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonArray("results");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("地址搜索失败(%s):%s", keywords, e.toString())));
			throw new Exception("地址搜索失败");
		}

	}

	/**
	 * 获取详细的地址信息
	 * 
	 * @param poiUids
	 * @param poiUidsList
	 * @return
	 * @throws Exception
	 */
	public JsonObject getPlaceDetail(String poiUids, List<String> poiUidsList) throws Exception {
		try {
			Map<String, String> params = new LinkedHashMap<>();
			params.put("uid", poiUids);
			if (poiUidsList == null || poiUidsList.isEmpty()) {
				params.put("uids", "");
			} else {
				StringBuilder uids = new StringBuilder();
				for (String uid : poiUidsList) {
					if (uid.length() > 0) {
						uids.append(",");
					}
					uids.append(uid);
				}
				params.put("uids", uids.toString());
			}
			// 是否返回POI信息
			params.put("scope", "2");
			params.put("ak", baiduConfiguration.getAk());
			params.put("output", "json");
			params.put("timestamp", String.valueOf(System.currentTimeMillis()));
			params.put("sn", createSignature(PLACE_DETAIL_API, params));
			String url = String.format("%s%s", BAIDU_API_DOMAIN, PLACE_DETAIL_API);
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonObject("result");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("地址详情获取失败:%s", e.toString())));
			throw new Exception("地址详情获取失败");
		}
	}

	/**
	 * 创建百度接口签名
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String createSignature(String url, Map<?, ?> params) throws UnsupportedEncodingException {
		String queryStr = HttpUtil.toQueryString(params);
		String wholeStr = URLEncoder.encode(String.format("%s?%s%s", url, queryStr, baiduConfiguration.getSk()));
		return DigestUtils.md5Hex(wholeStr);
	}
}
