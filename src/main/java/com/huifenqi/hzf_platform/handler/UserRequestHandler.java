/** 
 * Project Name: hzf_platform 
 * File Name: UserRequestHandler.java 
 * Package Name: com.huifenqi.hzf_platform.handler 
 * Date: 2017年8月14日 下午5:12:45 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.service.CommUserService;
import com.huifenqi.hzf_platform.utils.*;
import com.huifenqi.hzf_platform.vo.ApiResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;

/**
 * ClassName: UserRequestHandler date: 2017年8月14日 下午5:12:45 Description:
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Service
public class UserRequestHandler {

	private static final Log logger = LogFactory.getLog(UserRequestHandler.class);

	@Autowired
	private CommUserService commUserService;
	
	@Autowired
	private Configuration configuration;
	
	/**
	 * @Title: getGlobalConf
	 * @Description: 获取全局配置
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午10:34:30
	 */
	public Responses getGlobalConf() throws Exception {
		ApiResult result = (ApiResult) commUserService.getGlobalConf();
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: newUserRegister
	 * @Description: 新用户注册接口
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午6:15:36
	 */
	public Responses newUserRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.newUserRegister(request);
		
		String platform = RequestUtils.getParameterString(request, "platform");
		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code) && (Constants.platform.isWebPlatform(platform))) {
			int timeout = (int) (SessionManager.WEB_SESSION_TIMEOUT / 1000);
			// 微信的过期时间设置为和app一样
			if (Constants.platform.WX.equals(platform)) {
				timeout = (int) (SessionManager.APP_SESSION_TIMEOUT / 1000);
			}
			String domain = configuration.serverDomain.substring(configuration.serverDomain.indexOf("."));
			CookieUtil.save("sid", result.result.get("sid").getAsString(), timeout, domain, "/", response);
			CookieUtil.save("phone", result.result.get("phone").getAsString(), timeout, domain, "/", response);
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: newUserLogin
	 * @Description: 用户登录接口(密码登录)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午6:50:43
	 */
	public Responses newUserLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.newUserLogin(request);

		String platform = RequestUtils.getParameterString(request, "platform");
		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code) && (Constants.platform.isWebPlatform(platform))) {
			int timeout = (int) (SessionManager.WEB_SESSION_TIMEOUT / 1000);
			// 微信的过期时间设置为和app一样
			if (Constants.platform.WX.equals(platform)) {
				timeout = (int) (SessionManager.APP_SESSION_TIMEOUT / 1000);
			}
			String domain = configuration.serverDomain.substring(configuration.serverDomain.indexOf("."));
			CookieUtil.save("sid", result.result.get("sid").getAsString(), timeout, domain, "/", response);
			CookieUtil.save("phone", result.result.get("phone").getAsString(), timeout, domain, "/", response);
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: userQuickLogin
	 * @Description: 新用户快捷登录接口(密码登录)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:31:44
	 */
	public Responses userQuickLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.userQuickLogin(request);

		String platform = RequestUtils.getParameterString(request, "platform");
		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code) && (Constants.platform.isWebPlatform(platform))) {
			int timeout = (int) (SessionManager.WEB_SESSION_TIMEOUT / 1000);
			// 微信的过期时间设置为和app一样
			if (Constants.platform.WX.equals(platform)) {
				timeout = (int) (SessionManager.APP_SESSION_TIMEOUT / 1000);
			}
			String domain = configuration.serverDomain.substring(configuration.serverDomain.indexOf("."));
			CookieUtil.save("sid", result.result.get("sid").getAsString(), timeout, domain, "/", response);
			CookieUtil.save("phone", result.result.get("phone").getAsString(), timeout, domain, "/", response);
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: requestHzfCaptcha
	 * @Description: 获取验证码
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:44:49
	 */
	public Responses requestHzfCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.requestHzfCaptcha(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: verifyPhoneCaptcha
	 * @Description: 手机验证码校验
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:57:07
	 */
	public Responses verifyPhoneCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.verifyPhoneCaptcha(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: requestImageCaptcha
	 * @Description: 获取图片验证码
	 * @return 
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:52:01
	 */
	public void requestImageCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		commUserService.requestImageCaptcha(request, response);
	}

	/**
	 * @Title: verifyImgCaptcha
	 * @Description: 验证图形验证码
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午12:03:14
	 */
	public Responses verifyImgCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.verifyImgCaptcha(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: isPhoneRegistered
	 * @Description: 判断手机号是否已注册
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:22:31
	 */
	public Responses isPhoneRegistered(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.isPhoneRegistered(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	/**
	 * @Title: userPwdSet
	 * @Description: 用户是否已设置过密码
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:27:57
	 */
	public Responses userPwdSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ApiResult result = (ApiResult) commUserService.userPwdSet(request);

		String platform = RequestUtils.getParameterString(request, "platform");
		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code) && (Constants.platform.isWebPlatform(platform))) {
			int timeout = (int) (SessionManager.WEB_SESSION_TIMEOUT / 1000);
			// 微信的过期时间设置为和app一样
			if (Constants.platform.WX.equals(platform)) {
				timeout = (int) (SessionManager.APP_SESSION_TIMEOUT / 1000);
			}
			String domain = configuration.serverDomain.substring(configuration.serverDomain.indexOf("."));
			CookieUtil.save("sid", result.result.get("sid").getAsString(), timeout, domain, "/", response);
			CookieUtil.save("phone", result.result.get("phone").getAsString(), timeout, domain, "/", response);
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: isRealNamed
	 * @Description: 判断用户是否已经实名认证
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:33:59
	 */
	public Responses isRealNamed() throws Exception {
		ApiResult result = (ApiResult) commUserService.isRealNamed();
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: bindNewphone
	 * @Description: 用户绑定新的手机号
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:38:23
	 */
	public Responses bindNewphone(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.bindNewphone(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: bindNewphone
	 * @Description: 注销用户
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:41:41
	 */
	public Responses invalidUser(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.invalidUser(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: queryUserId
	 * @Description: 通过用户id查询用户详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:55:11
	 */
	public Responses queryUserId(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.queryUserId(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("userId", String.valueOf(result.result.get("user_id")));
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: updatePassword
	 * @Description: 用户修改密码（通过短信验证找回密码） 
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:59:35
	 */
	public Responses updatePassword(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.updatePassword(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: updatePasswordByOldPwd
	 * @Description: 用户修改密码（通过旧密码修改密码） 接口
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午4:22:21
	 */
	public Responses updatePasswordByOldPwd(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.updatePasswordByOldPwd(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: getUserInfo
	 * @Description: 查询个人信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:03:17
	 */
	public Responses getUserInfo(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.getUserInfo(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: updateUserInfo
	 * @Description: 修改个人信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:03:46
	 */
	public Responses updateUserInfo(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.updateUserInfo(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: reqDetailInfo
	 * @Description: 查询用户详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:04:10
	 */
	public Responses reqDetailInfo(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.reqDetailInfo(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: upadeDetailInfo
	 * @Description: 更新用户的详细信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:04:33
	 */
	public Responses upadeDetailInfo(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.upadeDetailInfo(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: saveDetailInfo
	 * @Description: 完善个人详细信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:05:04
	 */
	public Responses saveDetailInfo(HttpServletRequest request) throws Exception {
		ApiResult result = (ApiResult) commUserService.saveDetailInfo(request);
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result.result.toString());
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		responses.setBody(returnMap);
		return responses;
	}

	
}
