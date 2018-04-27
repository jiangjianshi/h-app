package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.entity.house.Apartment;
import org.springframework.stereotype.Repository;
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

	@Query("select a from Apartment a  where a.status=" + Constants.ApartmentStatus.STATUS_HOT
			+ " and a.cityId=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO + " order by a.updateTime desc")
	List<Apartment> findHotApartments(long cityId);

	@Query("select a from Apartment a  where a.status=" + Constants.ApartmentStatus.STATUS_HOT
			+ " and a.cityId=?1 and a.isDelete=" + Constants.Common.STATE_IS_DELETE_NO + " order by a.updateTime desc")
	List<Apartment> findHotApartments(long cityId, Pageable pageable);

	@Modifying
	@Query("update Apartment t set t.isDelete = ?3 where t.id = ?1 and t.cityId=?2")
	public int setIsDeleteByIdAndCityId(long id, long cityId, int isDelete);

	@Modifying
	@Query("update Apartment t set t.isDelete = ?2 where t.cityId = ?1")
	public int setIsDeleteByCityId(long cityId, int isDelete);

}
