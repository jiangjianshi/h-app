package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum HouseResourceStatusEnum {

    NO_LISTING("未上架", 2, 4000),
    UnShelve("已下架", 3, 5000);

    private String  desc;
    private Integer code;
    private Integer bdCode;

    private HouseResourceStatusEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    //百度转换找房
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (HouseResourceStatusEnum o : HouseResourceStatusEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    //找房转换百度
    public static Integer getBdCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (HouseResourceStatusEnum o : HouseResourceStatusEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
}
