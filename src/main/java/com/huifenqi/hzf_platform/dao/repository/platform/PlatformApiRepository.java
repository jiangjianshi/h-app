/** 
* Project Name: hzf_platform 
* File Name: PlatformApiRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:46:08 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.platform;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huifenqi.hzf_platform.context.entity.platform.PlatformApi;

/** 
* ClassName: PlatformApiRepository
* date: 2016年4月26日 下午2:46:08
* Description: 
* 
* @author maoxinwu 
* @version  
* @since JDK 1.8 
*/
public interface PlatformApiRepository extends JpaRepository<PlatformApi, Long> {

}
