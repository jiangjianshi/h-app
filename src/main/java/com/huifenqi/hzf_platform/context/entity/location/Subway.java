/** 
* Project Name: hzf_platform 
* File Name: Subway.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:26:05 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName: Subway date: 2016年4月25日 下午4:26:05 Description: 地铁
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_subway")
public class Subway {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long cityId;

	private int subwayStationId;

	private String station;

	private int subwayLineId;

	private String subwayLine;

	private int openStatus;

	private String latitude;

	private String longitude;
	
	private String uid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSubwayStationId() {
		return subwayStationId;
	}

	public void setSubwayStationId(int subwayStationId) {
		this.subwayStationId = subwayStationId;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public int getSubwayLineId() {
		return subwayLineId;
	}

	public void setSubwayLineId(int subwayLineId) {
		this.subwayLineId = subwayLineId;
	}

	public String getSubwayLine() {
		return subwayLine;
	}

	public void setSubwayLine(String subwayLine) {
		this.subwayLine = subwayLine;
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "Subway [id=" + id + ", cityId=" + cityId + ", subwayStationId=" + subwayStationId + ", station="
				+ station + ", subwayLineId=" + subwayLineId + ", subwayLine=" + subwayLine + ", openStatus="
				+ openStatus + ", latitude=" + latitude + ", longitude=" + longitude + ", uid=" + uid + "]";
	}


}
