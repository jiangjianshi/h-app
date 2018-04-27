/** 
 * Project Name: hzf_platform_project 
 * File Name: CityInfoVersionRepository.java 
 * Package Name: com.huifenqi.hzf_platform.dao.repository.location 
 * Date: 2016年5月18日下午8:37:34 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.repository.location;

import org.springframework.data.repository.CrudRepository;

import com.huifenqi.hzf_platform.context.entity.location.CityInfoVersion;

/**
 * ClassName: CityInfoVersionRepository date: 2016年5月18日 下午8:37:34 Description:
 * 
 * @author xiaozhan
 * @version
 * @since JDK 1.8
 */
public interface CityInfoVersionRepository extends CrudRepository<CityInfoVersion, Long> {

//	@Query("select c from CityInfoVersion as c where c.cityId=?1 and c.infoTyep=?2")
//	public CityInfoVersion findInfoVersionBy(long cityId, int infoType);

	CityInfoVersion findByInfoType(Integer infoType);
}
