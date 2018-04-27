package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_house_dicitem")
public class HouseDicitem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long f_id;

	/**
	 * 本级ID
	 */
	@Column(name = "f_dic_id")
	private String dicId;

	/**
	 * 公司ID
	 */
	@Column(name = "f_company_id")
	private String companyId;

	/**
	 * 公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;

	/**
	 * 字典类型
	 */
	@Column(name = "f_dic_type")
	private String dicType;

	/**
	 * 字典编码
	 */
	@Column(name = "f_dic_code")
	private String dicCode;

	/**
	 * 字典名称
	 */
	@Column(name = "f_dic_name")
	private String dicName;

	/**
	 * 字典值
	 */
	@Column(name = "f_dic_value")
	private String dicValue;
	
	/**
	 * 是否删除
	 */
	@Column(name = "f_is_delete")
	private int isDelete;
	
	/**
	 * 排序
	 */
	@Column(name = "f_sort")
	private String sort;
	
	/**
	 * 创建人
	 */
	@Column(name = "f_create_user")
	private String createUser;
	
	/**
	 * 父级ID
	 */
	@Column(name = "f_parent_id")
	private String parentId;
	
	/**
	 * 字典等级
	 */
	@Column(name = "f_dic_rank")
	private String dicRank;
	
	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	private Date createTime;
	
	
	public Long getF_id() {
		return f_id;
	}

	public void setF_id(Long f_id) {
		this.f_id = f_id;
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
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

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}


	public String getDicValue() {
		return dicValue;
	}


	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}


	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}




	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getDicRank() {
		return dicRank;
	}


	public void setDicRank(String dicRank) {
		this.dicRank = dicRank;
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

	/**
	 * 最后更新时间
	 */
	@Column(name = "f_update_time")
	private Date updateTime;
	
}
