package com.huifenqi.hzf_platform.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.huifenqi.hzf_platform.utils.BdHouseCheckUtil;

/**
 * @Description:
 * @Author chenshuai
 * @Date 2017/4/14 0014 12:01
 */
public class BdConstantsEnum {

    public interface EnumValue {
        String getName();

        int getIndex();
    }

	/**
     * 同步状态
     */
    public enum TransferFlagEnum implements EnumValue {

        FINSH(1, "已同步"),
        INIT(0, "待同步");

        private final Integer index;
        private final String name;

        TransferFlagEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
			for (TransferFlagEnum item : TransferFlagEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public int getIndex() {
			return this.index;
		}

		public static boolean checkIndexExist(int index, boolean isValue) {
			if (!isValue) {
				if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT) {
					return true;
				}
			}

			if (map.containsKey(index)) {
				return true;
			}
			return false;
		}

		public static String getName(Integer index) {
			if (index == null)
				return null;
			return map.get(index);
		}
	}
    /**
     * 出租方式
     */
    public enum RentTypeEnum implements EnumValue {

        ENTIRE(1, "整租"),
        SHARE(2, "单间出租");

        private final Integer index;
        private final String name;

        RentTypeEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (RentTypeEnum item : RentTypeEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }

    }

    /**
     * 出租屋类型
     */
    public enum BedRoomTypeEnum implements EnumValue {

        PRIMARY(31, "主卧"),
        SECONDARY(32, "次卧"),
        INDISCRIMINATE(33, "次卧");

        private final Integer index;
        private final String name;

        BedRoomTypeEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (BedRoomTypeEnum item : BedRoomTypeEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }
    }

    /**
     * 房间标签
     * 10离地铁近
     11有阳台
     12独立卫生间
     13厨房
     14首次出租
     15阳光房
     16无中介费
     17押一付一
     18押二付一
     19无服务费
     */
    public enum FeatureTagEnum implements EnumValue {

        SUBWAY_NEAR(10,"离地铁近"),
        BALCONY(11,"有阳台"),
        PRIVATE_BATHROOM(12,"独立卫生间"),
        KITCHEN(13,"厨房"),
        FIRST_RENT(14,"首次出租"),
        SUNSHINE(15,"阳光房"),
        NO_AGENT_FEE(16,"无中介费"),
        ONE_ONE(17,"押一付一"),
        TWO_ONE(18,"押二付一"),
        NO_SERVE_FEE(19,"无服务费");

        private final Integer index;
        private final String name;

        FeatureTagEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (FeatureTagEnum item : FeatureTagEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static boolean checkIndexExist(List<Integer> indexs, boolean isValue){

            if (CollectionUtils.isEmpty(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            for (Integer index : indexs) {
                if (!map.containsKey(index)){
                    return false;
                }
            }
            return true;
        }

        public static boolean checkIndexExist(String indexs, boolean isValue){
            if (StringUtils.isBlank(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            String[] split = indexs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isNotBlank(s)){
                    try {
                        Integer index = Integer.valueOf(s);
                        list.add(index);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }

            return checkIndexExist(list,isValue);
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }
    }


    /**
     * 房屋配置，枚举值，可多选，以逗号分隔
     71床  72衣柜  73书桌  74空调  75暖气
     76电视机  77燃气  78微波炉  79电磁炉
     80热水器  81洗衣机 82冰箱  83wifi
     84沙发  85橱柜  86油烟机
     */
    public enum DetailPointEnum implements EnumValue {

        BED(71,"床"),
        CHEST(72,"衣柜"),
        DESK(73,"书桌"),
        AIR_CONDITION(74,"空调"),
        HEATING(75,"暖气"),
        TV(76,"电视机"),
        GAS(77,"燃气"),
        MICROWAVE(78,"微波炉"),
        INDUCTION(79,"电磁炉"),
        WATER_HEATER(80,"热水器"),
        WASHER(81,"洗衣机"),
        FREEZER(82,"冰箱"),
        WIFI(83,"wifi"),
        SOFA(84,"沙发"),
        CABINET(85,"橱柜"),
        VENTILATOR(86,"油烟机");

        private final Integer index;
        private final String name;

        DetailPointEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (DetailPointEnum item : DetailPointEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static boolean checkIndexExist(List<Integer> indexs, boolean isValue){

            if (CollectionUtils.isEmpty(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            for (Integer index : indexs) {
                if (!map.containsKey(index)){
                    return false;
                }
            }
            return true;
        }

        public static boolean checkIndexExist(String indexs, boolean isValue){
            if (StringUtils.isBlank(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            String[] split = indexs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isNotBlank(s)){
                    try {
                        Integer index = Integer.valueOf(s);
                        list.add(index);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }

            return checkIndexExist(list,isValue);
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }
    }

    /**
     * 公寓配套服务，枚举值，可多选，以逗号分隔
     91健身房  92公寓超市  93智能门锁  94ATM机
     95代收快递  96专属客服  97房间清洁
     */
    public enum ServicePointEnum implements EnumValue {

        GYM(91,"健身房"),
        SUPERMARKET(92,"公寓超市"),
        AMMETER(93,"智能门锁"),
        ATM(94,"ATM机"),
        EXPRESS_COLLECTION(95,"代收快递"),
        PRIVATE_SERVICE(96,"专属客服"),
        ROOM_CLEAN(97,"房间清洁");

        private final Integer index;
        private final String name;

        ServicePointEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (ServicePointEnum item : ServicePointEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static boolean checkIndexExist(List<Integer> indexs, boolean isValue){

            if (CollectionUtils.isEmpty(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            for (Integer index : indexs) {
                if (!map.containsKey(index)){
                    return false;
                }
            }
            return true;
        }

        public static boolean checkIndexExist(String indexs, boolean isValue){
            if (StringUtils.isBlank(indexs)){
                if (!isValue){
                    return true;
                }
                return false;
            }
            String[] split = indexs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isNotBlank(s)){
                    try {
                        Integer index = Integer.valueOf(s);
                        list.add(index);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }

            return checkIndexExist(list,isValue);
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }
    }

    /**
     * 是否支持短租
     */
    public enum ShortRentEnum implements EnumValue {

        SUPPORT(1, "支持"),
        NONSUPPORT(0, "不支持");

        private final Integer index;
        private final String name;

        ShortRentEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (ShortRentEnum item : ShortRentEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }

    }

    /**
     * 小区供暖方式
     */
    public enum SupplyHeatingEnum implements EnumValue {

        FOCUS(1, "集中供暖"),
        OWN(2, "自供暖"),
        NO(3, "无供暖");

        private final Integer index;
        private final String name;

        SupplyHeatingEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (SupplyHeatingEnum item : SupplyHeatingEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }

    }

    /**
     * 建筑类型
     */
    public enum BuildTypeEnum implements EnumValue {

        TOWER_FLOOR(71, "塔楼"),
        BOARD_FLOOR(72, "板楼"),
        BOARD(73, "平板");

        private final Integer index;
        private final String name;

        BuildTypeEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (BuildTypeEnum item : BuildTypeEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }

    }

    /**
     * 房源状态(String)
     */
    public enum StatusEnum {

        PUT_AWAY("4000", "上架"),
        SOLD_OUT("5000", "下架");

        private final String index;
        private final String name;

        StatusEnum(String index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<String,String> map = new HashMap<String,String>();

        static {
            for (StatusEnum item : StatusEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        public String getName() {
            return this.name;
        }

        public String getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(String index,boolean isValue){
            if(!isValue){
                if (BdHouseCheckUtil.DEFAULT_ENUM_STR.equals(index)){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(String index) {
            if (StringUtils.isBlank(index)) return null;
            return map.get(index);
        }

    }

    /**
     * 朝向，枚举值：60-70{东，南，西，北，东南，西南，东北，西北，东西，南北，未知}
     */
    public enum FaceToTypeEnum implements EnumValue {

        EAST(60, "东"),
        SOUTH(61, "南"),
        WEST(62, "西"),
        NORTH(63, "北"),
        EAST_SOUTH(64, "东南"),
        WEST_SOUTH(65, "西南"),
        EAST_NORTH(66, "东北"),
        WEST_NORTH(67, "西北"),
        EAST_WEST(68, "东西"),
        SOUTH_NORTH(69, "南北"),
        UNKNOWN(70, "未知"),
        ;

        private final Integer index;
        private final String name;

        FaceToTypeEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private static HashMap<Integer,String> map = new HashMap<Integer,String>();

        static {
            for (FaceToTypeEnum item : FaceToTypeEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        public static boolean checkIndexExist(int index,boolean isValue){
            if(!isValue){
                if (index == BdHouseCheckUtil.DEFAULT_ENUM_INT){
                    return true;
                }
            }

            if (map.containsKey(index)){
                return true;
            }
            return false;
        }

        public static String getName(Integer index) {
            if (index == null) return null;
            return map.get(index);
        }

    }
}
