package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/24.
 */
@Entity
public class ContractSnapshot {

	/**
	 * 用户付款方式 - 月付
	 */
	public static final int PAYMENT_1 = 1;

	/**
	 * 用户付款方式 - 季付
	 */
	public static final int PAYMENT_3 = 2;

	/**
	 * 用户付款方式 - 半年付
	 */
	public static final int PAYMENT_6 = 3;

	/**
	 * 用户付款方式 - 年付
	 */
	public static final int PAYMENT_12 = 4;

	/**
	 * 合同状态 - 初始状态
	 */
	public static final int STATUS_INIT = 0;

	/**
	 * 合同状态 - 生效中
	 */
	public static final int STATUS_TAKING_EFFECT = 1;

	/**
	 * 合同状态 - 已终止
	 */
	public static final int STATUS_TERMINATED = 2;

	/**
	 * 合同状态 - 违约
	 */
	public static final int STATUS_RENEGE = 3;

	/**
	 * 合同状态 - hzf发起违约
	 */
	public static final int STATUS_HZF_RENEGE = 4;

	/**
	 * 合同状态 - 违约待确认
	 */
	public static final int STATUS_RENEGE_CHECKING = 5;

	/**
	 * 合同状态 - 转租
	 */
	public static final int STATUS_SUBLET = 6;

	/**
	 * 快照Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long snapshotId;

	/**
	 * 用户Id
	 */
	@Column(name = "userid")
	private long userId;

	/**
	 * 用户身份证号
	 */
	@Column(name = "user_id_no")
	private String userIdNo;

	/**
	 * 用户手机号
	 */
	private String userMobile;

	/**
	 * 录入合同的销售人员，对于C端用户，该销售就是跟进用户的销售；对于B端用户，跟进的是中介公司的销售，录入的是我们的销售
	 */
	private String enterBrokerId;

	/**
	 * 支付Id (pay表已废弃)
	 */
	private long payId;

	/**
	 * 会分期付款给房东的方式
	 */
	private long hzfPayType;

	/**
	 * 月租金
	 */
	private long installmentAmount;

	/**
	 * 是否有效合同，1：有效；0：无效；-1：物理删除
	 */
	private Integer state;

	/**
	 * 合同状态。0：初始状态，1：生效中，2. 已终止，3:违约
	 */
	private Integer contractStatus = STATUS_INIT;

	/**
	 * 用户付款方式
	 */
	private Integer userPayType;

	/**
	 * 房源地址
	 */
	private String address;

	/**
	 * 每月费率：千分比
	 */
	private Integer installmentRate;

	/**
	 * 每月固定还款日
	 */
	private Integer prepayDay;

	/**
	 * 租期开始时间
	 */
	@Temporal(TemporalType.DATE)
	private Date leaseBegin;

	/**
	 * 租期结束时间
	 */
	@Temporal(TemporalType.DATE)
	private Date leaseEnd;

	/**
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime = new Date();

	/**
	 * 合同号
	 */
	private String contractNo;

	/**
	 * 月服务费
	 */
	private long serviceFee;

	/**
	 * 租约类型:1:普租, 2:代理, 3:C端合同, 4:公寓, 5:收房业务
	 */
	private Integer leaseType;
	
	/**
	 * 签约日期
	 */
	@Temporal(TemporalType.DATE)
	private Date signTime;

	/**
	 * 中介公司id
	 */
	private Integer agencyId;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getSnapshotId() {
		return snapshotId;
	}

	public long getUserId() {
		return userId;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public String getEnterBrokerId() {
		return enterBrokerId;
	}

	public long getPayId() {
		return payId;
	}

	public long getHzfPayType() {
		return hzfPayType;
	}

	public long getInstallmentAmount() {
		return installmentAmount;
	}

	public Integer getState() {
		return state;
	}

	public Integer getContractStatus() {
		return contractStatus;
	}

	public Integer getUserPayType() {
		return userPayType;
	}

	public Integer getInstallmentRate() {
		return installmentRate;
	}

	public Integer getPrepayDay() {
		return prepayDay;
	}

	public Date getLeaseBegin() {
		return leaseBegin;
	}

	public Date getLeaseEnd() {
		return leaseEnd;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getContractNo() {
		return contractNo;
	}

	public long getServiceFee() {
		return serviceFee;
	}

	public Integer getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(Integer leaseType) {
		this.leaseType = leaseType;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	/*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
	@Override
	public String toString() {
		return "ContractSnapshot [snapshotId=" + snapshotId + ", userId=" + userId + ", userIdNo=" + userIdNo
				+ ", userMobile=" + userMobile + ", enterBrokerId=" + enterBrokerId + ", payId=" + payId
				+ ", hzfPayType=" + hzfPayType + ", installmentAmount=" + installmentAmount + ", state=" + state
				+ ", contractStatus=" + contractStatus + ", userPayType=" + userPayType + ", address=" + address
				+ ", installmentRate=" + installmentRate + ", prepayDay=" + prepayDay + ", leaseBegin=" + leaseBegin
				+ ", leaseEnd=" + leaseEnd + ", updateTime=" + updateTime + ", contractNo=" + contractNo
				+ ", serviceFee=" + serviceFee + ", leaseType=" + leaseType + "]";
	}

}