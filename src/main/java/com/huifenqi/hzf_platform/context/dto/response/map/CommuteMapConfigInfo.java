package com.huifenqi.hzf_platform.context.dto.response.map;

/**
 * Created by HFQ-changmingwei on 2017/11/08.
 */
public class CommuteMapConfigInfo {

    private long commuteId;

    private String travelTypeName;
        
    private String timeName;

    private String scale;

    public CommuteMapConfigInfo() {

    }
    public CommuteMapConfigInfo(long commuteId, String timeName,String scale) {
        this.commuteId = commuteId;
        this.timeName = timeName;
        this.scale = scale;
    }

	public long getCommuteId() {
		return commuteId;
	}

	public void setCommuteId(long commuteId) {
		this.commuteId = commuteId;
	}

	public String getTravelTypeName() {
		return travelTypeName;
	}

	public void setTravelTypeName(String travelTypeName) {
		this.travelTypeName = travelTypeName; 
    }

    

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

  
}