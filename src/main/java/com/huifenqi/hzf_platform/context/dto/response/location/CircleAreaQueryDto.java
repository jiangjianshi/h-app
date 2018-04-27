/** 
 * Project Name: hzf_platform_project 
 * File Name: CircleAreaQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response.location 
 * Date: 2016年5月7日下午6:19:09 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName: CircleAreaQueryDto
 * date: 2016年5月7日 下午6:19:09
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class CircleAreaQueryDto {
	
	private List<AreaInfo> circleAreas = new ArrayList<>();

	public List<AreaInfo> getCircleAreas() {
		return circleAreas;
	}

	public void setCircleAreas(List<AreaInfo> circleAreas) {
		this.circleAreas = circleAreas;
	}
	
	public void addCicleArea(AreaInfo areaInfo) {
		circleAreas.add(areaInfo);
	}
}
