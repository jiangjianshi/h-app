/** 
* Project Name: hzf_platform 
* File Name: HousePictureRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:35:55 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.BdHousePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * ClassName: HousePictureRepository date: 2016年4月26日 下午2:35:55 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface BdHousePictureRepository extends JpaRepository<BdHousePicture, Long> {

	@Modifying
	@Query("update BdHousePicture t set t.isRun = ?2 where t.id = ?1")
	int setIsRunById(String id,int isRun);


	@Query("select t from BdHousePicture t where t.isRun=?1 and t.isDelete="
			+ Constants.Common.STATE_IS_DELETE_NO)
	List<BdHousePicture> findFirst100AllByIsRun(int isRun);
}
