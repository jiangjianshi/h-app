package com.huifenqi.hzf_platform.context.dto.response.map;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.huifenqi.hzf_platform.context.Constants;

@SolrDocument(solrCoreName = Constants.SolrConstant.CORE_HOUSE)
public class HouseMapSolrResult {

	private int level;

	private String name;

	private long number;
	
	private String center;
	
	@Field("rentPriceMonth")
	private int price;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}


	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "MapHouseInfo [level=" + level + ", name=" + name + ", number=" + number + ", center=" + center
				+ ", price=" + price + "]";
	}


	
}
