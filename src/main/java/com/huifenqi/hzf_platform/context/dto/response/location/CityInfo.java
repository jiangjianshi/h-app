package com.huifenqi.hzf_platform.context.dto.response.location;

/**
 * Created by HFQ-Arison on 2017/8/14.
 */
public class CityInfo {

    private long cityId;

    private String cityName;
    
    private String center;
    
    private int hasSubway;//是否开通地铁

    
    	
    public CityInfo() {
		super();
	}

	public CityInfo(long cityId, String cityName, String center, int hasSubway) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.center = center;
		this.hasSubway = hasSubway;
	}

	public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getHasSubway() {
		return hasSubway;
	}

	public void setHasSubway(int hasSubway) {
		this.hasSubway = hasSubway;
	}

	@Override
	public String toString() {
		return "CityInfo [cityId=" + cityId + ", cityName=" + cityName + ", center=" + center + ", hasSubway="
				+ hasSubway + "]";
	}

	
}