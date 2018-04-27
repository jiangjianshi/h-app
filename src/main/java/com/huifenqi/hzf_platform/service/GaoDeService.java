/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduService.java 
 * Package Name: com.huifenqi.hzf_platform.service 
 * Date: 2016年4月29日下午3:58:58 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.configuration.GaodeConfiguration;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;

/** 
 * ClassName: GaoDeService
 * date: 2017年6月6日 下午3:58:58
 * Description: 高德API服务
 * 
 * @author changmingwei 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class GaoDeService {
	
	private static final Log logger = LogFactory.getLog(GaoDeService.class);
	
	@Autowired
	private GaodeConfiguration gaodeConfiguration;
	
	/**
	 * 根据城市获取商圈信息 
	 * @param address
	 * @param city
	 * @throws Exception 
	 */
	public JsonArray getCoordinateByCity(String cityName, String subdistrict) throws Exception {
		//http://restapi.amap.com/v3/config/district?key=9ab0896118026d7e3c348243cb33294f&keywords=重庆&subdistrict=3&showbiz=true&extensions=base
		// http://restapi.amap.com/v3/config/district?key=您的key&keywords=南京&subdistrict=2&extensions=base

		Map<String, String> params = new LinkedHashMap<>();
		// params.put("showbiz", "true");
		params.put("extensions", "base");	
		params.put("key", gaodeConfiguration.getKey());
		params.put("subdistrict", subdistrict);
		params.put("keywords", cityName);
		try {
			String url = gaodeConfiguration.getUrl();
			String result =  HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 1) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			JsonArray reJtoProvince = reJto.getAsJsonArray("districts");
			JsonArray reJtoCity = reJtoProvince.get(0).getAsJsonObject().getAsJsonArray("districts");
			JsonArray reJtoDistrict = reJtoCity.get(0).getAsJsonObject().getAsJsonArray("districts");
			if (subdistrict.equals("3")) {
				return reJtoDistrict;
			} else {
				return reJtoCity;
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("根据经纬度获取地址失败:%s", e.toString())));
			throw new Exception("根据城市获取商圈信息 ");
		}	
	}
}
