/** 
 * Project Name: hzf_platform_project 
 * File Name: CommUserService.java 
 * Package Name: com.huifenqi.hzf_platform.service 
 * Date: 2017年8月14日 下午5:25:58 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huifenqi.hzf_platform.context.Constants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.comm.GlobalConf;
import com.huifenqi.hzf_platform.comm.ImgCaptchaManager;
import com.huifenqi.hzf_platform.comm.UserUtils;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.CookieUtil;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtil;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.usercomm.dao.ThreePartUserRepository;
import com.huifenqi.usercomm.dao.UserInfoRepository;
import com.huifenqi.usercomm.dao.UserRepository;
import com.huifenqi.usercomm.dao.contract.InstallmentContractRepository;
import com.huifenqi.usercomm.domain.ThreePartUser;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.UserInfo;
import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import com.huifenqi.usercomm.domain.contract.InstallmentContract;

/** 
 * ClassName: CommUserService
 * date: 2017年8月14日 下午5:25:58 Description: Commom远程API服务
 * @author YDM 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class CommUserService extends BaseRequestHandler {
	
	private static final Log logger = LogFactory.getLog(CommUserService.class);
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	protected SessionManager sessionManager;
	
	@Autowired
	private ThreePartUserRepository threePartUserRepository;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ImgCaptchaManager imgCaptchaManager;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private GlobalConf globalConf;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private InstallmentContractRepository installmentContractRepository;
	
	/**
	 * @Title: getGlobalConf
	 * @Description: 获取全局配置
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月17日 上午10:36:30
	 */
	public ApiResult getGlobalConf() throws Exception {
		JsonObject retJo = new JsonObject();
		// 添加代扣参数
		Map<String, String> paymentConf = globalConf.getPayment();
		if (null == paymentConf) {
			logger.warn("failed to read payment global config, it is null");
		} else {
			JsonElement paymentE = gson.toJsonTree(paymentConf, new TypeToken<Map<String, String>>() {}.getType());
			JsonObject paymentJo = paymentE.getAsJsonObject();
			String agreementPath = paymentJo.get("agreement_path").getAsString();
			String agreementUrl = String.format("https://%s/%s", configuration.serverDomain, agreementPath);
			paymentJo.addProperty("agreement_path", agreementUrl);
			retJo.add("payment", paymentE);
		}
		// 添加登录参数
		Map<String, String> registerConf = globalConf.getRegister();
		if (null == registerConf) {
			logger.warn("failed to read register global config, it is null");
		} else {
			JsonElement registerElement = gson.toJsonTree(registerConf, new TypeToken<Map<String, String>>() {}.getType());
			JsonObject registerJo = registerElement.getAsJsonObject();
			String agreementPath = registerJo.get("agreement_path").getAsString();
			String registerAgreementUrl = String.format("https://%s/%s", configuration.serverDomain, agreementPath);
			registerJo.addProperty("agreement_path", registerAgreementUrl);
			String contractPath = registerJo.get("user_service_agreement_path").getAsString();
			String contractUrl = String.format("https://%s/%s", configuration.serverDomain, contractPath);
			registerJo.addProperty("user_service_agreement_path", contractUrl);
			registerJo.addProperty("regwx", registerJo.get("regwx").getAsString());
			retJo.add("register", registerElement);
		}
		// 添加显示开关参数
		Map<String, String> showConf = globalConf.getShow();
		if (null == registerConf) {
			logger.warn("failed to read show global config, it is null");
		} else {
			JsonElement registerElement = gson.toJsonTree(showConf, new TypeToken<Map<String, String>>() {
			}.getType());
			JsonObject registerJo = registerElement.getAsJsonObject();
			registerJo.addProperty("meter", registerJo.get("meter").getAsInt());
			registerJo.addProperty("findhouse", registerJo.get("findhouse").getAsInt());
			retJo.add("show", registerElement);
		}
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: newUserRegister
	 * @Description: 调用远程common接口 获取新用户注册接口返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午6:17:24
	 */
	public ApiResult newUserRegister(HttpServletRequest request) throws Exception {
		// 必传参数
		String phone = getStringParam("phone", "手机号");
		String platform = getStringParam("platform", "平台");
		// 非必传参数
		// 记录客户端cpu类型
		String cpu = getDefaultStringParam("cpu", null);
		String password = getDefaultStringParam("password", "931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11");// 默认密码：123456abc
		
		Map<String, String> saveUserParams = new HashMap<>();
		// 修改用户的最后登录时间
		saveUserParams.put("startsource", platform);
		saveUserParams.put("phone", phone);
		saveUserParams.put("p", password);
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_reg_service/";
		String postRet = HttpUtil.post(userServiceUrl, saveUserParams);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if ("03520004".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.USER_HAVE_EXISTED, "用户已存在");
		}
		JsonObject userInfoJo = result.result;
		long userId = userInfoJo.get("user_id").getAsLong();
		logger.debug("register userid  : " + userId);
		Map<String, String> updateUserParams = new HashMap<>();
		updateUserParams.put("user_id", userId + "");
		// 修改用户的最后登录时间
		updateUserParams.put("lastlogin", DateUtil.formatDateTime(new Date()));
		if (StringUtil.isNotBlank(cpu)) {
			updateUserParams.put("cpu", cpu);
		}
		// 如果是微信登录，则绑定用户的openid和unionid；扫码活动不需要绑定openId
		if (Constants.platform.WX.equals(platform)) {
			boolean wxInfoChanged = false;
			String wxUnionid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_unionid");
			if (StringUtil.isNotEmpty(wxUnionid)) {
				// user.setWxUnionid(wxUnionid);
				updateUserParams.put("wx_union_id", wxUnionid);
				wxInfoChanged = true;
			}
			String wxOpenid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_openid");
			if (StringUtil.isNotEmpty(wxOpenid)) {
                updateUserParams.put("wx_openid", wxOpenid);
                wxInfoChanged = true;
			}
			if (wxInfoChanged) {
				logger.info("user's unionid or openid changed, unionid=" + updateUserParams.get("wx_union_id")
						+ ", openid=" + updateUserParams.get("wx_openid"));
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "请在微信内操作！");
			}
		}
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_update_info_service/";
		try {
			postRet = HttpUtil.post(userServiceUrl, updateUserParams);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);

		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用更新用户信息接口失败");
		}
		JsonObject retJo = new JsonObject();
		retJo.addProperty("userId", userId);
		retJo.addProperty("phone", phone);
		retJo.addProperty("logFlag", 1);
		// 将用户信息放入session；app和web的会话时间是不一样的
		long timeout = SessionManager.WEB_SESSION_TIMEOUT;
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			timeout = SessionManager.APP_SESSION_TIMEOUT;
		}
		String sessionId = sessionManager.setUserId(userId, timeout);
		retJo.addProperty("sid", sessionId);
		// 添加第三方登录逻辑
		logger.debug(" before bind logic : " + platform);

		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource","");
			logger.debug(" bind source : " + loginSource);
			if(StringUtil.isNotEmpty(loginSource)) {
				String sourceId = getDefaultStringParam("sourceId", "");
				ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
				if ("qq".equals(loginSource)) {
					String qqImg = getDefaultStringParam("qqImg", "");
					String qqNickName = getDefaultStringParam("qqNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setQqId(sourceId);
					threePartUser.setQqImg(qqImg);
					threePartUser.setQqNickName(qqNickName);
					threePartUser.setQqState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
                    threePartUserRepository.save(threePartUser);
				}else if("wx".equals(loginSource)){
					String wxNickName = getDefaultStringParam("wxNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWxId(sourceId);
					threePartUser.setWxState(1);  //第一次保存时状态为1
					threePartUser.setWxNickName(wxNickName);
					threePartUser.setUpdateTime(new Date());
                    threePartUserRepository.save(threePartUser);
				}else if("wb".equals(loginSource)){
					String wbNickName = getDefaultStringParam("wxNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWbId(sourceId);
					threePartUser.setWbNickName(wbNickName);
					threePartUser.setWbState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else{
					
				}
			}
		}
		logger.debug(" after bind logic : " + platform);
		return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_OK), retJo);
	}
	
	/**
	 * @Title: newUserLogin
	 * @Description: 调用远程common接口 获取新用户登录接口(密码登录)返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月14日 下午6:52:25
	 */
	public ApiResult newUserLogin(HttpServletRequest request) throws Exception {
		// 必传参数
		String phone = getStringParam("phone", "手机号");
		String password = getStringParam("password", "密码");
		String platform = getStringParam("platform", "平台");
		// 非必传参数
		// 记录客户端cpu类型
		String cpu = getDefaultStringParam("cpu", null);
		
		// 查询用户信息调用基础用户服务 是否存在，先获取用户的user_id
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet;
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}
		JsonObject userInfoJo = result.result;
		long userId = userInfoJo.get("user_id").getAsLong();
		// 收房用户禁止登录
		int userType = userInfoJo.get("user_type").getAsInt();
		if (userType == User.USER_TYPE_ROOM_COLLECT) {
			return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_LOGIN_FORBIDDEN), "收房业务用户不允许登录");
		}
		//验证第三方登录逻辑
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource", "");
			ThreePartUser othserPartUser = threePartUserRepository.findByUserId(userId);
			if ("qq".equals(loginSource)) {
				if (othserPartUser !=null && othserPartUser.getQqState()!=0) {
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他QQ账号（" + othserPartUser.getQqNickName() + "）绑定");
				}
			}else if("wx".equals(loginSource)){
				if (othserPartUser !=null && othserPartUser.getWxState()!=0) {
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他微信账号（" + othserPartUser.getWxNickName()+ "）绑定");
				}
			}else if("wb".equals(loginSource)){
				if (othserPartUser !=null && othserPartUser.getWbState()!=0) {
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他微博账号（" + othserPartUser.getWbNickName() + "）绑定");
				}
			}
		}
		// 用户密码是否匹配
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_check_password_service/";
		params.clear();
		params.put("user_id", String.valueOf(userId));
		params.put("p", password);
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.USERNAME_OR_PWD_ERROR, "用户密码错误登录失败");
		}
		JsonObject flagresult=result.result;
		String flag=flagresult.get("flag").getAsString();
        if(!"1".equals(flag)){
			throw new BaseException(ErrorMsgCode.USERNAME_OR_PWD_ERROR, "用户密码错误登录失败");
		}
        
        Map<String, String> updateUserParams = new HashMap<>();
		updateUserParams.put("user_id", userId + "");
		// 修改用户的最后登录时间
		updateUserParams.put("lastlogin", DateUtil.formatDateTime(new Date()));
		if(cpu!=null){
			updateUserParams.put("cpu", cpu);
		}
		// 如果是微信登录，则绑定用户的openid和unionid；扫码活动不需要绑定openId
		if (Constants.platform.WX.equals(platform)) {
			boolean wxInfoChanged = false;
			String wxUnionid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_unionid");
			if (StringUtil.isNotEmpty(wxUnionid)) {
				// user.setWxUnionid(wxUnionid);
				updateUserParams.put("wx_union_id", wxUnionid);
				wxInfoChanged = true;
			}
			String wxOpenid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_openid");
			if (StringUtil.isNotEmpty(wxOpenid)) {
				// 判断微信wxOpenid是否发生变化，如果变化则微信更改次数加1
				String oldWxOpenId = userInfoJo.get("wx_openid").getAsString();
				if (!wxOpenid.equals(oldWxOpenId)) {
					updateUserParams.put("wx_change_times", (userInfoJo.get("wx_change_times").getAsInt() + 1) + "");
				}
				updateUserParams.put("wx_openid", wxOpenid);
				wxInfoChanged = true;
			}
			if (wxInfoChanged) {
				logger.info("user's unionid or openid changed, unionid=" + updateUserParams.get("wx_union_id")
						+ ", openid=" + updateUserParams.get("wx_openid"));
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "请在微信内操作！");
			}
		}
		// update user info
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_update_info_service/";
		try {
			postRet = HttpUtil.post(userServiceUrl, updateUserParams);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用更新用户信息接口失败");
		}
		JsonObject retJo = new JsonObject();
		retJo.addProperty("userId", userId);
		if(!JsonNull.INSTANCE.equals(userInfoJo.get("last_name"))){
			retJo.addProperty("name", userInfoJo.get("last_name").getAsString());
		}
		retJo.addProperty("phone", phone);
		retJo.addProperty("logFlag", 1);
		// 将用户信息放入session；app和web的会话时间是不一样的
		long timeout = SessionManager.WEB_SESSION_TIMEOUT;
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			timeout = SessionManager.APP_SESSION_TIMEOUT;
		}
		String sessionId = sessionManager.setUserId(userId, timeout);
		retJo.addProperty("sid", sessionId);
		// 添加第三方登录逻辑
		logger.debug(" before bind logic : " + platform);
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource","");
			logger.debug(" bind source : " + loginSource);
			if(StringUtil.isNotEmpty(loginSource)) {
				String sourceId = getDefaultStringParam("sourceId", "");
				ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
				if ("qq".equals(loginSource)) {
					String qqImg = getDefaultStringParam("qqImg", "");
					String qqNickName = getDefaultStringParam("qqNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setQqId(sourceId);
					threePartUser.setQqImg(qqImg);
					threePartUser.setQqNickName(qqNickName);
					threePartUser.setQqState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
                    threePartUserRepository.save(threePartUser);
				}else if("wx".equals(loginSource)){
					String wxNickName = getDefaultStringParam("wxNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWxId(sourceId);
					threePartUser.setWxState(1);  //第一次保存时状态为1
					threePartUser.setWxNickName(wxNickName);
					threePartUser.setUpdateTime(new Date());
                    threePartUserRepository.save(threePartUser);
				}else if("wb".equals(loginSource)){
					String wbNickName = getDefaultStringParam("wbNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWbId(sourceId);
					threePartUser.setWbNickName(wbNickName);
					threePartUser.setWbState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else{
					
				}
			}
		}
		logger.debug(" after bind logic : " + platform);
		return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_OK), retJo);
	}
	
	/**
	 * @Title: userQuickLogin
	 * @Description: 调用远程common接口 获取新用户快捷登录接口(密码登录)返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:33:49
	 */
	public ApiResult userQuickLogin(HttpServletRequest request) throws Exception {
		// 必传参数
		String phone = getStringParam("phone", "手机号");
		String password = getStringParam("password", "密码");
		String platform = getStringParam("platform", "平台");
		// 非必传参数
		// 记录客户端cpu类型
		String cpu = getDefaultStringParam("cpu", null);
		
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet = HttpUtil.post(userServiceUrl, params);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		boolean isNewUser = false;
		long userId = 0;
		if ("0".equals(result.status.code)) {
			isNewUser=false;//当前用户已经存在
			JsonObject userInfoJo = result.result;
			userId = userInfoJo.get("user_id").getAsLong();
			logger.debug("register userid  : "+userId);
		} else if ("03520005".equals(result.status.code)) { //当前用户不存在
			isNewUser=true;
		} else {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用服务接口失败");
		}
		Map<String, String> saveUserParams = new HashMap<>();
		saveUserParams.put("startsource", platform);
		saveUserParams.put("phone", phone);
		saveUserParams.put("p", password);
		if (isNewUser) {
			userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_reg_service/";
			postRet = HttpUtil.post(userServiceUrl, saveUserParams);
			result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
			if ("03520004".equals(result.status.code)) {
				throw new BaseException(ErrorMsgCode.USER_HAVE_EXISTED, "用户已存在");
			}
			JsonObject userInfoJo = result.result;
			userId= userInfoJo.get("user_id").getAsLong();
			logger.debug("register userid  : " + userId);
		}
		if (userId == 0) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用服务接口失败");
		}
		// 验证第三方登录逻辑 
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource", "");
			String sourceId = getDefaultStringParam("sourceId", "");
			if ("qq".equals(loginSource)) {
				ThreePartUser othserPartUser = threePartUserRepository.findByQqId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "QQ" + othserPartUser.getQqNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wx".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWxId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微信" + othserPartUser.getWxNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wb".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWbId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微博" + othserPartUser.getWbNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}
		}
		// 添加第三方登录逻辑
		logger.debug(" before bind logic : " + platform);
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource","");
			logger.debug(" bind source : " + loginSource);
			if(StringUtil.isNotEmpty(loginSource)) {
				String sourceId = getDefaultStringParam("sourceId", "");
				ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
				if ("qq".equals(loginSource)) {
					String qqImg = getDefaultStringParam("qqImg", "");
					String qqNickName = getDefaultStringParam("qqNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setQqId(sourceId);
					threePartUser.setQqImg(qqImg);
					threePartUser.setQqNickName(qqNickName);
					threePartUser.setQqState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wx".equals(loginSource)){
					String wxNickName = getDefaultStringParam("wxNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWxId(sourceId);
					threePartUser.setWxState(1);  //第一次保存时状态为1
					threePartUser.setWxNickName(wxNickName);
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wb".equals(loginSource)){
					String wbNickName = getDefaultStringParam("wbNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWbId(sourceId);
					threePartUser.setWbNickName(wbNickName);
					threePartUser.setWbState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else{
				}
			}
		}
		logger.debug(" after bind logic : " + platform);
		
		// 老用户及新用户： 更新用户的最后登录时间
		Map<String, String> updateUserParams = new HashMap<>();
		updateUserParams.put("user_id", userId + "");
		updateUserParams.put("lastlogin", DateUtil.formatDateTime(new Date()));
		if(StringUtil.isNotBlank(cpu)){
			updateUserParams.put("cpu", cpu);
		}
		// 如果是微信登录，则绑定用户的openid和unionid；扫码活动不需要绑定openId
		if (Constants.platform.WX.equals(platform)) {
			boolean wxInfoChanged = false;

			String wxUnionid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_unionid");
			if (StringUtil.isNotEmpty(wxUnionid)) {
				// user.setWxUnionid(wxUnionid);
				updateUserParams.put("wx_union_id", wxUnionid);
				wxInfoChanged = true;
			}

			String wxOpenid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_openid");
			if (StringUtil.isNotEmpty(wxOpenid)) {
				updateUserParams.put("wx_openid", wxOpenid);
				wxInfoChanged = true;
			}

			if (wxInfoChanged) {
				logger.info("user's unionid or openid changed, unionid=" + updateUserParams.get("wx_union_id")
						+ ", openid=" + updateUserParams.get("wx_openid"));
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "请在微信内操作！");
			}
		}
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_update_info_service/";
		try {
			postRet = HttpUtil.post(userServiceUrl, updateUserParams);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用更新用户信息接口失败");
		}
		JsonObject retJo = new JsonObject();
		retJo.addProperty("userId", userId);
		retJo.addProperty("phone", phone);
		retJo.addProperty("logFlag", 1);
		// app和web的会话时间是不一样的
		long timeout = SessionManager.WEB_SESSION_TIMEOUT;
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			timeout = SessionManager.APP_SESSION_TIMEOUT;
		}
		String sessionId = sessionManager.setUserId(userId, timeout);
		retJo.addProperty("sid", sessionId);
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: requestHzfCaptcha
	 * @Description: 调用远程common接口 获取验证码接口(密码登录)返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:51:11
	 */
	public ApiResult requestHzfCaptcha(HttpServletRequest request) throws Exception {
		String platform = getStringParam("platform", "平台");
		String phone = getStringParam("phone", "手机号");
		// 对电话号码的正确性加校验
		if (!RulesVerifyUtil.verifyPhone(phone)) {
			return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_PHONE_FORMATE_FAILED), "手机号不正确");
		}
		String imgCaptcha = getStringParam("imgCaptcha", "图形验证码");
		int imageVerifyResult = imgCaptchaManager.verifyImage(phone, imgCaptcha);
		if (imageVerifyResult == 0) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_IMG_CAPTCHA, "请输入正确的图形验证码!");
		}else if(imageVerifyResult==2){
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_IMG_CAPTCHA, "请重新获取图形验证码!");
		}
		// 发送短信验证码
		String clientIP = request.getHeader("X-Real-IP");
		if (StringUtil.isEmpty(clientIP)) {
			clientIP = "0.0.0.0";
			logger.warn("failed to get client ip, using the default: " + clientIP);
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("client_ip", clientIP);
		smsService.requestHzfCaptcha(phone, platform, headers);
		JsonObject retJo = new JsonObject();

		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: verifyPhoneCaptcha
	 * @Description: 调用远程common接口 校验验证码返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午12:00:38
	 */
	public ApiResult verifyPhoneCaptcha(HttpServletRequest request) throws Exception {
		String phone = getStringParam("phone", "手机号");
		String captcha = getStringParam("captcha", "验证码");
		int verifyResult = 1;
		if (!phone.equals(configuration.greenpassPhone) || !captcha.equals(configuration.greenpassCaptcha)) {
			   verifyResult=smsService.verifyCaptcha(phone, captcha);
		}
		if(0 == verifyResult){
            return new ApiResult(verifyResult+"","success");
		}else{
			 return new ApiResult(ErrorMsgCode.ERRCODE_CAPTCHA_INVALID + "", "验证码错误");	
		}
	}
	
	/**
	 * @Title: requestImageCaptcha
	 * @Description: 调用远程common接口 获取图片验证码接口(密码登录)返回
	 * @return 
	 * @author 叶东明
	 * @dateTime 2017年8月15日 上午11:52:21
	 */
	public void requestImageCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String platform = getStringParam("platform", "平台");
		// 获取app平台的版本号，如果不是App平台，返回0
		int clientVersion = getClientVersion();
		String clientIP = request.getHeader("X-Real-IP");
		logger.info(LogUtil.formatLog(String.format("[REQUEST]%s %d %s %s client_ip=%s", platform, clientVersion,
				getRequest().getUrl(), getRequest().getHttpParams(), clientIP)));
		ServletOutputStream servletOutputStream = null;
		BufferedImage bufferedImage = null;
		String phone = getStringParam("phone", "手机号");
		// 判断IP或者手机号是否已经达到上线
		if (imgCaptchaManager.reachImageLimt(clientIP, phone)) {
			logger.error("图形验证码请求次数达到上限.");
			return;
		}
		int width = getDefaultIntParam("iwidth", 0);
		int height = getDefaultIntParam("iheight", 0);
		if (width == 0 || height == 0) {
			bufferedImage = imgCaptchaManager.createImage(phone);
		} else {
			bufferedImage = imgCaptchaManager.createImage(phone, width, height);
		}
		if (null == bufferedImage) {
			logger.error("failed to create captcha image, kaptcha returned a empty image");
			return;
		}
		try {
			servletOutputStream = response.getOutputStream();
			ImageIO.write(bufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
			// 增加图形验证码的请求次数
			imgCaptchaManager.increseImageReqLimit(clientIP, phone);
		} catch (IOException e) {
			logger.error("error occured when create image captcha, e=" + e.getMessage());
		} finally {
			try {
				servletOutputStream.close();
			} catch (IOException e) {
				logger.error("error occured when close the ServletOutputStream, e=" + e.getMessage());
			}
			logger.info(LogUtil.formatLog(String.format("[RESPONSE]%s %d %s", platform, clientVersion, getRequest().getUrl())));
		}
	}
	
	/**
	 * @Title: verifyImgCaptcha
	 * @Description: 调用远程common接口 校验图片验证码返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午1:54:40
	 */
	public ApiResult verifyImgCaptcha(HttpServletRequest request) throws Exception {
		String phone = getStringParam("phone", "手机号");
		String captcha = getStringParam("captcha", "验证码");
		boolean passed = imgCaptchaManager.verify(phone, captcha);
		JsonObject retJo = new JsonObject();
		retJo.addProperty("passed", passed ? 1 : 0);
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: isPhoneRegistered
	 * @Description: 调用远程common接口 判断手机号是否已注册
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:22:04
	 */
	public ApiResult isPhoneRegistered(HttpServletRequest request) throws Exception {
		String phone = getStringParam("phone", "手机号");
		// 查询用户信息调用基础用户服务 是否存在，先获取用户的user_id
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet = HttpUtil.post(userServiceUrl, params);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if("0".equals(result.status.code)){
			return new ApiResult(String.valueOf(ErrorMsgCode.USER_HAVE_EXISTED), "用户已存在");
		}else if("03520005".equals(result.status.code)){
			JsonObject ret = new JsonObject();
			ret.addProperty("code", "0009");
			ApiResult nar = new ApiResult(0+"","success");
			nar.result = ret;
			return  nar;
		}else{
			return  result;
		}
	}
	
	/**
	 * @Title: userPwdSet
	 * @Description: 调用远程common接口 判断用户是否已设置过密码
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:28:51
	 */
	public ApiResult userPwdSet(HttpServletRequest request) throws Exception {
		String platform = getStringParam("platform", "平台");
		String phone = getStringParam("phone", "手机号");
		// 记录客户端cpu类型 非必传
		String cpu = getDefaultStringParam("cpu", null);
		
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet;
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		JsonObject retJo = new JsonObject();
		if ("03520005".equals(result.status.code)) {
			//新用户，未设置过密码
			throw new BaseException(ErrorMsgCode.NO_USER_PWD_SET, "用户未设置过密码");
		}else if(!"0".equals(result.status.code)){
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用服务接口失败");
		}
		JsonObject userInfoJo = result.result;
		// pwd_ver为2是表示已设置密码
		String isSetPwd = userInfoJo.get("pwd_ver").getAsString();
		if (!"2".equals(isSetPwd)) {
			//老用户，未设置过密码
			throw new BaseException(ErrorMsgCode.NO_USER_PWD_SET, "用户未设置过密码");
		}
		//老用户，已设置过密码时，直接调用登录接口.
		long userId = userInfoJo.get("user_id").getAsLong();
		//验证第三方登录逻辑
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource", "");
			String sourceId = getDefaultStringParam("sourceId", "");
			if ("qq".equals(loginSource)) {
				ThreePartUser othserPartUser = threePartUserRepository.findByQqId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "QQ" + othserPartUser.getQqNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wx".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWxId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微信" + othserPartUser.getWxNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wb".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWbId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微博" + othserPartUser.getWbNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}
		}
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource", "");
			ThreePartUser othserPartUser = threePartUserRepository.findByUserId(userId);
			if ("qq".equals(loginSource)) {
				if (othserPartUser !=null && othserPartUser.getQqState()!=0) {
					//User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他QQ账号（" + othserPartUser.getQqNickName() + "）绑定");
				}
			}else if("wx".equals(loginSource)){
				if (othserPartUser !=null && othserPartUser.getWxState()!=0) {
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					//throw new BaseException(ErrorCode.SOURSE_ID_EXIST_ERROR, "微信" + othserPartUser.getWxNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他微信账号（" + othserPartUser.getWxNickName()+ "）绑定");
				}
			}else if("wb".equals(loginSource)){
				if (othserPartUser !=null && othserPartUser.getWbState()!=0) {
					String otherPhone = new String(phone);
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "会分期账号（"+otherPhone+"）已被其他微博账号（" + othserPartUser.getWbNickName() + "）绑定");
				}
			}
		}
		logger.debug(" before bind logic : " + platform);
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("loginSource","");
			logger.debug(" bind source : " + loginSource);
			if(StringUtil.isNotEmpty(loginSource)) {
				String sourceId = getDefaultStringParam("sourceId", "");
				ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
				if ("qq".equals(loginSource)) {
					String qqImg = getDefaultStringParam("qqImg", "");
					String qqNickName = getDefaultStringParam("qqNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setQqId(sourceId);
					threePartUser.setQqImg(qqImg);
					threePartUser.setQqNickName(qqNickName);
					threePartUser.setQqState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wx".equals(loginSource)){
					String wxNickName = getDefaultStringParam("wxNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWxId(sourceId);
					threePartUser.setWxState(1);  //第一次保存时状态为1
					threePartUser.setWxNickName(wxNickName);
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wb".equals(loginSource)){
					String wbNickName = getDefaultStringParam("wbNickName", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWbId(sourceId);
					threePartUser.setWbNickName(wbNickName);
					threePartUser.setWbState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else{
				}
			}
		}
		logger.debug(" after bind logic : " + platform);
		// 将用户信息放入session；app和web的会话时间是不一样的
		long timeout = SessionManager.WEB_SESSION_TIMEOUT;
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			timeout = SessionManager.APP_SESSION_TIMEOUT;
		}
		String sessionId = sessionManager.setUserId(userId, timeout);
		retJo.addProperty("sid", sessionId);
		retJo.addProperty("userId", userId);
		retJo.addProperty("logFlag", 1);
		retJo.addProperty("phone", phone);
		retJo.addProperty("isSet", 1);
		// 老用户修改用户的最后登录时间
		Map<String, String> updateUserParams = new HashMap<>();
		updateUserParams.put("user_id", userId + "");
		updateUserParams.put("lastlogin", DateUtil.formatDateTime(new Date()));
		if(StringUtil.isNotBlank(cpu)){
			updateUserParams.put("cpu", cpu);
		}
		// 如果是微信登录，则绑定用户的openid和unionid；扫码活动不需要绑定openId
		if (Constants.platform.WX.equals(platform)) {
			boolean wxInfoChanged = false;
			String wxUnionid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_unionid");
			if (StringUtil.isNotEmpty(wxUnionid)) {
				updateUserParams.put("wx_union_id", wxUnionid);
				wxInfoChanged = true;
			}
			String wxOpenid = CookieUtil.getValue(getRequest().getHttpServletRequest(), "wx_openid");
			if (StringUtil.isNotEmpty(wxOpenid)) {
				updateUserParams.put("wx_openid", wxOpenid);
				wxInfoChanged = true;
			}
			if (wxInfoChanged) {
				logger.info("user's unionid or openid changed, unionid=" + updateUserParams.get("wx_union_id")
						+ ", openid=" + updateUserParams.get("wx_openid"));
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "请在微信内操作！");
			}
		}
		// update user info
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_update_info_service/";
		try {
			postRet = HttpUtil.post(userServiceUrl, updateUserParams);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);

		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用更新用户信息接口失败");
		}
		//修改用户的最后登录时间
		return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_OK), retJo);
	}
	
	/**
	 * @Title: isRealNamed
	 * @Description: 调用远程common接口 判断用户是否已经实名认证返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:36:15
	 */
	public ApiResult isRealNamed() throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		if (0 == userId) {
			logger.info(LogUtil.formatLog("session timeout, id=" + sessionId));
			throw new BaseException(ErrorMsgCode.ERRCODE_NEED_LOGIN,"较长时间未操作，请您重新登录");
		}
		//先去userInfo表中查询用户身份证信息。
		String idCardNo = null;
		UserInfo userInfo = userInfoRepository.findByUserId(userId);
		if (userInfo != null && userInfo.getUserIdNo() != null) {
			idCardNo=userInfo.getUserIdNo();
		}
		if(StringUtil.isBlank(idCardNo)) {
			User user = userUtils.getUser(userId);
			if(user != null) {
				idCardNo = user.getUserIdNo();
			}
		}
		boolean isRealNamed = false;
		if (StringUtil.isBlank(idCardNo)) {
			throw new BaseException(ErrorMsgCode.NO_REAL_NAME_USER, "该用户没有实名");
		} else {
			//根据身份证号查询用户银行卡列表，判断用户是否实名过。
			String url = configuration.getPaymentServiceUrl() + "api/bankcard/list/";
			Map<String, String> params = new HashMap<>();
			params.put("id_card_no", idCardNo);
			String postRet = HttpUtil.post(url, params);
			ApiResult bankcardList = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
			if(bankcardList.result!=null) {
				JsonElement jsonList = bankcardList.result.get("card_list");
				if(jsonList!=null) {
					JsonArray ja = jsonList.getAsJsonArray();
					if (ja != null && ja.size() > 0) {
						isRealNamed = true;
					}
				}
			}
		}
		if(!isRealNamed){
			throw new BaseException(ErrorMsgCode.NO_REAL_NAME_USER, "该用户没有实名");
		}
		return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_OK), new JsonObject());
	}
	
	/**
	 * @Title: bindNewphone
	 * @Description: 调用远程common接口 用户绑定新的手机号
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:38:51
	 */
	public ApiResult bindNewphone(HttpServletRequest request) throws Exception {
		String phone = getStringParam("phone", "手机号");
		// 查询用户信息调用基础用户服务 是否存在，先获取用户的user_id
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet = HttpUtil.post(userServiceUrl, params);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if("0".equals(result.status.code)){
			return new ApiResult(String.valueOf(ErrorMsgCode.USER_HAVE_EXISTED), "用户已存在");
		}else if("03520005".equals(result.status.code)){
			JsonObject ret = new JsonObject();
			ret.addProperty("code", "0009");
			ApiResult nar = new ApiResult(0+"","success");
			nar.result = ret;
			return  nar;
		}else{
			return  result;
		}
	}
	
	/**
	 * @Title: invalidUser
	 * @Description: 调用远程common接口 注销用户接口返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:42:02
	 */
	public ApiResult invalidUser(HttpServletRequest request) throws Exception {
		/*String phone = getStringParam("phone");
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		if (0 == userId) {
			logger.info(LogUtil.formatLog("session timeout, id=" + sessionId));
			throw new BaseException(ErrorMsgCode.ERRCODE_NEED_LOGIN,"较长时间未操作，请您重新登录");
		}
		//判断用户是否有没下在进行尚未完成的交易。
		boolean trueCondition = true;
		User user=new User();
		user.setUserid(userId);
		user.setPhone(phone);
		List<ContractSnapshot> effectContractList = contractService.findNoLogoffContractSnapshots(user);
		if (effectContractList.size()>0) {
			throw new BaseException(ErrorMsgCode.USER_HAVE_EFFECT_CONTRACT, "您有进行中的交易尚未完成，暂不支持注销");
		} else {
			List<InstallmentContract> effectInstallmentContractList = contractService.findNoLogoffInstallmentContract(user);
			if (effectInstallmentContractList.size() > 0) {
				throw new BaseException(ErrorMsgCode.USER_HAVE_EFFECT_CONTRACT, "您有进行中的交易尚未完成，暂不支持注销");
			}
		}
		// 是否智能电表用户
		int pageNum = 1;
		int pageSize = 3;
		JsonObject reqJo = smartService.reqMeterList(userId, pageNum, pageSize);
		JsonObject retJo = new JsonObject();
		JsonArray mArray = new JsonArray();

		int totalNum = reqJo.get("totalNum").getAsInt();
		logger.info(" totalNum of user smartmeter : "+totalNum);
		if (totalNum > 0) {
			throw new BaseException(ErrorMsgCode.USER_HAVE_EFFECT_CONTRACT, "您有进行中的交易尚未完成，暂不支持注销");
		}
		// 注销第三方登录接口
        ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
        if(threePartUser!=null && StringUtil.isNotBlank(threePartUser.getUserId()+"")) {
            threePartUserRepository.invalidUserById(threePartUser.getId(),new Date());
        }
		// 调用注销用户接口
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_unreg_service/";
		Map<String, String> params = new HashMap<>();
		params.put("user_id", userId+"");
		String postRet = HttpUtil.post(userServiceUrl, params);*/
		ApiResult result = GsonUtil.buildGson().fromJson("", ApiResult.class);
		return result;
	}
	
	/**
	 * @Title: queryUserId
	 * @Description: 调用远程common接口 通过用户id查询用户详情
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午3:56:30
	 */
	public ApiResult queryUserId(HttpServletRequest request) throws Exception {
		String phone = getStringParam("phone", "手机号");
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet = HttpUtil.post(userServiceUrl, params);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		long userId = 0;
		JsonObject retJo = new JsonObject();
		if ("0".equals(result.status.code)) {
			JsonObject userInfoJo = result.result;
			userId = userInfoJo.get("user_id").getAsLong();
			logger.debug("register userid  : " + userId);
		} else if ("03520005".equals(result.status.code)){
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}else{
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "调用服务接口失败");
		}
		retJo.addProperty("userId", userId);
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: updatePassword
	 * @Description: 调用远程common接口 用户修改密码返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午4:00:10
	 */
	public ApiResult updatePassword(HttpServletRequest request) throws Exception {
		String phone = this.getStringParam("phone");
		String platform = getStringParam("platform", "平台");
		// 查询用户信息调用基础用户服务 是否存在，先获取用户的user_id
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		String postRet = HttpUtil.post(userServiceUrl, params);
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}
		JsonObject userInfoJo = result.result;
		long userId = userInfoJo.get("user_id").getAsLong();
		//验证第三方登录逻辑
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("login_source", "");
			String sourceId = getDefaultStringParam("source_id", "");
			if ("qq".equals(loginSource)) {
				ThreePartUser othserPartUser = threePartUserRepository.findByQqId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "QQ" + othserPartUser.getQqNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wx".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWxId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微信" + othserPartUser.getWxNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}else if("wb".equals(loginSource)){
				ThreePartUser othserPartUser = threePartUserRepository.findByWbId(sourceId);
				if (othserPartUser !=null && othserPartUser.getUserId() != userId) {
					User otherUser = userUtils.getUser(othserPartUser.getUserId());
					String otherPhone = otherUser.getPhone();
					otherPhone = otherPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					throw new BaseException(ErrorMsgCode.SOURSE_ID_EXIST_ERROR, "微博" + othserPartUser.getWbNickName() + " 已被其他会分期账号（" + otherPhone + "）绑定");
				}
			}
		}
		//验证第三方登录逻辑 end
		String password = this.getStringParam("password");
		// 调用接口
	    userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_set_password_service/";
		params = new HashMap<>();
		params.put("user_id", userId+"");
		params.put("p", password);
	    postRet = HttpUtil.post(userServiceUrl, params);
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if(!"0".equals(result.status.code)){
			throw new BaseException(ErrorMsgCode.MODIFY_USER_PWD_FAIL, "修改密码失败");
		}
		// 添加第三方登录逻辑
		logger.debug(" before bind logic : " + platform);
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			String loginSource = getDefaultStringParam("login_source","");
			logger.debug(" bind source : " + loginSource);
			if(StringUtil.isNotEmpty(loginSource)) {
				String sourceId = getDefaultStringParam("source_id", "");
				ThreePartUser threePartUser = threePartUserRepository.findByUserId(userId);
				if ("qq".equals(loginSource)) {
					String qqImg = getDefaultStringParam("qq_img", "");
					String qqNickName = getDefaultStringParam("qq_nick_name", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setQqId(sourceId);
					threePartUser.setQqImg(qqImg);
					threePartUser.setQqNickName(qqNickName);
					threePartUser.setQqState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wx".equals(loginSource)){
					String wxNickName = getDefaultStringParam("wx_nick_name", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWxId(sourceId);
					threePartUser.setWxState(1);  //第一次保存时状态为1
					threePartUser.setWxNickName(wxNickName);
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else if("wb".equals(loginSource)){
					String wbNickName = getDefaultStringParam("wb_nick_name", "");
					if (threePartUser == null) {
						threePartUser = new ThreePartUser();
						threePartUser.setState(1);
						threePartUser.setCreateTime(new Date());
					}
					threePartUser.setUserId(userId);
					threePartUser.setWbId(sourceId);
					threePartUser.setWbNickName(wbNickName);
					threePartUser.setWbState(1);  //第一次保存时状态为1
					threePartUser.setUpdateTime(new Date());
					threePartUserRepository.save(threePartUser);
				}else{
				}
			}
		}
		logger.debug(" after bind logic : " + platform);
		return result;
	}
	
	/**
	 * @Title: updatePasswordByOldPwd
	 * @Description: 调用远程common接口 用户修改密码返回
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年8月15日 下午4:23:06
	 */
	public ApiResult updatePasswordByOldPwd(HttpServletRequest request) throws Exception {
		String oldPassword = this.getStringParam("old_password");
		String newPassword = this.getStringParam("new_password");
		String sessionId = getSessionId();
		
		long userId = sessionManager.getUserId(sessionId);
		if (0 == userId) {
			logger.info(LogUtil.formatLog("session timeout, id=" + sessionId));
			throw new BaseException(ErrorMsgCode.ERRCODE_NEED_LOGIN,"较长时间未操作，请您重新登录");
		}
		//检验原密码是否正确；用户密码是否匹配
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_check_password_service/";
		Map<String, String> params = new HashMap<>();
		params.clear();
		params.put("user_id", userId + "");
		params.put("p", oldPassword);
		String postRet="";
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		JsonObject flagresult=result.result;
		String flag=flagresult.get("flag").getAsString();
		if(!"1".equals(flag)){
			throw new BaseException(ErrorMsgCode.OLD_PWD_ERROR, "原密码不正确，请重新输入");
		}
		if(oldPassword.equals(newPassword)){
			throw new BaseException(ErrorMsgCode.OLDPWD_SAMEAS_NEWPWE_ERROR, "新密码不能与原密码相同");
		}
		// 调用接口
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_mod_password_service/";
		params.put("user_id", userId+"");
		params.put("p", oldPassword); // 原密码明文做sha256摘要
		params.put("new_p", newPassword); // 新密码明文做sha256摘要
		postRet = HttpUtil.post(userServiceUrl, params);
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if(!"0".equals(result.status.code)){
			throw new BaseException(ErrorMsgCode.MODIFY_USER_PWD_FAIL, "修改密码失败");
		}
		return result;
	}
	
	/**
	 * @Title: getUserInfo
	 * @Description: 查询个人信息
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午10:31:50
	 */
	public ApiResult getUserInfo(HttpServletRequest request) throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		JsonObject retJo = new JsonObject();
		User user = userRepository.findByUseridAndState(userId, 1);
		retJo.addProperty("name", StringUtil.trimToEmpty(user.getLastName()));
		retJo.addProperty("gender", StringUtil.parseInt(user.getSex(), 0));
		retJo.addProperty("userIdNo", StringUtil.trimToEmpty(user.getUserIdNo()));
		retJo.addProperty("workAddress", StringUtil.trimToEmpty(user.getWorkAddress()));
		retJo.addProperty("homeAddress", StringUtil.trimToEmpty(user.getHomeAddress()));
		retJo.addProperty("phone", StringUtil.trimToEmpty(user.getPhone()));
		// 根据用户phone查询出用户所有合同
		if (StringUtil.isEmpty(user.getUserIdNo())) {
			List<ContractSnapshot> contractList = contractService.findContractSnapshots(user);
			if (contractList != null && !contractList.isEmpty()) {
				for (ContractSnapshot cs : contractList) {
					if (!StringUtil.isBlank(cs.getUserIdNo())) {
						user.setUserIdNo(cs.getUserIdNo());
						retJo.addProperty("userIdNo", cs.getUserIdNo());
						break;
					}
				}
			}
		}
		if (StringUtil.parseInt(user.getSex(), 0) == 0) {
			retJo.addProperty("can_edit_gender", 1);
		} else {
			retJo.addProperty("can_edit_gender", 0);
		}
		if (StringUtil.isEmpty(user.getLastName())) {
			retJo.addProperty("can_edit_name", 1);
		} else {
			retJo.addProperty("can_edit_name", 0);
		}
		if (StringUtil.isEmpty(user.getUserIdNo())) {
			retJo.addProperty("can_edit_id_no", 1);
		} else {
			retJo.addProperty("can_edit_id_no", 0);
		}
		
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: updateUserInfo
	 * @Description: 修改个人信息
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午10:35:05
	 */
	public ApiResult updateUserInfo(HttpServletRequest request) throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		User user = userRepository.findByUseridAndState(userId,1);
		boolean hasDataChanged = false;
		String name = getDefaultStringParam("name", "");
		if (StringUtil.isNotEmpty(name) && !name.equals(user.getLastName())) {
			if (StringUtil.isNotEmpty(user.getLastName())) {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "已录入有效姓名，禁止修改");
			}
			user.setLastName(name);
			hasDataChanged = true;
		}
		String gender = getDefaultStringParam("gender", "");
		if (StringUtil.isNotEmpty(gender) && !gender.equals(user.getSex())) {
			user.setSex(gender);
			hasDataChanged = true;
		}
		String userIdNo = getDefaultStringParam("user_id_no", "");
		if (StringUtil.isNotEmpty(userIdNo) && !userIdNo.equals(user.getUserIdNo())) {
			if (StringUtil.isNotEmpty(user.getUserIdNo())) {
				throw new BaseException(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "已录入有效身份证号，禁止修改");
			}
			user.setUserIdNo(userIdNo);
			hasDataChanged = true;
		}
		String workAddress = getDefaultStringParam("work_address", "");
		if (StringUtil.isNotEmpty(workAddress) && !workAddress.equals(user.getWorkAddress())) {
			user.setWorkAddress(workAddress);
			hasDataChanged = true;
		}
		String homeAddress = getDefaultStringParam("home_address", "");
		if (StringUtil.isNotEmpty(homeAddress) && !homeAddress.equals(user.getHomeAddress())) {
			user.setHomeAddress(homeAddress);
			hasDataChanged = true;
		}
		if (hasDataChanged) {
			userRepository.save(user);
			logger.info(String.format("data of user %s has been changed", userId));
		}

		return new ApiResult();
	}
	
	/**
	 * @Title: reqDetailInfo
	 * @Description: 查询用户详情
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午10:42:13
	 */
	public ApiResult reqDetailInfo(HttpServletRequest request) throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：" + userId);
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}
		UserInfo info = userInfoRepository.findByUserId(userId);
		if (info == null) {
			info = fillUserInfoFromContract(info, userId, 1);
			if (info !=null) {
				logger.info(LogUtil.formatLog(String.format("初始化用户详细信息:%s", info)));
				userInfoRepository.save(info);
			} else {
				info = new UserInfo();
			}
		}
		// 上一份合同中的银行卡信息可能不是最新，需要更新用户银行卡到最新 add
		String idCardNo=info.getUserIdNo();
		if (StringUtil.isNotBlank(idCardNo)) {
			String url = configuration.getPaymentServiceUrl() + "api/bankcard/list/";
			Map<String, String> params = new HashMap<>();
			params.put("idCardNo", idCardNo);
			String postRet=null;
			try {
				postRet = HttpUtil.post(url, params);
			}catch (Exception e){
				throw new BaseException(e);
			}
			ApiResult bankcardList = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
			if(bankcardList.result!=null) {
				JsonElement jsonList = bankcardList.result.get("cardList");
				if(jsonList!=null) {
					JsonArray ja = jsonList.getAsJsonArray();
					for(JsonElement je:ja){
						JsonObject j= je.getAsJsonObject();
						if(!j.isJsonNull()) {  //已绑过卡
							info.setUserBankAccount(j.get("userName").getAsString());
							info.setUserCardNo(j.get("bankCardNo").getAsString());
							info.setUserBankName(j.get("cardName").getAsString());
							info.setUserCardMobile(j.get("phone").getAsString());
						}else{
							info.setUserBankAccount("");
							info.setUserCardNo("");
							info.setUserBankName("");
							info.setUserCardMobile("");
						}
						break;
					}
				}
			}else{
				info.setUserBankAccount("");
				info.setUserCardNo("");
				info.setUserBankName("");
				info.setUserCardMobile("");
			}
		}
		//更新用户银行卡到 end
		info.setUserMobile(user.getPhone());
		JsonObject retJo = GsonUtil.buildGson().fromJson(GsonUtil.beanToJson(info), JsonObject.class);
		return new ApiResult(retJo);
	}
	
	/**
	 * @Title: upadeDetailInfo
	 * @Description: 更新用户的详细信息
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午10:44:30
	 */
	public ApiResult upadeDetailInfo(HttpServletRequest request) throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		UserInfo uInfo = userInfoRepository.findByUserId(userId);
		if (uInfo == null) {
			//从合同中获取用户信息
			uInfo = fillUserInfoFromContract(uInfo, userId, 2);
		}
		String name = getDefaultStringParam("name", null);
		if (name != null) {
			if (!RulesVerifyUtil.verifyName(name)) {
				return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "姓名有误");
			}
			uInfo.setName(name);
		}
		String gender = getDefaultStringParam("sex", "");
		if (StringUtil.isNotEmpty(gender)) {
			logger.info("user info sex : "+gender);
			uInfo.setUserSex(Integer.parseInt(gender));
		}
		String userIdNo = getDefaultStringParam("userIdNo", null);
		if (userIdNo != null) {
			uInfo.setUserIdNo(userIdNo);
		}
		Date date = new Date();
		uInfo.setUpdateTime(date);
		logger.info(LogUtil.formatLog(String.format("修改用户详细信息:%s", uInfo)));
		userInfoRepository.save(uInfo);
		
		return new ApiResult();
	}
	
	/**
	 * @Title: saveDetailInfo
	 * @Description: 完善个人详细信息
	 * @return ApiResult
	 * @author 叶东明
	 * @dateTime 2017年9月18日 上午10:45:26
	 */
	public ApiResult saveDetailInfo(HttpServletRequest request) throws Exception {
		String sessionId = getSessionId();
		long userId = sessionManager.getUserId(sessionId);
		
		String name = getStringParam("name", "姓名");
		if (!RulesVerifyUtil.verifyName(name)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "姓名有误");
		}
		String userIdNo = getStringParam("userIdNo", "身份证号").toUpperCase();
		if (!RulesVerifyUtil.verifyIdNo(userIdNo)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "身份证号有误");
		}
		String qqNo = getStringParam("qqNo", "QQ号");
		if (!RulesVerifyUtil.verifyQQNo(qqNo)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "QQ号有误");
		}
		int linkmanRelation = getIntParam("linkmanRelation", "紧急联系人关系");
		String linkmanName = getStringParam("linkmanName", "紧急联系人姓名");
		if (!RulesVerifyUtil.verifyName(linkmanName, true)) {
			logger.error(LogUtil.formatLog("用户姓名:" + linkmanName));
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "紧急联系人姓名有误");
		}
		String linkmanPhone = getStringParam("linkmanPhone", "紧急联系人电话");
		if (!RulesVerifyUtil.verifyPhone(linkmanPhone)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "紧急联系人电话号码有误");
		}
		// 获取手机号
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：" + userId);
			return new ApiResult(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}
		// 对比用户和紧急联系人的手机号
		if (linkmanPhone.equals(user.getPhone())) {
			logger.error("紧急联系人电话号码与用户手机号相同" + "用户手机号：" + user.getPhone() + "紧急联系人手机号：" + linkmanPhone);
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "紧急联系人电话号码与用户手机号相同");
		}
		// 对比用户和紧急联系人的姓名
		if (linkmanName.equals(name)) {
			logger.error("紧急联系人姓名与用户姓名相同" + "用户姓名：" + name + "紧急联系人姓名：" + linkmanName);
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "紧急联系人姓名与用户姓名相同");
		}
		String coName = getDefaultStringParam("coName", "");
		String coMail = getDefaultStringParam("coMail", "");
		if (!"".equals(coMail) && !RulesVerifyUtil.verifyEmail(coMail)) {
			return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "邮箱有误");
		}
		String coAddress = getDefaultStringParam("coAddress", "");
		int coPosiStatus = getDefaultIntParam("coPosiStatus", 0);
		String coPosiName = getDefaultStringParam("coPosiName", "");
		int revenueInfo = getDefaultIntParam("revenueInfo", 0);
		String position = getDefaultStringParam("position", "");
		String contractNo = getDefaultStringParam("contractNo", "");
		if (!"".equals(contractNo)) {
			InstallmentContract installmentContract;
			installmentContract = installmentContractRepository.findByContractNo(contractNo);
			// 房东电话
			String ownerPhone = installmentContract.getOwnerPhone();
			// 对比房东和紧急联系人的手机号
			if (linkmanPhone.equals(ownerPhone)) {
				logger.error("紧急联系人电话号码与房东手机号相同" + "房东手机号：" + ownerPhone + "紧急联系人手机号：" + linkmanPhone);
				return new ApiResult(ErrorMsgCode.ERRCODE_INVALID_PARAM, "紧急联系人电话号码与房东手机号相同");
			}
		}
		try {
			Map<Object, Object> infoMap = userUtils.getUserInfoFromCache(user.getPhone());
			JsonObject properties = null;
			int infoStatus = Constants.User.USER_INFO_STATUS_DETAIL;
			if (infoMap == null || infoMap.isEmpty()) {
				return new ApiResult(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "请先提交身份证信息");
			} else {
				int cacheStatus = Integer.valueOf(infoMap.get("info_status").toString());
				if (cacheStatus == Constants.User.USER_INFO_STATUS_CHARGED) {
					return new ApiResult(ErrorMsgCode.ERRCODE_FORBIDDEN_OPER, "不允许提交");
				}
				if (cacheStatus > infoStatus) {
					infoStatus = cacheStatus;
				}
				properties = GsonUtil.buildGson().fromJson((String) infoMap.get("userInfo"), JsonObject.class);
			}
			properties.addProperty("userId", user.getUserid());
			properties.addProperty("name", name);
			properties.addProperty("userIdNo", userIdNo);
			properties.addProperty("qqNo", qqNo);
			properties.addProperty("linkmanRelation", linkmanRelation);
			properties.addProperty("linkmanName", linkmanName);
			properties.addProperty("linkmanPhone", linkmanPhone);
			properties.addProperty("coName", coName);
			properties.addProperty("coMail", coMail);
			properties.addProperty("coAddress", coAddress);
			properties.addProperty("coPosiStatus", coPosiStatus);
			properties.addProperty("coPosiName", coPosiName);
			properties.addProperty("revenueInfo", revenueInfo);
			properties.addProperty("position", position);
			properties.addProperty("contractNo", contractNo);

			int userSource = -1;
			try {
				userSource=Integer.valueOf(infoMap.get("userSource").toString());
			} catch (Exception e) {
                   logger.error(" userSource change error  " +e.getMessage());
			}
			if (userSource != -1) {
				userUtils.updateUserInfo(user.getPhone(), infoStatus, userSource,properties);
			} else {
				userUtils.updateUserInfo(user.getPhone(), infoStatus, properties);
			}
			// 更新用户表里面的身份证号
			user.setUserIdNo(userIdNo);
			// 新签约时重新将微信的更新次数设置为1
			user.setWxChangeTimes(1);
			userRepository.save(user);
		} catch (Exception e) {
			logger.error("保存用户信息失败：" + e);
			return new ApiResult(ErrorMsgCode.ERRCODE_MATERIAL_SAVE_FAILED, "信息保存失败");
		}
		try {
			userUtils.syncUserInfoFromCacheToDB(user.getUserid(), UserInfo.USER_INFO_STATUS_DETAIL);
		} catch (Exception e) {
			logger.error("查询用户信息失败：" + e);
			return new ApiResult(ErrorMsgCode.ERRCODE_USER_INFO_UNEXIST, "查询用户信息失败");
		}

		return new ApiResult();
	}
	
	/**
	 * 从合同中获取用户详细信息
	 * @param uInfo
	 * @param userId
	 * @param opt 1.初始化 2.修改
	 * @return
	 */
	private UserInfo fillUserInfoFromContract(UserInfo uInfo, long userId, int opt) {
		if (uInfo == null) {
			uInfo = new UserInfo();
		}
		User user = userUtils.getUser(userId);
		if (user == null) {
			logger.error("没有该用户：" + userId);
			throw new BaseException(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户不存在");
		}
		List<InstallmentContract> installmentContracts;
		installmentContracts = contractService.findAllInstallmentContracts(user);
		if (CollectionUtils.isNotEmpty(installmentContracts)) {
			InstallmentContract ic = installmentContracts.get(0);
			uInfo.setQqNo(ic.getUserQq());
			uInfo.setUserBankAccount(ic.getUserBankAccount());
			uInfo.setUserBankName(ic.getUserBank());
			uInfo.setUserCardNo(ic.getUserCardNo());
			uInfo.setUserCardMobile(ic.getUserCardMobile());
			uInfo.setUserCompanyAddress(ic.getUserCompanyAddress());
			uInfo.setUserCompanyMail(ic.getUserCompanyMail());
			uInfo.setUserCompanyName(ic.getUserCompanyName());
			uInfo.setUserIncome(ic.getUserIncome());
			uInfo.setUserJob(ic.getUserJob());
			uInfo.setUserLinkmanMobile(ic.getUserLinkmanMobile());
			uInfo.setUserLinkmanName(ic.getUserLinkmanName());
			uInfo.setUserLinkmanRelation(ic.getUserLinkmanRelation());
			uInfo.setUserWorkStatus(ic.getUserStatus());
			uInfo.setName(ic.getUserName());
			uInfo.setUserIdNo(ic.getUserIdNo());
			switch (ic.getUserIdNo().length()) {
			case 15:
				if (new Integer(ic.getUserIdNo().charAt(14) + "") % 2 == 0) {
					uInfo.setUserSex(2);
				} else {
					uInfo.setUserSex(1);
				}
				break;
			case 18:
				if (new Integer(ic.getUserIdNo().charAt(16) + "") % 2 == 0) {
					uInfo.setUserSex(2);
				} else {
					uInfo.setUserSex(1);
				}
				break;
			default:
				break;
			}
		} else if (opt == 1) {
			return null;
		}
		Date now = new Date();
		uInfo.setCreateTime(now);
		uInfo.setUpdateTime(now);
        uInfo.setUserId(userId);
        uInfo.setUserMobile(user.getPhone());
        logger.info(" the user"+userId+"   mobile is ******"+user.getPhone());
		return uInfo;
	}
	
	
}
