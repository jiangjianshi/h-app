/** 
* Project Name: hzf_platform 
* File Name: HouseSetting.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:07:04 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ClassName: HouseSetting date: 2016年4月25日 下午4:07:04 Description:房源配置
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_setting")
public class HouseSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房间id
	 */
	@Column(name = "f_room_id")
	private long roomId;

	/**
	 * 配置物品id
	 */
	@Column(name = "f_setting_code")
	private int settingCode;

	/**
	 * 配置物品数量
	 */
	@Column(name = "f_setting_nums")
	private int settingNum;

	/**
	 * 配置物品位置，1：卧室；2公共区域
	 */
	@Column(name = "f_setting_position")
	private int settingPosition;

	/**
	 * 配置类型
	 */
	@Column(name = "f_category_type")
	private int categoryType;

	/**
	 * 配置名称
	 */
	@Column(name = "f_setting_name")
	private String settingName;

	/**
	 * 配置价格
	 */
	@Column(name = "f_setting_price")
	private int settingPrice;

	/**
	 * 配置成本
	 */
	@Column(name = "f_setting_cost")
	private int settingCost;

	/**
	 * 是否已经配置完成
	 */
	@Column(name = "f_is_completed")
	private int isCompleted;

	/**
	 * 配置完成时间
	 */
	@Column(name = "f_completed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedTime;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public int getSettingCode() {
		return settingCode;
	}

	public void setSettingCode(int settingCode) {
		this.settingCode = settingCode;
	}

	public int getSettingNum() {
		return settingNum;
	}

	public void setSettingNum(int settingNum) {
		this.settingNum = settingNum;
	}

	public int getSettingPosition() {
		return settingPosition;
	}

	public void setSettingPosition(int settingPosition) {
		this.settingPosition = settingPosition;
	}

	public int getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public int getSettingPrice() {
		return settingPrice;
	}

	public void setSettingPrice(int settingPrice) {
		this.settingPrice = settingPrice;
	}

	public int getSettingCost() {
		return settingCost;
	}

	public void setSettingCost(int settingCost) {
		this.settingCost = settingCost;
	}

	public int getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Date getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "HouseSetting [id=" + id + ", sellId=" + sellId + ", roomId=" + roomId + ", settingCode=" + settingCode
				+ ", settingNum=" + settingNum + ", settingPosition=" + settingPosition + ", categoryType="
				+ categoryType + ", settingName=" + settingName + ", settingPrice=" + settingPrice + ", settingCost="
				+ settingCost + ", isCompleted=" + isCompleted + ", completedTime=" + completedTime + ", isDelete="
				+ isDelete + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
