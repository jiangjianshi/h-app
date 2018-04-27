package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_agency_manage")
public class Agency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 中介公司ID
	 */
	@Column(name = "f_agency_id")
	private String companyId;

	/**
	 * 中介公司名称
	 */
	@Column(name = "f_agency_name")
	private String companyName;
	
	/**
	 * 城市ID
	 */
	@Column(name = "f_city_id")
	private long cityId;

	/**
	 * 中介公司描述
	 */
	@Column(name = "f_agency_desc")
	private String companyDesc;

	/**
	 * 图片服务器位置
	 */
	@Column(name = "f_pic_root_path")
	private String picRootPath;

	/**
	 * 图片链接 for iphoneX
	 */
	@Column(name = "f_px_pic_path")
	private String pxPicPath;
	
	/**
	 * 图片网络路径
	 */
	@Column(name = "f_pic_sx_path")
	private String picSxPath;

	/**
	 * 跳转链接（参加活动的品牌公寓才存在）
	 */
	@Column(name = "f_destination_url")
	private String destinationUrl;
	
	/**
	 * 页面名称
	 */
	@Column(name = "f_page_name")
	private String pageName;
	
	/**
	 * 图片网络路径
	 */
	@Column(name = "f_pic_web_path")
	private String picWebPath;
	
	/**
	 * 某城市的公寓下的房间数量统计
	 */
	@Column(name = "f_room_count")
	private long roomCount;

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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
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

	public String getPicSxPath() {
		return picSxPath;
	}

	public void setPicSxPath(String picSxPath) {
		this.picSxPath = picSxPath;
	}

	public long getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(long roomCount) {
		this.roomCount = roomCount;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	
	public String getPxPicPath() {
		return pxPicPath;
	}

	public void setPxPicPath(String pxPicPath) {
		this.pxPicPath = pxPicPath;
	}

	@Override
	public String toString() {
		return "Agency [id=" + id + ", companyId=" + companyId + ", companyName=" + companyName + ", cityId=" + cityId
				+ ", companyDesc=" + companyDesc + ", picRootPath=" + picRootPath + ", pxPicPath=" + pxPicPath
				+ ", picSxPath=" + picSxPath + ", destinationUrl=" + destinationUrl + ", pageName=" + pageName
				+ ", picWebPath=" + picWebPath + ", roomCount=" + roomCount + ", isDelete=" + isDelete + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}
