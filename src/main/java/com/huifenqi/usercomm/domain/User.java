package com.huifenqi.usercomm.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/4.
 */
@Entity
public class User {

	/**
	 * 用户类型: 普通用户
	 */
	public static final int USER_TYPE_NORMAL = 1;

	/**
	 * 用户类型: 收房业务用户
	 */
	public static final int USER_TYPE_ROOM_COLLECT = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userid;

	/**
	 * 有效性
	 */
	private int state;

	/**
	 * 姓名(该属性未使用)
	 */
	private String name = "";

	/**
	 * 实际使用姓名
	 */
	private String lastName;

	/**
	 * 用户类型 1：普通用户, 2：收房业务用户, 默认值：1
	 */
	private int userType = USER_TYPE_NORMAL;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 备用手机号
	 */
	private String phone2 = "";

	/**
	 * 用户身份证号
	 */
	private String userIdNo = "";

	/**
	 * 家庭住址
	 */
	private String homeAddress;

	/**
	 * 工作地址
	 */
	private String workAddress;

	/**
	 * 性别：0：不确定；1：男；2：女
	 */
	private String sex;

	/**
	 * 用户注册的渠道：android、ios、wx、pc-web、mobile-web
	 */
	@Column(name = "startsource")
	private String startSource;

	/**
	 * 微信unionid
	 */
	private String wxUnionid = "";

	/**
	 * 微信微信openid
	 */
	private String wxOpenid = "";

	/**
	 * 开户银行
	 */
	private String bank = "";

	/**
	 * 银行账户
	 */
	private String bankAccount = "";

	/**
	 * 银行卡号
	 */
	private String bankCard = "";

	/**
	 * 中介公司id
	 */
	private String agencyId = "0";

	/**
	 * 客户来源(如58,赶集)
	 */
	private String comefrom = "";

	/**
	 * 经纪人id
	 */
	private String brokerId = "0";

	/**
	 * 最后登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastlogin")
	private Date lastLogin;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();

	/**
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime = new Date();

	/**
	 * 跟进时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date followUpTime = new Date();

	/**
	 * 注册时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime = new Date();

	/**
	 * 期望入住时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkInTime = new Date();

	/**
	 * 客户端cpu类型
	 */
	private String cpu;
	
	/**
	 * 微信切换次数
	 */
	private int wxChangeTimes;

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public void setUserIdNo(String userIdNo) {
		this.userIdNo = userIdNo;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStartSource() {
		return startSource;
	}

	public void setStartSource(String startSource) {
		this.startSource = startSource;
	}

	public String getWxUnionid() {
		return wxUnionid;
	}

	public void setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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

	public Date getFollowUpTime() {
		return followUpTime;
	}

	public void setFollowUpTime(Date followUpTime) {
		this.followUpTime = followUpTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public int getWxChangeTimes() {
		return wxChangeTimes;
	}

	public void setWxChangeTimes(int wxChangeTimes) {
		this.wxChangeTimes = wxChangeTimes;
	}

	@Override
	public String toString() {
		return String.format(
				"User [userid=%s, state=%s, name=%s, lastName=%s, userType=%s, pwd=%s, phone=%s, phone2=%s, userIdNo=%s, homeAddress=%s, workAddress=%s, sex=%s, startSource=%s, wxUnionid=%s, wxOpenid=%s, bank=%s, bankAccount=%s, bankCard=%s, agencyId=%s, comefrom=%s, brokerId=%s, lastLogin=%s, createTime=%s, updateTime=%s, followUpTime=%s, registerTime=%s, checkInTime=%s, cpu=%s, wxChangeTimes=%s]",
				userid, state, name, lastName, userType, pwd, phone, phone2, userIdNo, homeAddress, workAddress, sex,
				startSource, wxUnionid, wxOpenid, bank, bankAccount, bankCard, agencyId, comefrom, brokerId, lastLogin,
				createTime, updateTime, followUpTime, registerTime, checkInTime, cpu, wxChangeTimes);
	}
}
