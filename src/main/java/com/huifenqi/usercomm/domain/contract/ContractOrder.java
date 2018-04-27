/** 
* Project Name: trunk 
* File Name: ContractOrder.java 
* Package Name: com.huifenqi.usercomm.domain.huizhaofang 
* Date: 2016年6月23日上午10:09:01 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: ContractOrder date: 2016年6月23日 上午10:09:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_contract_order")
public class ContractOrder {

	/**
	 * 合同号
	 */
	@Id
	@Column(name = "f_contract_no")
	private String contractNo;

	/**
	 * 还款信息
	 */
	@Column(name = "f_subpay_list")
	private String subpayList;

	/**
	 * 是否有效合同，1：有效；0：无效；-1：物理删除
	 */
	@Column(name = "f_state")
	private int state;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_create_time")
	private Date createTime = new Date();

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_update_time")
	private Date updateTime = new Date();

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getSubpayList() {
		return subpayList;
	}

	public void setSubpayList(String subpayList) {
		this.subpayList = subpayList;
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

}
