package com.huifenqi.hzf_platform.context.enums.ali;

import java.security.InvalidParameterException;

import com.google.common.base.Preconditions;
import com.huifenqi.hzf_platform.context.Constants;

public enum FaceEnum {

    EAST("东","东向", 10001L, 1L),
    WEST("西","西向", 10002L, 3L),
    SOUTH("南","南向", 10003L, 2L),
    NORTH("北","北向", 10004L, 4L),
    SOUTHWEST("西南","西南朝向", 10023L, 8L),
    NORTHWEST("西北","西北朝向", 10024L, 5L),
    NORTHEAST("东北","东北朝向", 10014L, 6L),
    SOUTHEAST("东南","东南朝向", 10013L, 7L),
    NORTH_SOUTH("南北","南北朝向", 10034L, 10L),
    WEST_EAST("东西","东西朝向", 10012L, 9L);

    private String  desc;
    private String  titleDesc;
    private Long code;
    private Long aliCode;

    private FaceEnum(String desc,String titleDesc, Long code, Long aliCode) {
        this.desc = desc;
        this.titleDesc = titleDesc;
        this.code = code;
        this.aliCode = aliCode;
    }
    
    public static String getCodeByDesc(String desc) {
        Preconditions.checkArgument( desc != null, "parameter desc is null." );

        for (FaceEnum o : FaceEnum.values()) {
            if(o.desc.equals(desc)){
                return String.valueOf(o.code);
            }
        }
        return String.valueOf(Constants.AliHouseDetail.NOTFOUND);
    }
    
    
    public static Long getCode(Long aliCode) {
        Preconditions.checkArgument( aliCode != null, "parameter aliCode is null." );

        for (FaceEnum o : FaceEnum.values()) {
            if(o.aliCode.equals(aliCode)){
                return o.code;
            }
        }
        return Constants.AliHouseDetail.NOTFOUND;
    }
    
    public static Long getAliCode(Long code) {
        Preconditions.checkArgument( code != null, "parameter code is null." );

        for (FaceEnum o : FaceEnum.values()) {
            if(o.code.equals(code)){
                return o.aliCode;
            }
        }
        return null;
    }
    
    
    public static String getTitleDesc(Long code) {
        if(code == null){
            throw new InvalidParameterException("parameter code is null.");
        }
        for (FaceEnum o : FaceEnum.values()) {
            if(o.code.equals(code)){
                return o.titleDesc;
            }
        }
        return null;
    }
}
