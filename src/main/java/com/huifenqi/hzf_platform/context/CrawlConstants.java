/** 
 * Project Name: hzf_platform 
 * File Name: Constants.java 
 * Package Name: com.huifenqi.hzf_platform.context 
 * Date: 2016年4月27日下午12:21:09 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context;

/**
 * ClassName: Constants date: 2016年4月27日 下午12:21:09 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public class CrawlConstants {

	/**
	 * 爬虫数据类型
	 */
	public static class CrawlHouseDetail {
		// ------------------爬虫房源采集状态------------------------
		
		// 未收集下架
		public static final int IS_DELFLAG_NO = 0;

		// 已采集下架
		public static final int IS_DELFLAG_YES = 1;
				
				
		// 未采集
		public static final int IS_RUN_NO = 0;

		// 已采集
		public static final int IS_RUN_YES = 1;
		
		// ------------------爬虫房源删除状态------------------------
		// 未删除
		public static final int IS_DELETE_NO = 0;

		// 已删除
		public static final int IS_DELETE_YES = 1;
		
		
		// ------------------爬虫房源出租方式------------------------
		// 分租
		public static final String RENT_TYPE_SHARE = "合租";

		// 整租
		public static final String RENT_TYPE_ENTIRE = "整租";
		
		// ------------------爬虫房源出租方式------------------------
		// 分租
		public static final int RENT_TYPE_SHARE_NUM = 0;

		// 整租
		public static final int RENT_TYPE_ENTIRE_NUM = 1;


	}
	
	/**
	 * 爬虫数据类型
	 */
	public static class CrawlRoomDetail {
		// ------------------爬虫房间类型状态------------------------
		// 主卧
		public static final String ROOM_TYPE_MASTER_STR = "主卧";

		//次卧
		public static final String ROOM_TYPE_SECONDARY_STR = "次卧";
		
		// ------------------房间类型------------------------
		// 主卧
		public static final int ROOM_TYPE_MASTER = 1;

		// 次卧
		public static final int ROOM_TYPE_SECONDARY = 10;

		// 优化间
		public static final int ROOM_TYPE_OPTIMIZED = 20;

	}
	
	/**
	 * 爬虫数据类型
	 */
	public static class CrawlUtil {
		// ------------------爬虫房间类型状态------------------------
		//初始化数量
		public static final int INIT_NUM = 1;
		
		// 58业务编码
		public static final int CRAWL_APPID_58 = 110099;
		
		// 蘑菇业务编码
		public static final int CRAWL_APPID_MOGU = 110088;
		
		//赶集小区
		public static final int CRAWL_APPID_GANJI = 110077;
		
		//百度租房
		public static final int CRAWL_APPID_BAIDU = 110066;
		
		//蛋壳公寓
		public static final int CRAWL_APPID_DANKE = 110055;
		

	}
	
	
	// ------------------房源朝向------------------------
			// 朝向:东
			public static final String ORIENTATION_EAST = "东";

			// 朝向:西
			public static final String ORIENTATION_WEST = "西";

			// 朝向:南
			public static final String ORIENTATION_SOUTH = "南";

			// 朝向:北
			public static final String ORIENTATION_NORTH = "北";

			// 朝向:西南
			public static final String ORIENTATION_SOUTH_WEST = "西南";

			// 朝向:西北
			public static final String ORIENTATION_NORTH_WEST = "西北";

			// 朝向:东北
			public static final String ORIENTATION_NORTH_EAST = "东北";

			// 朝向:东南
			public static final String ORIENTATION_SOUTH_EAST = "东南";

			// 朝向:南北
			public static final String ORIENTATION_NORTH_SOUTH = "南北";

			// 朝向:东西
			public static final String ORIENTATION_EAST_WEST = "东西";

			public static int getOrentationCode(String orentation) {
				int code = -1;
				switch (orentation) {
				case "东":
					code = 10001;
					break;
				case "西":
					code = 10002;
					break;
				case "南":
					code = 10003;
					break;
				case "北":
					code = 10004;
					break;
				case "西南":
					code = 10023;
					break;
				case "西北":
					code = 10024;
					break;
				case "东北":
					code = 10014;
					break;
				case "东南":
					code = 10013;
					break;
				case "南北":
					code = 10034;
					break;
				case "东西":
					code = 10012;
					break;

				default:
					code = -1;
					break;
				}

				return code;
			}
}
