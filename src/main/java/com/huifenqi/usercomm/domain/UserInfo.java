/** 
 * Project Name: usercomm_project 
 * File Name: UserInfo.java 
 * Package Name: com.huifenqi.usercomm.domain 
 * Date: 2015年12月23日下午7:37:46 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.usercomm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: UserInfo date: 2015年12月23日 下午7:37:46 Description: 用户信息
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_user_info")
public class UserInfo implements Serializable {

	/**
	 * 已扫码
	 */
	public static final int USER_INFO_STATUS_SCANSIGNED = 0;

	/**
	 * 已确认合同信息并上传身份信息
	 */
	public static final int USER_INFO_STATUS_ID = 1;

	/**
	 * 完善个人信息
	 */
	public static final int USER_INFO_STATUS_DETAIL = 2;

	/**
	 * 绑定银行卡成功
 	 */
	public static final int USER_INFO_STATUS_BIND_CARD = 3;

	private static final long serialVersionUID = 7992190799854124135L;

	// 使用社保密码
	public static final int SOCIAL_PWD_USED = 0;

	// 未使用密码
	public static final int SOCIAL_PWD_UNUSED = 1;
	
	// 证件类型
	private static final int USER_ID_TYPE = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="f_id")
	private long id;

	/**
	 * 关联用户表Id
	 */
	@Column(name="f_user_id")
	private long userId;

	/**
	 * 姓名
	 */
	@Column(name="f_user_name")
	private String name;

	/**
	 * 手机号
	 */
	@Column(name="f_user_mobile")
	private String userMobile;
	
	/**
	 * 性别
	 */
	@Column(name="f_user_sex")
	private Integer userSex;
	
	/**
	 * 证件类型
	 */
	@Column(name="f_user_id_type")
	private Integer userIdType = USER_ID_TYPE;
	
	/**
	 * 身份证号
	 */
	@Column(name="f_user_id_no")
	private String userIdNo;
	
	/**
	 * 用户开户银行
	 */
	@Column(name="f_user_bank_name")
	private String userBankName;
	
	/**
	 * 账户名称
	 */
	@Column(name="f_user_bank_account")
	private String userBankAccount;

	/**
	 * 用户银行卡号
	 */
	@Column(name="f_user_card_no")
	private String userCardNo;
	
	/**
	 * 用户银行预留手机号
	 */
	@Column(name="f_user_card_mobile")
	private String userCardMobile;
	
	/**
	 * 紧急联系人姓名
	 */
	@Column(name="f_user_linkman_name")
	private String userLinkmanName;
	
	/**
	 * 紧急联系人电话
	 */
	@Column(name="f_user_linkman_mobile")
	private String userLinkmanMobile;
	
	/**
	 * 紧急联系人关系
	 */
	@Column(name="f_user_linkman_relation")
	private Integer userLinkmanRelation;
	
	/**
	 * qq号
	 */
	@Column(name="f_user_qq")
	private String qqNo;
	
	/**
	 * 公司名称
	 */
	@Column(name="f_user_company_name")
	private String userCompanyName;
	
	/**
	 * 公司地址
	 */
	@Column(name="f_user_company_address")
	private String userCompanyAddress;
	
	/**
	 * 用户工作状态
	 */
	@Column(name="f_user_work_status")
	private Integer userWorkStatus;
	
	/**
	 * 公司邮箱
	 */
	@Column(name="f_user_company_mail")
	private String userCompanyMail;
	
	/**
	 * 用户月收入
	 */
	@Column(name="f_user_income")
	private Integer userIncome;
	
	/**
	 * 工作职位
	 */
	@Column(name="f_user_job")
	private String userJob;
	
	/**
	 * 工作证照片
	 */
	@Column(name="f_user_company_card")
	private String userCompanyCard;
	
	/**
	 * 创建时间
	 */
	@Column(name="f_create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name="f_update_time")
	private Date updateTime;

	/**
	 * 无纸化流程 0.已扫码 1.已确认合同信息 2.已保存个人详细信息 3.已绑卡
	 */
	private Integer step;

	/**
	 * 身份证正面照片
	 */
	@Column(name="f_id_posi_name")
	private String idPosiName;

	/**
	 * 身份证反面照片
	 */
	@Column(name="f_id_neg_name")
	private String idNegName;

	/**
	 * 手持身份证照片
	 */
	@Column(name="f_face_pic_name")
	private String facePicName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public Integer getUserIdType() {
		return userIdType;
	}

	public void setUserIdType(Integer userIdType) {
		this.userIdType = userIdType;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public void setUserIdNo(String userIdNo) {
		this.userIdNo = userIdNo;
	}

	public String getUserBankName() {
		return userBankName;
	}

	public void setUserBankName(String userBankName) {
		this.userBankName = userBankName;
	}

	public String getUserBankAccount() {
		return userBankAccount;
	}

	public void setUserBankAccount(String userBankAccount) {
		this.userBankAccount = userBankAccount;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getUserCardMobile() {
		return userCardMobile;
	}

	public void setUserCardMobile(String userCardMobile) {
		this.userCardMobile = userCardMobile;
	}

	public String getUserLinkmanName() {
		return userLinkmanName;
	}

	public void setUserLinkmanName(String userLinkmanName) {
		this.userLinkmanName = userLinkmanName;
	}

	public String getUserLinkmanMobile() {
		return userLinkmanMobile;
	}

	public void setUserLinkmanMobile(String userLinkmanMobile) {
		this.userLinkmanMobile = userLinkmanMobile;
	}

	public Integer getUserLinkmanRelation() {
		return userLinkmanRelation;
	}

	public void setUserLinkmanRelation(Integer userLinkmanRelation) {
		this.userLinkmanRelation = userLinkmanRelation;
	}

	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	public String getUserCompanyName() {
		return userCompanyName;
	}

	public void setUserCompanyName(String userCompanyName) {
		this.userCompanyName = userCompanyName;
	}

	public String getUserCompanyAddress() {
		return userCompanyAddress;
	}

	public void setUserCompanyAddress(String userCompanyAddress) {
		this.userCompanyAddress = userCompanyAddress;
	}

	public Integer getUserWorkStatus() {
		return userWorkStatus;
	}

	public void setUserWorkStatus(Integer userWorkStatus) {
		this.userWorkStatus = userWorkStatus;
	}

	public String getUserCompanyMail() {
		return userCompanyMail;
	}

	public void setUserCompanyMail(String userCompanyMail) {
		this.userCompanyMail = userCompanyMail;
	}

	public Integer getUserIncome() {
		return userIncome;
	}

	public void setUserIncome(Integer userIncome) {
		this.userIncome = userIncome;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getUserCompanyCard() {
		return userCompanyCard;
	}

	public void setUserCompanyCard(String userCompanyCard) {
		this.userCompanyCard = userCompanyCard;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getIdPosiName() {
		return idPosiName;
	}

	public void setIdPosiName(String idPosiName) {
		this.idPosiName = idPosiName;
	}

	public String getIdNegName() {
		return idNegName;
	}

	public void setIdNegName(String idNegName) {
		this.idNegName = idNegName;
	}

	public String getFacePicName() {
		return facePicName;
	}

	public void setFacePicName(String facePicName) {
		this.facePicName = facePicName;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"id=" + id +
				", userId=" + userId +
				", name='" + name + '\'' +
				", userMobile='" + userMobile + '\'' +
				", userSex=" + userSex +
				", userIdType=" + userIdType +
				", userIdNo='" + userIdNo + '\'' +
				", userBankName='" + userBankName + '\'' +
				", userBankAccount='" + userBankAccount + '\'' +
				", userCardNo='" + userCardNo + '\'' +
				", userCardMobile='" + userCardMobile + '\'' +
				", userLinkmanName='" + userLinkmanName + '\'' +
				", userLinkmanMobile='" + userLinkmanMobile + '\'' +
				", userLinkmanRelation=" + userLinkmanRelation +
				", qqNo='" + qqNo + '\'' +
				", userCompanyName='" + userCompanyName + '\'' +
				", userCompanyAddress='" + userCompanyAddress + '\'' +
				", userWorkStatus=" + userWorkStatus +
				", userCompanyMail='" + userCompanyMail + '\'' +
				", userIncome=" + userIncome +
				", userJob='" + userJob + '\'' +
				", userCompanyCard='" + userCompanyCard + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", step=" + step +
				'}';
	}
}
