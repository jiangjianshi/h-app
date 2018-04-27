package com.huifenqi.usercomm.charge.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Charge {
	 /**
     * 交易类型 - 非合同类型交易（默认）
     */
    public static final int TRADE_TYPE_NORMAL = 0;

    /**
     * 交易类型 - 认证
     */
    public static final int TRADE_TYPE_CERT = 1;

    /**
     * 支付状态 - 未支付
     */
    public static final int STATUS_UNPAY = 0;

    /**
     * 支付状态 - 部分支付
     */
    public static final int STATUS_PART_PAID = 1;

    /**
     * 支付状态 - 支付完成
     */
    public static final int STATUS_PAID = 2;

    /**
     * 支付状态 - 已发起支付请求
     */
    public static final int STATUS_PAY_REQUESTED = 3;

    /**
     * 支付状态 - 支付失败
     */
    public static final int STATUS_PAY_FAIL = 4;

    /**
     * 支付状态 - 支付撤销
     */
    public static final int STATUS_PAY_CANCEL = 5;

    /**
     * 支付状态 - 支付退款
     */
    public static final int STATUS_REFUND = 6;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long chargeid;

    public long subpayId = -1;

    /**
     * 支付渠道，取值参考：{@link com.huifenqi.payment.comm.Constants.PayChannel}
     */
    public String channel;

    /**
     * 商品标题，对应ping++中的subject
     */
    public String subject;

    /**
     * 商品描述，对应ping++中的body
     */
    @Column(name = "`desc`")
    public String desc = "";

    public String orderNo = "";

    /**
     * 支付状态
     */
    public Integer status = STATUS_UNPAY;

    /**
     * 支付状态描述
     */
    public String statusDesc;

    public Long userId;

    /**
     * 用户应还本金：房租+滞纳金+会分期服务费，相当于Subpay.unpay_price
     */
    public Long price;

    /**
     * 会分期实际收取的费用：price - VoucherPrice
     */
    public Long payPrice;

    /**
     * 代金券金额
     */
    public Long voucherPrice = 0L;

    /**
     * 手续费：用于抵扣支付渠道收取的费用
     */
    public Long paymentChannelIncome = 0L;

    public String pingppChargeid = "";

    public String platformOrderId = "";

    public Long voucherId;

    /**
     * 交易类型
     */
    public Integer tradeType = TRADE_TYPE_NORMAL;

    public Integer state = 1;
    
    /**
     * 支付平台：1、android，2、ios，3、wxpub
     */
    public Integer payPlatform;


    @Temporal(TemporalType.TIMESTAMP)
    public Date payTime;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime = new Date();

    @Version
    public Date updateTime = new Date();

    public String currency = "cny";

    public Integer checked = 0;    
}
