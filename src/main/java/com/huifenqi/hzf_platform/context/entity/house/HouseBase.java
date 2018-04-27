/** 
* Project Name: hzf_platform 
* File Name: HouseBase.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:37 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ClassName: HouseBase date: 2016年4月25日 下午4:05:37 Description: 房源基础信息
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_base")
public class HouseBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房源是否上架 0:未上架；1：上架
	 */
	@Column(name = "f_is_sale")
	private int isSale;

	/**
	 * 房源状态
	 */
	@Column(name = "f_status")
	private int status;

	/**
	 * 400分机号
	 */
	@Column(name = "f_ext400")
	private String ext400;

	/**
	 * 房源描述
	 */
	@Column(name = "f_house_comment")
	private String comment;

	/**
	 * 可入住时间
	 */
	@Column(name = "f_can_checkin_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkinTime;

	/**
	 * 月租金
	 */
	@Column(name = "f_rent_price_month")
	private int monthRent;

	/**
	 * 日租金
	 */
	@Column(name = "f_rent_price_day")
	private int dayRent;

	/**
	 * 服务费或中介费
	 */
	@Column(name = "f_service_fee")
	private int serviceFee;

	/**
	 * 押金
	 */
	@Column(name = "f_deposit_fee")
	private int depositFee;

	/**
	 * 押金押几个月
	 */
	@Column(name = "f_deposit_month")
	private int depositMonth;

	/**
	 * 每次付几个月的租金
	 */
	@Column(name = "f_period_month")
	private int periodMonth;

	/**
	 * 经纪公司id
	 */
	@Column(name = "f_company_id")
	private String companyId; //2017-07-03 14:47:03  jjs 由int类型改为String

	/**
	 * 经纪公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;

	/**
	 * 经纪人id
	 */
	@Column(name = "f_agency_id")
	private int agencyId;

	/**
	 * 经纪人电话
	 */
	@Column(name = "f_agency_phone")
	private String agencyPhone;

	/**
	 * 经纪人姓名
	 */
	@Column(name = "f_agency_name")
	private String agencyName;

	/**
	 * 经纪人自我介绍
	 */
	@Column(name = "f_agency_introduce")
	private String agencyIntroduce;

	/**
	 * 经纪人性别 1-男 2-女
	 */
	@Column(name = "f_agency_gender")
	private int agencyGender;

	/**
	 * 经纪人头像
	 */
	@Column(name = "f_agency_avatar")
	private String agencyAvatar;

	/**
	 * 审核房源的人员id
	 */
	@Column(name = "f_approved_id")
	private int approvedId;

	/**
	 * 第一次上架时间
	 */
	@Column(name = "f_first_pub_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstPubDate;

	/**
	 * 上架时间
	 */
	@Column(name = "f_pub_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pubDate;

	/**
	 * 是否有钥匙；1:有；0:无
	 */
	@Column(name = "f_has_key")
	private int hasKey;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;
	
	/**
	 * 来源标识
	 */
	@Column(name = "f_source_flag")
	private int sourceFlag;

	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public int getIsSale() {
		return isSale;
	}

	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExt400() {
		return ext400;
	}

	public void setExt400(String ext400) {
		this.ext400 = ext400;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	public int getMonthRent() {
		return monthRent;
	}

	public void setMonthRent(int monthRent) {
		this.monthRent = monthRent;
	}

	public int getDayRent() {
		return dayRent;
	}

	public void setDayRent(int dayRent) {
		this.dayRent = dayRent;
	}

	public int getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}

	public int getDepositFee() {
		return depositFee;
	}

	public void setDepositFee(int depositFee) {
		this.depositFee = depositFee;
	}

	public int getDepositMonth() {
		return depositMonth;
	}

	public void setDepositMonth(int depositMonth) {
		this.depositMonth = depositMonth;
	}

	public int getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(int periodMonth) {
		this.periodMonth = periodMonth;
	}
	
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyIntroduce() {
		return agencyIntroduce;
	}

	public void setAgencyIntroduce(String agencyIntroduce) {
		this.agencyIntroduce = agencyIntroduce;
	}

	public int getAgencyGender() {
		return agencyGender;
	}

	public void setAgencyGender(int agencyGender) {
		this.agencyGender = agencyGender;
	}

	public String getAgencyAvatar() {
		return agencyAvatar;
	}

	public void setAgencyAvatar(String agencyAvatar) {
		this.agencyAvatar = agencyAvatar;
	}

	public int getApprovedId() {
		return approvedId;
	}

	public void setApprovedId(int approvedId) {
		this.approvedId = approvedId;
	}

	public Date getFirstPubDate() {
		return firstPubDate;
	}

	public void setFirstPubDate(Date firstPubDate) {
		this.firstPubDate = firstPubDate;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public int getHasKey() {
		return hasKey;
	}

	public void setHasKey(int hasKey) {
		this.hasKey = hasKey;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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

	public int getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(int sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	
}
