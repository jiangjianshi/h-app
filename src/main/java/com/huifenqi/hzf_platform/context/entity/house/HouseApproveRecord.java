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
 * 房源审批记录
 * @author jjs
 *
 */
@Entity
@Table(name = "t_house_approve_record")
public class HouseApproveRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 房源ID
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;
	
	/**
	 * 房间ID
	 */
	@Column(name = "f_room_id")
	private Long roomId;
	
	/**
	 * 操作者
	 */
	@Column(name = "f_operator")
	private String operator;

	/**
	 * 错误原因
	 */
	@Column(name = "f_error_reason")
	private String errorReason;

	/**
	 * 审批备注
	 */
	@Column(name = "f_approve_desc")
	private String approveDesc;
	
	/**
	 * 图片审核状态
	 */
	@Column(name = "f_image_status")
	private Integer imageStatus;
	
	/**
	 * 图片评分
	 */
	@Column(name = "f_image_score")
	private Integer imageScore;
	
	/**
	 * 是否有效
	 */
	@Column(name = "f_status")
	private Integer status;

	/**
	 * 审批时间
	 */
	@Column(name = "f_approve_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveDate;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getApproveDesc() {
		return approveDesc;
	}

	public void setApproveDesc(String approveDesc) {
		this.approveDesc = approveDesc;
	}

	public Integer getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(Integer imageStatus) {
		this.imageStatus = imageStatus;
	}

	public Integer getImageScore() {
		return imageScore;
	}

	public void setImageScore(Integer imageScore) {
		this.imageScore = imageScore;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
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
		return "HouseApproveRecord [id=" + id + ", sellId=" + sellId + ", roomId=" + roomId + ", operator=" + operator
				+ ", errorReason=" + errorReason + ", approveDesc=" + approveDesc + ", imageStatus=" + imageStatus
				+ ", imageScore=" + imageScore + ", status=" + status + ", approveDate=" + approveDate + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}


}
