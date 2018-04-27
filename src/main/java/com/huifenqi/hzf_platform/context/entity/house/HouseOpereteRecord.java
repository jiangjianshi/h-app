package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_house_operate_record")
public class HouseOpereteRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 *房源编号
	 */
	@Column(name = "f_house_sell_id")
	private String houseSellId;

	/**
	 *房间ID
	 */
	@Column(name = "f_room_id")
	private long roomId;
	
	/**
     *来源渠道
     */
    @Column(name = "f_source")
    private String source;
    

	/**
	 *中介公司ID
	 */
	@Column(name = "f_company_id")
	private String companyId;

	/**
	 *中介公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;

	
	/**
     *城市ID
     */
    @Column(name = "f_city_id")
    private long cityId;

    /**
     *城市名称
     */
    @Column(name = "f_city")
    private String city;
    
    
    /**
     *行政区ID
     */
    @Column(name = "f_district_id")
    private long districtId;

    /**
     *行政区
     */
    @Column(name = "f_district")
    private String district;
    
    
    /**
     *商圈ID
     */
    @Column(name = "f_biz_id")
    private long bizId;

    /**
     *商圈名称
     */
    @Column(name = "f_bizname")
    private String bizName;
    
	
	/**
	 *租住类型（0：分租；1：整租；2：整分皆可）
	 */
	@Column(name = "f_entire_rent")
	private Integer entireRent;

	/**
	 *操作类型（1：新增房源；2：更新房源；3：删除房源；4：新增房间；5：更新房间：6：删除房间）
	 */
	@Column(name = "f_opt_type")
	private Integer optType;

	/**
	 *房态 1待租 5已租 0未上架
	 */
	@Column(name = "f_house_status")
	private Integer houseStatus;

	/**
	 *审核状态
	 */
	@Column(name = "f_approve_status")
	private Integer approveStatus;

	/**
	 *百度坐标，经度
	 */
	@Column(name = "f_baidu_lo")
	private String baiduLo;

	/**
	 *百度坐标，纬度
	 */
	@Column(name = "f_baidu_la")
	private String baiduLa;

	/**
	 *创建时间
	 */
	@Column(name = "f_create_time")
	private Date createTime;

	/**
	 *更新时间
	 */
	@Column(name = "f_update_time")
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHouseSellId() {
		return houseSellId;
	}

	public void setHouseSellId(String houseSellId) {
		this.houseSellId = houseSellId;
	}


	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(Integer entireRent) {
		this.entireRent = entireRent;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Integer getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(Integer houseStatus) {
		this.houseStatus = houseStatus;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getBaiduLo() {
		return baiduLo;
	}

	public void setBaiduLo(String baiduLo) {
		this.baiduLo = baiduLo;
	}

	public String getBaiduLa() {
		return baiduLa;
	}

	public void setBaiduLa(String baiduLa) {
		this.baiduLa = baiduLa;
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


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getBizId() {
        return bizId;
    }

    public void setBizId(long bizId) {
        this.bizId = bizId;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    @Override
    public String toString() {
        return "HouseOpereteRecord [id=" + id + ", houseSellId=" + houseSellId + ", roomId=" + roomId + ", source="
                + source + ", companyId=" + companyId + ", companyName=" + companyName + ", cityId=" + cityId
                + ", city=" + city + ", districtId=" + districtId + ", district=" + district + ", bizId=" + bizId
                + ", bizName=" + bizName + ", entireRent=" + entireRent + ", optType=" + optType + ", houseStatus="
                + houseStatus + ", approveStatus=" + approveStatus + ", baiduLo=" + baiduLo + ", baiduLa=" + baiduLa
                + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }
	
	
}
