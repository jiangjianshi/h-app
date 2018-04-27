/** 
* Project Name: hzf_platform_project 
* File Name: HouseSolrRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository.house 
* Date: 2016年5月11日下午5:26:28 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house.solr;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.huifenqi.hzf_platform.context.dto.request.house.HouseSearchDto;
import com.huifenqi.hzf_platform.context.entity.house.solr.RoomSolrResult;

/**
 * ClassName: HouseSolrRepository date: 2016年5月11日 下午5:26:28 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface RoomSolrRepository extends SolrCrudRepository<RoomSolrResult, Long> {

	RoomSolrResult findBySellIdAndId(String sellId, long roomId);

	RoomSolrResult findById(long roomId);

	Page<RoomSolrResult> findCompanyOffCount(HouseSearchDto houseSearchDto);
	
	Page<RoomSolrResult> findAllByMultiCondition(HouseSearchDto houseSearchDto);
	
	Page<RoomSolrResult> getAllByMultiCondition(HouseSearchDto houseSearchDto, List<String> agencyIdList);
	
	Page<RoomSolrResult> searchAllByMultiCondition(HouseSearchDto houseSearchDto);
	
	GroupResult<RoomSolrResult> getNewAllByMultiCondition(HouseSearchDto houseSearchDto, List<String> agencyIdList);

	GroupResult<RoomSolrResult> findNewAllByMultiCondition(HouseSearchDto houseSearchDto);

}
