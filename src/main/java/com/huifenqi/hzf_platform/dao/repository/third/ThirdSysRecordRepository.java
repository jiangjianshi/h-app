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

import com.huifenqi.hzf_platform.context.entity.third.ThirdSysRecord;

/**
 * ClassName: ThirdSysRecordRepository date: 2017年5月10日 下午2:35:55 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface ThirdSysRecordRepository extends JpaRepository<ThirdSysRecord, Long> {

	@Query("select a from ThirdSysRecord a" + " where a.optStatus=?1")
	List<ThirdSysRecord> findAllByOptStatus(String optStatus);	
	
	@Query("select a from ThirdSysRecord a" + " where a.houseSellId=?1 and a.roomId=?2")
    ThirdSysRecord findBySellIdRoomId(String sellId,int roomId); 
	
	@Query("select a from ThirdSysRecord a" + " where a.houseSellId=?1")
	List<ThirdSysRecord> findBySellId(String sellId); 
}
