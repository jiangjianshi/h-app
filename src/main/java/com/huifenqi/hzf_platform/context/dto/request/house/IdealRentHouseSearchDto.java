/** 
* Project Name: hzf_platform_project 
* File Name: HouseSearchDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.request.house 
* Date: 2016年5月6日下午8:05:56 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.request.house;

/**
 * ClassName: HouseSearchDto date: 2016年5月6日 下午8:05:56 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class IdealRentHouseSearchDto {

	private String sellId;
	
	private String keyword;

	private long cityId;

	private long districtId;

	private long bizId;

	private long lineId;

	private long stationId;

	private String price;

	private int orientation;

	private String area;

	private int entireRent;

	private String location;

	private String distance;

	private String orderType;

	private String order;

	private int pageNum;

	private int pageSize;

	private String bedroomNum;

	private String houseTag;

	private String companyId;
	
	private int appId;

	/* 地铁坐标，此参数非传入，查询得到 */
	private String stationLocation;
	
	/* 朝向字符串 */
	private String orientationStr;

	/* 自在整租 */
	private String eBedRoomNums;
	
	/* 优选合租 */
	private String sBedRoomNums;
	
	/* 付款方式 */
	private String payType;
	
	/* 房源Id 搜索附近房源时，排除当前房源 */
	private String sellerId;
	
	/* 房间Id 搜索附近房间时，排除当前房间 */
	private int roomerId;

	private String isTop;

	/* saas房源发布类型 */
	private int pubType;
	
	//层级  1：行政区 2：商圈 3：小区
	private int level;
	
	//出行方式   1：公交 2：自驾 3：骑行 4：步行
	private String travelType;
	
	//出行时间
	private String time;
	
	//来源渠道
	private String channel;
	
	//通勤配置ID
    private int commuteId;
	
    /* 租房宝典,白领优选筛选合租主卧和整租一居室的房源 */
    private String cBedRoomNums;
	
	

	public String getSellId() {
		return sellId;
	}



	public void setSellId(String sellId) {
		this.sellId = sellId;
	}



	public String getKeyword() {
		return keyword;
	}



	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}



	public long getCityId() {
		return cityId;
	}



	public void setCityId(long cityId) {
		this.cityId = cityId;
	}



	public long getDistrictId() {
		return districtId;
	}



	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}



	public long getBizId() {
		return bizId;
	}



	public void setBizId(long bizId) {
		this.bizId = bizId;
	}



	public long getLineId() {
		return lineId;
	}



	public void setLineId(long lineId) {
		this.lineId = lineId;
	}



	public long getStationId() {
		return stationId;
	}



	public void setStationId(long stationId) {
		this.stationId = stationId;
	}



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
	}



	public int getOrientation() {
		return orientation;
	}



	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}



	public String getArea() {
		return area;
	}



	public void setArea(String area) {
		this.area = area;
	}



	public int getEntireRent() {
		return entireRent;
	}



	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public String getDistance() {
		return distance;
	}



	public void setDistance(String distance) {
		this.distance = distance;
	}



	public String getOrderType() {
		return orderType;
	}



	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}



	public String getOrder() {
		return order;
	}



	public void setOrder(String order) {
		this.order = order;
	}



	public int getPageNum() {
		return pageNum;
	}



	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public String getBedroomNum() {
		return bedroomNum;
	}



	public void setBedroomNum(String bedroomNum) {
		this.bedroomNum = bedroomNum;
	}



	public String getHouseTag() {
		return houseTag;
	}



	public void setHouseTag(String houseTag) {
		this.houseTag = houseTag;
	}



	public String getCompanyId() {
		return companyId;
	}



	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}



	public int getAppId() {
		return appId;
	}



	public void setAppId(int appId) {
		this.appId = appId;
	}



	public String getStationLocation() {
		return stationLocation;
	}



	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
	}



	public String getOrientationStr() {
		return orientationStr;
	}



	public void setOrientationStr(String orientationStr) {
		this.orientationStr = orientationStr;
	}



	public String geteBedRoomNums() {
		return eBedRoomNums;
	}



	public void seteBedRoomNums(String eBedRoomNums) {
		this.eBedRoomNums = eBedRoomNums;
	}



	public String getsBedRoomNums() {
		return sBedRoomNums;
	}



	public void setsBedRoomNums(String sBedRoomNums) {
		this.sBedRoomNums = sBedRoomNums;
	}



	public String getPayType() {
		return payType;
	}



	public void setPayType(String payType) {
		this.payType = payType;
	}



	public String getSellerId() {
		return sellerId;
	}



	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}



	public int getRoomerId() {
		return roomerId;
	}



	public void setRoomerId(int roomerId) {
		this.roomerId = roomerId;
	}



	public String getIsTop() {
		return isTop;
	}



	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}



	public int getPubType() {
		return pubType;
	}



	public void setPubType(int pubType) {
		this.pubType = pubType;
	}



	public int getLevel() {
		return level;
	}



	public void setLevel(int level) {
		this.level = level;
	}



	public String getTravelType() {
		return travelType;
	}



	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public int getCommuteId() {
		return commuteId;
	}



	public void setCommuteId(int commuteId) {
		this.commuteId = commuteId;
	}



	public String getcBedRoomNums() {
		return cBedRoomNums;
	}



	public void setcBedRoomNums(String cBedRoomNums) {
		this.cBedRoomNums = cBedRoomNums;
	}



	@Override
	public String toString() {
		return "MapHouseSearchDto [sellId=" + sellId + ", keyword=" + keyword + ", cityId=" + cityId + ", districtId="
				+ districtId + ", bizId=" + bizId + ", lineId=" + lineId + ", stationId=" + stationId + ", price="
				+ price + ", orientation=" + orientation + ", area=" + area + ", entireRent=" + entireRent
				+ ", location=" + location + ", distance=" + distance + ", orderType=" + orderType + ", order=" + order
				+ ", pageNum=" + pageNum + ", pageSize=" + pageSize + ", bedroomNum=" + bedroomNum + ", houseTag="
				+ houseTag + ", companyId=" + companyId + ", appId=" + appId + ", stationLocation=" + stationLocation
				+ ", orientationStr=" + orientationStr + ", eBedRoomNums=" + eBedRoomNums + ", sBedRoomNums="
				+ sBedRoomNums + ", payType=" + payType + ", sellerId=" + sellerId + ", roomerId=" + roomerId
				+ ", isTop=" + isTop + ", pubType=" + pubType + ", level=" + level + ", travelType=" + travelType
				+ ", time=" + time + ", channel=" + channel + "]";
	}

	
}
