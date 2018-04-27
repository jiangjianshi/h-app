package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_agency_activity")
public class ActivityAgency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 中介公司ID
	 */
	@Column(name = "f_agency_id")
	private String companyId;

	/**
	 * 活动页面名称
	 */
	@Column(name = "f_page_name")
	private String pageName;
	
	/**
	 * 活动名称
	 */
	@Column(name = "f_activity_name")
	private String activityName;
	
	/**
	 * 活动介绍
	 */
	@Column(name = "f_activity_desc")
	private String activityDesc;
	
	/**
	 * 城市ID
	 */
	@Column(name = "f_city_id")
	private long cityId;
	
	/**
	 * 品牌公寓名称
	 */
	@Column(name = "f_agency_name")
	private String companyName;

	/**
	 * 品牌公寓介绍
	 */
	@Column(name = "f_agency_desc")
	private String companyDesc;
	
	/**
	 * 品牌公寓LOGO
	 */
	@Column(name = "f_agency_logo")
	private String companyLogo;

	/**
	 * 活动开始时间
	 */
	@Column(name = "f_activity_start_time")
	private Date activityStartTime;
	
	/**
	 * 活动开始时间
	 */
	@Column(name = "f_activity_end_time")
	private Date activityEndTime;
	
	/**
	 * 活动图片的URL
	 */
	@Column(name = "f_img_url1")
	private String imgUrl1;

	/**
	 * 活动图片的URL
	 */
	@Column(name = "f_img_url2")
	private String imgUrl2;

	/**
	 * 活动图片的URL
	 */
	@Column(name = "f_img_url3")
	private String imgUrl3;
	
	/**
	 * 活动图片的URL
	 */
	@Column(name = "f_img_url4")
	private String imgUrl4;
	
	/**
	 * 活动图片的URL
	 */
	@Column(name = "f_img_url5")
	private String imgUrl5;

	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_update_time")
	private Date updateTime;
	
	/**
	 * 有效标识：1：默认有效；0：失效
	 */
	@Column(name = "f_state")
	private int state;

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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getImgUrl1() {
		return imgUrl1;
	}

	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	public String getImgUrl3() {
		return imgUrl3;
	}

	public void setImgUrl3(String imgUrl3) {
		this.imgUrl3 = imgUrl3;
	}

	public String getImgUrl4() {
		return imgUrl4;
	}

	public void setImgUrl4(String imgUrl4) {
		this.imgUrl4 = imgUrl4;
	}

	public String getImgUrl5() {
		return imgUrl5;
	}

	public void setImgUrl5(String imgUrl5) {
		this.imgUrl5 = imgUrl5;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	@Override
	public String toString() {
		return "ActivityAgency [id=" + id + ", companyId=" + companyId
				+ ", pageName=" + pageName + ", activityName=" + activityName
				+ ", activityDesc=" + activityDesc + ", cityId=" + cityId
				+ ", companyName=" + companyName + ", companyDesc="
				+ companyDesc + ", companyLogo=" + companyLogo
				+ ", activityStartTime=" + activityStartTime
				+ ", activityEndTime=" + activityEndTime + ", imgUrl1="
				+ imgUrl1 + ", imgUrl2=" + imgUrl2 + ", imgUrl3=" + imgUrl3
				+ ", imgUrl4=" + imgUrl4 + ", imgUrl5=" + imgUrl5
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", state=" + state + "]";
	}

}
