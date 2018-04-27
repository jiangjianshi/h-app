/** 
 * Project Name: hzf_platform_project 
 * File Name: SubwayLineDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月28日上午11:08:08 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: SubwayLineDto date: 2016年4月28日 上午11:08:08 Description: 地铁线信息
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class SubwayLineDto {

	private int lineId;

	private String lineName;

	private List<SubwayStationDto> subwayStations = new ArrayList<SubwayStationDto>();

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public List<SubwayStationDto> getSubwayStations() {
		return subwayStations;
	}

	public void setSubwayStations(List<SubwayStationDto> subwayStations) {
		this.subwayStations = subwayStations;
	}

	public void addSubwayStation(SubwayStationDto subwayStation) {
		subwayStations.add(subwayStation);
	}

	@Override
	public String toString() {
		return "SubwayLineDto [lineId=" + lineId + ", lineName=" + lineName + ", subwayStations=" + subwayStations
				+ "]";
	}

}
