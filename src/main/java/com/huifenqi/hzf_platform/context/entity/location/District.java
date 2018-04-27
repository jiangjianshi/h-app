/** 
* Project Name: hzf_platform 
* File Name: District.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:25:35 
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
 * ClassName: District date: 2016年4月25日 下午4:25:35 Description: 行政区
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_district")
public class District {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long districtId;

	private long provinceId;

	private long cityId;

	private String name;

	private int sort;

	private int status;
	
	private String center;

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
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
		return "District [districtId=" + districtId + ", provinceId=" + provinceId + ", cityId=" + cityId + ", name="
				+ name + ", sort=" + sort + ", status=" + status + ", center=" + center + "]";
	}
}
