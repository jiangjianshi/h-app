package com.huifenqi.usercomm.domain.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_contract_deposit")
public class ContractDeposit {

	@Id
	@Column(name = "f_deposit_id")
	private Long depositId;
	
	/**
	 * 合同号
	 */
	@Column(name = "f_contract_no")
	private String contractNo;
	
	/**
	 * 月租金
	 */
	@Column(name = "f_installment_amount")
	private Integer installmentAmount;
	
	/**
	 * 保证金费率
	 */
	@Column(name = "f_deposit_rate")
	private Integer depositRate;
	
	/**
	 * 服务费
	 */
	@Column(name = "f_service_amount")
	private Integer serviceAmount;
	
	/**
	 * 保证金
	 */
	@Column(name = "f_deposit_amount")
	private  Integer depositAmount;
	
	/**
	 * 支付状态
	 */
	@Column(name = "f_status")
	private Integer status;
	
	/**
	 * 创建时间
	 */
	@Column(name = "f_create_time")
	private Date createTime;
	
	/**
	 * 最后修改时间
	 */
	@Column(name = "f_update_time")
	private Date updateTime;
	
	/**
	 * 状态
	 */
	@Column(name = "f_state")
	private String  state;

	public Long getDepositId() {
		return depositId;
	}

	public void setDepositId(Long depositId) {
		this.depositId = depositId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Integer getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(Integer installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public Integer getDepositRate() {
		return depositRate;
	}

	public void setDepositRate(Integer depositRate) {
		this.depositRate = depositRate;
	}

	public Integer getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Integer serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Integer getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Integer depositAmount) {
		this.depositAmount = depositAmount;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}