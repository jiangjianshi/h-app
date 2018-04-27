/** 
 * Project Name: mq_project 
 * File Name: ErrorCode.java 
 * Package Name: com.huifenqi.mq.context 
 * Date: 2015年11月30日下午2:54:09 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context.exception;

/**
 * ClassName: ErrorCode date: 2015年11月30日 下午2:54:09 Description: 错误编码
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public class ErrorMsgCode {

	// 正确
	public final static int ERROR_MSG_OK = 0;

	// 通用错误码起始值
	public final static int ERROR_COMMON_OFFSET = 2310000;

	// 业务错误码起始值
	public final static int ERROR_BUSINESS_OFFSET = 2320000;

	// 找户用户错误码起始值
	public final static int ERROR_HUIZHOF_OFFSET = 2330000;
	// -----------------------------通用错误码-----------------------------------------------

	// 未知错误
	public final static int ERROR_MSG_UNKNOWN = ERROR_COMMON_OFFSET + 1;

	// 缺少参数
	public final static int ERROR_MSG_MISS_PARAMETERS = ERROR_COMMON_OFFSET + 2;

	// 参数异常
	public final static int ERROR_MSG_INVALID_PARAMETER = ERROR_COMMON_OFFSET + 3;

	// 接口签名鉴权失败
	public final static int ERROR_MSG_API_SIGN_AUTH_FAIL = ERROR_COMMON_OFFSET + 4;

	// 接口Token验证失败
	public final static int ERROR_MSG_API_TOEKN_AUTH_FAIL = ERROR_COMMON_OFFSET + 5;

	// 平台用户未找到
	public final static int ERROR_MSG_CUSTOMER_NOT_FOUND = ERROR_COMMON_OFFSET + 6;

	// -----------------------------业务错误码-----------------------------------------------
	// 发布房源失败
	public final static int ERROR_MSG_ADD_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 1;

	// 修改房源失败
	public final static int ERROR_MSG_MODIFY_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 2;

	// 查询房源失败
	public final static int ERROR_MSG_QUERY_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 3;

	// 删除房源失败
	public final static int ERROR_MSG_REMOVE_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 4;

	// 发布房间失败
	public final static int ERROR_MSG_ADD_ROOM_FAIL = ERROR_BUSINESS_OFFSET + 5;

	// 修改房间失败
	public final static int ERROR_MSG_MODIFY_ROOM_FAIL = ERROR_BUSINESS_OFFSET + 6;

	// 查询房间失败
	public final static int ERROR_MSG_QUERY_ROOM_FAIL = ERROR_BUSINESS_OFFSET + 7;

	// 删除房间失败
	public final static int ERROR_MSG_REMOVE_ROOM_FAIL = ERROR_BUSINESS_OFFSET + 8;

	// 保存投诉失败
	public final static int ERROR_MSG_ADD_COMPLAINT_FAIL = ERROR_BUSINESS_OFFSET + 9;

	// 修改投诉失败
	public final static int ERROR_MSG_MODIFY_COMPLAINT_FAIL = ERROR_BUSINESS_OFFSET + 10;

	// 查询投诉失败
	public final static int ERROR_MSG_QUERY_COMPLAINT_FAIL = ERROR_BUSINESS_OFFSET + 11;

	// 删除投诉失败
	public final static int ERROR_MSG_REMOVE_COMPLAINT_FAIL = ERROR_BUSINESS_OFFSET + 12;

	// 搜索房源失败
	public final static int ERROR_MSG_SEARCH_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 13;

	// 增加公寓失败
	public final static int ERROR_MSG_ADD_APARTMENT_FAIL = ERROR_BUSINESS_OFFSET + 14;

	// 修改公寓失败
	public final static int ERROR_MSG_MODIFY_APARTMENT_FAIL = ERROR_BUSINESS_OFFSET + 15;

	// 查询公寓失败
	public final static int ERROR_MSG_QUERY_APARTMENT_FAIL = ERROR_BUSINESS_OFFSET + 16;

	// 删除公寓失败
	public final static int ERROR_MSG_REMOVE_APARTMENT_FAIL = ERROR_BUSINESS_OFFSET + 17;

	// 增加推荐房源失败
	public final static int ERROR_MSG_ADD_RECOMMEND_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 18;

	// 修改推荐房源失败
	public final static int ERROR_MSG_MODIFY_RECOMMEND_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 19;

	// 查询推荐房源失败
	public final static int ERROR_MSG_QUERY_RECOMMEND_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 20;

	// 删除推荐房源失败
	public final static int ERROR_MSG_REMOVE_RECOMMEND_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 21;

	// 商圈查询失败
	public final static int ERROR_MSG_QUERY_AREA_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 22;

	// 不识别的房源设置类型
	public final static int ERROR_MSG_INVALID_HOUSE_SETTING_TYPE = ERROR_BUSINESS_OFFSET + 23;

	// saas房源置顶失败
	public final static int ERROR_MSG_INVALID_ISTOP_HOUSE_FAIL = ERROR_BUSINESS_OFFSET + 24;
	
	// 搜索关键词记录不存在
	public final static int ERROR_SKH_QUERY_FAIL = ERROR_BUSINESS_OFFSET + 25;
	
	// 浏览房源足迹记录保存失败
	public final static int ERROR_FMH_ADD_FAIL = ERROR_BUSINESS_OFFSET + 26;
	
	// 浏览房源足迹记录更新失败
	public final static int ERROR_FMH_UPDATE_FAIL = ERROR_BUSINESS_OFFSET + 27;
	
	// 保存订制数据失败
	public final static int ERROR_MSG_ADD_ORDER_CUSTOM_FAIL = ERROR_BUSINESS_OFFSET + 28;
	
	// 更新订制数据失败
	public final static int ERROR_MSG_UPDATE_ORDER_CUSTOM_FAIL = ERROR_BUSINESS_OFFSET + 29;
	
	// 搜索的订制数据不存在
	public final static int ERROR_MSG_SEARCH_ORDER_CUSTOM_FAIL = ERROR_BUSINESS_OFFSET + 30;
	
	// 查询中介公司活动数据失败
	public final static int ERROR_MSG_QUERY_ACTIVITY_FAIL = ERROR_BUSINESS_OFFSET + 31;
	
	// 用户未领取大礼包
	public final static int USER_OBTAIN_GIFTPACK = ERROR_BUSINESS_OFFSET + 32;

	// 创建闲鱼账户失败
    public final static int CREATE_USER_FAIL = ERROR_BUSINESS_OFFSET + 32;
	// -----------------------------百度接口新增房源信息错误码-----------------------------------------------
	// 表示数据包含错误字段
	public static final int ERROR_MSG_ADD_FIELD_ERROR = 1;

	// 表示房源重复，相同outHouseId的房源已经在线上
	public static final int ERROR_MSG_ADD_EXIST_SAME_HOUSE = 2;

	// 表示程序异常失败
	public static final int ERROR_MSG_ADD_PROG_EXCEPION = 3;

	// 表示机构签名校验失败
	public static final int ERROR_MSG_ADD_SIGN_VERIFY_FAIL = 4;


	// -----------------------------百度接口房源上下架信息错误码-----------------------------------------------
	// 表示数据字段错误
	public static final int ERROR_MSG_UPDATE_FIELD_ERROR = 1;

	// 表示修改不存在的房源
	public static final int ERROR_MSG_UPDATE_NO_EXIST_HOUSE = 2;

	// 表示程序异常失败
	public static final int ERROR_MSG_UPDATE_PROG_EXCEPION = 3;

	// 表示机构签名校验失败
	public static final int ERROR_MSG_UPDATE_SIGN_VERIFY_FAIL = 4;

	// -----------------------------百度接口房源上下架信息错误码-----------------------------------------------

	/**
	 * 执行成功后返回0
	 */
	public static final int ERRCODE_OK = 0;

	/**
	 * 该错误用于未知类型的错误，如未捕获的异常
	 */
	public static final int ERRCODE_INTERNAL_ERROR = getCommErrorCode(1);

	/**
	 * 缺少参数
	 */
	public static final int ERRCODE_LACK_PARAM = getBusiErrorCode(2);

	/**
	 * 需要登录
	 */
	public static final int ERRCODE_NEED_LOGIN = getBusiErrorCode(3);

	/**
	 * 验证码过期
	 */
	public static final int ERRCODE_CAPTCHA_EXPAIRED = getBusiErrorCode(4);

	/**
	 * 验证码错误
	 */
	public static final int ERRCODE_CAPTCHA_INVALID = getBusiErrorCode(5);

	/**
	 * 客户端收到此验证码，需弹窗提示用户强制升级
	 */
	public static final int ERRCODE_NEED_FORCE_UPDATE = getBusiErrorCode(6);

	/**
	 * 非法参数，格式不正确时抛出此异常
	 */
	public static final int ERRCODE_INVALID_PARAM = getBusiErrorCode(7);

	/**
	 * 已经提交过会分期申请
	 */
	public static final int ERRCODE_ALREADY_APPLIED_HFQ = getBusiErrorCode(8);

	/**
	 * 用户不存在
	 */
	public static final int ERRCODE_NO_SUCH_USER = getBusiErrorCode(9);

	/**
	 * 代金券不存在
	 */
	public static final int ERRCODE_NO_SUCH_VOUCHER = getBusiErrorCode(10);

	/**
	 * 调用服务接口失败
	 */
	public static final int ERRCODE_SERVICE_ERROR = getBusiErrorCode(11);

	/**
	 * 不支持的合作方
	 */
	public static final int ERRCODE_UNSUPPORT_PARTYB = getBusiErrorCode(12);

	/**
	 * 校验签名失败
	 */
	public static final int ERRCODE_VERIFY_SIGN_FAILED = getBusiErrorCode(13);

	/**
	 * 非法调用
	 */
	public static final int ERRCODE_ILLEGAL_INVOKE = getBusiErrorCode(14);

	/**
	 * 乙方接口调用失败
	 */
	public static final int ERRCODE_PARTYB_SERVICE_ERROR = getBusiErrorCode(15);

	/**
	 * 不被允许的操作
	 */
	public static final int ERRCODE_FORBIDDEN_OPER = getBusiErrorCode(16);

	/**
	 * 图片验证码请求失败
	 */
	public static final int ERRCODE_INVALID_IMG_CAPTCHA = getBusiErrorCode(17);

	/**
	 * Client ID保存失败
	 */
	public static final int ERRCODE_CLIENTID_SAVE_ERROR = getBusiErrorCode(18);

	/**
	 * 订单状态查询失败
	 */
	public static final int ERRCODE_PAY_STATUS_QUERY_FAILED = getBusiErrorCode(19);

	/**
	 * 材料保存失败
	 */
	public static final int ERRCODE_MATERIAL_SAVE_FAILED = getBusiErrorCode(20);

	/**
	 * 领取代金券失败，请稍后再试
	 */
	public static final int ERRCODE_LOTTERY_RECEIVE_FAILED = getBusiErrorCode(21);

	/**
	 * 您不能重复领取代金券
	 */
	public static final int ERRCODE_LOTTERY_REPEAT_RECEIVE = getBusiErrorCode(22);

	/**
	 * 电话号码格式有误
	 */
	public static final int ERRCODE_PHONE_FORMATE_FAILED = getBusiErrorCode(23);

	/**
	 * 抽奖失败
	 */
	public static final int ERRCODE_LOTTERY_FAILED = getBusiErrorCode(24);

	/**
	 * 该用户没有签订合同
	 */
	public static final int ERRCODE_NO_CONTRACT_FAILED = getBusiErrorCode(25);

	/**
	 * 上传文件失败
	 */
	public static final int ERRCODE_UPLOAD_FILE__FAILED = getBusiErrorCode(26);

	/**
	 * 下载文件失败
	 */
	public static final int ERRCODE_DOWNLOAD_FILE__FAILED = getBusiErrorCode(27);

	/**
	 * 用户资料已存在
	 */
	public static final int ERRCODE_USER_INFO_EXIST = getBusiErrorCode(28);

	/**
	 * 用户资料不存在
	 */
	public static final int ERRCODE_USER_INFO_UNEXIST = getBusiErrorCode(29);

	/**
	 * 获取微信授权信息失败
	 */
	public static final int ERRCODE_WXJSDK_AUTH_FAILED = getBusiErrorCode(30);

	/**
	 * 支付失败
	 */
	public static final int ERRCODE_CHARGE_FAILED = getBusiErrorCode(31);

	/**
	 * 银行卡信息有误
	 */
	public static final int ERRCODE_BANK_CARD_INFO_ERROR = getBusiErrorCode(32);

	/**
	 * 二维码失效
	 */
	public static final int ERRCODE_QR_TIMEOUT = getBusiErrorCode(33);

	/**
	 * 生成二维码失败
	 */
	public static final int ERRCODE_QR_GENERATE_FAIL = getBusiErrorCode(34);

	/**
	 * 确认合同状态失败
	 */
	public static final int ERRCODE_CONFIRM_CONTRACT_STATUS_FAIL = getBusiErrorCode(35);

	/**
	 * 解绑银行卡失败
	 */
	public static final int ERRCODE_PAY_BANKCARD_UNBAND_FAIL = getBusiErrorCode(36);

	/**
	 * 获取微信接口调用凭据失败
	 */
	public static final int ERRCODE_WX_GET_ACCESS_TOKEN_FAIL = getBusiErrorCode(37);

	/**
	 * 禁止登录
	 */
	public static final int ERRCODE_LOGIN_FORBIDDEN = getBusiErrorCode(38);

	/**
	 * 电子合同不存在
	 */
	public static final int ERRCODE_ELEC_CONTRACT_NOT_EXIST = getBusiErrorCode(39);

	/**
	 * 身份证号码不一致
	 */
	public static final int ERRCODE_USER_ID_NOT_MATCH = getBusiErrorCode(40);

	/**
	 * 转租失败
	 */
	public static final int SUBLET_FAILED = getBusiErrorCode(41);

	/**
	 * 订单相关错误
	 */
	public static final int ERRCODE_TRADE_ORDER_ERROR = getBusiErrorCode(51);


	/**
	 * 合同附属信息不存在
	 */
	public static final int CONTRACT_EXTEND_NOT_EXIST = getBusiErrorCode(42);

	/**
	 * 分期订单不存在
	 */
	public static final int NO_SUCH_SUBPAY = getBusiErrorCode(43);

	/**
	 * 用户已存在
	 */
	public static final int USER_HAVE_EXISTED = getBusiErrorCode(44);
	/**
	 * 用户存有正在生效的合同
	 */
	public static final int USER_HAVE_EFFECT_CONTRACT = getBusiErrorCode(45);

	/**
	 * 修改密码失败
	 */
	public static final int MODIFY_USER_PWD_FAIL = getBusiErrorCode(46);

	/**
	 * 绑定新手机号失败
	 */
	public static final int BIND_NEW_PHONE_FAIL = getBusiErrorCode(47);

	/**
	 * 原密码不正确
	 */
	public static final int OLD_PWD_ERROR = getBusiErrorCode(48);

	/**
	 * 新旧手机号相同
	 */
	public static final int OLDPWD_SAMEAS_NEWPWE_ERROR = getBusiErrorCode(49);

	/**
	 * 用户未实名
	 */
	public static final int NO_REAL_NAME_USER = getBusiErrorCode(50);

	/**
	 * 签约用户的身份证不存在
	 */
	public static final int ID_CARD_ERROR = getBusiErrorCode(51);

	/**
	 * 绑定的source_id已存在
	 */
	public static final int SOURSE_ID_EXIST_ERROR = getBusiErrorCode(52);

	/**
	 * 支付渠道不一致
	 */
	public static final int DIFF_PAY_CHANNEL_ERROR = getBusiErrorCode(53);

	/**
	 * 暂时无法解绑银行卡
	 */
	public static final int NO_UNBIND_CARD_ERROR = getBusiErrorCode(54);

	/**
	 * 用户未设置过密码
	 */
	public static final int NO_USER_PWD_SET = getBusiErrorCode(55);

	/**
	 * 已申请过退租
	 */
	public static final int HAVE_REFUND_APPLY = getBusiErrorCode(56);

	/**
	 * 逾期不能申请退租
	 */
	public static final int OUTOF_DAY_LIMIT_REFUND_APPLY = getBusiErrorCode(57);

	/**
	 * 5天内不能申请退租
	 */
	public static final int DAY_LIMIT_REFUND_APPLY = getBusiErrorCode(58);

	/**
	 * 未发起退租记录
	 */
	public static final int _REFUND_APPLY = getBusiErrorCode(59);

	public static final int USERNAME_OR_PWD_ERROR = getBusiErrorCode(60);

	public static final int BINDCARD_ERROR = getBusiErrorCode(61);
	
	public static final int CONFRIM_BINDCARD_ERROR = getBusiErrorCode(62);
	
	/**
	 * 智能门锁手势密码保存失败
	 */
	public static final int SAVE_GESTURE_PWD_ERROR = getBusiErrorCode(63);

	/**
	 * 智能门锁手势密码更新失败
	 */
	public static final int UPDATE_GESTURE_PWD_ERROR = getBusiErrorCode(65);

	/**
	 * 智能门锁手势密码不存在
	 */
	public static final int GESTURE_PWD_UNEXIST = getBusiErrorCode(66);

	/**
	 * 传入手机号对应用户与当前用户不匹配
	 */
	public static final int USER_NOT_EXIST = getBusiErrorCode(67);

	/**
	 * 未获取到门锁钥匙
	 */
	public static final int LOCK_NOT_EXIST = getBusiErrorCode(68);

	/**
	 * 未获取到临时密码
	 */
	public static final int DYNAMIC_NOT_EXIST = getBusiErrorCode(69);
	
	public static final int USER_FAIL_REGISTER = getBusiErrorCode(61);
	
	/**
     * 订单状态为支付中或支付完成，则不允许再次支付的提示信息
     */
    public static final int REPEAT_PAY_ERROR = getBusiErrorCode(62);

	private static final int getCommErrorCode(final int busiErrCode) {
		return ERROR_HUIZHOF_OFFSET + busiErrCode;
	}

	private static final int getBusiErrorCode(final int busiErrCode) {
		return ERROR_HUIZHOF_OFFSET + busiErrCode;
	}
}
