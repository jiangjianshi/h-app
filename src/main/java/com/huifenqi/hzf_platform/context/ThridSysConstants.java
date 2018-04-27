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
public class ThridSysConstants {

	public static class BaiduUtil {
		//百度sass图片上传成功标识
		public static final String IMG_UPLOAD_SUCCESS = "0";
		
		//百度sass公寓信息上传成功标识
		public static final String APARTMENT_APPLY_SUCCESS = "0";
		
		//百度sass公寓信息上传回调成功标识
		public static final String APARTMENT_BACK_SUCCESS = "3";
		
		//百度sass公寓信息上传回调失败标识
		public static final String APARTMENT_BACK_FAIL = "2";
		
		//公寓信息上传成功
		public static final int APARTMENT_IS_RUN = 1;
		
		//集中供暖
		public static final Integer SUPPLY_HEATING= 1;
		

		//不区分主次 房屋类型
		public static final Integer ROOM_TYPE_BD= 33;
		
		//不支持短租
		public static final Integer SHORT_RENT_NO = 0;
		
		//整租默认朝南
		public static final int FACE_TO_TYPE_NO = 10003;
		
	}
	
	public static class ThirdSysCompanyUtil {
	    
        public static final int COMPANY_STATUS_YES = 1;
        
        public static final int COMPANY_STATUS_NO = 0;
        
        public static final int COMPANY_TYPE_INNER = 0;
        
        public static final int COMPANY_TYPE_OUT = 1;
    }
	public static class ThirdSysRecordUtil {
		//同步状态 未处理0 1:成功  2:失败
		public static final String ThIRd_SYS_STATUS_INIT = "0";
		
		//同步状态 未处理0 1:成功  2:失败
		public static final String ThIRd_SYS_STATUS_SUCCESS = "1";
		
		//同步状态 未处理0 1:成功  2:失败
		public static final String ThIRd_SYS_STATUS_FARIL = "2";
		
		//操作状态 新增
		public static final int OPT_TYPE_INSERT = 1;
		
		//操作状态 更新
		public static final int OPT_TYPE_UPDATE = 2;
		
		//操作状态 下架
		public static final int OPT_TYPE_UP = 3;
		
		//操作状态 上架
		public static final int OPT_TYPE_DOWN = 5;
				
		//分隔符
		public static final String IS_EMPTY_SPLIT = ",";
		
        public static final String OPT_TARGET_NAME_ALI = "闲鱼";

        public static final int OPT_TARGET_CODE_ALI = 1;
        
        public static final Long DELETE_HOUSE_STATUS = 5L;
        
        public static final Long ALI_HOUSE_CLASS_BS = 1L;
        
        public static final Long ALI_HOUSE_CLASS_PT = 2L;

        public static final Long ALI_BATH_TOOM_TYPE_YES = 1L;
        
        public static final Long ALI_BATH_TOOM_TYPE_NO = 0L;
        
      
	}
	
	public static class ThirdSysUserRecordUtil {
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_INIT = 0;
        
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_SUCCESS = 1;
        
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_FARIL = 2;     
        
        //闲鱼下架 否0 1是
        public static final int IS_OFF_NO = 0;
        
        public static final int IS_OFF_YES = 1;
     
    }
	
	public static class ThirdSysUserUtil {
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_INIT = 0;
        
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_SUCCESS = 1;
        
        //同步状态 未处理0 1:成功  2:失败
        public static final int ThIRd_SYS_STATUS_FARIL = 2;
        
        //是否创建账号队列  0否 1是
        public static final int IS_USER_USE_INIT = 0;
        
        //是否创建账号队列  0否 1是
        public static final int IS_USER_USE_FINSH = 1;
        
        //是否删除  0否 1是
        public static final int IS_DELETE_USER_NO = 0;
        
        //是否删除  0否 1是
        public static final int IS_DELETE_USER_YES = 1;
        
        //闲鱼账户最大发布房源次数
        public static final int USER_MAX_FEED_COUNT = 300;
        
        //账号列表消息rediskey
        public static final String THIRD_SYS_USER_COMPANY = "third.sys.user.company.";
        
        //测试公司
        public static final String THIRD_SYS_USER_COMPANYID = "8888";
        
        //是否清除本公司账号
        public static final String IS_CLEAN_USER = "1";
        
        //闲鱼房态 待出租
        public static final int STATUS_ALI_WAIT_INT = 0;
        
        //闲鱼房态 待出租
        public static final Long STATUS_ALI_WAIT = 0L;
        //闲鱼房态 已租
        public static final Long STATUS_ALI_RENTED = 1L;
    }
	
	public static class ChannelUtil {
        
        //闲鱼
        public static final int OPT_TARGER_CODE_ALI = 1;
        
        //百度
        public static final int OPT_TARGER_CODE_BD = 2;
        
        //所有目标
        public static final int OPT_TARGER_CODE_ALL = -1;

    }
	
	
	public static class SignUtil {
        
        //md5加密
        public static final String SIGN_METHOD_MD5 = "md5";
        
        //hmac加密
        public static final String SIGN_METHOD_HMAC = "hmac";
        
        public static final String CHARSET_UTF8 = "utf-8";

    }
	
	public static class AliUtil {
	    
        public static final String IMG_UPLOAD_SUCCESS = "true";
        
        public static final String ITEM_ADD_OR_UPDATE_SUCCESS = "true";
        
    }
	
	public static class ThirdSysFileUtil {
        //同步状态 未处理0 1:成功  2:失败
        public static final String ThIRd_SYS_STATUS_INIT = "0";
        
        //同步状态 未处理0 1:成功  2:失败
        public static final String ThIRd_SYS_STATUS_SUCCESS = "1";
        
        //同步状态 未处理0 1:成功  2:失败
        public static final String ThIRd_SYS_STATUS_FARIL = "2";
        
        //同步图片最大限制
        public static final int ThIRd_SYS_FILE_MAX = 10;
        
    }
	
}
