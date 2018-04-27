package com.huifenqi.hzf_platform.context.enums;

import java.security.InvalidParameterException;

/**
 * 房屋标签
 * @author jjs
 *
 */
public enum HouseTagsEnum {
	
//	FIRST_TIME("首次发布", 1),
	ORIENTATION_SOUTH("南向", 2),
	INDEPENDENT_TOILET("独立卫生间", 3),
	INDEPENDENT_BALCONY("独立阳台", 4),
	FINE_DECORATION("精装修", 5),
	CLOSE_TO_SUBWAY("近地铁", 6),
//	FACILITIES_WELL_EQUIPPED("设施齐全", 7),
//	CENTRAL_HEATING("集中供暖", 8),
	PERIOD_MONTH_ONE("押一付一", 9),
//	INDEPENDENT_HEATING("独立供暖", 10),
//	SMART_LOCK("智能门锁", 11),
	COMPANY_PAY_STATUS("支持月付", 12),
	MASTER_ROOM("主卧", 13);
	
	
    private String  desc;
    private Integer code;
    
    
	private HouseTagsEnum(String desc, Integer code) {
		this.desc = desc;
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}


	public Integer getCode() {
		return code;
	}
    
	public static String getDesc(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (HouseTagsEnum o : HouseTagsEnum.values()) {
            if(o.code.equals(code)){
                return o.desc;
            }
        }
        return "";
    }
	
}
