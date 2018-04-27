package com.huifenqi.hzf_platform.monitor;

import java.util.concurrent.DelayQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huifenqi.hzf_platform.context.entity.phone.Message;
import com.huifenqi.hzf_platform.handler.SecretPhoneHandler;

@Component
public class UnbindConsumer implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(UnbindConsumer.class);

	private SecretPhoneHandler secretPhoneHandler;
	private DelayQueue<Message> queue;

	public UnbindConsumer() {

	}

	public UnbindConsumer(DelayQueue<Message> queue, SecretPhoneHandler secretPhoneHandler) {
		this.queue = queue;
		this.secretPhoneHandler = secretPhoneHandler;
	}

	@Override
	public void run() {

		while (queue.size() > 0) {
			try {
				Message unbindMsg = queue.take();
				try {
					boolean result = secretPhoneHandler.unbind(unbindMsg.getId(), unbindMsg.getSecretNo());
					if (!result) {
						reOffer(unbindMsg);
					}
				} catch (Exception e) {
					reOffer(unbindMsg);
				}
				logger.info("消费消息，id={}, secretNo={}", unbindMsg.getId(), unbindMsg.getSecretNo());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void reOffer(Message unbindMsg) {
		queue.offer(unbindMsg);
		logger.error("解绑失败，消息重新放入延时队列");
	}

}
