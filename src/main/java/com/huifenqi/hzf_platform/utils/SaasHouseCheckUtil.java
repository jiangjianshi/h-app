package com.huifenqi.hzf_platform.utils;

import javax.servlet.http.HttpServletRequest;

import com.huifenqi.hzf_platform.context.SaasConstantsEnum;
import com.huifenqi.hzf_platform.context.dto.request.house.SaasHousePublishDto;
import com.huifenqi.hzf_platform.context.exception.InvalidParameterException;

/**
 * @Description: 百度接口房源字段校验util
 * @Author chenshuai
 * @Date 2017/4/14 0014 17:43
 */
public class SaasHouseCheckUtil {

	public static final String DEFAULT_ENUM_STR = "-1";
    public static final int DEFAULT_ENUM_INT = -1;
    public static final int DEFAULT_INT = 0;
	private static final boolean IS_MUST_ENUM = true; // 字段必填
	private static final boolean NOT_MUST_ENUM = false; // 字段非必填

	public static SaasHousePublishDto getHousePublishDtoStatus(HttpServletRequest request) throws Exception {

        if (request == null) {
            return null;
        }

		SaasHousePublishDto saasHousePublishDto = new SaasHousePublishDto();

        // 分配给合作公寓的接入ID
        String appIdKey = "appId";
		String appId = RequestUtils.getParameterString(request, appIdKey);
		saasHousePublishDto.setAppId(appId);

		/**
		 * 房源编号
		 */
		String sellIdKey = "sellId";
		String sellId = RequestUtils.getParameterString(request, sellIdKey);
		saasHousePublishDto.setSellId(sellId);

        /**
         * 房间编号
         */
        String roomIdKey = "roomId";
		int roomId = RequestUtils.getParameterInt(request, roomIdKey);
		saasHousePublishDto.setRoomId(roomId);

        /**
		 * 置顶状态，0取消置顶 1置顶
		 */
		String isTopKey = "isTop";
		int isTop = RequestUtils.getParameterInt(request, isTopKey);
		checkEnumValue(SaasConstantsEnum.IsTopEnum.checkIndexExist(isTop, IS_MUST_ENUM), isTopKey);
		saasHousePublishDto.setIsTop(isTop);

		return saasHousePublishDto;
    }

	public static SaasHousePublishDto getHousePublishDtoPubType(HttpServletRequest request) throws Exception {

		if (request == null) {
			return null;
		}

		SaasHousePublishDto saasHousePublishDto = new SaasHousePublishDto();

		// 分配给合作公寓的接入ID
		String appIdKey = "appId";
		String appId = RequestUtils.getParameterString(request, appIdKey);
		saasHousePublishDto.setAppId(appId);

		/**
		 * 房源编号
		 */
		String sellIdKey = "sellId";
		String sellId = RequestUtils.getParameterString(request, sellIdKey);
		saasHousePublishDto.setSellId(sellId);

		/**
		 * 房间编号
		 */
		String roomIdKey = "roomId";
		int roomId = RequestUtils.getParameterInt(request, roomIdKey);
		saasHousePublishDto.setRoomId(roomId);

		/**
		 * 发布类型
		 */
		String pubTypeKey = "pubType";
		int pubType = RequestUtils.getParameterInt(request, pubTypeKey);
		saasHousePublishDto.setPubType(pubType);

		return saasHousePublishDto;
	}

	/**
	 * 验证枚举
	 */
	private static void checkEnumValue(boolean isExist, String keyName) {
		if (!isExist) {
			throw new InvalidParameterException("参数不合法:" + keyName);
		}
	}
}
