package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

public class CitySubwayDto {

	Long cityId;
	
	private List<SubwayLineDto> subways = new ArrayList<>();

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public List<SubwayLineDto> getSubways() {
		return subways;
	}

	public void addSubways(SubwayLineDto subway) {
		subways.add(subway);
	}

}
