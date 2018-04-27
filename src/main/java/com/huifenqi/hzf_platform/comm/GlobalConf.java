package com.huifenqi.hzf_platform.comm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by arison on 2015/11/23.
 */
@Component
@ConfigurationProperties(prefix = "gconf")
//@PropertySource("classpath:application-globalconf.properties")
public class GlobalConf {

	public Map<String, String> payment;

	public Map<String, String> register;

	public Map<String, String> show;

	public Map<String, String> getPayment() {
		return payment;
	}

	public void setPayment(Map<String, String> payment) {
		this.payment = payment;
	}

	public Map<String, String> getRegister() {
		return register;
	}

	public void setRegister(Map<String, String> register) {
		this.register = register;
	}

	public Map<String, String> getShow() {
		return show;
	}

	public void setShow(Map<String, String> show) {
		this.show = show;
	}

}
