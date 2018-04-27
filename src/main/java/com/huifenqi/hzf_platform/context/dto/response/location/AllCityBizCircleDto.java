package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

public class AllCityBizCircleDto {
	
	private long version;
	private int updateFlag;
	
	private List<CityBizCircleDto> citysBizCircles = new ArrayList<>();
	
	

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}

	public List<CityBizCircleDto> getCitysBizCircles() {
		return citysBizCircles;
	}

	public void addCitysBizCircles(CityBizCircleDto cityBizCircles) {
		citysBizCircles.add(cityBizCircles);
	}
	
}
