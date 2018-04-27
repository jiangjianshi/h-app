/** 
 * Project Name: fileservice_project 
 * File Name: FileInterceptor.java 
 * Package Name: com.huifenqi.file 
 * Date: 2015年12月30日下午2:13:24 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform;

import com.huifenqi.hzf_platform.comm.Request;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.LackParameterException;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.context.response.ResponseMeta;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.security.AuthorizationManager;
import com.huifenqi.hzf_platform.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: PlatformInterceptor date: 2017年8月15日 下午2:13:24 Description: 拦截器
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
@Component
public class PlatformInterceptor implements HandlerInterceptor {

	private static final Log logger = LogFactory.getLog(PlatformInterceptor.class);

	@Autowired
	private AuthorizationManager authorizationManager;


	@Autowired
	private SessionManager sessionManager;
	/**
	 * 在此白名单内的接口需要受到session有效性的制约
	 */
	private static final List<String> IGNORE_SESSION_INTERFACE_WHITELIST = new ArrayList<>();

	static {
		//用户注册相关接口
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/house/addFootmarkHistory");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/house/deleteFootmarkHistory");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/house/getFootmarkHistoryList");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/house/getCollectHouses");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/house/updateCollectHouseState");
		//合同、支付相关接口
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/list");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/listsubpay");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/charge");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/confirmyeepaycharge");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/channelincome/calc");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/getpaychannels");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/pay/api/reqChargeStatus");

		IGNORE_SESSION_INTERFACE_WHITELIST.add("/voucher/api/list");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/voucher/api/canuse");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/voucher/api/existnew");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/voucher/api/queryNums");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/coupon/api/list");
		
		// 在线缴租相关接口
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/reqbankcardlist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/reqbankcardinfo/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/bindbankcardreq/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/bindbankcard/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/listbankcard/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/bill/reqpaylist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v2/bill/reqpaylist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/bill/reqcontractpaylist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/bill/reqsubpaydetail/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/bill/createorder/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/bill/cancelorder/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/reqpaymentchannellist/");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/v2/pay/reqpaymentchannellist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/reqchannelfee/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/reqpaymentstatus/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/charge/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/confirmcharge/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/captcha/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/contract/reqrentcontractlist/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/contract/reqrentcontractdetail/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/contract/confirmcontract/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/autowithhold/");
        IGNORE_SESSION_INTERFACE_WHITELIST.add("/v1/pay/updatewithholdstatus/");

		//智能门锁功能
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/updategesturepwd");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/getgesturepwd");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/checkuser");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/doorlocklist");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/doorlockpwd");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/doorunlockrecord");
		IGNORE_SESSION_INTERFACE_WHITELIST.add("/doorlock/api/checkpwd");
	}

	private static final List<String> NO_CHECK_WHITELIST = new ArrayList<>();

	static {
		NO_CHECK_WHITELIST.add("/wx/auth/");
		NO_CHECK_WHITELIST.add("/wx/wxindexredirect/");
		NO_CHECK_WHITELIST.add("/reqwxjsdkauth/");
		NO_CHECK_WHITELIST.add("/wx/api/reqaccesstoken/");
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setCharacterEncoding("utf-8");
	    response.setContentType("text/html;charset=utf-8");
		// TODO 在这里将access日志格式化输出，为的是能够全面掌控访问日志的格式
		logger.debug("Cache the request, thread id=" + Thread.currentThread().getId());
		Request.initRequest(request);
		//获取URL,判断是否需要鉴权,如果鉴权失败则直接返回
		Request cRequest = Request.getRequest();
		String queryId = ResponseUtils.getQueryId();
		// 开始时间
		Date startDate = new Date();
		String interfaceName = request.getRequestURI();
		logger.debug("add by arison interface name : "+interfaceName);
		if(NO_CHECK_WHITELIST.contains(interfaceName)){
			return true;
		}

		Map<String,String> paramMap = null;
		//模拟百度接口鉴权特殊处理
		if(InterfaceUrl.isMember(interfaceName)){
			try{
				paramMap = BdRequestUtils.handlerDataToMap(request);
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("解析data数据失败:%s", e.getMessage())));
				BdResponses bdResponses = new BdResponses(ErrorMsgCode.ERROR_MSG_UPDATE_PROG_EXCEPION, "请求data数据格式错误");
				ResponseUtils.writer(response, ResponseUtils.getResponseContent(request, bdResponses));
				return false;
			}
			try {
				if (!authorizationManager.checkBaiduBaseSign(request,paramMap)) {
					logger.error(LogUtils.getCommLog(String.format("请求%s鉴权失败", request.getRequestURL())));
					BdResponses bdResponses = new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_SIGN_VERIFY_FAIL, "机构签名校验失败");
					ResponseUtils.writer(response, ResponseUtils.getResponseContent(request, bdResponses));
					return false;
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("鉴权失败:%s", e.getMessage())));
				BdResponses bdResponses = new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_SIGN_VERIFY_FAIL, "用户未登录");
				ResponseUtils.writer(response, ResponseUtils.getResponseContent(request, bdResponses));
				return false;
			}
			return true;
		}else{
          // 对接口进行验证
			try {
				if (!authorizationManager.checkSign(request)) {
					logger.error(LogUtils.getCommLog(String.format("请求%s鉴权失败", request.getRequestURL())));
					throw new BaseException(ErrorMsgCode.ERROR_MSG_API_SIGN_AUTH_FAIL, String.format("接口签名校验失败"));
				}
				// 白名单内的接口需要做登录校验
				if ( IGNORE_SESSION_INTERFACE_WHITELIST.contains(interfaceName)) {
					boolean validFlag = isSessionValid(cRequest);
					if (!validFlag) {
						logger.error(LogUtils.getCommLog(String.format("用户未登录", request.getRequestURL())));
						throw new BaseException(ErrorMsgCode.ERRCODE_NEED_LOGIN, "用户未登录");
					}
				}
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("预处理失败:%s", e.getMessage())));

				int errorCode = ErrorMsgCode.ERROR_MSG_UNKNOWN;
				String description = null;

				if (BaseException.class.isAssignableFrom(e.getClass())) {
					BaseException baseException = (BaseException) e;
					errorCode = baseException.getErrorcode();
					description = baseException.getDescription();
				} else {
					errorCode = ErrorMsgCode.ERROR_MSG_API_SIGN_AUTH_FAIL;
					description = "非法操作";
				}

				Responses responses = new Responses(errorCode, description);

				ResponseMeta meta = responses.getMeta();

				// 设置查询id
				ResponseUtils.fillQueryId(meta, queryId);

				// 设置主要元数据
				ResponseUtils.fillMainMeta(meta, request);

				// 设置接收请求时间
				ResponseUtils.fillDate(meta, startDate);

				// 获取处理时间
				ResponseUtils.getAndFillCostTime(meta, startDate);

				ResponseUtils.writer(response, ResponseUtils.getResponseContent(request, responses));
				return false;
			}
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("remove the request, thread id=" + Thread.currentThread().getId());
		Request.destroyRequest();
	}


	/**
	 * 如果是app，不对session校验
	 *
	 * @return
	 */
	public boolean isSessionValid(Request request) {
		try {
			String sessionId = request.getSessionId();
			long userId = sessionManager.getUserId(sessionId);
			if (0 == userId) {
				logger.info(LogUtil.formatLog("session timeout, id=" + sessionId));
			}
			return 0 != userId;
		} catch (LackParameterException e) {
			logger.info(LogUtil.formatLog(e.getDescription()));
		}
		return false;
	}


}
