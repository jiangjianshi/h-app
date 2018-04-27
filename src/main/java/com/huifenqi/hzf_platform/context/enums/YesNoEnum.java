package com.huifenqi.hzf_platform.context.enums;

public enum YesNoEnum {
	
	
	YES("是", 1), 
	No("否", 0);

	private String desc;
	private int code;
	
	private YesNoEnum(String desc, int code) {
		this.desc = desc;
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public int getCode() {
		return code;
	}

	
}
