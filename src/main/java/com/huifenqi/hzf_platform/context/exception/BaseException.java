/** 
 * Project Name: fileservice_project 
 * File Name: BaseException.java 
 * Package Name: com.huifenqi.file.context.exception 
 * Date: 2016年1月5日下午8:43:56 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.exception;

import org.slf4j.LoggerFactory;

import com.huifenqi.hzf_platform.utils.LogUtil;

/** 
 * ClassName: BaseException
 * date: 2016年1月5日 下午8:43:56
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = -8964389197754846174L;

	private int errorcode;
	
	private String description;
	
	public BaseException() {
		
	}
	
	public BaseException(int errorcode, String description) {
		super(description);
		this.errorcode = errorcode;
		this.description = description;
	}
	
	public BaseException(Exception e) {
        super(String.format("code=%s, msg=%s", ErrorMsgCode.ERRCODE_INTERNAL_ERROR, e.getMessage()));
        this.errorcode = ErrorMsgCode.ERRCODE_INTERNAL_ERROR;
        this.description = e.getMessage();

        LoggerFactory.getLogger(BaseException.class).error(LogUtil.formatLog(getMessage()));
    }
	
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
