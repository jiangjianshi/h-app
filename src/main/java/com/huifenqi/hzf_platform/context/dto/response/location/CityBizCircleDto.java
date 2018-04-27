package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

public class CityBizCircleDto {
	
	
	private Long cityId;
	
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

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	
}
