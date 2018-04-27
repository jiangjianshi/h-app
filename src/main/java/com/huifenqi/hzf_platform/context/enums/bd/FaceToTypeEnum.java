package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum FaceToTypeEnum {

    EAST("东", 10001, 60),
    WEST("西", 10002, 62),
    SOUTH("南", 10003, 61),
    NORTH("北", 10004, 63),
    SOUTHWEST("西南", 10023, 65),
    NORTHWEST("西北", 10024, 67),
    NORTHEAST("东北", 10014, 66),
    SOUTHEAST("东南", 10013, 64),
    NORTH_SOUTH("南北", 10034, 69),
    WEST_EAST("东西", 10012, 68);

    private String  desc;
    private Integer code;
    private Integer bdCode;

    private FaceToTypeEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    public static String getCodeByDesc(String desc) {
        if(desc == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (FaceToTypeEnum o : FaceToTypeEnum.values()) {
            if(o.desc.equals(desc)){
                return String.valueOf(o.code);
            }
        }
        return String.valueOf(Constants.BdHouseDetail.NOTFOUND);
    }
    
    
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (FaceToTypeEnum o : FaceToTypeEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    public static Integer getBdCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (FaceToTypeEnum o : FaceToTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
}
