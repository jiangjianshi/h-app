package com.huifenqi.activity.domain;

import javax.persistence.*;

import java.util.Date;

/**
 * Created by arison on 2015/9/5.
 *
 * 优惠券
 */
@Entity

@Table(name="t_coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="f_id")
	private long id;

	/**
	 * 用户id
	 */
	@Column(name="f_user_id")
	private long userId;

	/**
	 * 用户手机号
	 */
	@Column(name="f_phone")
	private String phone;
	
	/**
	 * 优惠券标题
	 */
	@Column(name="f_title")
	private String title;
	
	/**
	 * 优惠券类型
	 */
	@Column(name="f_coupon_type")
	private int couponType;

	/**
	 * 优惠券规则说明
	 */
	@Column(name="f_desc")
	private String desc;

	/**
	 * 优惠券券码
	 */
	@Column(name="f_code")
	private String code;
	
	/**
	 * 优惠券密码
	 */
	@Column(name="f_password")
	private String password;

	/**
	 * 优惠券规则说明细则
	 */
	@Column(name="f_ext_desc")
	private String extDesc;

	/**
	 * 优惠券备注
	 */
	@Column(name="f_comment")
	private String comment;
	
	/**
	 * 优惠券原链接
	 */
	@Column(name="f_coupon_link")
	private String couponLink;
	
	/**
	 * 优惠券短链接
	 */
	@Column(name="f_index_link")
	private String indexLink;

	/**
	 * 是否要过期
	 */
	@Transient
	private int goingExpire=0;

	/**
	 * 开始时间
	 */
	@Column(name="f_start_date")
	private Date startDate;
	
	/**
	 * 过期时间
	 */
	@Column(name="f_expire_date")
	private Date expiredDate;

	/**
	 * 创建时间
	 */
	@Column(name="f_create_time")
	private Date createTime = new Date();

	/**
	 * 更新时间
	 */
	@Column(name="f_update_time")
	private Date updateTime = new Date();
	
	/**
	 * 有效标识（1：默认有效；0：失效）
	 */
	@Column(name="f_state")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExtDesc() {
		return extDesc;
	}

	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public int getGoingExpire() {
		return goingExpire;
	}

	public void setGoingExpire(int goingExpire) {
		this.goingExpire = goingExpire;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCouponLink() {
		return couponLink;
	}

	public void setCouponLink(String couponLink) {
		this.couponLink = couponLink;
	}

	public String getIndexLink() {
		return indexLink;
	}

	public void setIndexLink(String indexLink) {
		this.indexLink = indexLink;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", userId=" + userId +", phone=" + phone + ", title=" + title
				+ ", couponType=" + couponType + ", desc=" + desc + ", code="
				+ code + ", password=" + password + ", extDesc=" + extDesc
				+ ", comment=" + comment + ", couponLink=" + couponLink
				+ ", indexLink=" + indexLink + ", goingExpire=" + goingExpire
				+ ", startDate=" + startDate + ", expiredDate=" + expiredDate
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", state=" + state + "]";
	}

	
}
