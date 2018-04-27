/** 
* Project Name: hzf_platform 
* File Name: HousePictureRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:35:55 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.third;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUser;

/**
 * ClassName: ThirdSysUserRepository date: 2017年12月23日 下午2:35:55 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface ThirdSysUserRepository extends JpaRepository<ThirdSysUser, Long> {
    
    //查询待初始化的闲鱼用户  未删除  已同步  未使用
	@Query("select a from ThirdSysUser a" + " where a.companyId=?1 and a.isDelete=" + ThridSysConstants.ThirdSysUserUtil.IS_DELETE_USER_NO+" and a.status="+ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_SUCCESS+" and a.isUse="+ThridSysConstants.ThirdSysUserUtil.IS_USER_USE_INIT)
	List<ThirdSysUser> findInitUserByCompanyId(String companyId);

	//查询待添加闲鱼用户  未删除   未同步
	@Query("select a from ThirdSysUser a" + " where a.companyId=?1 and a.isDelete=" + ThridSysConstants.ThirdSysUserUtil.IS_DELETE_USER_NO+" and a.status="+ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_INIT)
    List<ThirdSysUser> findAddUserByCompanyId(String companyId);
	
	//查询待删除闲鱼用户  已删除  未同步
	@Query("select a from ThirdSysUser a" + " where a.companyId=?1 and a.isDelete=" + ThridSysConstants.ThirdSysUserUtil.IS_DELETE_USER_YES+" and a.status="+ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_INIT)
    List<ThirdSysUser> findDelUserByCompanyId(String companyId);
	
	//查询待处理闲鱼用户 未同步
    @Query("select a from ThirdSysUser a" + " where a.companyId=?1  and a.status="+ThridSysConstants.ThirdSysUserUtil.ThIRd_SYS_STATUS_INIT)
    List<ThirdSysUser> findAllUserByCompanyId(String companyId);
    
}
