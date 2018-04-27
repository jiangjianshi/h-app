package com.huifenqi.hzf_platform.context.dto.response.house;

public class ApartmentInfo {

	private String apartmentName;

	private String apartmentPic;

	private String apartmentType;

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getApartmentPic() {
		return apartmentPic;
	}

	public void setApartmentPic(String apartmentPic) {
		this.apartmentPic = apartmentPic;
	}

	public String getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(String apartmentType) {
		this.apartmentType = apartmentType;
	}

	@Override
	public String toString() {
		return "ApartmentInfo [apartmentName=" + apartmentName + ", apartmentPic=" + apartmentPic + ", apartmentType="
				+ apartmentType + "]";
	}

}
