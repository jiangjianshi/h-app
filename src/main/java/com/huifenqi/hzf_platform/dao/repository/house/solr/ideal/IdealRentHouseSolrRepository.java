/** 
* Project Name: hzf_platform_project 
* File Name: HouseSolrRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository.house 
* Date: 2016年5月11日下午5:26:28 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house.solr.ideal;

import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.huifenqi.hzf_platform.context.dto.request.house.IdealRentHouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;

/**
 * ClassName: HouseSolrRepository date: 2016年5月11日 下午5:26:28 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface IdealRentHouseSolrRepository extends SolrCrudRepository<HouseSolrResult, Long>{

	GroupResult<HouseSolrResult> getAllByMultiCondition(IdealRentHouseSearchDto idealRentHouseSearchDto);
	
}
