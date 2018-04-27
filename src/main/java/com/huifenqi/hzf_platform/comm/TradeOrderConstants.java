/** 
 * Project Name: hzf_smart
 * File Name: TradeOrderConstants.java
 * Package Name: com.huifenqi.hzf_platform.comm
 * Date: 2018年1月17日 下午12:14:04
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.comm;

/** 
 * ClassName: TradeOrderConstants
 * date: 2018年1月17日 下午12:14:04
 * Description: 
 * 
 * @author Arison
 * @version  
 * @since JDK 1.8 
 */
public class TradeOrderConstants {
	
	/**
	 * 交易订单锁前缀
	 */
	public static final String TRADE_ORDER_LOCK_PREFIX = "trade.order.lock";
	
	/**
	 * 电表订单队列
	 */
	public static final String TRADE_ORDER_QUEUE_METER = "trade.order.queue.meter";

	/**
	 * 电表订单结算队列
	 */
	public static final String TRADE_ORDER_SETTLE_QUEUE_METER = "trade.order.settle.queue.meter";

	/**
	 * 电表订单退款队列
	 */
	public static final String TRADE_ORDER_REFUND_QUEUE_METER = "trade.order.refund.queue.meter";


	//电表的相关配置
	public static final String PRODUCT_METER_NO = "10000001";

	public static final String PRODUCT_METER_ORDER_ID = "01";

	public static final String PRODUCT_METER_NAME = "电表充值";
	
	
	/**
	 * 获取产品名称
	 * @param productNo
	 * @return
	 */
	public static String getProductNameByProductNo(String productNo) {
		String productName = "";
		switch(productNo) {
		case PRODUCT_METER_NO:
			productName = PRODUCT_METER_NAME;
			break;
		}
		return productName;
	}
	
	/**
	 * 获取产品订单编号
	 * @param productNo
	 * @return
	 */
	public static String getProductOrderIdByProductNo(String productNo) {
		String productOrderId = "";
		switch(productNo) {
		case PRODUCT_METER_NO:
			productOrderId = PRODUCT_METER_ORDER_ID;
			break;
		}
		return productOrderId;
	}
	
	
	/**
	 * 获取校验订单的资源
	 * @param orderNo
	 * @return
	 */
	public static String getTradeOrderLockKey(String orderNo) {
		StringBuilder sb = new StringBuilder();
		sb.append(TRADE_ORDER_LOCK_PREFIX).append(".").append(orderNo);
		return sb.toString();
	}
	
	/**
	 * 支付状态
	 */
	public static class TradeOrderStatus {
		
		//等待支付
		public final static int TRADE_ORDER_STATUS_UNPAY = 1;
		
		//支付成功
		public final static int TRADE_ORDER_STATUS_PAY_SUCCESS = 2;
		
		//交易成功
		public final static int TRADE_ORDER_STATUS_TRANSCATION_SUCCESS = 3;
		
		//退款中
		public final static int TRADE_ORDER_STATUS_REFUNDING = 4;
		
		//退款成功
		public final static int TRADE_ORDER_STATUS_REFUNDED = 5;
		
		//退款失败
		public final static int TRADE_ORDER_STATUS_REFUND_FAILED = 6;
		
		//交易关闭
		public final static int TRADE_ORDER_STATUS_CHARGE_CLOSED = 7;
		
		//发起支付
		public final static int TRADE_ORDER_STATUS_START_PAY = 8;
		
		//支付失败
		public final static int TRADE_ORDER_STATUS_PAY_FAILED = 9;
		
		//提交发货
		public final static int TRADE_ORDER_STATUS_SEND_GOODS = 10;
	
		//发货发货成功
		public final static int TRADE_ORDER_STATUS_SEND_GOODS_SUCCESS = 11;
		
		//发货失败
		public final static int TRADE_ORDER_STATUS_SEND_GOODS_FAILED = 12;
		
		
		/**
		 * 获取用户显示状态
		 * @param orderStatus
		 * @return
		 */
		public static int getUserViewStatus(int orderStatus) {
			switch(orderStatus) {
			case TRADE_ORDER_STATUS_PAY_SUCCESS:
			//case TRADE_ORDER_STATUS_SEND_GOODS_FAILED:
				return TRADE_ORDER_STATUS_PAY_SUCCESS; //商家发货  --> 2 支付成功
			
			case TRADE_ORDER_STATUS_SEND_GOODS_SUCCESS:
			case TRADE_ORDER_STATUS_TRANSCATION_SUCCESS: //交易成功 --> 发货成功
			//case TRADE_ORDER_STATUS_TRANSCATION_SUCCESS: //bug fix
				return TRADE_ORDER_STATUS_SEND_GOODS_SUCCESS;// 3. 充值成功  -->
			
			/*case TRADE_ORDER_STATUS_REFUNDED: //交易关闭
				return TRADE_ORDER_STATUS_CHARGE_CLOSED;
			*/
			//case TRADE_ORDER_STATUS_UNPAY:
			case TRADE_ORDER_STATUS_PAY_FAILED:
			case TRADE_ORDER_STATUS_CHARGE_CLOSED:
					return TRADE_ORDER_STATUS_CHARGE_CLOSED; // 7 交易关闭
			default:
				return orderStatus;
			}
		}
		
	}
}
