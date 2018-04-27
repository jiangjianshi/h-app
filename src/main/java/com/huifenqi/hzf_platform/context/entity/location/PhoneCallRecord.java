/** 
 * Project Name: hzf_platform_project 
 * File Name: PhoneCallRecord.java 
 * Package Name: com.huifenqi.hzf_platform.context.entity.location 
 * Date: 2016年5月19日下午4:03:46 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.entity.location;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * ClassName: PhoneCallRecord
 * date: 2016年5月19日 下午4:03:46
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Entity
@Table( name = "t_phone_call_static" )
public class PhoneCallRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "f_id")
	private long id;
	
	@Column(name = "f_phone")
	private String phone;
	
	@Column(name = "f_house_sell_id")
	private String sellId;
	
	@Column(name = "f_room_id")
	private long roomId;
	
	@Column(name = "f_create_time")
	private Date createTime;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "PhoneCallRecord [id=" + id + ", phone=" + phone + ", sellId=" + sellId + ", roomId=" + roomId
				+ ", createTime=" + createTime + "]";
	}
}
