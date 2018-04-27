package com.huifenqi.usercomm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName: DoorLock date: 2017年9月6日 下午4:15:23 Description: 智能门锁-指纹密码维护表
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_door_lock")
public class DoorLock {

	/**
     * 日志Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	/**
	 * 用户I	D
	 */
	@Column(name = "f_user_id")
	private long userId;

	/**
	 * 用户手机号
	 */
	@Column(name = "f_phone")
	private String phone;
	
	/**
	 * 手势密码
	 */
	@Column(name = "f_gesture_password")
	private String gesturePassword;

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
	 * TouchId设置标识：1：默认未设置过；0：已设置
	 */
	@Column(name = "f_touch_id")
	private int touchId;
	
	/**
	 * 有效标识：1：默认有效；0：失效
	 */
	@Column(name = "f_state")
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGesturePassword() {
		return gesturePassword;
	}

	public void setGesturePassword(String gesturePassword) {
		this.gesturePassword = gesturePassword;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getTouchId() {
		return touchId;
	}

	public void setTouchId(int touchId) {
		this.touchId = touchId;
	}

	@Override
	public String toString() {
		return "DoorLock [id=" + id + ", userId=" + userId + ", phone=" + phone
				+ ", gesturePassword=" + gesturePassword + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", touchId="
				+ touchId + ", state=" + state + "]";
	}

}
