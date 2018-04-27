/** 
* Project Name: hzf_platform 
* File Name: HouseDetailRepository.java 
* Package Name: com.huifenqi.hzf_platform.dao.repository 
* Date: 2016年4月26日下午2:31:30 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.vo.QueryDetailVo;

/**
 * ClassName: HouseDetailRepository date: 2016年4月26日 下午2:31:30 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
public interface HouseDetailRepository extends JpaRepository<HouseDetail, Long> {

	@Modifying
	@Query("update HouseDetail t set t.isDelete = ?2 where t.sellId = ?1")
	public int setIsDeleteBySellId(String sellId, int isDelete);

	@Modifying
	@Transactional
	@Query("update HouseDetail t set t.isRun = ?2 where t.sellId = ?1")
	int setIsRunBySellId(String sellId, int isRun);

	@Modifying
	@Query("update HouseDetail t set t.isRun = 0 where t.sellId in :ids")
	public void setIsRunByIds(@Param(value = "ids") List<String> ids);

	@Query("select a from HouseDetail a where a.sellId=?1  and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO)
	HouseDetail findBySellId(String sellId);
	
	@Query("select a from HouseDetail a where a.sellId=?1")
    HouseDetail findAllDeleteBySellId(String sellId);
	
	//查询程序删除之外的房源
	@Query("select a from HouseDetail a where a.sellId=?1  and a.isDelete !=" + Constants.Common.STATE_IS_DELETE_YES)
	HouseDetail findCanUpdateBySellId(String sellId);

	List<HouseDetail> findFirst100BySellIdNotInAndIsRunAndIsDeleteOrderByCreateTimeAsc(List<String> sellId, int isRun,
			int isDelete);

	List<HouseDetail> findFirst100ByIsRunAndIsDeleteOrderByCreateTimeAsc(int isRun, int isDelete);

	@Modifying
	@Transactional
	@Query("update HouseDetail t set t.isTop = ?2 where t.sellId = ?1")
	public int setIsTopBySellId(String sellId, int isTop);

	@Modifying
	@Transactional
	@Query("update HouseDetail t set t.pubType = ?2 where t.sellId = ?1")
	public int setPubTypeBySellId(String sellId, int pubType);

	String condition = Constants.PlatformStatus.ALLOW_BOTH + "," + Constants.PlatformStatus.DENY_SOLR;

	// 查询未被审核的数据
	@Query("SELECT a FROM HouseDetail a, HouseBase b, PlatformCustomer c"
			+ " where a.sellId = b.sellId and a.source = c.source and c.permissionStatus in (" + condition
			+ ") and b.status = 1 and a.isRun = 1 and a.isDelete =0 and a.approveStatus in (0,10)")
	Page<HouseDetail> findFirstUnapprove(Pageable pageable);
	
	
	// 查询未删除的房源，整租分租可选
	@Query("SELECT a FROM HouseDetail a where a.isDelete =0 and a.entireRent = ?1")
	Page<HouseDetail> selectHouseNotDeleted(Pageable pageable,int entireRent);

	@Query("SELECT new com.huifenqi.hzf_platform.vo.QueryDetailVo(hd.bizName as bizName, count(hd.bizName) as bizNameCount) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd WHERE hd.cityId = ?1 GROUP BY hd.bizName ORDER BY bizNameCount DESC")
	Page<QueryDetailVo> getBizNameList(long cityId, Pageable pageable);

	@Query("SELECT new com.huifenqi.hzf_platform.vo.QueryDetailVo(hd.cityId as cityId) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId AND hb.companyId = ?1 GROUP BY hd.cityId")
	List<QueryDetailVo> getCityIdListByAgengcyId(String agencyId);

	@Query("SELECT new com.huifenqi.hzf_platform.vo.QueryDetailVo(hd.cityId as cityId, hb.companyId as companyId, COUNT(hd.sellId) as houseNum) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId and hb.companyId = ?1 and hd.cityId = ?2 AND (hb.status = 1 or hb.status = 6) AND (hd.approveStatus = 1 or hd.approveStatus = 3) and hb.isDelete = 0 GROUP BY hb.companyId, hd.cityId")
	QueryDetailVo getHouseIsEmptyList(String agencyId, long cityId);

	@Query("SELECT new com.huifenqi.hzf_platform.vo.QueryDetailVo(hd.cityId as cityId, hb.companyId as companyId, COUNT(hd.sellId) as houseNum) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId AND (hb.status = 1 or hb.status = 6) AND (hd.approveStatus = 1 or hd.approveStatus = 3) and hb.isDelete = 0 GROUP BY hb.companyId, hd.cityId HAVING COUNT(hd.sellId) > 0")
	List<QueryDetailVo> getHouseIsNotEmptyList();

	@Query("select new com.huifenqi.hzf_platform.vo.QueryDetailVo(COUNT(hd.sellId) as communityHouseCount, 0l, 0l) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId AND hb.companyName = ?1 AND (hb.status = 1 or hb.status = 6) AND (hd.approveStatus = 1 or hd.approveStatus = 3) and hb.isDelete = 0")
	QueryDetailVo getCommunityHouseCountVo(String communityName);

	@Query("select new com.huifenqi.hzf_platform.vo.QueryDetailVo(0l, COUNT(hd.sellId) as companyHouseCount, 0l) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId AND hb.companyId = ?1 AND (hb.status = 1 or hb.status = 6) AND (hd.approveStatus = 1 or hd.approveStatus = 3) and hb.isDelete = 0")
	QueryDetailVo getCompanyHouseCountVo(String companyId);

	@Query("select new com.huifenqi.hzf_platform.vo.QueryDetailVo(0l, 0l, COUNT(DISTINCT hd.cityId) as companyCityCount) FROM com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.house.HouseBase hb WHERE hd.sellId = hb.sellId AND hb.companyId = ?1 AND hd.cityId > 0 AND (hb.status = 1 or hb.status = 6) AND (hd.approveStatus = 1 or hd.approveStatus = 3) and hb.isDelete = 0")
	QueryDetailVo getCompanyCityCountVo(String companyId);

	@Modifying
	@Transactional
	@Query("update HouseDetail t set t.approveStatus = ?2 where t.sellId = ?1")
	public int setApproveStatusBySellId(String sellId, int approveStatus);

	@Query("select new com.huifenqi.hzf_platform.vo.QueryDetailVo(hd.cityId as cityId, hd.city as cityName, c.center as center) from com.huifenqi.hzf_platform.context.entity.house.HouseDetail hd, com.huifenqi.hzf_platform.context.entity.location.City c where c.cityId = hd.cityId and hd.isDelete = 0 and hd.cityId not in ('1101','3101','4401','4403') GROUP BY hd.cityId ORDER BY COUNT(hd.sellId) DESC")
	List<QueryDetailVo> getCityInfoList();

	/**
	 * 计算评分时调用
	 * 
	 * @param isDelete
	 * @return
	 */
	@Query("select a from HouseDetail a where  a.entireRent = 1 and a.isDelete=0 and (a.updateTime >= ?1 or a.sellId in (select DISTINCT p.sellId from HousePicture p where p.updateTime >= ?1 and p.roomId=0))")
	Page<HouseDetail> findByTimeRegion(Pageable pageable, Date startTime);
}
