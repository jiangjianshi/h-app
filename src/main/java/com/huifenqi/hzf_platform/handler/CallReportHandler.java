package com.huifenqi.hzf_platform.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alicom.mns.tools.MessageListener;
import com.aliyun.mns.model.Message;
import com.huifenqi.hzf_platform.context.entity.phone.CallReport;
import com.huifenqi.hzf_platform.utils.GsonUtils;

@Service
public class CallReportHandler implements MessageListener {

	@Autowired
	private SecretPhoneHandler secretPhoneHandler;

	private static Logger logger = LoggerFactory.getLogger(CallReportHandler.class);

	@Override
	public boolean dealMessage(Message message) {

		logger.info("message id={},message dequeue count={}", message.getMessageId(), message.getDequeueCount());
		logger.error(GsonUtils.beanToJson(message));
		try {
			@SuppressWarnings("unchecked")
			CallReport report = GsonUtils.getInstace().fromJson(message.getMessageBodyAsString(), CallReport.class);
			logger.error(GsonUtils.beanToJson(report));
			secretPhoneHandler.saveCallInfoRecord(report);
		} catch (com.google.gson.JsonSyntaxException e) {
			logger.error("error_json_format:" + message.getMessageBodyAsString(), e);
			//理论上不会出现格式错误的情况，所以遇见格式错误的消息，只能先delete,否则重新推送也会一直报错
			return true;
		} catch (Throwable e) {
			//您自己的代码部分导致的异常，应该return false,这样消息不会被delete掉，而会根据策略进行重推
			logger.error("通话信息保存失败.", e);
			return false;
		}
		return true;
	}

}
