/** 
* Project Name: hzf_platform 
* File Name: RoomBase.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:07:13 
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
 * ClassName: RoomBase date: 2016年4月25日 下午4:07:13 Description: 房间基础信息
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_room_original")
public class RoomOriginal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 房间状态
	 */
	@Column(name = "f_room_id")
	private Long roomId;
	
	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房间状态
	 */
	@Column(name = "f_status")
	private int status;

	/**
	 * 面积
	 */
	@Column(name = "f_area")
	private float area;

	/**
	 * 房间描述
	 */
	@Column(name = "f_room_comment")
	private String desc;

	/**
	 * 房间类型
	 */
	@Column(name = "f_room_type")
	private int roomType;

	/**
	 * 房间用途类型
	 */
	@Column(name = "f_room_use")
	private int roomUse;

	/**
	 * 朝向
	 */
	@Column(name = "f_orientations")
	private int orientation;

	/**
	 * 可入住时间
	 */
	@Column(name = "f_can_checkin_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkInTime;

	/**
	 * 月租金
	 */
	@Column(name = "f_rent_price_month")
	private int price;

	/**
	 * 日租金
	 */
	@Column(name = "f_rent_price_day")
	private int dayRent;

	/**
	 * 服务费或中介费
	 */
	@Column(name = "f_service_fee")
	private int bonus;

	/**
	 * 押金
	 */
	@Column(name = "f_deposit_fee")
	private int depositFee;

	/**
	 * 押金押几个月
	 */
	@Column(name = "f_deposit_month")
	private int depositMonth;

	/**
	 * 每次付几个月的租金
	 */
	@Column(name = "f_period_month")
	private int periodMonth;


	/**
	 * 装修档次
	 */
	@Column(name = "f_decoration")
	private int decoration;

	/**
	 * 是否有独立卫生间
	 */
	@Column(name = "f_toilet")
	private int toilet;

	/**
	 * 是否有独立阳台
	 */
	@Column(name = "f_balcony")
	private int balcony;

	/**
	 * 是否有家财险
	 */
	@Column(name = "f_insurance")
	private int insurance;


	/**
	 * 房间标签
	 */
	@Column(name = "f_room_name")
	private String roomName;


	/**
	 * 是否有钥匙；1:有；0:无
	 */
	@Column(name = "f_has_key")
	private int hasKey;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

	/**
	 * 是否置顶
	 */
	@Column(name = "f_is_top")
	private int isTop;
	
	
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

	/**
	 * 经纪人电话
	 */
	@Column(name = "f_agency_phone")
	private String agencyPhone;
	
	/**
	 * 图片
	 */
	@Column(name = "f_room_pics")
	private String imgs;
	
	/**
	 * 配置
	 */
	@Column(name = "f_room_settings")
	private String settings;


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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public int getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(int roomUse) {
		this.roomUse = roomUse;
	}


	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}


	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDayRent() {
		return dayRent;
	}

	public void setDayRent(int dayRent) {
		this.dayRent = dayRent;
	}


	public int getDepositFee() {
		return depositFee;
	}

	public void setDepositFee(int depositFee) {
		this.depositFee = depositFee;
	}

	public int getDepositMonth() {
		return depositMonth;
	}

	public void setDepositMonth(int depositMonth) {
		this.depositMonth = depositMonth;
	}

	public int getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(int periodMonth) {
		this.periodMonth = periodMonth;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
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

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}


	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getHasKey() {
		return hasKey;
	}

	public void setHasKey(int hasKey) {
		this.hasKey = hasKey;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}


	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
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

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return "RoomOriginal [id=" + id + ", roomId=" + roomId + ", sellId=" + sellId + ", status=" + status + ", area="
				+ area + ", desc=" + desc + ", roomType=" + roomType + ", roomUse=" + roomUse + ", orientation="
				+ orientation + ", checkInTime=" + checkInTime + ", price=" + price + ", dayRent=" + dayRent
				+ ", bonus=" + bonus + ", depositFee=" + depositFee + ", depositMonth=" + depositMonth
				+ ", periodMonth=" + periodMonth + ", decoration=" + decoration + ", toilet=" + toilet + ", balcony="
				+ balcony + ", insurance=" + insurance + ", roomName=" + roomName + ", hasKey=" + hasKey + ", isDelete="
				+ isDelete + ", isTop=" + isTop + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", agencyPhone=" + agencyPhone + ", imgs=" + imgs + ", settings=" + settings + "]";
	}
	
}
