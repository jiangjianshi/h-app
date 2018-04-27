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

@Entity
@Table(name = "t_agency_default_score")
public class AgencyDefaultScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "f_source")
	private String source; //渠道来源
	
	@Column(name = "f_score")
	private Double score; //整租默认分
	
	@Column(name = "f_rscore")
	private Double rScore; //分租默认分
	
	@Column(name = "f_status")
	private Integer status; //状态：1，有效 0 无效
	
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime; //创建时间
	
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime; //最后更新时间
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
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
	
	
	public Double getrScore() {
		return rScore;
	}

	public void setrScore(Double rScore) {
		this.rScore = rScore;
	}

	@Override
	public String toString() {
		return "AgencyDefaultScore [id=" + id + ", source=" + source + ", score=" + score + ", rScore=" + rScore
				+ ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

	
}
