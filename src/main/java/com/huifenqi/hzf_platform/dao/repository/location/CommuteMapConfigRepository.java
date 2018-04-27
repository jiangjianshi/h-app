/** 
* Project Name: hzf_platform 
* File Name: HouseDetailRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:31:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.location.CommuteMapConfig;

/**
 * ClassName: HouseDetailRepository date: 2016年4月26日 下午2:31:30 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface CommuteMapConfigRepository extends JpaRepository<CommuteMapConfig, Long> {



	@Query("select a from CommuteMapConfig a" + " where a.id=?1 and status="+ Constants.CommuteMapConfig.OPEN_YES_FLAG)
	public CommuteMapConfig findCommuteMapConfigById(long id);
	
	@Query("select a from CommuteMapConfig a"+" where status="+ Constants.CommuteMapConfig.OPEN_YES_FLAG)
	public List<CommuteMapConfig> findAllCommuteMapConfig();
}
