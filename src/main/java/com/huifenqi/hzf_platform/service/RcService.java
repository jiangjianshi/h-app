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
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.usercomm.domain.contract.RcInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RiskControlService date: 2016年4月12日 下午2:41:25 Description: 风控服务
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Component
public class RcService {

	private static final String RC_API_HOST = "http://%s/%s";

	private static Log logger = LogFactory.getLog(RcService.class);

	private static Gson gson = GsonUtil.buildGson();

	@Autowired
	private Configuration configuration;

	/**
	 * 上传风控信息
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject reportRcInfo(RcInfo rcInfo) throws Exception {
		return reportRcInfo(rcInfo.getUserPhone(), rcInfo.getUserIdNo(), rcInfo.getRcToken());

	}

	/**
	 * 上传风控信息
	 * 
	 * @param params
	 * @return
	 */
	public JsonObject reportRcInfo(String phone, String id_card_no, String rcToken) throws Exception {
		// TODO 补充接口地址
		String url = String.format(RC_API_HOST, configuration.rcApiHost, "up/credit_apply/");
		Map<String, String> params = new HashMap<>();
		params.put("mobile", phone);
		params.put("credit_id", id_card_no);
		params.put("tongdun_token", rcToken);

		return request(url, params);
	}

	/**
	 * 发起请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private JsonObject request(String url, Map<String, String> params) {
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

}
