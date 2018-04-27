package com.huifenqi.hzf_platform.context.entity.house;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "t_company_white_config")
public class CompanyWhiteConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long f_id;

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
	 * 中介公司下架原因描述
	 */
	@Column(name = "f_white_desc")
	private String whiteDesc;
	
	/**
	 * 是否删除
	 */
	@Column(name = "f_status")
	private int status;
	
	/**
	 * 下架数量
	 */
	@Column(name = "f_white_count")
	private int whiteCount;
	
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

	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
	public String getWhiteDesc() {
        return whiteDesc;
    }

    public void setWhiteDesc(String whiteDesc) {
        this.whiteDesc = whiteDesc;
    }

    public int getWhiteCount() {
        return whiteCount;
    }

    public void setWhiteCount(int whiteCount) {
        this.whiteCount = whiteCount;
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

    @Override
    public String toString() {
        return "CompanyWhiteConfig [f_id=" + f_id + ", source=" + source + ", appId=" + appId + ", companyId="
                + companyId + ", companyName=" + companyName + ", whiteDesc=" + whiteDesc + ", status=" + status
                + ", whiteCount=" + whiteCount + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }


}
