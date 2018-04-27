/** 
* Project Name: hzf_platform_project 
* File Name: SubwayStationDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.response.location 
* Date: 2016年5月5日下午6:15:18 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.response.location;

/**
 * ClassName: SubwayStationDto date: 2016年5月5日 下午6:15:18 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class SubwayStationDto {

	private long stationId;

	private String stationName;

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public String toString() {
		return "SubwayStationDto [stationId=" + stationId + ", name=" + stationName + "]";
	}

}
