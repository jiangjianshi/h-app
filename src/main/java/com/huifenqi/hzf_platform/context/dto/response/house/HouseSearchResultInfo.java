/** 
 * Project Name: hzf_platform_project 
 * File Name: HouseSearchResultDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response.house 
 * Date: 2016年5月6日下午8:07:32 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.house;

/**
 * ClassName: HouseSearchResultInfo date: 2016年5月6日 下午8:07:32 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseSearchResultInfo {

	private String sellId;

	private long roomId;

	private String title;

	private int price;

	private String area;

	private String houseArea;

	private String floor;

	private String subway;

	private String houseTag;

	private String pic;

	private String pubDesc;

	private String pubDate;

	private String communityName;

	private String subTitle;

	private String address;

	private int livingroomNums;

	private int bedroomNums;

	private String decoration;

	private String floorNo;

	private String floorTotal;

	private int entireRent;

	private String entireRentDesc;

	private int distance;

	private String orientationName;

	private int isTop;

	private String rtName;

	private String companyId;
	
	/* 房源收藏标识 */
	private int collectFlag;
	
	/* 房源/房间出租标识 */
	private int rentFlag;

	private String source;
	
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

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getLivingroomNums() {
		return livingroomNums;
	}

	public void setLivingroomNums(int livingroomNums) {
		this.livingroomNums = livingroomNums;
	}

	public int getBedroomNums() {
		return bedroomNums;
	}

	public void setBedroomNums(int bedroomNums) {
		this.bedroomNums = bedroomNums;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	public String getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}

	public String getFloorTotal() {
		return floorTotal;
	}

	public void setFloorTotal(String floorTotal) {
		this.floorTotal = floorTotal;
	}

	public int getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
	}

	public String getEntireRentDesc() {
		return entireRentDesc;
	}

	public void setEntireRentDesc(String entireRentDesc) {
		this.entireRentDesc = entireRentDesc;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getOrientationName() {
		return orientationName;
	}

	public void setOrientationName(String orientationName) {
		this.orientationName = orientationName;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	public String getRtName() {
		return rtName;
	}

	public void setRtName(String rtName) {
		this.rtName = rtName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public int getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(int collectFlag) {
		this.collectFlag = collectFlag;
	}

	public int getRentFlag() {
		return rentFlag;
	}

	public void setRentFlag(int rentFlag) {
		this.rentFlag = rentFlag;
	}

	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "HouseSearchResultInfo [sellId=" + sellId + ", roomId=" + roomId + ", title=" + title + ", price="
				+ price + ", area=" + area + ", houseArea=" + houseArea + ", floor=" + floor + ", subway=" + subway
				+ ", houseTag=" + houseTag + ", pic=" + pic + ", pubDesc=" + pubDesc + ", pubDate=" + pubDate
				+ ", communityName=" + communityName + ", subTitle=" + subTitle + ", address=" + address
				+ ", livingroomNums=" + livingroomNums + ", bedroomNums=" + bedroomNums + ", decoration=" + decoration
				+ ", floorNo=" + floorNo + ", floorTotal=" + floorTotal + ", entireRent=" + entireRent
				+ ", entireRentDesc=" + entireRentDesc + ", distance=" + distance + ", orientationName=" + orientationName
				+ ", isTop=" + isTop + ", rtName=" + rtName + ", companyId=" + companyId 
				+ ", collectFlag=" + collectFlag+ ", rentFlag=" + rentFlag + "]";
	}
	

}
