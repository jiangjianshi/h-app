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
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 * 
 */
@Component
public class MongoCrawlConfiguration {
	
	@Value("${hfq.mongocrawl.collection.58.name}")
	private String cn;
	
	@Value("${hfq.mongocrawl.collection.mogu.name}")
	private String mgn;
	
	@Value("${hfq.mongocrawl.collention.db}")
	private String cd;
	
	@Value("${hfq.mongocrawl.collention.ip}")
	private String ci;
	
	@Value("${hfq.mongocrawl.collention.port}")
	private int cp;
	
	@Value("${hfq.mongocrawl.collention.pwd}")
	private String pwd;
	
	@Value("${hfq.mongocrawl.collention.user}")
	private String user;
	
	
	
	public String getMgn() {
		return mgn;
	}

	public void setMgn(String mgn) {
		this.mgn = mgn;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}




}
