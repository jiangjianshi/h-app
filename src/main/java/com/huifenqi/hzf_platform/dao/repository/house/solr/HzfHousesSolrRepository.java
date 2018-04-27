/** 
* Project Name: hzf_platform_project 
* File Name: HouseSolrRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository.house 
* Date: 2016年5月11日下午5:26:28 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house.solr;

import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.house.solr.HzfHousesSolrResult;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * ClassName: HouseSolrRepository date: 2016年5月11日 下午5:26:28 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HzfHousesSolrRepository extends SolrCrudRepository<HzfHousesSolrResult, Long>{

	HouseSolrResult findBySellId(String sellId);

	Page<HzfHousesSolrResult> findAllByMultiCondition(HouseSearchDto houseSearchDto);
	
	Page<HzfHousesSolrResult> getAllByMultiCondition(HouseSearchDto houseSearchDto, List<String> agencyIdList);
	
	Page<HzfHousesSolrResult> searchAllByMultiCondition(HouseSearchDto houseSearchDto);

}
