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
@Table(name = "t_call_record")
public class CallInfoRecord {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 唯一业务ID
	 */
	@Column(name = "f_out_id")
	private String outId = "";
	
	/**
	 * 拨打ID
	 */
	@Column(name = "f_call_id")
	private String callId;

	/**
	 * 主叫电话
	 */
	@Column(name = "f_custom_phone")
	private String customPhone = "";

	/**
	 * aliyun返回的拨打开始时间
	 */
	@Column(name = "f_call_time")
	private String callTime;

	/**
	 * 释放方向
	 */
	@Column(name = "f_release_dir")
	private String releaseDir;

	/**
	 * 通话时长
	 */
	@Column(name = "f_call_duration")
	private int callDuration;
	
	/**
	 * 释放方向
	 */
	@Column(name = "f_voice_record_url")
	private String voiceRecordUrl;
	
	
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

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCustomPhone() {
		return customPhone;
	}

	public void setCustomPhone(String customPhone) {
		this.customPhone = customPhone;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getReleaseDir() {
		return releaseDir;
	}

	public void setReleaseDir(String releaseDir) {
		this.releaseDir = releaseDir;
	}

	public int getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(int callDuration) {
		this.callDuration = callDuration;
	}

	public String getVoiceRecordUrl() {
		return voiceRecordUrl;
	}

	public void setVoiceRecordUrl(String voiceRecordUrl) {
		this.voiceRecordUrl = voiceRecordUrl;
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
