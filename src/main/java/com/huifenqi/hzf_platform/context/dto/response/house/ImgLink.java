package com.huifenqi.hzf_platform.context.dto.response.house;

public class ImgLink {
	
	private String smallImgUrl;
	private String bigImgUrl;
	
	public String getSmallImgUrl() {
		return smallImgUrl;
	}
	public void setSmallImgUrl(String smallImgUrl) {
		this.smallImgUrl = smallImgUrl;
	}
	public String getBigImgUrl() {
		return bigImgUrl;
	}
	public void setBigImgUrl(String bigImgUrl) {
		this.bigImgUrl = bigImgUrl;
	}
	
	@Override
	public String toString() {
		return "ImgLink [smallImgUrl=" + smallImgUrl + ", bigImgUrl=" + bigImgUrl + "]";
	}
	
	
}
