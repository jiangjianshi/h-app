/** 
* Project Name: hzf_platform 
* File Name: SubwayRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:52:28 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.Subway;

/**
 * ClassName: SubwayRepository date: 2016年4月26日 下午2:52:28 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface SubwayRepository extends JpaRepository<Subway, Long> {

	/**
	 * 查询指定城市的地铁
	 */
	@Query("select a from Subway a where a.cityId=?1 and a.openStatus=" + Constants.SubwayStatus.OPEN_STATUS_OPEN
			+ " order by a.subwayLineId asc, a.subwayStationId asc")
	public List<Subway> findSubWaysByCityId(long cityId);

	@Query("select a from Subway a where a.latitude=?1 and a.longitude=?2")
	public List<Subway> findByCoordinate(String latitude, String longitude);
	
	@Query("select a from Subway a where a.id=?1 and a.openStatus=" + Constants.SubwayStatus.OPEN_STATUS_OPEN)
	public Subway findSubWayByStationId(long stationId);
	
	@Query("select a from Subway a where a.uid=?1 and a.openStatus=" + Constants.SubwayStatus.OPEN_STATUS_OPEN)
	public List<Subway> findSubWayByUid(String uid);
	
	@Modifying
	@Query("update Subway t set t.latitude=?1 , t.longitude=?2  where t.id = ?3")
	public int setLatAndLngById(String lat, String lng,long id);
}
