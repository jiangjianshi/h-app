/** 
* Project Name: hzf_platform 
* File Name: HouseDetailRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:31:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.CompanyWhiteConfig;

/**
 * ClassName: CompanyWhiteConfigRepository date: 2016年11月20日 下午2:31:30 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface CompanyWhiteConfigRepository extends JpaRepository<CompanyWhiteConfig, Long> {
	
	@Query("select a from CompanyWhiteConfig a"+" where a.source=?1 and a.companyId=?2 and status="+ Constants.CompanyOffConfig.OPEN_YES_STATUS)
	public CompanyWhiteConfig findBySourceAndCompanyId(String source,String companyId);
}
