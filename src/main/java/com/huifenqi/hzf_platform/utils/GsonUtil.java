package com.huifenqi.hzf_platform.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by arison on 2015/9/24.
 */
public class GsonUtil {

	private static Logger logger = LoggerFactory.getLogger(GsonUtil.class);

	private static Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	private static Gson camelGson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();

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
