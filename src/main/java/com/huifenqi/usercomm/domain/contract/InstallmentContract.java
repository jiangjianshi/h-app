package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/24.
 *
 * 会分期合同
 */
@Entity
public class InstallmentContract {

	/**
	 * 审核状态 - 审核中
	 */
	public static final int CONTRACT_STATUS_EVALUATING = 0;

	/**
	 * 审核状态 - 审核失败
	 */
	public static final int CONTRACT_STATUS_EVALUATE_FAIL = 1;

	/**
	 * 审核状态 - 审核成功
	 */
	public static final int CONTRACT_STATUS_EVALUATE_SUCCESS = 2;

	/**
	 * 审核状态 - 待确认收款
	 */
	public static final int CONTRACT_STATUS_NOT_RECEIPT = 3;

	/**
	 * 审核状态 - 确认已收款
	 */
	public static final int CONTRACT_STATUS_RECEIPTED = 4;

	/**
	 * 审核状态 - 待审核
	 */
	public static final int CONTRACT_STATUS_WAITING_EVALUATING = 5;

	/**
	 * 审核状态 - 预审拒绝
	 */
	public static final int CONTRACT_STATUS_REJECT_PREEVALUATE = 6;

	/**
	 * 审核状态 - 待确认
	 */
	public static final int CONTRACT_STATUS_PREVERIFY = 7;

	/**
	 * 审核状态 - 待补全
	 */
	public static final int CONTRACT_STATUS_WAITING_COMPLETION = 8;

	/**
	 * 审核状态 - 修改待确认
	 */
	public static final int CONTRACT_STATUS_WAITING_CONFIRMATION_AFTER_MODIFICATION = 9;

	/**
	 * 租约类型 - 普租
	 */
	public static final int LEASE_TYPE_NORMAL = 1;

	/**
	 * 租约类型 - 代理
	 */
	public static final int LEASE_TYPE_AGENCY = 2;

	/**
	 * 租约类型 - C端合同
	 */
	public static final int LEASE_TYPE_CONTRACT_C = 3;

	/**
	 * 租约类型 - 公寓
	 */
	public static final int LEASE_TYPE_Apartment = 4;

	/**
	 * 租约类型 - 收房业务
	 */
	public static final int LEASE_TYPE_ROOM_COLLECT = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long installmentContractId;

	/**
	 * 审核状态
	 */
	private int contractStatus = CONTRACT_STATUS_EVALUATING;

	/**
	 * 用户Id
	 */
	@Column(name = "userid")
	private long userId;

	/**
	 * 用户号码
	 */
	private String userMobile;

	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 用户身份证号
	 */
	private String userIdNo;

	/**
	 * 会分期付款给房东的方式
	 */
	private long hzfPayType;

	/**
	 * 每月金额
	 */
	private long monthlyAmount;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 用户付款方式
	 */
	private int userPayType;

	/**
	 * 租约开始时间
	 */
	@Temporal(TemporalType.DATE)
	private Date leaseBegin;

	/**
	 * 租约结束时间
	 */
	@Temporal(TemporalType.DATE)
	private Date leaseEnd;

	/**
	 * 合同号
	 */
	private String contractNo;

	/**
	 * 是否有效合同，1：有效；0：无效；-1：物理删除
	 */
	private int state;

	/**
	 * 录入合同的销售人员，对于C端用户，该销售就是跟进用户的销售；对于B端用户，跟进的是中介公司的销售，录入的是我们的销售
	 */
	private String enterBrokerId;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime = new Date();

	/**
	 * 总共多少个月
	 */
	private int totalMonths;

	/**
	 * 总共多少天
	 */
	private int totalDays;

	/**
	 * 房东姓名
	 */
	private String ownerName;

	/**
	 * 房东卡号
	 */
	private String ownerCardNo;

	/**
	 * 房东电话
	 */
	private String ownerPhone;

	/**
	 * 服务费
	 */
	private long serviceFee;

