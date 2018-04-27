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
import com.huifenqi.hzf_platform.context.entity.location.CommunityBase;

/**
 * ClassName: HouseDetailRepository date: 2016年4月26日 下午2:31:30 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public interface CommunityBaseRepository extends JpaRepository<CommunityBase, Long> {

	@Query("select a from CommunityBase a" + " where a.communityName=?1")
	CommunityBase findCommunityByName(String communityName);


	@Query("select a from CommunityBase a" + " where a.cityId=?1 and flag="+ Constants.CommunityStatus.OPEN_YES_FLAG)
	public List<CommunityBase> findCommunityByCityId(long cityId);
	
	@Query("select a from CommunityBase a" + " where a.cityId=?1 and flag=?2")
	public List<CommunityBase> findCommunityByCityIdAndFlag(long cityId,int flag);
}
