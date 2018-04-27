/** 
* Project Name: hzf_platform 
* File Name: HouseBaseRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:29:01 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;

/**
 * ClassName: HouseBaseRepository date: 2016年4月26日 下午2:29:01 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HouseBaseRepository extends JpaRepository<HouseBase, Long> {

	@Modifying
	@Query("update HouseBase t set t.isDelete = ?2 where t.sellId = ?1")
	int setIsDeleteBySellId(String sellId, int isDelete);

	@Query("select a from HouseBase a" + " where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	HouseBase findBySellId(String sellId);
	
	//查询程序删除之外的房源
	@Query("select a from HouseBase a" + " where a.sellId=?1  and a.isDelete !=" + Constants.Common.STATE_IS_DELETE_YES)
	HouseBase findCanUpdateBySellId(String sellId);
	
	//查询所有房源，包含删除的
    @Query("select a from HouseBase a" + " where a.sellId=?1 ")
    HouseBase findAllDeleteBySellId(String sellId);

	@Query("select a.sellId from HouseBase a" + " where a.companyId=?1  and a.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<String> findByCompanyId(String companyId);

	@Modifying
	@Query("update HouseBase t set t.status = ?2 where t.sellId = ?1")
	int setStatusBySellId(String sellId, int status);
	
	@Query("select count(*) from HouseBase a" + " where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	int countBySellId(String sellId);
	
	/**
	 * 计算rank分调用
	 * @param sellId
	 * @return
	 */
	@Query("select a.monthRent from HouseBase a where a.sellId=?1")
	int selectMonthRentBySellId(String sellId);
}
