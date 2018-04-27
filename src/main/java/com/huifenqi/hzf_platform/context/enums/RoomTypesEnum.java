package com.huifenqi.hzf_platform.context.enums;

public enum RoomTypesEnum {

	MASTER("主卧", 1),
	SECONDARY("次卧", 10),
	OPTIMIZED("优化间", 20),
	PARTION("隔断", 30);
	
    private String  desc;
    private Integer code;
    
    
	private RoomTypesEnum(String desc, Integer code) {
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
