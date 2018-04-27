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

/**
 * 房源原始信息Model
 * @author jjs
 *
 */
@Entity
@Table(name = "t_house_original")
public class HouseOriginal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 房源销售编号
	 */
	@Column(name = "f_house_sell_id")
	private String sellId;

	/**
	 * 房源状态
	 */
	@Column(name = "f_status")
	private int status;


	/**
	 * 可入住时间
	 */
	@Column(name = "f_can_checkin_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkInTime; 

	/**
	 * 月租金
	 */
	@Column(name = "f_rent_price_month")
	private int price;

	/**
	 * 日租金
	 */
	@Column(name = "f_rent_price_day")
	private int dayRent;

	/**
	 * 服务费或中介费
	 */
	@Column(name = "f_service_fee")
	private int serviceFee;

	/**
	 * 押金
	 */
	@Column(name = "f_deposit_fee")
	private int depositFee;

	/**
	 * 押金押几个月
	 */
	@Column(name = "f_deposit_month")
	private int depositMonth;

	/**
	 * 每次付几个月的租金
	 */
	@Column(name = "f_period_month")
	private int periodMonth;

	/**
	 * 经纪公司id
	 */
	@Column(name = "f_company_id")
	private String companyId; //2017-07-03 14:47:03  jjs 由int类型改为String

	/**
	 * 经纪公司名称
	 */
	@Column(name = "f_company_name")
	private String companyName;

	/**
	 * 经纪人id
	 */
	@Column(name = "f_agency_id")
	private int agencyId;

	/**
	 * 经纪人电话
	 */
	@Column(name = "f_agency_phone")
	private String agencyPhone;

	/**
	 * 经纪人姓名
	 */
	@Column(name = "f_agency_name")
	private String agencyName;

	/**
	 * 经纪人自我介绍
	 */
	@Column(name = "f_agency_introduce")
	private String agencyIntroduce;

	/**
	 * 经纪人性别 1-男 2-女
	 */
	@Column(name = "f_agency_gender")
	private int agencyGender;

	/**
	 * 经纪人头像
	 */
	@Column(name = "f_agency_avatar")
	private String agencyAvatar;


	/**
	 * 是否有钥匙；1:有；0:无
	 */
	@Column(name = "f_has_key")
	private int hasKey;
	
	/**
	 * 楼栋编号
	 */
	@Column(name = "f_building_no")
	private String buildingNo;

	/**
	 * 单元号
	 */
	@Column(name = "f_unit_no")
	private String unitNo;

	/**
	 * 所在楼层
	 */
	@Column(name = "f_flow_no")
	private String flowNo;

	/**
	 * 总楼层
	 */
	@Column(name = "f_flow_total")
	private String flowTotal;

	/**
	 * 门牌号
	 */
	@Column(name = "f_house_no")
	private String houseNo;

	/**
	 * 建筑面积
	 */
	@Column(name = "f_area")
	private float area;

	/**
	 * 朝向
	 */
	@Column(name = "f_orientations")
	private int orientation;

	/**
	 * 卧室数量
	 */
	@Column(name = "f_bedroom_nums")
	private int bedroomNum;

	/**
	 * 起居室数量
	 */
	@Column(name = "f_livingroom_nums")
	private int livingroomNum;

	/**
	 * 厨房数量
	 */
	@Column(name = "f_kitchen_nums")
	private int kitchenNum;

	/**
	 * 卫生间数量
	 */
	@Column(name = "f_toilet_nums")
	private int toiletNum;

	/**
	 * 阳台数量
	 */
	@Column(name = "f_balcony_nums")
	private int balconyNum;

	/**
	 * 省份
	 */
	@Column(name = "f_province")
	private String province;

	/**
	 * 城市
	 */
	@Column(name = "f_city")
	private String city;

	/**
	 * 行政区
	 */
	@Column(name = "f_district")
	private String district;

	/**
	 * 商圈
	 */
	@Column(name = "f_bizname")
	private String bizName;


	/**
	 * 详细地址
	 */
	@Column(name = "f_address")
	private String address;

	/**
	 * 小区名称
	 */
	@Column(name = "f_community_name")
	private String communityName;
	
	/**
	 * 楼栋名称
	 */
	@Column(name = "f_building_name")
	private String buildingName;

	/**
	 * 百度坐标，经度
	 */
	@Column(name = "f_baidu_lo")
	private String positionY;

	/**
	 * 百度坐标，纬度
	 */
	@Column(name = "f_baidu_la")
	private String positionX;

	/**
	 * 建筑类型
	 */
	@Column(name = "f_building_type")
	private int buildingType;

	/**
	 * 建筑时间
	 */
	@Column(name = "f_building_year")
	private int buildingYear;

	/**
	 * 是否有独立卫生间
	 */
	@Column(name = "f_toilet")
	private int toilet;

	/**
	 * 是否有独立阳台
	 */
	@Column(name = "f_balcony")
	private int balcony;

	/**
	 * 是否有家财险
	 */
	@Column(name = "f_insurance")
	private int insurance;
	
	/**
	 * 装修档次
	 */
	@Column(name = "f_decoration")
	private int decoration;
	
	
	/**
	 * 租住类型
	 */
	@Column(name = "f_entire_rent")
	private int entireRent;
	
	/**
	 * 房源图片
	 */
	@Column(name = "f_house_pics")
	private String imgs;
	
	/**
	 * 房源设置
	 */
	@Column(name = "f_house_setting")
	private String settings;
	

	/**
	 * 房源描述
	 */
	@Column(name = "f_comment")
	private String desc;
	

	/**
	 * 来源
	 */
	@Column(name = "f_source")
	private String source;

	/**
	 * 是否置顶
	 */
	@Column(name = "f_is_top")
	private int isTop;

	/**
	 * 集中式房源编号
	 */
	@Column(name = "f_focus_code")
	private String focusCode;

	/**
	 * 房源类型
	 */
	@Column(name = "f_house_type")
	private int houseType;
	
	/**
	 * 房源类型
	 */
	@Column(name = "f_is_delete")
	private int isDelete;
	
	/**
	 * 创建时间
	 */
	@Column(name = "f_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "f_last_change_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}


	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDayRent() {
		return dayRent;
	}

	public void setDayRent(int dayRent) {
		this.dayRent = dayRent;
	}

	public int getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}

	public int getDepositFee() {
		return depositFee;
	}

	public void setDepositFee(int depositFee) {
		this.depositFee = depositFee;
	}

	public int getDepositMonth() {
		return depositMonth;
	}

	public void setDepositMonth(int depositMonth) {
		this.depositMonth = depositMonth;
	}

	public int getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(int periodMonth) {
		this.periodMonth = periodMonth;
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

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyIntroduce() {
		return agencyIntroduce;
	}

	public void setAgencyIntroduce(String agencyIntroduce) {
		this.agencyIntroduce = agencyIntroduce;
	}

	public int getAgencyGender() {
		return agencyGender;
	}

	public void setAgencyGender(int agencyGender) {
		this.agencyGender = agencyGender;
	}

	public String getAgencyAvatar() {
		return agencyAvatar;
	}

	public void setAgencyAvatar(String agencyAvatar) {
		this.agencyAvatar = agencyAvatar;
	}

	public int getHasKey() {
		return hasKey;
	}

	public void setHasKey(int hasKey) {
		this.hasKey = hasKey;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getFlowTotal() {
		return flowTotal;
	}

	public void setFlowTotal(String flowTotal) {
		this.flowTotal = flowTotal;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}


	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getBedroomNum() {
		return bedroomNum;
	}

	public void setBedroomNum(int bedroomNum) {
		this.bedroomNum = bedroomNum;
	}

	public int getLivingroomNum() {
		return livingroomNum;
	}

	public void setLivingroomNum(int livingroomNum) {
		this.livingroomNum = livingroomNum;
	}

	public int getKitchenNum() {
		return kitchenNum;
	}

	public void setKitchenNum(int kitchenNum) {
		this.kitchenNum = kitchenNum;
	}

	public int getToiletNum() {
		return toiletNum;
	}

	public void setToiletNum(int toiletNum) {
		this.toiletNum = toiletNum;
	}

	public int getBalconyNum() {
		return balconyNum;
	}

	public void setBalconyNum(int balconyNum) {
		this.balconyNum = balconyNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

	public String getPositionX() {
		return positionX;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getBuildingYear() {
		return buildingYear;
	}

	public void setBuildingYear(int buildingYear) {
		this.buildingYear = buildingYear;
	}

	public int getToilet() {
		return toilet;
	}

	public void setToilet(int toilet) {
		this.toilet = toilet;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public int getDecoration() {
		return decoration;
	}

	public void setDecoration(int decoration) {
		this.decoration = decoration;
	}

	public int getEntireRent() {
		return entireRent;
	}

	public void setEntireRent(int entireRent) {
		this.entireRent = entireRent;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getFocusCode() {
		return focusCode;
	}

	public void setFocusCode(String focusCode) {
		this.focusCode = focusCode;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
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
	
	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}
	
	
	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "HouseOriginal [id=" + id + ", sellId=" + sellId + ", status=" + status + ", checkInTime=" + checkInTime
				+ ", price=" + price + ", dayRent=" + dayRent + ", serviceFee=" + serviceFee + ", depositFee="
				+ depositFee + ", depositMonth=" + depositMonth + ", periodMonth=" + periodMonth + ", companyId="
				+ companyId + ", companyName=" + companyName + ", agencyId=" + agencyId + ", agencyPhone=" + agencyPhone
				+ ", agencyName=" + agencyName + ", agencyIntroduce=" + agencyIntroduce + ", agencyGender="
				+ agencyGender + ", agencyAvatar=" + agencyAvatar + ", hasKey=" + hasKey + ", buildingNo=" + buildingNo
				+ ", unitNo=" + unitNo + ", flowNo=" + flowNo + ", flowTotal=" + flowTotal + ", houseNo=" + houseNo
				+ ", area=" + area + ", orientation=" + orientation + ", bedroomNum=" + bedroomNum + ", livingroomNum="
				+ livingroomNum + ", kitchenNum=" + kitchenNum + ", toiletNum=" + toiletNum + ", balconyNum="
				+ balconyNum + ", province=" + province + ", city=" + city + ", district=" + district + ", bizName="
				+ bizName + ", address=" + address + ", communityName=" + communityName + ", buildingName="
				+ buildingName + ", positionY=" + positionY + ", positionX=" + positionX + ", buildingType="
				+ buildingType + ", buildingYear=" + buildingYear + ", toilet=" + toilet + ", balcony=" + balcony
				+ ", insurance=" + insurance + ", decoration=" + decoration + ", entireRent=" + entireRent + ", imgs="
				+ imgs + ", settings=" + settings + ", desc=" + desc + ", source=" + source + ", isTop=" + isTop
				+ ", focusCode=" + focusCode + ", houseType=" + houseType + ", isDelete=" + isDelete + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}
