package com.huifenqi.hzf_platform.vo;

/**
 * ClassName: QueryDetailVo date: 2017年8月10日 上午11:05:50 Description:查询VO类
 * 
 * @author YDM
 * @version
 * @since JDK 1.8
 */
public class QueryDetailVo {
	
	public QueryDetailVo(long communityHouseCount, long companyHouseCount, long companyCityCount) {
		super();
		this.communityHouseCount = communityHouseCount;
		this.companyHouseCount = companyHouseCount;
		this.companyCityCount = companyCityCount;
	}
	
	public QueryDetailVo(String bizName, long bizNameCount) {
		super();
		this.bizName = bizName;
		this.bizNameCount = bizNameCount;
	}
	
	public QueryDetailVo(long cityId) {
		super();
		this.cityId = cityId;
	}
	
	public QueryDetailVo(long cityId, String cityName, String center) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.center = center;
	}
	
	public QueryDetailVo(long cityId, String companyId, long houseNum) {
		super();
		this.cityId = cityId;
		this.companyId = companyId;
		this.houseNum = houseNum;
	}
	
	public QueryDetailVo(String sellId, int roomId) {
		super();
		this.sellId = sellId;
		this.roomId = roomId;
	}
	
	public QueryDetailVo(String companyId, String companyName, String companyDesc, String picRootPath, String picWebPath, long houseNum) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.companyDesc = companyDesc;
		this.picRootPath = picRootPath;
		this.picWebPath = picWebPath;
		this.houseNum = houseNum;
	}
	
	/**
	 * 商圈名称
	 */
	private String bizName;
	
	/**
	 * 商圈总数
	 */
	private long bizNameCount;
	
	/**
	 * 同小区房源总数
	 */
	private long communityHouseCount;
	
	/**
	 * 公寓下所有房源总数
	 */
	private long companyHouseCount;
	
	/**
	 * 公寓下所有城市总数
	 */
	private long companyCityCount;
	
	/**
	 * 城市ID
	 */
	private long cityId;
	
	/**
	 * 城市名称
	 */
	private String cityName;
	
	/**
	 * 中心城市坐标点
	 */
	private String center;
	
	/**
	 * 品牌公寓ID
	 */
	private String companyId;
	
	/**
	 * 品牌公寓名称
	 */
	private String companyName;
	
	/**
	 * 品牌公寓描述
	 */
	private String companyDesc;
	
	/**
	 * 品牌公寓图片
	 */
	private String picRootPath;
	
	/**
	 * 品牌公寓图片
	 */
	private String picWebPath;
	
	/**
	 * 房源总数
	 */
	private long houseNum;
	
	/**
	 * 房源ID
	 */
	private String sellId;
	
	/**
	 * 房间ID
	 */
	private int roomId;

	
	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public long getBizNameCount() {
		return bizNameCount;
	}

	public void setBizNameCount(long bizNameCount) {
		this.bizNameCount = bizNameCount;
	}

	public long getCommunityHouseCount() {
		return communityHouseCount;
	}

	public void setCommunityHouseCount(long communityHouseCount) {
		this.communityHouseCount = communityHouseCount;
	}

	public long getCompanyHouseCount() {
		return companyHouseCount;
	}

	public void setCompanyHouseCount(long companyHouseCount) {
		this.companyHouseCount = companyHouseCount;
	}

	public long getCompanyCityCount() {
		return companyCityCount;
	}

	public void setCompanyCityCount(long companyCityCount) {
		this.companyCityCount = companyCityCount;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public long getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(long houseNum) {
		this.houseNum = houseNum;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getPicRootPath() {
		return picRootPath;
	}

	public void setPicRootPath(String picRootPath) {
		this.picRootPath = picRootPath;
	}

	public String getPicWebPath() {
		return picWebPath;
	}

	public void setPicWebPath(String picWebPath) {
		this.picWebPath = picWebPath;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}


}
