package com.huifenqi.hzf_platform.context.dto.response.map;

import java.util.ArrayList;
import java.util.List;

import com.huifenqi.hzf_platform.context.dto.response.house.ApartmentInfo;

public class MapHouseQueryDto {

	private List<MapHouseInfo> mapList = new ArrayList<MapHouseInfo>();



	public List<MapHouseInfo> getMapList() {
		return mapList;
	}

	public void setMapList(List<MapHouseInfo> mapList) {
		this.mapList = mapList;
	}

	public void addMapHouseInfo(MapHouseInfo mapHouseInfo) {
		mapList.add(mapHouseInfo);
	}

	@Override
	public String toString() {
		return "mapList [mapList=" + mapList + "]";
	}

}
