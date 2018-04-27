/** 
* Project Name: hzf_platform 
* File Name: PlatformCustomer.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:07:36 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.platform;

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
 * ClassName: PlatformCustomer date: 2016年4月25日 下午4:07:36 Description:平台用户
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_platform_customer")
public class PlatformCustomer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 用户appId，平台唯一标识
	 */
	@Column(name = "f_app_id")
	private String appId;

	/**
	 * 平台用户标识
	 */
	@Column(name = "f_source")
	private String source;

	/**
	 * 平台用户用于创建签名和加密数据的秘钥
	 */
	@Column(name = "f_secret_key")
	private String secretKey;

	/**
	 * 授权ip或者授权ip段
	 */
	@Column(name = "f_permission_ip")
	private String permissionIp;

	/**
	 * 授权开始时间
	 */
	@Column(name = "f_permission_begin_date")
	@Temporal(TemporalType.DATE)
	private Date permissionBegin;

	/**
	 * 授权结束时间
	 */
	@Column(name = "f_permission_end_date")
	@Temporal(TemporalType.DATE)
	private Date permissionEnd;
	
	/**
	 * 权限状态， 1 有效， 0 无效
	 */
	@Column(name = "f_permission_status")
	private Integer permissionStatus;
	
	/**
	 * 是否saas平台 0否      1是
	 */
	@Column(name = "f_is_saas")
	private Integer isSaas;
	
	/**
	 * 是否图片美化 1是 0 否
	 */
	@Column(name = "f_is_img")
	private Integer isImg;
	
	/**
	 * 图片美化样式参数
	 */
	@Column(name = "f_image_css")
	private String imageCss;
	
	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.DATE)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.DATE)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getPermissionIp() {
		return permissionIp;
	}

	public void setPermissionIp(String permissionIp) {
		this.permissionIp = permissionIp;
	}

	public Date getPermissionBegin() {
		return permissionBegin;
	}

	public void setPermissionBegin(Date permissionBegin) {
		this.permissionBegin = permissionBegin;
	}

	public Date getPermissionEnd() {
		return permissionEnd;
	}

	public void setPermissionEnd(Date permissionEnd) {
		this.permissionEnd = permissionEnd;
	}
	
	
	public Integer getPermissionStatus() {
		return permissionStatus;
	}

	public void setPermissionStatus(Integer permissionStatus) {
		this.permissionStatus = permissionStatus;
	}

	
	public Integer getIsSaas() {
		return isSaas;
	}

	public void setIsSaas(Integer isSaas) {
		this.isSaas = isSaas;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	
	public Integer getIsImg() {
		return isImg;
	}

	public void setIsImg(Integer isImg) {
		this.isImg = isImg;
	}

	public String getImageCss() {
		return imageCss;
	}

	public void setImageCss(String imageCss) {
		this.imageCss = imageCss;
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
		return "PlatformCustomer [id=" + id + ", appId=" + appId + ", source=" + source + ", secretKey=" + secretKey
				+ ", permissionIp=" + permissionIp + ", permissionBegin=" + permissionBegin + ", permissionEnd="
				+ permissionEnd + ", permissionStatus=" + permissionStatus + ", isDelete=" + isDelete + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
	
}
