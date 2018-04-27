package com.huifenqi.hzf_platform.context.exception;


/**
 * Created by arison on 2015/9/14.
 */
public class NoSuchUserException extends BaseException {


    public NoSuchUserException(long userId) {
        super(ErrorMsgCode.ERRCODE_NO_SUCH_USER, "用户" + userId + "不存在!");
    }
}
