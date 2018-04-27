package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.DoorLockRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by arison on 2017/12/18.
 * 
 * 智能门锁相关
 * 
 */
@RestController
public class DoorLockController {

	@Autowired
    private DoorLockRequestHandler doorLockRequestHandler;
	/**
	 * @Title: listPayStatus
	 * @Description: 保存/更新用户手势密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月19日 下午16:58:35
	 */
	@RequestMapping(value = "/doorlock/api/updategesturepwd", method = RequestMethod.POST)
	public Responses updateGesturePwd(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.updateGesturePwd();
	}

	/**
	 * @Title: getGesturePwd
	 * @Description: 获取手势密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 下午5:25:56
	 */
	@RequestMapping(value = "/doorlock/api/getgesturepwd", method = RequestMethod.POST)
	public Responses getGesturePwd(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.getGesturePwd();
	}
	
	/**
	 * @Title: checkGesturePwd
	 * @Description: 验证密码是否正确
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 上午10:17:54
	 */
	@RequestMapping(value = "/doorlock/api/checkpwd", method = RequestMethod.POST)
	public Responses checkPwd(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.checkPwd();
	}
	
	/**
	 * @Title: userPwdLogin
	 * @Description: 验证输入手机号是否对应当前用户
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 下午3:32:06
	 */
	@RequestMapping(value = "/doorlock/api/checkuser", method = RequestMethod.POST)
	public Responses checkUser(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.checkUser();
	}
	
	/**
	 * @Title: getDoorLockList
	 * @Description: 获取智能门锁列表
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 下午4:32:25
	 */
	@RequestMapping(value = "/doorlock/api/doorlocklist", method = RequestMethod.POST)
	public Responses getDoorLockList(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.getDoorLockList();
	}
	
	/**
	 * @Title: getDoorLockPwd
	 * @Description: 获取蓝牙锁密码
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 下午2:10:04
	 */
	@RequestMapping(value = "/doorlock/api/doorlockpwd", method = RequestMethod.POST)
	public Responses getDoorLockPwd(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.getDoorLockPwd();
	}
	
	/**
	 * @Title: getDoorUnlockRecord
	 * @Description: 获取开锁记录
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年12月18日 下午2:54:03
	 */
	@RequestMapping(value = "/doorlock/api/doorunlockrecord", method = RequestMethod.POST)
	public Responses getDoorUnlockRecord(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.getDoorUnlockRecord();
	}
	
	/**
	 * @Title: getDynamicPassword
	 * @Description: 获取临时密码
	 * @return Responses
	 * @author
	 * @dateTime 2018年03月13日 上午11:18:24
	 */
	@RequestMapping(value = "/doorlock/api/getdynamicpwd", method = RequestMethod.POST)
	public Responses getDynamicPassword(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.getDynamicPassword();
	}
		
	/**
	 * 网关锁修改密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doorlock/api/modifyPwd", method = RequestMethod.POST)
	public Responses gatewayLockModifyPwd(HttpServletRequest request, HttpServletResponse response) {
		return doorLockRequestHandler.modifyPwd();
	}
	
}
