/** 
* Project Name: hzf_platform 
* File Name: City.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:25:27 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName: City date: 2016年4月25日 下午4:25:27 Description: 城市
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_city")
public class City {

	@Id
	@Column(name = "city_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cityId;

	@Column(name = "province_id")
	private long provinceId;

	@Column(name = "name")
	private String name;

	@Column(name = "sort")
	private int sort;

	@Column(name = "center")
	private String center;

	@Column(name = "has_subway")
	private int hasSubway; // 是否开通地铁

	@Column(name = "status")
	private int status;

	@Column(name = "ismap")
	private int ismap;

	@Column(name = "for_count")
	private int forCount;

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
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

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsmap() {
		return ismap;
	}

	public void setIsmap(int ismap) {
		this.ismap = ismap;
	}

	public int getForCount() {
		return forCount;
	}

	public void setForCount(int forCount) {
		this.forCount = forCount;
	}

	public int getHasSubway() {
		return hasSubway;
	}

	public void setHasSubway(int hasSubway) {
		this.hasSubway = hasSubway;
	}

	@Override
	public String toString() {
		return "City [cityId=" + cityId + ", provinceId=" + provinceId + ", name=" + name + ", sort=" + sort
				+ ", center=" + center + ", hasSubway=" + hasSubway + ", status=" + status + ", ismap=" + ismap
				+ ", forCount=" + forCount + "]";
	}

}
