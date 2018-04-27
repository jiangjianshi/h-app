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
@Table(name = "t_house_detail_extend_qft")
public class BdHouseDetailQft {

	public BdHouseDetailQft() {
	}

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 合作公寓的房屋ID
	 */
	@Column(name = "f_out_house_id")
	private String outHouseId;

	/**
	 * 分配给合作公寓的接入ID与f_out_house_id确定唯一房源
	 */
	@Column(name = "f_app_id")
	private String appId;


	/**
	 * 合作公寓的房屋图片url列表，图片大小小于5M，房源图片数量不超过20张。
	 */
	@Column(name = "f_pic_url_list")
	private String picUrlList;

	/**
	 * 出租方式
	 */
	@Column(name = "f_rent_type")
	private int rentType;

	/**
	 * 房屋户型-室
	 */
	@Column(name = "f_bed_room_num")
	private int bedRoomNum;

	/**
	 * 房屋户型-厅
	 */
	@Column(name = "f_living_room_num")
	private int livingRoomNum;

	/**
	 * 房屋户型-卫
	 */
	@Column(name = "f_toilet_num")
	private int toiletNum;

	/**
	 * 房屋面积，最多支持两位小数
	 */
	@Column(name = "f_rent_room_area")
	private float rentRoomArea;

	/**
	 * 出租类型，枚举值31:主卧 32:次卧 33:不区分主次
	 */
	@Column(name = "f_bed_room_type")
	private String bedRoomType;

	/**
	 * 房间名称
	 */
	@Column(name = "f_room_name")
	private String roomName;

	/**
	 * 房间编码
	 */
	@Column(name = "f_room_code")
	private String roomCode;

	/**
	 * 朝向
	 */
	@Column(name = "f_face_to_type")
	private String faceToType;

	/**
	 * 楼层总数
	 */
	@Column(name = "f_total_floor")
	private int totalFloor;

	/**
	 * 房间所在楼层
	 */
	@Column(name = "f_house_floor")
	private int houseFloor;

	/**
	 * 房间标签
	 */
	@Column(name = "f_feature_tag")
	private String featureTag;

	/**
	 * 房屋配置
	 */
	@Column(name = "f_detail_point")
	private String detailPoint;

	/**
	 * 公寓配套服务
	 */
	@Column(name = "f_service_point")
	private String servicePoint;

	/**
	 * 月租金
	 */
	@Column(name = "f_month_rent")
	private int monthRent;


