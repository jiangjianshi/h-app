package com.huifenqi.usercomm.domain;
import java.util.Date;

import javax.persistence.*;

/**
 * Created by HFQ-Arison on 2017/5/11.
 */
@Entity
@Table(name = "t_three_part_user")
public class ThreePartUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="f_id")
    private long id;
    /**
     * 关联用户表Id
     */
    @Column(name="f_user_id")
    private long userId;

    @Column(name="f_qq_id")
    private String qqId;

    @Column(name="f_qq_img")
    private String qqImg;


    @Column(name="f_qq_state")
    private int qqState;

    @Column(name="f_wx_id")
    private String wxId;

    @Column(name="f_wx_state")
    private int wxState;


    @Column(name="f_wb_id")
    private String wbId;

    @Column(name="f_wb_state")
    private int wbState;


    @Column(name="f_state")
    private int state;


    @Column(name="f_qq_nick_name")
    private String qqNickName;

    @Column(name="f_wx_nick_name")
    private String wxNickName;

    @Column(name="f_wb_nick_name")
    private String wbNickName;

    @Column(name="f_create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name="f_update_time")
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getQqImg() {
        return qqImg;
    }

    public void setQqImg(String qqImg) {
        this.qqImg = qqImg;
    }

    public int getQqState() {
        return qqState;
    }

    public void setQqState(int qqState) {
        this.qqState = qqState;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public int getWxState() {
        return wxState;
    }

    public void setWxState(int wxState) {
        this.wxState = wxState;
    }

    public String getWbId() {
        return wbId;
    }

    public void setWbId(String wbId) {
        this.wbId = wbId;
    }

    public int getWbState() {
        return wbState;
    }

    public void setWbState(int wbState) {
        this.wbState = wbState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getQqNickName() {
        return qqNickName;
    }

    public void setQqNickName(String qqNickName) {
        this.qqNickName = qqNickName;
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

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getWbNickName() {
        return wbNickName;
    }

    public void setWbNickName(String wbNickName) {
        this.wbNickName = wbNickName;
    }

    @Override
    public String toString() {
        return "ThreePartUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", qqId='" + qqId + '\'' +
                ", qqImg='" + qqImg + '\'' +
                ", qqState=" + qqState +
                ", wxId='" + wxId + '\'' +
                ", wxState=" + wxState +
                ", wbId='" + wbId + '\'' +
                ", wbState=" + wbState +
                ", state=" + state +
                ", qqNickName='" + qqNickName + '\'' +
                ", wxNickName='" + wxNickName + '\'' +
                ", wbNickName='" + wbNickName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}