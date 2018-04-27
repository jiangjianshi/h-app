package com.huifenqi.hzf_platform.comm;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arison on 2015/9/4.
 * <p>
 * 在所有Controller中使用Handler的时候，应该调用handle方法，传入需要调用的方法名，而不应该直接调用 对应的方法
 */
@Service
public abstract class BaseRequestHandler {

	protected static Logger logger = LoggerFactory.getLogger(BaseRequestHandler.class);

	protected Gson gson = GsonUtil.buildCommGson();

	protected static final String HTTP_404_PAGE = "http://%s/http_status/404.html";

	/**
	 * 平台服务接口
	 */
	private static final List<String> PLATFORM_INNER_INTERFACE_WHITELIST = new ArrayList<>();
	static {
		PLATFORM_INNER_INTERFACE_WHITELIST.add("/order/api/reqorderdetail/");
	}
	

	public BaseRequestHandler() {
		logger.debug("this constructor will be execute when the tomcat server start");
	}

	/**
	 * 获取参数，默认必传
	 *
	 * @param name
	 * @return
	 */
	protected String getStringParam(String name) {
		return getRequest().getStringParam(name);
	}

	protected String getStringParam(String name, String displayName) {
		return getRequest().getStringParam(name, displayName);
	}

	protected String getDefaultStringParam(String name, String defaultValue) {
		return getRequest().getDefaultStringParam(name, defaultValue);
	}

	protected long getLongParam(String name) {
		return getRequest().getLongParam(name);
	}

	protected long getLongParam(String name, String displayName) {
		return getRequest().getLongParam(name, displayName);
	}

	protected long getDefaultLongParam(String name, long defaultValue) {
		return getRequest().getDefaultLongParam(name, defaultValue);
	}

	protected int getIntParam(String name) {
		return getRequest().getIntParam(name);
	}

	protected int getIntParam(String name, String displayName) {
		return getRequest().getIntParam(name, displayName);
	}

	protected int getDefaultIntParam(String name, int defaultValue) {
		return getRequest().getDefaultIntParam(name, defaultValue);
	}

	protected Request getRequest() {
		return Request.getRequest();
	}

	protected String getPlatform() {
		return getRequest().getPlatform();
	}

	protected String getClientIp() {
		HttpServletRequest request = getRequest().getHttpServletRequest();
		String clientIp = request.getHeader("X-Real-IP");

		if (StringUtil.isEmpty(clientIp)) {
			clientIp = request.getHeader("Remote Address");
		}

		if (StringUtil.isEmpty(clientIp)) {
			clientIp = request.getHeader("host");
		}

		return clientIp;
	}

	protected int getClientVersion() {
		return getRequest().getClientVersion();
	}

	protected String getSessionId() {
		return getRequest().getSessionId();
	}


	public ApiResult processServiceResponse(JsonObject serviceRetJo) {
		JsonObject statusJo = serviceRetJo.getAsJsonObject("status");

		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(statusJo.getAsJsonPrimitive("code").getAsString())) {
			return new ApiResult(serviceRetJo.getAsJsonObject("result"));
		} else {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			return new ApiResult(String.valueOf(ErrorMsgCode.ERRCODE_SERVICE_ERROR), errMsg);
		}

	}

}
