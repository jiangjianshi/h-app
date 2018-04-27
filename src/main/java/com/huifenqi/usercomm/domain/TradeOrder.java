/** 
 * Project Name: usercomm_smart 
 * File Name: TradeOrder.java 
 * Package Name: com.huifenqi.usercomm.domain.huizhaofang 
 * Date: 2017年3月2日下午5:08:10 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.usercomm.domain;


import javax.persistence.*;
import java.util.Date;

/** 
 * ClassName: TradeOrder
 * date: 2017年3月2日 下午5:08:10
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Table(name = "t_trade_order")
@Entity
public class TradeOrder {

	@Column(name = "f_id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "f_user_id")
	private long userId;

	@Column(name = "f_order_no")
	private String orderNo;
	
	@Column(name = "f_product_type_no")
	private String productTypeNo;
	
	@Column(name = "f_product_no")
	private String productNo;
	
	@Column(name = "f_product_hfq_id")
	private long productHfqId;
	
	@Column(name = "f_product_name")
	private String productName;
	
	@Column(name = "f_product_desc")
	private String productDesc;

	@Column(name = "f_business_no")
	private String businessNo;

	@Column(name = "f_pay_channel")
	private String payChannel;

	@Column(name = "f_unit_price")
	private int unitPrice;

	@Column(name = "f_quantity")
	private int quantity;

	@Column(name = "f_pay_amount")
	private int payAmount;
	
	@Column(name = "f_order_status")
	private int orderStatus;
	
	@Column(name = "f_order_desc")
	private String orderDesc;

	@Column(name = "f_pay_no")
	private	String payNo;
	
	@Column(name = "f_refund_no")
	private String refundNo;
	
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "f_refund_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date refundTime;
	
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	//add by arison
	@Transient
	private String callBackUrl;


	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getProductTypeNo() {
		return productTypeNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public long getProductHfqId() {
		return productHfqId;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public String getPayNo() {
		return payNo;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setProductTypeNo(String productTypeNo) {
		this.productTypeNo = productTypeNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public void setProductHfqId(long productHfqId) {
		this.productHfqId = productHfqId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	//add by arison
	@Transient
	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	@Override
	public String toString() {
		return String.format(
				"TradeOrder [id=%s, userId=%s, orderNo=%s, productTypeNo=%s, productNo=%s, productHfqId=%s, productName=%s, productDesc=%s, businessNo=%s, payChannel=%s, unitPrice=%s, quantity=%s, payAmount=%s, orderStatus=%s, orderDesc=%s, payNo=%s, refundNo=%s, createTime=%s, refundTime=%s, updateTime=%s]",
				id, userId, orderNo, productTypeNo, productNo, productHfqId, productName, productDesc, businessNo,
				payChannel, unitPrice, quantity, payAmount, orderStatus, orderDesc, payNo, refundNo, createTime,
				refundTime, updateTime);
	}
}
