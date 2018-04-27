/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.third;

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
 * ClassName: t_third_sys_user date: 2017年12月23日 下午4:05:50 Description:闲鱼账户信息
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_third_sys_user")
public class ThirdSysUser {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 闲鱼登录账户
	 */
	@Column(name = "f_user_id")
	private String userId;
	/**
	 * 渠道名称
	 */
	@Column(name = "f_channel_name")
	private String channelName;
	
	/**
     * 渠道ID
     */
    @Column(name = "f_channel_id")
    private String channelId;
    
	/**
	 * 公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;
	/**
	 * 公司ID
	 */
	@Column(name = "f_company_id")
	private String companyId;
	
	/**
	 * 账户状态
	 */
	@Column(name = "f_is_delete")
	private int isDelete;
	
	/**
     * 返回编码
     */
    @Column(name = "f_error_code")
    private String errorCode;
    /**
     * 返回描述
     */
    @Column(name = "f_error_msg")
    private String errorMsg;

    /**
     * 上传状态
     */
    @Column(name = "f_status")
    private int status;
    
    
    /**
     * 账号是否可用
     */
    @Column(name = "f_is_use")
    private int isUse;
    
    /**
     * 账号剩余使用次数
     */
    @Column(name = "f_use_count")
    private int userCount;
    
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

 

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    
    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
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
        return "ThirdSysUser [id=" + id + ", userId=" + userId + ", channelName=" + channelName + ", channelId="
                + channelId + ", companyName=" + companyName + ", companyId=" + companyId + ", isDelete=" + isDelete
                + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", status=" + status + ", isUse=" + isUse
                + ", userCount=" + userCount + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }

	
}
