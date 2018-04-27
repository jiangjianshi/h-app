package com.huifenqi.hzf_platform.context.entity.phone;

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
@Table(name = "t_bind_record")
public class BindRecord {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "f_house_sell_id")
	private String sellId;

	@Column(name = "f_room_id")
	private long roomId;

	@Column(name = "f_agency_phone")
	private String agencyPhone = "";

	/**
	 * 保护号
	 */
	@Column(name = "f_secret_no")
	private String secretNo = "";

	/**
	 * 绑定关系ID
	 */
	@Column(name = "f_sub_id")
	private String subId = "";

	/**
	 * 业务ID
	 */
	@Column(name = "f_out_id")
	private String outId = "";

	/**
	 * 虚拟号分配状态
	 */
	@Column(name = "f_assign_status")
	private Integer assignStatus;

	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getSecretNo() {
		return secretNo;
	}

	public void setSecretNo(String secretNo) {
		this.secretNo = secretNo;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public Integer getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(Integer assignStatus) {
		this.assignStatus = assignStatus;
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
