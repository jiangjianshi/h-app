package com.huifenqi.hzf_platform.context.enums;

/**
 * 虚拟号分配状态
 * @author jjs
 *
 */
public enum SecretNoAssignStatus {
	
	ASSIGNED("已分配", 0), 
	PHONE_NOT_ALLOW("号码不支持", 1), 
	SECRETNO_NOT_ENOUGH("虚拟号不够用", 2),
	BIND_FAIL("虚拟号绑定失败",3);

	private String desc;
	private Integer code;
	
	private SecretNoAssignStatus(String desc, Integer code) {
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
