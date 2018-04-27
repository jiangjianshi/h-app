package com.huifenqi.hzf_platform.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.utils.Configuration;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YDM
 */
@Component
public class SmsService {

    /**
     * 短信验证码来源类型 - 会分期
     */
    public static final String SOURCE_TYPE_HFQ = "1";

    /**
     * 短信验证码来源类型 - 好贷网
     */
    public static final String SOURCE_TYPE_HAODAI = "2";

    /**
     * 验证成功
     */
    public static final int VERIFY_RET_OK = 0;

    /**
     * 短信服务接口的token
     */
    private static final String SMS_SERVICE_TOKEN = "T4SWVxHQNy38QM3S";

    @Autowired
    private Configuration configuration;

    private static Logger logger = LoggerFactory.getLogger(SmsService.class);


    /**
     * 获取短信验证码
     *
     * @param phone
     * @param sourceType
     * @param platform
     * @param headers
     */
    public void requestSmsCaptcha(String phone, String sourceType, String platform, Map<String, String> headers) {
        requestCaptcha(phone, "1", sourceType, platform, headers);
    }

    /**
     * 获取语音验证码
     *
     * @param phone
     * @param sourceType
     * @param platform
     * @param headers
     */
    public void requestVoiceCaptcha(String phone, String sourceType, String platform, Map<String, String> headers) {
        requestCaptcha(phone, "2", sourceType, platform, headers);
    }

    /**
     * 获取短信或者语音验证码
     *
     * 参考文档：http://123.56.135.63:9000/pages/viewpage.action?pageId=4587716#id-短信平台-1.发送验证码
     *
     * @param phone
     * @param verifyType 验证类型：1：短信 2：语音
     * @param sourceType 1：会分期，2：好贷。好贷只支持短信验证，不支持语音
     * @param platform
     * @param headers 需要从用户请求header中获取，并将放在调用service接口的header中
     */
    public void requestCaptcha(String phone, String verifyType, String sourceType, String platform,
                               Map<String, String> headers) {
        String url = String.format("http://%sverify/send_verify/", configuration.serviceSmsHost);
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("verify_type", verifyType);
        params.put("source_type", sourceType);
        params.put("platform", platform);

        Pair<Long, String> sign = genSmsSign(phone);
        params.put("sign", sign.getRight());
        params.put("timestamp", sign.getLeft() + "");

        params.put("client_ip", headers.get("client_ip"));
        params.put("user_agent", headers.get("user_agent"));
        params.put("referer", headers.get("referer"));

        logger.info("The Phone(" + phone + ") request sms captcha, sourceType=" + sourceType);

        String postRet = null;
        try {
            postRet = HttpUtil.post(url, params);
        } catch (Exception e) {
        	throw new BaseException(e);
        }

        ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
        if (!String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code)) {
        	throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, result.status.description);
        }
    }


    /**
     * 获取短信或者语音验证码
     *
     * 参考文档：http://123.57.69.21:9000/pages/viewpage.action?pageId=4587716#id-短信平台-11.自定义签名验证码接口
     *
     * @param phone
     * @param platform
     * @param headers 需要从用户请求header中获取，并将放在调用service接口的header中
     */
    public void requestHzfCaptcha(String phone, String platform,
                               Map<String, String> headers) {
        String url = String.format("http://%sverify/send_platforms_verify/", configuration.serviceSmsHost);
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("sign_id", 10000+"");
        params.put("platform", platform);
        params.put("template_id", configuration.hfqHzfTemplateId);
        Pair<Long, String> sign = genSmsSign(phone);

        params.put("client_ip", headers.get("client_ip"));

        logger.info("The Phone(" + phone + ") request sms captcha, client_ip=" +headers.get("client_ip") );

        String postRet = null;
        try {
            postRet = HttpUtil.post(url, params);
        } catch (Exception e) {
        	 throw new BaseException(e);
        }

        ApiResult result = GsonUtil.buildGson().fromJson(postRet, ApiResult.class);
        if (!String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code)) {
        	throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, result.status.description);
        }
    }

    /**
     * 校验验证码
     *
     * @param phone
     * @param captcha
     * @return
     */
    public int verifyCaptcha(String phone, String captcha) {
        String url = String.format("http://%s:%s/verify/verify_code/",
                configuration.serviceSmsHost, configuration.serviceSmsPort);

        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", captcha);

        String ret = null;
        try {
            ret = HttpUtil.post(url, params);
        } catch (Exception e) {
        	throw new BaseException(e);
        }

        ApiResult result = GsonUtil.buildGson().fromJson(ret, ApiResult.class);
        if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code)) {
            return VERIFY_RET_OK;
        } else {
        	throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, result.status.description);
        }
    }

    /**
     * 生成签名
     *
     * @param phone
     * @return
     */
    private Pair<Long, String> genSmsSign(String phone) {
        long timestamp = System.currentTimeMillis();
        String sign = DigestUtils.md5Hex(SMS_SERVICE_TOKEN + phone + timestamp);

        return Pair.of(timestamp, sign);
    }
    
    /**
     * 发送普通模板短信
     * @param phone
     * @param templateId
     * @param datas
     * @return
     */
	public Integer sendCommonSMS(String phone, String templateId, String datas) {

		String url = String.format("http://%ssms/send_platforms_sms/", configuration.serviceSmsHost);
		Map<String, String> params = new HashMap<>();
		params.put("phone", phone);
		params.put("sign_id", "10000");
		params.put("template_id", templateId);
		params.put("datas", datas);

		String ret = null;
		try {
			ret = HttpUtil.post(url, params);
		} catch (Exception e) {
			throw new BaseException(e);
		}

		ApiResult result = GsonUtil.buildGson().fromJson(ret, ApiResult.class);
		if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(result.status.code)) {
			return VERIFY_RET_OK;
		} else {
			throw new BaseException(ErrorMsgCode.ERRCODE_SERVICE_ERROR, result.status.description);
		}
	}
	
}
