/**
 * Project Name: hzf_platform 
 * File Name: Constants.java 
 * Package Name: com.huifenqi.hzf_platform.context 
 * Date: 2016年4月27日下午12:21:09 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.context;

import com.huifenqi.hzf_platform.utils.StringUtil;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.Map.Entry;

/**
 * ClassName: Constants date: 2016年4月27日 下午12:21:09 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class Constants {
	/**
	 * 缓存key的前缀
	 */
	public static final String CACHE_KEY_PREFIX = "usercomm";
	
	public static class imageUtil {
        //房源详情默认图片
        public static final String HOUUSE_DETAIL_IMG_DEFALUE = "http://hzf-image.oss-cn-beijing.aliyuncs.com/house_img_default/house_detail.png";

    }
	
	public static class HouseOptTypeUtil {

        public static final int HOUSE_OPT_ADD = 1;
        public static final int HOUSE_OPT_UPDATE = 2;
        public static final int HOUSE_OPT_DELETE = 3; 
        public static final int ROOM_OPT_ADD = 4;
        public static final int ROOM_OPT_UPDATE = 5;
        public static final int ROOM_OPT_DELETE = 6;  

    }
	
	/**
	 * 通用参数
	 */
	public static class Common {


		// 未删除
		public static final int STATE_IS_DELETE_NO = 0;

		// 程序删除
		public static final int STATE_IS_DELETE_YES = 1;
		
		// 手动删除
		public static final int STATE_IS_DELETE_AR = 2;

		// HTTP状态码
		public static final int HTTP_STATUS_CODE_OK = 200;

		// 纬度最小值
		public static final double LATITUDE_MIN = -90.0;

		// 纬度最大值
		public static final double LATITUDE_MAX = 90.0;

		// 经度最小值
		public static final double LONGITUDE_MIN = -180.0;

		// 经度最大值
		public static final double LONGITUDE_MAX = 180.0;

		// 查询限制数量
		public static final int QUERY_LIMIT_ALL = -1;
		
		// 房源描述
		public static final String houseDesc = "好房不等人，立即致电获取最新房态";

	}
	
	public static class PlatformStatus {

		// 0 默认两者都允许，
		public static final int ALLOW_BOTH = 0;
		// 1 禁止solr查，
		public static final int DENY_SOLR = 1;
		// 2 程序不审核，
		public static final int DENY_APPROVE = 2;
		// 3 两者都不允许
		public static final int DENY_BOTH = 3;

	}

	/**
	 * 房源基本信息
	 */
	public static class HouseBase {
		// ------------------saas过滤公司编号------------------------
		// 公司ID10580
		public static final String COMPANY_ID_10580 = "10580";

		// 公司ID10907
		public static final String COMPANY_ID_10907 = "10907";
		
		// 店铺页
		public static final String COMPANY_SAAS_SHOP = "SaaS平台";

		// ------------------房源标识状态------------------------
		// 房源标识:抓取
		public static final int SOURCE_FALG_CRAWL = 1;

		// 房源标识:中介
		public static final int SOURCE_FALG_API = 0;

		// ------------------房源上架状态------------------------
		// 房源上架状态:未上架
		public static final int IS_SALE_NO = 0;

		// 房源上架状态:已上架
		public static final int IS_SALE_YES = 1;

		// ------------------房源采集状态------------------------
		// 未采集
		public static final int IS_RUN_NO = 0;

		// 已采集
		public static final int IS_RUN_YES = 1;

		// ------------------房源状态------------------------
		// 房源状态:草稿
		public static final int STATUS_DRAFT = 0;

		// 房源状态:待出租
		public static final int STATUS_NEW = 1;

//		// 房源状态:待审核
//		public static final int STATUS_PRE_VERIFY = 2;
//
//		// 房源状态:审核通过
//		public static final int STATUS_VERIFY_SUCCESS = 3;
//
//		// 房源状态:审核不通过
//		public static final int STATUS_VERIFY_FAIL = 4;

		// 房源状态:已出租
		public static final int STATUS_RENT = 5;

		// 房源状态:部分出租 (房间没有该状态)
		public static final int STATUS_PARTLY_RENT = 6;

//		// 房源状态:程序审核不通过
//		public static final int SYS_APPROVE_FAIL = 7;

		/**
		 * 获取状态描述
		 * 
		 * @param status
		 * @return
		 */
		public static String getStatusDesc(int status) {
			String name = null;
			switch (status) {
			case STATUS_DRAFT:
				name = "草稿";
				break;
			case STATUS_NEW:
				name = "新房源";
				break;
//			case STATUS_PRE_VERIFY:
//				name = "待审核";
//				break;
//			case STATUS_VERIFY_SUCCESS:
//				name = "审核通过";
//				break;
//			case STATUS_VERIFY_FAIL:
//				name = "审核不通过";
//				break;
			case STATUS_RENT:
				name = "已出租";
				break;
			case STATUS_PARTLY_RENT:
				name = "部分出租";
				break;

			default:
				name = "未知";
				break;
			}

			return name;
		}

		/**
		 * 获取状态描述
		 * 
		 * @param status
		 * @return
		 */
		public static String getSimplifiedStatusDesc(int status) {
			String name = null;
			switch (status) {
			case STATUS_DRAFT:
			case STATUS_NEW:
//			case STATUS_PRE_VERIFY:
//			case STATUS_VERIFY_SUCCESS:
//			case STATUS_VERIFY_FAIL:
			case STATUS_PARTLY_RENT:
				name = "待出租";
				break;
			case STATUS_RENT:
				name = "已出租";
				break;

			default:
				name = "未知";
				break;
			}

			return name;
		}

		// ------------------房源发布状态------------------------
		// 房源发布状态:未上架
		public static final int PUBLISH_STATUS_NOT_ON_SALE = 0;

		// 房源发布状态:待出租
		public static final int PUBLISH_STATUS_PRE_RENT = 2;

		// 房源发布状态:已出租
		public static final int PUBLISH_STATUS_RENT = 3;

		// ------------------房源朝向------------------------
		// 朝向:默认值
		public static final int ORIENTATION_INIT = 0;
		
		// 朝向:东
		public static final int ORIENTATION_EAST = 10001;

		// 朝向:西
		public static final int ORIENTATION_WEST = 10002;

		// 朝向:南
		public static final int ORIENTATION_SOUTH = 10003;

		// 朝向:北
		public static final int ORIENTATION_NORTH = 10004;

		// 朝向:西南
		public static final int ORIENTATION_SOUTH_WEST = 10023;

		// 朝向:西北
		public static final int ORIENTATION_NORTH_WEST = 10024;

		// 朝向:东北
		public static final int ORIENTATION_NORTH_EAST = 10014;

		// 朝向:东南
		public static final int ORIENTATION_SOUTH_EAST = 10013;

		// 朝向:南北
		public static final int ORIENTATION_NORTH_SOUTH = 10034;

		// 朝向:东西
		public static final int ORIENTATION_EAST_WEST = 10012;

		public static String getOrentationName(int orentation) {
			String name = null;
			switch (orentation) {
			case ORIENTATION_EAST:
				name = "东";
				break;
			case ORIENTATION_WEST:
				name = "西";
				break;
			case ORIENTATION_SOUTH:
				name = "南";
				break;
			case ORIENTATION_NORTH:
				name = "北";
				break;
			case ORIENTATION_SOUTH_WEST:
				name = "西南";
				break;
			case ORIENTATION_NORTH_WEST:
				name = "西北";
				break;
			case ORIENTATION_NORTH_EAST:
				name = "东北";
				break;
			case ORIENTATION_SOUTH_EAST:
				name = "东南";
				break;
			case ORIENTATION_NORTH_SOUTH:
				name = "南北";
				break;
			case ORIENTATION_EAST_WEST:
				name = "东西";
				break;

			default:
				name = "";
				break;
			}

			return name;
		}

		// ------------------楼栋类型------------------------
		// 初始值
		public static final int BUILDING_TYPE_INIT = 0;

		// 板楼
		public static final int BUILDING_TYPE_SLAB = 1;

		// 塔楼
		public static final int BUILDING_TYPE_TOWER = 2;

		// 板塔结合
		public static final int BUILDING_TYPE_SLAB_AND_TOWER = 3;

		// 独栋
		public static final int BUILDING_TYPE_SINGLE = 4;

		// 联排
		public static final int BUILDING_TYPE_ROW = 5;

		// 叠拼
		public static final int BUILDING_TYPE_OVERLAY = 6;

		// ------------------房源用途------------------------
		// 居住
		public static final int HOUSE_USE_RESIDENT = 1;

		// 写字楼
		public static final int HOUSE_USE_OFFICE = 2;

		// 商铺
		public static final int HOUSE_USE_SHOP = 3;

		// ------------------装修类型------------------------
		// 初始值
		public static final int DECORATION_INIT = 0;

		// 精装
		public static final int DECORATION_FINE = 1;

		// 简装
		public static final int DECORATION_BRIEF = 2;

		// 毛坯
		public static final int DECORATION_ROUGH = 3;

		// 老旧
		public static final int DECORATION_OLD = 4;

		// 豪装
		public static final int DECORATION_LUXURY = 5;

		// 中装
		public static final int DECORATION_MEDIUM = 6;

		// 普装
		public static final int DECORATION_NORMAL = 7;

		/**
		 * 获取装饰名称
		 * 
		 * @param decoration
		 * @return
		 */
		public static String getDecorationName(int decoration) {
			String name = null;
			switch (decoration) {
			case DECORATION_FINE:
				name = "精装";
				break;
			case DECORATION_BRIEF:
				name = "简装";
				break;
			case DECORATION_ROUGH:
				name = "毛坯";
				break;
			case DECORATION_OLD:
				name = "老旧";
				break;
			case DECORATION_LUXURY:
				name = "豪装";
				break;
			case DECORATION_MEDIUM:
				name = "中装";
				break;
			case DECORATION_NORMAL:
				name = "普装";
				break;

			default:
				name = "未知";
				break;
			}

			return name;
		}

		// ------------------性别------------------------
		// 初始值
		public static final int GENDER_INIT = 0;

		// 男性
		public static final int GENDER_MALE = 1;

		// 女性
		public static final int GENDER_FEMALE = 2;

		// ------------------卫生间/阳台 是否独立------------------------
		// 独立
		public static final int INDEPENDENT = 1;

		// 非独立
		public static final int NOT_INDEPENDENT = 0;

		// ------------------家财险------------------------
		// 有保险
		public static final int INSURANCE_EXIST = 1;

		// 无保险
		public static final int INSURANCE_NOT_EXIST = 0;

		// ------------------是否有钥匙------------------------
		// 无钥匙
		public static final int HAS_KEY_NO = 0;

		// 有钥匙
		public static final int HAS_KEY_YES = 1;

		// ------------------字段限制大小-----------------
		// 公司ID最大长度
		public static final int HOUSE_COMPANY_ID_LENGTH_MAX = 36;

		// 公司名最大长度
		public static final int HOUSE_COMPANY_NAME_LENGTH_MAX = 50;

		// 电话号码最大长度
		public static final int HOUSE_PHONE_LENGTH_MAX = 16;

		// 经纪人姓名最大长度
		public static final int HOUSE_AGENCY_NAME_LENGTH_MAX = 30;

		// 经纪人介绍最大长度
		public static final int HOUSE_AGENCY_INTRODUCE_LENGTH_MAX = 255;

		// 经纪人头像最大长度
		public static final int HOUSE_AGENCY_AVATAR_LENGTH_MAX = 255;

		// 面积最小值
		public static final double HOUSE_AREA_MIN = 8;

	}

	/**
	 * 房源详情信息
	 */
	public static class HouseDetail {

		// 分散式（普通）
		public static final int HOUSE_TYPE_NORMAL = 0;
		// 集中式
		public static final int HOUSE_TYPE_CENTRAL = 1;

		// ------------------房屋出租类型-----------------
		// 全部
		public static final int RENT_TYPE_ALL = -1;
		
		// 全部
		public static final String COMPANY_TYPE_ALL = "-1";

		// 合租
		public static final int RENT_TYPE_SHARE = 0;

		// 整租
		public static final int RENT_TYPE_ENTIRE = 1;

		// 整分皆可
		public static final int RENT_TYPE_BOTH = 2;

		public static String getRentTypeName(int rentType) {
			String rentTypeName = null;
			switch (rentType) {
			case RENT_TYPE_SHARE:
				rentTypeName = "合租";
				break;
			case RENT_TYPE_ENTIRE:
				rentTypeName = "整租";
				break;
			case RENT_TYPE_BOTH:
				rentTypeName = "整分皆可";
				break;
			case RENT_TYPE_ALL:
				rentTypeName = "全部";
				break;
			default:
				rentTypeName = "未知";
				break;
			}

			return rentTypeName;
		}

		// 表示数据未经过审核
		public static final int  NOT_APPROVE = 0;

		// 表示数据已审核
		public static final int HAS_APPROVED = 1;

		// 表示数据未经采集
		public static final int HOUSE_UNRUN = 0;

		// 表示数据已经采集
		public static final int HOUSE_RUNED = 1;

		// 表示数据采集失败
		public static final int HOUSE_FAILD = -1;

		//收藏标识
		public static final int COLLECT_FLAG = 1;
		
		//支持月付
		public static final String PAY_TYPE = "4";
		
		// ------------------房屋标签-----------------
//		// 首次发布
//		public static final int HOUSE_TAG_PUB_FIRST_TIME = 1;
//
//		// 南向
//		public static final int HOUSE_TAG_ORIENTATION_SOUTH = 2;
//
//		// 独立卫生间
//		public static final int HOUSE_TAG_INDEPENDENT_TOILET = 3;
//
//		// 独立阳台
//		public static final int HOUSE_TAG_INDEPENDENT_BALCONY = 4;
//
//		// 精装修
//		public static final int HOUSE_TAG_DECORATION_FINE = 5;
//
//		// 地铁周边
//		public static final int HOUSE_TAG_CLOSE_TO_SUBWAY = 6;
//
//		// 设施齐全
//		public static final int HOUSE_TAG_FACILITIES_WELL_EQUIPPED = 7;
//
//		// 集中供暖
////		public static final int HOUSE_TAG_CENTRAL_HEATING = 8;
//
//		// 押一付一
//		public static final int HOUSE_TAG_PERIOD_MONTH_ONE = 9;
//
//		// 月付
//		public static final int HOUSE_TAG_COMPANY_PAY_STATUS = 12;

		// ------------------字段限制大小-----------------
		// 小区名长度最大值
		public static final int HOUSE_COMMUNITY_NAME_LENGTH_MAX = 50;

		// 楼栋号最大长度
		public static final int HOUSE_BUILDING_NO_LENGTH_MAX = 50;

		// 单元号最大长度
		public static final int HOUSE_UNIT_NO_LENGTH_MAX = 50;

		// 门牌号最大长度
		public static final int HOUSE_HOUSE_NO_LENGTH_MAX = 40;

		// 楼栋名最大长度
		public static final int HOUSE_BUILDING_NAME_LENGTH_MAX = 30;

		// 当前楼层最大长度
		public static final int HOUSE_FLOW_NO_LENGTH_MAX = 30;

		// 总楼层最大长度
		public static final int HOUSE_FLOW_TOTAL_LENGTH_MAX = 30;

		// 商圈名最大长度
		public static final int HOUSE_BIZ_NAME_LENGTH_MAX = 20;

		// 房源描述最大长度
		public static final int HOUSE_COMMENT_LENGTH_MAX = 1024;

		// 房源面积最小值
		public static final int HOUSE_AREA_MIN = 5;

		// 房间编码
		public static final int HOUSE_ROOMCODE_LENGTH_MAX = 5;

		// 房间ID
		public static final int HOUSE_HOUSEID_LENGTH_MAX = 45;

		// 房间名称
		public static final int HOUSE_ROOMNAME_LENGTH_MAX = 30;

		// 集中式房源编号
		public static final int HOUSE_FOCUSCODE_LENGTH_MAX = 30;
	}

	/**
	 * 房源配置信息
	 */
	public static class HouseSetting {
		// ------------------配置位置------------------------
		// 卧室
		public static final int POSITION_BEDROOM = 1;

		// 公共区域
		public static final int POSITION_PUBLIC = 2;

		// ------------------配置是否完成------------------------
		// 未完成
		public static final int IS_COMPLETE_NO = 0;

		// 已完成
		public static final int IS_COMPLETE_YES = 1;

		// ------------------配置分类------------------------
		// 主要配置
		public static final int CATEGORY_PRIMARY = 0; // TODO 确认常量

		// 次要配置
		public static final int CATEGORY_SECONDARY = 1; // TODO 确认常量

		// ------------------配置类型------------------------
		// 家具
		public static final int SETTING_TYPE_FURNITURE = 1;

		// 家电
		public static final int SETTING_TYPE_ELECTRIC = 2;

		// 家居
		public static final int SETTING_TYPE_HOUSEHOLD = 3;

		// 其他
		public static final int SETTING_TYPE_ADDON = 4;

		// ------------------配置Id------------------------
		// 床
		public static final int SETTING_CODE_FURNITURE_BED = 1;

		// 沙发
		public static final int SETTING_CODE_FURNITURE_SOFA = 2;

		// 电脑桌
		public static final int SETTING_CODE_FURNITURE_TABLE = 3;

		// 衣柜
		public static final int SETTING_CODE_FURNITURE_WARDROBE = 4;

		// 椅子
		public static final int SETTING_CODE_FURNITURE_CHAIR = 5;

		// 电视
		public static final int SETTING_CODE_ELECTRIC_TV = 1;

		// 冰箱
		public static final int SETTING_CODE_ELECTRIC_FRIDGE = 2;

		// 空调
		public static final int SETTING_CODE_ELECTRIC_AC = 3;

		// 洗衣机
		public static final int SETTING_CODE_ELECTRIC_WASHER = 4;

		// 微波炉
		public static final int SETTING_CODE_ELECTRIC_MICROWAVE_OVEN = 5;

		// 电水壶
		public static final int SETTING_CODE_ELECTRIC_KETTLE = 6;

		// 窗帘
		public static final int SETTING_CODE_HOUSEHOLD_CURTAIN = 1;

		// 被褥
		public static final int SETTING_CODE_HOUSEHOLD_MATTRESS = 2;

		// 花瓶
		public static final int SETTING_CODE_HOUSEHOLD_VASE = 3;

		// 台灯
		public static final int SETTING_CODE_HOUSEHOLD_LAMP = 4;

		// 装饰画
		public static final int SETTING_CODE_HOUSEHOLD_DECORATION = 5;

		// WIFI
		public static final int SETTING_CODE_ADDON_WIFI = 1;

		// ------------------配置 key------------------------
		// 床
		public static final String SETTING_KEY_FURNITURE_BED = "bed";

		// 沙发
		public static final String SETTING_KEY_FURNITURE_SOFA = "sofa";

		// 电脑桌
		public static final String SETTING_KEY_FURNITURE_TABLE = "table";

		// 衣柜
		public static final String SETTING_KEY_FURNITURE_WARDROBE = "wardrobe";

		// 椅子
		public static final String SETTING_KEY_FURNITURE_CHAIR = "chair";

		// 电视
		public static final String SETTING_KEY_ELECTRIC_TV = "tv";

		// 冰箱
		public static final String SETTING_KEY_ELECTRIC_FRIDGE = "fridge";

		// 空调
		public static final String SETTING_KEY_ELECTRIC_AC = "ac";

		// 洗衣机
		public static final String SETTING_KEY_ELECTRIC_WASHER = "washer";

		// 微波炉
		public static final String SETTING_KEY_ELECTRIC_MICROWAVE_OVEN = "microwaveoven";

		// 电水壶
		public static final String SETTING_KEY_ELECTRIC_KETTLE = "kettle";

		// 窗帘
		public static final String SETTING_KEY_HOUSEHOLD_CURTAIN = "curtain";

		// 被褥
		public static final String SETTING_KEY_HOUSEHOLD_MATTRESS = "mattress";

		// 花瓶
		public static final String SETTING_KEY_HOUSEHOLD_VASE = "vase";

		// 台灯
		public static final String SETTING_KEY_HOUSEHOLD_LAMP = "lamp";

		// 装饰画
		public static final String SETTING_KEY_HOUSEHOLD_DECORATION = "decoration";

		// WIFI
		public static final String SETTING_KEY_ADDON_WIFI = "wifi";

		public static final int SETTING_TYPE_UNRECOGNIZED = -1; // 未知设置类型
		public static final int SETTING_CODE_UNRECOGNIZED = -1; // 未知设置Id

		private static Map<Pair<Integer, Integer>, String> settingMap = new HashMap<Pair<Integer, Integer>, String>();
		private static List<Integer> primarySettingTypeList = new ArrayList<Integer>();
		private static List<Integer> secondarySettingTypeList = new ArrayList<Integer>();

		static {
			init();
		}

		private static void init() {
			putValue(SETTING_KEY_FURNITURE_BED, SETTING_TYPE_FURNITURE, SETTING_CODE_FURNITURE_BED);
			putValue(SETTING_KEY_FURNITURE_SOFA, SETTING_TYPE_FURNITURE, SETTING_CODE_FURNITURE_SOFA);
			putValue(SETTING_KEY_FURNITURE_TABLE, SETTING_TYPE_FURNITURE, SETTING_CODE_FURNITURE_TABLE);
			putValue(SETTING_KEY_FURNITURE_WARDROBE, SETTING_TYPE_FURNITURE, SETTING_CODE_FURNITURE_WARDROBE);
			putValue(SETTING_KEY_FURNITURE_CHAIR, SETTING_TYPE_FURNITURE, SETTING_CODE_FURNITURE_CHAIR);

			putValue(SETTING_KEY_ELECTRIC_TV, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_TV);
			putValue(SETTING_KEY_ELECTRIC_FRIDGE, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_FRIDGE);
			putValue(SETTING_KEY_ELECTRIC_AC, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_AC);
			putValue(SETTING_KEY_ELECTRIC_WASHER, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_WASHER);
			putValue(SETTING_KEY_ELECTRIC_MICROWAVE_OVEN, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_MICROWAVE_OVEN);
			putValue(SETTING_KEY_ELECTRIC_KETTLE, SETTING_TYPE_ELECTRIC, SETTING_CODE_ELECTRIC_KETTLE);

			putValue(SETTING_KEY_HOUSEHOLD_CURTAIN, SETTING_TYPE_HOUSEHOLD, SETTING_CODE_HOUSEHOLD_CURTAIN);
			putValue(SETTING_KEY_HOUSEHOLD_MATTRESS, SETTING_TYPE_HOUSEHOLD, SETTING_CODE_HOUSEHOLD_MATTRESS);
			putValue(SETTING_KEY_HOUSEHOLD_VASE, SETTING_TYPE_HOUSEHOLD, SETTING_CODE_HOUSEHOLD_VASE);
			putValue(SETTING_KEY_HOUSEHOLD_LAMP, SETTING_TYPE_HOUSEHOLD, SETTING_CODE_HOUSEHOLD_LAMP);
			putValue(SETTING_KEY_HOUSEHOLD_DECORATION, SETTING_TYPE_HOUSEHOLD, SETTING_CODE_HOUSEHOLD_DECORATION);

			putValue(SETTING_KEY_ADDON_WIFI, SETTING_TYPE_ADDON, SETTING_CODE_ADDON_WIFI);

			primarySettingTypeList.add(SETTING_TYPE_FURNITURE);
			primarySettingTypeList.add(SETTING_TYPE_ELECTRIC);
			primarySettingTypeList.add(SETTING_TYPE_HOUSEHOLD);

			secondarySettingTypeList.add(SETTING_TYPE_ADDON);
		}

		private static void putValue(String key, int type, int code) {
			Pair<Integer, Integer> pair = Pair.of(type, code);
			settingMap.put(pair, key);
		}

		/**
		 * 获取设置key
		 * 
		 * @param type
		 * @param code
		 * @return
		 */
		public static String getSettingKey(int type, int code) {
			Pair<Integer, Integer> pair = Pair.of(type, code);
			String key = settingMap.get(pair);
			return key;
		}

		/**
		 * 获取设置类型
		 * 
		 * @param settingKey
		 * @return
		 */
		public static int getSettingType(String settingKey) {

			int settingType = SETTING_TYPE_UNRECOGNIZED;
			if (StringUtil.isEmpty(settingKey)) {
				return settingType;
			}

			Set<Entry<Pair<Integer, Integer>, String>> entrySet = settingMap.entrySet();
			Iterator<Entry<Pair<Integer, Integer>, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<Pair<Integer, Integer>, String> next = iterator.next();
				Pair<Integer, Integer> key = next.getKey();
				Integer type = key.getLeft();
				String value = next.getValue();
				if (settingKey.equalsIgnoreCase(value)) {
					settingType = type;
					break;
				}
			}

			return settingType;
		}

		/**
		 * 获取设置Code
		 * 
		 * @param settingKey
		 * @return
		 */
		public static int getSettingCode(String settingKey) {
			int settingCode = SETTING_CODE_UNRECOGNIZED;
			if (StringUtil.isEmpty(settingKey)) {
				return settingCode;
			}

			Set<Entry<Pair<Integer, Integer>, String>> entrySet = settingMap.entrySet();
			Iterator<Entry<Pair<Integer, Integer>, String>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<Pair<Integer, Integer>, String> next = iterator.next();
				Pair<Integer, Integer> key = next.getKey();
				Integer code = key.getRight();
				String value = next.getValue();
				if (settingKey.equalsIgnoreCase(value)) {
					settingCode = code;
					break;
				}
			}

			return settingCode;
		}

		public static List<Integer> getPrimarySettingTypes() {
			return new ArrayList<Integer>(primarySettingTypeList);
		}

		public static List<Integer> getSecondarySettingTypes() {
			return new ArrayList<Integer>(secondarySettingTypeList);
		}

		public static List<String> getPrimarySettingKeys() {
			// TODO 优化本方法
			List<String> list = new ArrayList<String>();
			Set<Pair<Integer, Integer>> keySet = settingMap.keySet();
			Iterator<Pair<Integer, Integer>> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				Pair<Integer, Integer> next = iterator.next();
				Integer type = next.getLeft();
				if (primarySettingTypeList.contains(type)) {
					String key = settingMap.get(next);
					if (StringUtil.isNotBlank(key)) {
						list.add(key);
					}
				}
			}
			return list;
		}

		public static List<String> getSecondarySettingKeys() {
			// TODO 优化本方法
			List<String> list = new ArrayList<String>();
			Set<Pair<Integer, Integer>> keySet = settingMap.keySet();
			Iterator<Pair<Integer, Integer>> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				Pair<Integer, Integer> next = iterator.next();
				Integer type = next.getLeft();
				if (secondarySettingTypeList.contains(type)) {
					String key = settingMap.get(next);
					if (StringUtil.isNotBlank(key)) {
						list.add(key);
					}
				}
			}
			return list;
		}

	}

	/**
	 * 房源图片信息
	 */
	public static class HousePicture {

		// ------------------配置位置------------------------
		// 身份证正面
		public static final int PICTURE_TYPE_IDCARD_FRONT = 1;

		// 身份证背面
		public static final int PICTURE_TYPE_IDCARD_BACK = 2;

		// 房屋产权证照片
		public static final int PICTURE_TYPE_OWNERSHIP_CERTIFICATE = 3;

		// 业主图片
		public static final int PICTURE_TYPE_OWNER = 4;

		// 租客图片
		public static final int PICTURE_TYPE_TENANT = 5;

		// 户型图
		public static final int PICTURE_TYPE_RENDERING = 6;

		// ------------------是否为首图------------------------
		// 非首图
		public static final int IS_DEFAULT_NO = 0;

		// 首图
		public static final int IS_DEFAULT_YES = 1;

		// ------------------字段限制大小-----------------
		// 图片最大个数
		public static final int HOUSE_IMGS_MAX = 30;

	}

	/**
	 * 房间信息
	 */
	public static class RoomBase {

		// ------------------房间类型------------------------
		// 主卧
//		public static final int ROOM_TYPE_MASTER = 1;
//
//		// 次卧
//		public static final int ROOM_TYPE_SECONDARY = 10;
//
//		// 优化间
//		public static final int ROOM_TYPE_OPTIMIZED = 20;

//		public static String getRoomType(int roomType) {
//			String roomTypeName = null;
//			switch (roomType) {
//			case ROOM_TYPE_MASTER:
//				roomTypeName = "主卧";
//				break;
//			case ROOM_TYPE_SECONDARY:
//				roomTypeName = "次卧";
//				break;
//			case ROOM_TYPE_OPTIMIZED:
//				roomTypeName = "优化间";
//				break;
//
//			default:
//				roomTypeName = "未知";
//				break;
//			}
//
//			return roomTypeName;
//		}

		// ------------------字段限制大小----------------------------
		// 房间描述最大长度
		public static final int ROOM_COMMENT_LENGTH_MAX = 1024;

		// 房间名称最大长度
		public static final int ROOM_NAME_LENGTH_MAX = 30;

		// 房间面积最小值
		public static final int ROOM_AREA_MIN = 3;
	}

	// 投诉
	public static class Complaint {

		// ------------------投诉是否存在------------------------
		// 投诉存在
		public static final int COMPLAINT_EXIST = 1;

		// 投诉不存在
		public static final int COMPLAINT_NOT_EXIST = 0;

		// ------------------投诉类型------------------------
		// 虚假房源
		public static final int COMPLAINT_TYPE_FAKE_HOUSE = 1;

		// 房源信息有误
		public static final int COMPLAINT_TYPE_INCORRECT_INFO = 2;

		// 服务态度差
		public static final int COMPLAINT_TYPE_POOR_ATTITUDE = 3;

		// 已出租
		public static final int COMPLAINT_TYPE_RENT = 4;

		public static String getComplaintType(int complaintType) {
			String complaintTypeName = null;
			switch (complaintType) {
			case COMPLAINT_TYPE_FAKE_HOUSE:
				complaintTypeName = "虚假房源";
				break;
			case COMPLAINT_TYPE_INCORRECT_INFO:
				complaintTypeName = "房源信息有误";
				break;
			case COMPLAINT_TYPE_POOR_ATTITUDE:
				complaintTypeName = "服务态度差";
				break;
			case COMPLAINT_TYPE_RENT:
				complaintTypeName = "已出租";
				break;

			default:
				complaintTypeName = "未知";
				break;
			}

			return complaintTypeName;
		}

		// ------------------字段限制大小----------------------------
		// uid最大长度
		public static final int COMPLAINT_UID_LENGTH_MAX = 255;

		// 投诉详情最大长度
		public static final int COMPLAINT_COMMENT_LENGTH_MAX = 1024;
	}

	// 地区状态
	public static class AreaStatus {

		// 普通
		public static final int AREA_STATUS_COMM = 1;

		// 搜索热点
		public static final int AREA_STATUS_HOT = 2;

	}

	// 地铁状态
	public static class SubwayStatus {

		// 开通
		public static final int OPEN_STATUS_OPEN = 1;

		// 未开通
		public static final int OPEN_STATUS_NOT_OPEN = 0;

	}

	// 行政区状态
	public static class DistrictStatus {

		// 可提供房源
		public static final int OPEN_STATUS_OPEN = 1;

		// 不提供房源
		public static final int OPEN_STATUS_NOT_OPEN = 0;

	}
	
	// 行政区状态
	public static class CommunityStatus {

		//已收集坐标
		public static final int OPEN_YES_FLAG = 1;

		//未收集坐标
		public static final int OPEN_NOT_FLAG = 0;

	}
	
	// 城市状态
	public static class CityStatus {

		// 可提供房源
		public static final int OPEN_STATUS_OPEN = 1;

		// 不提供房源
		public static final int OPEN_STATUS_NOT_OPEN = 0;
		
		// 有地铁
		public static final int HAS_SUBWAY = 1;
		
		// 无地铁
		public static final int NO_SUBWAY = 0;

	}
	
	// 通勤配置
	public static class CommuteMapConfig {

		//可展示
		public static final int OPEN_YES_FLAG = 1;

		//不展示
		public static final int OPEN_NOT_FLAG = 0;
		
		//分钟
		public static final int  MINUTE_60= 60;
		
		//分钟
		public static final int  COMMUTE_ID_INIT= 0;
		
		//分钟
		public static final int  COMMUTE_ID_ISDEFAULT= 1;

		//公交
		public static final String TYPE_BUS_CODE = "1";
		
		//自驾
		public static final String TYPE_DRIVER_CODE = "2";
		
		//骑行
		public static final String TYPE_RIDING_CODE = "3";
		//步行
		public static final String TYPE_WALK_CODE = "4";
		
		//公交
		public static final String TYPE_BUS_NAME = "公交";
		
		//自驾
		public static final String TYPE_DRIVER_NAME = "自驾";
		
		//骑行
		public static final String TYPE_RIDING_NAME = "骑行";
		//步行
		public static final String TYPE_WALK_NAME = "步行";
	}
	
	
	// 下架公司配置
	public static class CompanyOffConfig {

		//下架公司
		public static final int OPEN_YES_STATUS = 1;

		//正常展示
		public static final int OPEN_NOT_STATUS = 0;
	
	}

	
	//理想生活圈
	public static class IdealrRentConfig {
		//group 字段
		public static final String COMPANY_NAME = "companyName";
		
		//循环次数
		public static final int DEFAULT_SIZE = 30;
	
	}
	
	//房源列表公司打散
	public static class HouseListConfig {
		//group 字段
		public static final String COMPANY_NAME = "companyName";
		
		//循环次数
		public static final int FOREACH_COUNT = 10;
		
		//展示数量
		public static final int RESULT_COUNT_HOUSELIST = 100;
		
		//展示数量
		public static final int RESULT_COUNT_SEARCH = 10;
		
		//月付房源展示数量
		public static final int MONTHLY_PAY_HOUSE_COUNT = 15;
		
		//超时时间 毫秒
		public static final int REDIS_TIME_OUT = 600000;
	
	}
	
	
	// 公寓状态
	public static class ApartmentStatus {

		// 普通
		public static final int STATUS_COMM = 1;

		// 热门
		public static final int STATUS_HOT = 2;

		// ------------------公寓类型------------------------
		// 公寓
		public static final int TYPE_APARTMENT = 1;

		// 商圈
		public static final int TYPE_AREA = 2;

		public static String getTypeDesc(int type) {
			String typeDesc = null;
			switch (type) {
			case TYPE_APARTMENT:
				typeDesc = "公寓";
				break;
			case TYPE_AREA:
				typeDesc = "商圈";
				break;

			default:
				typeDesc = "未知";
				break;
			}

			return typeDesc;
		}

	}

	public static class Search {

		// ------------------排序字段------------------------
		// 价格
		public static final String ORDER_TYPE_PRICE = "price";

		// 价格
		public static final String ORDER_TYPE_PRICE_Zero = "priceZero";
		// 面积
		public static final String ORDER_TYPE_AREA = "area";

		// 发布日期
		public static final String ORDER_TYPE_PUBDATE = "pubDate";

		public static boolean containsOrderType(String orderType) {
			if (orderType == null) {
				return false;
			}
			if (orderType.equals(ORDER_TYPE_PRICE) || orderType.equals(ORDER_TYPE_AREA)
					|| orderType.equals(ORDER_TYPE_PUBDATE)) {
				return true;
			}
			return false;
		}

		// ------------------排序类型------------------------
		// 降序
		public static final String ORDER_DESC = "desc";

		// 升序
		public static final String ORDER_ASC = "asc";

		public static boolean containsOrder(String order) {
			if (order == null) {
				return false;
			}
			if (order.equals(ORDER_DESC) || order.equals(ORDER_ASC)) {
				return true;
			}
			return false;
		}

	}

	public static class SolrConstant {

		public static final String CORE_HOUSE = "hfqHouse";

		public static final String CORE_ROOM = "hfqRoom";
		public static final String CORE_HZF_HOUSES = "hzfHouses";
		// hfqHouse 字段名
		public static final String HOUSE_FIELD_SELLID = "hsId";
		public static final String HOUSE_FIELD_PUBDATE = "pubDateStr";
		public static final String HOUSE_FIELD_COMMUNITYNAME = "communityName";
		public static final String HOUSE_FIELD_PRICE = "rentPriceMonth";
		public static final String HOUSE_FIELD_IMGS = "picRootPath";
		public static final String HOUSE_FIELD_ADDRESS = "address";
		public static final String HOUSE_FIELD_LIVINGROOMNUMS = "livingroomNums";
		public static final String HOUSE_FIELD_BEDROOMNUMS = "bedroomNums";
		public static final String HOUSE_FIELD_TOILETNUMS = "toiletNums";
		public static final String HOUSE_FIELD_ORIENTATIONS = "orientations";
		public static final String HOUSE_FIELD_DECORATION = "decoration";
		public static final String HOUSE_FIELD_AREA = "fArea";
		public static final String HOUSE_FIELD_FLOWNO = "flowNo";
		public static final String HOUSE_FIELD_FLOWTOTAL = "flowTotal";
		public static final String HOUSE_FIELD_ENTIRERENT = "entireRent";
		public static final String HOUSE_FIELD_DISTANCE = "_dist_";
		public static final String HOUSE_FIELD_SUBWAY = "subway";
		public static final String HOUSE_FIELD_HOUSETAG = "housedTag";
		public static final String HOUSE_FIELD_STATUS = "status";
		public static final String HOUSE_FIELD_CHECKIN = "canCheckinDateStr";
		public static final String HOUSE_FIELD_DEPOSITMONTH = "depositMonth";
		public static final String HOUSE_FIELD_PERIODMONTH = "periodMonth";
		public static final String HOUSE_FIELD_UPDATEDATE = "updateDateStr";
		public static final String HOUSE_FIELD_BAIDULO = "baiduLo";
		public static final String HOUSE_FIELD_BAIDULA = "baiduLa";
		public static final String HOUSE_FIELD_SUBWAYDISTANCE = "subwayDistance";
		public static final String HOUSE_FIELD_BUSSTATIONS = "busStations";
		public static final String HOUSE_FIELD_SURROUND = "surround";
		public static final String HOUSE_FIELD_BIZNAME = "bizName";
		public static final String HOUSE_FIELD_BUILDINGYEAR = "buildingYear";
		public static final String HOUSE_FIELD_TOILET = "ftoilet";
		public static final String HOUSE_FIELD_BALCONY = "fbalcony";
		public static final String HOUSE_FIELD_INSURANCE = "finsurance";
		public static final String HOUSE_FIELD_COMMENT = "fcomment";
		public static final String HOUSE_FIELD_SOURCE = "fsource";
		public static final String HOUSE_FIELD_ROOM_ID = "rId";
		public static final String HOUSE_FIELD_ROOM_NAME = "roomname";
		public static final String HOUSE_FIELD_ROOM_TYPE = "roomType";
		public static final String HOUSE_FIELD_ROOM_PRICE = "rRentPriceMonth";
		public static final String HOUSE_FIELD_ROOM_ORIENTATION = "roomori";
		public static final String HOUSE_FIELD_ROOM_AREA = "rArea";
		public static final String HOUSE_FIELD_ROOM_PUBDATE = "rpubdateStr";
		public static final String HOUSE_FIELD_ROOM_STATUS = "rStatus";
		public static final String HOUSE_FIELD_ROOM_DEPOSITMONTH = "rDepositMonth";
		public static final String HOUSE_FIELD_ROOM_PERIODMONTH = "rPeriodMonth";
		public static final String HOUSE_FIELD_ROOM_TOILET = "rtoilet";
		public static final String HOUSE_FIELD_ROOM_BALCONY = "rbalcony";
		public static final String HOUSE_FIELD_SETTING_CODE = "settingCode";
		public static final String HOUSE_FIELD_SETTING_CATEGORYTYPE = "categoryTypes";
		public static final String HOUSE_FIELD_SETTING_NUMS = "settingNums";

		// hfqRoom 字段名
		public static final String ROOM_FIELD_SELLID = "hsId";
	}

	// 城市信息
	public static class CityInfo {

		// 地铁
		public static final int CITY_INFO_TYPE_SUBWAY = 1;

		// 商圈
		public static final int CITY_INFO_TYPE_CIRCLE = 2;

		// 需要更新
		public static final int NEED_UPDATE_FLAG = 0;

		// 不需要更新
		public static final int UNNEED_UPDATE_FLAG = 1;
	}

	/**
	 * 模拟百度房源详情信息
	 */
	public static class BdHouseDetail {
		// ------------------房屋出租类型-----------------
		// 合租
		public static final int RENT_TYPE_SHARE = 2;

		// 整租
		public static final int RENT_TYPE_ENTIRE = 1;

		// 上架
		public static final int HOUSE_STATE_UP = 4000;

		// 下架
		public static final int HOUSE_STATE_LOWER = 5000;

		public static final Integer NOTFOUND = 11111;

		public static final String UNKNOWN = "unknown";
		
		// saas公司
		public static final int IS_SAAS_YES = 1;
		// 非saas公司
		public static final int IS_SAAS_NO = 0;
	}

	/**
     * 闲鱼房源详情
     */
    public static class AliHouseDetail {

        public static final Long NOTFOUND = 11111L;

        public static final String UNKNOWN = "unknown";

    }
    
	/**
	 * 模拟百度房源详情信息
	 */
	public static class BdHouseDetailQft {
		// ------------------房屋出租类型-----------------
		// 合租
		public static final int RENT_TYPE_SHARE = 2;

		// 整租
		public static final int RENT_TYPE_ENTIRE = 1;

		// 上架
		public static final int HOUSE_STATE_UP = 4000;

		// 下架
		public static final int HOUSE_STATE_LOWER = 5000;

		public static final Integer NOTFOUND = 11111;

		public static final String UNKNOWN = "unknown";
	}
	
