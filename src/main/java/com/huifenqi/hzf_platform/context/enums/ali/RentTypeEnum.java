package com.huifenqi.hzf_platform.context.enums.ali;

import java.security.InvalidParameterException;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.Constants;

public enum RentTypeEnum {

    SHARE("合租", 0L, 2L),
    ENTIRE("整租", 1L, 1L),
	BOTH("整分皆可", 2L, 2L);
	

    private String  desc;
    private Long code;
    private Long aliCode;

    private RentTypeEnum(String desc, Long code, Long aliCode) {
        this.desc = desc;
        this.code = code;
        this.aliCode = aliCode;
    }
    
    public static Long getCode(Integer aliCode) {
        Preconditions.checkArgument( aliCode != null, "parameter bdCode is null." );

        for (RentTypeEnum o : RentTypeEnum.values()) {
            if(o.aliCode.equals(aliCode)){
                return o.code;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
    
    public static Long getAliCode(Long code) {
        Preconditions.checkArgument( code != null, "parameter code is null." );

        for (RentTypeEnum o : RentTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.aliCode;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
}
