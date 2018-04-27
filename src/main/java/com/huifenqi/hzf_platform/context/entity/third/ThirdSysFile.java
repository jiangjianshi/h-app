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
 * ClassName: ThirdSysImage date: 2017年12月23日 下午4:05:50 Description:同步文件
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_third_sys_file")
public class ThirdSysFile {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 同步目标
	 */
	@Column(name = "f_channel")
	private int channel;
	/**
	 * 找房图片地址
	 */
	@Column(name = "f_pic_root_path")
	private String picRootPath;
	
	/**
     * 对接方返回文件id
     */
    @Column(name = "f_file_id")
    private String fileId;
    
	/**
	 * 闲鱼登录账户
	 */
	@Column(name = "f_user_id")
	private String userId;
	/**
	 * 账户session
	 */
	@Column(name = "f_user_session")
	private String userSession;
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
	private String status;
	
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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getPicRootPath() {
        return picRootPath;
    }

    public void setPicRootPath(String picRootPath) {
        this.picRootPath = picRootPath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
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

   
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

	
	
	
}
