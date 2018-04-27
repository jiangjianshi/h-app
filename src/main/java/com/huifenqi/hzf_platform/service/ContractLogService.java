/** 
* Project Name: trunk 
* File Name: RiskControlService.java 
* Package Name: com.huifenqi.usercomm.service 
* Date: 2016年4月12日下午2:41:25 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RiskControlService date: 2016年4月12日 下午2:41:25 Description: 风控服务
 * 
 * @author arison
 * @version
 * @since JK 1.8
 */
@Component
public class ContractLogService {

	private static final String REFUND_API_HOST = "http://%s/%s";

	private static Log logger = LogFactory.getLog(ContractLogService.class);

	private static Gson gson = GsonUtils.buildGson();

	@Autowired
	private Configuration configuration;

	/**
	 * 合同日志写
	 *
	 * @return
	 */
	public JsonObject contractLogWrite(String contractNo,String userId,String remark,int type) throws Exception {
		String url = String.format(REFUND_API_HOST, configuration.contractLogServiceHost, "contract/contract_log/contract_log/");
		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);
		String extender="";
		if(type==0) {
			params.put("oper_type", "2010001" );//发起违约
		}else{
			params.put("oper_type","2010007");//修改违约备注
		}
		//extender="{'breach_initiator':2,'breach_initiator_name':'租客'}";
		extender="{\"breach_initiator\":2,\"breach_initiator_name\":\"租客\"}";
		params.put("oper_id", userId);
		params.put("extender", extender);
		params.put("remark", remark);

		return postRequest(url, params);
	}

	/**
	 * 合同日志读
	 * @return
	 */
	public JsonObject  contractLogRead(String contractNo,String operType) throws Exception {
		String url = String.format(REFUND_API_HOST, configuration.contractLogServiceHost, "contract/contract_log/contract_log/");
		/*
		2010001: 发起违约
		2010007: 修改违约备注
		2010002: 中介确认违约
		2010006: 财务确认违约平账
		2010012: 违约代扣返回
		中介财务确认违约
		* */

		//String operType="2010004";
		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);
		params.put("oper_type", operType);
		return getRequest(url, params);
	}

	/**
	 * 发起post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private JsonObject postRequest(String url, Map<String, String> params) {
		String response = null;
		try {
			response = HttpUtil.post(url, params);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}

		JsonObject serviceRespJo = gson.fromJson(response, JsonObject.class);

		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		int code = statusJo.getAsJsonPrimitive("code").getAsInt();
		if (0 != code) {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			throw new ServiceInvokeFailException(statusJo.getAsJsonPrimitive("code").getAsInt(), errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}

	/**
	 * 发起get请求
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	private JsonObject getRequest(String url, Map<String, String> params) {
		String response = null;
		try {
			response = HttpUtil.get(url, params);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}

		JsonObject serviceRespJo = gson.fromJson(response, JsonObject.class);

		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		int code = statusJo.getAsJsonPrimitive("code").getAsInt();
		if (0 != code) {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			throw new ServiceInvokeFailException(statusJo.getAsJsonPrimitive("code").getAsInt(), errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}
}