	/**
	 * 快照表ID
	 */
	private long contractSnapshotId;

	/**
	 * 在快照表里面的状态
	 */
	private int snapshotStatus;

	/**
	 * 租约类型:1:普租, 2:代理, 3:C端合同, 4:公寓, 5:收房业务
	 */
	private int leaseType;

	/**
	 * 合同删除状态：0.未删除, 1.已删除
	 */
	private int fundType;

	/**
	 * 中介公司Id
	 */
	private int agencyId;
	
	/**
	 * 用户手机号
	 */
	private String userQq;
	
	/**
	 * 用户开户银行
	 */
	private String userBank;
	
	/**
	 * 用户账户名称
	 */
	private String userBankAccount;
	
	/**
	 * 用户银行卡号 
	 */
	private String userCardNo;
	
	/**
	 * 银行预留手机号
	 */
	private String userCardMobile;
	
	/**
	 * 用户公司名称
	 */
	private String userCompanyName;
	
	/**
	 * 用户公司地址
	 */
	private String userCompanyAddress;
	
	/**
	 * 用户公司邮箱
	 */
	private String userCompanyMail;
	
	/**
	 * 用户收入
	 */
	private int userIncome;
	
	/**
	 * 工作职位
	 */
	private String userJob;
	
	/**
	 * 紧急联系人姓名
	 */
	private String userLinkmanName;
	
	/**
	 * 紧急联系人电话
	 */
	private String userLinkmanMobile;

	/**
	 * 紧急联系人关系
	 */
	private int userLinkmanRelation;
	
	/**
	 * 用户工作状态
	 */
	private int userStatus;
	
	/**
	 * 签约日期
	 */
	@Temporal(TemporalType.DATE)
	private Date signTime;

	/**
	 * 合同来源
	 * 0：crm提交合同
	 * 1：经纪人客户端(无纸化)提交合同
	 */
	private Integer source;

	/**
	 * 是否转租
	 */
	private Integer transfer;

	/**
	 * 转租来源
	 */
	private String srcContractNo;

	public long getInstallmentContractId() {
		return installmentContractId;
	}

	public void setInstallmentContractId(long installmentContractId) {
		this.installmentContractId = installmentContractId;
	}

