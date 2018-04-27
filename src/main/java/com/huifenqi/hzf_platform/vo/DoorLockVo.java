package com.huifenqi.hzf_platform.vo;

public class DoorLockVo {

    private String smartDeviceId;
    private String deviceName;
    private String sn;
    private String lockMac;
    private int lockType;
    private int useType;
    private String location;
    private int electricity;
    private String online;
    private String createTime;
    private String houseUuid;
    private String roomUuid;
	public String getSmartDeviceId() {
		return smartDeviceId;
	}
	public void setSmartDeviceId(String smartDeviceId) {
		this.smartDeviceId = smartDeviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getLockMac() {
		return lockMac;
	}
	public void setLockMac(String lockMac) {
		this.lockMac = lockMac;
	}
	public int getLockType() {
		return lockType;
	}
	public void setLockType(int lockType) {
		this.lockType = lockType;
	}
	public int getUseType() {
		return useType;
	}
	public void setUseType(int useType) {
		this.useType = useType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getElectricity() {
		return electricity;
	}
	public void setElectricity(int electricity) {
		this.electricity = electricity;
	}
	
	
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getHouseUuid() {
		return houseUuid;
	}
	public void setHouseUuid(String houseUuid) {
		this.houseUuid = houseUuid;
	}
	public String getRoomUuid() {
		return roomUuid;
	}
	public void setRoomUuid(String roomUuid) {
		this.roomUuid = roomUuid;
	}
	@Override
	public String toString() {
		return "DoorLockVo [smartDeviceId=" + smartDeviceId + ", deviceName=" + deviceName + ", sn=" + sn + ", lockMac="
				+ lockMac + ", lockType=" + lockType + ", useType=" + useType + ", location=" + location
				+ ", electricity=" + electricity + ", online=" + online + ", createTime=" + createTime + ", houseUuid="
				+ houseUuid + ", roomUuid=" + roomUuid + "]";
	}

}
