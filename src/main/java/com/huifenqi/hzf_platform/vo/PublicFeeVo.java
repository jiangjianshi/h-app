/** 
 * Project Name: usercomm_smart 
 * File Name: SmartMeterVo.java
 * Package Name: com.huifenqi.usercomm.domain.dto.response 
 * Date: 2017年3月3日下午9:54:07 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.vo;

/**
 * ClassName: SmartMeterVo
 * date: 2017年3月3日 下午9:54:07
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class PublicFeeVo {
	
	private String ammeterSn;
	
	private String totalKwh;
	
	private int publicState;
	
	private long userId;

	private String publicTime;

	public String getAmmeterSn() {
		return ammeterSn;
	}

	public void setAmmeterSn(String ammeterSn) {
		this.ammeterSn = ammeterSn;
	}

	public String getTotalKwh() {
		return totalKwh;
	}

	public void setTotalKwh(String totalKwh) {
		this.totalKwh = totalKwh;
	}

	public int getPublicState() {
		return publicState;
	}

	public void setPublicState(int publicState) {
		this.publicState = publicState;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPublicTime() {
		return publicTime;
	}

	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}

	@Override
	public String toString() {
		return "PublicFeeVo{" +
				"ammeterSn='" + ammeterSn + '\'' +
				", totalKwh='" + totalKwh + '\'' +
				", publicState=" + publicState +
				", userId=" + userId +
				", publicTime='" + publicTime + '\'' +
				'}';
	}
}
