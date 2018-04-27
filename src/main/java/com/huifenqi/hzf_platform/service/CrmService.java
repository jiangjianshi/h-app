/** 
 * Project Name: usercomm_project 
 * File Name: CrmService.java 
 * Package Name: com.huifenqi.usercomm.service 
 * Date: 2016年1月20日上午10:21:02 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.ContractDeletedException;
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
 * ClassName: CrmService date: 2016年1月20日 上午10:21:02 Description: CRM对接服务
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class CrmService {

	private static final String CRM_API = "http://%s/%s";

	private static Log logger = LogFactory.getLog(CrmService.class);

	private static Gson gson = GsonUtils.buildGson();

	@Autowired
	private Configuration configuration;

	/**
	 * 提交用户信息
	 * 
	 * @param userInfo
	 * @return
	 */
	public JsonObject commitUserInfo(JsonObject userInfo) throws Exception {
		String url = String.format(CRM_API, configuration.crmApiHost, "api/contract/v2/quick_commit/");
		Map<String, String> params = new HashMap<>();
		params.put("user_id", userInfo.get("user_id").getAsString());
		params.put("user_mobile", userInfo.get("phone").getAsString());
		params.put("user_photo_pos", userInfo.get("id_posi_name").getAsString());
		params.put("user_photo_neg", userInfo.get("id_neg_name").getAsString());
		params.put("user_photo_face", userInfo.get("face_pic_name").getAsString());
		params.put("user_name", userInfo.get("name").getAsString());
		params.put("user_id_no", userInfo.get("user_id_no").getAsString());
		params.put("user_linkman_name", userInfo.get("linkman_name").getAsString());
		params.put("user_linkman_mobile", userInfo.get("linkman_phone").getAsString());
		params.put("user_linkman_relation", userInfo.get("linkman_relation").getAsString());
		params.put("user_qq", userInfo.get("qq_no").getAsString());
		params.put("user_company_name", userInfo.get("co_name").getAsString());
		params.put("user_company_address", userInfo.get("co_address").getAsString());
		params.put("user_company_mail", userInfo.get("co_mail").getAsString());
		params.put("user_company_card", userInfo.get("co_posi_name").getAsString());
		params.put("user_income", userInfo.get("revenue_info").getAsString());
		params.put("user_job", userInfo.get("position").getAsString());
		params.put("user_status", userInfo.get("co_posi_status").getAsString());
		params.put("user_card_no", userInfo.get("bank_card_no").getAsString());
		params.put("user_card_mobile", userInfo.get("bank_bind_phone").getAsString());
		params.put("contract_no", userInfo.get("contract_no").getAsString());
		params.put("user_bank", userInfo.get("bank_name").getAsString());

		return request(url, params);
	}

	/**
	 * 租客确认合同信息修改
	 * 
	 * @param contractNo
	 * @return
	 */
	public JsonObject confirmContractStatus(String contractNo) throws Exception {
		String url = String.format(CRM_API, configuration.crmApiHost, "api/contract/v2/user_confirm/");
		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);

		return request(url, params);
	}

	public JsonObject launchsublet (Map<String, String> params) throws Exception{
		String url = String.format(CRM_API, configuration.crmApiHost, "/api/contract/launch_sublet/");
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
			if ("00320013".equals(statusJo.getAsJsonPrimitive("code").getAsString())) {
				throw new ContractDeletedException(statusJo.getAsJsonPrimitive("code").getAsInt() , "该合同已经被删除,请联系经纪人员");
			}
			throw new ServiceInvokeFailException(statusJo.getAsJsonPrimitive("code").getAsInt(), errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}
}
