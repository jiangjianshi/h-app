package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.context.entity.house.Agency;

public interface AgencyManageRepository extends CrudRepository<Agency, Long> {

	@Query("select a from Agency a where a.isDelete = 0 and (a.cityId = ?1 or a.cityId = 0)")
	List<Agency> findAgencies(long cityId);
	
	@Query("select a from Agency a where a.isDelete = 0 and a.cityId = ?1")
	List<Agency> findAgenciesByCityId(long cityId);
	
	@Query("select a from Agency a where a.isDelete = 0 order by a.id")
	List<Agency> findAllAgency();
	
	@Query("select a from Agency a where a.companyId = ?1 and a.isDelete = 0")
	List<Agency> queryListByAgencyId(String agencyId);
	
	@Transactional
	@Modifying
	@Query("delete Agency a where a.companyId = ?1 and a.cityId = ?2 and a.isDelete = 0")
	public void deleteByAgencyIdAndCityId(String agencyId, long cityId);
	
	@Query("select a from Agency a where a.isDelete = 0 and a.destinationUrl IS NOT NULL and a.destinationUrl <> ''")
	List<Agency> findActivityAgency();
	
	@Query("select a from Agency a where a.isDelete = 0 and a.companyId = ?1")
	List<Agency> getAgencyByCompanyId(String companyId);
	
	@Query("select a from Agency a where a.isDelete = 0 and a.companyId = ?1 and a.cityId = ?2")
	Agency getAgencyByCompanyIdAndCityId(String companyId, long cityId);
	
	@Query("select a from Agency a where a.isDelete = 0 and a.cityId = ?1 and a.roomCount >= 5 order by a.roomCount desc")
	List<Agency> getAgencyListByCityId(long cityId);
	
	@Transactional
	@Modifying
	@Query("update Agency a set a.roomCount = ?3 where a.companyId = ?1 and a.cityId = ?2 and a.isDelete = 0")
	public void updateByAgencyIdAndCityId(String agencyId, long cityId, long number);
	
}
