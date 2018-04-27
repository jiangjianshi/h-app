/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.third;

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
 * ClassName: ThirdSysRecord date: 2017年12月23日 下午4:05:50 Description:同步数据
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_third_sys_company")
public class ThirdSysCompany {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 渠道
	 */
	@Column(name = "f_source")
	private String source;
	/**
	 * 渠道ID
	 */
	@Column(name = "f_app_id")
	private String appId;
	
	/**
     * 公司名称
     */
    @Column(name = "f_company_name")
    private String companyName;
    /**
     * 公司ID
     */
    @Column(name = "f_company_id")
    private String companyId;
    
    /**
     * 描述
     */
    @Column(name = "f_desc")
    private String desc;
    
    /**
     * 状态
     */
    @Column(name = "f_status")
    private int status;
    
    /**
     * 发布方向  0会分期公司   1外部中介
     */
    @Column(name = "f_type")
    private int type;
	
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
