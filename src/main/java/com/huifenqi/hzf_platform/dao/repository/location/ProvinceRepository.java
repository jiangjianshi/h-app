/** 
* Project Name: hzf_platform 
* File Name: ProvinceRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:50:16 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huifenqi.hzf_platform.context.entity.location.Province;

/** 
* ClassName: ProvinceRepository
* date: 2016年4月26日 下午2:50:16
* Description: 
* 
* @author maoxinwu 
* @version  
* @since JDK 1.8 
*/
public interface ProvinceRepository extends JpaRepository<Province, Long> {

}
