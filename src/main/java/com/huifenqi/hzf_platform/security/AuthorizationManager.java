/** 
 * Project Name: hzf_platform_project 
 * File Name: Authorization.java 
 * Package Name: com.huifenqi.hzf_platform.security 
 * Date: 2016年4月25日下午3:24:50 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.configuration.AuthConfiguration;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.platform.PlatformCustomerRepository;
import com.huifenqi.hzf_platform.utils.BdRequestUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RequestUtils;

/**
 * ClassName: AuthorizationManager date: 2016年4月25日 下午3:24:50 Description: 授权管理
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Component
public class AuthorizationManager {

	private static final Log logger = LogFactory.getLog(AuthorizationManager.class);

	@Autowired
	private AuthConfiguration authConfiguration;

	@Autowired
	private PlatformCustomerRepository platformCustomerRepository;

	/**
	 * 进行签名校验
	 * 
	 * @param request
	 * @return true:校验成功;false:校验失败
	 * @throws Exception
	 */
	public boolean checkSign(HttpServletRequest request) throws Exception {

		// String referrer = request.getHeader("referer");
		// if (referrer != null && !RefererUrl.isMember(referrer)) {
		// logger.error(LogUtils.getCommLog(String.format("非法链接签名失败,referrer:%s",
		// referrer)));
		// }
		String appId = RequestUtils.getParameterString(request, "appId");

		boolean needCheckSign = needCheckSign(appId);

		if (needCheckSign) {

			PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
			if (platformCustomer == null) {
				logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
				throw new BaseException(ErrorMsgCode.ERROR_MSG_CUSTOMER_NOT_FOUND, "无效的平台用户");
			}
			String secretKey = platformCustomer.getSecretKey();

			String ts = RequestUtils.getParameterString(request, "ts");
			String sign = RequestUtils.getParameterString(request, "sign");

			Map<String, String> params = RequestUtils.getApiParams(request);

			// 移除掉不需要排序的值
			params.remove("appId");
			params.remove("secretKey");
			params.remove("ts");
			params.remove("sign");
			params.remove("platform");

			if (request.getRequestURI().equals("/saas/search") || request.getRequestURI().equals("/saas/search/")) {// saas店铺页查询房源
				params.remove("agencyId");
			}

			List<String> sParamValues = sortParamValues(params);

			String platform = request.getParameter("platform");

			return checkSign(appId, secretKey, ts, sParamValues, sign, platform);

		} else { // 不需要校验签名
			return true;
		}

	}

	/**
	 * 接口是否需要签名
	 */
	private boolean needCheckSign(String appId) {

		boolean checkSign = authConfiguration.isCheckSign();
		if (!checkSign) {
			return false;
		}

		if (appId.equals(authConfiguration.getTestAppId())) { // 会分期测试Id，不进行校验
			return false;
		}

		return true;
	}

	/**
	 * 进行签名校验
	 * 
	 * @param appId
	 * @param secretKey
	 * @param ts
	 * @param values
	 * @param sign
	 * @return
	 */
	public boolean checkSign(String appId, String secretKey, String ts, List<String> values, String sign,
			String platform) {

		// 拼接加密的字符串
		StringBuilder signSeed = new StringBuilder();
		signSeed.append(appId);
		for (String value : values) {
			signSeed.append(value);
		}
		signSeed.append(secretKey);
		signSeed.append(ts);

		// 使用sha256进行加密
		String seed = signSeed.toString();
		// h5访问时编码设置
		// if (Constants.Platform.H5.equals(platform)) {
		// try {
		// seed = URLEncoder.encode(seed, "UTF-8");
		// // seed = Base64.encodeBase64String(seed.getBytes("UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// }


		String cSign = DigestUtils.sha256Hex(seed);

		if (cSign.equals(sign)) {
			return true;
		}

		logger.info(String.format("校验签名失败, 计算签名:%s, 传入签名 :%s, 计算字符串:%s", cSign, sign, seed));

		return false;
	}
	
	
	/**
	 * 进行签名校验
	 * 
	 * @param appId
	 * @param secretKey
	 * @param values
	 * @param sign
	 * @return
	 */
	public boolean bdCheckSign(String appId, String secretKey,List<String> values, String sign) {

		// 拼接加密的字符串
		StringBuilder signSeed = new StringBuilder();
		signSeed.append(appId);
		for (String value : values) {
			signSeed.append(value);
		}
		signSeed.append(secretKey);

		// 使用sha256进行加密
		String seed = signSeed.toString();

//		if (Constants.Platform.IOS.equals(platform)) {
//		try {
//			// seed = URLEncoder.encode(seed, "UTF-8");
//			seed = Base64.encodeBase64String(seed.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}

		String cSign = DigestUtils.sha256Hex(seed);

		if (cSign.equals(sign)) {
			return true;
		}

		logger.info(String.format("校验签名失败, 计算签名:%s, 传入签名 :%s, 计算字符串:%s", cSign, sign, seed));

		return false;
	}

	/**
	 * 按照参数名称的字典排序
	 * 
	 * @param params
	 * @return
	 */
	public List<String> sortParamValues(Map<String, String> params) {
		List<String> sortResult = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			return sortResult;
		}
		List<String> pKeys = new ArrayList<>();
		pKeys.addAll(params.keySet());
		Collections.sort(pKeys, new Comparator<String>() {

			@Override
			public int compare(String key1, String key2) {
				return key1.compareTo(key2);
			}
		});

		for (String pKey : pKeys) {
			sortResult.add(params.get(pKey));
		}
		return sortResult;
	}
	
	/** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return
     */  
    public final static String getIpAddress(HttpServletRequest request) {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (logger.isInfoEnabled()) {  
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);  
        }  
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);  
                }  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }

    //update by arsion

	/**
	 * 进行基于百度的数据导入接口签名校验
	 *
	 * @param request
	 * @return true:校验成功;false:校验失败
	 * @throws Exception
	 */
	public boolean checkBaiduBaseSign(HttpServletRequest request,Map<String,String> paramMap) throws Exception {
//		String referrer = request.getHeader("referer");
//		if (referrer != null && !RefererUrl.isMember(referrer)) {
//			logger.error(LogUtils.getCommLog(String.format("非法链接签名失败,referrer:%s", referrer)));
//		}

		String appId = paramMap.get("appId");
		logger.debug("current appid: "+appId);
		boolean needCheckSign = needCheckSign(appId);
		if (needCheckSign) {
			PlatformCustomer platformCustomer = platformCustomerRepository.findByAppId(appId);
			if (platformCustomer == null) {
				logger.error(LogUtils.getCommLog(String.format("未找到平台用户,appId:%s", appId)));
				return false;
			}
			String secretKey = platformCustomer.getSecretKey();

			//String ts = RequestUtils.getParameterString(request, "ts");
			String sign = request.getParameter("ak");

			//Map<String, String> params = RequestUtils.getApiParams(request);
			//params.remove("ts");
			//params.remove("ak");
			Map<String, String> params  = BdRequestUtils.handlerDataToMap(request);
			List<String> sParamValues = sortParamValues(params);
			return bdCheckSign(appId, secretKey, sParamValues, sign);
		} else { // 不需要校验签名
			return true;
		}
	}

}
