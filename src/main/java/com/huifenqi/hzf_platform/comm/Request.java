package com.huifenqi.hzf_platform.comm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.LackParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;
import com.huifenqi.hzf_platform.utils.CookieUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * Created by arison on 2015/9/4.
 */
public class Request {

	private static Logger logger = LoggerFactory.getLogger(Request.class);

	/**
	 * 通过该全局map，将request对象与处理本次请求的线程关联，在任请求处理的任何一个位置均可使用
	 */
	private static final Map<Long, Request> THREAD_REQUEST_MAP = new ConcurrentHashMap<>();

	private String url;

	private Map<String, String> httpParams = new HashMap<>();

	/**
	 * 访问server的客户端ip
	 */
	private String ip;

	/**
	 * 当前线程id
	 */
	private String id;

	private String interfaceName;

	private HttpServletRequest httpServletRequest;

	public Request(HttpServletRequest httpServletRequest) {
		// 初始化一些request必要的参数
		this.httpServletRequest = httpServletRequest;
		this.url = httpServletRequest.getRequestURL().toString();
		this.ip = httpServletRequest.getRemoteAddr();
		this.id = Thread.currentThread().getId() + "";

		int endIndex = url.lastIndexOf("/");
		int startIndex = url.lastIndexOf("/", endIndex - 1) + 1;
		interfaceName = url.substring(startIndex, endIndex);

		// 从HtpServletRequest中解析原始参数
		Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String value = "";
			if (entry.getValue() != null && entry.getValue().length > 0) {
				value = entry.getValue()[0];
			}

			// 参数放入的时候对两端的空格做处理
			httpParams.put(entry.getKey(), StringUtil.trim(value));
		}
	}

	/**
	 * 在过滤器中将HttpServletRequet传入到Request对象中
	 *
	 * @param httpServletRequest
	 */
	public static void initRequest(HttpServletRequest httpServletRequest) {
		THREAD_REQUEST_MAP.put(Thread.currentThread().getId(), new Request(httpServletRequest));
	}

	/**
	 * 本次请求有效期内，可通过该方法随时获取到Request对象
	 * 
	 * @return
	 */
	public static Request getRequest() {
		return THREAD_REQUEST_MAP.get(Thread.currentThread().getId());
	}

	/**
	 * 在请求结束之前销毁Request对象
	 */
	public static void destroyRequest() {
		Request request = THREAD_REQUEST_MAP.remove(Thread.currentThread().getId());
		request = null;
	}

	// ----- 获取公共参数

	/**
	 * 获取String参数
	 *
	 * @param name
	 * @return
	 */
	public String getStringParam(String name) {
		return getStringParam(name, null, name, true);
	}

	public String getStringParam(String name, String displayName) {
		return getStringParam(name, null, displayName, true);
	}

	public String getDefaultStringParam(String name, String defaultValue) {
		return getStringParam(name, defaultValue, null, false);
	}

	public String getStringParam(String name, String defaultValue, String displayName, boolean required) {
		String value = httpParams.get(name);
		if (StringUtil.isEmpty(value)) {
			if (required) {
				throw new LackParameterException(getLackParaDisplayStr(displayName));
			} else {
				return defaultValue;
			}
		}
		return value.trim();
	}

	/**
	 * 获取Long类型数值
	 * 
	 * @param name
	 * @return
	 */
	protected long getLongParam(String name) {
		return getLongParam(name, 0, name, true);
	}

	public long getLongParam(String name, String displayName) {
		return getLongParam(name, 0, displayName, true);
	}

	protected long getDefaultLongParam(String name, long defaultValue) {
		return getLongParam(name, defaultValue, null, false);
	}

	protected long getLongParam(String name, long defaultValue, String displayName, boolean required) {
		String value = getDefaultStringParam(name, StringUtil.EMPTY);

		if (StringUtil.isEmpty(value)) {
			if (required) {
				throw new LackParameterException(getLackParaDisplayStr(displayName));
			}
		}

		return StringUtil.parseLong(value, defaultValue);
	}

	/**
	 * 获取Int类型数值
	 * 
	 * @param name
	 * @return
	 */
	protected int getIntParam(String name) {
		return getIntParam(name, 0, name, true);
	}

	protected int getIntParam(String name, String displayName) {
		return getIntParam(name, 0, displayName, true);
	}

	protected int getDefaultIntParam(String name, int defaultValue) {
		return getIntParam(name, defaultValue, null, false);
	}

	protected int getIntParam(String name, int defaultValue, String displayName, boolean required) {
		String value = getDefaultStringParam(name, StringUtil.EMPTY);
		if (StringUtil.isEmpty(value)) {
			if (required) {
				throw new LackParameterException(getLackParaDisplayStr(displayName));
			}
		}

		return StringUtil.parseInt(value, defaultValue);
	}

	/**
	 * 平台，该参数必传
	 *
	 * @return
	 */
	public String getPlatform() {
		String platform = getStringParam("platform");

		if(!platform.isEmpty()){//非空校验
		    if (!Constants.platform.HFQ_PLATFORM.contains(platform)) {
	            throw new InvalidParameterException(getInvalidParaDisplayStr("平台"));
	        }  
		}
		

		return platform;
	}

	/**
	 * 获取客户端版本号，app必传，web不需要传
	 *
	 * @return
	 */
	public int getClientVersion() {
		String platform = getPlatform();
		if (Constants.platform.isAppPlatform(platform)) {
			return getDefaultIntParam("appver", 0);
		}

		return 0;
	}

	/**
	 * 获取sessionid
	 *
	 * @return
	 */
	public String getSessionId() {

		// 先尝试从url中获取
		String sessionId = httpParams.get("sid");

		// // 获取失败并且为web客户端，再从cookie中获取
		String platform = getPlatform();
		if (StringUtil.isEmpty(sessionId) && Constants.platform.isWebPlatform(platform)) {
			sessionId = CookieUtil.getValue(httpServletRequest, "sid");
		}
//		if (StringUtil.isEmpty(sessionId)) {
//			throw new LackParameterException(getLackParaDisplayStr("会话Id"));
//		}
		return sessionId;
	}

	private String getLackParaDisplayStr(String paraName) {
		return String.format("缺少参数:\"%s\"", paraName);
	}

	private String getInvalidParaDisplayStr(String paraName) {
		return String.format("非法参数:\"%s\"", paraName);
	}

	// --------getter & setter------------

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHttpParams() {
		return httpParams;
	}

	public void setHttpParams(Map<String, String> httpParams) {
		this.httpParams = httpParams;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

}
