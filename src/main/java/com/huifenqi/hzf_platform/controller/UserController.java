/** 
 * Project Name: hzf_platform 
 * File Name: UserController.java 
 * Package Name: com.huifenqi.hzf_platform.controller 
 * Date: 2017年8月14日下午4:48:39 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.UserRequestHandler;

/**
 * ClassName: UserController date: 2017年8月14日下午4:48:39 Description: 用户体系
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@RestController
public class UserController {

	@Autowired
	private UserRequestHandler userRequestHandler;
	
	/**
	 * @Title: getGlobalConf
	 * @Description: 获取全局配置
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午10:34:41
	 */
	@RequestMapping(value = "/globalconfig/", method = RequestMethod.POST)
	public Responses getGlobalConf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.getGlobalConf();
	}

	/**
	 * @Title: feedHouse
	 * @Description: 新用户注册接口
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午5:56:29
	 */
	@RequestMapping(value = "/user/api/newuserregister/", method = RequestMethod.POST)
	public Responses newUserRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.newUserRegister(request, response);
	}

	/**
	 * @Title: newUserLogin
	 * @Description: 用户登录接口(密码登录)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午6:50:03
	 */
	@RequestMapping(value = "/user/api/pwdlogin", method = RequestMethod.POST)
	public Responses newUserLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.newUserLogin(request, response);
	}
	
	/**
	 * @Title: userQuickLogin
	 * @Description: 新用户快捷登录接口(密码登录)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:32:03
	 */
	@RequestMapping(value = "/user/api/userquicklogin/", method = RequestMethod.POST)
	public Responses userquicklogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.userQuickLogin(request, response);
	}
	
	/**
	 * @Title: requestHzfCaptcha
	 * @Description: 获取验证码
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:36:08
	 */
	@RequestMapping(value = "/reqcaptcha/", method = RequestMethod.POST)
	public Responses requestHzfCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.requestHzfCaptcha(request, response);
	}
	
	/**
	 * @Title: verifyPhoneCaptcha
	 * @Description: 手机验证码校验
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:56:04
	 */
	@RequestMapping(value = "/user/api/verifyphonecaptcha/", method = RequestMethod.POST)
	public Responses verifyPhoneCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.verifyPhoneCaptcha(request, response);
	}
	
	/**
	 * @Title: reqImgcaptcha
	 * @Description: 获取图片验证码
	 * @return 
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:37:38
	 */
	@RequestMapping(value = "/reqimgcaptcha/")
	public void reqImgcaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Content-Type", "image/jpg");
		userRequestHandler.requestImageCaptcha(request, response);
	}
	
	/**
	 * @Title: verifyImgCaptcha
	 * @Description: 验证图形验证码
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午12:02:27
	 */
	@RequestMapping(value = "/verifyimgcaptcha/", method = RequestMethod.POST)
	public Responses verifyImgCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.verifyImgCaptcha(request, response);
	}
	
	/**
	 * @Title: isPhoneRegistered
	 * @Description: 判断手机号是否已注册
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:22:22
	 */
	@RequestMapping(value = "/user/api/verifyphoneregistered/", method = RequestMethod.POST)
	public Responses isPhoneRegistered(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.isPhoneRegistered(request, response);
	}
	
	/**
	 * @Title: userPwdSet
	 * @Description: 用户是否已设置过密码(老用户快捷登录时不用再重新设置密码)
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:26:57
	 */
	@RequestMapping(value = "/user/api/userpwdset/", method = RequestMethod.POST)
	public Responses userPwdSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.userPwdSet(request, response);
	}
	
	/**
	 * @Title: isRealNamed
	 * @Description: 判断用户是否已经实名认证
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:31:22
	 */
	@RequestMapping(value = "/user/api/isrealnamed/", method = RequestMethod.POST)
	public Responses isRealNamed(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.isRealNamed();
	}
	
	/**
	 * @Title: bindNewphone
	 * @Description: 用户绑定新的手机号
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:38:28
	 */
	@RequestMapping(value = "/user/api/bindnewphone/", method = RequestMethod.POST)
	public Responses bindNewphone(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.bindNewphone(request);
	}
	
	/**
	 * @Title: invalidUser
	 * @Description: 注销用户
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:41:17
	 */
	@RequestMapping(value = "/user/api/invaliduser/", method = RequestMethod.POST)
	public Responses invalidUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.invalidUser(request);
	}
	
	/**
	 * @Title: queryUserId
	 * @Description: 通过用户id查询用户详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:49:18
	 */
	@RequestMapping(value = "/user/api/queryuserid/", method = RequestMethod.POST)
	public Responses queryUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.queryUserId(request);
	}
	
	/**
	 * @Title: updatePassword
	 * @Description: 用户修改密码（通过短信验证找回密码） 接口
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:58:32
	 */
	@RequestMapping(value = "/user/api/updatepassword/", method = RequestMethod.POST)
	public Responses updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.updatePassword(request);
	}
	
	/**
	 * @Title: updatePasswordByOldPwd
	 * @Description: 用户修改密码（通过旧密码修改密码） 接口
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午4:21:53
	 */
	@RequestMapping(value = "/user/api/updatepasswordbyoldpwd/", method = RequestMethod.POST)
	public Responses updatePasswordByOldPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.updatePasswordByOldPwd(request);
	}
	
	/**
	 * @Title: getUserInfo
	 * @Description: 查询个人信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:11:01
	 */
	@RequestMapping(value = "/user/api/getuserinfo/", method = RequestMethod.POST)
	public Responses getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.getUserInfo(request);
	}
	
	/**
	 * @Title: updateUserInfo
	 * @Description: 修改个人信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:11:19
	 */
	@RequestMapping(value = "/user/api/updateuserinfo/", method = RequestMethod.POST)
	public Responses updateUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.updateUserInfo(request);
	}
	
	/**
	 * @Title: reqDetailInfo
	 * @Description: 查询用户详情
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:11:38
	 */
	@RequestMapping(value = "/user/api/reqdetailinfo/", method = RequestMethod.POST)
	public Responses reqDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.reqDetailInfo(request);
	}
	
	/**
	 * @Title: upadeDetailInfo
	 * @Description: 更新用户的详细信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:12:00
	 */
	@RequestMapping(value = "/user/api/upadedetailinfo/", method = RequestMethod.POST)
	public Responses upadeDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.upadeDetailInfo(request);
	}
	
	/**
	 * @Title: saveDetailInfo
	 * @Description: 完善个人详细信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午11:12:18
	 */
	@RequestMapping(value = "/user/api/savedetailinfo/", method = RequestMethod.POST)
	public Responses saveDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.addHeader("Access-Control-Allow-Origin", "*");
		return userRequestHandler.saveDetailInfo(request);
	}
	

}
