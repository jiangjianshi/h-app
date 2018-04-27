/** 
 * Project Name: hzf_platform_project 
 * File Name: Responses.java 
 * Package Name: com.huifenqi.hzf_platform.context 
 * Date: 2016年4月25日上午11:52:24 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * ClassName: Responses date: 2016年4月25日 上午11:52:24 Description: 响应结果
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class Responses {

	// 消息头部
	private ResponseMeta meta;

	// 消息题
	private Object body;

	public Responses() {
		meta = new ResponseMeta();
	}

	public Responses(Object body) {
		this();
		this.body = body;
	}

	public Responses(int errorCode, String errorMessage) {
		this();
		meta.setErrorCode(errorCode);
		meta.setErrorMessage(errorMessage);
	}

	public ResponseMeta getMeta() {
		return meta;
	}

	public void setMeta(ResponseMeta meta) {
		this.meta = meta;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Responses [meta=" + meta + ", body=" + body + "]";
	}
}
