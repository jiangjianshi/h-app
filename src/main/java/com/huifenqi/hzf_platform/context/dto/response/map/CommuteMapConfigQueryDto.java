/** 
 * Project Name: hzf_platform_project 
 * File Name: SubwayQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月27日下午8:26:17 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.dto.response.map;

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName: CommuteMapConfigQueryDto
 * date: 2016年4月27日 下午8:26:17
 * Description: 通勤配置数据传输对象
 * 
 * @author changmingwei 
 * @version  
 * @since JDK 1.8 
 */
public class CommuteMapConfigQueryDto {
	
	
	private List<CommuteMapConfigDto> commuteConfigs = new ArrayList<>();

	public List<CommuteMapConfigDto> getCommuteConfigs() {
		return commuteConfigs;
	}

	public void setCommuteConfigs(List<CommuteMapConfigDto> commuteConfigs) {
		this.commuteConfigs = commuteConfigs;
	}

	public void addDto(CommuteMapConfigDto dto) {
		commuteConfigs.add(dto);
	}

	@Override
	public String toString() {
		return "CommuteMapConfigQueryDto [commuteConfigs=" + commuteConfigs + ", getCommuteConfigs()="
				+ getCommuteConfigs() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
