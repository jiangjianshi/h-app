package com.huifenqi.hzf_platform.vo;

public class RentContractVo {
    private String communityName;
    private String apartmentName;
    private String district;

    private String houseNo;
    private String roomName;
    private String rentContractCode;
    private double rentPrice;
    private String rentBeginDate;
    private String rentEndDate;
    private String rentPayPeriod;
    private int constractStatus;
    private int operationType;
    private String constractStatusDesc;
    private int unpayAmount;
    private String signerPhone;
    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRentContractCode() {
        return rentContractCode;
    }

    public void setRentContractCode(String rentContractCode) {
        this.rentContractCode = rentContractCode;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getRentBeginDate() {
        return rentBeginDate;
    }

    public void setRentBeginDate(String rentBeginDate) {
        this.rentBeginDate = rentBeginDate;
    }

    public String getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(String rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public String getRentPayPeriod() {
        return rentPayPeriod;
    }

    public void setRentPayPeriod(String rentPayPeriod) {
        this.rentPayPeriod = rentPayPeriod;
    }

    public int getConstractStatus() {
        return constractStatus;
    }

    public void setConstractStatus(int constractStatus) {
        this.constractStatus = constractStatus;
    }

    public String getConstractStatusDesc() {
        return constractStatusDesc;
    }

    public void setConstractStatusDesc(String constractStatusDesc) {
        this.constractStatusDesc = constractStatusDesc;
    }

    public int getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(int unpayAmount) {
        this.unpayAmount = unpayAmount;
    }



    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSignerPhone() {
        return signerPhone;
    }

    public void setSignerPhone(String signerPhone) {
        this.signerPhone = signerPhone;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "RentContractVo{" +
                "communityName='" + communityName + '\'' +
                ", apartmentName='" + apartmentName + '\'' +
                ", district='" + district + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", roomName='" + roomName + '\'' +
                ", rentContractCode='" + rentContractCode + '\'' +
                ", rentPrice=" + rentPrice +
                ", rentBeginDate='" + rentBeginDate + '\'' +
                ", rentEndDate='" + rentEndDate + '\'' +
                ", rentPayPeriod='" + rentPayPeriod + '\'' +
                ", constractStatus=" + constractStatus +
                ", needConfirm=" + operationType +
                ", constractStatusDesc='" + constractStatusDesc + '\'' +
                ", unpayAmount=" + unpayAmount +
                ", signerPhone='" + signerPhone + '\'' +
                '}';
    }
}
