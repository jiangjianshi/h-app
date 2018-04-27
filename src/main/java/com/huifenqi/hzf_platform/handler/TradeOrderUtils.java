/** 
 * Project Name: hzf_smart
 * File Name: TradeOrderUtils.java 
 * Package Name: com.huifenqi.hzf_platform.handler
 * Date: 2017年3月4日下午12:10:15 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.handler;

import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/** 
 * ClassName: TradeOrderUtils
 * date: 2018年1月16日 下午12:10:15
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class TradeOrderUtils {
	
	@Autowired
	private Configuration configuration;

	@Autowired
	private RedisClient redisClient;
	
	private Object syncObjct = new Object();
	
	private static final String TRADE_ORDER_KEY = "trade.order.index";
	
	private static final int KEY_TIME_OUT = 1000;

	/**
	 * 获取交易订单号
	 * @param productId
	 * @return
	 */
	public String getTradeOrderNo(String productId) {
		
		int orderId = 0;
		
		synchronized (syncObjct) {
			Object tempId = redisClient.get(TRADE_ORDER_KEY);
			if (tempId == null) {
				redisClient.set(TRADE_ORDER_KEY, 0, KEY_TIME_OUT);
			} else {
				//update by arison
				orderId = Integer.parseInt(tempId.toString());
				redisClient.set(TRADE_ORDER_KEY, ++orderId);
			}
		}
		
		String dateTime = DateUtil.format("yyyyMMddHHmmss",new Date());
		
		StringBuilder sb = new StringBuilder();
		sb.append(orderId);
		while (sb.length() < 4) {
			sb.insert(0, 0);
		}
		
		String value = String.format("%s%s%s%s",configuration.orderSystemId, productId, dateTime, sb.toString());
		
		return value;
	}
}
