package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_house_type_mapping")
public class HouseTypeMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * 户型描述
	 */
	@Column(name = "f_live_bed_totile")
	private String liveBedTotile;
	
	/**
	 * 卧室数量
	 */
	@Column(name = "f_bedroom_nums")
	private Integer bedroomNums;
	
	/**
	 * 客厅数量
	 */
	@Column(name = "f_livingroom_nums")
	private Integer livingroomNums;
	
	/**
	 * 卫生间数量
	 */
	@Column(name = "f_toilet_nums")
	private Integer toiletNums;
	
	/**
	 * 状态
	 */
	@Column(name = "f_status")
	private Integer status;
	
	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	private Date createTime;
	
	
	/**
	 * 最后更新时间
	 */
	@Column(name = "f_last_change_date")
	private Date updateTime;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getLiveBedTotile() {
		return liveBedTotile;
	}


	public void setLiveBedTotile(String liveBedTotile) {
		this.liveBedTotile = liveBedTotile;
	}


	public Integer getBedroomNums() {
		return bedroomNums;
	}


	public void setBedroomNums(Integer bedroomNums) {
		this.bedroomNums = bedroomNums;
	}


	public Integer getLivingroomNums() {
		return livingroomNums;
	}


	public void setLivingroomNums(Integer livingroomNums) {
		this.livingroomNums = livingroomNums;
	}


	public Integer getToiletNums() {
		return toiletNums;
	}


	public void setToiletNums(Integer toiletNums) {
		this.toiletNums = toiletNums;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
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
	
}