	/**
	 * 起租时间
	 */
	@Column(name = "f_rent_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rentStartDate;

	/**
	 * 是否支持短租
	 */
	@Column(name = "f_short_rent")
	private int shortRent;

	/**
	 * 房屋
	 */
	@Column(name = "f_provice")
	private String provice;

	/**
	 * 房屋所在城市
	 */
	@Column(name = "f_city_name")
	private String cityName;

	/**
	 * 房屋所在区县
	 */
	@Column(name = "f_county_name")
	private String countyName;

	/**
	 * 房屋所在商圈
	 */
	@Column(name = "f_area_name")
	private String areaName;


	/**
	 * 房屋所在小区名称
	 */
	@Column(name = "f_district_name")
	private String districtName;

	/**
	 * 房屋所在小区详细地址
	 */
	@Column(name = "f_street")
	private String street;

	/**
	 * 出租房屋门牌地址
	 */
	@Column(name = "f_address")
	private String address;

	/**
	 * 房屋附近地铁站所在线路名称 1号线
	 */
	@Column(name = "f_subway_line")
	private String subwayLine;

	/**
	 * f_subway_station
	 */
	@Column(name = "f_subway_station")
	private String subwayStation;

	/**
	 * 房间描述
	 */
	@Column(name = "f_house_desc")
	private String houseDesc;

	/**
	 * 房间位置坐标-经度
	 */
	@Column(name = "f_x_code")
	private String xCode;

	/**
	 * 房间位置坐标-纬度
	 */
	@Column(name = "f_y_code")
	private String yCode;

	/**
	 * 房管员手机号
	 */
	@Column(name = "f_agent_phone")
	private String agentPhone;

	/**
	 * 预约短信接收到的手机号
	 */
	@Column(name = "f_order_phone")
	private String orderPhone;

	/**
	 * 房管员姓名
	 */
	@Column(name = "f_agent_name")
	private String agentName;

	/**
	 * 合作公寓
	 */
	@Column(name = "f_video_url")
	private String videoUrl;

	/**
	 * 房源建筑年代
	 */
	@Column(name = "f_build_year")
	private int buildYear;

	/**
	 * 小区供暖方式
	 */
	@Column(name = "f_supply_heating")
	private int supplyHeating;

	/**
	 * 小区绿化率
	 */
	@Column(name = "f_green_ratio")
	private int greenRatio;

	/**
	 * 小区建筑类型
	 */
	@Column(name = "f_build_type")
	private int buildType;

	/**
	 * 房源状态
	 */
	@Column(name = "f_state")
	private String state;

	/**
	 * 改动原因
	 */
	@Column(name = "f_memo")
	private String memo;
	
	/**
	 * 处理标志，将数据转换到house_detail表；0 :未转换，1：已转换
	 */
	@Column(name = "f_transfer_flag")
	private int transferFlag;

	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	/**
	 * 房源唯一标识
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;

	/**
	 * 公司ID
	 */
	@Column(name = "f_company_id")
	private String companyId;

	/**
	 * 公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;

	/**
	 * 装修状态
	 */
	@Column(name = "f_fitment_state")
	private String fitmentState;

	/**
	 * 户型
	 */
	@Column(name = "f_live_bed_totile")
	private String liveBedTotile;

	/**
	 * 根据地址查询的经度
	 */
	@Column(name = "f_lng")
	private String lng;

	/**
	 * 根据地质查询的纬度
	 */
	@Column(name = "f_lat")
	private String lat;

	/**
	 * 位置的附加信息
	 */
	@Column(name = "f_precise")
	private int precise;

	/**
	 * 可信度
	 */
	@Column(name = "f_confidence")
	private int confidence;

	/**
	 * 地址类型
	 */
	@Column(name = "f_level")
	private String level;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOutHouseId() {
		return outHouseId;
	}

	public void setOutHouseId(String outHouseId) {
		this.outHouseId = outHouseId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(String picUrlList) {
		this.picUrlList = picUrlList;
	}

	public int getRentType() {
		return rentType;
	}

	public void setRentType(int rentType) {
		this.rentType = rentType;
	}

	public int getBedRoomNum() {
		return bedRoomNum;
	}

	public void setBedRoomNum(int bedRoomNum) {
		this.bedRoomNum = bedRoomNum;
	}

	public int getLivingRoomNum() {
		return livingRoomNum;
	}

	public void setLivingRoomNum(int livingRoomNum) {
		this.livingRoomNum = livingRoomNum;
	}

	public int getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(int toiletNum) {
		this.toiletNum = toiletNum;
	}

	public float getRentRoomArea() {
		return rentRoomArea;
	}

	public void setRentRoomArea(float rentRoomArea) {
		this.rentRoomArea = rentRoomArea;
	}

	public String getBedRoomType() {
		return bedRoomType;
	}

	public void setBedRoomType(String bedRoomType) {
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

	public String getFaceToType() {
		return faceToType;
	}

	public void setFaceToType(String faceToType) {
		this.faceToType = faceToType;
	}



	public int getTotalFloor() {
		return totalFloor;
	}

	public void setTotalFloor(int totalFloor) {
		this.totalFloor = totalFloor;
	}

	public int getHouseFloor() {
		return houseFloor;
	}

	public void setHouseFloor(int houseFloor) {
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

	public int getMonthRent() {
		return monthRent;
	}

	public void setMonthRent(int monthRent) {
		this.monthRent = monthRent;
	}

	public Date getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(Date rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public int getShortRent() {
		return shortRent;
	}

	public void setShortRent(int shortRent) {
		this.shortRent = shortRent;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
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

	public String getxCode() {
		return xCode;
	}

	public void setxCode(String xCode) {
		this.xCode = xCode;
	}

	public String getyCode() {
		return yCode;
	}

	public void setyCode(String yCode) {
		this.yCode = yCode;
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

	public int getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}

	public int getSupplyHeating() {
		return supplyHeating;
	}

	public void setSupplyHeating(int supplyHeating) {
		this.supplyHeating = supplyHeating;
	}

	public int getGreenRatio() {
		return greenRatio;
	}

	public void setGreenRatio(int greenRatio) {
		this.greenRatio = greenRatio;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getTransferFlag() {
		return transferFlag;
	}

	public void setTransferFlag(int transferFlag) {
		this.transferFlag = transferFlag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHouseSellId() {
		return houseSellId;
	}

	public void setHouseSellId(String houseSellId) {
		this.houseSellId = houseSellId;
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


	public String getFitmentState() {
		return fitmentState;
	}

	public void setFitmentState(String fitmentState) {
		this.fitmentState = fitmentState;
	}

	public String getLiveBedTotile() {
		return liveBedTotile;
	}

	public void setLiveBedTotile(String liveBedTotile) {
		this.liveBedTotile = liveBedTotile;
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

	public int getPrecise() {
		return precise;
	}

	public void setPrecise(int precise) {
		this.precise = precise;
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "BdHouseDetailQft [id=" + id + ", outHouseId=" + outHouseId + ", appId=" + appId + ", picUrlList="
				+ picUrlList + ", rentType=" + rentType + ", bedRoomNum=" + bedRoomNum + ", livingRoomNum="
				+ livingRoomNum + ", toiletNum=" + toiletNum + ", rentRoomArea=" + rentRoomArea + ", bedRoomType="
				+ bedRoomType + ", roomName=" + roomName + ", roomCode=" + roomCode + ", faceToType=" + faceToType
				+ ", totalFloor=" + totalFloor + ", houseFloor=" + houseFloor + ", featureTag=" + featureTag
				+ ", detailPoint=" + detailPoint + ", servicePoint=" + servicePoint + ", monthRent=" + monthRent
				+ ", rentStartDate=" + rentStartDate + ", shortRent=" + shortRent + ", provice=" + provice
				+ ", cityName=" + cityName + ", countyName=" + countyName + ", areaName=" + areaName + ", districtName="
				+ districtName + ", street=" + street + ", address=" + address + ", subwayLine=" + subwayLine
				+ ", subwayStation=" + subwayStation + ", houseDesc=" + houseDesc + ", xCode=" + xCode + ", yCode="
				+ yCode + ", agentPhone=" + agentPhone + ", orderPhone=" + orderPhone + ", agentName=" + agentName
				+ ", videoUrl=" + videoUrl + ", buildYear=" + buildYear + ", supplyHeating=" + supplyHeating
				+ ", greenRatio=" + greenRatio + ", buildType=" + buildType + ", state=" + state + ", memo=" + memo
				+ ", transferFlag=" + transferFlag + ", updateTime=" + updateTime + ", createTime=" + createTime
				+ ", houseSellId=" + houseSellId + ", companyId=" + companyId + ", companyName=" + companyName
				+ ", fitmentState=" + fitmentState + ", liveBedTotile=" + liveBedTotile + ", lng=" + lng + ", lat="
				+ lat + ", precise=" + precise + ", confidence=" + confidence + ", level=" + level + "]";
	}

	
	
}