	public int getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(int contractStatus) {
		this.contractStatus = contractStatus;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public void setUserIdNo(String userIdNo) {
		this.userIdNo = userIdNo;
	}

	public long getHzfPayType() {
		return hzfPayType;
	}

	public void setHzfPayType(long hzfPayType) {
		this.hzfPayType = hzfPayType;
	}

	public long getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(long monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUserPayType() {
		return userPayType;
	}

	public void setUserPayType(int userPayType) {
		this.userPayType = userPayType;
	}

	public Date getLeaseBegin() {
		return leaseBegin;
	}

	public void setLeaseBegin(Date leaseBegin) {
		this.leaseBegin = leaseBegin;
	}

	public Date getLeaseEnd() {
		return leaseEnd;
	}

	public void setLeaseEnd(Date leaseEnd) {
		this.leaseEnd = leaseEnd;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getEnterBrokerId() {
		return enterBrokerId;
	}

	public void setEnterBrokerId(String enterBrokerId) {
		this.enterBrokerId = enterBrokerId;
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

	public int getTotalMonths() {
		return totalMonths;
	}

	public void setTotalMonths(int totalMonths) {
		this.totalMonths = totalMonths;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerCardNo() {
		return ownerCardNo;
	}

	public void setOwnerCardNo(String ownerCardNo) {
		this.ownerCardNo = ownerCardNo;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
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

	public int getSnapshotStatus() {
		return snapshotStatus;
	}

	public void setSnapshotStatus(int snapshotStatus) {
		this.snapshotStatus = snapshotStatus;
	}

	public int getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(int leaseType) {
		this.leaseType = leaseType;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getUserQq() {
		return userQq;
	}

	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}

	public String getUserBank() {
		return userBank;
	}

	public void setUserBank(String userBank) {
		this.userBank = userBank;
	}

	public String getUserBankAccount() {
		return userBankAccount;
	}

	public void setUserBankAccount(String userBankAccount) {
		this.userBankAccount = userBankAccount;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getUserCardMobile() {
		return userCardMobile;
	}

	public void setUserCardMobile(String userCardMobile) {
		this.userCardMobile = userCardMobile;
	}

	public String getUserCompanyName() {
		return userCompanyName;
	}

	public void setUserCompanyName(String userCompanyName) {
		this.userCompanyName = userCompanyName;
	}

	public String getUserCompanyAddress() {
		return userCompanyAddress;
	}

	public void setUserCompanyAddress(String userCompanyAddress) {
		this.userCompanyAddress = userCompanyAddress;
	}

	public String getUserCompanyMail() {
		return userCompanyMail;
	}

	public void setUserCompanyMail(String userCompanyMail) {
		this.userCompanyMail = userCompanyMail;
	}

	public int getUserIncome() {
		return userIncome;
	}

	public void setUserIncome(int userIncome) {
		this.userIncome = userIncome;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public String getUserLinkmanName() {
		return userLinkmanName;
	}

	public void setUserLinkmanName(String userLinkmanName) {
		this.userLinkmanName = userLinkmanName;
	}

	public String getUserLinkmanMobile() {
		return userLinkmanMobile;
	}

	public void setUserLinkmanMobile(String userLinkmanMobile) {
		this.userLinkmanMobile = userLinkmanMobile;
	}

	public int getUserLinkmanRelation() {
		return userLinkmanRelation;
	}

	public void setUserLinkmanRelation(int userLinkmanRelation) {
		this.userLinkmanRelation = userLinkmanRelation;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTransfer() {
		return transfer;
	}

	public void setTransfer(Integer transfer) {
		this.transfer = transfer;
	}

	public String getSrcContractNo() {
		return srcContractNo;
	}

	public void setSrcContractNo(String srcContractNo) {
		this.srcContractNo = srcContractNo;
	}

	@Override
	public String toString() {
		return "InstallmentContract [installmentContractId=" + installmentContractId + ", contractStatus="
				+ contractStatus + ", userId=" + userId + ", userMobile=" + userMobile + ", userName=" + userName
				+ ", userIdNo=" + userIdNo + ", hzfPayType=" + hzfPayType + ", monthlyAmount=" + monthlyAmount
				+ ", address=" + address + ", userPayType=" + userPayType + ", leaseBegin=" + leaseBegin + ", leaseEnd="
				+ leaseEnd + ", contractNo=" + contractNo + ", state=" + state + ", enterBrokerId=" + enterBrokerId
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", totalMonths=" + totalMonths
				+ ", totalDays=" + totalDays + ", ownerName=" + ownerName + ", ownerCardNo=" + ownerCardNo
				+ ", ownerPhone=" + ownerPhone + ", serviceFee=" + serviceFee + ", contractSnapshotId="
				+ contractSnapshotId + ", snapshotStatus=" + snapshotStatus + ", leaseType=" + leaseType + ", fundType="
				+ fundType + ", agencyId=" + agencyId + ", userQq=" + userQq + ", userBank=" + userBank
				+ ", userBankAccount=" + userBankAccount + ", userCardNo=" + userCardNo + ", userCardMobile="
				+ userCardMobile + ", userCompanyName=" + userCompanyName + ", userCompanyAddress=" + userCompanyAddress
				+ ", userCompanyMail=" + userCompanyMail + ", userIncome=" + userIncome + ", userJob=" + userJob
				+ ", userLinkmanName=" + userLinkmanName + ", userLinkmanMobile=" + userLinkmanMobile
				+ ", userLinkmanRelation=" + userLinkmanRelation + ", userStatus=" + userStatus + "]";
	}

}
