package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/25.
 *
 * 子订单
 */
@Entity
public class Subpay {

	/**
	 * 支付状态 - 等待支付
	 */
	public static final int PAYSTATE_WAITING = 0;

	/**
	 * 支付状态 - 部分支付
	 */
	public static final int PAYSTATE_PAY_PART = 1;

	/**
	 * 支付状态 - 支付完成
	 */
	public static final int PAYSTATE_PAY_ALL = 2;

	/**
	 * 支付状态-已还款待平账
	 */
	public static final int PAYSTATE_PAY_UNBALANCE = 3;

	/**
	 * 支付状态-转租支付
	 */
	public static final int PAYSTATE_PAY_SUBLEASE_PAY_ALL = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "subpayid")
	private long subpayId;

	/**
	 * 房租订单id (无用，pay表已废弃)
	 */
	private long payId;

	/**
	 * 是否有效 0:无效, 1:有效
	 */
	private int state;

	/**
	 * 支付状态
	 */
	@Column(name = "status")
	private int payState;

	/**
	 * 期次
	 */
	private int index;

	/**
	 * 本期支付总金额，含服务费，单位分
	 */
	private long price;

	/**
	 * 本期未付金额，单位分
	 */
	private long unpayPrice;

	/**
	 * 每月租金，不含服务费，单位分
	 */
	private long basePrice;

	/**
	 * 月服务费
	 */
	private long serviceFee;

	/**
	 * 合同快照表id
	 */
	private long contractSnapshotId;

	/**
	 * 分期合同id
	 */
	private long installmentContractId;

	/**
	 * 最晚付款日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date payDate;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	/**
	 * 滞纳金
	 */
	@Column(name = "overdue_fine")
	private int lateFees;

	/**
	 * 应付多少钱滞纳金
	 */
	@Column(name = "overdue_fine_nature")
	private int lateFeesNature;

	/**
	 * 减免多少钱
	 */
	@Column(name = "overdue_fine_reduce")
	private int lateFeesReduce;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * 合同号
	 */
	private String contractNo;
	
	/**
	 * 首次资金来源
	 */
	private String sourcesCapital;
	
	/**
	 * 实际支付日期
	 */
	@Transient
	private Date realPayDate = new Date();
	
	/**
	 * 代金券金额
	 */
	@Transient
	private Long voucherPrice = 0L;
	
	/**
	 * 用户实际支付金额
	 */
	@Transient
	private Long realPayPrice = 0L;

	/**
	 * 线下还款时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date offlinePayTime;
	
	public long getSubpayId() {
		return subpayId;
	}

	public void setSubpayId(long subpayId) {
		this.subpayId = subpayId;
	}

	public long getPayId() {
		return payId;
	}

	public void setPayId(long payId) {
		this.payId = payId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPayState() {
		return payState;
	}

	public void setPayState(int payState) {
		this.payState = payState;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getUnpayPrice() {
		return unpayPrice;
	}

	public void setUnpayPrice(long unpayPrice) {
		this.unpayPrice = unpayPrice;
	}

	public long getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(long basePrice) {
		this.basePrice = basePrice;
	}

	public long getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(long serviceFee) {
		this.serviceFee = serviceFee;
	}

	public long getContractSnapshotId() {
		return contractSnapshotId;
	}

	public void setContractSnapshotId(long contractSnapshotId) {
		this.contractSnapshotId = contractSnapshotId;
	}

	public long getInstallmentContractId() {
		return installmentContractId;
	}

	public void setInstallmentContractId(long installmentContractId) {
		this.installmentContractId = installmentContractId;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getLateFees() {
		return lateFees;
	}

	public void setLateFees(int lateFees) {
		this.lateFees = lateFees;
	}

	public int getLateFeesNature() {
		return lateFeesNature;
	}

	public void setLateFeesNature(int lateFeesNature) {
		this.lateFeesNature = lateFeesNature;
	}

	public int getLateFeesReduce() {
		return lateFeesReduce;
	}

	public void setLateFeesReduce(int lateFeesReduce) {
		this.lateFeesReduce = lateFeesReduce;
	}

	/**
	 * 获取用户最终的金额
	 * 
	 * @return
	 */
	public int getActualOverDue() {
		return this.lateFeesNature - this.lateFeesReduce;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	
	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getSourcesCapital() {
		return sourcesCapital;
	}

	public void setSourcesCapital(String sourcesCapital) {
		this.sourcesCapital = sourcesCapital;
	}

	public Date getRealPayDate() {
		return realPayDate;
	}

	public void setRealPayDate(Date realPayDate) {
		this.realPayDate = realPayDate;
	}

	public Long getVoucherPrice() {
		return voucherPrice;
	}

	public void setVoucherPrice(Long voucherPrice) {
		this.voucherPrice = voucherPrice;
	}

	public Long getRealPayPrice() {
		return realPayPrice;
	}

	public void setRealPayPrice(Long realPayPrice) {
		this.realPayPrice = realPayPrice;
	}

	public Date getOfflinePayTime() {
		return offlinePayTime;
	}

	public void setOfflinePayTime(Date offlinePayTime) {
		this.offlinePayTime = offlinePayTime;
	}

	@Override
	public String toString() {
		return "Subpay [subpayId=" + subpayId + ", payId=" + payId + ", state=" + state + ", payState=" + payState
				+ ", index=" + index + ", price=" + price + ", unpayPrice=" + unpayPrice + ", basePrice=" + basePrice
				+ ", serviceFee=" + serviceFee + ", contractSnapshotId=" + contractSnapshotId
				+ ", installmentContractId=" + installmentContractId + ", payDate=" + payDate + ", startDate="
				+ startDate + ", endDate=" + endDate + ", lateFees=" + lateFees + ", lateFeesNature=" + lateFeesNature
				+ ", lateFeesReduce=" + lateFeesReduce + ", userId=" + userId + "]";
	}

}
