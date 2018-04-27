/** 
* Project Name: hzf_platform 
* File Name: SlideShowUrl.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2017年8月31日 上午11:55:50
* Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
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

/**
 * ClassName: SlideShowUrl date: 2017年8月31日 上午11:55:50 Description:轮播图维护表
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_slide_show_url")
public class SlideShowUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 轮播图图片URL
	 */
	@Column(name = "f_image_url")
	private String imageurl;
	
	/**
	 * 轮播图图片URL for iphoneX
	 */
	@Column(name = "f_px_image_url")
	private String pxImageurl;
	
	/**
	 * 跳转链接
	 */
	@Column(name = "f_destination_url")
	private String destinationUrl;
	
	/**
	 * 页面名称
	 */
	@Column(name = "f_page_name")
	private String pageName;
	
	/**
	 * 图片类型（0：默认未分类；1：会找房轮播图；2：会分期轮播图）
	 */
	@Column(name = "f_image_type")
	private int imageType;
	
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

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	
	public String getPxImageurl() {
		return pxImageurl;
	}

	public void setPxImageurl(String pxImageurl) {
		this.pxImageurl = pxImageurl;
	}

	@Override
	public String toString() {
		return "SlideShowUrl [id=" + id + ", imageurl=" + imageurl + ", pxImageurl=" + pxImageurl + ", destinationUrl="
				+ destinationUrl + ", pageName=" + pageName + ", imageType=" + imageType + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", state=" + state + "]";
	}

}