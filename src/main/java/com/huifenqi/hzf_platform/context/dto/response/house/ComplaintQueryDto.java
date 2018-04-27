/** 
 * Project Name: hzf_platform_project 
 * File Name: ComplaintQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
 * Date: 2016年4月29日上午10:28:05 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.List;

/**
 * ClassName: ComplaintQueryDto date: 2016年4月29日 上午10:28:05 Description: 投诉查询Dto
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class ComplaintQueryDto {

	private List<ComplaintInfo> complaints;

	public List<ComplaintInfo> getComplaints() {
		return complaints;
	}

	public void setComplaints(List<ComplaintInfo> complaints) {
		this.complaints = complaints;
	}

}
