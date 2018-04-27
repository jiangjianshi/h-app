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
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class HousePublishDtoAsz {

	/**
	 * 公寓房源编号ID
	 */
	private String apartmentId;


	/**
	 * 房源编号
	 */
	private String apartmentCode;

	/**
	 * 房源所属房屋
	 */
	private String houseId;

	/**
	 * 出租方式 ENTIRE 整租 SHARE 合租
	 */
	private String rentType;

	/**
	 * 房间号 出租方式为合租存在
	 */
	private String roomNo;

	/**
	 * 出租状态WAITING_RENT:待出租
	 */
	private String rentStatus;

	/**
	 * 出租月租金
	 */
	private String rentPrice;

	/**
	 * 租客信息
	 */
	private String customerPerson;

	/**
	 * 楼盘名称
	 */
	private String residentialName;

	/**
	 * 城市ID
	 */
	private String cityCode;

	/**
	 * 城市名称
	 */
	private String cityName;

	/**
	 * 城市ID
	 */
	private String areaCode;

	/**
	 * 区域名称
	 */
	private String areaName;

	/**
	 * 街道地址
	 */
	private String address;

	/**
	 * 商圈
	 */
	private String businessCircleMulti;

	/**
	 * 经度
	 */
	private String lng;

	/**
	 * 纬度
	 */
	private String lat;

	/**
	 * 物业用途
	 */
	private String propertyUse;

	/**
	 * 所属楼层
	 */
	private Integer floor;

	/**
	 * 总楼层
	 */
	private Integer groudFloors;

	/**
	 * 室
	 */
	private Integer rooms;

	/**
	 * 厅
	 */
	private Integer livings;

	/**
	 * 卫
	 */
	private Integer bathRooms;

	/**
	 * 房源面积
	 */
	private float buildArea;

	/**
	 * 总面积
	 */
	private float totalArea;

	/**
	 * 朝向
	 */
	private String orientation;

	/**
	 * 装修情况
	 */
	private String fitmentType;

	/**
	 * 房屋配置
	 */
	private String houseRoomFeature;

	/**
	 * 装修情况
	 */
	private String houseConfiuTation;

	/**
	 * 房屋描述
	 */
	private String remark;

	/**
	 * 图片url
	 */
	private String imgList;

	/**
	 * 房管员
	 */
	private String agentUname;


	/**
	 * 经纪人手机号
	 */
	private String agentUphone;

	/**
	 * 所属岗位
	 */
	private String agentPost;

	/**
	 * 所属部门
	 */
	private String agentDepartment;

	/**
	 * 上架状态 ONLINE：上架OFFLINE:下架
	 */
	private String onlineStatus;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */

	private Date updateTime;

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public String getApartmentCode() {
		return apartmentCode;
	}

	public void setApartmentCode(String apartmentCode) {
		this.apartmentCode = apartmentCode;
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getRentType() {
		return rentType;
	}

	public void setRentType(String rentType) {
		this.rentType = rentType;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getRentStatus() {
		return rentStatus;
	}

	public void setRentStatus(String rentStatus) {
		this.rentStatus = rentStatus;
	}

	public String getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(String rentPrice) {
		this.rentPrice = rentPrice;
	}

	public String getCustomerPerson() {
		return customerPerson;
	}

	public void setCustomerPerson(String customerPerson) {
		this.customerPerson = customerPerson;
	}

	public String getResidentialName() {
		return residentialName;
	}

	public void setResidentialName(String residentialName) {
		this.residentialName = residentialName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusinessCircleMulti() {
		return businessCircleMulti;
	}

	public void setBusinessCircleMulti(String businessCircleMulti) {
		this.businessCircleMulti = businessCircleMulti;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getPropertyUse() {
		return propertyUse;
	}

	public void setPropertyUse(String propertyUse) {
		this.propertyUse = propertyUse;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getGroudFloors() {
		return groudFloors;
	}

	public void setGroudFloors(Integer groudFloors) {
		this.groudFloors = groudFloors;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}

	public Integer getLivings() {
		return livings;
	}

	public void setLivings(Integer livings) {
		this.livings = livings;
	}

	public Integer getBathRooms() {
		return bathRooms;
	}

	public void setBathRooms(Integer bathRooms) {
		this.bathRooms = bathRooms;
	}

	public float getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(float buildArea) {
		this.buildArea = buildArea;
	}

	public float getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(float totalArea) {
		this.totalArea = totalArea;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getFitmentType() {
		return fitmentType;
	}

	public void setFitmentType(String fitmentType) {
		this.fitmentType = fitmentType;
	}

	public String getHouseRoomFeature() {
		return houseRoomFeature;
	}

	public void setHouseRoomFeature(String houseRoomFeature) {
		this.houseRoomFeature = houseRoomFeature;
	}

	public String getHouseConfiuTation() {
		return houseConfiuTation;
	}

	public void setHouseConfiuTation(String houseConfiuTation) {
		this.houseConfiuTation = houseConfiuTation;
	}



	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImgList() {
		return imgList;
	}

	public void setImgList(String imgList) {
		this.imgList = imgList;
	}

	public String getAgentUname() {
		return agentUname;
	}

	public void setAgentUname(String agentUname) {
		this.agentUname = agentUname;
	}

	public String getAgentUphone() {
		return agentUphone;
	}

	public void setAgentUphone(String agentUphone) {
		this.agentUphone = agentUphone;
	}

	public String getAgentPost() {
		return agentPost;
	}

	public void setAgentPost(String agentPost) {
		this.agentPost = agentPost;
	}

	public String getAgentDepartment() {
		return agentDepartment;
	}

	public void setAgentDepartment(String agentDepartment) {
		this.agentDepartment = agentDepartment;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
