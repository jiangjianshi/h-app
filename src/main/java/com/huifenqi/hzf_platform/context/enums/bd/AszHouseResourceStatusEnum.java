package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.AszConstants;

public enum AszHouseResourceStatusEnum {

//	WAITING_RENT("待出租", 2, "WAITING_RENT"), RENTED("已出租", 3, "RENTED"), BOOKED("已下架", 0, "BOOKED"), INVALID("已下架", 0,
//			"INVALID");
	
	//上架=待出租   下架=已出租
	ONLINE("待出租", 2, "ONLINE"), OFFLINE("已出租", 3, "OFFLINE");

    private String  desc;
	private Integer code;
	private String aszCode;

	private AszHouseResourceStatusEnum(String desc, Integer code, String aszCode) {
        this.desc = desc;
        this.code = code;
        this.aszCode = aszCode;
    }
    
	// 爱上租房态转换找房
    public static Integer getCode(String aszCode) {
        if(aszCode == null){
            throw new InvalidParameterException("parameter aszCode is null.");
        }
        for (AszHouseResourceStatusEnum o : AszHouseResourceStatusEnum.values()) {
            if(o.aszCode.equals(aszCode)){
                return o.code;
            }
        }
		return AszConstants.HouseDetail.NOTFOUND;
    }
}
