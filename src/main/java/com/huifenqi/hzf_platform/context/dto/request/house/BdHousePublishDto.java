/** 
 * Project Name: hzf_platform 
 * File Name: HouseDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto 
 * Date: 2016年4月26日下午7:52:07 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.Date;

/**
 * ClassName: HouseDto date: 2016年4月26日 下午7:52:07 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class BdHousePublishDto {

// ===================================  新增房源接口字段  ==========================================
	/**
	 * 分配给合作公寓的接入ID
	 */
	private String appId;

	/**
	 * 合作公寓的房屋ID
	 */
	private String outHouseId;

	/**
	 * 图片list
	 */
	private String picUrlList;

	/**
	 * 出租方式，枚举值1：整租 2：单间出租􀠪
	 */
	private Integer rentType;

	/**
	 * 居室数量
	 */
	private Integer bedRoomNum;

	/**
	 * 起居室数量
	 */
	private Integer livingRoomNum;

	/**
	 * 卫生间数量
	 */
	private Integer toiletNum;

	/**
	 * 面积
	 */
	private Float rentRoomArea;

	/**
	 * 出租屋类型 31主卧 32 次卧  33不区分主次
	 */
	private Integer bedRoomType;

	/**
	 * 房间名称
	 */
	private String roomName;

	/**
	 * 房间编码
	 */
	private String roomCode;

	/**
	 * 朝向
	 */
	private Integer faceToType;

	/**
	 * 总楼层数量
	 */
	private Integer totalFloor;

	/**
	 * 所在楼层，物理楼层
	 */
	private Integer houseFloor;

	/**
	 * 房间标签，枚举值，可多选，以逗号分隔
	 10离地铁近
	 11有阳台
	 12独立卫生间
	 13厨房
	 14首次出租
	 15阳光房
	 16无中介费
	 17押一付一
	 18押二付一
	 19无服务费
	 */
	private String featureTag;

	/**
	 * 房屋配置，枚举值，可多选，以逗号分隔
	 71床  72衣柜  73书桌  74空调  75暖气
	 76电视机  77燃气  78微波炉  79电磁炉
	 80热水器  81洗衣机 82冰箱  83wifi
	 84沙发  85橱柜  86油烟机
	 */
	private String detailPoint;

	/**
	 * 公寓配套服务，枚举值，可多选，以逗号分隔
	 91健身房  92公寓超市  93智能门锁  94ATM机
	 95代收快递  96专属客服  97房间清洁
	 */
	private String servicePoint;

	/**
	 * 租金月租金，单位为分
	 */
	private Integer monthRent;

	/**
	 * 入住时间
	 */
	private Date rentStartDate;

	/**
	 * 是否支持短租，枚举值0不支持  1支持
	 */
	private Integer shortRent;

	/**
	 * 房屋所在省
	 */
	private String province;

	/**
	 * 房屋所在城市
	 */
	private String cityName;

	/**
	 * 房屋所在区县
	 */
	private String countyName;

	/**
	 * 房屋所在商圈
	 */
	private String areaName;

	/**
	 * 小区名称
	 */
	private String districtName;

	/**
	 * 房屋所在小区详细地址 北京市昌平区蓝天家园
	 */
	private String street;

	/**
	 * 出租房屋门牌地址 1号楼3单元208室
	 */
	private String address;

	/**
	 * 房屋附近地铁站所在线路名称 1号线
	 */
	private String subwayLine;

	/**
	 * 房屋附近地铁站名 北京南站
	 */
	private String subwayStation;

	/**
	 * 房间描述
	 */
	private String houseDesc;

	/**
	 * x坐标 纬度
	 */
	private String xCoord;

	/**
	 * y坐标 经度
	 */
	private String yCoord;

	/**
	 * 经纪人联系电话
	 */
	private String agentPhone;

	/**
	 * 预约短信接收到的手机号
	 */
	private String orderPhone;

	/**
	 * 经纪人姓名
	 */
	private String agentName;

	/**
	 * 合作公寓系统房屋视频url
	 */
	private String videoUrl;

	/**
	 * 建筑时间
	 */
	private Integer buildYear;

	/**
	 * 小区供暖方式，1集中供暖 2自供暖 3无供暖
	 */
	private Integer supplyHeating;

	/**
	 * 小区绿化率，不带百分号
	 */
	private Integer greenRatio;

	/**
	 * 建筑类型
	 */
	private Integer buildType;
// ===================================  新增房源接口字段  ==========================================

