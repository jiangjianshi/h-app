/** 
* Project Name: hzf_platform 
* File Name: HousePicture.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:06:22 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: BdHousePicture date: 2017年4月13日 下午18:06:22 Description: 房源图片
 * 
 * @author arison
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_house_pics_extend")
public class BdHousePicture {

	public BdHousePicture() {
	}

	@Id
	@Column(name = "id")
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
	private int roomId;

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
	 * 图片序号
	 */
	@Column(name = "f_pic_detailnum")
	private int picDetailnum;

	/**
	 * 图片标签
	 */
	@Column(name = "f_pic_desc")
	private String picDesc;

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
	 * 是否存储过图片
	 */
	@Column(name = "f_is_run")
	private int isRun;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;


	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
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

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
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

	public int getPicDetailnum() {
		return picDetailnum;
	}

	public void setPicDetailnum(int picDetailnum) {
		this.picDetailnum = picDetailnum;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
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

    public int getIsRun() {
        return isRun;
    }

    public void setIsRun(int isRun) {
        this.isRun = isRun;
    }

    @Override
	public String toString() {
		return "BdHousePicture{" +
				"id=" + id +
				", sellId='" + sellId + '\'' +
				", roomId=" + roomId +
				", picRootPath='" + picRootPath + '\'' +
				", picWebPath='" + picWebPath + '\'' +
				", picDetailnum=" + picDetailnum +
				", picDesc='" + picDesc + '\'' +
				", picType=" + picType +
				", isDefault=" + isDefault +
				", isDelete=" + isDelete +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}