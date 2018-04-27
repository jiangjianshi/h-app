package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2017/04/20.
 *
 * 资金来源表
 */
@Entity
@Table(name = "t_account_source")
public class AccountSource {

	public AccountSource() {
	}

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 第一资金来源
	 */
	@Column(name = "f_sources_capital")
	private String sourceCapital;

	/**
	 * 账号集
	 */
	@Column(name = "f_sn")
	private String sn;

	/**
	 * 是否有效 0:无效, 1:有效
	 */
	@Column(name = "f_state")
	private int state;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_update_time")
	private Date updateTime;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSourceCapital() {
		return sourceCapital;
	}

	public void setSourceCapital(String sourceCapital) {
		this.sourceCapital = sourceCapital;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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
		return "AccountSource{" +
				"id=" + id +
				", sourceCapital='" + sourceCapital + '\'' +
				", sn='" + sn + '\'' +
				", state=" + state +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
