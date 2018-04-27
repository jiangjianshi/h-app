/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ClassName: BdHouseDetail date: 2017年4月13日 下午4:05:50 Description:房源详情
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_detail_extend_asz")
public class HouseDetailAsz {

	public HouseDetailAsz() {
	}

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 找房房源编号
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;

	/**
	 * 公寓房源编号ID
	 */
	@Column(name = "f_apartment_id")
	private String apartmentId;


	/**
	 * 房源编号
	 */
	@Column(name = "f_apartment_code")
	private String apartmentCode;

	/**
	 * 房源所属房屋
	 */
	@Column(name = "f_house_id")
	private String houseId;

	/**
	 * 出租方式 ENTIRE 整租 SHARE 合租
	 */
	@Column(name = "f_rent_type")
	private String rentType;

	/**
	 * 房间号 出租方式为合租存在
	 */
	@Column(name = "f_room_no")
	private String roomNo;

	/**
	 * 出租状态WAITING_RENT:待出租
	 */
	@Column(name = "f_rent_status")
	private String rentStatus;

	/**
	 * 出租月租金
	 */
	@Column(name = "f_rent_price")
	private String rentPrice;

	/**
	 * 租客信息
	 */
	@Column(name = "f_customer_person")
	private String customerPerson;

	/**
	 * 楼盘名称
	 */
	@Column(name = "f_residential_name")
	private String residentialName;

	/**
	 * 城市ID
	 */
	@Column(name = "f_city_code")
	private String cityCode;

	/**
	 * 城市名称
	 */
	@Column(name = "f_city_name")
	private String cityName;

	/**
	 * 城市ID
	 */
	@Column(name = "f_area_code")
	private String areaCode;

	/**
	 * 区域名称
	 */
	@Column(name = "f_area_name")
	private String areaName;

	/**
	 * 街道地址
	 */
	@Column(name = "f_address")
	private String address;

	/**
	 * 商圈
	 */
	@Column(name = "f_business_circle_multi")
	private String businessCircleMulti;

	/**
	 * 经度
	 */
	@Column(name = "f_lng")
	private String lng;

	/**
	 * 纬度
	 */
	@Column(name = "f_lat")
	private String lat;

	/**
	 * 物业用途
	 */
	@Column(name = "f_property_use")
	private String propertyUse;

	/**
	 * 所属楼层
	 */
	@Column(name = "f_floor")
	private int floor;

	/**
	 * 总楼层
	 */
	@Column(name = "f_ground_floors")
	private int groudFloors;

	/**
	 * 室
	 */
	@Column(name = "f_rooms")
	private int rooms;

	/**
	 * 厅
	 */
	@Column(name = "f_livings")
	private int livings;

	/**
	 * 卫
	 */
	@Column(name = "f_bathrooms")
	private int bathRooms;

	/**
	 * 房源面积
	 */
	@Column(name = "f_build_area")
	private float buildArea;

	/**
	 * 总面积
	 */
	@Column(name = "f_total_area")
	private float totalArea;

	/**
	 * 朝向
	 */
	@Column(name = "f_orientation")
	private String orientation;

	/**
	 * 装修情况
	 */
	@Column(name = "f_fitment_type")
	private String fitmentType;

	/**
	 * 房屋配置
	 */
	@Column(name = "f_house_room_feature")
	private String houseRoomFeature;

	/**
	 * 装修情况
	 */
	@Column(name = "f_house_configu_tation")
	private String houseConfiuTation;

	/**
	 * 房屋描述
	 */
	@Column(name = "f_remark")
	private String remark;

	/**
	 * 图片url
	 */
	@Column(name = "f_img_list")
	private String imgList;

	/**
	 * 房管员
	 */
	@Column(name = "f_agent_uname")
	private String agentUname;


	/**
	 * 经纪人手机号
	 */
	@Column(name = "f_agent_uphone")
	private String agentUphone;

	/**
	 * 所属岗位
	 */
	@Column(name = "f_agent_post")
	private String agentPost;

	/**
	 * 所属部门
	 */
	@Column(name = "f_agent_department")
	private String agentDepartment;

	/**
	 * 上架状态 ONLINE：上架OFFLINE:下架
	 */
	@Column(name = "f_online_status")
	private String onlineStatus;

	/**
	 * 同步状态 0未同步 1已同步
	 */
	@Column(name = "f_transfer_flag")
	private int transferFlag;

	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHouseSellId() {
		return houseSellId;
	}

	public void setHouseSellId(String houseSellId) {
		this.houseSellId = houseSellId;
	}

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

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getGroudFloors() {
		return groudFloors;
	}

	public void setGroudFloors(int groudFloors) {
		this.groudFloors = groudFloors;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getLivings() {
		return livings;
	}

	public void setLivings(int livings) {
		this.livings = livings;
	}

	public int getBathRooms() {
		return bathRooms;
	}

	public void setBathRooms(int bathRooms) {
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

	public int getTransferFlag() {
		return transferFlag;
	}

	public void setTransferFlag(int transferFlag) {
		this.transferFlag = transferFlag;
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