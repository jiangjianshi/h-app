/** 
* Project Name: hzf_platform_project 
* File Name: HouseRecommendRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository.house 
* Date: 2016年5月9日下午8:15:03 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseRecommend;

/**
 * ClassName: HouseRecommendRepository date: 2016年5月9日 下午8:15:03 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HouseRecommendRepository extends JpaRepository<HouseRecommend, Long> {

	@Query("select a from HouseRecommend a  where a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	List<HouseRecommend> findAllValidRecommend();

	@Query("select a from HouseRecommend a  where a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO
			+ " and a.cityId=?1 order by a.updateTime desc")
	List<HouseRecommend> findHouseRecommends(long cityId);

	@Query("select a from HouseRecommend a  where a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO
			+ " and a.cityId=?1 order by a.updateTime desc")
	List<HouseRecommend> findHouseRecommends(long cityId, Pageable pageable);

	@Modifying
	@Query("update HouseRecommend t set t.isDelete = ?3 where t.id = ?1 and t.cityId=?2")
	public int setIsDeleteByIdAndCityId(long id, long cityId, int isDelete);

	@Modifying
	@Query("update HouseRecommend t set t.isDelete = ?2 where t.cityId = ?1")
	public int setIsDeleteByCityId(long cityId, int isDelete);

	@Modifying
	@Query("update HouseRecommend t set t.isDelete = ?2 where t.id = ?1")
	public int setIsDeleteById(long id, int isDelete);

}
