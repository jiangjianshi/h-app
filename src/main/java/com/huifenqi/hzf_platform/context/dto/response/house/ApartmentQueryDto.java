package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.ArrayList;
import java.util.List;

public class ApartmentQueryDto {

	private List<ApartmentInfo> apartments = new ArrayList<ApartmentInfo>();

	public List<ApartmentInfo> getApartments() {
		return apartments;
	}

	public void setApartments(List<ApartmentInfo> apartments) {
		this.apartments = apartments;
	}

	public void addApartments(ApartmentInfo apartmentInfo) {
		apartments.add(apartmentInfo);
	}

	@Override
	public String toString() {
		return "ApartmentQueryDto [apartments=" + apartments + "]";
	}

}
