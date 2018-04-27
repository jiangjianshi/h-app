/** 
* Project Name: hzf_platform 
* File Name: DistrictRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:51:02 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.District;

/**
 * ClassName: DistrictRepository date: 2016年4月26日 下午2:51:02 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface DistrictRepository extends JpaRepository<District, Long> {

	/**
	 * 查询指定城市的区域
	 * 
	 * @param cityId
	 * @return
	 */
	@Query("select d from District d where cityId=?1 and status="+ Constants.DistrictStatus.OPEN_STATUS_OPEN+" order by sort asc")
	public List<District> findDistrictsByCityId(long cityId);
	
	/**
	 * 根据名称查询区域
	 * @param name
	 * @return
	 */
	public District findDistrictByName(String name);
	
	/**
	 * 根据名称查询区域
	 * @param name,cityId
	 * @return
	 */
	public District findDistrictByNameAndCityId(String name,long cityId);
}
