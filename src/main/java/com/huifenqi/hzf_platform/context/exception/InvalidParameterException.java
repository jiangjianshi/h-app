package com.huifenqi.hzf_platform.context.exception;

/**
 * 
 * ClassName: InvalidParameterException date: 2016年4月8日 下午12:36:19
 * Description:非法参数异常
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class InvalidParameterException extends BaseException {

	private static final long serialVersionUID = -3883814863434936445L;

	public InvalidParameterException(String msg) {
		super(ErrorMsgCode.ERROR_MSG_INVALID_PARAMETER, msg);
	}
}
