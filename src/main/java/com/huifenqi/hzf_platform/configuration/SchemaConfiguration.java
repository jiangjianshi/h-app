/** 
 * Project Name: hzf_platform_project 
 * File Name: SchemaConfiguration.java 
 * Package Name: com.huifenqi.hzf_platform.configuration 
 * Date: 2016年5月5日下午7:29:19 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * ClassName: SchemaConfiguration
 * date: 2016年5月5日 下午7:29:19
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class SchemaConfiguration {

	//收集信息的线程
	@Value("${hfq.schema.collectworker.num}")
	private int collectWorkerNum;
	
	//房源附近地铁的距离
	@Value("${hfq.schema.subway.radius}")
	private int subwayRadius;
	
	//地铁周边的距离
	@Value("${hfq.schema.sbuway.near.circle.radius}")
	private int subwayNearRadius;
	
	@Value("${hfq.schema.subway.walk.speed}")
	private int walkSpeed;
	
	public int getCollectWorkerNum() {
		return collectWorkerNum;
	}

	public void setCollectWorkerNum(int collectWorkerNum) {
		this.collectWorkerNum = collectWorkerNum;
	}

	public int getSubwayRadius() {
		return subwayRadius;
	}

	public void setSubwayRadius(int subwayRadius) {
		this.subwayRadius = subwayRadius;
	}

	public int getSubwayNearRadius() {
		return subwayNearRadius;
	}

	public void setSubwayNearRadius(int subwayNearRadius) {
		this.subwayNearRadius = subwayNearRadius;
	}

	public int getWalkSpeed() {
		return walkSpeed;
	}

	public void setWalkSpeed(int walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
	
	
}
