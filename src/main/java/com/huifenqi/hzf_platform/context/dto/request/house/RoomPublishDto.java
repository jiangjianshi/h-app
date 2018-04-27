/** 
 * Project Name: hzf_platform 
 * File Name: RoomDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto 
 * Date: 2016年4月26日下午8:41:31 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.Date;

/**
 * ClassName: RoomDto date: 2016年4月26日 下午8:41:31 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class RoomPublishDto {

	/**
	 * 房源Id
	 */
	private String sellId;

	/**
	 * 月租金，单位为分
	 */
	private int price;

	/**
	 * 服务费 ，单位为分
	 */
	private int bonus;

	/**
	 * 押金 ，单位为分
	 */
	private int deposit;

	/**
	 * 是否有钥匙
	 */
	private int hasKey;

	/**
	 * 经纪公司id
	 */
	private String companyId;

	/**
	 * 经纪公司名称
	 */
	private String companyName;

	/**
	 * 经纪人ID
	 */
	private int agencyId;

	/**
	 * 经纪人联系电话
	 */
	private String agencyPhone;

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
	 * 经纪人头像
	 */
	private String agencyAvatar;

	/**
	 * 房源状态
	 */
	private int status;

	/**
	 * 面积
	 */
	private float area;

	/**
	 * 朝向
	 */
	private int orientation;

	/**
	 * 装修档次
	 */
	private int fitmentType;

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
	private int insurance;

	/**
	 * 入住时间
	 */
	private Date checkInTime;

	/**
	 * 押几
	 */
	private int depositMonth;

	/**
	 * 付几
	 */
	private int periodMonth;

	/**
	 * 主要室内设施
	 */
	private String settings;

	/**
	 * 次要设施
	 */
	private String settingsAddon;

	/**
	 * 房源描述
	 */
	private String desc;

	/**
	 * 房源照片
	 */
	private String imgs;

	/**
	 * 房间名称
	 */
	private String roomName;
	
	/**
	 * 房间类型
	 */
	private int roomType;

	/**
	 * 发布类型
	 */
	private int pubType;


	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
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

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getHasKey() {
		return hasKey;
	}

	public void setHasKey(int hasKey) {
		this.hasKey = hasKey;
	}


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getAgencyAvatar() {
		return agencyAvatar;
	}

	public void setAgencyAvatar(String agencyAvatar) {
		this.agencyAvatar = agencyAvatar;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getFitmentType() {
		return fitmentType;
	}

	public void setFitmentType(int fitmentType) {
		this.fitmentType = fitmentType;
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

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
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

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getSettingsAddon() {
		return settingsAddon;
	}

	public void setSettingsAddon(String settingsAddon) {
		this.settingsAddon = settingsAddon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getPubType() {
		return pubType;
	}

	public void setPubType(int pubType) {
		this.pubType = pubType;
	}

	@Override
	public String toString() {
		return "RoomPublishDto [sellId=" + sellId + ", price=" + price + ", bonus=" + bonus + ", deposit=" + deposit
				+ ", hasKey=" + hasKey + ", companyId=" + companyId + ", companyName=" + companyName + ", agencyId="
				+ agencyId + ", agencyPhone=" + agencyPhone + ", agencyName=" + agencyName + ", agencyIntroduce="
				+ agencyIntroduce + ", agencyGender=" + agencyGender + ", agencyAvatar=" + agencyAvatar + ", status="
				+ status + ", area=" + area + ", orientation=" + orientation + ", fitmentType=" + fitmentType
				+ ", toilet=" + toilet + ", balcony=" + balcony + ", insurance=" + insurance + ", checkInTime="
				+ checkInTime + ", depositMonth=" + depositMonth + ", periodMonth=" + periodMonth + ", settings="
				+ settings + ", settingsAddon=" + settingsAddon + ", desc=" + desc + ", imgs=" + imgs + ", roomName="
				+ roomName + ", roomType=" + roomType + "]";
	}


}
