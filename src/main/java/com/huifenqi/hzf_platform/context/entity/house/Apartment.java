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

@Entity
@Table(name = "t_apartment")
public class Apartment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 城市Id
	 */
	@Column(name = "f_city_id")
	private long cityId;

	/**
	 * 类型
	 */
	@Column(name = "f_type")
	private int type;

	/**
	 * 公寓名称
	 */
	@Column(name = "f_apartment_name")
	private String name;

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
	 * 公寓状态
	 */
	@Column(name = "f_status")
	private int status;

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

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		return "Apartment [id=" + id + ", cityId=" + cityId + ", type=" + type + ", name=" + name + ", picRootPath="
				+ picRootPath + ", picWebPath=" + picWebPath + ", status=" + status + ", isDelete=" + isDelete
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
