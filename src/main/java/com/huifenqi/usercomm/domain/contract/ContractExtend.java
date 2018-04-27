package com.huifenqi.usercomm.domain.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_contract_extend")
public class ContractExtend {
	/**
	 * 合同号
	 */
	@Id
	@Column(name = "f_contract_no")
	private String contractNo;
	
	/**
	 * 租客首付方式 0：押一付零 1：押一付一 2：押一付二
	 */
	@Column(name = "f_down_payment_way")
	private int downPaymentWay;
	
	/**
	 * 服务费承担方1:租客 2:中介
	 */
	@Column(name = "f_service_fee_payer")
	private int serviceFeePayer;
	
	/**
	 * 提前还款天数
	 */
	@Column(name = "f_prepay_days")
	private int prePayDays;
	
	/**
	 * 优惠月数
	 */
	@Column(name = "f_privilege_month")
	private int privilegeMonth;
	
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
	private int state;
	
	/**
	 * 是否免除代理合同（1是，0不是）
	 */
	@Column(name = "f_exempt_agent")
	private int exemptAgent;
	
	/**
	 * (0,资料不全，1资料全，2有保证函)
	 */
	@Column(name = "f_perfect")
	private int perfect;
	
	/**
	 * 1,免，2中介 3合同
	 */
	@Column(name = "f_breach_service_fee")
	private int breachServiceFee;
	
	/**
	 * 第一期还款日
	 */
	@Column(name = "f_first_pay_date")
	private Date firstPayDate;
	
	/**
	 * 第一资金来源
	 */
	@Column(name = "f_first_sources_capital")
	private String firstSourcesCapital;
	
	/**
	 * 是否收保证金   0不受    1收
	 */
	@Column(name = "f_is_deposit")
	private int isDeposit;

	/**
	 * 转租状态 0：否	 1：转租进行中
	 */
	@Column(name = "f_sublet")
	private int sublet;
	/**
	 * 手动下线 0：显示	 1：隐藏
	 */
	@Column(name = "f_manual_mark")
	private int manualMark;

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public int getDownPaymentWay() {
		return downPaymentWay;
	}

	public void setDownPaymentWay(int downPaymentWay) {
		this.downPaymentWay = downPaymentWay;
	}

	public int getServiceFeePayer() {
		return serviceFeePayer;
	}

	public void setServiceFeePayer(int serviceFeePayer) {
		this.serviceFeePayer = serviceFeePayer;
	}

	public int getPrePayDays() {
		return prePayDays;
	}

	public void setPrePayDays(int prePayDays) {
		this.prePayDays = prePayDays;
	}

	public int getPrivilegeMonth() {
		return privilegeMonth;
	}

	public void setPrivilegeMonth(int privilegeMonth) {
		this.privilegeMonth = privilegeMonth;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getExemptAgent() {
		return exemptAgent;
	}

	public void setExemptAgent(int exemptAgent) {
		this.exemptAgent = exemptAgent;
	}

	public int getPerfect() {
		return perfect;
	}

	public void setPerfect(int perfect) {
		this.perfect = perfect;
	}

	public int getBreachServiceFee() {
		return breachServiceFee;
	}

	public void setBreachServiceFee(int breachServiceFee) {
		this.breachServiceFee = breachServiceFee;
	}

	public Date getFirstPayDate() {
		return firstPayDate;
	}

	public void setFirstPayDate(Date firstPayDate) {
		this.firstPayDate = firstPayDate;
	}

	public String getFirstSourcesCapital() {
		return firstSourcesCapital;
	}

	public void setFirstSourcesCapital(String firstSourcesCapital) {
		this.firstSourcesCapital = firstSourcesCapital;
	}

	public int getIsDeposit() {
		return isDeposit;
	}

	public void setIsDeposit(int isDeposit) {
		this.isDeposit = isDeposit;
	}

	public int getSublet() {
		return sublet;
	}

	public void setSublet(int sublet) {
		this.sublet = sublet;
	}

	public int getManualMark() {
		return manualMark;
	}

	public void setManualMark(int manualMark) {
		this.manualMark = manualMark;
	}
}
