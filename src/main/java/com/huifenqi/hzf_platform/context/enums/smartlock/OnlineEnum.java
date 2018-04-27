package com.huifenqi.hzf_platform.context.enums.smartlock;

public enum OnlineEnum {
	
	
	OFFLINE("离线","0"),
	ONLINE("在线","1");
	
	private String desc;
	private String code;
	
	private OnlineEnum(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

	
	
}
