/** 
 * Project Name: hzf_platform_project 
 * File Name: SubwayLineDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月28日上午11:08:08 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.map;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CommuteMapConfigDto date: 2016年4月28日 上午11:08:08 Description: 通勤配置信息
 * 
 * @author CHANGMINGWEI
 * 
 * @version
 * @since JDK 1.8
 */
public class CommuteMapConfigDto {

	private String travelType;
	private String travelTypeName;
	private long defaultCommuteIndex;

	private List<CommuteMapConfigInfo> commuteConfig = new ArrayList<CommuteMapConfigInfo>();

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

	

	public long getDefaultCommuteIndex() {
		return defaultCommuteIndex;
	}

	public void setDefaultCommuteIndex(long defaultCommuteIndex) {
		this.defaultCommuteIndex = defaultCommuteIndex;
	}

	public List<CommuteMapConfigInfo> getCommuteConfig() {
		return commuteConfig;
	}

	public void setCommuteConfig(List<CommuteMapConfigInfo> commuteConfig) {
		this.commuteConfig = commuteConfig;
	}



}
