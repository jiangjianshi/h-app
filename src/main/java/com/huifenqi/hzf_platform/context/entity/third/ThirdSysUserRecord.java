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
@Table(name = "t_third_sys_user_record")
public class ThirdSysUserRecord {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
     * 闲鱼登录账户
     */
    @Column(name = "f_company_id")
    private String companyId;
    
    
	/**
	 * 闲鱼登录账户
	 */
	@Column(name = "f_user_id")
	private String userId;
	
    
	/**
	 * 房源ID
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;
	
	/**
     * 房间ID
     */
    @Column(name = "f_room_id")
    private int roomId;
    
    /**
     * 房间ID
     */
    @Column(name = "f_house_entire")
    private long houseEntire;
    
    /**
     * 房间ID
     */
    @Column(name = "f_item_id")
    private String itemId;
    
    /**
     * 房态
     */
    @Column(name = "f_house_status")
    private long houseStatus;

	
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
     * 闲鱼是否下架  0否 1是
     */
    @Column(name = "f_is_off")
    private int isOff;
    
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

    public String getHouseSellId() {
        return houseSellId;
    }

    public void setHouseSellId(String houseSellId) {
        this.houseSellId = houseSellId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public long getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(long houseStatus) {
        this.houseStatus = houseStatus;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public long getHouseEntire() {
        return houseEntire;
    }

    public void setHouseEntire(long houseEntire) {
        this.houseEntire = houseEntire;
    }

    
    public int getIsOff() {
        return isOff;
    }

    public void setIsOff(int isOff) {
        this.isOff = isOff;
    }

    @Override
    public String toString() {
        return "ThirdSysUserRecord [id=" + id + ", companyId=" + companyId + ", userId=" + userId + ", houseSellId="
                + houseSellId + ", roomId=" + roomId + ", houseEntire=" + houseEntire + ", itemId=" + itemId
                + ", houseStatus=" + houseStatus + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", status="
                + status + ", isOff=" + isOff + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }


	
}
