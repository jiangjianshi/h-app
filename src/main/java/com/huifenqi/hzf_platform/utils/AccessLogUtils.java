/** 
 * Project Name: feature-20170427-optimize 
 * File Name: AccessLogUtils.java 
 * Package Name: com.huifenqi.saas.utils 
 * Date: 2017年4月27日下午3:14:43 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.context.response.Responses;
import javax.servlet.http.Cookie;


/** 
 * ClassName: AccessLogUtils
 * date: 2017年4月27日 下午3:14:43
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class AccessLogUtils {

	private static Log logger = LogFactory.getLog(AccessLogUtils.class);

	/**
	 * 输出访问日志
	 * @param request
	 * @param apiResult
	 */
	public static void printAccessLog(HttpServletRequest request, Responses apiResult) {
		//获取原IP
		String sourceIp = request.getHeader("X-Real-IP");
		if(sourceIp==null) {
			sourceIp="";
		}
		//获取目的IP
		String destIp = request.getHeader("Server-IP");
		try {
			destIp = NetUtil.getFirstExternalIp();
		} catch (SocketException e) {
		}
		
		if(destIp==null) {
		    destIp="";
        }
		//获取访问接口
		String url = request.getRequestURI();

		//获取输入参数
		StringBuilder sb = new StringBuilder();
		sb.append("");
		Enumeration<String> parameters = request.getParameterNames();
		if (parameters != null) {
			while (parameters.hasMoreElements()) {
				String parameterName = parameters.nextElement();
				String parameter;
				if(StringUtil.isEmpty(parameterName)) {
					parameter="";
				}else {
					parameter=request.getParameter(parameterName);
					if(StringUtil.isEmpty(parameter)) {
						parameter="";
					}
				}
				sb.append(parameterName).append("=").append(parameter.replaceAll("\r|\n", "")).append("&");
			}
		}
		//替换掉最后一个&字符
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		//获取header
		String header = request.getHeader("device-info");
		StringBuilder headStr=new StringBuilder();
		headStr.append("");
		if(!StringUtil.isEmpty(header)) {
			header=header.replaceAll(";","&").replaceAll("\r|\n", "");
			headStr=headStr.append(header);
			header=headStr.toString();
		}else {
			header="";
		}
		
		//获取cookie
		StringBuilder cookieStr = new StringBuilder();
		cookieStr.append("");
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				String cookieValue;
				if(StringUtil.isEmpty(cookieName)) {
					cookieValue="";
				}else {
					cookieValue=cookie.getValue();
					if(StringUtil.isEmpty(cookieValue)) {
						cookieValue="";
					}
				}
				cookieStr.append(cookieName).append("=").append(cookieValue.replaceAll("\r|\n", "")).append("&");
			}
			if (cookieStr.length() > 0) {
				cookieStr.deleteCharAt(cookieStr.length() - 1);
			}
		}

		
		logger.info(String.format("%s|%s|%s|%s|%s|%s|%s|%s", sourceIp, destIp, url,
				sb.toString(), cookieStr.toString(), header, apiResult.getMeta().getErrorCode(),
				apiResult.getMeta().getErrorMessage().equals("")?"success":apiResult.getMeta().getErrorMessage()));
	}
}
