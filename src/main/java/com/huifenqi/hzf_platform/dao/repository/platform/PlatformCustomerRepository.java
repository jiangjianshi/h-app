/** 
* Project Name: hzf_platform 
* File Name: PlatformCustomerRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:43:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.platform.PlatformCustomer;

/**
 * ClassName: PlatformCustomerRepository date: 2016年4月26日 下午2:43:50 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface PlatformCustomerRepository extends JpaRepository<PlatformCustomer, Long> {

	@Query("select a from PlatformCustomer a" + " where a.appId=?1  and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	PlatformCustomer findByAppId(String appId);
	
	@Query("select a from PlatformCustomer a" + " where a.source=?1  and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	PlatformCustomer findBySource(String source);
	
	@Query("select a from PlatformCustomer a" + " where a.isImg="+Constants.platform.IS_IMG_YES+"and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<PlatformCustomer> findByisImgs();
	
	String condition = Constants.PlatformStatus.DENY_SOLR + "," + Constants.PlatformStatus.DENY_BOTH;
	@Query("select a.source from PlatformCustomer a where a.permissionStatus " + "in(" + condition + ") and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<String> findAllWithoutPermission();
	
	String conditionS = Constants.PlatformStatus.DENY_SOLR + "," + Constants.PlatformStatus.DENY_BOTH;
	@Query("select a from PlatformCustomer a where a.permissionStatus " + "in(" + conditionS + ") and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<PlatformCustomer> findAllWithoutPermissions();
	

}
