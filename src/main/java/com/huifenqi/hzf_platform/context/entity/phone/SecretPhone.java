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
@Table(name = "t_secret_phone")
public class SecretPhone {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "f_secret_phone_no")
	private String  secretPhoneNo;
	
	@Column(name = "f_bind_status")
	private int  bindStatus;
	
	@Column(name = "f_status")
	private Integer  status;
	
	@Column(name = "f_version")
	private long  version;
	
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createTime;
	
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  updateTime;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getSecretPhoneNo() {
		return secretPhoneNo;
	}

	public void setSecretPhoneNo(String secretPhoneNo) {
		this.secretPhoneNo = secretPhoneNo;
	}

	public int getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(int bindStatus) {
		this.bindStatus = bindStatus;
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
	
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "SecretPhone [id=" + id + ", secretPhoneNo=" + secretPhoneNo + ", bindStatus=" + bindStatus + ", status="
				+ status + ", version=" + version + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
}
