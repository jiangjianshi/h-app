/** 
* Project Name: hzf_platform_project 
* File Name: HouseRecommendQueryInfo.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
* Date: 2016年5月9日下午8:53:48 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: HouseRecommendQueryDto date: 2016年5月9日 下午8:53:48 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseRecommendQueryDto {

	private List<HouseRecommendInfo> recommendHouses = new ArrayList<HouseRecommendInfo>();

	public List<HouseRecommendInfo> getHouseRecommends() {
		return recommendHouses;
	}

	public void setHouseRecommends(List<HouseRecommendInfo> recommendHouses) {
		this.recommendHouses = recommendHouses;
	}

	public void addHouseRecommend(HouseRecommendInfo houseRecommendInfo) {
		recommendHouses.add(houseRecommendInfo);
	}

}
