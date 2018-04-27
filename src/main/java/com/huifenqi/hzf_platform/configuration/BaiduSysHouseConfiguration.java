/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月4日下午2:55:16 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: BaiduConfiguration
 * date: 2016年5月4日 下午2:55:16
 * Description: 
 * 
 * @author changmingwei 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class BaiduSysHouseConfiguration {
	
	@Value("${hfq.baidu.sys.houseSubmit.url}")
	private String hsUrl;
	
	@Value("${hfq.baidu.sys.houseModify.url}")
	private String hmUrl;
	
	@Value("${hfq.baidu.sys.roomSingleModify.url}")
	private String rsmUrl;

	@Value("${hfq.baidu.sys.apartmentApply.url}")
	private String atUrl;
	
	@Value("${hfq.baidu.sys.imageUpload.url}")
	private String imgUrl;

	@Value("${hfq.baidu.sys.apartmentApplyBack.url}")
	private String backUrl;
	
	@Value("${hfq.baidu.sys.ak}")
	private String ak;
	
	@Value("${hfq.baidu.sys.appId}")
	private String appId;
	
	@Value("${hfq.baidu.sys.aesKey}")
	private String aesKey;

	public String getHsUrl() {
		return hsUrl;
	}

	public void setHsUrl(String hsUrl) {
		this.hsUrl = hsUrl;
	}

	public String getHmUrl() {
		return hmUrl;
	}

	public void setHmUrl(String hmUrl) {
		this.hmUrl = hmUrl;
	}

	public String getRsmUrl() {
		return rsmUrl;
	}

	public void setRsmUrl(String rsmUrl) {
		this.rsmUrl = rsmUrl;
	}

	public String getAtUrl() {
		return atUrl;
	}

	public void setAtUrl(String atUrl) {
		this.atUrl = atUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	
	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	
	
}
