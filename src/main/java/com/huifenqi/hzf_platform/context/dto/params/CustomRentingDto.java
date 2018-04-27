package com.huifenqi.hzf_platform.context.dto.params;

import java.util.Map;

public class CustomRentingDto {

	/**
	 * 标题
	 */
	private String title;

	/**
	 * banner 链接
	 */
	private String picWebPath;

	/**
	 * 内部banner链接
	 */
	private String innerPicWebPath;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 返给前端的参数
	 */
	private Map<String, Object> params;
	
	/**
	 * 选择按钮的参数
	 */
	private Map<String, Object> selectMap;

	/**
	 * 是否可点击， 0不可点击，1可点击，默认可点击
	 */
	private int isClick = 1;
	

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicWebPath() {
		return picWebPath;
	}

	public void setPicWebPath(String picWebPath) {
		this.picWebPath = picWebPath;
	}

	public String getInnerPicWebPath() {
		return innerPicWebPath;
	}

	public void setInnerPicWebPath(String innerPicWebPath) {
		this.innerPicWebPath = innerPicWebPath;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIsClick() {
		return isClick;
	}

	public void setIsClick(int isClick) {
		this.isClick = isClick;
	}

	public Map<String, Object> getSelectMap() {
		return selectMap;
	}

	public void setSelectMap(Map<String, Object> selectMap) {
		this.selectMap = selectMap;
	}
	
	
}
