/** 
 * Project Name: hzf_platform_project 
 * File Name: HandlerMonitor.java 
 * Package Name: com.huifenqi.hzf_platform.monitor 
 * Date: 2016年5月10日下午4:31:41 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.monitor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.huifenqi.hzf_platform.comm.Request;
import com.huifenqi.hzf_platform.utils.AccessLogUtils;
import com.huifenqi.hzf_platform.utils.LogUtil;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.ResponseMeta;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.ResponseUtils;

/**
 * ClassName: HandlerMonitor date: 2016年5月10日 下午4:31:41 Description:
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Aspect
@Component
public class HandlerMonitor {

	private static Log logger = LogFactory.getLog(HandlerMonitor.class);

	@Around("execution(public com.huifenqi.hzf_platform.context.response.Responses com.huifenqi.hzf_platform.controller..*.*(..))")
	public Responses processHandler(ProceedingJoinPoint pjp) {

		Responses response = null;

		String queryId = ResponseUtils.getQueryId();

		logger.info(LogUtils.getCommLog(
				String.format("The process method: %s, queryId: %s", pjp.getSignature().getName(), queryId)));

		// 开始时间
		Date startDate = new Date();

		HttpServletRequest request = null;
		Request cRequest = null;
		try {
			Object[] args = pjp.getArgs();
			if (args != null && args.length > 0) {
				for (Object arg : args) {
					if (arg instanceof HttpServletRequest) {
						request = (HttpServletRequest) arg;
						break;
					}
				}
			}
			if (request == null) {
				throw new Exception("该方法没有HttpServletRequest参数.");
			}
			//add by arison 请求参数日志打印
			cRequest=Request.getRequest();
			String platform = RequestUtils.getParameterString(request, "platform", "");
			String clientIP = request.getHeader("X-Real-IP");
			logger.info(LogUtil.formatLog(String.format("[REQUEST]%s %s %s client_ip=%s", platform,
					cRequest.getUrl(), cRequest.getRequest().getHttpParams(), clientIP)));
			response = (Responses) pjp.proceed();
		} catch (Exception e) {

			response = new Responses();

			ResponseMeta meta = response.getMeta();

			// 判断是否为BaseException异常及其子异常
			if (BaseException.class.isAssignableFrom(e.getClass())) {
				BaseException baseException = (BaseException) e;
				meta.setErrorCode(baseException.getErrorcode());
				meta.setErrorMessage(baseException.getDescription());
				logger.error(LogUtils.getCommLog(baseException.toString()));
			} else {
				meta.setErrorCode(ErrorMsgCode.ERROR_MSG_UNKNOWN);
				meta.setErrorMessage("数据加载失败，请稍后重试");
				logger.error(LogUtils.getCommLog(e.toString()), e);
			}
		} catch (Throwable e) {
			response = new Responses();
			ResponseMeta meta = response.getMeta();
			meta.setErrorCode(ErrorMsgCode.ERROR_MSG_UNKNOWN);
			meta.setErrorMessage("数据加载失败，请稍后重试");
			logger.error(LogUtils.getCommLog(e.toString()), e);
		}

		ResponseMeta meta = response.getMeta();

		// 设置查询id
		ResponseUtils.fillQueryId(meta, queryId);

		// 设置主要元数据
		ResponseUtils.fillMainMeta(meta, request);

		// 设置接收请求时间
		ResponseUtils.fillDate(meta, startDate);

		// 获取处理时间
		int costTime = ResponseUtils.getAndFillCostTime(meta, startDate);

		//add by arison 日志打印
		logger.info(LogUtils.getResponseLog(costTime, ResponseUtils.getResponseContent(request, response),
				request.getRequestURL().toString()));
		
		try{
          //输出访问日志
            AccessLogUtils.printAccessLog(request, response); 
        }catch(Throwable e){
            logger.error(LogUtils.getCommLog("保存AccessLog成功日志失败" + e.getMessage()));
            e.printStackTrace();
        }
		
		return response;
	}
}
