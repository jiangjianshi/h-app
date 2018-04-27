package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.context.enums.smartlock.GwModityPwdErrorCodeEnum;
import com.huifenqi.hzf_platform.context.enums.smartlock.LockTypeEnum;
import com.huifenqi.hzf_platform.context.enums.smartlock.OnlineEnum;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.RulesVerifyUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.hzf_platform.vo.DoorLockVo;
import com.huifenqi.usercomm.dao.DoorLockRepository;
import com.huifenqi.usercomm.domain.DoorLock;


/**
 * Created by Arison on 2017/12/19.
 * 
 * 智能门锁相关
 * 
 */
@Service
public class DoorLockRequestHandler extends BaseRequestHandler {

	@Autowired
    private DoorLockRepository doorLockRepository;

	@Autowired
	private Configuration configuration;

	@Autowired
	private SessionManager sessionManager;
	private static final String SMART_LOCK_API = "http://%s/%s";
	
	private static Logger logger = LoggerFactory.getLogger(DoorLockRequestHandler.class);

	/**
	 * @Title: updateGesturePwd
	 * @Description: 保存/更新用户手势密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年9月6日 下午5:51:18
	 */
	public Responses updateGesturePwd() {
		long userId =  sessionManager.getUserIdFromSession();
		if(userId==0){
			throw new BaseException(ErrorMsgCode.ERRCODE_NEED_LOGIN, "用户未登录");
		}
		String gesturePwd = getStringParam("gesturePwd","手势密码");
		String phone = getDefaultStringParam("phone", "");
		int touchId = getDefaultIntParam("touchID", 1);
		// 通过userId查询用户下是否存在手势密码：存在，则执行更新，否则执行保存
		DoorLock doorLock = doorLockRepository.getDoorLockByUserId(userId);
		if (doorLock == null) {
			doorLock = new DoorLock();
			doorLock.setGesturePassword(gesturePwd);
			doorLock.setUserId(userId);
			doorLock.setPhone(phone);
			doorLock.setCreateTime(new Date());
			doorLock.setUpdateTime(new Date());
			doorLock.setTouchId(touchId);
			doorLock.setState(1);
			DoorLock doorLocks = doorLockRepository.save(doorLock);
			if (doorLocks == null) {
				throw new BaseException(ErrorMsgCode.SAVE_GESTURE_PWD_ERROR, "智能门锁手势密码保存失败");
			}
		} else {
			int updateFlag = doorLockRepository.updateDoorLock(userId, gesturePwd, phone, touchId);
			if (updateFlag == 0) {
				throw new BaseException(ErrorMsgCode.UPDATE_GESTURE_PWD_ERROR, "智能门锁手势密码更新失败");
			}
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: getGesturePwd
	 * @Description: 通过userId获取手势密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年9月6日 下午6:04:01
	 */
	public Responses getGesturePwd() {
		JsonObject retJo = new JsonObject();
		long userId =  sessionManager.getUserIdFromSession();
		// 通过userId查询手势密码
		DoorLock doorLock = doorLockRepository.getDoorLockByUserId(userId);
		if (doorLock == null) {
			throw new BaseException(ErrorMsgCode.GESTURE_PWD_UNEXIST, "手势密码不存在");
		} else {
			retJo.addProperty("gesturePwd", doorLock.getGesturePassword());
			retJo.addProperty("touchID", doorLock.getTouchId());
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}
	
	/**
	 * @Title: checkPwd
	 * @Description: 验证密码是否正确
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年9月13日 上午10:35:29
	 */
	public Responses checkPwd() throws BaseException {
		String password = getStringParam("password", "密码");
		long userId =  sessionManager.getUserIdFromSession();
		// 1、验证用户是否存在
		String userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_query_service/";
		Map<String, String> params = new HashMap<>();
		DoorLock doorLock = doorLockRepository.getDoorLockByUserId(userId);
		if (doorLock == null) {
			throw new BaseException(ErrorMsgCode.GESTURE_PWD_UNEXIST, "手势密码不存在");
		} else {
			params.put("phone", doorLock.getPhone());
		}
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
		// 2、验证密码是否正确
		userServiceUrl = configuration.hfqUserServiceIp + "/user_server/user_check_password_service/";
		params.clear();
		params.put("user_id", userId + "");
		params.put("p", password);
		try {
			postRet = HttpUtil.post(userServiceUrl, params);
		} catch (Exception e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INTERNAL_ERROR,"未知类型的错误");
		}
		result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.USERNAME_OR_PWD_ERROR, "密码错误，请重新输入");
		}
		JsonObject flagresult = result.result;
		String flag = flagresult.get("flag").getAsString();
        if(!"1".equals(flag)){
			throw new BaseException(ErrorMsgCode.USERNAME_OR_PWD_ERROR, "密码错误，请重新输入");
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
	 * @Title: checkUser
	 * @Description: 验证输入手机号是否对应当前用户
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月19日 下午4:07:54
	 */
	public Responses checkUser() throws BaseException {
		long userId =  sessionManager.getUserIdFromSession();
		String phone = getStringParam("phone", "手机号");
		// 验证传入手机号对应用户是否是当前用户
		DoorLock doorLock = doorLockRepository.getDoorLockByPhone(phone);
		if (doorLock != null) {
			long userIdCheck = doorLock.getUserId();
			if (userIdCheck != userId) {
				throw new BaseException(ErrorMsgCode.USER_NOT_EXIST, "传入手机号对应用户与当前用户不匹配");
			}
		}
		Responses responses = new Responses();
		Map<String, String> returnMap = new HashMap<String, String>();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(returnMap);
		return responses;
	}
	
	/**
	 * @Title: getDoorLockList
	 * @Description: 获取智能门锁列表
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月19日 下午5:05:43
	 */
	public Responses getDoorLockList() throws BaseException {
//		long userId =  sessionManager.getUserIdFromSession();
		long userId =  14;//TODO 测试时去掉
		int pageNum = getDefaultIntParam("pageNum", 1);
		int pageSize = getDefaultIntParam("pageSize", 20);
		Map<String, String> lockParams = new HashMap<>();
		lockParams.put("userId", String.valueOf(userId));
		lockParams.put("pageNum", String.valueOf(pageNum));
		lockParams.put("pageSize", String.valueOf(pageSize));
		lockParams.put("lockType", String.valueOf(LockTypeEnum.ALL.getCode()));
		String lockServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost, "v3/locker/customer/list");
		String postRet = "";
		try {
			postRet= HttpUtil.post(lockServiceUrl, lockParams);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		if (postRet.contains("\"result\":null")) {
			postRet = postRet.replace("\"result\":null", "\"result\":{}");
		}
		if (postRet.contains(":null")) {
			postRet = postRet.replace(":null", ":\"\"");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		// 如果租客没有门锁，也返回查询成功，只是列表为空
		if ("03426035".equals(result.status.code)) {
			result.status.code = "0";
			result.status.description = "success";
			JsonArray jsonArray = new JsonArray();
			result.result.add("lockers", jsonArray);
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		JsonObject ret=new JsonObject();
		//进行json排序
		JsonObject jo=sortJson(result.result);
		ret.add("result",jo);
		responses.setBody(ret);
		return responses;
	}

	//对json串进行排序
	private JsonObject sortJson(JsonObject result){
		JsonArray lockers=result.get("lockers").getAsJsonArray();
		if(!lockers.isJsonNull()){
			List<DoorLockVo> doorLockVoList=new ArrayList<>();
			Gson gson = new Gson();
			doorLockVoList =gson.fromJson(lockers.toString(), new TypeToken<List<DoorLockVo>>(){}.getType());
//			for(JsonElement je:lockers){
//				DoorLockVo doorLockVo=new DoorLockVo();
//				JsonObject jo=je.getAsJsonObject();
//				doorLockVo.setSmartDeviceId(jo.get("smartDeviceId")==null?"":jo.get("smartDeviceId").getAsString());
//				doorLockVo.setSn(jo.get("sn")==null?"":jo.get("sn").getAsString());
//				doorLockVo.setLockMac(jo.get("lockMac")==null?"":jo.get("lockMac").getAsString());
//				doorLockVo.setDeviceName(jo.get("deviceName")==null?"":jo.get("deviceName").getAsString());
//				doorLockVo.setLockType(jo.get("lockType")==null?0:jo.get("lockType").getAsInt());
//				doorLockVo.setUseType(jo.get("useType")==null?0:jo.get("useType").getAsInt());
//				doorLockVo.setLocation(jo.get("location")==null?"":jo.get("location").getAsString());
//				doorLockVo.setElectricity(jo.get("electricity")==null?0:jo.get("electricity").getAsInt());
//				doorLockVo.setOnline(jo.get("online")==null?"":jo.get("online").getAsString());
//				doorLockVo.setCreateTime(jo.get("createTime")==null?"":jo.get("createTime").getAsString());
//				doorLockVoList.add(doorLockVo);
//			}
			List<DoorLockVo> retDoorLockVoList =doorLockVoList.stream()
					.sorted((x,y)-> {
								if (x.getUseType() > y.getUseType()) {
									return 1;
								} else if (x.getUseType() == y.getUseType()) {
									return x.getCreateTime().compareTo(y.getCreateTime());
								} else {
									return -1;
								}
							}
					).collect(Collectors.toList());
			result.add("lockers",GsonUtil.buildGson().toJsonTree(retDoorLockVoList));
		}
		return result;
	}
	
	/**
	 * @Title: getDoorLockPwd
	 * @Description: 获取蓝牙锁密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月19日 下午2:41:38
	 */
	public Responses getDoorLockPwd() throws BaseException {
		long userId =  sessionManager.getUserIdFromSession();                                                      // 用户ID
		String lockerDeviceId = getDefaultStringParam("lockerDeviceId", StringUtil.EMPTY);         // 设备ID
		// 获取钥匙
		Map<String, String> keyParams = new HashMap<>();
		keyParams.put("smartDeviceId", lockerDeviceId);
		keyParams.put("userId", String.valueOf(userId));
		String keyServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost, "v3/key/customer/key");
		String postKeyRet = null;
		try {
			postKeyRet = HttpUtil.post(keyServiceUrl, keyParams);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		if (postKeyRet.contains(":null")) {
			postKeyRet = postKeyRet.replace(":null", ":\"\"");
		}
		ApiResult keyResult = GsonUtil.buildGson().fromJson(postKeyRet, ApiResult.class);
		JsonObject jsonData = keyResult.result;
		String keyNo = jsonData.get("keyNo").getAsString();
		if (StringUtil.isBlank(keyNo)) {
			throw new BaseException(ErrorMsgCode.LOCK_NOT_EXIST, "当前无权开此门锁，请联系经纪人处理");
		}
		// 获取蓝牙锁密码
		Map<String, String> lockParams = new HashMap<>();
		lockParams.put("smartDeviceId", lockerDeviceId);
		lockParams.put("userId", String.valueOf(userId));
		lockParams.put("keyNo", keyNo);
		String lockServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost, "v3/pwd/customer/get");
		String postRet = null;
		try {
			postRet = HttpUtil.post(lockServiceUrl, lockParams);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		if (postRet.contains("\"result\":null")) {
			postRet = postRet.replace("\"result\":null", "\"result\":{}");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);

		if(!"0".equals(result.status.code)){
			throw new BaseException(ErrorMsgCode.LOCK_NOT_EXIST, "当前无权开此门锁，请联系经纪人处理");
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		JsonObject ret=new JsonObject();
		ret.add("result",result.result);
		responses.setBody(ret);
		return responses;
	}
	
	/**
	 * @Title: getDoorUnlockRecord
	 * @Description: 获取开锁记录
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月19日 下午2:56:29
	 */
	public Responses getDoorUnlockRecord() throws BaseException {
		long userId =  sessionManager.getUserIdFromSession();                                                   // 用户ID
		String lockerDeviceId = getDefaultStringParam("lockerDeviceId", StringUtil.EMPTY);      // 设备ID
		Date nowDate = new Date();
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(nowDate);  
        calendar.add(Calendar.DAY_OF_MONTH, -15);
		String startTime = DateUtil.format("yyyy-MM-dd HH:mm:ss", calendar.getTime());
		String endTime = DateUtil.format("yyyy-MM-dd HH:mm:ss", nowDate);
		int pageNum = getDefaultIntParam("pageNum", 1);
		int pageSize = getDefaultIntParam("pageSize", 20);
		Map<String, String> lockParams = new HashMap<>();
		lockParams.put("smartDeviceId", lockerDeviceId);
		lockParams.put("userId", String.valueOf(userId));
		lockParams.put("startTime", startTime);
		lockParams.put("endTime", endTime);
		lockParams.put("pageNum", String.valueOf(pageNum));
		lockParams.put("pageSize", String.valueOf(pageSize));
		String lockServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost, "v3/locker/customer/unlock_record");
		String postRet = null;
		try {
			postRet = HttpUtil.post(lockServiceUrl, lockParams);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}
		if (postRet.contains("\"result\":null")) {
			postRet = postRet.replace("\"result\":null", "\"result\":{}");
		}
		if (postRet.contains(":null")) {
			postRet = postRet.replace(":null", ":\"\"");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		JsonObject ret=new JsonObject();
		ret.add("result",result.result);
		responses.setBody(ret);
		return responses;
	}
	
	/**
	 * @Title: getDynamicPassword
	 * @Description: 获取临时密码
	 * @return Responses
	 * @author
	 * @dateTime 2018年03月13日 上午11:20:04
	 */
	public Responses getDynamicPassword() {
		long userId = sessionManager.getUserIdFromSession(); // 用户ID
		String smartDeviceId = getDefaultStringParam("smartDeviceId", StringUtil.EMPTY); // 设备ID
		String houseUuid = getDefaultStringParam("houseUuid", StringUtil.EMPTY); // 房源ID
		String roomUuid = getDefaultStringParam("roomUuid", StringUtil.EMPTY); // 房间ID

		// 获取临时密码
		Map<String, String> lockParams = new HashMap<>();
		lockParams.put("userId", String.valueOf(userId));
		lockParams.put("smartDeviceId", smartDeviceId);
		lockParams.put("houseUuid", houseUuid);
		lockParams.put("roomUuid", roomUuid);
		String lockServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost,
				"v3/pwd/customer/dynamic_pwd");
		String postRet = null;
		try {
			postRet = HttpUtil.post(lockServiceUrl, lockParams);
			ApiResult resu = GsonUtil.jsonToBean(postRet, ApiResult.class);
			resu.result.addProperty("effectiveTime", "有效期：今天00:00:00-23:59:59");//result中加入有效期
			postRet = GsonUtil.beanToJson(resu);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "服务暂时不可用");
		}
		if (postRet.contains("\"result\":null")) {
			postRet = postRet.replace("\"result\":null", "\"result\":{}");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);

		if (!"0".equals(result.status.code)) {
			throw new BaseException(ErrorMsgCode.DYNAMIC_NOT_EXIST, "获取临时密码错误，请联系经纪人处理");
		}

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(Integer.parseInt(result.status.code));
		responses.getMeta().setErrorMessage(result.status.description);
		JsonObject ret = new JsonObject();
		ret.add("result", result.result);
		responses.setBody(ret);
		return responses;
	}
	
		
	/**
	 * 网关锁修改密码
	 * @return
	 * @throws BaseException
	 */
	public Responses modifyPwd() throws BaseException {
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(1);
		
		long userId = sessionManager.getUserIdFromSession();// 用户ID
		String lockerDeviceId = getDefaultStringParam("lockerDeviceId", StringUtil.EMPTY);// 设备ID
		String houseUuid = getDefaultStringParam("houseUuid", StringUtil.EMPTY);// 房源ID
		String roomUuid = getDefaultStringParam("roomUuid", StringUtil.EMPTY);// 房间ID
		String isOnline = getDefaultStringParam("isOnline", StringUtil.EMPTY);// 是否在线
		String oldPwd = getDefaultStringParam("oldPwd", StringUtil.EMPTY);// 原始密码
		String newPwd = getDefaultStringParam("newPwd", StringUtil.EMPTY);// 新密码
		if (OnlineEnum.OFFLINE.getCode().equals(isOnline)) {
			responses.getMeta().setErrorCode(100);
			responses.getMeta().setErrorMessage("门锁离线，无法修改！");
			return responses;
		}
		if (!RulesVerifyUtil.verifyNoSixToTen(oldPwd)) {
			responses.getMeta().setErrorMessage("原密码格式不正确，请重新输入！");
			return responses;
		}
		if (!RulesVerifyUtil.verifyNoSixToTen(newPwd)) {
			responses.getMeta().setErrorMessage("新密码格式不正确，请重新输入！");
			return responses;
		}
		if (oldPwd.equals(newPwd)) {
			responses.getMeta().setErrorMessage("新密码不能与原密码相同！");
			return responses;
		}
		Map<String, String> lockParams = new HashMap<>();
		lockParams.put("userId", String.valueOf(userId));
		lockParams.put("smartDeviceId", lockerDeviceId);
		lockParams.put("houseUuid", houseUuid);
		lockParams.put("roomUuid", roomUuid);
		lockParams.put("oldPwd", oldPwd);
		lockParams.put("newPwd", newPwd);
		String lockServiceUrl = String.format(SMART_LOCK_API, configuration.smartServiceHost,
				"/v3002/pwd/customer/modify");
		String postRet = null;
		try {
			postRet = HttpUtil.post(lockServiceUrl, lockParams);
		} catch (Exception e) {
			throw new ServiceInvokeFailException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, "服务暂时不可用");
		}
		if (postRet.contains("\"result\":null")) {
			postRet = postRet.replace("\"result\":null", "\"result\":{}");
		}
		ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
		
		int code = 0;
		String repMsg = GwModityPwdErrorCodeEnum.getDesc(result.status.code);
		if (Integer.parseInt(result.status.code) != 0) {
			code = 1;
			repMsg = GwModityPwdErrorCodeEnum.getDesc(result.status.code);
			if (StringUtils.isEmpty(repMsg)) {
				repMsg = result.status.description;
			}
			logger.error("网关锁修改密码失败 code={}, desc={}", result.status.code, result.status.description);

		}
		responses.getMeta().setErrorCode(code);
		responses.getMeta().setErrorMessage(repMsg);
		ImmutableMap<String, String> map = ImmutableMap.of("result", repMsg);
		responses.setBody(map);
		return responses;
	}
	
}
