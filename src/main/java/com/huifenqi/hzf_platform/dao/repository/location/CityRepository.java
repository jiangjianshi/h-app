/** 
* Project Name: hzf_platform 
* File Name: CityRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:49:43 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.City;

/** 
* ClassName: CityRepository
* date: 2016年4月26日 下午2:49:43
* Description: 
* 
* @author maoxinwu 
* @version  
* @since JDK 1.8 
*/
public interface CityRepository extends JpaRepository<City, Long> {
	
	@Query("select c from City c  where c.status="+ Constants.CityStatus.OPEN_STATUS_OPEN+ " order by c.sort asc")
	public List<City> findCityByStatus();
	
	@Query("select c from City c where c.cityId=?1")
	public City findCityById(long cityId);
	
	@Query("select c from City c where c.cityId=?1 and c.status="+ Constants.CityStatus.OPEN_STATUS_OPEN)
	public City findCityStatusById(long cityId);
	
	public City findCityByName(String name);
	
    @Query("select c from City c where c.name like ?1 and c.status="+ Constants.CityStatus.OPEN_STATUS_OPEN)
	public List<City> findCityByNameLike(String cityName);

	@Query("select c from City c where c.cityId in ('1101','3101','4403')")
	public List<City> findSteadyCity();
	
	@Query("select c from City c where c.status="+ Constants.CityStatus.OPEN_STATUS_OPEN+ " order by c.sort asc")
	public List<City> findOpenCityList();
	
	@Query("select c.cityId from City c where c.hasSubway = 1 and c.status="+ Constants.CityStatus.OPEN_STATUS_OPEN+ " order by c.sort asc")
	public List<Long> findOpenedHasSubwayCityIdList();
	
	
}