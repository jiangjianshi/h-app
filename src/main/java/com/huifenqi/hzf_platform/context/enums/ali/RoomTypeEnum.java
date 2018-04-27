package com.huifenqi.hzf_platform.context.enums.ali;

import java.security.InvalidParameterException;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.Constants;

public enum RoomTypeEnum {

    
    MASTER("主卧", 1L, 3L),
    SMALL_MASTER("次卧", 10L, 2L),
    NOFUND("次卧", 0L, 2L),
    LIVING_SECONDARY("隔间", 30L, 1L),
    OPTIMIZED("优化间", 20L, 1L);
	

    private String  desc;
    private Long code;
    private Long aliCode;

    private RoomTypeEnum(String desc, Long code, Long aliCode) {
        this.desc = desc;
        this.code = code;
        this.aliCode = aliCode;
    }
    
    public static Long getCode(Integer aliCode) {
        Preconditions.checkArgument( aliCode != null, "parameter aliCode is null." );

        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.aliCode.equals(aliCode)){
                return o.code;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
    
    public static Long getAliCode(Long code) {
        Preconditions.checkArgument( code != null, "parameter code is null." );

        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.aliCode;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
}
