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
public class SaasHousePublishDto {

	/**
	 * 分配给合作公寓的接入ID
	 */
	private String appId;

	/**
	 * 房屋ID
	 */
	private String sellId;

	/**
	 * 房间ID
	 */
	private int roomId;

	/**
	 * 置顶设置
	 */
	private int isTop;

	/**
	 * 发布类型
	 */
	private int pubType;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public int getPubType() {
		return pubType;
	}

	public void setPubType(int pubType) {
		this.pubType = pubType;
	}

}
