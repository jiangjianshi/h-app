/** 
 * Project Name: fileservice_project 
 * File Name: ResponseUtils.java 
 * Package Name: com.huifenqi.file.utils 
 * Date: 2016年4月26日下午7:36:23 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huifenqi.hzf_platform.context.response.BdResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.huifenqi.hzf_platform.PlatformInterceptor;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.ResponseMeta;
import com.huifenqi.hzf_platform.context.response.Responses;

/**
 * ClassName: ResponseUtils date: 2016年4月26日 下午7:36:23 Description: 响应处理工具
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class ResponseUtils {

	private static final Log logger = LogFactory.getLog(PlatformInterceptor.class);

	public static final String ACCEPT_XML = "text/xml";

	/**
	 * 根据用户端接受的文件格式，生成对应的格式
	 * 
	 * @param request
	 * @param responses
	 * @return
	 */
	public static String getResponseContent(HttpServletRequest request, Responses responses) {
		// 获取客户端能够接收的数据格式
		String accept = request.getHeader("Accept");
		String content = "";
		if (ACCEPT_XML.equals(accept)) {
			try {
				content = JacksonUtils.getXmlMapper().writeValueAsString(responses);
			} catch (JsonProcessingException e) {
				logger.error(LogUtils.getCommLog(String.format("转XML失败:%s", e.getMessage())));
			}
		} else {
			content = GsonUtils.getInstace().toJson(responses);
		}
		return content;
	}



	/**
	 * add by arsion 20170414
	 * 根据用户端接受的文件格式，生成对应的格式
	 *
	 * @param request
	 * @param bdResponses
	 * @return
	 */
	public static String getResponseContent(HttpServletRequest request, BdResponses bdResponses) {
		// 获取客户端能够接收的数据格式
		String accept = request.getHeader("Accept");
		String content = "";
		if (ACCEPT_XML.equals(accept)) {
			try {
				content = JacksonUtils.getXmlMapper().writeValueAsString(bdResponses);
			} catch (JsonProcessingException e) {
				logger.error(LogUtils.getCommLog(String.format("转XML失败:%s", e.getMessage())));
			}
		} else {
			content = GsonUtils.getInstace().toJson(bdResponses);
		}
		return content;
	}

	/**
	 * 向客户端输出内容
	 * 
	 * @param response
	 * @param content
	 */
	public static void writer(HttpServletResponse response, String content) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(content);
			out.flush();
		} catch (IOException e) {
			logger.error(LogUtils.getCommLog(String.format("响应客户端出错:%s", e.getMessage())));
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(LogUtils.getCommLog(String.format("关闭输出流出错:%s", e.getMessage())));
				}
			}
		}
	}

	/**
	 * 设置主要响应元数据
	 * 
	 * @param meta
	 * @param request
	 */
	public static void fillMainMeta(ResponseMeta meta, HttpServletRequest request) {
		if (meta == null) {
			return;
		}
		if (request == null) {
			return;
		}

		String appId = ResponseUtils.getAppId(request);
		meta.setAppId(appId);

		String pathInfo = request.getServletPath();
		meta.setApi(pathInfo);

		int errorCode = meta.getErrorCode();
		if (errorCode == ErrorMsgCode.ERROR_MSG_OK) {
			meta.setStatus(0); // TODO 定义常量
		} else {
			meta.setStatus(1);
		}

		if (meta.getErrorMessage() == null) {
			meta.setErrorMessage(StringUtil.EMPTY);
		}

		if ("GET".equals(request.getMethod())) {

			Map<String, String> params = RequestUtils.getApiParams(request);

			// 移除掉不需要排序的值
			params.remove("appId");
			params.remove("secretKey");
			params.remove("ts");
			params.remove("sign");

			meta.setQueryParams(params);
			meta.setQueryString(ResponseUtils.getQueryString(params));
		}

		if (meta.getQueryString() == null) {
			meta.setQueryString(StringUtil.EMPTY);
		}

		if (StringUtil.isEmpty(meta.getToken())) {
			meta.setToken(getToken());
		}

	}

	/**
	 * 设置查询Id
	 * 
	 * @param meta
	 * @param queryId
	 */
	public static void fillQueryId(ResponseMeta meta, String queryId) {
		if (meta == null) {
			return;
		}
		meta.setQueryId(queryId);
	}

	/**
	 * 设置接收请求时间
	 * 
	 * @param meta
	 * @param startTime
	 */
	public static void fillDate(ResponseMeta meta, Date startTime) {
		if (meta == null) {
			return;
		}
		if (startTime == null) {
			return;
		}
		meta.setDate(startTime);
	}

	/**
	 * 设置记录条数
	 * 
	 * @param meta
	 * @param startTime
	 */
	public static void fillRow(ResponseMeta meta, int rows, int totalRows) {
		if (meta == null) {
			return;
		}
		meta.setRows(rows);
		meta.setTotalRows(totalRows);
	}

	/**
	 * 获取处理时间
	 * 
	 * @param meta
	 * @param startTime
	 * @return
	 */
	public static int getAndFillCostTime(ResponseMeta meta, Date startTime) {
		if (meta == null) {
			return 0;
		}
		if (startTime == null) {
			return 0;
		}

		meta.setDate(startTime);

		Date endDate = new Date();
		int costTime = (int) (endDate.getTime() - startTime.getTime());

		meta.setCostTime(costTime);

		return costTime;

	}

	/**
	 * 获取AppId
	 * 
	 * @param request
	 * @return
	 */
	public static String getAppId(HttpServletRequest request) {
		if (request == null) {
			return null;
		}

		String appId = null;
		try {
			appId = RequestUtils.getParameterString(request, "appId");
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog("get appId failed, " + e.getMessage()));
		}

		return appId;

	}

	/**
	 * 获取查询字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String getQueryString(Map<String, String> params) {
		if (params == null) {
			return null;
		}

		StringBuilder queryString = new StringBuilder();

		Set<Entry<String, String>> entrySet = params.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();

		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			String value = next.getValue();
			queryString.append(key);
			queryString.append(StringUtil.EQUALS);
			queryString.append(value);
			if (iterator.hasNext()) {
				queryString.append(StringUtil.AND);
			}
		}

		return queryString.toString();
	}

	/**
	 * 获取查询Id
	 * 
	 * @return
	 */
	public static String getQueryId() {
		String queryId = MsgUtils.generateNoncestr(16);
		return queryId;
	}

	/**
	 * 
	 * 获取Token
	 * 
	 * @return
	 */
	public static String getToken() {
		return MsgUtils.generateNoncestr(32);
	}

}
