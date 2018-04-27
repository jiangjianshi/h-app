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

import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysCompany;

/**
 * ClassName: ThirdSysRecordRepository date: 2017年5月10日 下午2:35:55 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface ThirdSysCompanyRepository extends JpaRepository<ThirdSysCompany, Long> {
	
	@Query("select a from ThirdSysCompany a" + " where  a.source=?1 and a.companyId=?2 and a.status="+ThridSysConstants.ThirdSysCompanyUtil.COMPANY_STATUS_YES)
	ThirdSysCompany findBySourceAndCompanyId(String source,String companyId);  
}
