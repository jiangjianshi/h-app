
package com.huifenqi.hzf_platform.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/** 
 * ClassName: SmartService
 * date: 2018年1月16日 下午3:40:57
 * Description: 
 * 
 * @author Arison
 * @version  
 * @since JDK 1.8 
 */

@Service
public class SmartService {
	
	private static final String SMART_API = "http://%s/%s";

	private static Log logger = LogFactory.getLog(SmartService.class);

	private static Gson gson = GsonUtils.buildGson();

	@Autowired
	private Configuration configuration;
	
	/**
	 * 查询用户的电表列表
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public JsonObject reqMeterList(long userId, int pageNum, int pageSize) {
		Map<String, String> param = new HashMap<>();
		param.put("userId", userId + "");
		param.put("pageNum", pageNum + "");
		param.put("pageSize", pageSize + "");
		
		//String url = String.format(SMART_API, configuration.smartServiceHost, "ammeter/ammeter/list");
		String url = String.format(SMART_API, configuration.smartServiceHost, "v2/ammeter/customer/list");

		JsonObject retObj = request(url, param);
		return retObj;
	}
	
	/**
	 * 获取电表详情
	 * @param meterId
	 * @return
	 */
	public JsonObject reqMetarDetail(long meterId) {
		Map<String, String> param = new HashMap<>();
		param.put("id", meterId + "");
		
		//String url = String.format(SMART_API, configuration.smartServiceHost, "ammeter/ammeter/detail");
		//update by arison 20170509
		String url = String.format(SMART_API, configuration.smartServiceHost, "v2/ammeter/ammeter/detail");
		JsonObject retObj = request(url, param);
		return retObj;
	}

	/**
	 * 获取电量明细
	 * @param roomUuid
	 * @param ammeterSn
	 * @param time
	 * @return
	 */
	public JsonObject reqChargeDetail(String userId,String roomUuid,String ammeterSn,String time) {
		Map<String, String> param = new HashMap<>();
		param.put("userId", userId);
		param.put("roomUuid", roomUuid);
		param.put("ammeterSn", ammeterSn);
		param.put("time", time);
		String url = String.format(SMART_API, configuration.smartServiceHost, "v2/ammeter/power/details");
		JsonObject retObj = request(url, param);
		return retObj;
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
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务暂时不可用");
		}

		JsonObject serviceRespJo = gson.fromJson(response, JsonObject.class);

		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		String code = statusJo.getAsJsonPrimitive("code").getAsString();
		if (!"0".equals(code)) {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			throw new BaseException(ErrorMsgCode.ERRCODE_ILLEGAL_INVOKE, errMsg);
		}
		return serviceRespJo.getAsJsonObject("result");
	}


	/**
	 * 查询租客公摊记录列表
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public JsonObject reqPublicFeeList(long userId, int pageNum, int pageSize) {
		Map<String, String> param = new HashMap<>();
		param.put("userId", userId + "");
		param.put("pageNum", pageNum + "");
		param.put("pageSize", pageSize + "");
		//String url = String.format(SMART_API, configuration.smartServiceHost, "ammeter/ammeter/list");
		String url = String.format(SMART_API, configuration.smartServiceHost, "v2/house/public/listpublicroom");
		JsonObject retObj = request(url, param);
		return retObj;
	}
}
