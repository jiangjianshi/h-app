package com.huifenqi.hzf_platform.monitor;

import java.text.ParseException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alicom.mns.tools.DefaultAlicomMessagePuller;
import com.aliyuncs.exceptions.ClientException;
import com.huifenqi.hzf_platform.configuration.SecretPhoneConfiguration;
import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.handler.CallReportHandler;

@Component
@Order(value = 1)
public class ReceiveCallReportListener implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(ReceiveCallReportListener.class);

	@Resource
	private CallReportHandler callReportHandler;
	@Resource
	private SecretPhoneConfiguration config;
	@Resource
	private TaskConfiguration taskConfiguration;

	@Override
	public void run(String... args) {
		
		if (!"production".equals(taskConfiguration.getActiveEnv())) {
			logger.error(">>>>ReceiveCallReportListener does not run.<<<<");
			return;
		}
		
		logger.info(">>>>ReceiveCallReportListener starts successfully.<<<<");

		DefaultAlicomMessagePuller puller = new DefaultAlicomMessagePuller();
		//设置异步线程池大小及任务队列的大小，还有无数据线程休眠时间
		puller.setConsumeMinThreadSize(2);
		puller.setConsumeMaxThreadSize(6);
		puller.setThreadQueueSize(200);
		puller.setPullMsgThreadSize(1);

		String accessKeyId = config.alicomAccessKeyId;
		String accessKeySecret = config.alicomAccessKeySecret;

		String messageType = "SecretReport";//此处应该替换成相应产品的消息类型
		String queueName = config.secretReportQueue;//在云通信页面开通相应业务消息后，就能在页面上获得对应的queueName,格式类似Alicom-Queue-xxxxxx-SmsReport
		try {
			puller.startReceiveMsg(accessKeyId, accessKeySecret, messageType, queueName, callReportHandler);
		} catch (ClientException e) {
			logger.error("获取通话记录线程，初始化失败>>>", e);
		} catch (ParseException e) {
			logger.error("解析失败", e);
		}

	}

}
