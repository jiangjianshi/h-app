/** 
* Project Name: hzf_platform 
* File Name: Area.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:25:55 
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
 * ClassName: Area date: 2016年4月25日 下午4:25:55 Description:商圈
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_area")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long areaId;

	private long cityId;

	private long districtId;

	private String name;
	
	private String center;

	private int sort;

	private int status;

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	@Override
	public String toString() {
		return "Area [areaId=" + areaId + ", cityId=" + cityId + ", districtId=" + districtId + ", name=" + name
				+ ", center=" + center + ", sort=" + sort + ", status=" + status + "]";
	}


}
