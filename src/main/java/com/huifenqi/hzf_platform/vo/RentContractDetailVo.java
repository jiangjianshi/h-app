package com.huifenqi.hzf_platform.vo;

import java.util.ArrayList;
import java.util.List;

public class RentContractDetailVo {
    private String communityName;
    private String district;
    private String apartmentName;
    private String houseNo;
    private List<HouseItems> basicNumbers=new ArrayList<>();
    private List<HouseItems> goodsItems=new ArrayList<>();
    private List<HouseItems> otherFees=new ArrayList<>();
    private String rentPayPeriod;
    private String agencyRentContractCode;
    private String rentContractCode;
    private String rentBeginDate;
    private String rentEndDate;
    private int rentDepositFee;
    private int rentPrice;
    private String roomName;
    private String signDate;
    private String signerName;
    private String signerPhone;
    private String tenantName;
    private String tenantDocType;
    private String tenantPhone;
    private String tenantIdNo;
    private int unpayAmount;
    private String bankToken;
    private String comment;
    private int constractStatus;
    private int contractConfirmStatus;
    private int  withHoldStatus;
    private int isAgencyUseWithhold;
    private int isConfirmed;

    private List<String> contractPics=new ArrayList<>();

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }


    public String getRentPayPeriod() {
        return rentPayPeriod;
    }

    public void setRentPayPeriod(String rentPayPeriod) {
        this.rentPayPeriod = rentPayPeriod;
    }

    public String getAgencyRentContractCode() {
        return agencyRentContractCode;
    }

    public void setAgencyRentContractCode(String agencyRentContractCode) {
        this.agencyRentContractCode = agencyRentContractCode;
    }

    public String getRentContractCode() {
        return rentContractCode;
    }

    public void setRentContractCode(String rentContractCode) {
        this.rentContractCode = rentContractCode;
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



    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerPhone() {
        return signerPhone;
    }

    public void setSignerPhone(String signerPhone) {
        this.signerPhone = signerPhone;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantDocType() {
        return tenantDocType;
    }

    public void setTenantDocType(String tenantDocType) {
        this.tenantDocType = tenantDocType;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public String getTenantIdNo() {
        return tenantIdNo;
    }

    public void setTenantIdNo(String tenantIdNo) {
        this.tenantIdNo = tenantIdNo;
    }

    public int getUnpayAmount() {
        return unpayAmount;
    }

    public void setUnpayAmount(int unpayAmount) {
        this.unpayAmount = unpayAmount;
    }

    public int getContractConfirmStatus() {
        return contractConfirmStatus;
    }

    public void setContractConfirmStatus(int contractConfirmStatus) {
        this.contractConfirmStatus = contractConfirmStatus;
    }

    public int getWithHoldStatus() {
        return withHoldStatus;
    }

    public void setWithHoldStatus(int withHoldStatus) {
        this.withHoldStatus = withHoldStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<HouseItems> getBasicNumbers() {
        return basicNumbers;
    }

    public void setBasicNumbers(List<HouseItems> basicNumbers) {
        this.basicNumbers = basicNumbers;
    }

    public List<HouseItems> getGoodsItems() {
        return goodsItems;
    }

    public void setGoodsItems(List<HouseItems> goodsItems) {
        this.goodsItems = goodsItems;
    }

    public List<HouseItems> getOtherFees() {
        return otherFees;
    }

    public void setOtherFees(List<HouseItems> otherFees) {
        this.otherFees = otherFees;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public List<String> getContractPics() {
        return contractPics;
    }

    public void setContractPics(List<String> contractPics) {
        this.contractPics = contractPics;
    }

    public int getConstractStatus() {
        return constractStatus;
    }

    public void setConstractStatus(int constractStatus) {
        this.constractStatus = constractStatus;
    }

    public int getIsAgencyUseWithhold() {
        return isAgencyUseWithhold;
    }

    public void setIsAgencyUseWithhold(int isAgencyUseWithhold) {
        this.isAgencyUseWithhold = isAgencyUseWithhold;
    }

    public void setRentDepositFee(int rentDepositFee) {
        this.rentDepositFee = rentDepositFee;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public int getRentDepositFee() {
        return rentDepositFee;
    }

    public String getBankToken() {
        return bankToken;
    }

    public void setBankToken(String bankToken) {
        this.bankToken = bankToken;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public String toString() {
        return "RentContractDetailVo{" +
                "communityName='" + communityName + '\'' +
                ", district='" + district + '\'' +
                ", apartmentName='" + apartmentName + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", basicNumbers=" + basicNumbers +
                ", goodsItems=" + goodsItems +
                ", otherFees=" + otherFees +
                ", rentPayPeriod='" + rentPayPeriod + '\'' +
                ", agencyRentContractCode='" + agencyRentContractCode + '\'' +
                ", rentContractCode='" + rentContractCode + '\'' +
                ", rentBeginDate='" + rentBeginDate + '\'' +
                ", rentEndDate='" + rentEndDate + '\'' +
                ", rentDepositFee=" + rentDepositFee +
                ", rentPrice=" + rentPrice +
                ", roomName='" + roomName + '\'' +
                ", signDate='" + signDate + '\'' +
                ", signerName='" + signerName + '\'' +
                ", signerPhone='" + signerPhone + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", tenantDocType='" + tenantDocType + '\'' +
                ", tenantPhone='" + tenantPhone + '\'' +
                ", tenantIdNo='" + tenantIdNo + '\'' +
                ", unpayAmount=" + unpayAmount +
                ", bankToken='" + bankToken + '\'' +
                ", comment='" + comment + '\'' +
                ", constractStatus=" + constractStatus +
                ", contractConfirmStatus=" + contractConfirmStatus +
                ", withHoldStatus=" + withHoldStatus +
                ", isAgencyUseWithhold=" + isAgencyUseWithhold +
                ", isConfirmed=" + isConfirmed +
                ", contractPics=" + contractPics +
                '}';
    }
}
