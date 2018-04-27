package com.huifenqi.usercomm.domain.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by tengguodong on 2016/8/25.
 */
@Entity
@Table(name = "t_agency_conf")
public class AgencyConf {
    /**
     * 中介公司id
     */
    @Id
    @Column(name = "f_agency_id")
    int agencyid;

       /**
     * 是否支持分期
     */
    @Column(name = "f_devide_periods")
    Integer isSupportHfq;

    /**
     * 是否支持转租
     */
    @Column(name = "f_sublet")
    Integer sublet;

    public int getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(int agencyid) {
        this.agencyid = agencyid;
    }

    public Integer getIsSupportHfq() {
        return isSupportHfq;
    }

    public void setIsSupportHfq(Integer isSupportHfq) {
        this.isSupportHfq = isSupportHfq;
    }

    public Integer getSublet() {
        return sublet;
    }

    public void setSublet(Integer sublet) {
        this.sublet = sublet;
    }

    @Override
    public String toString() {
        return "AgencyConf{" +
                "agencyid=" + agencyid +
                ", isSupportHfq=" + isSupportHfq +
                ", sublet=" + sublet +
                '}';
    }
}
