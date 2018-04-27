/** 
 * Project Name: usercomm_smart 
 * File Name: TradeOrderException.java 
 * Package Name: com.huifenqi.usercomm.exceptions 
 * Date: 2017年3月11日下午4:53:42 
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.exception;

/** 
 * ClassName: TradeOrderException
 * date: 2017年3月11日 下午4:53:42
 * Description: 
 * 
 * @author Arison
 * @version  
 * @since JDK 1.8 
 */
public class TradeOrderException extends BaseException {

	 private static final long serialVersionUID = -6649026945757515123L;

	 public TradeOrderException(Exception e) {
		super(e);
	}
	
	 public TradeOrderException(Integer code, String msg) {
		 super(code, msg);
	 }
	 
	 public TradeOrderException(String msg) {
		 super(ErrorMsgCode.ERRCODE_TRADE_ORDER_ERROR, msg);
	 }

}
