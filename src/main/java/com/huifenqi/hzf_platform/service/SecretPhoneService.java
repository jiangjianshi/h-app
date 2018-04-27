package com.huifenqi.hzf_platform.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnRequest;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse;
import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse.SecretBindDTO;
import com.aliyuncs.dyplsapi.model.v20170525.QueryRecordFileDownloadUrlRequest;
import com.aliyuncs.dyplsapi.model.v20170525.QueryRecordFileDownloadUrlResponse;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionRequest;
import com.aliyuncs.dyplsapi.model.v20170525.UnbindSubscriptionResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.huifenqi.hzf_platform.configuration.SecretPhoneConfiguration;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;

/**
 * 
 * @author jjs
 *
 */
@Service
public class SecretPhoneService {

	@Resource
	private SecretPhoneConfiguration config;

	private static Logger logger = LoggerFactory.getLogger(SecretPhoneService.class);

	private static final String product = "Dyplsapi";
	static final String domain = "dyplsapi.aliyuncs.com";
	private static IClientProfile profile = null;

	private void initEnv() {

		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化acsClient,暂不支持region化
		profile = DefaultProfile.getProfile("cn-hangzhou", config.alicomAccessKeyId, config.alicomAccessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * AXN绑定
	 * 
	 * @return
	 * @throws ClientException
	 * @throws ServerException
	 */
	public SecretBindDTO bindAxn(String agencyPhone, String secretNo, String outId) {
		initEnv();

		IAcsClient acsClient = new DefaultAcsClient(profile);
		BindAxnRequest request = new BindAxnRequest();
		request.setPhoneNoA(agencyPhone);//必填:AXN关系中的A号码
		request.setPhoneNoX(secretNo);//X 关系号
		Date date = DateUtils.addMinutes(new Date(), 5);
		request.setExpiration(DateUtil.formatDateTime(date));//必填:绑定关系对应的失效时间-不能早于当前系统时间
		request.setPoolKey(config.poolKey);
//		request.setNoType("NO_95");
		request.setIsRecordingEnabled(true);//是否需要录音，默认false
		request.setOutId(outId);
		//hint 此处可能会抛出异常，注意catch
		BindAxnResponse axnResponse = null;
		try {
			axnResponse = acsClient.getAcsResponse(request);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		logger.info("号码绑定：agencyPhone={}，secretNo={}", agencyPhone, secretNo);
		if ("OK".equals(axnResponse.getCode())) {
			logger.info("bindDto={}", axnResponse.getSecretBindDTO().getSecretNo(),
					axnResponse.getSecretBindDTO().getSubsId());
			return axnResponse.getSecretBindDTO();
		} else {
			logger.error("关系号绑定失败，code={}, message={}", axnResponse.getCode(), axnResponse.getMessage());
		}
		return null;
	}

	/**
	 * 订购关系解绑(解绑接口不区分AXB、AXN)
	 * 
	 * @return
	 */
	public boolean unbind(String subsId, String secretNo) {
		initEnv();

		IAcsClient acsClient = new DefaultAcsClient(profile);
		UnbindSubscriptionRequest request = new UnbindSubscriptionRequest();
		request.setPoolKey(config.poolKey);
		request.setSecretNo(secretNo);
		request.setSubsId(subsId);
		UnbindSubscriptionResponse response = null;
		try {
			response = acsClient.getAcsResponse(request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解绑失败", e);
			return false;
		}
		logger.info(response.getMessage());
		return "OK".equals(response.getCode());

	}

	/**
	 * 获取通话录音
	 * 
	 * @param callId
	 * @param callTime
	 * @return
	 */
	public String getVoiceRecordUrl(String callId, String callTime) {

		initEnv();

		IAcsClient acsClient = new DefaultAcsClient(profile);
		QueryRecordFileDownloadUrlRequest request = new QueryRecordFileDownloadUrlRequest();
		//对应的号池Key
		request.setPoolKey(config.poolKey);
		//话单回执中返回的标识每一通唯一通话行为的callId
		request.setCallId(callId);
		request.setCallTime(callTime);
		//hint 此处可能会抛出异常，注意catch
		QueryRecordFileDownloadUrlResponse response = new QueryRecordFileDownloadUrlResponse();
		try {
			response = acsClient.getAcsResponse(request);
		} catch (Exception e) {
			logger.error("调用获取录音接口失败", e);
		}
		String downloadUrl = "";
		if (response.getCode() != null && response.getCode().equals("OK")) {
			downloadUrl = response.getDownloadUrl();
			logger.info(GsonUtil.beanToJson(response));
		} else {
			logger.error("获取录音失败,callId={}, callTime={}, code={}, message={}", callId, callTime, response.getCode(),
					response.getMessage());
		}
		return downloadUrl;
	}
}
