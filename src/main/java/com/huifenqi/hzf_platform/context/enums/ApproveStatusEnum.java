package com.huifenqi.hzf_platform.context.enums;

public enum ApproveStatusEnum {
	
	WAITTING_APPROVE("待审核",0),
	SYS_APP_PASS("程序审核通过", 1),
	SYS_APP_REJECT("程序审核不通过",2),
	IMG_APP_PASS("图片审核通过",3),
	IMG_APP_REJECT("图片审核不通过",4),
	IMG_APP_TEMP("图片审核临时状态",10);
	
	
	
	
	private String desc;
	private Integer code;
	
	private ApproveStatusEnum(String desc, Integer code) {
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
