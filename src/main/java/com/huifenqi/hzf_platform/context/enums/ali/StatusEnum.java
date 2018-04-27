package com.huifenqi.hzf_platform.context.enums.ali;

import java.security.InvalidParameterException;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.Constants;

public enum StatusEnum {

    WAIT("待出租", 1L, 0L),
    RENTED("已出租", 5L, 1L),
    INVALID("未上架", 0L, 1L);

    private String  desc;
    private Long code;
    private Long aliCode;

    private StatusEnum(String desc, Long code, Long aliCode) {
        this.desc = desc;
        this.code = code;
        this.aliCode = aliCode;
    }
    
    public static Long getCode(Long aliCode) {
        Preconditions.checkArgument( aliCode != null, "parameter aliCode is null." );

        for (StatusEnum o : StatusEnum.values()) {
            if(o.aliCode.equals(aliCode)){
                return o.code;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
    
    public static Long getAliCode(Long code) {
        Preconditions.checkArgument( code != null, "parameter aliCode is null." );
        for (StatusEnum o : StatusEnum.values()) {
            if(o.code.equals(code)){
                return o.aliCode;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
}
