/** 
* Project Name: hzf_platform 
* File Name: PlatformPermission.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:45:08 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.platform;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huifenqi.hzf_platform.context.entity.platform.PlatformPermission;

/**
 * ClassName: PlatformPermissionRepository date: 2016年4月26日 下午2:45:08
 * Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface PlatformPermissionRepository extends JpaRepository<PlatformPermission, Long> {

}
