package com.huifenqi.hzf_platform.context.enums;

public enum UpdateFlagEnum {
	
	
	UPDATE("更新", 0), 
	NOT_UPDATE("不更新", 1);
	
	private String desc;
	private Integer code;
	
	private UpdateFlagEnum(String desc, Integer code) {
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
