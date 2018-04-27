/** 
 * Project Name: hzf_platform_project 
 * File Name: BusinessCircleDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月28日下午5:27:35 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BusinessCircleDto date: 2016年4月28日 下午5:27:35 Description:
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class BusinessCircle {

	private long districtId;

	private String districtName;

	private List<AreaInfo> areaInfos = new ArrayList<>();

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public List<AreaInfo> getAreaInfos() {
		return areaInfos;
	}

	public void setAreaInfos(List<AreaInfo> areaInfos) {
		this.areaInfos = areaInfos;
	}

	public void addAreaInfo(AreaInfo areaInfo) {
		this.areaInfos.add(areaInfo);
	}

	public void addAreaInfo(long areaId, String areaName) {
		this.areaInfos.add(new AreaInfo(areaId, areaName));
	}


	@Override
	public String toString() {
		return "BusinessCircle [districtId=" + districtId + ", districtName=" + districtName + ", areaInfos="
				+ areaInfos + "]";
	}

}
