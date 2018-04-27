/** 
* Project Name: hzf_platform 
* File Name: Province.java 
* Package Name: com.huifenqi.hzf_platform.context.location 
* Date: 2016年4月25日下午4:25:19 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName: Province date: 2016年4月25日 下午4:25:19 Description: 省份
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_province")
public class Province {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long provinceId;

	private String name;

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
