/** 
* Project Name: hzf_platform 
* File Name: RoomBase.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:07:13 
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
 * ClassName: RoomBase date: 2016年4月25日 下午4:07:13 Description: 房间基础信息
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_room_base")
public class RoomBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房间状态
	 */
	@Column(name = "f_status")
	private int status;

	/**
	 * 面积
	 */
	@Column(name = "f_area")
	private float area;

	/**
	 * 房间描述
	 */
	@Column(name = "f_room_comment")
	private String roomComment;

	/**
	 * 房间类型
	 */
	@Column(name = "f_room_type")
	private int roomType;

	/**
	 * 房间用途类型
	 */
	@Column(name = "f_room_use")
	private int roomUse;

	/**
	 * 朝向
	 */
	@Column(name = "f_orientations")
	private int orientations;

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
	 * 审核房源的人员id
	 */
	@Column(name = "f_approved_id")
	private int approvedId;

	/**
	 * 装修档次
	 */
	@Column(name = "f_decoration")
	private int decoration;

	/**
	 * 是否有独立卫生间
	 */
	@Column(name = "f_toilet")
	private int toilet;

	/**
	 * 是否有独立阳台
	 */
	@Column(name = "f_balcony")
	private int balcony;

	/**
	 * 是否有家财险
	 */
	@Column(name = "f_insurance")
	private int insurance;

	/**
	 * 房间描述
	 */
	@Column(name = "f_comment")
	private String comment;

	/**
	 * 房间代号
	 */
	@Column(name = "f_room_code")
	private String roomCode;

	/**
	 * 房间标签
	 */
	@Column(name = "f_room_tag")
	private String roomTag;

	/**
	 * 产品名称
	 */
	@Column(name = "f_production_name")
	private String productionName;

	/**
	 * 房间标签
	 */
	@Column(name = "f_room_name")
	private String roomName;

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
	 * 是否置顶
	 */
	@Column(name = "f_is_top")
	private int isTop;
	
	/**
	 * 是否已审核
	 */
	@Column(name = "f_approve_status")
	private int approveStatus;
	
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

	/**
	 * 经纪人电话
	 */
	@Column(name = "f_agency_phone")
	private String agencyPhone;

	/**
	 * 是否支持月付 0:不支持；1：支持
	 */
	@Column(name = "f_is_pay_month")
	private int isPayMonth;

	/**
	 * saas发布类型
	 */
	@Column(name = "f_pub_type")
	private int pubType;
	

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public String getRoomComment() {
		return roomComment;
	}

	public void setRoomComment(String roomComment) {
		this.roomComment = roomComment;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public int getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(int roomUse) {
		this.roomUse = roomUse;
	}

	public int getOrientations() {
		return orientations;
	}

	public void setOrientations(int orientations) {
		this.orientations = orientations;
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

	public int getApprovedId() {
		return approvedId;
	}

	public void setApprovedId(int approvedId) {
		this.approvedId = approvedId;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
	}

	public int getToilet() {
		return toilet;
	}

	public void setToilet(int toilet) {
		this.toilet = toilet;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomTag() {
		return roomTag;
	}

	public void setRoomTag(String roomTag) {
		this.roomTag = roomTag;
	}

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
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


	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
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

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}
	

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

	public int getIsPayMonth() {
		return isPayMonth;
	}

	public void setIsPayMonth(int isPayMonth) {
		this.isPayMonth = isPayMonth;
	}

	public int getPubType() {
		return pubType;
	}

	public void setPubType(int pubType) {
		this.pubType = pubType;
	}

	@Override
	public String toString() {
		return "RoomBase [id=" + id + ", sellId=" + sellId + ", status=" + status + ", area=" + area + ", roomComment="
				+ roomComment + ", roomType=" + roomType + ", roomUse=" + roomUse + ", orientations=" + orientations
				+ ", checkinTime=" + checkinTime + ", monthRent=" + monthRent + ", dayRent=" + dayRent + ", serviceFee="
				+ serviceFee + ", depositFee=" + depositFee + ", depositMonth=" + depositMonth + ", periodMonth="
				+ periodMonth + ", approvedId=" + approvedId + ", decoration=" + decoration + ", toilet=" + toilet
				+ ", balcony=" + balcony + ", insurance=" + insurance + ", comment=" + comment + ", roomCode="
				+ roomCode + ", roomTag=" + roomTag + ", productionName=" + productionName + ", roomName=" + roomName
				+ ", firstPubDate=" + firstPubDate + ", pubDate=" + pubDate + ", hasKey=" + hasKey + ", isDelete="
				+ isDelete + ", isTop=" + isTop + ", approveStatus=" + approveStatus + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", agencyPhone=" + agencyPhone + "]";
	}

	
}
