/** 
 * Project Name: mq_project 
 * File Name: RequestUtils.java 
 * Package Name: com.huifenqi.mq.utils 
 * Date: 2016年2月2日下午12:07:03 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huifenqi.hzf_platform.context.exception.LackParameterException;
import org.apache.commons.lang3.StringUtils;

import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;

/**
 * ClassName: RequestUtils date: 2016年2月2日 下午12:07:03 Description: 请求处理工具
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class RequestUtils {


	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getParameterString(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		return value.trim();
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static String getParameterString(HttpServletRequest request, String key, String defaultValue)
			throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value.trim();
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static long getParameterLong(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Long longValue = null;
		try {
			longValue = Long.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return longValue;
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static long getParameterLong(HttpServletRequest request, String key, long defaultValue) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		Long longValue = null;
		try {
			longValue = Long.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return longValue;
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static int getParameterInt(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Integer intValue = null;
		try {
			intValue = Integer.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return intValue;
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static int getParameterInt(HttpServletRequest request, String key, int defaultValue) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		Integer intValue = null;
		try {
			intValue = Integer.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return intValue;
	}

	/**
	 * 获取double参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static double getParameterDouble(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Double doubleValue = null;
		try {
			doubleValue = Double.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return doubleValue;
	}

	/**
	 * 获取double参数
	 * 
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static double getParameterDouble(HttpServletRequest request, String key, int defaultValue) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		Double doubleValue = null;
		try {
			doubleValue = Double.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return doubleValue;
	}

	/**
	 * 获取float参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static float getParameterFloat(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Float floatValue = null;
		try {
			floatValue = Float.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return floatValue;
	}

	/**
	 * 获取float参数
	 * 
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static float getParameterFloat(HttpServletRequest request, String key, int defaultValue) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		Float floatValue = null;
		try {
			floatValue = Float.valueOf(value.trim());
		} catch (Exception e) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return floatValue;
	}

	/**
	 * 获取日期参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Date getParameterDate(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Date parseDate = DateUtil.parseDate(value);
		if (parseDate == null) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return parseDate;
	}

	/**
	 * 获取日期时间参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Date getParameterDateTime(HttpServletRequest request, String key) throws Exception {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new LackParameterException("缺少参数:" + key);
		}
		Date parseDate = DateUtil.parseDateTime(value);
		if (parseDate == null) {
			throw new InvalidParameterException("参数异常:" + key);
		}
		return parseDate;
	}

	/**
	 * 获取API参数
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getApiParams(HttpServletRequest request) {
		Map<String, String> paramsMap = new HashMap<>();
		// 从HtpServletRequest中解析原始参数
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String value = "";
			if (entry.getValue() != null && entry.getValue().length > 0) {
				value = entry.getValue()[0];
			}

			// 参数放入的时候对两端的空格做处理
			paramsMap.put(entry.getKey(), StringUtil.trim(value));
		}
		return paramsMap;
	}

	protected String getSessionId(HttpServletRequest request) {
		Map<String, String> httpParams = new HashMap<>();
		// 从HtpServletRequest中解析原始参数
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String value = "";
			if (entry.getValue() != null && entry.getValue().length > 0) {
				value = entry.getValue()[0];
			}

			// 参数放入的时候对两端的空格做处理
			httpParams.put(entry.getKey(), StringUtil.trim(value));
		}
		// 先尝试从url中获取
		String sessionId = httpParams.get("sid");

		// // 获取失败并且为web客户端，再从cookie中获取
		/*String platform = getPlatform();
		if (StringUtil.isEmpty(sessionId) && Constants.platform.isWebPlatform(platform)) {
			sessionId = CookieUtil.getValue(httpServletRequest, "sid");
		}

		if (StringUtil.isEmpty(sessionId)) {
			throw new LackParameterException(getLackParaDisplayStr("会话Id"));
		}*/

		return sessionId;
	}

}
