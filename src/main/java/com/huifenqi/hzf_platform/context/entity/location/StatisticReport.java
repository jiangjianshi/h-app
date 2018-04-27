package com.huifenqi.hzf_platform.context.entity.location;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "f_statistic_report")
public class StatisticReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="f_id")
    private long Id;

    public StatisticReport()
    {

    }
    public StatisticReport(long ftol,long fpv,long fss,long fbdj,long fjg,long fppgy,long fhz,long fzz,long ffq,String strTime){
        this.ftol=ftol;
        this.fpv=fpv;
        this.fss=fss;
        this.fbdj=fbdj;
        this.fjg=fjg;
        this.fppgy=fppgy;
        this.fhz=fhz;
        this.fzz=fzz;
        this.ffq=ffq;
        this.strTime=strTime;
    }
    /**
     * 所有页面访问数量
     */
    @Column(name="f_ftol")
    private long ftol;

    /**
     * 页面访问数量
     */
    @Column(name="f_fpv")
    private long fpv;


    /**
     * 搜索框点击量
     */
    @Column(name="f_fss")
    private long fss;

    /**
     * banner点击量
     */
    @Column(name="f_fbdj")
    private long fbdj;

    /**
     * 金刚点击量
     */
    @Column(name="f_fjg")
    private long fjg;

    /**
     * 品牌公寓点击量
     */
    @Column(name="f_fppgy")
    private long fppgy;

    /**
     * 合租点击量
     */
    @Column(name="f_fhz")
    private long fhz;


    /**
     * 整租点击量
     */
    @Column(name="f_fzz")
    private long fzz;

    /**
     * 分期点击量
     */
    @Column(name="f_ffq")
    private long ffq;

    /**
     * 日期字符串
     */
    @Column(name="f_time")
    private String strTime;

    /**
     * 城市id
     */
    @Column(name="f_city_id")
    private long cityId;


    @Column(name="f_state")
    private long state;

    /**
     * 创建时间
     */
    @Column(name="f_create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name="f_update_time")
    private Date updateTime;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getFpv() {
        return fpv;
    }

    public void setFpv(long fpv) {
        this.fpv = fpv;
    }

    public long getFss() {
        return fss;
    }

    public void setFss(long fss) {
        this.fss = fss;
    }

    public long getFbdj() {
        return fbdj;
    }

    public void setFbdj(long fbdj) {
        this.fbdj = fbdj;
    }

    public long getFjg() {
        return fjg;
    }

    public void setFjg(long fjg) {
        this.fjg = fjg;
    }

    public long getFppgy() {
        return fppgy;
    }

    public void setFppgy(long fppgy) {
        this.fppgy = fppgy;
    }

    public long getFhz() {
        return fhz;
    }

    public void setFhz(long fhz) {
        this.fhz = fhz;
    }

    public long getFzz() {
        return fzz;
    }

    public void setFzz(long fzz) {
        this.fzz = fzz;
    }

    public long getFfq() {
        return ffq;
    }

    public void setFfq(long ffq) {
        this.ffq = ffq;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getFtol() {
        return ftol;
    }

    public void setFtol(long ftol) {
        this.ftol = ftol;
    }

    @Override
    public String toString() {
        return "StatisticReport{" +
                "Id=" + Id +
                ", ftol=" + ftol +
                ", fpv=" + fpv +
                ", fss=" + fss +
                ", fbdj=" + fbdj +
                ", fjg=" + fjg +
                ", fppgy=" + fppgy +
                ", fhz=" + fhz +
                ", fzz=" + fzz +
                ", ffq=" + ffq +
                ", strTime='" + strTime + '\'' +
                ", cityId=" + cityId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
