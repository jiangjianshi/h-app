package com.huifenqi.hzf_platform.context.enums.bd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.enums.SettingCategoryEnum;

public enum SettingsEnum {

    bed("床", "1", "71", SettingCategoryEnum.FURNITURE,1),
    sofa("沙发", "2", "84", SettingCategoryEnum.FURNITURE,10),
    table("电脑桌", "3", "73", SettingCategoryEnum.FURNITURE,8),
    wardrobe("衣柜", "4", "72", SettingCategoryEnum.FURNITURE,7),
    chair("椅子", "5", "", SettingCategoryEnum.FURNITURE,9),

    tv("电视", "1", "76", SettingCategoryEnum.ELECTRIC,6),
    fridge("冰箱", "2", "82", SettingCategoryEnum.ELECTRIC,5),
    ac("空调", "3", "74", SettingCategoryEnum.ELECTRIC,2),
    microwaveoven("微波炉", "4", "78", SettingCategoryEnum.ELECTRIC,11),
    washer("洗衣机", "5", "81", SettingCategoryEnum.ELECTRIC,4),
    kettle("电水壶", "6",  "", SettingCategoryEnum.ELECTRIC,12),

    curtain("窗帘", "1", "", SettingCategoryEnum.HOUSEHOLD,14),
    mattress("被褥", "2", "", SettingCategoryEnum.HOUSEHOLD,15),
    vase("花瓶", "3", "", SettingCategoryEnum.HOUSEHOLD,17),
    lamp("台灯", "4", "", SettingCategoryEnum.HOUSEHOLD,13),
    decoration("装饰画", "5", "", SettingCategoryEnum.HOUSEHOLD,16),

    wifi("WiFi", "1", "83", SettingCategoryEnum.OTHER,3);

    //一下为模拟百度独有
    //    heating("暖气", "2", "75", SettingCategoryEnum.OTHER),
    //    gas("燃气", "3", "77", SettingCategoryEnum.OTHER),
    //    cooker("电磁炉", "4", "79", SettingCategoryEnum.OTHER),
    //    water_heater("热水器", "5", "80", SettingCategoryEnum.OTHER),
    //    cabinet("橱柜", "6", "85", SettingCategoryEnum.OTHER),
    //    smoke_exhaust("油烟机", "7", "86", SettingCategoryEnum.OTHER);

    private String              desc;
    private String              code;
    private String              bdCode;
    private SettingCategoryEnum category;
    private int sort;


    private SettingsEnum(String desc, String code, String bdCode, SettingCategoryEnum category, int sort) {
		this.desc = desc;
		this.code = code;
		this.bdCode = bdCode;
		this.category = category;
		this.sort = sort;
	}

	private static String getItemCode(String bdCode) {
        if (bdCode == null && "".equals(bdCode)) {
            return "";
        }
        for (SettingsEnum o : SettingsEnum.values()) {
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
        for (SettingsEnum o : SettingsEnum.values()) {
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
            String temp = SettingsEnum.getItemCode(setting);
            if (!"".equals(temp)) {
                sb.append(temp + ":1").append(",");
            }
        }
        if(sb.length()>0){
        	return sb.substring(0, sb.length() - 1);
        }
        return "";
    }
    
    
    public static String getSettingCodes(String setttingsNames) {

        if (setttingsNames == null && "".equals(setttingsNames)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String[] arr = setttingsNames.split(",");
        for (String name : arr) {
            String temp = SettingsEnum.getItemCodeByName(name);
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
        for (SettingsEnum o : SettingsEnum.values()) {
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
        for (SettingsEnum o : SettingsEnum.values()) {
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

    public static SettingsEnum getEnumObject(int type, int code) {

        for (SettingsEnum o : SettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o;
            }
        }
        return null;
    }
    
    public static String getSettingDesc(int type, int code) {

        for (SettingsEnum o : SettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o.desc;
            }
        }
        return "";
    }

    public static String getBdSettingKey(int type, int code) {

        for (SettingsEnum o : SettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o.bdCode;
            }
        }
        return "";
    }
    
    public static List<String> getPrimarySettingKeys() {
        List<String> list = new ArrayList<String>();
        for (SettingsEnum o : SettingsEnum.values()) {
        	list.add(o.name());
        }
        return list;
    }

    public static List<String> getSecondarySettingKeys() {
        List<String> list = new ArrayList<String>();
        for (SettingsEnum o : SettingsEnum.values()) {
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

    //getter
	public String getBdCode() {
		return bdCode;
	}

	public void setBdCode(String bdCode) {
		this.bdCode = bdCode;
	}

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

	public int getSort() {
		return sort;
	}
    
    
}
