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
 * 租房宝典 定制租房实体类
 * 
 * @author jjs
 * @Date 2017-10-10 14:06:00
 */
@Entity
@Table(name = "t_custom_renting")
public class CustomRenting {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 标题
	 */
	@Column(name = "f_title")
	private String title;
	

	/**
	 * banner 链接
	 */
	@Column(name = "f_banner_url")
	private String bannerUrl;
	

	/**
	 * 内部banner链接
	 */
	@Column(name = "f_inner_banner_url")
	private String innerBannerUrl;
	

	/**
	 * 描述
	 */
	@Column(name = "f_desc")
	private String desc;
	

	/**
	 * 状态 1 有效，0 无效
	 */
	@Column(name = "f_status")
	private Integer status;
	

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


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getBannerUrl() {
		return bannerUrl;
	}


	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}


	public String getInnerBannerUrl() {
		return innerBannerUrl;
	}


	public void setInnerBannerUrl(String innerBannerUrl) {
		this.innerBannerUrl = innerBannerUrl;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
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
		return "CustomRenting [id=" + id + ", title=" + title + ", bannerUrl=" + bannerUrl + ", innerBannerUrl="
				+ innerBannerUrl + ", desc=" + desc + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
	

}
