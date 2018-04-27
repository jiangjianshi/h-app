package com.huifenqi.hzf_platform.context.exception;

/**
 * Created by arison on 2017/10/13.
 */
public class ServiceInvokeFailException extends BaseException {

    public ServiceInvokeFailException(Exception e) {
        super(e);
    }
    
    public ServiceInvokeFailException(int code, String msg) {
    	super(code, msg);
    }

    public ServiceInvokeFailException(String msg) {
        super(ErrorMsgCode.ERRCODE_SERVICE_ERROR, msg);
    }
}
