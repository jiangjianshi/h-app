package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.AszConstants;

public enum AszRentTypeEnum {

	SHARE("合租", 0, "SHARE"), ENTIRE("整租", 1, "ENTIRE"),
 BOTH("整分皆可", 2, "");
	

    private String  desc;
    private Integer code;
	private String aszCode;

	private AszRentTypeEnum(String desc, Integer code, String aszCode) {
        this.desc = desc;
        this.code = code;
		this.aszCode = aszCode;
    }
    
	public static Integer getCode(String aszCode) {
		if (aszCode == null) {
			throw new InvalidParameterException("parameter aszCode is null.");
        }
        for (AszRentTypeEnum o : AszRentTypeEnum.values()) {
			if (o.aszCode.equals(aszCode)) {
                return o.code;
            }
        }
		return AszConstants.HouseDetail.NOTFOUND;
    }
    

}
