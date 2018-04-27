package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/12.
 * 
 * 会分期申请
 */
@Entity
public class InstallmentApply {

	/**
	 * 出租类型 - 未定义
	 */
	public static final int PATTERN_TYPE_DEFAULT = 0;

	/**
	 * 出租类型 - 整租
	 */
	public static final int PATTERN_TYPE_ENTIRE = 1;

	/**
	 * 出租类型 - 合租
	 */
	public static final int PATTERN_TYPE_JOIN = 2;

	/**
	 * 审核状态 - 审核中
	 */
	public static final int USER_STATE_CHECKING = 0;

	/**
	 * 审核状态 - 通过
	 */
	public static final int USER_STATE_SUCCESS = 1;

	/**
	 * 审核状态 - 失败
	 */
	public static final int USER_STATE_FAIL = 2;

	/**
	 * 分期申请Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long installmentApplyId;

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 身份证
	 */
	private String userIdNo;

	/**
	 * 用户证件类型，1：身份证；2：护照
	 */
	private int userIdType;

	/**
	 * 手机号
	 */
	@Column(name = "user_mobile")
	private String phone;

	/**
	 * 微信unionid
	 */
	private String wxUnionid;

	/**
	 * 经纪人id
	 */
	private long brokerId;

	/**
	 * 支付方式： 0：季付 1：半年付 2：年付
	 */
	private int payment;

	/**
	 * 租金
	 */
	private long rent;

	/**
	 * 支付时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date payDate;

	/**
	 * 房屋地址
	 */
	private String address;

	/**
	 * 出租方式
	 */
	private int patternInt;

	/**
	 * 申请状态
	 */
	@Column(name = "status")
	private int applyStatus;

	/**
	 * 用户状态
	 */
	private int userState;

	/**
	 * 推荐人
	 */
	private String referrer;

	/**
	 * 渠道
	 */
	private String channel;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 有效性 1:有效
	 */
	private int state = 1;

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
	 * 最后更进时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date followupTime;

	public long getInstallmentApplyId() {
		return installmentApplyId;
	}

	public void setInstallmentApplyId(long installmentApplyId) {
		this.installmentApplyId = installmentApplyId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public int getUserIdType() {
		return userIdType;
	}

	public void setUserIdType(int userIdType) {
		this.userIdType = userIdType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWxUnionid() {
		return wxUnionid;
	}

	public void setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(long brokerId) {
		this.brokerId = brokerId;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	public long getRent() {
		return rent;
	}

	public void setRent(long rent) {
		this.rent = rent;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPatternInt() {
		return patternInt;
	}

	public void setPatternInt(int patternInt) {
		this.patternInt = patternInt;
	}

	public int getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(int applyStatus) {
		this.applyStatus = applyStatus;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public Date getFollowupTime() {
		return followupTime;
	}

	public void setFollowupTime(Date followupTime) {
		this.followupTime = followupTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InstallmentApply [installmentApplyId=" + installmentApplyId + ", userId=" + userId + ", userName="
				+ userName + ", userIdNo=" + userIdNo + ", userIdType=" + userIdType + ", phone=" + phone
				+ ", wxUnionid=" + wxUnionid + ", brokerId=" + brokerId + ", payment=" + payment + ", rent=" + rent
				+ ", payDate=" + payDate + ", address=" + address + ", patternInt=" + patternInt + ", applyStatus="
				+ applyStatus + ", userState=" + userState + ", referrer=" + referrer + ", channel=" + channel
				+ ", city=" + city + ", state=" + state + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", followupTime=" + followupTime + "]";
	}

}
