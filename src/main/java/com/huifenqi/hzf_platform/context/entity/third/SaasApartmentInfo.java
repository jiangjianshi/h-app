/** 
* Project Name: hzf_platform 
* File Name: HouseDetail.java 
* Package Name: com.huifenqi.hzf_platform.context.business 
* Date: 2016年4月25日下午4:05:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.context.entity.third;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ClassName: SaasApartmentInfo date: 2017年5月10日 下午4:05:50 Description:公寓信息
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Entity
@Table(name = "t_saas_apartment_info")
public class SaasApartmentInfo {

	@Id
	@Column(name = "f_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * saas的系统对公寓的id
	 */
	@Column(name = "f_apart_id")
	private String apartId;
	/**
	 * 公司全称  
	 */
	@Column(name = "f_company_name")
	private String companyName;
	/**
	 * 公寓名称  
	 */
	@Column(name = "f_apart_name")
	private String apartName;
	/**
	 * 公寓介绍
	 */
	@Column(name = "f_apart_intro ")
	private String apartIntro ;
	/**
	 * 公寓服务  
	 */
	@Column(name = "f_apart_service")
	private String apartService;
	
	/**
	 * 法人姓名 
	 */
	@Column(name = "f_id_card_hiphoto_key")
	private String idCardHiphotoKey;
	/**
	 * 法人身份证照片的key  
	 */
	@Column(name = "f_corporate_name")
	private String corporateName;
	/**
	 * 样板间的房源照片 
	 */
	@Column(name = "f_templet_room_pic_key")
	private String templetRoomPicKey;
	/**
	 *  联系人姓名
	 */
	@Column(name = "f_contact_name")
	private String contactName;
	/**
	 * 联系人电话 
	 */
	@Column(name = "f_contact_phone")
	private String contactPhone;
	/**
	 * 营业执照号码 
	 */
	@Column(name = "f_licence_number ")
	private String licenceNumber ;
	/**
	 *  营业执照照片key
	 */
	@Column(name = "f_licence_hiphoto_key")
	private String licenceHiphotoKey;
	/**
	 * 组织机构代码 
	 */
	@Column(name = "f_organization_code")
	private String organizationCode;

	/**
	 * 税务登记代码 
	 */
	@Column(name = "f_tax_code")
	private String taxCode;
	/**
	 * 企业注册地址 
	 */
	@Column(name = "f_registered_address")
	private String registeredAddress;
	/**
	 *  公章照片key
	 */
	@Column(name = "f_cachet_hiphoto_key")
	private String cachetHiphotoKey;
	/**
	 * 公寓覆盖的城市 
	 */
	@Column(name = "f_covered_city")
	private String coveredCity;
	/**
	 *  公寓在saas运营时间
	 */
	@Column(name = "f_operate_duration_in_saas")
	private int operateDurationInSaas;
	/**
	 * 公寓空闲房源总数量 
	 */
	@Column(name = "f_room_free_count")
	private int roomFreeCount;
	
	/**
	 * 公寓房源总数量 
	 */
	@Column(name = "f_room_total_count")
	private int roomTotalCount;
	/**
	 * 公寓推荐理由 
	 */
	@Column(name = "f_saas_recommend_reason")
	private String saasRecommendReason;
	
	/**
	 * 分配给sass的id
	 */
	@Column(name = "f_saas_id")
	private String sassId;
	/**
	 * 审核结果
	 */
	@Column(name = "f_review_status")
	private String review_status;
	/**
	 * 审核失败原因
	 */
	@Column(name = "f_give_back_reason")
	private String giveBackReason;
	/**
	 * 分配的ak
	 */
	@Column(name = "f_ak")
	private String ak;
	/**
	 * 分配的appid
	 */
	@Column(name = "f_app_id")
	private String appId;
	
	/**
	 * 公寓信息创建时间
	 */
	@Column(name = "f_create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/**
	 * 公寓信息更新时间
	 */
	@Column(name = "f_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	/**
	 * saas回调时间
	 */
	@Column(name = "f_back_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date backTime;
	/**
	 * 公寓上传状态
	 */
	@Column(name = "f_run")
	private int isRun;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getApartId() {
		return apartId;
	}
	public void setApartId(String apartId) {
		this.apartId = apartId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getApartName() {
		return apartName;
	}
	public void setApartName(String apartName) {
		this.apartName = apartName;
	}
	public String getApartIntro() {
		return apartIntro;
	}
	public void setApartIntro(String apartIntro) {
		this.apartIntro = apartIntro;
	}
	public String getApartService() {
		return apartService;
	}
	public void setApartService(String apartService) {
		this.apartService = apartService;
	}
	public String getIdCardHiphotoKey() {
		return idCardHiphotoKey;
	}
	public void setIdCardHiphotoKey(String idCardHiphotoKey) {
		this.idCardHiphotoKey = idCardHiphotoKey;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getTempletRoomPicKey() {
		return templetRoomPicKey;
	}
	public void setTempletRoomPicKey(String templetRoomPicKey) {
		this.templetRoomPicKey = templetRoomPicKey;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getLicenceNumber() {
		return licenceNumber;
	}
	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}
	public String getLicenceHiphotoKey() {
		return licenceHiphotoKey;
	}
	public void setLicenceHiphotoKey(String licenceHiphotoKey) {
		this.licenceHiphotoKey = licenceHiphotoKey;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	public String getCachetHiphotoKey() {
		return cachetHiphotoKey;
	}
	public void setCachetHiphotoKey(String cachetHiphotoKey) {
		this.cachetHiphotoKey = cachetHiphotoKey;
	}
	public String getCoveredCity() {
		return coveredCity;
	}
	public void setCoveredCity(String coveredCity) {
		this.coveredCity = coveredCity;
	}
	public int getOperateDurationInSaas() {
		return operateDurationInSaas;
	}
	public void setOperateDurationInSaas(int operateDurationInSaas) {
		this.operateDurationInSaas = operateDurationInSaas;
	}
	public int getRoomFreeCount() {
		return roomFreeCount;
	}
	public void setRoomFreeCount(int roomFreeCount) {
		this.roomFreeCount = roomFreeCount;
	}
	public int getRoomTotalCount() {
		return roomTotalCount;
	}
	public void setRoomTotalCount(int roomTotalCount) {
		this.roomTotalCount = roomTotalCount;
	}
	public String getSaasRecommendReason() {
		return saasRecommendReason;
	}
	public void setSaasRecommendReason(String saasRecommendReason) {
		this.saasRecommendReason = saasRecommendReason;
	}
	public String getSassId() {
		return sassId;
	}
	public void setSassId(String sassId) {
		this.sassId = sassId;
	}
	public String getReview_status() {
		return review_status;
	}
	public void setReview_status(String review_status) {
		this.review_status = review_status;
	}
	public String getGiveBackReason() {
		return giveBackReason;
	}
	public void setGiveBackReason(String giveBackReason) {
		this.giveBackReason = giveBackReason;
	}
	public String getAk() {
		return ak;
	}
	public void setAk(String ak) {
		this.ak = ak;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	public Date getBackTime() {
		return backTime;
	}
	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}
	public int getIsRun() {
		return isRun;
	}
	public void setIsRun(int isRun) {
		this.isRun = isRun;
	}
	@Override
	public String toString() {
		return "SaasApartmentInfo [id=" + id + ", apartId=" + apartId + ", companyName=" + companyName + ", apartName="
				+ apartName + ", apartIntro=" + apartIntro + ", apartService=" + apartService + ", idCardHiphotoKey="
				+ idCardHiphotoKey + ", corporateName=" + corporateName + ", templetRoomPicKey=" + templetRoomPicKey
				+ ", contactName=" + contactName + ", contactPhone=" + contactPhone + ", licenceNumber=" + licenceNumber
				+ ", licenceHiphotoKey=" + licenceHiphotoKey + ", organizationCode=" + organizationCode + ", taxCode="
				+ taxCode + ", registeredAddress=" + registeredAddress + ", cachetHiphotoKey=" + cachetHiphotoKey
				+ ", coveredCity=" + coveredCity + ", operateDurationInSaas=" + operateDurationInSaas
				+ ", roomFreeCount=" + roomFreeCount + ", roomTotalCount=" + roomTotalCount + ", saasRecommendReason="
				+ saasRecommendReason + ", sassId=" + sassId + ", review_status=" + review_status + ", giveBackReason="
				+ giveBackReason + ", ak=" + ak + ", appId=" + appId + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", backTime=" + backTime + ", isRun=" + isRun + "]";
	}
	
	
	
}
