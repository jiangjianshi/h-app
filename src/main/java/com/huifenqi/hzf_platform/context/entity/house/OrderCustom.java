/** 
* Project Name: hzf_platform 
* File Name: OrderCustom.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2017年8月18日 上午9:55:50
* Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
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

/**
 * ClassName: OrderCustom date: 2017年8月18日 上午9:55:50 Description:用户订制
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_order_custom")
public class OrderCustom {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 用户ID
	 */
	@Column(name = "f_user_id")
	private long userId;
	
	/**
	 * 地理位置
	 */
	@Column(name = "f_location")
	private String location;
	
	/**
	 * 房间价格(最小区间)
	 */
	@Column(name = "f_min_price")
	private long minPrice;
	
	/**
	 * 房间价格(最大区间)
	 */
	@Column(name = "f_max_price")
	private long maxPrice;
	
	/**
	 * 入住时间
	 */
	@Column(name = "f_check_in_time")
	private Date checkInTime;
	
	/**
	 * 手机号
	 */
	@Column(name = "f_phone")
	private String phone;
	
	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
	private Date updateTime;
	
	/**
	 * 有效标识：1：默认有效；0：失效
	 */
	@Column(name = "f_state")
	private int state;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(long minPrice) {
		this.minPrice = minPrice;
	}

	public long getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(long maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}