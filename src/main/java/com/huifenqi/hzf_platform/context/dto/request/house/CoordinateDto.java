/** 
* Project Name: hzf_platform_project 
* File Name: HouseSearchDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.request.house 
* Date: 2016年5月6日下午8:05:56 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.request.house;

/**
 * ClassName: HouseSearchDto date: 2016年5月6日 下午8:05:56 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class CoordinateDto {
	
	//经度
	private String lng;
	
	//纬度
	private String lat;

	// 位置的附加信息，是否精确查找。1为精确查找，即准确打点；0为不精确，即模糊打点。
	private int precise;
	// 可信度，描述打点准确度
	private int confidence;
	// 地址类型
	private String level;

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
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

	@Override
	public String toString() {
		return "CoordinateDto [lng=" + lng + ", lat=" + lat + ", precise=" + precise + ", confidence=" + confidence
				+ ", level=" + level + "]";
	}
	
}
