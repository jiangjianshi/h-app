/** 
* Project Name: hzf_platform_project 
* File Name: ComplaintDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.request 
* Date: 2016年4月29日上午10:12:33 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.request.house;

/**
 * ClassName: ComplaintDto date: 2016年4月29日 上午10:12:33 Description: 投诉DTO
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class ComplaintSaveDto {

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
	private int complaint;

	/**
	 * 投诉内容
	 */
	private String comment;

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

	@Override
	public String toString() {
		return "ComplaintDto [sellId=" + sellId + ", roomId=" + roomId + ", uid=" + uid + ", complaint=" + complaint
				+ ", comment=" + comment + "]";
	}

}
