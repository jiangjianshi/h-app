package com.huifenqi.usercomm.domain.contract;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by HFQ-Arison on 2017/5/11.
 */
@Entity
@Table(name = "agency_refund")
public class AgencyRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refund_id")
    private Long refundId;
    @Column(name = "contract_no")
    private String contractNo;
    @Column(name = "state")
    private Long state;
    @Column(name = "refund_status")
    private int refundStatus;

    @Column(name = "launch_breach_time")
    private Date launchTime;

    @Column(name = "breach_initiator")
    private int breachInitiator;


    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "refund_desc")
    private String refundDesc;

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public int getBreachInitiator() {
        return breachInitiator;
    }

    public void setBreachInitiator(int breachInitiator) {
        this.breachInitiator = breachInitiator;
    }

    @Override
    public String toString() {
        return "AgencyRefund{" +
                "refundId=" + refundId +
                ", contractNo='" + contractNo + '\'' +
                ", state=" + state +
                ", refundStatus=" + refundStatus +
                ", createTime=" + createTime +
                ", refundDesc='" + refundDesc + '\'' +
                '}';
    }
}

