package com.huifenqi.hzf_platform.context.enums.smartlock;

public enum GwModityPwdErrorCodeEnum {
	
	/*			03426035=租客不存在
				03430049=密码长度只能为6-10位
				03430057=新密码与旧密码相同
				03460204=原始密码错误
				03460205=密码过于相似，建议更换
				03430051=密码已过期*/
	SUCCESS("修改申请成功，请注意查收短信！","0"),
	RENTER_NOT_EXIST("租客不存在","0342603"),
	PWD_TOO_LONG("密码长度只能为6-10位", "03430049"), 
	SAME_AS_OLDPWED("新密码不能与原密码相同！","03430057"),
	OLD_PWD_ERROR("原密码格式不正确，请重新输入！","03460204"),
	OLD_PWD_ERROR_2("原密码格式不正确，请重新输入！","03460211"),
	PWD_SIMILAR("密码过于相似，请重新输入！","03460205"),
	PWD_OVERDUE("密码失效，无法修改！","03430051");
	
	private String desc;
	private String code;
	private GwModityPwdErrorCodeEnum(String desc, String code) {
		this.desc = desc;
		this.code = code;
	}
	
	public String getDesc() {
		return desc;
	}
	public String getCode() {
		return code;
	}
	
	public static String getDesc(String code) {
        if(code == null){
            return "";
        }
        for (GwModityPwdErrorCodeEnum o : GwModityPwdErrorCodeEnum.values()) {
            if(o.code.equals(code)){
                return o.desc;
            }
        }
        return "";
    }
	
}
