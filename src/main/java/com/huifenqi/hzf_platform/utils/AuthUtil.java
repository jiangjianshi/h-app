/** 
* Project Name: hzf_platform_project 
* File Name: AuthUtil.java 
* Package Name: com.huifenqi.hzf_platform.utils 
* Date: 2016年5月16日下午3:46:53 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * ClassName: AuthUtil date: 2016年5月16日 下午3:46:53 Description: 鉴权工具类
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class AuthUtil {

	private static final int OFFSET = 10000;

	/**
	 * 获取appId
	 * 
	 * @param id
	 * @return
	 */
	public static String getAppId(long id) {
		long appId = id + OFFSET;
		return String.valueOf(appId);
	}

	/**
	 * 获取SecretKey
	 * 
	 * @param appId
	 * @param customKey
	 * @return
	 */
	public static String getSecretKey(String appId, String customKey) {
		if (StringUtil.isEmpty(appId)) {
			return null;
		}
		if (StringUtil.isEmpty(customKey)) {
			return null;
		}
		String dateTime = DateUtil.formatCurrentDateTime();

		StringBuilder builder = new StringBuilder();
		builder.append(appId);
		builder.append(dateTime);
		builder.append(customKey);
		String data = builder.toString();

		String secretKey = DigestUtils.md5Hex(data);
		return secretKey;

	}
	
	// public static void main(String[] args) {
	// String secretKey = getSecretKey("110001", "hzf3hC3gY3dBZ1n07v5");
	// System.out.println(secretKey);
	// }

}
