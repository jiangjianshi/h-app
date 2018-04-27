package com.huifenqi.hzf_platform.context.dto.response.location;

import java.util.ArrayList;
import java.util.List;

public class AllCitySubwayDto {
	
	
	private long version;
	private int updateFag;
	
	private List<CitySubwayDto> citysSubway = new ArrayList<>();
	
	

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getUpdateFag() {
		return updateFag;
	}

	public void setUpdateFag(int updateFag) {
		this.updateFag = updateFag;
	}

	public List<CitySubwayDto> getCitysSubway() {
		return citysSubway;
	}

	public void addCitysSubway(CitySubwayDto citySubway) {
		citysSubway.add(citySubway);
	}
	
	
	
}
