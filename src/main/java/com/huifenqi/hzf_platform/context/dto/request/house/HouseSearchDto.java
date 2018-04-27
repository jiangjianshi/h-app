/** 
* Project Name: hzf_platform_project 
* File Name: HouseSearchDto.java 
* Package Name: com.huifenqi.hzf_platform.context.dto.request.house 
* Date: 2016年5月6日下午8:05:56 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.List;

import com.huifenqi.hzf_platform.context.Constants;

/**
 * ClassName: HouseSearchDto date: 2016年5月6日 下午8:05:56 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseSearchDto {

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

	private int entireRent = Constants.HouseDetail.RENT_TYPE_ALL;

	private String location;

	private String distance;// 到地铁的距离

	private String orderType;

	private String order;

	private int pageNum = 1;

	private int pageSize = 10;

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

	/* 租房宝典,白领优选筛选合租主卧和整租一居室的房源 */
	private String cBedRoomNums;

	/* 付款方式 */
	private String payType;

	/* 房源Id 搜索附近房源时，排除当前房源 */
	private String sellerId;

	/* 房间Id 搜索附近房间时，排除当前房间 */
	private int roomerId;

	private String isTop;

	/* saas房源发布类型 */
	private int pubType;

	/* 小区名称(地图找房用到) */
	private String communityName;

	/* 是否查询品牌公寓房源标识（1：品牌公寓房源；2：非品牌公寓房源） */
	private String companyType;

	// 限制渠道列表
	private List<String> denyList;

	// 限制公司列表
	private List<String> companyOffList;

	// 默认第一页展示房源
	private List<String> houseList;

	// 默认第一页展示房间
	private List<String> roomList;

	//是否首页 0 否 1是
	private int isHomePage;
	
	//是否首页 0 否 1是
    private long userId;
	
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

	public String getOrientationStr() {
		return orientationStr;
	}

	public void setOrientationStr(String orientationStr) {
		this.orientationStr = orientationStr;
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

	public String getStationLocation() {
		return stationLocation;
	}

	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
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

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
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

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
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

	public int getPubType() {
		return pubType;
	}

	public void setPubType(int pubType) {
		this.pubType = pubType;
	}

	public String getcBedRoomNums() {
		return cBedRoomNums;
	}

	public void setcBedRoomNums(String cBedRoomNums) {
		this.cBedRoomNums = cBedRoomNums;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public List<String> getDenyList() {
		return denyList;
	}

	public void setDenyList(List<String> denyList) {
		this.denyList = denyList;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public List<String> getCompanyOffList() {
		return companyOffList;
	}

	public void setCompanyOffList(List<String> companyOffList) {
		this.companyOffList = companyOffList;
	}

	public List<String> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<String> houseList) {
		this.houseList = houseList;
	}

	public List<String> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<String> roomList) {
		this.roomList = roomList;
	}

	
	public int getIsHomePage() {
        return isHomePage;
    }

    public void setIsHomePage(int isHomePage) {
        this.isHomePage = isHomePage;
    }

    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
	public String toString() {
		return "HouseSearchDto [sellId=" + sellId + ", keyword=" + keyword + ", cityId=" + cityId + ", districtId="
				+ districtId + ", bizId=" + bizId + ", lineId=" + lineId + ", stationId=" + stationId + ", price="
				+ price + ", orientation=" + orientation + ", area=" + area + ", entireRent=" + entireRent
				+ ", location=" + location + ", distance=" + distance + ", orderType=" + orderType + ", order=" + order
				+ ", pageNum=" + pageNum + ", pageSize=" + pageSize + ", bedroomNum=" + bedroomNum + ", houseTag="
				+ houseTag + ", companyId=" + companyId + ", appId=" + appId + ", stationLocation=" + stationLocation
				+ ", orientationStr=" + orientationStr + ", eBedRoomNums=" + eBedRoomNums + ", sBedRoomNums="
				+ sBedRoomNums + ", cBedRoomNums=" + cBedRoomNums + ", payType=" + payType + ", sellerId=" + sellerId
				+ ", roomerId=" + roomerId + ", isTop=" + isTop + ", pubType=" + pubType + ", communityName="
				+ communityName + ", denyList=" + denyList + ", companyType=" + companyType + "]";
	}

}
