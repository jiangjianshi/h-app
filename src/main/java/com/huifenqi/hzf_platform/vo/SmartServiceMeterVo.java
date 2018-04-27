/** 
 * Project Name: hzf_smart
 * File Name: SmartServiceMeterVo.java
 * Package Name: com.huifenqi.hzf_platform.vo
 * Date: 2018年1月16日日下午9:57:01
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.vo;

import java.math.BigDecimal;

/** 
 * ClassName: SmartServiceMeterVo
 * date: 2018年1月16日 下午9:57:01
 * Description: 
 * 
 * @author Arison
 * @version  
 * @since JDK 1.8 
 */
public class SmartServiceMeterVo {
	
	private long id;
	
	private String ammeterSn;
	
	private int priceId;
	
	private String priceName;
	
	private int price;
	
	private String ammeterName;
	

	private String remark;
	
	//0:离线；1:在线
	private int	onlineState;
	
	private int meterType;
	
	private String createTime;
	
	private BigDecimal remainingMoney;
	
	private String enableTime;

	private String roomName;

	private BigDecimal remainingKwh;

	private int agencyId;

	private String userName;

	private String userPhone;

	private String roomUuid;

	private int houseType;

	private int rentType;
	
	private String plateNo;


	public long getId() {
		return id;
	}

	public String getAmmeterSn() {
		return ammeterSn;
	}

	public int getPriceId() {
		return priceId;
	}

	public String getPriceName() {
		return priceName;
	}

	public int getPrice() {
		return price;
	}

	public String getAmmeterName() {
		return ammeterName;
	}



	public String getRemark() {
		return remark;
	}

	public int getOnlineState() {
		return onlineState;
	}

	public int getMeterType() {
		return meterType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public BigDecimal getRemainingKwh() {
		return remainingKwh;
	}

	public String getEnableTime() {
		return enableTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAmmeterSn(String ammeterSn) {
		this.ammeterSn = ammeterSn;
	}

	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setAmmeterName(String ammeterName) {
		this.ammeterName = ammeterName;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setOnlineState(int onlineState) {
		this.onlineState = onlineState;
	}

	public void setMeterType(int meterType) {
		this.meterType = meterType;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setRemainingKwh(BigDecimal remainingKwh) {
		this.remainingKwh = remainingKwh;
	}

	public void setEnableTime(String enableTime) {
		this.enableTime = enableTime;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public BigDecimal getRemainingMoney() {
		return remainingMoney;
	}

	public void setRemainingMoney(BigDecimal remainingMoney) {
		this.remainingMoney = remainingMoney;
	}


	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getRoomUuid() {
		return roomUuid;
	}

	public void setRoomUuid(String roomUuid) {
		this.roomUuid = roomUuid;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public int getRentType() {
		return rentType;
	}

	public void setRentType(int rentType) {
		this.rentType = rentType;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	@Override
	public String toString() {
		return "SmartServiceMeterVo [id=" + id + ", ammeterSn=" + ammeterSn + ", priceId=" + priceId + ", priceName="
				+ priceName + ", price=" + price + ", ammeterName=" + ammeterName + ", remark=" + remark
				+ ", onlineState=" + onlineState + ", meterType=" + meterType + ", createTime=" + createTime
				+ ", remainingMoney=" + remainingMoney + ", enableTime=" + enableTime + ", roomName=" + roomName
				+ ", remainingKwh=" + remainingKwh + ", agencyId=" + agencyId + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", roomUuid=" + roomUuid + ", houseType=" + houseType + ", rentType="
				+ rentType + ", plateNo=" + plateNo + "]";
	}
	
	
}	