//	/**
//	 * 房源标签
//	 */
//	public static Map<Integer, String> houseTag;
//	static {
//		houseTag = new HashMap<Integer, String>();
//		houseTag.put(1, "首次发布");
//		houseTag.put(2, "南向");
//		houseTag.put(3, "独立卫生间");
//		houseTag.put(4, "独立阳台");
//		houseTag.put(5, "精装修");
//		houseTag.put(6, "近地铁");
//		houseTag.put(7, "设施齐全");
////		houseTag.put(8, "集中供暖");
//		houseTag.put(9, "押一付一");
////		houseTag.put(10, "独立供暖");
////		houseTag.put(11, "智能门锁");
//		houseTag.put(12, "支持月付");
//	}
	
	/**
	 * 平台
	 */
	public static final class platform {
		
		//是否图片美化
		public static final int IS_IMG_YES = 1;
		
		public static final int IS_IMG_NO = 0;
		// 客户端
		public static final String ANDROID = "hzf-android";
		public static final String IOS = "hzf-iOS";
		public static final String H5 = "hzf-h5";
		public static final String SHOP = "hzf-shop";
		/**
		 * 微信平台
		 */
		public static final String WX = "wx";
		/**
		 * pc
		 */
		public static final String PCWEB = "pcweb";
		/**
		 * 移动端
		 */
		public static final String MWEB = "mweb";
		/**
		 * 开方给乙方
		 */
		public static final String PARTY_B = "partyb";
		/**
		 * 内部系统调用
		 */
		public static final String INNER = "inner";

		public static final Set<String> HFQ_PLATFORM = new HashSet<>();
		static {
			HFQ_PLATFORM.add(ANDROID);
			HFQ_PLATFORM.add(IOS);
			HFQ_PLATFORM.add(H5);
			HFQ_PLATFORM.add(WX);
			HFQ_PLATFORM.add(PCWEB);
			HFQ_PLATFORM.add(MWEB);
			HFQ_PLATFORM.add(PARTY_B);
			HFQ_PLATFORM.add(INNER);
			HFQ_PLATFORM.add(SHOP);
		}

		public static boolean isWebPlatform(String platform) {
			return WX.equalsIgnoreCase(platform) || MWEB.equalsIgnoreCase(platform) || PCWEB.equalsIgnoreCase(platform) || INNER.equalsIgnoreCase(platform);
		}

		public static boolean isAppPlatform(String platform) {
			return IOS.equalsIgnoreCase(platform) || ANDROID.equalsIgnoreCase(platform) || H5.equalsIgnoreCase(platform) || SHOP.equalsIgnoreCase(platform);
		}
		
		public static boolean isInnerPlatform(String platform) {
			return INNER.equals(platform);
		}

		public static String getPayPlatform(String platform) {
			switch (platform) {
			case ANDROID:
				return "1";
			case IOS:
				return "2";
			case WX:
				return "3";
			default:
				return "1";
			}
		}
	}

	/**
	 * 客户来源
	 */
	public static final class camefrom {

		// 来自扫码推广活动
		public static final String CAMEFROM_SCANCODE = "scancode";

		// 刮刮乐抽奖活动
		public static final String CAMEFROM_LOTTERY = "lottery";

		// 砸金蛋抽奖活动
		public static final String CAMEFROM_GOLDEN = "golden";
	}
	
	/**
	 * 付款方式
	 */
	public static class payType {
		// 免押金
		public static final int NO_DEPOSIT = 0;
		// 押一付一
		public static final int DEPOSIT_ONE_PAY_ONE = 1;
		// 付三押一
		public static final int DEPOSIT_ONE_PAY_THREE = 2;
		// 半年/年付
		public static final int YEAR_PAY = 3;
		// 支持月付
		public static final int MONTH_PAY = 4;
	}
	
	/**
	 * 查询房源列表的排序规则
	 */
	public static class rankStyle {
		// 默认排序
		public static final int DEFAULT_RANK = 0;
		// 价格从低到高
		public static final int PRICE_01_RANK = 1;
		// 价格从高到底
		public static final int PRICE_10_RANK = 2;
		// 面积从小到大
		public static final int AREA_01_RANK = 3;
		// 面积从大到小
		public static final int AREA_10_RANK = 4;
	}

	/**
	 * 代金券使用场景
	 */
	public static final class voucher_use_scene {

		/**
		 * 还款
		 */
		public static final int REPAY = 1;
	}

	/**
	 * 代金券活动id
	 */
	public static final class voucher_activity {

		/**
		 * 还款
		 */
		public static final long REPAY = 1;

		/**
		 * 二维码活动
		 */
		public static final long QR = 2;

		/**
		 * 注册
		 */
		public static final long REGISTER = 3;

		/**
		 * 系统发放
		 */
		public static final long SYSTEM = 4;

		/**
		 * 会分裂
		 */
		public static final long FISSION = 5;

		/**
		 * 刮刮乐抽奖
		 */
		public static final long GGL_LOTTERY = 6;

		/**
		 * 砸金蛋抽奖(new) 对新用户规则
		 */
		public static final long SMASH_NEWGOLD = 7;

		/**
		 * 砸金蛋抽奖(old) 对老用户规则
		 */
		public static final long SMASH_OLDGOLD = 8;
	}

	// 申请合同验证状态
	public static class UserConstractStatus {

		// 申请受理中
		public static final int APPLY_ACCEPTING = 1;

		// 审核中
		public static final int APPLY_VERIFIYING = 2;

		// 审核成功
		public static final int APPLY_VERIFY_SUCCESS = 3;

		// 审核失败
		public static final int APPLY_VERIFY_FAILED = 4;

		// 审核拒绝
		public static final int APPLY_VERIFY_REFUSED = 5;

		// 出纳
		public static final int APPLY_CASHIER = 6;

		// 已转租
		public static final int APPLY_SUBLET = 7;

		// 已结束
		public static final int APPLY_FINISH = 8;

		// BOSS中采用的状态信息
		// 审核中
		public static final int BOSS_APPLY_VERIFIYING = 0;

		// 审核未通过
		public static final int BOSS_APPLY_FAILED = 1;

		// 审核通过
		public static final int BOSS_APPLY_SUCCESS = 2;

		// 待确认收款
		public static final int BOSS_CONFRIMING_PAY = 3;

		// 已确认收款
		public static final int BOSS_CONFIRMED_PAY = 4;

		// 待审核
		public static final int BOSS_READY_VERIFY = 5;

		// 预审拒绝
		public static final int BOSS_APPLY_REFUSED = 6;

		// 待确认
		public static final int BOSS_APPLY_PRE_CONFIRM = 7;

		// 待补全
		public static final int BOSS_APPLY_PRE_COMPLETE = 8;

		// 修改待确认
		public static final int BOSS_APPLY_MOD_PRE_CONFIRM = 9;

		// 待缴保证金
		public static final int BOSS_APPLY_PRE_DEPOSIT = 10;

		/**
		 * 将BOSS的合同状态对应为用户的状态
		 *
		 * @param bossStatus
		 * @return 默认返回审核中
		 */
		public static int changeBossStatusToUserStatus(int bossStatus) {

			switch (bossStatus) {
				// 审核中
				case BOSS_APPLY_VERIFIYING:
				case BOSS_READY_VERIFY:
				case BOSS_APPLY_PRE_CONFIRM:
				case BOSS_APPLY_PRE_COMPLETE:
				case BOSS_APPLY_FAILED:
				case BOSS_APPLY_PRE_DEPOSIT:
					return APPLY_VERIFIYING;

				// 审核失败
				// case BOSS_APPLY_FAILED:
				// return APPLY_VERIFY_FAILED;

				// 审核成功
				case BOSS_CONFRIMING_PAY:
				case BOSS_CONFIRMED_PAY:
				case BOSS_APPLY_SUCCESS:
					return APPLY_VERIFY_SUCCESS;

				// 审核拒绝
				case BOSS_APPLY_REFUSED:
					return APPLY_VERIFY_REFUSED;

			}

			return APPLY_VERIFIYING;
		}

		/**
		 * 获取合同状态描述
		 *
		 * @param contractStatus
		 * @return
		 */
		public static String getContractDesByStatus(int contractStatus) {
			switch (contractStatus) {
				// 审核中
				case APPLY_VERIFIYING:
					return "审核中";
				// 审核成功
				case APPLY_VERIFY_SUCCESS:
					return "审核成功";
				// 审核失败
				case APPLY_VERIFY_FAILED:
					return "审核失败";
				// 审核拒绝
				case APPLY_VERIFY_REFUSED:
					return "审核拒绝";
				// 出纳
				case APPLY_CASHIER:
					return "出纳";
				// 已转租
				case APPLY_SUBLET:
					return "已转租";
				// 已结束
				case APPLY_FINISH:
					return "已结束";
			}
			return "审核中";
		}
	}

	// 交易
	public static class Charge {
		// 未支付
		public static final int CHARGE_STATUS_UNCHARGE = 0;

		// 支付完成
		public static final int CHARGE_STATUS_COMPLETED = 2;

		// 已发起支付，即支付中
		public static final int CHARGE_STATUS_CHARGING = 3;

		// 支付失败
		public static final int CHARGE_STATUS_FAILED = 4;

		// 支付撤销
		public static final int CHARGE_STATUS_CANCLE = 5;

		// 最低只支持一元钱的银行名单
		public static final String[] ONE_YUAN_BANK = new String[] { "交通银行" };

		/**
		 * 返回银行支持的最小金额
		 *
		 * @param bankName
		 * @return 最小金额 单位分
		 */
		public static int getMinMoney(String bankName) {
			for (String name : ONE_YUAN_BANK) {
				if (name.equals(bankName)) {
					return 100;
				}
			}
			return 1;
		}
	}

	// 支付处理渠道
	public static class PayHandleChannel {

		// 会找房
		public static final String HZF = "hzf";
		
		// 会分期
		public static final String HFQ = "hfq";

		// 京东
		public static final String JD = "jd";

		// 北银
		public static final String BOB = "bob";

		// 用于调用支付后台
		public static String getBankCardChannel(String payChannel) {
			switch (payChannel) {
				case HFQ:
					return "1";
				case JD:
					return "2";
				default:
					return "1";
			}
		}

		// 用于解析支付后台回传的结果
		public static String getPayChannel(String bankCardChannel) {
			switch (bankCardChannel) {
				case "1":
					return HFQ;
				case "2":
					return JD;
				default:
					return HFQ;
			}
		}
	}

	public static class User {
		// 用户信息不存在
		public static final int USER_INFO_EXIST = 1;

		// 用户信息存在
		public static final int USER_INFO_UNEXIST = 0;

		// 用户确认合同信息
		public static final int USER_INFO_STATUS_CONFRIM_CONTRACT = 0;

		// 身份证信息
		public static final int USER_INFO_STATUS_ID = 1;

		// 完善个人信息
		public static final int USER_INFO_STATUS_DETAIL = 2;

		// 绑定银行卡成功
		public static final int USER_INFO_STATUS_BIND_CARD = 3;

		// 支付成功
		public static final int USER_INFO_STATUS_CHARGED = 4;

		// 用户来源
		// 普租
		public static final int USER_SOURCE_TYPE_GENERAL = 1;

		// 代理
		public static final int USER_SOURCE_TYPE_PROXY = 2;

		// C端
		public static final int USER_SOURCE_TYPE_CLIENT = 3;

		// 公寓
		public static final int USER_SOURCE_TYPE_APARTMENT = 1;

	}


	// 支付渠道
	public static class PayChannel {

		// 支付宝手机支付
		public static final String ALIPAY = "alipay";

		// 支付宝手机网页支付
		public static final String ALIPAY_WAP = "alipay_wap";

		// 支付宝pc网页支付
		public static final String ALIPAY_PC = "alipay_pc_direct";

		// 微信支付
		public static final String WX = "wx";

		// 微信公众号支付
		public static final String WX_PUB = "wx_pub";

		// 银联全渠道支付
		public static final String UPACP = "upacp";

		// 银联全渠道手机网页支付
		public static final String UPACP_WAP = "upacp_wap";

		// 银联PC网页支付
		public static final String UPACP_PC = "upacp_pc";

		// 易宝支付
		public static final String YEEPAY = "yeepay";

		// 京东支付
		public static final String JD = "jd";

		// 银行卡支付
		public static final String BANKCARD = "bank_card";

		// 连联支付
		public static final String LIANLIAN = "lianlian";

		/**
		 * 是否支付宝相关支付
		 *
		 * @param payChannel
		 * @return
		 */
		public static boolean isAliPay(String payChannel) {
			return ALIPAY.equals(payChannel) || ALIPAY_WAP.equals(payChannel) || ALIPAY_PC.equals(payChannel);
		}

		/**
		 * 是否微信相关支付
		 *
		 * @param payChannel
		 * @return
		 */
		public static boolean isWeixinPay(String payChannel) {
			return WX.equals(payChannel) || WX_PUB.equals(payChannel);
		}

		/**
		 * 是否银联相关支付
		 *
		 * @param payChannel
		 * @return
		 */
		public static boolean isUnionPay(String payChannel) {
			return UPACP.equals(payChannel) || UPACP_WAP.equals(payChannel) || UPACP_PC.equals(payChannel);
		}

		/**
		 * 是否易宝支付
		 *
		 * @param payChannel
		 * @return
		 */
		public static boolean isYeePay(String payChannel) {
			return YEEPAY.equals(payChannel);
		}

		/**
		 * 是否京东支付
		 *
		 * @param payChannel
		 * @return
		 */
		public static boolean isJdPay(String payChannel) {
			return JD.equals(payChannel);
		}

		public static boolean isLianLian(String payChannel) {
			return LIANLIAN.equals(payChannel);
		}

	}

	public static class Alipay {
		// 阿里支付开关
		// 打开
		public static final String ALIPAY_SWITCH_ON = "0";

		// 关闭
		public static final String ALIPAY_SWITCH_OFF = "1";

	}


	public static class ProductType {
		/**
		 * 保证金
		 */
		public static final String DEPOSIT = "1";

		/**
		 * 租金
		 */
		public static final String RENT = "2";
	}
	
	
	// 品牌公寓
	public static class ServiceApartment {

		// 默认品牌公寓城市ID
		public static final int DEFAULT_CITY_ID = -1;
		
		// 默认更多品牌公司ID
		public static final String DEFAULT_COMPANY_ID = "-1";
	}

	/**
	 * 平台
	 */
	public static final class statisticReport {
		// 客户端
		public static final String  statisticReport= "statistic-report";
		public static final String firstPage = "first-page";
		public static final String total = "total";
		public static final String listPage = "list-page";
		public static final String detailPage = "detail-page";
		public static final String propagationPage = "propagation-page";

		public static final Set<String> STATISTICREPORT = new HashSet<>();

		static {
			STATISTICREPORT.add(firstPage);
			STATISTICREPORT.add(total);
			STATISTICREPORT.add(listPage);
			STATISTICREPORT.add(detailPage);
			STATISTICREPORT.add(propagationPage);
		}

		public static boolean isRealSource(String realSource) {
			return firstPage.equalsIgnoreCase(realSource) || total.equalsIgnoreCase(realSource) || listPage.equalsIgnoreCase(realSource) || detailPage.equalsIgnoreCase(realSource) || propagationPage.equalsIgnoreCase(realSource);
		}
	}
	
	/**
	 * 房源详情信息
	 */
	public static class MapHouse {
		
		// ------------------是否展示行政区、商圈房源量-----------------
		// 展示
		public static final int CITY_MAP_OPEN = 1;
				
		// ------------------行政区-----------------
		// 最大 范围距离
		public static final String DISTANCE_MAX = "999999999";

		// 最小范围距离
		public static final String DISTANCE_MIN = "5000";
				
		
		// ------------------渠道-----------------
		// 安卓
		public static final String CHANNEL_AND = "1";

		// IOS
		public static final String CHANNEL_IOS = "2";
		
		// H5
		public static final String CHANNEL_H5 = "3";

		// ------------------层级-----------------
		// 行政区层级
		public static final int LEVEL_DIS = 1;

		//商圈层级
		public static final int LEVEL_BIZ = 2;

		//小区层级
		public static final int LEVEL_COMMUNITY = 3;

		//小区层级
		public static final int LEVEL_COMPANY = 5;
		
		public static String getLevelName(int level) {
			String levelName = null;
			switch (level) {
			case LEVEL_DIS:
				levelName = "districtId";
				break;
			case LEVEL_BIZ:
				levelName = "bizId";
				break;
			case LEVEL_COMMUNITY:
				levelName = "communityName";
				break;
			case LEVEL_COMPANY:
				levelName = "companyId";
				break;
			default:
				levelName = "";
				break;
			}
			return levelName;
		}
	}

	// 是否展示当前城市下品牌公寓标识
	public static class ShowApartmentFlag {
		// 展示品牌公寓模块
		public static final int SHOW_APARTMENT = 1;
		// 不展示品牌公寓模块
		public static final int HIDDEN_APARTMENT = 0;
	}
	
