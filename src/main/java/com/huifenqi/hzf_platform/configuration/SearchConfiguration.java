/** 
* Project Name: hzf_platform_project 
* File Name: SearchConfiguration.java 
* Package Name: com.huifenqi.hzf_platform.configuration 
* Date: 2016年5月19日下午8:34:19 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: SearchConfiguration date: 2016年5月19日 下午8:34:19 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Component
public class SearchConfiguration {

	@Value("${hfq.search.distance.nearby}")
	private int nearybyDistance;

	@Value("${hfq.search.distance.station}")
	private int stationDistance;

	@Value("${hfq.search.agencyid}")
	private String agencyids;

	public int getNearybyDistance() {
		return nearybyDistance;
	}

	public void setNearybyDistance(int nearybyDistance) {
		this.nearybyDistance = nearybyDistance;
	}

	public int getStationDistance() {
		return stationDistance;
	}

	public void setStationDistance(int stationDistance) {
		this.stationDistance = stationDistance;
	}

	public String getAgencyids() {
		return agencyids;
	}

	public void setAgencyids(String agencyids) {
		this.agencyids = agencyids;
	}
}
