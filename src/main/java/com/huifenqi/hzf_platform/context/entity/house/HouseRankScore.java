package com.huifenqi.hzf_platform.context.entity.house;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_house_rank_score")
public class HouseRankScore {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; // 主键
	
	@Column(name = "f_house_sell_id")
	private String sellId; // 房源ID
	
	@Column(name = "f_room_id")
	private long roomId; // 房间ID
	
	@Column(name = "f_source_score")
	private Double sourceScore; // 来源得分
	
	@Column(name = "f_pic_score")
	private Double picScore; // 图片得分
	
	@Column(name = "f_base_info_score")
	private Double baseInfoScore; // 基本信息得分
	
	@Column(name = "f_subway_score")
	private Double subwayScore; // 地铁得分
	
	@Column(name = "f_random_score")
	private Double randomScore; // 随机分数
	
	@Column(name = "f_img_deco_score")
	private Double imgDecoScore; // 装修度
	
	@Column(name = "f_img_repeat_score")
	private Double imgRepeatScore; // 重复度
	
	@Column(name = "f_img_shooting_score")
	private Double imgShootingScore; // 拍摄度
	
	@Column(name = "f_img_cover_score")
	private Double imgCoverScore; // 覆盖度
	
	@Column(name = "f_img_total_score")
	private Double imgTotalScore; // 图片总分
	
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate; // 创建时间
	
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChangeDate; // 更新时间
	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getSellId() {
		return sellId;
	}


	public void setSellId(String sellId) {
		this.sellId = sellId;
	}


	public long getRoomId() {
		return roomId;
	}


	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}


	public Double getSourceScore() {
		return sourceScore;
	}


	public void setSourceScore(Double sourceScore) {
		this.sourceScore = sourceScore;
	}


	public Double getPicScore() {
		return picScore;
	}


	public void setPicScore(Double picScore) {
		this.picScore = picScore;
	}


	public Double getBaseInfoScore() {
		return baseInfoScore;
	}


	public void setBaseInfoScore(Double baseInfoScore) {
		this.baseInfoScore = baseInfoScore;
	}


	public Double getSubwayScore() {
		return subwayScore;
	}


	public void setSubwayScore(Double subwayScore) {
		this.subwayScore = subwayScore;
	}


	public Double getImgDecoScore() {
		return imgDecoScore;
	}


	public void setImgDecoScore(Double imgDecoScore) {
		this.imgDecoScore = imgDecoScore;
	}


	public Double getImgRepeatScore() {
		return imgRepeatScore;
	}


	public void setImgRepeatScore(Double imgRepeatScore) {
		this.imgRepeatScore = imgRepeatScore;
	}


	public Double getImgShootingScore() {
		return imgShootingScore;
	}


	public void setImgShootingScore(Double imgShootingScore) {
		this.imgShootingScore = imgShootingScore;
	}


	public Double getImgCoverScore() {
		return imgCoverScore;
	}


	public void setImgCoverScore(Double imgCoverScore) {
		this.imgCoverScore = imgCoverScore;
	}


	public Double getImgTotalScore() {
		return imgTotalScore;
	}


	public void setImgTotalScore(Double imgTotalScore) {
		this.imgTotalScore = imgTotalScore;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getLastChangeDate() {
		return lastChangeDate;
	}


	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}


	public Double getRandomScore() {
		return randomScore;
	}


	public void setRandomScore(Double randomScore) {
		this.randomScore = randomScore;
	}


	@Override
	public String toString() {
		return "HouseRankScore [id=" + id + ", sellId=" + sellId + ", roomId=" + roomId + ", sourceScore=" + sourceScore
				+ ", picScore=" + picScore + ", baseInfoScore=" + baseInfoScore + ", subwayScore=" + subwayScore
				+ ", randomScore=" + randomScore + ", imgDecoScore=" + imgDecoScore + ", imgRepeatScore="
				+ imgRepeatScore + ", imgShootingScore=" + imgShootingScore + ", imgCoverScore=" + imgCoverScore
				+ ", imgTotalScore=" + imgTotalScore + ", creationDate=" + creationDate + ", lastChangeDate="
				+ lastChangeDate + "]";
	}

	
}
