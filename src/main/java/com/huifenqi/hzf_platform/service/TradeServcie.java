/** 
 * Project Name: hzfr_smart
 * File Name: TradeServcie.java
 * Package Name: com.huifenqi.hzf_platform.service
 * Date: 2017年3月13日下午4:06:14 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.service;

import com.google.gson.Gson;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.comm.TradeOrderConstants;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.usercomm.domain.TradeOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/** 
 * ClassName: TraderServcie
 * date: 2018年1月16日 下午4:06:14
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Service
public class TradeServcie {
	
	private static final Log logger = LogFactory.getLog(TradeServcie.class);
	
	@Autowired
	private RedisClient redisClient;
	
	private Gson gson = GsonUtils.buildGson();
	
	/**
	 * 处理交易
	 * @param tradeOrder
	 */
	public void processTrade(TradeOrder tradeOrder) {
		if (TradeOrderConstants.PRODUCT_METER_NO.equals(tradeOrder.getProductTypeNo())) {
			processChargeMeter(tradeOrder);
		}
	}
	
	/**
	 * 处理电表交易
	 * @param tradeOrder
	 */
	private void processChargeMeter(TradeOrder tradeOrder) {
		//add by arison 20170509
		tradeOrder.setCallBackUrl("/order/api/prepayasyncallback/");
		String orderJo = gson.toJson(tradeOrder);
		logger.info(String.format("将订单%s添加到队列:%s", tradeOrder.getOrderNo(), orderJo));

		redisClient.lefltPushQueue(TradeOrderConstants.TRADE_ORDER_QUEUE_METER, orderJo);
	}
}
