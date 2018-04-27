/** 
 * Project Name: hzf_platform_project 
 * File Name: PhoneCallRecordRepository.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.house 
 * Date: 2016年5月19日下午4:13:27 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.repository.CrudRepository;

import com.huifenqi.hzf_platform.context.entity.location.PhoneCallRecord;

/** 
 * ClassName: PhoneCallRecordRepository
 * date: 2016年5月19日 下午4:13:27
 * Description: 
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public interface PhoneCallRecordRepository extends CrudRepository<PhoneCallRecord, Long> {

}
