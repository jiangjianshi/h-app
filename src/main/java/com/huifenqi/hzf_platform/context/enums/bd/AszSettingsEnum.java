package com.huifenqi.hzf_platform.context.enums.bd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.enums.SettingCategoryEnum;

public enum AszSettingsEnum {

	bed("床", "1", "BED", SettingCategoryEnum.FURNITURE), 
	sofa("沙发", "2", "SOFA",SettingCategoryEnum.FURNITURE),
    table("电脑桌", "3", "73", SettingCategoryEnum.FURNITURE),
    wardrobe("衣柜", "4","CHEST", SettingCategoryEnum.FURNITURE),
    chair("椅子", "5", "CHAIR", SettingCategoryEnum.FURNITURE),
	tv("电视机", "1", "TV", SettingCategoryEnum.ELECTRIC), 
	fridge("冰箱", "2", "REFRIGERATOR",SettingCategoryEnum.ELECTRIC), 
	ac("空调", "3", "AIR_CONDITION", SettingCategoryEnum.ELECTRIC), 
	microwaveoven("微波炉", "4", "MICROWAVE",SettingCategoryEnum.ELECTRIC), 
	washer("洗衣机", "5", "WASHER", SettingCategoryEnum.ELECTRIC),
	// kettle("电水壶", "6", "", SettingCategoryEnum.ELECTRIC),
	// curtain("窗帘", "1", "", SettingCategoryEnum.HOUSEHOLD),
	// mattress("被褥", "2", "", SettingCategoryEnum.HOUSEHOLD),
	// vase("花瓶", "3", "", SettingCategoryEnum.HOUSEHOLD),
	// lamp("台灯", "4", "", SettingCategoryEnum.HOUSEHOLD),
	// decoration("装饰品", "5", "", SettingCategoryEnum.HOUSEHOLD),
	wifi("wifi", "1", "BROADBAND", SettingCategoryEnum.OTHER);
    //一下为模拟百度独有
    //    heating("暖气", "2", "75", SettingCategoryEnum.OTHER),
    //    gas("燃气", "3", "77", SettingCategoryEnum.OTHER),
    //    cooker("电磁炉", "4", "79", SettingCategoryEnum.OTHER),
    //water_heater("热水器", "5", "ELECTRIC_HEATER", SettingCategoryEnum.OTHER),
    //    cabinet("橱柜", "6", "85", SettingCategoryEnum.OTHER),
    //smoke_exhaust("油烟机", "7", "RANGE_HOOD", SettingCategoryEnum.OTHER);

    private String              desc;
    private String              code;
    private String              bdCode;
    private SettingCategoryEnum category;

    private AszSettingsEnum(String desc, String code, String bdCode, SettingCategoryEnum category) {
        this.desc = desc;
        this.code = code;
        this.bdCode = bdCode;
        this.category = category;
    }

    private static String getItemCode(String bdCode) {
        if (bdCode == null && "".equals(bdCode)) {
            return "";
        }
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.bdCode.equals(bdCode)) {
                return o.name();
            }
        }
        return "";
    }
    
    private static String getItemCodeByName(String settingName) {
        if (settingName == null && "".equals(settingName)) {
            return "";
        }
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.desc.equals(settingName)) {
                return o.name();
            }
        }
        return "";
    }
    

    public static String getCodes(String setttings) {
        if (setttings == null && "".equals(setttings)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String[] arr = setttings.split(",");
        for (String setting : arr) {
            String temp = AszSettingsEnum.getItemCode(setting);
            if (!"".equals(temp)) {
                sb.append(temp + ":1").append(",");
            }
        }
        if(sb.length()<2){//未匹配到类型返回空
        	return "";
        }
        String sets = sb.substring(0, sb.length() - 1);
        return sets;
    }
    
    
    public static String getSettingCodes(String setttingsNames) {

        if (setttingsNames == null && "".equals(setttingsNames)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String[] arr = setttingsNames.split(",");
        for (String name : arr) {
            String temp = AszSettingsEnum.getItemCodeByName(name);
            if (!"".equals(temp)) {
                sb.append(temp + ":1").append(",");
            }
        }
        if (sb.toString().length() == 0) {
            return "";
        }
        
        String sets = sb.substring(0, sb.length() - 1);
        return sets;
    }

    /**
     * 根据配置key 如：bed, tv, 获取对应的category值
     * 
     * @param settingKey
     * @return
     */
    public static int getSettingType(String settingKey) {
        if (StringUtils.isEmpty(settingKey)) {
            return Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED;
        }
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.name().equals(settingKey)) {
                return o.category.getCode();
            }
        }
        return Constants.HouseSetting.SETTING_TYPE_UNRECOGNIZED;
    }

    /**
     * 根据配置key 如：bed, tv, 获取对应的编码
     * 
     * @param settingKey
     * @return
     */
    public static int getSettingCode(String settingKey) {

        if (StringUtils.isEmpty(settingKey)) {
            return Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED;
        }
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.name().equals(settingKey)) {
                if (StringUtils.isEmpty(o.code)) {
                    return Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED;
                } else {
                    return Integer.parseInt(o.code);
                }
            }
        }
        return Constants.HouseSetting.SETTING_CODE_UNRECOGNIZED;
    }

    public static String getSettingKey(int type, int code) {

        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o.name();
            }
        }
        return "";
    }

    public static String getBdSettingKey(int type, int code) {

        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o.bdCode;
            }
        }
        return "";
    }
    
    public static List<String> getPrimarySettingKeys() {
        List<String> list = new ArrayList<String>();
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
        	list.add(o.name());
        }
        return list;
    }

    public static List<String> getSecondarySettingKeys() {
        List<String> list = new ArrayList<String>();
        for (AszSettingsEnum o : AszSettingsEnum.values()) {
            if (o.category.getCode() == SettingCategoryEnum.OTHER.getCode()) {
                list.add(o.name());
            }
        }
        return list;
    }

    public static List<Integer> getPrimarySettingTypes() {

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(SettingCategoryEnum.FURNITURE.getCode());
        list.add(SettingCategoryEnum.ELECTRIC.getCode());
        list.add(SettingCategoryEnum.HOUSEHOLD.getCode());
        return list;
    }

    public static List<Integer> getSecondarySettingTypes() {

        return Arrays.asList(SettingCategoryEnum.OTHER.getCode());
    }

}