/** ====================================在线缴租相关常量信息start====================================  */
	
	/** 账单类型(-1：全部；102：出租房租；110：其他账单) */
	public static class financeType {
		public static final int ALL_FINANCE_TYPE = -1;
		public static final int RENT_FINANCE_TYPE = 102;
		public static final int OTHER_FINANCE_TYPE = 110;
	}
	
	/** 支付状态(-1：全部；0：未支付；1：待支付欠款；2：已支付；3：已撤销/失效；4：未收款；5：已收款；6：待收款欠款；-2：未支付完成) */
	public static class payStatus {
		public static final int ALL_PAY_STATUS = -1;
		public static final int NOT_FINISHED_STATUS = -2;
		public static final int NOT_PAID_STATUS = 0;
		public static final int DEBT_STATUS = 1;
		public static final int PAID_STATUS = 2;
		public static final int INVALID_STATUS = 3;
		public static final int NOT_RECEIPT_STATUS = 4;
		public static final int RECEIPTED_STATUS = 5;
		public static final int RECEIPT_DEBT_STATUS = 6;
	}
	
	/** 参考：支付状态排序规则(0：未支付；1：未支付欠款；2：未收款；3：未收款欠款；4：已支付；5：已收款；6：已撤销/失效) */
	public static class payStatusReference {
		public static final int NOT_PAID_STATUS = 0;
		public static final int DEBT_STATUS = 1;
		public static final int NOT_RECEIPT_STATUS = 2;
		public static final int RECEIPT_DEBT_STATUS = 3;
		public static final int PAID_STATUS = 4;
		public static final int RECEIPTED_STATUS = 5;
		public static final int INVALID_STATUS = 6;
	}
	
	/** 收款/付款话术 */
	public static Map<Integer,String> payStatusName;
	static{
		payStatusName = new HashMap<>();
		payStatusName.put(0, "待支付");
		payStatusName.put(1, "欠款");
		payStatusName.put(2, "支付成功");
		payStatusName.put(3, "已失效");
		payStatusName.put(4, "待收款");
		payStatusName.put(5, "收款成功");
	}
	
	/** 应收/应付账单类型(-1：全部；1：待支付；2：待收款) */
	public static class paymentType {
		public static final int ALL_PAYMENT_TYPE = -1;
		public static final int EXPENDITURE_PAYMENT_TYPE = 1;
		public static final int INCOME_PAYMENT_TYPE = 2;
	}
	
	/** 支付状态的颜色显示 */
	public static class paymentTypeColor {
		public static final String WAIT_DEAL_STATUS = "f16600";
		public static final String DEAL_SUCCESS = "00acb9";
		public static final String DEAL_INVALID = "999999";
	}
	
	/** 操作终端 */
	public static class operateTerminal {
		/* 安卓平台 */
		public static final String ANDROID = "android";
		/* 安卓平台 */
		public static final String IOS = "ios";
		/* 微信平台 */
		public static final String WX = "wx";
		/* pc主站 */
		public static final String WEB = "web";
		/* 移动端主站 */
		public static final String MWEB = "mweb";
	}
	
	/** 收款/付款话术 */
	public static Map<Integer,String> paymentTypeComment;
	static{
		paymentTypeComment = new HashMap<>();
		paymentTypeComment.put(0, "应收款日");
		paymentTypeComment.put(1, "最后付款日");
	}
	
	/** 账单话术 */
	public static Map<Integer,String> bugetComment;
	static{
		bugetComment = new HashMap<>();
		bugetComment.put(0, "应收金额");
		bugetComment.put(1, "应付金额");
		bugetComment.put(2, "账单金额");
	}
	
	/** 发起支付状态 */
	public static class sponsorPayStatus {
		public static final int IS_SPONSOR_STATUS = 1;
		public static final int NOT_SPONSOR_STATUS = 0;
	}
	
	/** 发起支付后的支付状态(0：支付中；1：支付完成；2：支付失败) */
	public static class paymentStatus {
		public static final int PAYING_STATUS = 0;
		public static final int SUCCESS_PAID_STATUS = 1;
		public static final int FAIL_PAID_STATUS = 2;
	}
	
	/** 账单剩余付款天数对应展示文案 */
	public static Map<Integer,String> remainDaysName;
	static{
		remainDaysName = new HashMap<>();
		remainDaysName.put(1, "还剩");
		remainDaysName.put(0, "今天是交费日");
		remainDaysName.put(-1, "已逾期");
		remainDaysName.put(8, "");
		remainDaysName.put(2, "今天是收款日");
	}
	
/** ====================================在线缴租相关常量信息end====================================  */


	/** 是否支付用户代扣(0：不支持；1：支择) */
	public static class SupportWithDraw {
		public static final int  NO_SUPPORT= 0;
		public static final int SUPPORT = 1;
	}


	/** 交房租用户实名及代扣类型(0：不支持；1：支择) */
	public static class ContractConfirmStatus {
		public static final int  DEFAULT= 0;  //未实名，有代扣
		 public static final int  NO_RN_WD= 1;  //未实名，有代扣
		public static final int  NO_RN_NO_WD= 2; //未实名，无代扣
		public static final int  RN_WD= 3;    //已实名，有代扣
		public static final int  RN_NO_WD= 4; //已实名，无代扣
		public static final int  RE_CONFIRM= 5; //二次确认
	}

}
