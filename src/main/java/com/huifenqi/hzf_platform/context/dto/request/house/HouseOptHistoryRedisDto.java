/** 
 * Project Name: hzf_platform 
 * File Name: HouseDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto 
 * Date: 2016年4月26日下午7:52:07 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.request.house;


/**
 * ClassName: HouseDto date: 2016年4月26日 下午7:52:07 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class HouseOptHistoryRedisDto {
  
	
	/**
     * 房源编号
     */
    private String sellId;

    /**
     * 房间ID
     */
    private long roomId;
    
	/**
	 * 操作类型
	 */
	private int optType;

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }
	

}
