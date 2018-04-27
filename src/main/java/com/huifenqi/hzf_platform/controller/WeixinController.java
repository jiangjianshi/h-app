/** 
* Project Name: test_20160328 
* File Name: WeixinController.java 
* Package Name: com.huifenqi.usercomm.app.weixin 
* Date: 2016年3月30日下午4:58:58 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.controller;


import com.huifenqi.hzf_platform.context.WeixinConstants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.handler.WeiXinKits;
import com.huifenqi.hzf_platform.handler.WeixinRequestHandler;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.CookieUtil;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: WeixinController date: 2016年3月30日 下午4:58:58 Description:
 * 
 * @author Arison
 * @version
 * @since JDK 1.8
 */
@RestController
public class WeixinController {

	private static Logger logger = LoggerFactory.getLogger(WeixinController.class);

	@Autowired
	private Configuration configuration;

	@Autowired
	private WeiXinKits weiXinKits;

	@Autowired
	private WeixinRequestHandler weixinRequestHandler;


		// 404页面
		public static final String HTTP_404_PAGE = "http://%s/http_status/404.html";


	/**
	 * 微信首页，需要先向微信申请拿到code
	 *
	 * @param response
	 */
	@RequestMapping(value = "/wx/auth/", method = RequestMethod.GET)
	public void wxAuth(@RequestParam(value = "target_url", required = false) String targetUrl,
			HttpServletResponse response) {

		if (StringUtil.isEmpty (targetUrl)) {
			targetUrl = getFullUrl(WeixinConstants.WX_AUTH_REDIRECT);
		}

		try {
			String url = weiXinKits.createGetCodeUrl(WeiXinKits.WX_HZF, targetUrl);

			logger.info("add by arison  -> redirect to url: " + url);

			response.sendRedirect(url);
			return;
		} catch (IOException e) {
			logger.error("重定向到[" + targetUrl + "]失败", e);
		} catch (Exception e) {
			logger.error("获取code接口url失败", e);
		}

		/*try {
			response.sendRedirect(getFullUrl(Urls.HTTP_404_PAGE));
		} catch (IOException e) {
			logger.error("重定向到[" + getFullUrl(Urls.HTTP_404_PAGE) + "]失败", e);
		}*/
		return;
	}

	/**
	 * 微信首页跳转
	 * 
	 * @param code
	 * @param state
	 * @param response
	 */
	@RequestMapping(value = "/wx/wxindexredirect/", method = RequestMethod.GET)
	public void wxIndexRedirect(@RequestParam(value = "code", required = true) String code,
                                @RequestParam(value = "state", required = true) String state, HttpServletResponse response, HttpServletRequest httpHequest) {

		WeiXinKits.WeiXinPubAuth pubAuth = null;

		try {
			pubAuth = weiXinKits.requestAuth(code, WeiXinKits.WX_HZF);
		} catch (Exception e) {
			logger.error("获取微信openid失败", e);
		}

		try {
			if (null != pubAuth) {
				// String domain = configuration.serverDomain.substring(configuration.serverDomain.indexOf("."));
				String domain = ".huizhaofang.com";
				logger.info("add by arison  -> domain " + domain);

				// 微信的过期时间设置为和app一样
				int timeout = (int) (SessionManager.APP_SESSION_TIMEOUT / 1000);

				if (StringUtil.isNotEmpty(pubAuth.unionId)) {
					CookieUtil.save("wx_unionid", pubAuth.unionId, timeout, domain, "/", response);
					logger.debug(" in wxIndexRedirect wx_unionid" +CookieUtil.getValue(httpHequest, "wx_unionid"));
				}

				CookieUtil.save("wx_openid", pubAuth.openId, timeout, domain, "/", response);

				logger.debug(" in wxIndexRedirect wx_openid" +CookieUtil.getValue(httpHequest, "wx_openid"));

				CookieUtil.save("channel", state, timeout, domain, "/", response);

				response.sendRedirect(getJumpPage(WeixinConstants.WX_INDEX_PAGE));
			} else {
				throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务不可用");
				//response.sendRedirect(getFullUrl(Urls.HTTP_404_PAGE));
			}
		} catch (IOException e) {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR,"服务不可用");
			//logger.error("重定向到[" + getFullUrl(Urls.HTTP_404_PAGE) + "]失败", e);
		}
	}

	/**
	 * 微信申请接口跳转
	 * 
	 * @param code
	 * @param state
	 * @param response
	 */
	@RequestMapping(value = "/wx/oldapplyindex/", method = RequestMethod.GET)
	public void oldApplyIndex(@RequestParam(value = "code", required = true) String code,
                              @RequestParam(value = "state", required = true) String state, HttpServletResponse response) {

		String targetUrl = "http://www.huifenqi.com/c/intent/api/installmententry/";
		targetUrl += "?code=" + code + "&state=" + state;

		try {
			response.sendRedirect(targetUrl);
		} catch (IOException e) {
			logger.error("failed to redirect to url=" + targetUrl, e);
		}
	}

	/**
	 * 查询JSDK的授权信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/reqwxjsdkauth/", method = RequestMethod.POST)
	public Responses reqWxjsdkAuth(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		return weixinRequestHandler.reqWxjsdkAuth();
	}

	/**
	 * 获取微信接口调用凭据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/wx/api/reqaccesstoken/", method = RequestMethod.POST)
	public Responses reqWxAccessToken() {

		return weixinRequestHandler.reqWxAccessToken();
	}

	private String getFullUrl(String template) {
		return String.format(template, configuration.serverDomain);
	}

	private String getJumpPage(String template) {
		return String.format(template, configuration.serverH5Domain);
	}
}
