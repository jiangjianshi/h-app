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
 * ClassName: HouseDetail date: 2016年4月25日 下午4:05:50 Description:房源详情
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_detail")
public class HouseDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 楼栋编号
	 */
	@Column(name = "f_building_no")
	private String buildingNo;

	/**
	 * 单元号
	 */
	@Column(name = "f_unit_no")
	private String unitNo;

	/**
	 * 所在楼层
	 */
	@Column(name = "f_flow_no")
	private String flowNo;

	/**
	 * 总楼层
	 */
	@Column(name = "f_flow_total")
	private String flowTotal;

	/**
	 * 门牌号
	 */
	@Column(name = "f_house_no")
	private String houseNo;

	/**
	 * 建筑面积
	 */
	@Column(name = "f_area")
	private float area;

	/**
	 * 朝向
	 */
	@Column(name = "f_orientations")
	private int orientations;

	/**
	 * 卧室数量
	 */
	@Column(name = "f_bedroom_nums")
	private int bedroomNum;

	/**
	 * 起居室数量
	 */
	@Column(name = "f_livingroom_nums")
	private int livingroomNum;

	/**
	 * 厨房数量
	 */
	@Column(name = "f_kitchen_nums")
	private int kitchenNum;

	/**
	 * 卫生间数量
	 */
	@Column(name = "f_toilet_nums")
	private int toiletNum;

	/**
	 * 阳台数量
	 */
	@Column(name = "f_balcony_nums")
	private int balconyNum;

	/**
	 * 省份
	 */
	@Column(name = "f_province")
	private String province;

	/**
	 * 城市
	 */
	@Column(name = "f_city")
	private String city;

	/**
	 * 行政区
	 */
	@Column(name = "f_district")
	private String district;

	/**
	 * 商圈
	 */
	@Column(name = "f_bizname")
	private String bizName;

	/**
	 * 房源用途
	 */
	@Column(name = "f_house_function")
	private int houseFunction;

	/**
	 * 详细地址
	 */
	@Column(name = "f_address")
	private String address;

	/**
	 * 最近地铁站
	 */
	@Column(name = "f_subway")
	private String subway;

	/**
	 * 到最近地铁站距离
	 */
	@Column(name = "f_subway_distance")
	private int subwayDistance;

	/**
	 * 附近公交站
	 */
	@Column(name = "f_bus_stations")
	private String busStations;

	/**
	 * 周边
	 */
	@Column(name = "f_surround")
	private String surround;

	/**
	 * 小区名称
	 */
	@Column(name = "f_community_name")
	private String communityName;

	/**
	 * 城市id
	 */
	@Column(name = "f_city_id")
	private long cityId;

	/**
	 * 行政区id
	 */
	@Column(name = "f_district_id")
	private long districtId;

	/**
	 * 商圈id
	 */
	@Column(name = "f_biz_id")
	private long bizId;

	/**
	 * 地铁线路id
	 */
	@Column(name = "f_subway_line_id")
	private String subwayLineId;

	/**
	 * 地铁站id
	 */
	@Column(name = "f_subway_station_id")
	private String subwayStationId;

	/**
	 * 楼栋名称
	 */
	@Column(name = "f_building_name")
	private String buildingName;

	/**
	 * 百度坐标，经度
	 */
	@Column(name = "f_baidu_lo")
	private String baiduLongitude;

	/**
	 * 百度坐标，纬度
	 */
	@Column(name = "f_baidu_la")
	private String baiduLatitude;

	/**
	 * 建筑类型
	 */
	@Column(name = "f_building_type")
	private int buildingType;

	/**
	 * 建筑时间
	 */
	@Column(name = "f_building_year")
	private int buildingYear;

	/**
	 * 装修档次
	 */
	@Column(name = "f_decoration")
	private int decoration;

	/**
	 * 是否有独立卫生间
	 */
	@Column(name = "f_toilet")
	private int toilet;

	/**
	 * 是否有独立阳台
	 */
	@Column(name = "f_balcony")
	private int balcony;

	/**
	 * 是否有家财险
	 */
	@Column(name = "f_insurance")
	private int insurance;

	/**
	 * 租住类型
	 */
	@Column(name = "f_entire_rent")
	private int entireRent;

	/**
	 * 房源描述
	 */
	@Column(name = "f_comment")
	private String comment;

	/**
	 * 房屋标签，客户自定义标签
	 */
	@Column(name = "f_house_tag")
	private String houseTag;

	/**
	 * 来源
	 */
	@Column(name = "f_source")
	private String source;

	/**
	 * 是否置顶
	 */
	// @Column(name = "f_is_top")
	@Column(name = "f_is_top", updatable = false)
	private int isTop;

	/**
	 * 发布类型
	 */
	@Column(name = "f_pub_type")
	private int pubType;

	/**
	 * 集中式房源编号
	 */
	@Column(name = "f_focus_code")
	private String focusCode;

	/**
	 * 是否删除
	 */
	@Column(name = "f_house_type")
	private int houseType;

	/**
	 * 是否已审核
	 */
	@Column(name = "f_approve_status")
	private int approveStatus;

	/**
	 * 是否支持月付 0:不支持；1：支持
	 */
	@Column(name = "f_is_pay_month")
	private int isPayMonth;

	/**
	 * 数据是否采集 0:未采集；1：已采集
	 */
	@Column(name = "f_run")
	private int isRun;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

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

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
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

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public int getOrientations() {
		return orientations;
	}

	public void setOrientations(int orientations) {
		this.orientations = orientations;
	}

	public int getBedroomNum() {
		return bedroomNum;
	}

	public void setBedroomNum(int bedroomNum) {
		this.bedroomNum = bedroomNum;
	}

	public int getLivingroomNum() {
		return livingroomNum;
	}

	public void setLivingroomNum(int livingroomNum) {
		this.livingroomNum = livingroomNum;
	}

	public int getKitchenNum() {
		return kitchenNum;
	}

	public void setKitchenNum(int kitchenNum) {
		this.kitchenNum = kitchenNum;
	}

	public int getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(int toiletNum) {
		this.toiletNum = toiletNum;
	}

	public int getBalconyNum() {
		return balconyNum;
	}

	public void setBalconyNum(int balconyNum) {
		this.balconyNum = balconyNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public int getHouseFunction() {
		return houseFunction;
	}

	public void setHouseFunction(int houseFunction) {
		this.houseFunction = houseFunction;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubway() {
		return subway;
	}

	public void setSubway(String subway) {
		this.subway = subway;
	}

	public int getSubwayDistance() {
		return subwayDistance;
	}

	public void setSubwayDistance(int subwayDistance) {
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

	public String getSubwayLineId() {
		return subwayLineId;
	}

	public void setSubwayLineId(String subwayLineId) {
		this.subwayLineId = subwayLineId;
	}

	public String getSubwayStationId() {
		return subwayStationId;
	}

	public void setSubwayStationId(String subwayStationId) {
		this.subwayStationId = subwayStationId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
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

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getBuildingYear() {
		return buildingYear;
	}

	public void setBuildingYear(int buildingYear) {
		this.buildingYear = buildingYear;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
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

	public int getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHouseTag() {
		return houseTag;
	}

	public void setHouseTag(String houseTag) {
		this.houseTag = houseTag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getFocusCode() {
		return focusCode;
	}

	public void setFocusCode(String focusCode) {
		this.focusCode = focusCode;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public int getIsPayMonth() {
		return isPayMonth;
	}

	public void setIsPayMonth(int isPayMonth) {
		this.isPayMonth = isPayMonth;
	}

	public int getIsRun() {
		return isRun;
	}

	public void setIsRun(int isRun) {
		this.isRun = isRun;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
	

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

	public int getPubType() {
		return pubType;
	}

	public void setPubType(int pubType) {
		this.pubType = pubType;
	}

	@Override
	public String toString() {
		return "HouseDetail [id=" + id + ", sellId=" + sellId + ", buildingNo=" + buildingNo + ", unitNo=" + unitNo
				+ ", flowNo=" + flowNo + ", flowTotal=" + flowTotal + ", houseNo=" + houseNo + ", area=" + area
				+ ", orientations=" + orientations + ", bedroomNum=" + bedroomNum + ", livingroomNum=" + livingroomNum
				+ ", kitchenNum=" + kitchenNum + ", toiletNum=" + toiletNum + ", balconyNum=" + balconyNum
				+ ", province=" + province + ", city=" + city + ", district=" + district + ", bizName=" + bizName
				+ ", houseFunction=" + houseFunction + ", address=" + address + ", subway=" + subway
				+ ", subwayDistance=" + subwayDistance + ", busStations=" + busStations + ", surround=" + surround
				+ ", communityName=" + communityName + ", cityId=" + cityId + ", districtId=" + districtId + ", bizId="
				+ bizId + ", subwayLineId=" + subwayLineId + ", subwayStationId=" + subwayStationId + ", buildingName="
				+ buildingName + ", baiduLongitude=" + baiduLongitude + ", baiduLatitude=" + baiduLatitude
				+ ", buildingType=" + buildingType + ", buildingYear=" + buildingYear + ", decoration=" + decoration
				+ ", toilet=" + toilet + ", balcony=" + balcony + ", insurance=" + insurance + ", entireRent="
				+ entireRent + ", comment=" + comment + ", houseTag=" + houseTag + ", source=" + source + ", isTop="
				+ isTop + ", focusCode=" + focusCode + ", houseType=" + houseType + ", approveStatus=" + approveStatus
				+ ", isRun=" + isRun + ", isDelete=" + isDelete + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
