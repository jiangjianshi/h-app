/** 
* Project Name: hzf_platform 
* File Name: HousePictureRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:35:55 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;

/**
 * ClassName: HousePictureRepository date: 2016年4月26日 下午2:35:55 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HousePictureRepository extends JpaRepository<HousePicture, Long> {

	@Modifying
	@Query("update HousePicture t set t.isDelete = ?2 where t.sellId = ?1")
	int setIsDeleteBySellId(String sellId, int isDelete);

	@Modifying
	@Query("update HousePicture t set t.isDelete = ?2 where t.roomId = ?1")
	int setIsDeleteByRoomId(long roomId, int isDelete);

	@Modifying
	@Query("update HousePicture t set t.isDelete = ?2 where t.id in ?1")
	int setIsDeleteByIds(Collection<Long> ids, int isDelete);
	
	@Query("select a from HousePicture a where  a.picDhash=?1 and a.picRootPath != '' ")
	List<HousePicture> findByPicDhash(Pageable pageable, String picDhash);
	
	@Query("select a from HousePicture a where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	List<HousePicture> findAllBySellId(String sellId);
	
	@Query("select a from HousePicture a where a.sellId=?1")
    List<HousePicture> findAllDeleteBySellId(String sellId);

	@Query("select a from HousePicture a where a.sellId=?1  and a.roomId=?2 " + "and a.isDelete!="
			+ Constants.Common.STATE_IS_DELETE_YES)
	List<HousePicture> findAllBySellIdAndRoomId(String sellId, long roomId);

	@Query("select a from HousePicture a where a.sellId=?1  and a.roomId=?2 and a.isDelete=?3")
	List<HousePicture> findVaildBySellIdAndRoomId(String sellId, long roomId, int isDelete);
	
	
	@Query("select a from HousePicture a where a.sellId=?1 and a.roomId=?2 and isDefault=0 and a.isDelete="+Constants.Common.STATE_IS_DELETE_NO)
	List<HousePicture> findNotDefaultBySellIdAndRoomId(String sellId, long roomId);
	
	@Query("select a from HousePicture a where a.isDelete=0 and a.isDefault = 1 and a.picDhash !='' group by a.picDhash having count(1) > 1")
	Page<HousePicture> findRepeatDefaultImage(Pageable pageable);

}
