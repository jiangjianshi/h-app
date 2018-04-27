/** 
 * Project Name: test_20160328 
 * File Name: WeixinRequestHandller.java 
 * Package Name: com.huifenqi.usercomm.app.weixin 
 * Date: 2016年3月30日下午4:59:14 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName: WeixinRequestHandller date: 2016年3月30日 下午4:59:14 Description:
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
@Service
public class WeixinRequestHandler extends BaseRequestHandler {

	@Autowired
	private WeiXinKits weiXinKits;

	/**
	 * 获取微信JSDK的授权信息
	 * 
	 * @return
	 */
	public Responses reqWxjsdkAuth() {
		String url = getDefaultStringParam("target_url", null);
        JsonObject retJo = new JsonObject();
		try {
			if (url == null) {
				url = (String) getRequest().getHttpServletRequest().getHeader("Referer");
			}
			if (url == null) {
				 throw new BaseException(ErrorMsgCode.ERRCODE_WXJSDK_AUTH_FAILED, "获取授权信息失败");
			}
			Map<String, String> authInfos = weiXinKits.reqJsSDKAuth(url);
			for (String key : authInfos.keySet()) {
				retJo.addProperty(key, authInfos.get(key));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new  BaseException(ErrorMsgCode.ERRCODE_WXJSDK_AUTH_FAILED, "获取授权信息失败");
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 获取微信接口调用凭据
	 * 
	 * @return
	 */
	public Responses reqWxAccessToken() {
		JsonObject jsonObject = new JsonObject();
		try {
			String wxAccessToken = weiXinKits.reqWxAccessToken();
			jsonObject.addProperty("access_token", wxAccessToken);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new  BaseException(ErrorMsgCode.ERRCODE_WX_GET_ACCESS_TOKEN_FAIL, "获取授权信息失败");
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(jsonObject);
		return responses;
	}
}
