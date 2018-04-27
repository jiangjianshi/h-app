/** 
* Project Name: payment2.0_project_trunk 
* File Name: BailFlow.java 
* Package Name: com.huifenqi.payment.dao.domain 
* Date: 2016年6月27日下午3:01:53 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.usercomm.charge.domain;

import javax.persistence.*;
import java.util.Date;

/** 
* ClassName: BailFlow
* date: 2016年6月27日 下午3:01:53
* Description: 
* 
* @author Ernest 
* @version  
* @since JDK 1.8 
*/
@Entity
@Table(name = "t_bail_flow")
public class BailFlow {
	
    /**
     * 支付状态 - 未支付
     */
    public static final int STATUS_UNPAY = 0;

    /**
     * 支付状态 - 部分支付
     */
    public static final int STATUS_PART_PAID = 1;

    /**
     * 支付状态 - 支付完成
     */
    public static final int STATUS_PAID = 2;

    /**
     * 支付状态 - 已发起支付请求
     */
    public static final int STATUS_PAY_REQUESTED = 3;

    /**
     * 支付状态 - 支付失败
     */
    public static final int STATUS_PAY_FAIL = 4;

    /**
     * 支付状态 - 支付撤销
     */
    public static final int STATUS_PAY_CANCEL = 5;

    /**
     * 支付状态 - 支付退款
     */
    public static final int STATUS_REFUND = 6;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "f_flow_id")
	public Long flowId;
	
	/**
	 * 订单号
	 */
	@Column(name = "f_order_no")
	public String orderNo = "";
	
	
	/**
	 * 保证金ID
	 */
	@Column(name = "f_deposit_id")
	public Long depositId = -1L;
	
	/**
	 * 订单名称
	 */
	@Column(name = "f_order_title")
	public String orderTitle = "";
	
	/**
	 * 订单描述
	 */
	@Column(name = "f_order_desc")
	public String orderDesc = "";
	
	/**
	 * 应付金额
	 */
	@Column(name = "f_pay_price")
	public Long payPrice;
	
	/**
	 * 实付金额
	 */
	@Column(name = "f_real_price")
	public Long realPrice;
	
	/**
	 * 渠道费用
	 */
	@Column(name = "f_channel_price")
	public Long channelPrice = 0L;
	
	/**
	 * 支付状态
	 */
	@Column(name = "f_pay_status")
	public Integer status = STATUS_UNPAY;
	
	/**
	 * 支付描述
	 */
	@Column(name = "f_status_desc")
	public String statusDesc = "";
	
	/**
	 * 平台类型：1:IOS,2:ANDROID,3:WX
	 */
	@Column(name = "f_platform_type")
	public Integer platformType;
	
	/**
	 * 支付时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_pay_time")
	public Date payTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_create_time")
	public Date createTime = new Date();
	
	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_update_time")
	public Date updateTime = new Date();
	
	/**
	 * 有效状态
	 */
	@Column(name = "f_state")
	public Integer state = 1;
	
	/**
	 * 支付渠道
	 */
	@Column(name = "f_channel")
	public String channel;
	
	public static final class Builder {
		private BailFlow bailFlow;
		
		public Builder() {
			bailFlow = new BailFlow();
		}
		
		public Builder setOrderNo(String orderNo) {
			bailFlow.orderNo = orderNo;
			return this;
		}
		
		public Builder setDepositId(Long depositId) {
			bailFlow.depositId = depositId;
			return this;
		}
		
		public Builder setOrderTitle(String orderTitle) {
			bailFlow.orderTitle = orderTitle;
			return this;
		}
		
		public Builder setOrderDesc(String orderDesc) {
			bailFlow.orderDesc = orderDesc;
			return this;
		}
		
		public Builder setPayPrice(Long payPrice) {
			bailFlow.payPrice = payPrice;
			return this;
		}
		
		public Builder setRealPrice(Long realPrice) {
			bailFlow.realPrice = realPrice;
			return this;
		}
		
		public Builder setChannelPrice(Long channelPrice) {
			bailFlow.channelPrice = channelPrice;
			return this;
		}
		
		public Builder setPayStatus(Integer status) {
			bailFlow.status = status;
			return this;
		}
		
		public Builder setStatusDesc(String statusDesc) {
			bailFlow.statusDesc = statusDesc;
			return this;
		}
		
		public Builder setPlatformType(Integer platformType) {
			bailFlow.platformType = platformType;
			return this;
		}
		
		public Builder setChannel(String channel) {
			bailFlow.channel = channel;
			return this;
		}
	}
	
}
