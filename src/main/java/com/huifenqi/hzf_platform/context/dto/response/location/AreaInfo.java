/** 
 * Project Name: hzf_platform_project 
 * File Name: AreaInfo.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response.location 
 * Date: 2016年5月7日下午6:29:15 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.dto.response.location;

/** 
 * ClassName: AreaInfo
 * date: 2016年5月7日 下午6:29:15
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class AreaInfo {

	private long areaId;

	private String areaName;

	public AreaInfo() {

	}

	public AreaInfo(long areaId, String areaName) {
		this.areaId = areaId;
		this.areaName = areaName;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Override
	public String toString() {
		return "areaInfo [areaId=" + areaId + ", areaName=" + areaName + "]";
	}

}
