package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum BuildTypeEnum {

    BOARD("板楼", 1, 72),
    TOWER("塔楼", 2, 71),
    PENDING("待定", 0, 73);

    private String  desc;
    private Integer code;
    private Integer bdCode;

    private BuildTypeEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (BuildTypeEnum o : BuildTypeEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    public static Integer getBCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (BuildTypeEnum o : BuildTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
}
