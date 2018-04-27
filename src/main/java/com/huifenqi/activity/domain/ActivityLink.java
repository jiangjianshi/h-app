package com.huifenqi.activity.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by YDM on 2017/10/12.
 *
 * 优惠券链接维护表
 */
@Entity
@Table(name="t_activity_link")
public class ActivityLink {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;

	/**
	 * 领取原链接
	 */
	@Column(name="f_coupon_link")
	private String couponLink;
	
	/**
	 * 领取短链接
	 */
	@Column(name="f_index_link")
	private String indexLink;

	/**
	 * 优惠券名称
	 */
	@Column(name="f_coupon_name")
	private String couponName;

	/**
	 * 优惠券类型（4：善之泉净水器1682元抵用券）
	 */
	@Column(name="f_coupon_type")
	private int couponType;

	/**
	 * 使用说明
	 */
	@Column(name="f_coupon_desc")
	private String couponDesc;

	/**
	 * 开始时间
	 */
	@Column(name="f_start_date")
	private Date startDate;
	
	/**
	 * 过期时间
	 */
	@Column(name="f_expired_date")
	private Date expiredDate;
	
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

	public String getCouponLink() {
		return couponLink;
	}

	public void setCouponLink(String couponLink) {
		this.couponLink = couponLink;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getIndexLink() {
		return indexLink;
	}

	public void setIndexLink(String indexLink) {
		this.indexLink = indexLink;
	}

	@Override
	public String toString() {
		return "ActivityLink [id=" + id + ", couponLink=" + couponLink
				+ ", indexLink=" + indexLink + ", couponName=" + couponName
				+ ", couponType=" + couponType + ", couponDesc=" + couponDesc
				+ ", startDate=" + startDate + ", expiredDate=" + expiredDate
				+ ", state=" + state + "]";
	}

}
