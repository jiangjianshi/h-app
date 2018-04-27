/** 
 * Project Name: hzf_platform_project 
 * File Name: BaiduConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月4日下午2:55:16 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: BaiduConfiguration
 * date: 2016年5月4日 下午2:55:16
 * Description: 
 * 
 * @author changmingwei 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class TaskConfiguration {
	
	@Value("${hfq.baidu.api.house.task}")
	private String task;

	@Value("${hfq.houseTag.monthPay.house.task}")
	private String housetTagTask;

	@Value("${hzf.hzfplatform.schedule}")
	private boolean scheduleStatus;
	
	@Value("${spring.profiles.active}")
	private String activeEnv;

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public boolean isScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(boolean scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public String getHousetTagTask() {
		return housetTagTask;
	}

	public void setHousetTagTask(String housetTagTask) {
		this.housetTagTask = housetTagTask;
	}

	public String getActiveEnv() {
		return activeEnv;
	}

	public void setActiveEnv(String activeEnv) {
		this.activeEnv = activeEnv;
	}
	
}
