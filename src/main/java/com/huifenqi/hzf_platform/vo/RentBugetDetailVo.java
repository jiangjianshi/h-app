package com.huifenqi.hzf_platform.vo;

public class RentBugetDetailVo {
	
    private String bugetNo;
    private int financeType;
    private int currentIndex;
    private int totalIndex;
    private String payDate;
    private int isDelay;
    private int remainDays;
    private String remainDaysName;
    private int dueFlag;
    private int price;
    private int paidPrice;
    private int deptPrice;
    private int payStatus;
    private int statusOrder;
    private String payStatusName;
    private int paymentType;
    private int bugetFlag;
    private int bugetStatus;
    private String address;
    private String paymentTypeComment;
    private String paymentTypeColor;
    private String bugetComment;
    private String startDate;
    private String endDate;
    private int sponsorPayStatus;
    private int isConfirmed;
    
    
	public String getBugetNo() {
		return bugetNo;
	}
	
	public void setBugetNo(String bugetNo) {
		this.bugetNo = bugetNo;
	}
	
	public int getFinanceType() {
		return financeType;
	}
	
	public void setFinanceType(int financeType) {
		this.financeType = financeType;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public int getTotalIndex() {
		return totalIndex;
	}
	
	public void setTotalIndex(int totalIndex) {
		this.totalIndex = totalIndex;
	}
	
	public String getPayDate() {
		return payDate;
	}
	
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	
	public int getIsDelay() {
		return isDelay;
	}
	
	public void setIsDelay(int isDelay) {
		this.isDelay = isDelay;
	}
	
	public int getDueFlag() {
		return dueFlag;
	}
	
	public void setDueFlag(int dueFlag) {
		this.dueFlag = dueFlag;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getPaidPrice() {
		return paidPrice;
	}
	
	public void setPaidPrice(int paidPrice) {
		this.paidPrice = paidPrice;
	}
	
	public int getDeptPrice() {
		return deptPrice;
	}
	
	public void setDeptPrice(int deptPrice) {
		this.deptPrice = deptPrice;
	}
	
	public int getPayStatus() {
		return payStatus;
	}
	
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	
	public int getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	
	public int getBugetFlag() {
		return bugetFlag;
	}
	
	public void setBugetFlag(int bugetFlag) {
		this.bugetFlag = bugetFlag;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getBugetStatus() {
		return bugetStatus;
	}
	
	public void setBugetStatus(int bugetStatus) {
		this.bugetStatus = bugetStatus;
	}
	
	public int getRemainDays() {
		return remainDays;
	}
	
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	
	public String getPaymentTypeComment() {
		return paymentTypeComment;
	}
	
	public void setPaymentTypeComment(String paymentTypeComment) {
		this.paymentTypeComment = paymentTypeComment;
	}
	
	public String getBugetComment() {
		return bugetComment;
	}
	
	public void setBugetComment(String bugetComment) {
		this.bugetComment = bugetComment;
	}
	
	public String getPaymentTypeColor() {
		return paymentTypeColor;
	}
	
	public void setPaymentTypeColor(String paymentTypeColor) {
		this.paymentTypeColor = paymentTypeColor;
	}
	
	public String getPayStatusName() {
		return payStatusName;
	}
	
	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getSponsorPayStatus() {
		return sponsorPayStatus;
	}

	public void setSponsorPayStatus(int sponsorPayStatus) {
		this.sponsorPayStatus = sponsorPayStatus;
	}
	
	public String getRemainDaysName() {
		return remainDaysName;
	}

	public void setRemainDaysName(String remainDaysName) {
		this.remainDaysName = remainDaysName;
	}

	public int getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public int getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(int statusOrder) {
		this.statusOrder = statusOrder;
	}

	@Override
	public String toString() {
		return "RentBugetDetailVo [bugetNo=" + bugetNo + ", financeType="
				+ financeType + ", currentIndex=" + currentIndex
				+ ", totalIndex=" + totalIndex + ", payDate=" + payDate
				+ ", isDelay=" + isDelay + ", remainDays=" + remainDays
				+ ", remainDaysName=" + remainDaysName + ", dueFlag=" + dueFlag
				+ ", price=" + price + ", paidPrice=" + paidPrice
				+ ", deptPrice=" + deptPrice + ", payStatus=" + payStatus
				+ ", statusOrder=" + statusOrder + ", payStatusName="
				+ payStatusName + ", paymentType=" + paymentType
				+ ", bugetFlag=" + bugetFlag + ", bugetStatus=" + bugetStatus
				+ ", address=" + address + ", paymentTypeComment="
				+ paymentTypeComment + ", paymentTypeColor=" + paymentTypeColor
				+ ", bugetComment=" + bugetComment + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", sponsorPayStatus="
				+ sponsorPayStatus + ", isConfirmed=" + isConfirmed + "]";
	}

}
