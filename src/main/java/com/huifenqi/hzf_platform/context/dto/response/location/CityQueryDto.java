/** 
 * Project Name: hzf_platform_project 
 * File Name: CityQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月28日下午7:23:58 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CityQueryDto date: 2016年4月28日 下午7:23:58 Description:
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class CityQueryDto {

	private List<CityInfo> cities = new ArrayList<>();

	public void addCity(long cityId, String cityName, String center, int hasSubway) {
		this.cities.add(new CityInfo(cityId, cityName, center, hasSubway));
	}

	public List<CityInfo> getCities() {
		return cities;
	}

	public void setCities(List<CityInfo> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "CityQueryDto [cities=" + cities + "]";
	}

}
