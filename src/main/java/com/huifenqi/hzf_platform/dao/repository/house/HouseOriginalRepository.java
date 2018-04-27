package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseOriginal;

/**
 * 
 * @author jjs
 *
 */
public interface HouseOriginalRepository extends JpaRepository<HouseOriginal, Long>{
	
	@Query("select a from HouseOriginal a" + " where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	HouseOriginal findBySellId(String sellId);

	
	@Modifying
	@Query("update HouseOriginal t set t.isDelete = ?2 where t.sellId = ?1")
	public int setIsDeleteBySellId(String sellId, int isDelete);

	@Modifying
	@Query("update HouseOriginal t set t.status = ?2 where t.sellId = ?1")
	int setStatusBySellId(String sellId, int status);
}
