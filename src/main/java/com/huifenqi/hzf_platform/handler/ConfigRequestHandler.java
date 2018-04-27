package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.RequestUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * Created by YDM on 2017/11/3.
 */
@Service
public class ConfigRequestHandler extends BaseRequestHandler {

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private Configuration configuration;
	
	/**
	 * @Title: getGlobalConfig
	 * @Description: 获取全局配置信息
	 * @return Responses
	 * @author 叶东明
	 * @dateTime 2017年11月3日 下午2:39:16
	 */
	public Responses getGlobalConfig(HttpServletRequest request) throws Exception {
		long cityId = RequestUtils.getParameterLong(request, "cityId");
		JSONObject retMap = new JSONObject();
		// 品牌公寓相关配置
		JSONObject apartmentJson = new JSONObject();
		String apartmentShowCityIds = configuration.apartmentShowCityIds;
		List<Long> cityList = new ArrayList<Long>();
		if (StringUtil.isNotBlank(apartmentShowCityIds)) {
			String[] apartmentShowCityIdArray = apartmentShowCityIds.split(",");
			for (int i = 0; i < apartmentShowCityIdArray.length; i++) {
				cityList.add(Long.parseLong(apartmentShowCityIdArray[i]));
			}
		}
		if (cityList.contains(cityId)) {
			apartmentJson.put("apartmentShowFlag", Constants.ShowApartmentFlag.SHOW_APARTMENT);
		} else {
			apartmentJson.put("apartmentShowFlag", Constants.ShowApartmentFlag.HIDDEN_APARTMENT);
		}
		retMap.put("apartmentData", apartmentJson);
		// 大礼包活动相关配置
		JSONObject giftJson = new JSONObject();
		int activityShowFlag = Integer.parseInt(configuration.activityShowFlag);
		String destinationUrl = configuration.destinationUrl;
		String giftImgUrl = configuration.giftImgUrl;
		String showImgUrl = configuration.showImgUrl;
		giftJson.put("activityShowFlag", activityShowFlag);
		giftJson.put("destinationUrl", destinationUrl);
		giftJson.put("giftImgUrl", giftImgUrl);
		giftJson.put("showImgUrl", showImgUrl);
		giftJson.put("title", "会找房I领取大礼包");
		retMap.put("giftData", giftJson);
		
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retMap);
		return responses;
	}
	
}
