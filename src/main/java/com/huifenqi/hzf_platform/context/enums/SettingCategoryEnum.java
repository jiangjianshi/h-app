package com.huifenqi.hzf_platform.context.enums;

/**
 * 配置分类
 * @author jjs 2017年4月28日 下午2:00:50
 */
public enum SettingCategoryEnum {

    FURNITURE("家具", 1),
    ELECTRIC("家电", 2),
    HOUSEHOLD("家居", 3),
    OTHER("其他", 4);

    private String  desc;
    private Integer code;

    private SettingCategoryEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
    
}
