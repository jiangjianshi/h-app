package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum RoomTypeEnum {

	MASTER("主卧", 1, 31),
	BUILD_SELF("主卧", 1, 42),
	
	
	SECONDARY("次卧", 10, 32),
	SMALL_MASTER("次卧", 10, 34),
	SOUTH_SECONDARY("次卧", 10, 37),
	NORTY_SECONDARY("次卧", 10, 38),
	
	
	BRIGHT_PARTION("隔间", 30, 35),
	DARK_PARTION("隔间", 30, 36),
	LIVING_SECONDARY("隔间", 30, 39),
	DINING_SECONDARY("隔间", 30, 40),
	KITCHEN_SECONDARY("隔间", 30, 41),
	
	// OPTIMIZED("优化间-不区分主次", 20, 33);
	OPTIMIZED("次卧", 20, 33);
	
//	SMALL_MASTER("小卧", 34, 34),
//	BRIGHT_PARTION("明隔", 35, 35),
//	DARK_PARTION("暗隔", 36, 36),
//	SOUTH_SECONDARY("南次卧", 37, 37),
//	NORTY_SECONDARY("北次卧", 38, 38),
//	LIVING_SECONDARY("客厅隔断", 39, 39),
//	DINING_SECONDARY("餐厅隔断", 40, 40),
//	KITCHEN_SECONDARY("厨房隔断", 41, 41),
//	BUILD_SELF("自建房", 42, 42)
	
    private String  desc;
    private Integer code;
    private Integer bdCode;

    private RoomTypeEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    public static Integer getOriginalCodeIfNull(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return bdCode;
    }
    
    
    public static Integer getBdCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    /**
     * 根据编码，获取房间类型描述
     * @param code
     * @return
     */
    public static String getRoomTypeDesc(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (RoomTypeEnum o : RoomTypeEnum.values()) {
            if(o.code.equals(code)){
                return o.desc;
            }
        }
        return "卧室";
    }
    
    /**
     * 根据编码，获取描述
     * @param code
     * @return
     */
    public static String getDesc(Integer code) {
    	if(code == null){
    		throw new InvalidParameterException("parameter code is null.");
    	}
    	for (RoomTypeEnum o : RoomTypeEnum.values()) {
    		if(o.code.equals(code)){
    			return o.desc;
    		}
    	}
    	return "未知";
    }
}
