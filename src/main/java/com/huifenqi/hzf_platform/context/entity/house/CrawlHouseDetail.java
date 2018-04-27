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
@Table(name = "t_house_detail_crawl")
public class CrawlHouseDetail {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房间更新时间
	 */
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	/**
	 * 电话
	 */
	@Column(name = "F_phone_num")
	private String phoneNum;
	/**
	 * 租金
	 */
	@Column(name = "f_price")
	private String price;
	
	/**
	 * 地址
	 */
	@Column(name = "f_address")
	private String address;
	/**
	 * 出租方式
	 */
	@Column(name = "f_rent_type")
	private String rentType;
	/**
	 * 距离地铁距离
	 */
	@Column(name = "f_traffic")
	private String traffic;
	/**
	 * 爬虫提取时间
	 */
	@Column(name = "f_crawl_time")
	private String crawlTime;
	/**
	 * 房间信息
	 */
	@Column(name = "f_hall_room")
	private String hallRoom;
	/**
	 * 城市信息
	 */
	@Column(name = "f_city")
	private String city;
	/**
	 * 商圈
	 */
	@Column(name = "f_zone")
	private String zone;
	/**
	 * 区
	 */
	@Column(name = "f_district")
	private String district;
	/**
	 * 爬取地址
	 */
	@Column(name = "f_url")
	private String url;
	/**
	 * 商圈
	 */
	@Column(name = "f_floor")
	private String floor;
	/**
	 * 面积
	 */
	@Column(name = "f_area")
	private String area;
	/**
	 * 公寓信息
	 */
	@Column(name = "f_department_name")
	private String departmentName;
	/**
	 * 公寓名称
	 */
	@Column(name = "f_department")
	private String department;
	/**
	 * 公寓Id
	 */
	@Column(name = "f_department_id")
	private String departmentId;

	/**
	 * 
	 * 是否收集
	 */
	@Column(name = "f_run")
	private int isRun;
	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;
	/**
	 * 收集时间
	 */
	@Column(name = "f_collect_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date collectTime;
	
	/**
	 * 房源ID
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;
	
	/**
	 * 房间ID
	 */
	@Column(name = "f_room_id")
	private int roomId;
	/**
	 * 房源标签
	 */
	@Column(name = "f_tags")
	private String tags;
	/**
	 * 房源图片
	 */
	@Column(name = "f_image_urls")
	private String imageUrls;
	/**
	 * 房屋配置
	 */
	@Column(name = "f_facilities")
	private String facilities;
	/**
	 * 配套服务
	 */
	@Column(name = "f_services")
	private String services;
	/**
	 * 房源描述
	 */
	@Column(name = "f_house_desc")
	private String houseDesc;
	/**
	 * 经度
	 */
	@Column(name = "f_longitude")
	private String longitude;
	/**
	 * 纬度
	 */
	@Column(name = "f_latitude")
	private String latitude;
	
	/**
	 * 公寓logo图片
	 */
	@Column(name = "f_department_logo_url")
	private String deparmentLogoUrl;
	/**
	 * 支付方式
	 */
	@Column(name = "f_rent_montyly")
	private String rentMontyly;
	/**
	 * 是否可以短租
	 */
	@Column(name = "f_allow_short_rent")
	private String allowShortRent;
	
	/**
	 * 房源ID
	 */
	@Column(name = "f_page_type")
	private int pagetype;
	
	/**
	 * 业务编号
	 */
	@Column(name = "f_appid")
	private int appId;
	
	/**
	 * 爬虫下架时间
	 */
	@Column(name = "f_coll_del_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date collDelTime;
	
	/**
	 * 下架状态
	 */
	@Column(name = "f_del_flag")
	private int delFlag;
	/**
	 * 管家
	 */
	@Column(name = "f_broker_name")
	private String brokerName;
	
	/**
	 * 支付方式
	 */
	@Column(name = "f_pay_type")
	private String payType;
	/**
	 * 房源类型
	 */
	@Column(name = "f_house_type")
	private String houseType;
	/**
	 * 小区名称
	 */
	@Column(name = "f_community_name")
	private String communityName;
	/**
	 * 押金
	 */
	@Column(name = "f_other_fee")
	private String otherFee;

	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRentType() {
		return rentType;
	}
	public void setRentType(String rentType) {
		this.rentType = rentType;
	}
	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	public String getCrawlTime() {
		return crawlTime;
	}
	public void setCrawlTime(String crawlTime) {
		this.crawlTime = crawlTime;
	}
	public String getHallRoom() {
		return hallRoom;
	}
	public void setHallRoom(String hallRoom) {
		this.hallRoom = hallRoom;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getHouseSellId() {
		return houseSellId;
	}
	public void setHouseSellId(String houseSellId) {
		this.houseSellId = houseSellId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}
	public String getFacilities() {
		return facilities;
	}
	public void setFacilities(String facilities) {
		this.facilities = facilities;
	}
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}
	public String getHouseDesc() {
		return houseDesc;
	}
	public void setHouseDesc(String houseDesc) {
		this.houseDesc = houseDesc;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getDeparmentLogoUrl() {
		return deparmentLogoUrl;
	}
	public void setDeparmentLogoUrl(String deparmentLogoUrl) {
		this.deparmentLogoUrl = deparmentLogoUrl;
	}
	public String getRentMontyly() {
		return rentMontyly;
	}
	public void setRentMontyly(String rentMontyly) {
		this.rentMontyly = rentMontyly;
	}
	public String getAllowShortRent() {
		return allowShortRent;
	}
	public void setAllowShortRent(String allowShortRent) {
		this.allowShortRent = allowShortRent;
	}
	public int getPagetype() {
		return pagetype;
	}
	public void setPagetype(int pagetype) {
		this.pagetype = pagetype;
	}

	public Date getCollDelTime() {
		return collDelTime;
	}
	public void setCollDelTime(Date collDelTime) {
		this.collDelTime = collDelTime;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	
}
