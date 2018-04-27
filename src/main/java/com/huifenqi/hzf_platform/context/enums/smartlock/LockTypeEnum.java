package com.huifenqi.hzf_platform.context.enums.smartlock;

public enum LockTypeEnum {
	
	
	ALL("所有",0),
	BLUETOOTH("蓝牙锁", 1), 
	GATEWAY("网关所",2);
	
	private String desc;
	private Integer code;
	
	private LockTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	
}
