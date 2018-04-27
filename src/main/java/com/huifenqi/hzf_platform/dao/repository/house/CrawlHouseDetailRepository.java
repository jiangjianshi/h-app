/** 
* Project Name: hzf_platform 
* File Name: HousePictureRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:35:55 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.CrawlHouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;

/**
 * ClassName: HousePictureRepository date: 2016年4月26日 下午2:35:55 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface CrawlHouseDetailRepository extends JpaRepository<CrawlHouseDetail, Long> {

	@Modifying
	@Query("update CrawlHouseDetail t set t.isRun = ?2 where t.id = ?1")
	int setIsRunById(String id,int isRun);
	
	@Modifying
	@Query("update CrawlHouseDetail t set t.isDelete = ?1 where t.appId = ?2")
	int setIsDeleteByAppId(int isDelete,int appId);

//	@Query("select t from CrawlHouseDetail t where t.isRun=?1 and t.isDelete="
//			+ Constants.Common.STATE_IS_DELETE_NO)
//	List<CrawlHouseDetail> findFirst100AllByIsRun(int isRun);
	
	@Query("select departmentId from CrawlHouseDetail t where t.appId=?1 and  t.isDelete="+ Constants.Common.STATE_IS_DELETE_NO)
	List<String> findFirstAllByAppId(int appId);
	
	@Query("select t from CrawlHouseDetail t where t.appId=?1 and  t.isDelete="+ Constants.Common.STATE_IS_DELETE_NO)
	List<CrawlHouseDetail> findAllByAppId(int appId);
	
	List<CrawlHouseDetail> findFirst100ByIsRunAndIsDelete(int isRun, int isDelete);
	
	List<CrawlHouseDetail> findFirst100ByIsRunAndIsDeleteAndDelFlag(int isRun, int isDelete,int delFlag);
	
	List<CrawlHouseDetail> findFirst100ByDepartmentIdNotInAndIsRunAndIsDeleteOrderByCrawlTimeAsc(List<String> departmentId, int isRun,
			int isDelete);
	
	List<CrawlHouseDetail> findFirst100ByDepartmentIdNotInAndIsRunAndIsDeleteAndDelFlagOrderByCrawlTimeAsc(List<String> departmentId, int isRun,
			int isDelete,int delFlag);
		
	@Query("select a from CrawlHouseDetail a" + " where a.departmentId=?1")
	CrawlHouseDetail findByDepartmentId(String departmentId);
	
	
}
