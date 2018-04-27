/** 
* Project Name: hzf_platform 
* File Name: CustomerComplaint.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:13:59 
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
 * ClassName: Complaint date: 2016年4月25日 下午4:13:59 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_complaint")
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房间id
	 */
	@Column(name = "f_room_id")
	private long roomId;

	/**
	 * 用户id
	 */
	@Column(name = "f_uid")
	private String uid;

	/**
	 * 投诉原因
	 */
	@Column(name = "f_complaint")
	private int complaint;

	/**
	 * 投诉详细内容
	 */
	@Column(name = "f_comment")
	private String comment;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

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

	public int getComplaint() {
		return complaint;
	}

	public void setComplaint(int complaint) {
		this.complaint = complaint;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	@Override
	public String toString() {
		return "Complaint [id=" + id + ", sellId=" + sellId + ", roomId=" + roomId + ", uid=" + uid + ", complaint="
				+ complaint + ", comment=" + comment + ", isDelete=" + isDelete + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
