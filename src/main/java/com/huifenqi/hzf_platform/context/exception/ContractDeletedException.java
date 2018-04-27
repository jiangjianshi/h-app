/** 
 * Project Name: usercomm_project 
 * File Name: ContractDeletedException.java 
 * Package Name: com.huifenqi.usercomm.exceptions 
 * Date: 2016年5月18日下午3:21:05 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.context.exception;

/**
 * ClassName: ContractDeletedException
 * date: 2016年5月18日 下午3:21:05
 * Description: 合同已被删除异常
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class ContractDeletedException extends BaseException {
	
	public ContractDeletedException(Exception e) {
        super(e);
    }
    
    public ContractDeletedException(int code, String msg) {
    	super(code, msg);
    }

    public ContractDeletedException(String msg) {
        super(ErrorMsgCode.ERRCODE_SERVICE_ERROR, msg);
    }
}
