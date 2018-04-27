package com.huifenqi.hzf_platform.vo;

/**
 * Created by HFQ-Arison on 2017/9/14.
 */
public class PayChannelVo {
    private String name;
    private String type;
    private String bankCode;
    private String bankName;
    private String cardTop;
    private String cardLast;
    private String desc;
    private String phone;
    private String realName;
    private String bankToken;
    private String idNo;
    private String scheme;

    public PayChannelVo(){

    }
    public PayChannelVo(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardTop() {
        return cardTop;
    }

    public void setCardTop(String cardTop) {
        this.cardTop = cardTop;
    }

    public String getCardLast() {
        return cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String backName) {
        this.bankName = backName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankToken() {
        return bankToken;
    }

    public void setBankToken(String bankToken) {
        this.bankToken = bankToken;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "PayChannelVo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardTop='" + cardTop + '\'' +
                ", cardLast='" + cardLast + '\'' +
                ", desc='" + desc + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", bankToken='" + bankToken + '\'' +
                ", idNo='" + idNo + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}
