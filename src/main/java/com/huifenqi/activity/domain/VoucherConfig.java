package com.huifenqi.activity.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arison on 2015/9/10.
 *
 * 代金券配置
 */
@Entity
public class VoucherConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 关联活动Id
	 */
	private long activityId;

	/**
	 * 代金券面额，单位：分
	 */
	private int price;

	/**
	 * 有效期，单位：天
	 */
	private int validity;

	/**
	 * 发放量
	 */
	private int paymentAmount;

	/**
	 * 使用量
	 */
	private int usageAmount;

	/**
	 * 分享状态，0：可分享；1：不可分享
	 */
	private int voucherShareState;

	/**
	 * 抽奖概率，万分位
	 */
	private int lotteryProb;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getValidity() {
		return validity;
	}

	public void setValidity(int validity) {
		this.validity = validity;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public int getUsageAmount() {
		return usageAmount;
	}

	public void setUsageAmount(int usageAmount) {
		this.usageAmount = usageAmount;
	}

	public int getVoucherShareState() {
		return voucherShareState;
	}

	public void setVoucherShareState(int voucherShareState) {
		this.voucherShareState = voucherShareState;
	}

	public int getLotteryProb() {
		return lotteryProb;
	}

	public void setLotteryProb(int lotteryProb) {
		this.lotteryProb = lotteryProb;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VoucherConfig [id=" + id + ", activityId=" + activityId + ", price=" + price + ", validity=" + validity
				+ ", paymentAmount=" + paymentAmount + ", usageAmount=" + usageAmount + ", voucherShareState="
				+ voucherShareState + ", lotteryProb=" + lotteryProb + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
