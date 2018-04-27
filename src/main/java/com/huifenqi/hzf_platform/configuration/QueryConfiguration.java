/** 
* Project Name: hzf_platform_project 
* File Name: QueryConfiguration.java 
* Package Name: com.huifenqi.hzf_platform.configuration 
* Date: 2016年5月30日下午5:24:46 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: QueryConfiguration date: 2016年5月30日 下午5:24:46 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Component
public class QueryConfiguration {

	/**
	 * 热门公寓限制数量
	 */
	@Value("${hfq.query.house.apartment.hot.limit}")
	private int hotApartmentLimit;

	/**
	 * 热门商圈限制数量
	 */
	@Value("${hfq.query.location.area.hot.limit}")
	private int hotAreaLimit;

	/**
	 * 推荐房源限制数量
	 */
	@Value("${hfq.query.house.recommend.limit}")
	private int recommendHouseLimit;

	public int getHotApartmentLimit() {
		return hotApartmentLimit;
	}

	public void setHotApartmentLimit(int hotApartmentLimit) {
		this.hotApartmentLimit = hotApartmentLimit;
	}

	public int getHotAreaLimit() {
		return hotAreaLimit;
	}

	public void setHotAreaLimit(int hotAreaLimit) {
		this.hotAreaLimit = hotAreaLimit;
	}

	public int getRecommendHouseLimit() {
		return recommendHouseLimit;
	}

	public void setRecommendHouseLimit(int recommendHouseLimit) {
		this.recommendHouseLimit = recommendHouseLimit;
	}

}
