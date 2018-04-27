package com.huifenqi.hzf_platform.context.enums;

public enum HouseDataErrorEnums {
	
	PRICE("价格不合理",1),
	BEDROOM("居室为0", 2),
	PAY("付几为0",3),
	RATE("房源均价小于30",4),
	ROOM_AREA("分租房源房间面积大于60或者小于5",5),
	DONGHUAMEN("东华门的房源",6),
	PRICE_HOUSE("整租价格小于300或者大于30000",7),
	PRICE_ROOM("合租价格小于100或者大于10000",8),
	COMPANY_CITY("该中介公司未开通该城市",9),
    HOUSE_AREA("整租房源面积小于5",10),
    HOUSE_FLOW_NO("当前楼层不能大于100",11),
    HOUSE_FLOW_TOTAL("总楼层不能大于100",12),
    COMPANYNAME("公司名称不能包含<测试><演示>字段",13),
    BIG_AREA("面积大于500",14);
    
	
	
	private String desc;
	private Integer code;
	
	private HouseDataErrorEnums(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

	public String getDesc() {
		return desc;
	}

	public Integer getCode() {
		return code;
	}
	
}
