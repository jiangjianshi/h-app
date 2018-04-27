package com.huifenqi.hzf_platform.context.enums;

public enum ImgTypeEnums {

	ORIGINAL("全部", "_original"), 
	FULL("全部", "_full"), 
	PART("部分", "_part"),
	DEFAULT("默认", "_defalut"),
	ERECT("竖着", "_erect");

	private String desc;
	private String code;
	
	
	
	private ImgTypeEnums(String desc, String code) {
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
