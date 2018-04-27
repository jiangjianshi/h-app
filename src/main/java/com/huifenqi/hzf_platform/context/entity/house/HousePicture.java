/** 
* Project Name: hzf_platform 
* File Name: HousePicture.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:06:22 
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
 * ClassName: HousePicture date: 2016年4月25日 下午4:06:22 Description: 房源图片
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_pics")
public class HousePicture {

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
	 * 图片服务器位置
	 */
	@Column(name = "f_pic_root_path")
	private String picRootPath;

	/**
	 * 图片网络路径
	 */
	@Column(name = "f_pic_web_path")
	private String picWebPath;
	
	/**
	 * 图片的差异值哈希
	 */
	@Column(name = "f_pic_dhash")
	private String picDhash;

	/**
	 * 图片类型
	 */
	@Column(name = "f_pic_type")
	private int picType;

	/**
	 * 是否为首图，1：首图；0：非首图
	 */
	@Column(name = "f_is_default")
	private int isDefault;

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

	public String getPicRootPath() {
		return picRootPath;
	}

	public void setPicRootPath(String picRootPath) {
		this.picRootPath = picRootPath;
	}

	public String getPicWebPath() {
		return picWebPath;
	}

	public void setPicWebPath(String picWebPath) {
		this.picWebPath = picWebPath;
	}

	public String getPicDhash() {
		return picDhash;
	}

	public void setPicDhash(String picDhash) {
		this.picDhash = picDhash;
	}

	public int getPicType() {
		return picType;
	}

	public void setPicType(int picType) {
		this.picType = picType;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
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
		return "HousePicture [id=" + id + ", sellId=" + sellId + ", roomId=" + roomId + ", picRootPath=" + picRootPath
				+ ", picWebPath=" + picWebPath + ", picDhash=" + picDhash + ", picType=" + picType + ", isDefault="
				+ isDefault + ", isDelete=" + isDelete + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}


}
