/** 
 * Project Name: mq_project 
 * File Name: LackParameterException.java
 * Package Name: com.huifenqi.mq.context.exception 
 * Date: 2016年2月2日下午2:25:38 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.exception;

/** 
 * ClassName: LackParameterException
 * date: 2016年2月2日 下午2:25:38
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class LackParameterException extends BaseException{
	

	private static final long serialVersionUID = -7305673475606021068L;

	public LackParameterException(String description) {
		super(ErrorMsgCode.ERROR_MSG_MISS_PARAMETERS, description);
	}
}
