package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huifenqi.hzf_platform.context.enums.bd.FaceToTypeEnum;
import com.huifenqi.hzf_platform.context.enums.bd.HouseResourceStatusEnum;
import com.huifenqi.hzf_platform.context.enums.bd.HouseTagEnum;
import com.huifenqi.hzf_platform.context.enums.bd.RentTypeEnum;
import com.huifenqi.hzf_platform.context.enums.bd.RoomTypeEnum;
public class BdSysHouseDetailToLocalDto {

	/**
	 * 
	 */
	private Long id;
	/**
	 * 合作公寓的房屋ID
	 */
	private String outHouseId;

	/**
	 * 房源唯一标识
	 */
	private String houseSellId;

	/**
	 * 分配给合作公寓的接入ID与f_out_house_id确定唯一房源
	 */
	private String appId;
	
	/**
	 * 分配给合作公寓ak
	 */
	private String ak;

	/**
	 * 合作公寓的房屋图片url列表，图片大小小于5M，房源图片数量不超过20张。
	 */
	private List<Object> picUrlList;

	/**
	 * 出租方式
	 */
	private Integer rentType;

	/**
	 * 房屋户型-室
	 */
	private Integer bedRoomNum;

	/**
	 * 房屋户型-厅
	 */
	private Integer livingRoomNum;

	/**
	 * 房屋户型-卫
	 */
	private Integer toiletNum;

	/**
	 * 房屋面积，最多支持两位小数
	 */
	private float rentRoomArea;

	/**
	 * 出租类型，枚举值31:主卧 32:次卧 33:不区分主次
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
	 * 楼层总数
	 */
	private Integer totalFloor;

	/**
	 * 房间所在楼层
	 */
	private Integer houseFloor;

	/**
	 * 房间标签
	 */
	private String featureTag;

	/**
	 * 房屋配置
	 */
	private String detailPoint;

	/**
	 * 房屋次要配置 ***
	 */
	private String settingsAddon;

	/**
	 * 公寓配套服务
	 */
	private String servicePoint;

	/**
	 * 月租金
	 */
	private Integer monthRent;

	/**
	 * 起租时间
	 */
	private String rentStartDate;

	/**
	 * 是否支持短租
	 */
	private Integer shortRent;

	/**
	 * 房屋
	 */
	private String provice;

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
	 * 房屋所在小区名称
	 */
	private String districtName;

	/**
	 * 房屋所在小区详细地址
	 */
	private String street;

	/**
	 * 出租房屋门牌地址
	 */
	private String address;

	/**
	 * 房屋附近地铁站所在线路名称 1号线
	 */
	private String subwayLine;

	/**
	 * f_subway_station
	 */
	private String subwayStation;

	/**
	 * 房间描述
	 */
	private String houseDesc;

	/**
	 * 房间位置坐标-经度
	 */
	private String xCoord;

	/**
	 * 房间位置坐标-纬度
	 */
	private String yCoord;

	/**
	 * 房管员手机号
	 */
	private String agentPhone;

	/**
	 * 预约短信接收到的手机号
	 */
	private String orderPhone;

	/**
	 * 房管员姓名
	 */
	private String agentName;

	/**
	 * 合作公寓
	 */
	private String videoUrl;

	/**
	 * 房源建筑年代
	 */
	private Integer buildYear;

	/**
	 * 小区供暖方式
	 */
	private Integer supplyHeating;

	/**
	 * 小区绿化率
	 */
	private Integer greenRatio;

	/**
	 * 小区建筑类型
	 */
	private Integer buildType;

	/**
	 * 房源状态
	 */
	private String status;

	/**
	 * 改动原因
	 */
	private String memo;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 更新时间
	 */
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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


	public List<Object> getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(List<Object> picUrlList) {
		this.picUrlList = picUrlList;
	}

	public Integer getRentType() {

		if (rentType != null) {
			return RentTypeEnum.getBdCode(rentType);
		}
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

	public float getRentRoomArea() {
		return rentRoomArea;
	}

	public void setRentRoomArea(float rentRoomArea) {
		this.rentRoomArea = rentRoomArea;
	}

	public Integer getBedRoomType() {
		if (bedRoomType != null) {
			return RoomTypeEnum.getBdCode(bedRoomType);
		}
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

		if (faceToType != null) {
			return FaceToTypeEnum.getBdCode(faceToType);
		}
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
		if (!StringUtils.isEmpty(featureTag)) {
			return HouseTagEnum.getBdCodes(featureTag);
		}
		return featureTag;
	}

	public void setFeatureTag(String featureTag) {
		this.featureTag = featureTag;
	}

	public String getDetailPoint() {
//		if (!StringUtils.isEmpty(detailPoint)) {
//			return SettingsEnum.getCodes(detailPoint);
//		}
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


	public String getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(String rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public Integer getShortRent() {
		return shortRent;
	}

	public void setShortRent(Integer shortRent) {
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

	public String getStatus() {
		if (!StringUtils.isEmpty(status)) {
			Integer bdCode = Integer.parseInt(status);
			Integer status = HouseResourceStatusEnum.getBdCode(bdCode);
			return String.valueOf(status);
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getSettingsAddon() {
//		// 模拟百度 83 wifi
//		if (detailPoint.indexOf("83") > 0) {
//			return SettingsEnum.wifi.name();
//		}
		return settingsAddon;
	}

	public void setSettingsAddon(String settingsAddon) {
		this.settingsAddon = settingsAddon;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}
	
}