// ===================================  房源上下架接口新增字段  ==========================================
	/**
	 * 改动原因
	 */
	private String memo;

	/**
	 * 房源状态，4000上架  5000下架
	 */
	private String status;
// ===================================  房源上下架接口新增字段  ==========================================


	/**
	 * 公司ID
	 */
	private String companyId;

	/**
	 * 公司名称
	 */
	private String companyName;

	/**
	 * 装修状态
	 */
	private Integer fitmentState;
	
	/**
	 * 是否saas公司 0否  1是
	 */
	private Integer isSaas;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOutHouseId() {
		return outHouseId;
	}

	public void setOutHouseId(String outHouseId) {
		this.outHouseId = outHouseId;
	}

	public String getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(String picUrlList) {
		this.picUrlList = picUrlList;
	}

	public Integer getRentType() {
		return rentType;
	}

	public void setRentType(Integer rentType) {
		this.rentType = rentType;
	}

	public Integer getBedRoomNum() {
		return bedRoomNum;
	}

	public void setBedRoomNum(Integer bedRoomNum) {
		this.bedRoomNum = bedRoomNum;
	}

	public Integer getLivingRoomNum() {
		return livingRoomNum;
	}

	public void setLivingRoomNum(Integer livingRoomNum) {
		this.livingRoomNum = livingRoomNum;
	}

	public Integer getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(Integer toiletNum) {
		this.toiletNum = toiletNum;
	}

	public Float getRentRoomArea() {
		return rentRoomArea;
	}

	public void setRentRoomArea(Float rentRoomArea) {
		this.rentRoomArea = rentRoomArea;
	}

	public Integer getBedRoomType() {
		return bedRoomType;
	}

	public void setBedRoomType(Integer bedRoomType) {
		this.bedRoomType = bedRoomType;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public Integer getFaceToType() {
		return faceToType;
	}

	public void setFaceToType(Integer faceToType) {
		this.faceToType = faceToType;
	}

	public Integer getTotalFloor() {
		return totalFloor;
	}

	public void setTotalFloor(Integer totalFloor) {
		this.totalFloor = totalFloor;
	}

	public Integer getHouseFloor() {
		return houseFloor;
	}

	public void setHouseFloor(Integer houseFloor) {
		this.houseFloor = houseFloor;
	}

	public String getFeatureTag() {
		return featureTag;
	}

	public void setFeatureTag(String featureTag) {
		this.featureTag = featureTag;
	}

	public String getDetailPoint() {
		return detailPoint;
	}

	public void setDetailPoint(String detailPoint) {
		this.detailPoint = detailPoint;
	}

	public String getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}

	public Integer getMonthRent() {
		return monthRent;
	}

	public void setMonthRent(Integer monthRent) {
		this.monthRent = monthRent;
	}

	public Date getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(Date rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public Integer getShortRent() {
		return shortRent;
	}

	public void setShortRent(Integer shortRent) {
		this.shortRent = shortRent;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubwayLine() {
		return subwayLine;
	}

	public void setSubwayLine(String subwayLine) {
		this.subwayLine = subwayLine;
	}

	public String getSubwayStation() {
		return subwayStation;
	}

	public void setSubwayStation(String subwayStation) {
		this.subwayStation = subwayStation;
	}

	public String getHouseDesc() {
		return houseDesc;
	}

	public void setHouseDesc(String houseDesc) {
		this.houseDesc = houseDesc;
	}

	public String getxCoord() {
		return xCoord;
	}

	public void setxCoord(String xCoord) {
		this.xCoord = xCoord;
	}

	public String getyCoord() {
		return yCoord;
	}

	public void setyCoord(String yCoord) {
		this.yCoord = yCoord;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Integer getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(Integer buildYear) {
		this.buildYear = buildYear;
	}

	public Integer getSupplyHeating() {
		return supplyHeating;
	}

	public void setSupplyHeating(Integer supplyHeating) {
		this.supplyHeating = supplyHeating;
	}

	public Integer getGreenRatio() {
		return greenRatio;
	}

	public void setGreenRatio(Integer greenRatio) {
		this.greenRatio = greenRatio;
	}

	public Integer getBuildType() {
		return buildType;
	}

	public void setBuildType(Integer buildType) {
		this.buildType = buildType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getFitmentState() {
		return fitmentState;
	}

	public void setFitmentState(Integer fitmentState) {
		this.fitmentState = fitmentState;
	}

	public Integer getIsSaas() {
		return isSaas;
	}

	public void setIsSaas(Integer isSaas) {
		this.isSaas = isSaas;
	}

	
}
