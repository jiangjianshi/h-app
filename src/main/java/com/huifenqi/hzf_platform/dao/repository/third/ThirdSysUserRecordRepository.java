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
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUserRecord;

/**
 * ClassName: ThirdSysUserRepository date: 2017年12月23日 下午2:35:55 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface ThirdSysUserRecordRepository extends JpaRepository<ThirdSysUserRecord, Long> {

    @Query("select a from ThirdSysUserRecord a" + " where a.userId=?1 and a.status=" + ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_SUCCESS)
    List<ThirdSysUserRecord> findAllByUserId(String userId);
    
    @Query("select count(1) from ThirdSysUserRecord a" + " where a.companyId=?1 and a.userId=?2 and a.status=" + ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_SUCCESS+" and a.houseStatus=" + ThridSysConstants.ThirdSysUserUtil.STATUS_ALI_WAIT_INT)
    int findCountByUserId(String companyId,String userId);
    
    @Query("select a from ThirdSysUserRecord a" + " where a.houseSellId=?1  and a.roomId=?2 and a.status=" + ThridSysConstants.ThirdSysRecordUtil.ThIRd_SYS_STATUS_SUCCESS)
    ThirdSysUserRecord findUserId(String sellId,int roomId);
    
}
