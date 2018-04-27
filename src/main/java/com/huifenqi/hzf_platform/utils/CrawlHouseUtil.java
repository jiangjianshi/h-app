/** 
 * Project Name: hzf_platform 
 * File Name: HouseUtil.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年4月27日下午12:13:52 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifenqi.hzf_platform.context.CrawlConstants;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.mongodb.BasicDBObject;

/**
 * ClassName: HouseUtil date: 2016年4月27日 下午12:13:52 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class CrawlHouseUtil {

	private static final Log logger = LogFactory.getLog(CrawlHouseUtil.class);

	/**
	 * 从mongo获取房源
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static CrawlHouseDetail getCrawlHouseDetail58(BasicDBObject bdbObj) {
		if (bdbObj == null) {
			return null;
		}
		//初始化爬虫数据入库
		 CrawlHouseDetail CrawlHouseDetail = new CrawlHouseDetail();;
	    String updatetime = bdbObj.getString("update_time");
	    if(updatetime!=null){
		    SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
			try {
				CrawlHouseDetail.setUpdateTime(sim.parse(bdbObj.getString("update_time")));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
	    }  
		CrawlHouseDetail.setHallRoom(bdbObj.getString("hall_room"));
		CrawlHouseDetail.setTags(bdbObj.getString("tags"));	
		CrawlHouseDetail.setPrice(bdbObj.getString("price"));
		CrawlHouseDetail.setRentType(bdbObj.getString("rent_type"));
		CrawlHouseDetail.setImageUrls(bdbObj.getString("image_urls"));
		CrawlHouseDetail.setFacilities(bdbObj.getString("facilities"));
		CrawlHouseDetail.setTraffic(bdbObj.getString("traffic"));
		CrawlHouseDetail.setCrawlTime(bdbObj.getString("crawl_time"));
		CrawlHouseDetail.setAddress(bdbObj.getString("address"));
		CrawlHouseDetail.setServices(bdbObj.getString("services"));
		CrawlHouseDetail.setPhoneNum(bdbObj.getString("phone_num"));
		CrawlHouseDetail.setHouseDesc(bdbObj.getString("house_desc"));
		CrawlHouseDetail.setCity(bdbObj.getString("city"));
		CrawlHouseDetail.setDistrict(bdbObj.getString("district"));
		CrawlHouseDetail.setZone(bdbObj.getString("zone"));
		CrawlHouseDetail.setArea(bdbObj.getString("area"));
		CrawlHouseDetail.setUrl(bdbObj.getString("url"));
		CrawlHouseDetail.setAllowShortRent(bdbObj.getString("allow_short_rent"));
		CrawlHouseDetail.setFloor(bdbObj.getString("floor"));
		CrawlHouseDetail.setLongitude(bdbObj.getString("longitude"));
		CrawlHouseDetail.setDepartmentName(bdbObj.getString("department_name"));
		CrawlHouseDetail.setRentMontyly(bdbObj.getString("rent_montyly"));
		//CrawlHouseDetail.setPagetype(bdbObj.getInt("pagetype"));
		CrawlHouseDetail.setDepartment(bdbObj.getString("department"));
		CrawlHouseDetail.setLatitude(bdbObj.getString("latitude"));
		CrawlHouseDetail.setDeparmentLogoUrl(bdbObj.getString("department_logo_url"));
		CrawlHouseDetail.setDepartmentId(bdbObj.getString("department_id"));
		CrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_NO);// 设置房源收集状态
		CrawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
		CrawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_NO);
		CrawlHouseDetail.setAppId(CrawlConstants.CrawlUtil.CRAWL_APPID_58);
		return CrawlHouseDetail;
	}
	
	
	
	
	/**
	 * 从mongo获取房源
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static CrawlHouseDetail getCrawlHouseDetailMogu(BasicDBObject bdbObj) {
		if (bdbObj == null) {
			return null;
		}
		
		//初始化爬虫数据入库
		 CrawlHouseDetail CrawlHouseDetail = new CrawlHouseDetail();

		CrawlHouseDetail.setTags(bdbObj.getString("tags"));	
		CrawlHouseDetail.setPrice(bdbObj.getString("price"));
		CrawlHouseDetail.setRentType(bdbObj.getString("rent_type"));
		CrawlHouseDetail.setImageUrls(bdbObj.getString("image_urls"));
		CrawlHouseDetail.setFacilities(bdbObj.getString("facilities"));
		//CrawlHouseDetail.setTraffic(bdbObj.getString("traffic"));//距离地铁无
		CrawlHouseDetail.setCrawlTime(bdbObj.getString("crawl_time"));
		CrawlHouseDetail.setAddress(bdbObj.getString("address"));
		CrawlHouseDetail.setHouseDesc(bdbObj.getString("house_desc"));
		CrawlHouseDetail.setCity(bdbObj.getString("city"));
		CrawlHouseDetail.setDistrict(bdbObj.getString("district"));
		//CrawlHouseDetail.setZone(bdbObj.getString("zone"));//无商圈
		CrawlHouseDetail.setArea(bdbObj.getString("area"));
		CrawlHouseDetail.setUrl(bdbObj.getString("url"));
		CrawlHouseDetail.setFloor(bdbObj.getString("floor"));
		CrawlHouseDetail.setLongitude(bdbObj.getString("longitude"));
		CrawlHouseDetail.setDepartmentName(bdbObj.getString("department_name"));
		//CrawlHouseDetail.setPagetype(bdbObj.getInt("pagetype"));
		CrawlHouseDetail.setDepartment(bdbObj.getString("department"));
		CrawlHouseDetail.setLatitude(bdbObj.getString("latitude"));
		CrawlHouseDetail.setDepartmentId(bdbObj.getString("department_id"));
		
		CrawlHouseDetail.setHallRoom(bdbObj.getString("house_overview"));//变更字段
		CrawlHouseDetail.setPhoneNum(bdbObj.getString("telephone"));//电话字段变更		
		CrawlHouseDetail.setBrokerName(bdbObj.getString("broker_name"));
		CrawlHouseDetail.setPayType(bdbObj.getString("pay_type"));
		CrawlHouseDetail.setHouseType(bdbObj.getString("house_type"));
		CrawlHouseDetail.setCommunityName(bdbObj.getString("community_name"));
		CrawlHouseDetail.setOtherFee(bdbObj.getString("other_fee"));
		
		CrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设置房源收集状态
		CrawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
		CrawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);
		CrawlHouseDetail.setAppId(CrawlConstants.CrawlUtil.CRAWL_APPID_MOGU);
		return CrawlHouseDetail;
	}
	
	/**
	 * 从mongo获取房源
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static CrawlHouseDetail getCrawlHouseDetailDanke(BasicDBObject bdbObj) {
		if (bdbObj == null) {
			return null;
		}
		
		//初始化爬虫数据入库
		 CrawlHouseDetail CrawlHouseDetail = new CrawlHouseDetail();

		 
		CrawlHouseDetail.setTags(bdbObj.getString("house_tag"));	
		CrawlHouseDetail.setPrice(bdbObj.getString("price"));
//		CrawlHouseDetail.setRentType(bdbObj.getString("rent_type"));
		CrawlHouseDetail.setImageUrls(bdbObj.getString("image_urls"));
//		CrawlHouseDetail.setFacilities(bdbObj.getString("facilities"));
//		//CrawlHouseDetail.setTraffic(bdbObj.getString("traffic"));//距离地铁无
		CrawlHouseDetail.setCrawlTime(bdbObj.getString("crawl_time"));
//		CrawlHouseDetail.setAddress(bdbObj.getString("address"));
		CrawlHouseDetail.setHouseDesc(bdbObj.getString("house_direction"));
		CrawlHouseDetail.setCity(bdbObj.getString("city"));
		CrawlHouseDetail.setDistrict(bdbObj.getString("district"));
		CrawlHouseDetail.setZone(bdbObj.getString("zone"));//无商圈
		CrawlHouseDetail.setArea(bdbObj.getString("area"));
		CrawlHouseDetail.setUrl(bdbObj.getString("url"));
		CrawlHouseDetail.setFloor(bdbObj.getString("house_floor"));
//		CrawlHouseDetail.setLongitude(bdbObj.getString("longitude"));
		CrawlHouseDetail.setDepartmentName(bdbObj.getString("house_name"));
//		//CrawlHouseDetail.setPagetype(bdbObj.getInt("pagetype"));
//		CrawlHouseDetail.setDepartment(bdbObj.getString("department"));
//		CrawlHouseDetail.setLatitude(bdbObj.getString("latitude"));
		CrawlHouseDetail.setDepartmentId(bdbObj.getString("house_id"));
//		
//		CrawlHouseDetail.setHallRoom(bdbObj.getString("house_overview"));//变更字段
//		CrawlHouseDetail.setPhoneNum(bdbObj.getString("telephone"));//电话字段变更		
//		CrawlHouseDetail.setBrokerName(bdbObj.getString("broker_name"));
//		CrawlHouseDetail.setPayType(bdbObj.getString("pay_type"));
		CrawlHouseDetail.setHouseType(bdbObj.getString("house_type"));
//		CrawlHouseDetail.setCommunityName(bdbObj.getString("community_name"));
//		CrawlHouseDetail.setOtherFee(bdbObj.getString("other_fee"));
		
		CrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设置房源收集状态
		CrawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
		CrawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);
		CrawlHouseDetail.setAppId(CrawlConstants.CrawlUtil.CRAWL_APPID_DANKE);
		return CrawlHouseDetail;
	}
	
	/**
	 * 从mongo获取房源
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static CrawlHouseDetail getCrawlHouseDetailGanji(BasicDBObject bdbObj) {
		if (bdbObj == null) {
			return null;
		}
		
		//初始化爬虫数据入库
		 CrawlHouseDetail CrawlHouseDetail = new CrawlHouseDetail();

		CrawlHouseDetail.setZone(bdbObj.getString("zone"));//商圈
		CrawlHouseDetail.setCommunityName(bdbObj.getString("community_name"));
		CrawlHouseDetail.setDepartmentId(bdbObj.getString("p_monitor_id"));
		CrawlHouseDetail.setCity(bdbObj.getString("city"));
		
		CrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设置房源收集状态
		CrawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
		CrawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);
		CrawlHouseDetail.setAppId(CrawlConstants.CrawlUtil.CRAWL_APPID_GANJI);
		return CrawlHouseDetail;
	}
	
	/**
	 * 从mongo获取房源
	 * 
	 * @param bdHousePublishDto
	 * @return
	 */
	public static CrawlHouseDetail getCrawlHouseDetailBaidu(BasicDBObject bdbObj) {
		if (bdbObj == null) {
			return null;
		}
		//初始化爬虫数据入库
		 CrawlHouseDetail CrawlHouseDetail = new CrawlHouseDetail();;
//	    String updatetime = bdbObj.getString("update_time");
//	    if(updatetime!=null){
//		    SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
//			try {
//				CrawlHouseDetail.setUpdateTime(sim.parse(bdbObj.getString("update_time")));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			} 
//	    }  
		CrawlHouseDetail.setHallRoom(bdbObj.getString("basic_information").replace(" ", ""));//朝向 面积 楼层 室厅卫
		CrawlHouseDetail.setTags(bdbObj.getString("tags"));	 //标签
		CrawlHouseDetail.setPrice(bdbObj.getString("price")); //标签月租金
//		CrawlHouseDetail.setRentType(bdbObj.getString("rent_type"));
		CrawlHouseDetail.setImageUrls(bdbObj.getString("image_urls"));
		CrawlHouseDetail.setFacilities(bdbObj.getString("facilities")); //配置
//		CrawlHouseDetail.setTraffic(bdbObj.getString("traffic"));
		CrawlHouseDetail.setCrawlTime(bdbObj.getString("crawl_time"));
//		CrawlHouseDetail.setAddress(bdbObj.getString("address"));
//		CrawlHouseDetail.setServices(bdbObj.getString("services"));
		CrawlHouseDetail.setPhoneNum(bdbObj.getString("phone"));
//		CrawlHouseDetail.setHouseDesc(bdbObj.getString("house_desc"));
		CrawlHouseDetail.setCity(bdbObj.getString("city"));
		CrawlHouseDetail.setDistrict(bdbObj.getString("district"));
		CrawlHouseDetail.setZone(bdbObj.getString("zone"));
//		CrawlHouseDetail.setArea(bdbObj.getString("area"));
		CrawlHouseDetail.setUrl(bdbObj.getString("url"));
//		CrawlHouseDetail.setAllowShortRent(bdbObj.getString("allow_short_rent"));
//		CrawlHouseDetail.setFloor(bdbObj.getString("floor"));
//		CrawlHouseDetail.setLongitude(bdbObj.getString("longitude"));
		CrawlHouseDetail.setDepartmentName(bdbObj.getString("house_name"));
//		CrawlHouseDetail.setRentMontyly(bdbObj.getString("rent_montyly"));
//		//CrawlHouseDetail.setPagetype(bdbObj.getInt("pagetype"));
//		CrawlHouseDetail.setDepartment(bdbObj.getString("department"));
//		CrawlHouseDetail.setLatitude(bdbObj.getString("latitude"));
//		CrawlHouseDetail.setDeparmentLogoUrl(bdbObj.getString("department_logo_url"));
		CrawlHouseDetail.setDepartmentId(bdbObj.getString("house_id"));
		CrawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设置房源收集状态
		CrawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);
		CrawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);
		CrawlHouseDetail.setAppId(CrawlConstants.CrawlUtil.CRAWL_APPID_BAIDU);
		return CrawlHouseDetail;
	}
	/**
	 * 获取房源对象
	 * 
	 * @param bdbObj crawlType
	 * @return CrawlHouseDetail
	 */
	public static CrawlHouseDetail getCrawlHouseDetailByCrawlType(BasicDBObject bdbObj,int crawlType){
		CrawlHouseDetail crawlHouseDetail = null;
		if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_58){
			 crawlHouseDetail= getCrawlHouseDetail58(bdbObj);
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_MOGU){
			 crawlHouseDetail= getCrawlHouseDetailMogu(bdbObj);
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_GANJI){
			 crawlHouseDetail= getCrawlHouseDetailGanji(bdbObj);
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_BAIDU){
			 crawlHouseDetail= getCrawlHouseDetailBaidu(bdbObj);
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_DANKE){
			 crawlHouseDetail= getCrawlHouseDetailDanke(bdbObj);
		}
		return crawlHouseDetail;
	}
	
	/**
	 * 获取房源对象
	 * 
	 * @param bdbObj crawlType
	 * @return CrawlHouseDetail
	 */
	public static CrawlHouseDetail getCrawlHouseDetailFlag(CrawlHouseDetail crawlHouseDetail,int crawlType){
		if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_MOGU){
			crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设S置房源收集状态
			crawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);//下架状态
			crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);//下架收集状态
			crawlHouseDetail.setUpdateTime(new Date());
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_GANJI){
			crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设S置房源收集状态
			crawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);//下架状态
			crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);//下架收集状态
			crawlHouseDetail.setUpdateTime(new Date());
		}if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_BAIDU){
			crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设S置房源收集状态
			crawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);//下架状态
			crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);//下架收集状态
			crawlHouseDetail.setUpdateTime(new Date());
		}
		if(crawlType == CrawlConstants.CrawlUtil.CRAWL_APPID_DANKE){
			crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_YES);// 设S置房源收集状态
			crawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);//下架状态
			crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_YES);//下架收集状态
			crawlHouseDetail.setUpdateTime(new Date());
		}
		else{
			crawlHouseDetail.setIsRun(CrawlConstants.CrawlHouseDetail.IS_RUN_NO);// 设置房源收集状态
			crawlHouseDetail.setIsDelete(CrawlConstants.CrawlHouseDetail.IS_DELETE_NO);//下架状态
			crawlHouseDetail.setDelFlag(CrawlConstants.CrawlHouseDetail.IS_DELFLAG_NO);//下架收集状态
			crawlHouseDetail.setUpdateTime(new Date());
		}
		return crawlHouseDetail;
	}	
	
}
