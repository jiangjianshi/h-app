package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.Constants;

public enum AszFaceToTypeEnum {

	EAST("东", 10001, "EAST"),
	WEST("西", 10002, "WAST"), 
	SOUTH("南", 10003, "SOURTH"), 
	NORTH("北", 10004,"NORTH"), 
	SOUTHWEST("西南", 10023, "WEST_SOURTH"),
	NORTHWEST("西北", 10024, "WEST_NORTT"),
	NORTHEAST("东北",10014, "EAST_NORTH"), 
	SOUTHEAST("东南", 10013, "EAST_SOURCE"), 
	NORTH_SOUTH("南北", 10034,"SOURCE_NORTH"), 
	WEST_EAST("东西", 10012, "EAST_WEST");

    private String  desc;
    private Integer code;
	private String aszCode;

	private AszFaceToTypeEnum(String desc, Integer code, String aszCode) {
        this.desc = desc;
        this.code = code;
		this.aszCode = aszCode;
    }
    
    public static Integer getCodeByDesc(String desc) {
        if(desc == null){
			throw new InvalidParameterException("parameter desc is null.");
        }
        for (AszFaceToTypeEnum o : AszFaceToTypeEnum.values()) {
            if(o.desc.equals(desc)){
                return o.code;
            }
        }
        return AszConstants.HouseDetail.NOTFOUND;
    }
    
    
	public static Integer getCode(String aszCode) {
		if (aszCode == null) {
			throw new InvalidParameterException("parameter aszCode is null.");
        }
        for (AszFaceToTypeEnum o : AszFaceToTypeEnum.values()) {
			if (o.aszCode.equals(aszCode)) {
                return o.code;
            }
        }
		return AszConstants.HouseDetail.NOTFOUND;
    }
    
}
