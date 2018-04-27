/** 
 * Project Name: fileservice_project 
 * File Name: BaseException.java 
 * Package Name: com.huifenqi.file.context.exception 
 * Date: 2016年1月5日下午8:43:56 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.exception;

/** 
 * ClassName: BaseException
 * date: 2017年4月14日 下午15:43:56
 * Description: 
 * 
 * @author arison
 * @version  
 * @since JDK 1.8 
 */
public class BdBaseException extends RuntimeException {

	private static final long serialVersionUID = -8962189197754826374L;

	private int code;
	private String msg;

	public BdBaseException() {

	}

	public BdBaseException(int errorcode, String msg) {
		super(msg);
		this.code = errorcode;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
