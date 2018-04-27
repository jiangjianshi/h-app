/** 
 * Project Name: hzf_platform_project 
 * File Name: ResponseMeta.java 
 * Package Name: com.huifenqi.hzf_platform.context.response 
 * Date: 2016年4月25日下午12:04:33 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.response;

import java.util.Date;
import java.util.Map;

/** 
 * ClassName: ResponseMeta
 * date: 2016年4月25日 下午12:04:33
 * Description: 消息头部
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class ResponseMeta {
	
	//对应没一次api请求平台多会返回一个queryId，这个id是不重复且随机的一个字符串
	private String queryId;
	
	//平台分配给每个使用这得唯一身份标识id
	private String appId;
	
	//本次请求的api名称
	private String api;
	
	//标识本次请求成功还是失败，0：代表成功；1：代表失败
	private int status;
	
	//本次请求所影响到的条目数，如果是查询既返回结果数，如果是更行既更新的条目数
	private int rows;
	
	//结果集的总条目数
	private int totalRows;
	
	//错误代码
	private int errorCode;
	
	//错误信息
	private String errorMessage;
	
	//本次请求服务器处理时间，毫秒级
	private int costTime;
	
	//平台服务器接收到客户端请求的时间
	private Date date;
	
	//客户端发送请求是的请求参数字符串，该字段只有在用户请求为get方法的时候有效
	private String queryString;
	
	//客户端请求时每个参数的值，只有请求为get方式时有效
	private Map<String, String> queryParams;
	
	//服务器返回给客户端的token，此token在多步接口时有效，比如发房三步接口
	private String token;

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getCostTime() {
		return costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "ResponseMeta [queryId=" + queryId + ", appId=" + appId + ", api=" + api + ", status=" + status
				+ ", rows=" + rows + ", totalRows=" + totalRows + ", errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", costTime=" + costTime + ", date=" + date + ", queryString=" + queryString
				+ ", queryParams=" + queryParams + ", token=" + token + "]";
	}
}
