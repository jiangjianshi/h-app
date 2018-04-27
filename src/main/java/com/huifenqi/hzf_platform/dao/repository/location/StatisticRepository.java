/** 
* Project Name: hzf_platform 
* File Name: ProvinceRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:50:16 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import com.huifenqi.hzf_platform.context.entity.location.StatisticReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
* ClassName: ProvinceRepository
* date: 2017年9月28日 下午2:50:16
* Description: 
* 
* @author maoxinwu 
* @version  
* @since JDK 1.8 
*/

public interface StatisticRepository extends JpaRepository<StatisticReport, Long> {

    @Query("SELECT new com.huifenqi.hzf_platform.context.entity.location.StatisticReport(sum(sr.ftol)," +
            "sum(sr.fpv),sum(sr.fss),sum(sr.fbdj),sum(sr.fjg)," +
            "sum(sr.fppgy),sum(sr.fhz),sum(sr.fzz),sum(sr.ffq), sr.strTime) " +
            " FROM com.huifenqi.hzf_platform.context.entity.location.StatisticReport sr" +
            " where (sr.strTime> ?1 and sr.strTime< ?2) GROUP BY sr.strTime")
    List<StatisticReport> getStatisticReport(String strTimeBeg,String strTimeEnd);
}
