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
@Table(name = "t_custom_city_price")
public class CustomCityPrice {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 城市id
	 */
	@Column(name = "f_city_id")
	private long cityId;

	/**
	 * 限定价格
	 */
	@Column(name = "f_limit_price")
	private Integer limitPrice;
	
	/**
	 * 不包含的ModelID
	 */
	@Column(name = "f_exclude_model_id")
	private String excludeModelId;

	/**
	 * 状态
	 */
	@Column(name = "f_status")
	private Integer status;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public Integer getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Integer limitPrice) {
		this.limitPrice = limitPrice;
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

	public String getExcludeModelId() {
		return excludeModelId;
	}

	public void setExcludeModelId(String excludeModelId) {
		this.excludeModelId = excludeModelId;
	}

	@Override
	public String toString() {
		return "CustomCityPrice [id=" + id + ", cityId=" + cityId + ", limitPrice=" + limitPrice + ", excludeModelId="
				+ excludeModelId + ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
	
}
