/** 
 * Project Name: usercomm_project 
 * File Name: MqService.java 
 * Package Name: com.huifenqi.usercomm.service 
 * Date: 2015年12月9日下午6:29:27 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huifenqi.hzf_platform.context.dto.params.PushMessageDto;
import com.huifenqi.hzf_platform.context.dto.params.PushRecordDto;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;

/**
 * ClassName: MqService date: 2015年12月9日 下午6:29:27 Description: MQ系统服务
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class MqService {

	protected static Logger logger = LoggerFactory.getLogger(MqService.class);

	private static final String MQ_URL = "http://%s:%s/sendtemplatemsg/";
	private static final String MQ_URL_GETMSG = "http://%s:%s/api/list/getmsg/";
	private static Gson gson = GsonUtil.buildGson();

	@Autowired
	private Configuration configuration;

	/**
	 * 发送申请成功模板消息
	 * 
	 * @param userPhone
	 */
	public void sendApplySuccessMsg(String userPhone) {
		sendApplyMsg(configuration.mqSuccessTemplateId, userPhone, null);
	}

	/**
	 * 发送申请不支持模板消息
	 * 
	 * @param userPhone
	 */
	public void sendApplyNotSupportMsg(String userPhone) {
		sendApplyMsg(configuration.mqFailTemplateId, userPhone, null);
	}

	/**
	 * 发送申请状态
	 * 
	 * @param uid
	 *            用户ID
	 * @param msgText
	 *            消息内容
	 */
	public void sendApplyMsg(String templateId, String userPhone, Map<String, String> properties) {

		String url = String.format(MQ_URL, configuration.mqHost, configuration.mqPost);

		MqTemplateMsg msg = new MqTemplateMsg();

		msg.setTemplateId(templateId);
		msg.addUser(userPhone);
		msg.setUserType(MqTemplateMsg.USER_TYPE_USER);

		if (properties != null) {
			msg.setProperties(properties);
		}

		Map<String, String> params = new HashMap<>();
		params.put("msg", GsonUtil.buildGson().toJson(msg));

		try {
			HttpUtil.post(url, params);
		} catch (Exception e) {
			logger.error("Send Apply msg failed, templateId=" + templateId + ", userPhone=" + userPhone + "):"
					+ e.getMessage());
		}
	}

	class MqTemplateMsg {

		// 用户类型：租户（客户端用户）
		public static final int USER_TYPE_USER = 0;

		// 用户类型：经纪人
		public static final int USER_TYPE_AGENT = 1;

		// 模板Id
		private String templateId;

		// 推送用户列表
		private List<String> toUsers = new ArrayList<String>();

		// 用户类型
		private int userType;

		// 属性Map
		private Map<String, String> properties = new HashMap<String, String>();

		public String getTemplateId() {
			return templateId;
		}

		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}

		public List<String> getToUsers() {
			return toUsers;
		}

		public void setToUsers(List<String> toUsers) {
			this.toUsers = toUsers;
		}

		public int getUserType() {
			return userType;
		}

		public void setUserType(int userType) {
			this.userType = userType;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		public void addUser(String userPhone) {
			toUsers.add(userPhone);
		}

		public void putProperty(String key, String value) {
			properties.put(key, value);
		}

	}

	public JsonObject getMsg(Map<String, String> params) {
		String url = String.format(MQ_URL_GETMSG, configuration.mqHost, configuration.mqPost);
		JsonObject resJsonObj = request(url, params);
		if (resJsonObj.isJsonArray()) {
			JsonArray records = resJsonObj.getAsJsonArray("records");
			for (int i = 0; i < records.size(); i++) {
				JsonObject record = (JsonObject) records.get(i);
				String msgContent = record.getAsJsonPrimitive("msg_content").getAsString();

				String msgParameters = record.getAsJsonPrimitive("msg_parameters").getAsString();
				if (!"null".equals(msgParameters)) {
					JsonObject parameters = (JsonObject) new JsonParser().parse(msgParameters);
					if (parameters != null) {
						JsonObject properties = (JsonObject) parameters.get("parameters");
						if (properties != null) {
							Set<Entry<String, JsonElement>> props = properties.entrySet();
							if (props != null) {
								for (Entry<String, JsonElement> entry : props) {
									msgContent.replace("{" + entry.getKey() + "}", entry.getValue().getAsString());
								}
							}
						}
					}
				}
			}
		}
		return resJsonObj;
	}

	public List<PushRecordDto> getPushMsg(Map<String, String> params) {
		String url = String.format(MQ_URL_GETMSG, configuration.mqHost, configuration.mqPost);
		JsonObject resJsonObj = request(url, params);
		
//		PushMessageDto msgDto = new PushMessageDto();
//		msgDto.setPageNum(resJsonObj.get("current_page_num").getAsInt());
//		msgDto.setPageSize(resJsonObj.get("current_page_elements").getAsInt());
//		msgDto.setTotalPages(resJsonObj.get("total_pages").getAsInt());
//		msgDto.setTotalElements(resJsonObj.get("total_elements").getAsInt());
		
		List<PushRecordDto> recordlist = Lists.newArrayList();
		if (resJsonObj.get("records").isJsonArray()) {
			JsonArray records = resJsonObj.getAsJsonArray("records");
			for (int i = 0; i < records.size(); i++) {
				PushRecordDto pushRecordDto = new PushRecordDto();

				JsonObject record = (JsonObject) records.get(i);
				String msgTitle = record.getAsJsonPrimitive("msg_title").getAsString();
				String msgContent = record.getAsJsonPrimitive("msg_content").getAsString();
				String createTime = record.getAsJsonPrimitive("create_time").getAsString();
				pushRecordDto.setMsgTitle(msgTitle);
				pushRecordDto.setMsgContent(msgContent);
				pushRecordDto.setCreateTime(createTime);
				recordlist.add(pushRecordDto);
			}
		}
//		msgDto.setRecords(recordlist);
		return recordlist;
	}

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
			throw new ServiceInvokeFailException(errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}
}
