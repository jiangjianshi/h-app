package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里号码保护（电话转接）配置
 * 
 * @author jjs
 *
 */
@Component
public class SecretPhoneConfiguration {

	@Value("${alicom.AccessKeyId}")
	public String alicomAccessKeyId;

	@Value("${alicom.AccessKeySecret}")
	public String alicomAccessKeySecret;

	@Value("${secretphone.sms.templateId}")
	public String secretPhoneSmsTemplateId;

	@Value("${alicom.PoolKey}")
	public String poolKey;

	@Value("${alicom.SecretReport.Queue}")
	public String secretReportQueue;

}
