/** 
* Project Name: hzf_platform 
* File Name: AreaRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:51:52 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.Area;

/**
 * ClassName: AreaRepository date: 2016年4月26日 下午2:51:52 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

	@Query("select a from Area a where districtId in ?1 order by districtId asc, sort asc")
	public List<Area> findAreasByDistrictIds(List<Long> districtIds);

	@Query("select a from Area a where a.districtId=?1 order by districtId asc, sort asc")
	public List<Area> findAreasByDistrictId(long districtId);
	
	@Query("select a from Area a where a.cityId=?1 order by districtId asc, sort asc")
	public List<Area> findAreasByCityId(long cityId);

	@Query("select a from Area a where districtId in ?1 and status=" + Constants.AreaStatus.AREA_STATUS_HOT
			+ " order by districtId asc, sort asc")
	public List<Area> findHotAreaByDistrictIds(List<Long> districtIds);

	@Query("select a from Area a where districtId in ?1 and status=" + Constants.AreaStatus.AREA_STATUS_HOT
			+ " order by districtId asc, sort asc")
	public List<Area> findHotAreaByDistrictIds(List<Long> districtIds, Pageable pageable);

	@Query("select a from Area a where a.cityId=?1 and status=" + Constants.AreaStatus.AREA_STATUS_HOT
			+ " order by districtId asc, sort asc")
	public List<Area> findHotAreaByCityId(long cityId);

	@Query("select a from Area a where a.cityId=?1 and status=" + Constants.AreaStatus.AREA_STATUS_HOT
			+ " order by districtId asc, sort asc")
	public List<Area> findHotAreaByCityId(long cityId, Pageable pageable);

	public Area findAreaByName(String name);

	@Modifying
	@Query("update Area a set a.status=" + Constants.AreaStatus.AREA_STATUS_HOT + " where a.areaId = ?1"
			+ " and a.cityId = ?2")
	public int setHotAreaByIdAndCityId(long id, long cityId);

	@Modifying
	@Query("update Area a set a.status=" + Constants.AreaStatus.AREA_STATUS_COMM + " where a.areaId = ?1"
			+ " and a.cityId = ?2")
	public int unsetHotAreaByIdAndCityId(long id, long cityId);

	@Modifying
	@Query("update Area a set a.status=" + Constants.AreaStatus.AREA_STATUS_COMM + " where a.cityId = ?1")
	public int unsetHotAreaByCityId(long cityId);
}
