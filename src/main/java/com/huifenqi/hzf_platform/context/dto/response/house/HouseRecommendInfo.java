/** 
* Project Name: hzf_platform_project 
* File Name: HouseRecommenQueryDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
* Date: 2016年5月9日下午8:35:44 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.response.house;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: HouseRecommendInfo date: 2016年5月9日 下午8:35:44 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseRecommendInfo {

	private String sellId;

	private long roomId;

	private String title;

	private int price;

	private String area;

	private String floor;

	private String subway;

	private String houseTag;

	private String pic;

	private String pubDesc;

	private String entireRentDesc;// 租赁方式

	private String orientationName;// 朝向

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getSubway() {
		return subway;
	}

	public void setSubway(String subway) {
		this.subway = subway;
	}

	public String getHouseTag() {
		return houseTag;
	}

	public void setHouseTag(String houseTag) {
		this.houseTag = houseTag;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPubDesc() {
		return pubDesc;
	}

	public void setPubDesc(String pubDesc) {
		this.pubDesc = pubDesc;
	}

	public String getOrientationName() {
		return orientationName;
	}

	public void setOrientationName(String orientationName) {
		this.orientationName = orientationName;
	}

	public String getEntireRentDesc() {
		return entireRentDesc;
	}

	public void setEntireRentDesc(String entireRentDesc) {
		this.entireRentDesc = entireRentDesc;
	}

	@Override
	public String toString() {
		return "HouseRecommendInfo [sellId=" + sellId + ", roomId=" + roomId + ", title=" + title + ", price=" + price
				+ ", area=" + area + ", floor=" + floor + ", subway=" + subway + ", houseTag=" + houseTag + ", pic="
				+ pic + ", pubDesc=" + pubDesc + ", entireRentDesc=" + entireRentDesc + ", orientationName="
				+ orientationName + "]";
	}

}
