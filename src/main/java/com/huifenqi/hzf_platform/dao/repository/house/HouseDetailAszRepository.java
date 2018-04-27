/** 
* Project Name: hzf_platform 
* File Name: HouseDetailRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:31:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetailAsz;

/**
 * ClassName: HouseDetailRepository date: 2016年4月26日 下午2:31:30 Description:
 * 
 * @author changmingwei，arison
 * @version
 * @since JDK 1.8
 */

public interface HouseDetailAszRepository extends JpaRepository<HouseDetailAsz, Long> {

	@Query("select a from HouseDetailAsz a" + " where a.apartmentCode=?1")
	HouseDetailAsz findByApartmentCode(String apartmentCode);

	@Modifying
	@Query("update HouseDetailAsz a set a.transferFlag=?2 where a.apartmentCode=?1 ")
	void updateHouseTrandferFlag(String apartmentCode, int transferFlag);

	@Query("select a from HouseDetailAsz a" + " where a.transferFlag = " + AszConstants.HouseDetail.TRANSFER_FLAG_NO)
	List<HouseDetailAsz> findByTransferFlag();
	
    @Modifying
    @Query("update HouseDetailAsz a set a.houseSellId=?2, a.transferFlag="+ AszConstants.HouseDetail.TRANSFER_FLAG_YES+" where a.apartmentCode=?1")
    void updateHouseSellId(String apartmentCode, String houseSellId);

}
