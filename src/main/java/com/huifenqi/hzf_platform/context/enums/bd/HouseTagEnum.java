package com.huifenqi.hzf_platform.context.enums.bd;

import java.security.InvalidParameterException;

import com.huifenqi.hzf_platform.context.Constants;

public enum HouseTagEnum {
	// ------------------房屋标签-----------------
	CLOSE_TO_SUBWAY("地铁周边", 6, 10),
	INDEPENDENT_BALCONY("独立阳台", 4, 11),
	INDEPENDENT_TOILET("独立卫生间", 3, 12),
	FIRST_TIME("首次发布", 1, 14),
	ORIENTATION_SOUTH("南向", 2, 15),
	PERIOD_MONTH_ONE("月付", 9, 17);
//	FACILITIES_WELL_EQUIPPED("设施齐全", 7, -1),
//	DECORATION_FINE("将装修", 5, -1),
//	CENTRAL_HEATING ("集中供暖", 8, -1);
	
    private String  desc;
    private Integer code;
    private Integer bdCode;

    private HouseTagEnum(String desc, Integer code, Integer bdCode) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
    }
    
    public static Integer getCode(Integer bdCode) {
        if(bdCode == null){
            throw new InvalidParameterException("parameter bdCode is null.");
        }
        for (HouseTagEnum o : HouseTagEnum.values()) {
            if(o.bdCode.equals(bdCode)){
                return o.code;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    
    public static Integer getBdCode(Integer code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (HouseTagEnum o : HouseTagEnum.values()) {
            if(o.code.equals(code)){
                return o.bdCode;
            }
        }
        return Constants.BdHouseDetail.NOTFOUND;
    }
    

    public static String getBdCodes(String setttings) {
        if (setttings == null && "".equals(setttings)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String[] arr = setttings.split(",");
        for (String setting : arr) {
        	Integer temp = HouseTagEnum.getBdCode(Integer.valueOf(setting));
            if (!"".equals(temp)) {
                sb.append(temp).append(",");
            }
        }
        String sets = sb.substring(0, sb.length() - 1);
        return sets;
    }
}
