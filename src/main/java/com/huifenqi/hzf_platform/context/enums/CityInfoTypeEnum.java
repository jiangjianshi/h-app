package com.huifenqi.hzf_platform.context.enums;

public enum CityInfoTypeEnum {
	
	
	subway("地铁", 1), 
	bizCircle("商圈", 2);
	
	private String desc;
	private Integer code;
	
	private CityInfoTypeEnum(String desc, Integer code) {
		this.desc = desc;
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getCode() {
		return code;
	}
	
}
