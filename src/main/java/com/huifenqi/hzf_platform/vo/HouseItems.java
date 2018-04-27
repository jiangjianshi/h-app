package com.huifenqi.hzf_platform.vo;

public class HouseItems {
    private String name;
    private String info;
    private int payType;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "HouseItems{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
