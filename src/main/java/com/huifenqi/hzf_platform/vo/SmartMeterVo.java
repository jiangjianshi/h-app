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
public class SmartMeterVo {
	
	private String meterSn;
	
	private String meterName;
	
	private int meterStatus;
	
	private double laveKwh;
	
	private long meterId;

	public String getMeterSn() {
		return meterSn;
	}

	public String getMeterName() {
		return meterName;
	}

	public int getMeterStatus() {
		return meterStatus;
	}

	public double getLaveKwh() {
		return laveKwh;
	}

	public void setMeterSn(String meterSn) {
		this.meterSn = meterSn;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public void setMeterStatus(int meterStatus) {
		this.meterStatus = meterStatus;
	}

	public void setLaveKwh(double laveKwh) {
		this.laveKwh = laveKwh;
	}

	public long getMeterId() {
		return meterId;
	}

	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	@Override
	public String toString() {
		return String.format("SmartMeterVo [meterSn=%s, meterName=%s, meterStatus=%s, laveKwh=%s, meterId=%s]",
				meterSn, meterName, meterStatus, laveKwh, meterId);
	}
}
