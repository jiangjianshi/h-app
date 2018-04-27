/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
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
 * ClassName: CompanyPayMonth date: 2017年8月31日 下午4:05:50 Description:支持分期公司
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
/**
 * @author huifenqi
 *
 */
@Entity
@Table(name = "t_company_pay_month")
public class CompanyPayMonth {

	public CompanyPayMonth() {
	}

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 公司名称-hzf
	 */
	@Column(name = "f_company_name_hzf")
	private String companyNameHzf;

	/**
	 * 公司ID-hzf
	 */
	@Column(name = "f_company_id_hzf")
	private String companyIdHzf;


	/**
	 * 公司名称-saas
	 */
	@Column(name = "f_company_name_saas")
	private String companyNameSaas;

	/**
	 * 公司ID-saas
	 */
	@Column(name = "f_company_id_saas")
	private String companyIdsaas;

	/**
	 * 是否支持月份
	 */
	@Column(name = "f_pay_status")
	private int payStatus;

	/**
	 * 是否收集月付标签
	 */
	@Column(name = "f_run")
	private int run;

	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;

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

	public String getCompanyNameHzf() {
		return companyNameHzf;
	}

	public void setCompanyNameHzf(String companyNameHzf) {
		this.companyNameHzf = companyNameHzf;
	}

	public String getCompanyIdHzf() {
		return companyIdHzf;
	}

	public void setCompanyIdHzf(String companyIdHzf) {
		this.companyIdHzf = companyIdHzf;
	}

	public String getCompanyNameSaas() {
		return companyNameSaas;
	}

	public void setCompanyNameSaas(String companyNameSaas) {
		this.companyNameSaas = companyNameSaas;
	}

	public String getCompanyIdsaas() {
		return companyIdsaas;
	}

	public void setCompanyIdsaas(String companyIdsaas) {
		this.companyIdsaas = companyIdsaas;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getRun() {
		return run;
	}

	public void setRun(int run) {
		this.run = run;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
		return "CompanyPayMonth [id=" + id + ", companyNameHzf=" + companyNameHzf + ", companyIdHzf=" + companyIdHzf
				+ ", companyNameSaas=" + companyNameSaas + ", companyIdsaas=" + companyIdsaas + ", payStatus="
				+ payStatus + ", run=" + run + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
}