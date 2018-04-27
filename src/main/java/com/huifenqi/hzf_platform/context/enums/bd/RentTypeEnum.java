package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum RentTypeEnum {

    SHARE("单间", 0, 2),
    ENTIRE("整租", 1, 1),
	BOTH("整分皆可", 2, 2);
	

    private String  desc;
    private Integer code;
    private Integer bdCode;

    private RentTypeEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (RentTypeEnum o : RentTypeEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    public static Integer getBdCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (RentTypeEnum o : RentTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
}
