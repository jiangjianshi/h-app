/** 
 * Project Name: hzf_platform_project 
 * File Name: SubwayQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月27日下午8:26:17 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName: SubwayQueryDto
 * date: 2016年4月27日 下午8:26:17
 * Description: 地铁数据传输对象
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class SubwayQueryDto {
	
	private long version;
	
	private int updateFag;
	
	private List<SubwayLineDto> subways = new ArrayList<>();

	public List<SubwayLineDto> getSubways() {
		return subways;
	}

	public void setSubways(List<SubwayLineDto> subways) {
		this.subways = subways;
	}
	
	public void addSubway(SubwayLineDto subway) {
		subways.add(subway);
	}
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getUpdateFag() {
		return updateFag;
	}

	public void setUpdateFag(int updateFag) {
		this.updateFag = updateFag;
	}

	@Override
	public String toString() {
		return "SubwayQueryDto [version=" + version + ", updateFag=" + updateFag + ", subways=" + subways + "]";
	}
}
