/** 
* Project Name: hzf_platform 
* File Name: HousePictureRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:35:55 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.third;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.third.SaasApartmentInfo;

/**
 * ClassName: SaasApartMnetInfoRepository date: 2017年5月10日 下午2:35:55 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface SaasApartMnetInfoRepository extends JpaRepository<SaasApartmentInfo, Long> {

	@Query("select a from SaasApartmentInfo a" + " where a.apartId=?1")
	SaasApartmentInfo findByApartId(String apartId);	
	
}
