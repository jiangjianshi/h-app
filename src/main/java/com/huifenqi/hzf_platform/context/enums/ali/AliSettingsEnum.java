package com.huifenqi.hzf_platform.context.enums.ali;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.enums.SettingCategoryEnum;

public enum AliSettingsEnum {

    tv("电视机", "1", 7L, SettingCategoryEnum.ELECTRIC),
    fridge("冰箱", "2", 2L, SettingCategoryEnum.ELECTRIC),
    ac("空调", "3", 1L, SettingCategoryEnum.ELECTRIC),
    washer("洗衣机", "5", 3L, SettingCategoryEnum.ELECTRIC),
    wifi("wifi", "1", 6L, SettingCategoryEnum.OTHER);

    //房屋配套 1空调 2冰箱 3洗衣机 4燃气灶 5热水器 6WIFI 7 电视机 8 电磁炉 9 暖气

    private String              desc;
    private String              code;
    private Long              aliCode;
    private SettingCategoryEnum category;

    private AliSettingsEnum(String desc, String code, Long aliCode, SettingCategoryEnum category) {
        this.desc = desc;
        this.code = code;
        this.aliCode = aliCode;
        this.category = category;
    }

   
    public static Long getAliSettingKey(int type, int code) {
        for (AliSettingsEnum o : AliSettingsEnum.values()) {
            if (o.category.getCode() == type && o.code.equals(String.valueOf(code))) {
                return o.aliCode;
            }
        }
        return null;
    }

}
