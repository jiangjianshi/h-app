/** 
* Project Name: trunk 
* File Name: RcInfo.java 
* Package Name: com.huifenqi.usercomm.domain 
* Date: 2016年4月12日下午3:31:47 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.usercomm.domain.contract;

/**
 * ClassName: RcInfo date: 2016年4月12日 下午3:31:47 Description: 风控信息
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class RcInfo {

	// 用户手机号
	private String userPhone;

	// 用户身份证号
	private String userIdNo;

	// 风控标记
	private String rcToken;

	public RcInfo() {
	}

	public RcInfo(String userPhone, String userIdNo, String rcToken) {
		this.userPhone = userPhone;
		this.userIdNo = userIdNo;
		this.rcToken = rcToken;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public void setUserIdNo(String userIdNo) {
		this.userIdNo = userIdNo;
	}

	public String getRcToken() {
		return rcToken;
	}

	public void setRcToken(String rcToken) {
		this.rcToken = rcToken;
	}

	@Override
	public String toString() {
		return "RcInfo [userPhone=" + userPhone + ", userIdNo=" + userIdNo + ", rcToken=" + rcToken + "]";
	}

}
