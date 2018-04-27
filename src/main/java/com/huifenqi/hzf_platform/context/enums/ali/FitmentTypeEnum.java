package com.huifenqi.hzf_platform.context.enums.ali;

import java.security.InvalidParameterException;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.Constants;

public enum FitmentTypeEnum {

    JING("精装", 1L, 2L),
    JIAN("简装", 2L, 3L),
    MAO("毛坯", 3L, 4L),
    LAO("老旧", 4L, 3L),
    HAO("豪装", 5L, 1L),
    ZHONG("中装", 6L, 2L),
    PU("普装", 7L, 3L),
    NOFUND("未知", 0L, 3L);
	

    private String  desc;
    private Long code;
    private Long aliCode;

    private FitmentTypeEnum(String desc, Long code, Long aliCode) {
        this.desc = desc;
        this.code = code;
        this.aliCode = aliCode;
    }
    
    public static Long getCode(Integer aliCode) {
        Preconditions.checkArgument( aliCode != null, "parameter aliCode is null." );

        for (FitmentTypeEnum o : FitmentTypeEnum.values()) {
            if(o.aliCode.equals(aliCode)){
                return o.code;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
    
    public static Long getAliCode(Long code) {
        Preconditions.checkArgument( code != null, "parameter code is null." );

        for (FitmentTypeEnum o : FitmentTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.aliCode;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
}
