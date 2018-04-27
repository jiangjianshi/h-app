/** 
* Project Name: hzf_platform 
* File Name: City.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:25:27 
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ClassName: CommuteMapConfig date: 2016年4月25日 下午4:25:27 Description: 通勤配置
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_commute_map_config")
public class CommuteMapConfig {
	
	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "f_travel_type")
	private String travelType;

	@Column(name = "f_travel_type_name")
	private String travelTypeName;

	@Column(name = "f_travel_type_speed")
	private int travelTypeSpeed;
	
	@Column(name = "f_travel_type_url")
	private String travelTypeUrl;
	
	@Column(name = "f_time")
	private String time;
	
	@Column(name = "f_time_name")
	private String timeName;
	
	@Column(name = "f_scale")
	private String scale;

	@Column(name = "f_is_default")
	private int isDefault;
	
	@Column(name = "f_status")
	private int status;
	
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTravelType() {
		return travelType;
	}

	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}

	public String getTravelTypeName() {
		return travelTypeName;
	}

	public void setTravelTypeName(String travelTypeName) {
		this.travelTypeName = travelTypeName;
	}

	public int getTravelTypeSpeed() {
		return travelTypeSpeed;
	}

	public void setTravelTypeSpeed(int travelTypeSpeed) {
		this.travelTypeSpeed = travelTypeSpeed;
	}

	public String getTravelTypeUrl() {
		return travelTypeUrl;
	}

	public void setTravelTypeUrl(String travelTypeUrl) {
		this.travelTypeUrl = travelTypeUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		return "CommuteMapConfig [id=" + id + ", travelType=" + travelType + ", travelTypeName=" + travelTypeName
				+ ", travelTypeSpeed=" + travelTypeSpeed + ", travelTypeUrl=" + travelTypeUrl + ", time=" + time
				+ ", timeName=" + timeName + ", scale=" + scale + ", isDefault=" + isDefault + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
