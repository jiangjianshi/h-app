/** 
 * Project Name: usercomm_project 
 * File Name: BankCard.java 
 * Package Name: com.huifenqi.usercomm.domain 
 * Date: 2016年2月16日下午5:57:34 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.usercomm.charge.domain;

import javax.persistence.*;

/**
 * ClassName: BankCard date: 2016年2月16日 下午5:57:34 Description: 银行卡信息
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_proxy_collect_bankcards")
public class BankCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "f_id")
	private long id;

	@Column(name = "f_user_mobile")
	private String userMobile;

	@Column(name = "f_user_name")
	private String userName;

	@Column(name = "f_bank_card_no")
	private String bankCardNo;

	@Column(name = "f_id_card_no")
	private String idCardNo;

	@Column(name = "f_card_name")
	private String bankName;

	@Column(name = "f_status")
	private int bindStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(int bindStatus) {
		this.bindStatus = bindStatus;
	}

	@Override
	public String toString() {
		return "BankCard [id=" + id + ", userMobile=" + userMobile + ", userName=" + userName + ", bankCardNo="
				+ bankCardNo + ", idCardNo=" + idCardNo + ", bankName=" + bankName + ", bindStatus=" + bindStatus + "]";
	}

}
