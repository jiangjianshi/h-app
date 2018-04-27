package com.huifenqi.hzf_platform.vo;

import java.util.List;

public class RentContractBugetVo {
	
    private String rentContractCode;
    private String agencyPhone;
    private String agencyName;
    private String address;
    private int contractEffectStatus;
    private List<RentBugetDetailVo> bugets;
    
    
	public String getRentContractCode() {
		return rentContractCode;
	}
	
	public void setRentContractCode(String rentContractCode) {
		this.rentContractCode = rentContractCode;
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
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getContractEffectStatus() {
		return contractEffectStatus;
	}
	
	public void setContractEffectStatus(int contractEffectStatus) {
		this.contractEffectStatus = contractEffectStatus;
	}
	
	public List<RentBugetDetailVo> getBugets() {
		return bugets;
	}
	
	public void setBugets(List<RentBugetDetailVo> bugets) {
		this.bugets = bugets;
	}

	@Override
	public String toString() {
		return "RentContractBugetVo [rentContractCode=" + rentContractCode
				+ ", agencyPhone=" + agencyPhone + ", agencyName=" + agencyName
				+ ", address=" + address + ", contractEffectStatus="
				+ contractEffectStatus + ", bugets=" + bugets + "]";
	}
	
}
