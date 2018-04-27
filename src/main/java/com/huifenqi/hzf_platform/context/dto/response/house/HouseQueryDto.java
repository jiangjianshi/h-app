/** 
 * Project Name: hzf_platform 
 * File Name: HouseQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月27日下午8:06:27 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * ClassName: HouseQueryDto date: 2016年4月27日 下午8:06:27 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class HouseQueryDto {

	/**
	 * 坐标，包括经纬度
	 */
	private String position;

	/**
	 * x坐标 纬度
	 */
	private String positionX;

	/**
	 * y坐标 经度
	 */
	private String positionY;

	/**
	 * 城市Id
	 */
	private long cityId;

	/**
	 * 小区名称
	 */
	private String communityName;

	/**
	 * 位置
	 */
	private String address;

	/**
	 * 租金月租金，单位为分
	 */
	private int price;

	/**
	 * 服务费 ，单位为分
	 */
	private int bonus;

	/**
	 * 是否有钥匙
	 */
	private int hasKey;

	/**
	 * 房源标签
	 */
	private String houseTag;

	/**
	 * 经纪公司id
	 */
	private String companyId;

	/**
	 * 经纪公司名称
	 */
	private String companyName;

	/**
	 * 经纪公司信誉等级
	 */
	private String companyCredit;

	/**
	 * 经纪人ID
	 */
	private int agencyId;

	/**
	 * 经纪人联系电话
	 */
	private String agencyPhone;

	/**
	 * 经纪人400分机号
	 */
	private String agencyExt;

	/**
	 * 经纪人姓名
	 */
	private String agencyName;

	/**
	 * 经纪人自我介绍
	 */
	private String agencyIntroduce;

	/**
	 * 经纪人性别 1-男 2-女
	 */
	private int agencyGender;

	/**
	 * 房源状态
	 */
	private int status;

	/**
	 * 房源状态描述
	 */
	private String statusDesc;

	/**
	 * 房源标题
	 */
	private String title;

	/**
	 * 几室几厅
	 */
	private String room;

	/**
	 * 面积
	 */
	private String area;

	/**
	 * 楼层信息
	 */
	private String floor;

	/**
	 * 朝向
	 */
	private String orentation;

	/**
	 * 建筑类型
	 */
	private String buildingType;;

	/**
	 * 装修档次
	 */
	private String fitmentType;

	/**
	 * 建筑时间
	 */
	private String buildingYear;

	/**
	 * 独立卫生间
	 */
	private int toilet;

	/**
	 * 独立阳台
	 */
	private int balcony;

	/**
	 * 家财险
	 */
	private String insurance;

	/**
	 * 入住时间
	 */
	private String checkIn;

	/**
	 * 押几
	 */
	private int depositMonth;

	/**
	 * 押金形式 , 例如押一付三
	 */
	private String payType;

	/**
	 * 付几
	 */
	private int periodMonth;

	/**
	 * 租住类型
	 */
	private int entireRent;

	/**
	 * 租住类型描述
	 */
	private String entireRentDesc;

	/**
	 * 主要室内设施
	 */
	private List<SettingVo> settings;

	/**
	 * 最近地铁站点
	 */
	private String subway;

	/**
	 * 到最近的地铁距离
	 */
	private String subwayDistance;

	/**
	 * 附近的公交站点
	 */
	private String busStations;

	/**
	 * 周边
	 */
	private String surround;

	/**
	 * 房源描述
	 */
	private String desc;

	/**
	 * 房源照片
	 */
	private List<ImgLink> imgs;

	/**
	 * 是否收藏 0-未收藏 1-已收藏
	 */
	private int mark;

	/**
	 * 房间的类型:主卧、次卧
	 */
	private String roomType;

	/**
	 * 房间名称
	 */
	private String roomName;

	/**
	 * 同一个house_id的其他房间
	 */
	private List<RoomQueryDto> referHouse;

	/**
	 * 房间用途
	 */
	private String roomUse;

	/**
	 * 建筑用途
	 */
	private String buildType;

	/**
	 * 发布时间
	 */
	private String pubDate;

	/**
	 * 最后更新时间
	 */
	private String lastModifyDate;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 分租房源面积
	 */
	private String houseArea;

	/**
	 * 同小区房源总数
	 */
	private long communityHouseCount;

	/**
	 * 公寓下所有房源总数
	 */
	private long companyHouseCount;

	/**
	 * 公寓下所有城市总数
	 */
	private long companyCityCount;

	/**
	 * 是否支持月付
	 */
	private int isMonthlyPay;

	/**
	 * 房源/房间是否被收藏：1 已收藏；0 未收藏
	 */
	private int collectFlag;

	/**
	 * 房源对应的品牌公寓logo
	 */
	private String picWebPath;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPositionX() {
		return positionX;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getHasKey() {
		return hasKey;
	}

	public void setHasKey(int hasKey) {
		this.hasKey = hasKey;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCredit() {
		return companyCredit;
	}

	public void setCompanyCredit(String companyCredit) {
		this.companyCredit = companyCredit;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getAgencyExt() {
		return agencyExt;
	}

	public void setAgencyExt(String agencyExt) {
		this.agencyExt = agencyExt;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyIntroduce() {
		return agencyIntroduce;
	}

	public void setAgencyIntroduce(String agencyIntroduce) {
		this.agencyIntroduce = agencyIntroduce;
	}

	public int getAgencyGender() {
		return agencyGender;
	}

	public void setAgencyGender(int agencyGender) {
		this.agencyGender = agencyGender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public String getOrentation() {
		return orentation;
	}

	public void setOrentation(String orentation) {
		this.orentation = orentation;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getFitmentType() {
		return fitmentType;
	}

	public void setFitmentType(String fitmentType) {
		this.fitmentType = fitmentType;
	}

	public String getBuildingYear() {
		return buildingYear;
	}

	public void setBuildingYear(String buildingYear) {
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

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkInTime) {
		this.checkIn = checkInTime;
	}

	public int getDepositMonth() {
		return depositMonth;
	}

	public void setDepositMonth(int depositMonth) {
		this.depositMonth = depositMonth;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public int getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(int periodMonth) {
		this.periodMonth = periodMonth;
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


	public List<SettingVo> getSettings() {
		return settings;
	}

	public void setSettings(List<SettingVo> settings) {
		this.settings = settings;
	}

	public String getSubway() {
		return subway;
	}

	public void setSubway(String subway) {
		this.subway = subway;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ImgLink> getImgs() {
		return imgs;
	}

	public void setImgs(List<ImgLink> imgs) {
		this.imgs = imgs;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<RoomQueryDto> getReferHouse() {
		return referHouse;
	}

	public void setReferHouse(List<RoomQueryDto> referHouse) {
		this.referHouse = referHouse;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	public long getCommunityHouseCount() {
		return communityHouseCount;
	}

	public void setCommunityHouseCount(long communityHouseCount) {
		this.communityHouseCount = communityHouseCount;
	}

	public long getCompanyHouseCount() {
		return companyHouseCount;
	}

	public void setCompanyHouseCount(long companyHouseCount) {
		this.companyHouseCount = companyHouseCount;
	}

	public long getCompanyCityCount() {
		return companyCityCount;
	}

	public void setCompanyCityCount(long companyCityCount) {
		this.companyCityCount = companyCityCount;
	}

	public int getIsMonthlyPay() {
		return isMonthlyPay;
	}

	public void setIsMonthlyPay(int isMonthlyPay) {
		this.isMonthlyPay = isMonthlyPay;
	}

	public int getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(int collectFlag) {
		this.collectFlag = collectFlag;
	}

	public String getPicWebPath() {
		return picWebPath;
	}

	public void setPicWebPath(String picWebPath) {
		this.picWebPath = picWebPath;
	}

	@Override
	public String toString() {
		return "HouseQueryDto{" + "position='" + position + '\'' + ", positionX='" + positionX + '\'' + ", positionY='"
				+ positionY + '\'' + ", cityId=" + cityId + ", communityName='" + communityName + '\'' + ", address='"
				+ address + '\'' + ", price=" + price + ", bonus=" + bonus + ", hasKey=" + hasKey + ", houseTag='"
				+ houseTag + '\'' + ", companyId=" + companyId + ", companyName='" + companyName + '\''
				+ ", companyCredit='" + companyCredit + '\'' + ", agencyId=" + agencyId + ", agencyPhone='"
				+ agencyPhone + '\'' + ", agencyExt='" + agencyExt + '\'' + ", agencyName='" + agencyName + '\''
				+ ", agencyIntroduce='" + agencyIntroduce + '\'' + ", agencyGender=" + agencyGender + ", status="
				+ status + ", statusDesc='" + statusDesc + '\'' + ", title='" + title + '\'' + ", room='" + room + '\''
				+ ", area='" + area + '\'' + ", floor='" + floor + '\'' + ", orentation='" + orentation + '\''
				+ ", buildingType='" + buildingType + '\'' + ", fitmentType='" + fitmentType + '\'' + ", buildingYear='"
				+ buildingYear + '\'' + ", toilet=" + toilet + ", balcony=" + balcony + ", insurance='" + insurance
				+ '\'' + ", checkIn='" + checkIn + '\'' + ", depositMonth=" + depositMonth + ", payType='" + payType
				+ '\'' + ", periodMonth=" + periodMonth + ", entireRent=" + entireRent + ", entireRentDesc='"
				+ entireRentDesc + '\'' + ", settings=" + settings + ", subway='" + subway + '\'' + ", subwayDistance='"
				+ subwayDistance + '\'' + ", busStations='" + busStations + '\'' + ", surround='" + surround + '\''
				+ ", desc='" + desc + '\'' + ", imgs=" + imgs + ", mark=" + mark + ", roomType='" + roomType + '\''
				+ ", roomName='" + roomName + '\'' + ", referHouse=" + referHouse + ", roomUse='" + roomUse + '\''
				+ ", buildType='" + buildType + '\'' + ", pubDate='" + pubDate + '\'' + ", lastModifyDate='"
				+ lastModifyDate + '\'' + ", source='" + source + '\'' + ", houseArea='" + houseArea + '\''
				+ ", communityHouseCount=" + communityHouseCount + ", companyHouseCount=" + companyHouseCount
				+ ", companyCityCount=" + companyCityCount + ", isMonthlyPay=" + isMonthlyPay + ", collectFlag="
				+ collectFlag + ", picWebPath=" + picWebPath + '}';
	}
}
