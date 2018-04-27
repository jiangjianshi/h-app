/** 
* Project Name: hzf_platform 
* File Name: HouseCollection.java
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2017年8月9日 上午11:55:50
* Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: HouseCollection date: 2017年8月10日 上午11:55:50 Description:搜索历史关键词表
 * 
 * @author Arison
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_collection")
public class HouseCollection {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 用户ID
	 */
	@Column(name = "f_user_id")
	private long userId;

	/**
	 * 房源ID
	 */
	@Column(name = "f_sell_id")
	private String sellId;

	/**
	 * 房屋ID
	 */
	@Column(name = "f_room_id")
	private int roomId;

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
	 * 是否出租标识：0：默认未出租；1：已出租
	 */
	@Column(name = "f_is_rent")
	private int isRent;

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

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
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

	public int getIsRent() {
		return isRent;
	}

	public void setIsRent(int isRent) {
		this.isRent = isRent;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}