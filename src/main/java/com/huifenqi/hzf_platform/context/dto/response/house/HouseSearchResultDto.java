/** 
* Project Name: hzf_platform_project 
* File Name: HouseSearchResultDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
* Date: 2016年5月11日下午3:08:18 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: HouseSearchResultDto date: 2016年5月11日 下午3:08:18 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseSearchResultDto {

	private List<HouseSearchResultInfo> searchHouses = new ArrayList<HouseSearchResultInfo>();

	public List<HouseSearchResultInfo> getSearchHouses() {
		return searchHouses;
	}

	public void setSearchHouses(List<HouseSearchResultInfo> searchHouses) {
		this.searchHouses = searchHouses;
	}

	public void addHouseRecommend(HouseSearchResultInfo houseSearchResultInfo) {
		searchHouses.add(houseSearchResultInfo);
	}

}
