package com.huifenqi.activity.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VoucherActivity {

	// 活动结束
	public static final int ACTIVITY_STATE_CLOSED = 0;

	// 活动进行中
	public static final int ACTIVITY_STATE_RUNING = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 活动名称
	 */
	private String name;

	/**
	 * 活动状态 0:活动已结束； 1:活动正在进行
	 */
	private int state;

	/**
	 * 是否显示 0:显示 1:不显示
	 */
	private int displayState;

	/**
	 * 活动来源
	 */
	private String comefrom;

	/**
	 * 活动图片地址
	 */
	private String acImg;

	/**
	 * 活动主页地址
	 */
	private String acUrl;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDisplayState() {
		return displayState;
	}

	public void setDisplayState(int displayState) {
		this.displayState = displayState;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getAcImg() {
		return acImg;
	}

	public void setAcImg(String acImg) {
		this.acImg = acImg;
	}

	public String getAcUrl() {
		return acUrl;
	}

	public void setAcUrl(String acUrl) {
		this.acUrl = acUrl;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VoucherActivity [id=" + id + ", name=" + name + ", state=" + state + ", displayState=" + displayState
				+ ", comefrom=" + comefrom + ", acImg=" + acImg + ", acUrl=" + acUrl + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
