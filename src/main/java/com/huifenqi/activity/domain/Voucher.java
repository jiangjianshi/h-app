package com.huifenqi.activity.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/5.
 *
 * 代金券
 */
@Entity
public class Voucher {

	/**
	 * 代金券状态 - 未使用
	 */
	public static int USE_STATE_NOTUSED = 0;

	/**
	 * 代金券状态 - 已使用
	 */
	public static int USE_STATE_USED = 1;

	/**
	 * 代金券状态 - 已过期
	 */
	public static int USE_STATE_EXPIRED = 2;

	/**
	 * 代金券状态 - 未领取
	 */
	public static int USE_STATE_NOTCLAIM = 3;

	/**
	 * 分享状态 - 可分享
	 */
	public static int SHARE_STATE_CANSHARE = 0;

	/**
	 * 分享状态 - 不可分享
	 */
	public static int SHARE_STATE_CANTSHARE = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 代金券活动id
	 */
	private long voucherActivityId;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * 代金券配置id
	 */
	private long voucherConfId;

	/**
	 * 代金券有效时长，单位：天
	 */
	private int validity;

	/**
	 * 代金券面额，单位：分
	 */
	private int price;

	/**
	 * 分享状态，0：可分享；1：不可分享
	 */
	private int voucherShareState;

	/**
	 * 分享出去的时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date shareTime;

	/**
	 * 代金券使用状态， 0：未使用 1：已使用 2：已过期 3：未领取
	 */
	private int voucherUseState;

	/**
	 * 代金券被使用的时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date useTime;

	/**
	 * 代金券分享的时候所生成的临时id，为避免被破解，通常是加密的
	 */
	@Column(name = "shareid")
	private String shareId;

	/**
	 * 关联支付流水id
	 */
	private int chargeId;

	/**
	 * 关联订单id
	 */
	private long payId;

	/**
	 * 关联合同快照表id
	 */
	private long contractSnapshotId;

	/**
	 * 代金券分享来源用户id，即用户的代金券是别的用户分享而来，这个字段记录了代金券传递的路径
	 */
	private int srcUserId;

	/**
	 * 代金券分享来源代金券id，例如，自己的代金券是通过点击别人分享的代金券生成的，则这个字段记录了点击的代金券的id
	 */
	private int srcVoucherId;

	/**
	 * 过期时间
	 */
	@Temporal(TemporalType.DATE)
	private Date expireDate;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	/**
	 * 代金劵剩余天数
	 */
	@Transient
	private long defferDayNum;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVoucherActivityId() {
		return voucherActivityId;
	}

	public void setVoucherActivityId(long voucherActivityId) {
		this.voucherActivityId = voucherActivityId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getVoucherConfId() {
		return voucherConfId;
	}

	public void setVoucherConfId(long voucherConfId) {
		this.voucherConfId = voucherConfId;
	}

	public int getValidity() {
		return validity;
	}

	public void setValidity(int validity) {
		this.validity = validity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getVoucherShareState() {
		return voucherShareState;
	}

	public void setVoucherShareState(int voucherShareState) {
		this.voucherShareState = voucherShareState;
	}

	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

	public int getVoucherUseState() {
		return voucherUseState;
	}

	public void setVoucherUseState(int voucherUseState) {
		this.voucherUseState = voucherUseState;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareid) {
		this.shareId = shareid;
	}

	public int getChargeId() {
		return chargeId;
	}

	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}

	public long getPayId() {
		return payId;
	}

	public void setPayId(long payId) {
		this.payId = payId;
	}

	public long getContractSnapshotId() {
		return contractSnapshotId;
	}

	public void setContractSnapshotId(long contractSnapshotId) {
		this.contractSnapshotId = contractSnapshotId;
	}

	public int getSrcUserId() {
		return srcUserId;
	}

	public void setSrcUserId(int srcUserId) {
		this.srcUserId = srcUserId;
	}

	public int getSrcVoucherId() {
		return srcVoucherId;
	}

	public void setSrcVoucherId(int srcVoucherId) {
		this.srcVoucherId = srcVoucherId;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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

	public long getDefferDayNum() {
		return defferDayNum;
	}

	public void setDefferDayNum(long defferDayNum) {
		this.defferDayNum = defferDayNum;
	}

	@Override
	public String toString() {
		return "Voucher [id=" + id + ", voucherActivityId=" + voucherActivityId + ", userId=" + userId
				+ ", voucherConfId=" + voucherConfId + ", validity=" + validity + ", price=" + price
				+ ", voucherShareState=" + voucherShareState + ", shareTime=" + shareTime + ", voucherUseState="
				+ voucherUseState + ", useTime=" + useTime + ", shareId=" + shareId + ", chargeId=" + chargeId
				+ ", payId=" + payId + ", contractSnapshotId=" + contractSnapshotId + ", srcUserId=" + srcUserId
				+ ", srcVoucherId=" + srcVoucherId + ", expireDate=" + expireDate + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", defferDayNum=" + defferDayNum + "]";
	}

}
