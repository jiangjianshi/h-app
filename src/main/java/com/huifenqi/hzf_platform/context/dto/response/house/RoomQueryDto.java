/** 
 * Project Name: hzf_platform 
 * File Name: RoomQueryDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto.response 
 * Date: 2016年4月27日下午8:19:04 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.dto.response.house;

import java.util.List;

/**
 * ClassName: RoomQueryDto date: 2016年4月27日 下午8:19:04 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class RoomQueryDto {

	/**
	 * 房源销售Id
	 */
	private String sellId;

	/**
	 * 房间Id
	 */
	private long roomId;

	/**
	 * 房间类型:主卧、次卧
	 */
	private String roomType;

	/**
	 * 月租
	 */
	private int rentPriceMonth;

	/**
	 * 朝向
	 */
	private String orientations;

	/**
	 * 面积
	 */
	private String area;

	/**
	 * 图片
	 */
	private List<String> imgs;

	/**
	 * 发布日期
	 */
	private String pubDate;

	/**
	 * 房间状态
	 */
	private int status;

	/**
	 * 房间状态描述
	 */
	private String statusDesc;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 押金形式 , 例如押一付三
	 */
	private String payType;

	/**
	 * 独立卫生间
	 */
	private int toilet;

	/**
	 * 独立阳台
	 */
	private int balcony;

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getRoomType() {
		return roomType;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public int getRentPriceMonth() {
		return rentPriceMonth;
	}

	public void setRentPriceMonth(int rentPriceMonth) {
		this.rentPriceMonth = rentPriceMonth;
	}

	public String getOrientations() {
		return orientations;
	}

	public void setOrientations(String orientations) {
		this.orientations = orientations;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public int getToilet() {
		return toilet;
	}

	public void setToilet(int toilet) {
		this.toilet = toilet;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	@Override
	public String toString() {
		return "RoomQueryDto [sellId=" + sellId + ", roomType=" + roomType + ", rentPriceMonth=" + rentPriceMonth
				+ ", orientations=" + orientations + ", area=" + area + ", imgs=" + imgs + ", pubDate=" + pubDate
				+ ", status=" + status + ", statusDesc=" + statusDesc + ", title=" + title + ", payType=" + payType
				+ ", toilet=" + toilet + ", balcony=" + balcony + "]";
	}

}
