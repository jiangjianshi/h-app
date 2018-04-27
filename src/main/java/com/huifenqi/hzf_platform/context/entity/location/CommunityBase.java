/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
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
 * ClassName: CommunityBase date: 2017年10月11日 下午4:05:50 Description:小区库
 * 
 * @author changminwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_community_base")
public class CommunityBase {

	public CommunityBase() {
	}

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	
	/**
	 * 房屋所在城市
	 */
	@Column(name = "f_city_name")
	private String cityName;
	
	/**
	 * 房屋所在城市ID
	 */
	@Column(name = "f_city_id")
	private long cityId;

	/**
	 * 房屋所在区
	 */
	@Column(name = "f_district_name")
	private String districtName;
	
	/**
	 * 房屋所在区ID
	 */
	@Column(name = "f_district_id")
	private long districtId;

	/**
	 * 房屋所在商圈
	 */
	@Column(name = "f_biz_name")
	private String bizName;
	
	/**
	 * 房屋所在商圈ID
	 */
	@Column(name = "f_biz_id")
	private long bizId;



	/**
	 * 房屋所在小区名称
	 */
	@Column(name = "f_community_name")
	private String communityName;

	/**
	 * 小区详细地址
	 */
	@Column(name = "f_address")
	private String address;


	/**
	 * 小区坐标点
	 */
	@Column(name = "f_center")
	private String center;

	/**
	 * 位置的附加信息
	 */
	@Column(name = "f_precise")
	private int precise;

	/**
	 * 可信度
	 */
	@Column(name = "f_confidence")
	private int confidence;

	/**
	 * 地址类型
	 */
	@Column(name = "f_level")
	private String level;
	
	/**
	 * 收集类型
	 */
	@Column(name = "f_flag")
	private int flag;
	
	
	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}



	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getPrecise() {
		return precise;
	}

	public void setPrecise(int precise) {
		this.precise = precise;
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "CommunityBase [id=" + id + ", cityName=" + cityName + ", cityId=" + cityId + ", districtName="
				+ districtName + ", districtId=" + districtId + ", bizName=" + bizName + ", bizId=" + bizId
				+ ", communityName=" + communityName + ", address=" + address + ", center=" + center + ", precise="
				+ precise + ", confidence=" + confidence + ", level=" + level + ", flag=" + flag + ", updateTime="
				+ updateTime + ", createTime=" + createTime + "]";
	}

	
}