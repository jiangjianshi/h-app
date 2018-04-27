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
public class HousePublishDto {

	/**
	 * x坐标 纬度
	 */
	private String positionX;

	/**
	 * y坐标 经度
	 */
	private String positionY;

	/**
	 * 小区名称
	 */
	private String communityName;

	/**
	 * 租金月租金，单位为分
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
	private String companyId; //2017-07-03 14:44:59  jjs 由int改为String

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
	 * 居室数量
	 */
	private int bedroomNum;

	/**
	 * 起居室数量
	 */
	private int livingroomNum;

	/**
	 * 厨房数量
	 */
	private int kitchenNum;

	/**
	 * 卫生间数量
	 */
	private int toiletNum;

	/**
	 * 阳台数量
	 */
	private int balconyNum;

	/**
	 * 楼栋编号
	 */
	private String buildingNo;

	/**
	 * 单元号
	 */
	private String unitNo;

	/**
	 * 门牌号
	 */
	private String houseNo;

	/**
	 * 楼栋名
	 */
	private String buildingName;

	/**
	 * 面积
	 */
	private float area;

	/**
	 * 所在楼层，物理楼层
	 */
	private String flowNo;

	/**
	 * 总楼层数量
	 */
	private String flowTotal;

	/**
	 * 朝向
	 */
	private int orientation;

	/**
	 * 建筑类型
	 */
	private int buildingType;;

	/**
	 * 装修档次
	 */
	private int fitmentType;

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
	 * 租住类型
	 */
	private int entireRent;

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
	 * 商圈
	 */
	private String bizName;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 集中式房源编号
	 */
	private String focusCode;

	/**
	 * 房源类型
	 */
	private int houseType;

	/**
	 * saas发布类型
	 */
	private int pubType;

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

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
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

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getFitmentType() {
		return fitmentType;
	}

	public void setFitmentType(int fitmentType) {
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

	public int getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
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

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
	

}
