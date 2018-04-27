/** 
 * Project Name: hzf_platform_project 
 * File Name: BussinessCircleQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月28日下午5:44:42 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName: BussinessCircleQueryDto
 * date: 2016年4月28日 下午5:44:42
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class BussinessCircleQueryDto {
	
	private long version;
	
	private int updateFag;
	
	private List<BusinessCircle> businessCircles = new ArrayList<>();

	public List<BusinessCircle> getBusinessCircles() {
		return businessCircles;
	}

	public void setBusinessCircles(List<BusinessCircle> businessCircles) {
		this.businessCircles = businessCircles;
	}
	
	public void addBusinessCircle(BusinessCircle businessCircle) {
		this.businessCircles.add(businessCircle);
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
		return "BussinessCircleQueryDto [version=" + version + ", updateFag=" + updateFag + ", businessCircles="
				+ businessCircles + "]";
	}
}
