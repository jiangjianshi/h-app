/** 
 * Project Name: hzf_platform_project 
 * File Name: CityInfoVersion.java 
 * Package Name: com.huifenqi.hzf_platform.context.entity.location 
 * Date: 2016年5月18日下午8:20:37 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.entity.location;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * ClassName: CityInfoVersion
 * date: 2016年5月18日 下午8:20:37
 * Description: 城市相关信息版本
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Entity
@Table(name = "t_city_info_version")
public class CityInfoVersion {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "f_type_desc")
	private String typeDesc;
	
	@Column(name = "f_info_type")
	private int infoType;
	
	@Column(name = "f_version")
	private Integer version;
	
	@Column(name = "f_create_time")
	private Date createTime;
	
	@Column(name = "f_update_time")
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}


	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}


	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
