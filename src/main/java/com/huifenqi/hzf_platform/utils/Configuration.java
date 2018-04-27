package com.huifenqi.hzf_platform.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by arison on 2017/9/14.
 *
 * 读取配置文件
 */
@Component
public class Configuration {

	@Value("${hfq.server.domain}")
	public String serverDomain;
    //thereis a bug
	@Value("${hfq.user.service}")
	public String hfqUserServiceIp;
	
	@Value("${hfq.service.sms.host}")
	public String serviceSmsHost;
	
	@Value("${hfq.service.sms.port}")
	public String serviceSmsPort;
	
	@Value("${hfq.service.sms.templateid}")
	public String hfqHzfTemplateId;
	
	@Value("${hfq.greenpass.phone}")
	public String greenpassPhone;

	@Value("${hfq.greenpass.captcha}")
	public String greenpassCaptcha;
	
	@Value("${hfq.service.payment.host}")
	public String servicePaymentHost;

	@Value("${hfq.service.payment.port}")
	public String servicePaymentPort;

	@Value("${hfq.service.payment.jedi.host}")
	public String serviceJediPaymentHost;

	@Value("${hfq.service.payment.jedi.port}")
	public String serviceJediPaymentPort;


	@Value("${hfq.service.payment.authid}")
	public String servicePaymentAuthid;

	@Value("${hfq.service.payment.token}")
	public String servicePaymentToken;

	@Value("${hfq.search.agencyid}")
	public String searchAgencyId;

	@Value("${hfq.hzf.sass}")
	public String hfqHzfSassServiceHost;
	
	@Value("${hfq.hzf.saaslite}")
	public String hfqHzfSaaSLiteServiceHost;

	@Value("${hfq.register.agreement_path}")
	public String agreementPath;

	@Value("${hfq.register.private_agreement_path}")
	public String privateAgreementPath;

	@Value("${hfq.service.mq.host}")
	public String mqHost;

	@Value("${hfq.service.mq.port}")
	public String mqPost;

	@Value("${hfq.service.mq.templateid.apply.success}")
	public String mqSuccessTemplateId;

	@Value("${hfq.service.mq.templateid.apply.fail.city.not.support}")
	public String mqFailTemplateId;

	@Value("${hfq.service.crm.host}")
	public String crmApiHost;

	@Value("${hfq.service.rc.host}")
	public String rcApiHost;

	@Value("${hfq.singed.error.url}")
	public String singedErroUrl;

	@Value("${hfq.compatible.switch}")
	public boolean compatibleSwitch;
	
	@Value("${hfq.pay.yeepay.captcha.length.min}")
	public int minYeepayCaptchaLength;

	@Value("${hfq.pay.yeepay.captcha.length.max}")
	public int maxYeepayCaptchaLength;

	@Value("${hfq.withhold.tip.desc}")
	public String withHoldTip;

	@Value("${hfq.withhold.tip.show}")
	public int showWithHoldTip;

	@Value("${hfq.service.contract.withhold.url}")
	public String withHoldUrl;

	@Value("${hfq.log.service}")
	public String contractLogServiceHost;

	@Value("${hfq.service.contract.host}")
	public String contractApiHost;

	@Value("${hfq.logoff.daylimit}")
	public long invalidDayLimit;

	@Value("${hfq.logoff.hourlimit}")
	public long invalidHourLimit;

	@Value("${hfq.statistic.emails}")
	public String wxToMails;

	@Value("${hfq.statistic.excel.path}")
	public String xlsPath;
	
	// 公寓板块显示城市集合
	@Value("${hfq.hzf.apartment.cityIds}")
	public String apartmentShowCityIds;
	
	// 活动标识显示标识
	@Value("${hfq.hzf.activity.showFlag}")
	public String activityShowFlag;
	
	// 大礼包活动跳转链接
	@Value("${hfq.hzf.activity.destinationUrl}")
	public String destinationUrl;
	
	// 首页展示的礼包图片
	@Value("${hfq.hzf.activity.giftImgUrl}")
	public String giftImgUrl;
	
	// 抽奖页面图片展示
	@Value("${hfq.hzf.activity.showImgUrl}")
	public String showImgUrl;

	// 抽奖页面图片展示
	@Value("${hfq.service.payment.hzfmerchid}")
	public String hzfMerchId;
	// 抽奖页面图片展示
	@Value("${hfq.service.payment.jedi.pubkey.file}")
	public String pubKeyFile;
	// 抽奖页面图片展示
	@Value("${hfq.service.payment.jedi.prikey.file}")
	public String  privKeyFile;

	@Value("${wx.hzf.appid}")
	public String hzfAppid;

	@Value("${wx.hzf.secret}")
	public String hzfSecret;

	@Value("${hfq.server.h5.domain}")
	public String serverH5Domain;
	
	@Value("${hfq.service.smart.host}")
	public String smartServiceHost;

	@Value("${hfq.order.system.id}")
	public String orderSystemId;

	public String getPaymentServiceUrl() {
		return String.format("http://%s:%s/", servicePaymentHost, servicePaymentPort);
	}

	public String getPaymentNewServiceUrl() {
		return String.format("http://%s:%s/", serviceJediPaymentHost, serviceJediPaymentPort);
	}
}
