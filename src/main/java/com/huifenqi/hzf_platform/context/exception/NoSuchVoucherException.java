package com.huifenqi.hzf_platform.context.exception;

/**
 * Created by arison on 2015/9/14.
 */
public class NoSuchVoucherException extends BaseException {

    public NoSuchVoucherException(long voucherId) {
        super(ErrorMsgCode.ERRCODE_NO_SUCH_VOUCHER, "代金券" + voucherId + "不存在");
    }
}
