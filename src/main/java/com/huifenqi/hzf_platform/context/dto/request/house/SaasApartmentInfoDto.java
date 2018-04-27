package com.huifenqi.hzf_platform.context.dto.request.house;

import java.util.List;

public class SaasApartmentInfoDto {

	/**
	 * saas的系统对公寓的id
	 */
	private String apart_id;
	/**
	 * 公司全称  
	 */
	private String company_name;
	/**
	 * 公寓名称  
	 */
	private String apart_name;
	/**
	 * 公寓介绍
	 */
	private String apart_intro ;
	/**
	 * 公寓服务  
	 */
	private String apart_service;
	/**
	 * 法人姓名 
	 */
	private String corporate_name;
	
	/**
	 * 法人身份证照片的key  
	 */
	private String id_card_hiphoto_key;

	/**
	 * 样板间的房源照片 
	 */
	private List<String> templet_room_pic_key;
	/**
	 *  联系人姓名
	 */
	private String contact_name;
	/**
	 * 联系人电话 
	 */
	private String contact_phone;
	/**
	 * 营业执照号码 
	 */
	private String licence_number ;
	/**
	 *  营业执照照片key
	 */
	private String licence_hiphoto_key;
	/**
	 * 组织机构代码 
	 */
	private String organization_code;

	/**
	 * 税务登记代码 
	 */
	private String tax_code;
	/**
	 * 企业注册地址 
	 */
	private String registered_address;
	/**
	 *  公章照片key
	 */
	private String cachet_hiphoto_key;
	/**
	 * 公寓覆盖的城市 
	 */
	private String covered_city;
	/**
	 *  公寓在saas运营时间
	 */
	private int operate_duration_in_saas;
	/**
	 * 公寓空闲房源总数量 
	 */
	private int room_free_count;
	
	/**
	 * 公寓房源总数量 
	 */
	private int room_total_count;
	/**
	 * 公寓推荐理由 
	 */
	private String saas_recommend_reason;
	
	/**
	 * 通知公寓预约信息的接口  N
	 */
	private String order_notify_url;

	public String getApart_id() {
		return apart_id;
	}

	public void setApart_id(String apart_id) {
		this.apart_id = apart_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getApart_name() {
		return apart_name;
	}

	public void setApart_name(String apart_name) {
		this.apart_name = apart_name;
	}

	public String getApart_intro() {
		return apart_intro;
	}

	public void setApart_intro(String apart_intro) {
		this.apart_intro = apart_intro;
	}

	public String getApart_service() {
		return apart_service;
	}

	public void setApart_service(String apart_service) {
		this.apart_service = apart_service;
	}

	public String getCorporate_name() {
		return corporate_name;
	}

	public void setCorporate_name(String corporate_name) {
		this.corporate_name = corporate_name;
	}

	public String getId_card_hiphoto_key() {
		return id_card_hiphoto_key;
	}

	public void setId_card_hiphoto_key(String id_card_hiphoto_key) {
		this.id_card_hiphoto_key = id_card_hiphoto_key;
	}

	public List<String> getTemplet_room_pic_key() {
		return templet_room_pic_key;
	}

	public void setTemplet_room_pic_key(List<String> templet_room_pic_key) {
		this.templet_room_pic_key = templet_room_pic_key;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getLicence_number() {
		return licence_number;
	}

	public void setLicence_number(String licence_number) {
		this.licence_number = licence_number;
	}

	public String getLicence_hiphoto_key() {
		return licence_hiphoto_key;
	}

	public void setLicence_hiphoto_key(String licence_hiphoto_key) {
		this.licence_hiphoto_key = licence_hiphoto_key;
	}

	public String getOrganization_code() {
		return organization_code;
	}

	public void setOrganization_code(String organization_code) {
		this.organization_code = organization_code;
	}

	public String getTax_code() {
		return tax_code;
	}

	public void setTax_code(String tax_code) {
		this.tax_code = tax_code;
	}

	public String getRegistered_address() {
		return registered_address;
	}

	public void setRegistered_address(String registered_address) {
		this.registered_address = registered_address;
	}

	public String getCachet_hiphoto_key() {
		return cachet_hiphoto_key;
	}

	public void setCachet_hiphoto_key(String cachet_hiphoto_key) {
		this.cachet_hiphoto_key = cachet_hiphoto_key;
	}

	public String getCovered_city() {
		return covered_city;
	}

	public void setCovered_city(String covered_city) {
		this.covered_city = covered_city;
	}

	public int getOperate_duration_in_saas() {
		return operate_duration_in_saas;
	}

	public void setOperate_duration_in_saas(int operate_duration_in_saas) {
		this.operate_duration_in_saas = operate_duration_in_saas;
	}

	public int getRoom_free_count() {
		return room_free_count;
	}

	public void setRoom_free_count(int room_free_count) {
		this.room_free_count = room_free_count;
	}

	public int getRoom_total_count() {
		return room_total_count;
	}

	public void setRoom_total_count(int room_total_count) {
		this.room_total_count = room_total_count;
	}

	public String getSaas_recommend_reason() {
		return saas_recommend_reason;
	}

	public void setSaas_recommend_reason(String saas_recommend_reason) {
		this.saas_recommend_reason = saas_recommend_reason;
	}

	public String getOrder_notify_url() {
		return order_notify_url;
	}

	public void setOrder_notify_url(String order_notify_url) {
		this.order_notify_url = order_notify_url;
	}
	

}
