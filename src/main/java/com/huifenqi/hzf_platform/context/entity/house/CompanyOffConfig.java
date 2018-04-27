package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_company_off_config")
public class CompanyOffConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long f_id;

	/**
	 * 中介公司ID
	 */
	@Column(name = "f_company_id")
	private String companyId;

	/**
	 * 中介公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;
	
	/**
	 * 城市ID
	 */
	@Column(name = "f_city_id")
	private long cityId;
	
	/**
	 * 城市名称
	 */
	@Column(name = "f_city_name")
	private String cityName;

	/**
	 * 中介公司下架原因描述
	 */
	@Column(name = "f_off_desc")
	private String offDesc;
	
	/**
	 * 是否删除
	 */
	@Column(name = "f_status")
	private int status;
	
	/**
	 * 下架数量
	 */
	@Column(name = "f_off_count")
	private int offCount;
	
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

	public long getF_id() {
		return f_id;
	}

	public void setF_id(long f_id) {
		this.f_id = f_id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOffDesc() {
		return offDesc;
	}

	public void setOffDesc(String offDesc) {
		this.offDesc = offDesc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	public int getOffCount() {
		return offCount;
	}

	public void setOffCount(int offCount) {
		this.offCount = offCount;
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
		return "CompanyOffConfig [f_id=" + f_id + ", companyId=" + companyId + ", companyName=" + companyName
				+ ", cityId=" + cityId + ", cityName=" + cityName + ", offDesc=" + offDesc + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}


}
