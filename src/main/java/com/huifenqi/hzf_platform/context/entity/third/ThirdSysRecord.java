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
 * ClassName: ThirdSysRecord date: 2017年12月23日 下午4:05:50 Description:同步数据
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_third_sys_record")
public class ThirdSysRecord {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 操作类型
	 */
	@Column(name = "f_opt_type")
	private int optType;
	/**
	 * 操作目标名称
	 */
	@Column(name = "f_opt_target_name")
	private String optTargetName;
	
	/**
     * 操作目标编码
     */
    @Column(name = "f_opt_target_code")
    private int optTargetCode;
    
	/**
	 * 房源来源名称
	 */
	@Column(name = "f_house_source_name")
	private String houseSourceName;
	/**
	 * 房源来源编号
	 */
	@Column(name = "f_house_source_appid")
	private String houseSourceAppid;
	/**
	 * 出租方式
	 */
	@Column(name = "f_house_entire_rent")
	private int houseEntireRent;
	/**
	 * 房间ID
	 */
	@Column(name = "f_room_id")
	private int roomId;
	/**
	 * 房源ID
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;
	/**
	 * 操作状态
	 */
	@Column(name = "f_opt_status")
	private String optStatus;
	
	/**
     * 错误描述
     */
    @Column(name = "f_error_code")
    private String errorCode;
    
	
	/**
	 * 错误编号
	 */
	@Column(name = "f_error_msg")
	private String errorMsg;
	
	/**
	 * 操作用户
	 */
	@Column(name = "f_opt_userid")
	private String optUserId;
	
	/**
	 * 操作用户
	 */
	@Column(name = "f_success_url")
	private String successUrl;
	
	/**
	 * 公寓信息创建时间
	 */
	@Column(name = "f_opt_creatime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	/**
	 * 公寓信息更新时间
	 */
	@Column(name = "f_opt_updatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public int getOptType() {
		return optType;
	}

	public void setOptType(int optType) {
		this.optType = optType;
	}

	public String getOptTargetName() {
        return optTargetName;
    }

    public void setOptTargetName(String optTargetName) {
        this.optTargetName = optTargetName;
    }


    public int getOptTargetCode() {
        return optTargetCode;
    }

    public void setOptTargetCode(int optTargetCode) {
        this.optTargetCode = optTargetCode;
    }

    public String getHouseSourceName() {
		return houseSourceName;
	}

	public void setHouseSourceName(String houseSourceName) {
		this.houseSourceName = houseSourceName;
	}

	public String getHouseSourceAppid() {
		return houseSourceAppid;
	}

	public void setHouseSourceAppid(String houseSourceAppid) {
		this.houseSourceAppid = houseSourceAppid;
	}


	public int getHouseEntireRent() {
		return houseEntireRent;
	}

	public void setHouseEntireRent(int houseEntireRent) {
		this.houseEntireRent = houseEntireRent;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getHouseSellId() {
		return houseSellId;
	}

	public void setHouseSellId(String houseSellId) {
		this.houseSellId = houseSellId;
	}

	public String getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(String optStatus) {
		this.optStatus = optStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getOptUserId() {
		return optUserId;
	}

	public void setOptUserId(String optUserId) {
		this.optUserId = optUserId;
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

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
	
	
}
