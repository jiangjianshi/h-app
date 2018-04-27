/** 
 * Project Name: mq_project 
 * File Name: GsonUtils.java 
 * Package Name: com.huifenqi.mq.util 
 * Date: 2015年11月27日下午5:40:15 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * ClassName: GsonUtils
 * date: 2015年11月27日 下午5:40:15
 * Description: JSON工具类,使用的是google的JSON
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class GsonUtils {
	
	 private final static Gson instance = new GsonBuilder()
	            .serializeNulls()
	            .setDateFormat("yyyy-MM-dd HH:mm:ss")
	            .create();
	 
	 public static Gson getInstace() {
		 return instance;
	 }

	private static Logger logger = LoggerFactory.getLogger(GsonUtils.class);

	private static Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	private static Gson camelGson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static Gson buildGson() {
		return gson;
	}

	public static Gson buildCommGson() {
		return camelGson;
	}

	/**
	 * 将字符串转换成JavaBean
	 */
	public static <T> T jsonToBean(String json, Class<T> clazz) {
		T bean = null;
		try {
			bean = gson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			logger.debug(e.getMessage());
		}
		return bean;

	}

	/**
	 * 将JavaBean转换成字符串
	 */
	public static String beanToJson(Object bean) {
		return gson.toJson(bean);
	}

}
