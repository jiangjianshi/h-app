/** 
 * Project Name: hzf_platform_project 
 * File Name: ComplaintQueryInfo.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
 * Date: 2016年4月29日上午11:32:46 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.Date;

/**
 * ClassName: ComplaintQueryInfo date: 2016年4月29日 上午11:32:46 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class ComplaintInfo {
	/**
	 * 投诉id
	 */
	private long complaintId;

	/**
	 * 房源销售id
	 */
	private String sellId;

	/**
	 * 房间编号
	 */
	private long roomId;

	/**
	 * 用户id
	 */
	private String uid;

	/**
	 * 投诉原因
	 */
	private String complaint;

	/**
	 * 投诉内容
	 */
	private String comment;

	/**
	 * 投诉时间
	 */
	private Date createDate;

	/**
	 * 投诉更新时间
	 */
	private Date updateDate;

	public long getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(long complaintId) {
		this.complaintId = complaintId;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "ComplaintInfo [complaintId=" + complaintId + ", sellId=" + sellId + ", roomId=" + roomId + ", uid="
				+ uid + ", complaint=" + complaint + ", comment=" + comment + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + "]";
	}

}
