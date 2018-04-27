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
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class ScheduleConstants {

	/**
	 * 全房通数据转换
	 */
	public static class QftUtil {
		// ------------------全房通数据转换采集状态------------------------
		
		// 发布或更新失败
		public static final int IS_FLAG_UPDATE_FAIL = -2;
				
		// 转化失败（坐标问题）
		public static final int IS_FLAG_FAIL = -1;

		// 转化成功
		public static final int IS_FLAG_YES = 1;


		// 百度接口成功
		public static final int IS_FLAG_SUCCESS = 2;//全房通无坐标，转换成功的房源

		// 已删除
		public static final int IS_DELETE_YES = 1;

		// 未删除
		public static final int IS_DELEE_NO = 0;

		// 整租
		public static final int RENT_TYPE_ENTIRE = 1;

		// 合租
		public static final int RENT_TYPE_SHARE = 2;
	}
	
	
}
