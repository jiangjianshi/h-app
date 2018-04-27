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
 * ClassName: Constants date: 2017年8月31日 下午12:21:09 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class AszConstants {

	/**
	 * 爱上租配置参数
	 */
	public static class ConfigDetail {

		// 请求url测试
		public static final String HTTP_ASZ_URL_TEST = "http://122.225.206.74:9981/isz_thirdparty/openapi/apartment/getApartmentList.action";

		// 请求url线上
		public static final String HTTP_ASZ_URL = "http://isz.ishangzu.com/isz_thirdparty/openapi/apartment/getApartmentList.action";
		

		// RSA私钥测试
		public static final String SIGN_RSA_PRIVATE_TEST = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL6qibrpNeri8oZ5apGtJ2Xvr68aMH3Y6bNGi2+U+MFXY3Q6ZzoYvR+9CyvlpVlBcx4vsSsxcIz1BV5yjamKphfEqnyRrOvVdI7U+JztI2nyVOF/nK3WY5fcYU51NgNvmzQqsbNLPhrhxE9V4Z+2cmI5i3bprT/q2cPQVbhuKe0FAgMBAAECgYABGAliQSRGDLdHfjrWSyAGvbFMV+IfVrdAiA8UvM4QjefMKumcs7eiDvuZbN/d+zol2jAyBz6WEHHPcOjPKDR7u+Edf8pNgB7N11I2W2YKa13ZHGFDgEryk42ipcyZIKMHt8Jlz1PwqORKGc1Y5KkLiOJ8cVetf0Geg6Ojc7rAoQJBAPNLKxUQ1WENliHCRrq9LdnckV+A/eA+oI8kmfWWJ3ziKzRJUkVvE/RlS1qq3HCVF/kdOK/a55y6+rVDi3tKzLcCQQDIn73OcludC8OCJ5O9Ba2FOVmAiLRZPj8hhL0NwUVR70L6agiy6kT/yeoL7h6Adar6+DuegW7RcgFwwReXf5AjAkAT8jy0/G1SCKgfWmssEih5LREqEExAH0JQmgKZVNcl8PDz13MMSEANkGRuKYXrIP4XKWMlX8APZHD7fW8pC4ffAkEAgMhx2dT01BHaFXF8V6kOYueWeXjHdEYN1mFTzkGTUu4oa4CnRto1IpElaTUYZVOjRukTtELXtSDepdd9YmWjSwJAbfhK9taqhPOtVvswb6Ruh5tGUjBaCv8S08rmmaRc9lwJekCd5KftVLSxrN62T0g0OODbc/T8Qm7PtOVaFB0kPg==";

		// RSA私钥测试
		public static final String HTTP_CLIENT_ID_TEST = "ISZ17091501";

		// 上次获取房源key
		public static final String ASZ_TASK_LAST_TIME = "asz.task.last.time";

		// 每次获取数据条数
		public static final int REQUEST_PAGE_SIZE = 1000;
		
		// 每次获取数据条数
		public static final String APP_ID = "120007";
				
		// 每次获取数据条数
		public static final String HOUSE_SOURCE = "爱上租";

	}
		
	/**
	 * 爱上租房源信息
	 */
	public static class HouseDetail {

		// 待转换房源
		public static final int TRANSFER_FLAG_NO = 0;
		// 已转换
		public static final int TRANSFER_FLAG_YES = 1;
		
		// 作废
		public static final int TRANSFER_FLAG_UNKNOW = 2;

		// 整租
		public static final String RENT_TYPE_ENTIRE = "1";
		// 分租
		public static final String RENT_TYPE_SHARE = "0";

		// 未知房态
		public static final Integer NOTFOUND = 11111;
		
		//房间号 甲
		public static final String ROOM_NO_METH = "METH";
		
		//房间类型 主卧
		public static final Integer ROOM_TYPE_MAST = 1;
					
		//房间类型 次卧
		public static final Integer ROOM_TYPE_SECONDARY = 10;
				
		//房间类型 隔间
		public static final Integer ROOM_TYPE_PARTION = 30;
		
	}
}
