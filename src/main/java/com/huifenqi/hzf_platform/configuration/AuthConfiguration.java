/** 
* Project Name: hzf_platform_project 
* File Name: AuthConfiguration.java 
* Package Name: com.huifenqi.hzf_platform.configuration 
* Date: 2016年5月9日下午2:40:20 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: AuthConfiguration date: 2016年5月9日 下午2:40:20 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Component
public class AuthConfiguration {

	/**
	 * 是否校验签名
	 */
	@Value("${hfq.auth.checksign}")
	private boolean checkSign;

	/**
	 * secretKey生成时使用的自定义key
	 */
	@Value("${hfq.auth.generate.key}")
	private String generateKey;

	/**
	 * 客户端正式appId
	 */
	@Value("${hfq.auth.appid.hfq.client.formal}")
	private String formalAppId;

	/**
	 * 客户端测试appId
	 */
	@Value("${hfq.auth.appid.hfq.client.test}")
	private String testAppId;

	public boolean isCheckSign() {
		return checkSign;
	}

	public void setCheckSign(boolean checkSign) {
		this.checkSign = checkSign;
	}

	public String getGenerateKey() {
		return generateKey;
	}

	public void setGenerateKey(String generateKey) {
		this.generateKey = generateKey;
	}

	public String getFormalAppId() {
		return formalAppId;
	}

	public void setFormalAppId(String formalAppId) {
		this.formalAppId = formalAppId;
	}

	public String getTestAppId() {
		return testAppId;
	}

	public void setTestAppId(String testAppId) {
		this.testAppId = testAppId;
	}

}
