/** 
* Project Name: hzf_platform_project 
* File Name: HouseSolrResult.java 
* Package Name: com.huifenqi.hzf_platform.context.entity.house.solr 
* Date: 2016年5月11日下午5:41:52 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house.solr;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.huifenqi.hzf_platform.context.Constants;

/**
 * ClassName: HouseSolrResult date: 2016年5月11日 下午5:41:52 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@SolrDocument(solrCoreName = Constants.SolrConstant.CORE_HOUSE)
public class HouseSolrResult {

	@Id
	private long id;

	/**
	 * 房源Id
	 */
	@Field("hsId")
	private String sellId;

	/**
	 * 城市Id
	 */
	private long cityId;

	/**
	 * 发布时间
	 */
	@Field("pubDateStr")
	private String pubDate;

	/**
	 * 小区名
	 */
	@Field
	private String communityName;

	/**
	 * 月租金
	 */
	@Field("rentPriceMonth")
	private int price;

	/**
	 * 月租金
	 */
	@Field("rentPriceZero")
	private int priceZero;

	/**
	 * 图片
	 */
	@Field("picRootPath")
	private List<String> imgs;

	/**
	 * 地址
	 */
	@Field
	private String address;

	/**
	 * 客厅数
	 */
	@Field
	private int livingroomNums;

	/**
	 * 卧室数
	 */
	@Field
	private int bedroomNums;

	/**
	 * 卫生间数
	 */
	@Field
	private int toiletNums;

	/**
	 * 朝向
	 */
	@Field
	private int orientations;

	/**
	 * 装饰
	 */
	@Field("fdecoration")
	private int decoration;

	/**
	 * 面积
	 */
	@Field("fArea")
	private double area;

	/**
	 * 楼层
	 */
	@Field
	private String flowNo;

	/**
	 * 总楼层
	 */
	@Field
	private String flowTotal;

	/**
	 * 整租/分租
	 */
	@Field
	private int entireRent;

	/**
	 * 距离
	 */
	@Field("_dist_")
	private double distance;

	/**
	 * 地铁信息
	 */
	@Field
	private String subway;

	/**
	 * 房源标签
	 */
	@Field("housedTag")
	private String houseTag;

	/**
	 * 房源状态
	 */
	@Field
	private int status;

	/**
	 * 入住日期
	 */
	@Field("canCheckinDateStr")
	private String checkIn;

	/**
	 * 押几个月
	 */
	@Field
	private int depositMonth;

	/**
	 * 付几个月
	 */
	@Field
	private int periodMonth;

	/**
	 * 更新日期
	 */
	@Field("updateDateStr")
	private String updateDate;

	/**
	 * 百度经度
	 */
	@Field("baiduLo")
	private String baiduLongitude;

	/**
	 * 百度纬度
	 */
	@Field("baiduLa")
	private String baiduLatitude;

	/**
	 * 到地铁站距离
	 */
	@Field
	private String subwayDistance;

	/**
	 * 附近公交
	 */
	@Field
	private String busStations;

	/**
	 * 周边信息
	 */
	@Field
	private String surround;
	
	
	/**
	 * 商圈名称
	 */
	@Field("bizname")
	private String bizName;
	

	/**
	 * 建筑时间
	 */
	@Field
	private int buildingYear;

	/**
	 * 是否有独立卫生间
	 */
	@Field("ftoilet")
	private int toilet;

	/**
	 * 是否有独立阳台
	 */
	@Field("fbalcony")
	private int balcony;

	/**
	 * 是否有家财险
	 */
	@Field("finsurance")
	private int insurance;

	/**
	 * 房源描述
	 */
	@Field("fcomment")
	private String comment;

	/**
	 * 来源
	 */
	@Field("fsource")
	private String source;

	/**
	 * 联系号码
	 */
	@Field
	private String agencyPhone;

	/**
	 * 房间Id
	 */
	@Field("rId")
	private List<Integer> roomIds;

	/**
	 * 房间名
	 */
	@Field("roomname")
	private List<String> roomNames;

	/**
	 * 房间类型
	 */
	@Field("roomType")
	private List<Integer> roomTypes;

	/**
	 * 房间月租金
	 */
	@Field("rRentPriceMonth")
	private List<Integer> roomPrices;

	/**
	 * 房间审核状态
	 */
	@Field("approveStatus")
	private int approveStatus;
	
	/**
	 * 房间审核状态
	 */
	@Field("rapproveStatus")
	private List<Integer> roomApproveStatus;

	/**
	 * 房间朝向
	 */
	@Field("roomori")
	private List<Integer> roomOrientations;

	/**
	 * 房间面积
	 */
	@Field("rArea")
	private List<Double> roomAreas;

	/**
	 * 房间发布日期
	 */
	@Field("rpubdateStr")
	private List<String> roomPubDates;

	/**
	 * 房间状态
	 */
	@Field("rStatus")
	private List<Integer> roomStatus;

	/**
	 * 房间押几
	 */
	@Field("rDepositMonth")
	private List<Integer> roomDepositMonths;

	/**
	 * 房间付几
	 */
	@Field("rPeriodMonth")
	private List<Integer> roomPeriodMonths;

	/**
	 * 房间独立卫生间
	 */
	@Field("rtoilet")
	private List<Integer> roomToilets;

	/**
	 * 房间独立阳台
	 */
	@Field("rbalcony")
	private List<Integer> roomBalconys;

	/**
	 * 配置编码
	 */
	@Field("settingCode")
	private List<Integer> settingCodes;

	/**
	 * 配置类型
	 */
	@Field("categoryType")
	private List<Integer> categoryTypes;

	/**
	 * 配置数量
	 */
	@Field("settingNums")
	private List<Integer> settingNums;

	/**
	 * 出租方式
	 */
	@Field("rentName")
	private String rentName;
	
	/**
	 * 是否置顶
	 */
	@Field("isTop")
	private int isTop;

	/**
	 * 房源类型
	 */
	@Field("houseType")
	private int houseType;

	/**
	 * 集中式公寓编号
	 */
	@Field("focusCode")
	private String focusCode;

	/**
	 * 公寓Id
	 */
	@Field("companyId")
	private String companyId;
	
	/**
	 * 公寓名称
	 */
	@Field("companyName")
	private String companyName;
	
	/**
	 * 是否支持月付
	 */
	@Field("isPayMonth")
	private int isPayMonth;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
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

	public int getToiletNums() {
		return toiletNums;
	}

	public void setToiletNums(int toiletNums) {
		this.toiletNums = toiletNums;
	}

	public int getOrientations() {
		return orientations;
	}

	public void setOrientations(int orientations) {
		this.orientations = orientations;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getFlowTotal() {
		return flowTotal;
	}

	public void setFlowTotal(String flowTotal) {
		this.flowTotal = flowTotal;
	}

	public int getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public int getDepositMonth() {
		return depositMonth;
	}

	public void setDepositMonth(int depositMonth) {
		this.depositMonth = depositMonth;
	}

	public int getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(int periodMonth) {
		this.periodMonth = periodMonth;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getBaiduLongitude() {
		return baiduLongitude;
	}

	public void setBaiduLongitude(String baiduLongitude) {
		this.baiduLongitude = baiduLongitude;
	}

	public String getBaiduLatitude() {
		return baiduLatitude;
	}

	public void setBaiduLatitude(String baiduLatitude) {
		this.baiduLatitude = baiduLatitude;
	}

	public String getSubwayDistance() {
		return subwayDistance;
	}

	public void setSubwayDistance(String subwayDistance) {
		this.subwayDistance = subwayDistance;
	}

	public String getBusStations() {
		return busStations;
	}

	public void setBusStations(String busStations) {
		this.busStations = busStations;
	}

	public String getSurround() {
		return surround;
	}

	public void setSurround(String surround) {
		this.surround = surround;
	}

	public int getBuildingYear() {
		return buildingYear;
	}

	public void setBuildingYear(int buildingYear) {
		this.buildingYear = buildingYear;
	}

	public int getToilet() {
		return toilet;
	}

	public void setToilet(int toilet) {
		this.toilet = toilet;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public List<Integer> getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(List<Integer> roomIds) {
		this.roomIds = roomIds;
	}

	public List<String> getRoomNames() {
		return roomNames;
	}

	public void setRoomNames(List<String> roomNames) {
		this.roomNames = roomNames;
	}

	public List<Integer> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<Integer> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public List<Integer> getRoomPrices() {
		return roomPrices;
	}

	public void setRoomPrices(List<Integer> roomPrices) {
		this.roomPrices = roomPrices;
	}

	public List<Integer> getRoomOrientations() {
		return roomOrientations;
	}

	public void setRoomOrientations(List<Integer> roomOrientations) {
		this.roomOrientations = roomOrientations;
	}

	public List<Double> getRoomAreas() {
		return roomAreas;
	}

	public void setRoomAreas(List<Double> roomAreas) {
		this.roomAreas = roomAreas;
	}

	public List<String> getRoomPubDates() {
		return roomPubDates;
	}

	public void setRoomPubDates(List<String> roomPubDates) {
		this.roomPubDates = roomPubDates;
	}

	public List<Integer> getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(List<Integer> roomStatus) {
		this.roomStatus = roomStatus;
	}

	public List<Integer> getRoomDepositMonths() {
		return roomDepositMonths;
	}

	public void setRoomDepositMonths(List<Integer> roomDepositMonths) {
		this.roomDepositMonths = roomDepositMonths;
	}

	public List<Integer> getRoomPeriodMonths() {
		return roomPeriodMonths;
	}

	public void setRoomPeriodMonths(List<Integer> roomPeriodMonths) {
		this.roomPeriodMonths = roomPeriodMonths;
	}

	public List<Integer> getRoomToilets() {
		return roomToilets;
	}

	public void setRoomToilets(List<Integer> roomToilets) {
		this.roomToilets = roomToilets;
	}

	public List<Integer> getRoomBalconys() {
		return roomBalconys;
	}

	public void setRoomBalconys(List<Integer> roomBalconys) {
		this.roomBalconys = roomBalconys;
	}

	public List<Integer> getSettingCodes() {
		return settingCodes;
	}

	public void setSettingCodes(List<Integer> settingCodes) {
		this.settingCodes = settingCodes;
	}

	public List<Integer> getCategoryTypes() {
		return categoryTypes;
	}

	public void setCategoryTypes(List<Integer> categoryTypes) {
		this.categoryTypes = categoryTypes;
	}

	public List<Integer> getSettingNums() {
		return settingNums;
	}

	public void setSettingNums(List<Integer> settingNums) {
		this.settingNums = settingNums;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getRentName() {
		return rentName;
	}

	public void setRentName(String rentName) {
		this.rentName = rentName;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public String getFocusCode() {
		return focusCode;
	}

	public void setFocusCode(String focusCode) {
		this.focusCode = focusCode;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getIsPayMonth() {
		return isPayMonth;
	}

	public void setIsPayMonth(int isPayMonth) {
		this.isPayMonth = isPayMonth;
	}

    public int getPriceZero() {
        return priceZero;
    }

    public void setPriceZero(int priceZero) {
        this.priceZero = priceZero;
    }

	public List<Integer> getRoomApproveStatus() {
		return roomApproveStatus;
	}

	public void setRoomApproveStatus(List<Integer> roomApproveStatus) {
		this.roomApproveStatus = roomApproveStatus;
	}

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

}
