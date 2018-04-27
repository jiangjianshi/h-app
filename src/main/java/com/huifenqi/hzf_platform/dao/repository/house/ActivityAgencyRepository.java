package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.huifenqi.hzf_platform.context.entity.house.ActivityAgency;

public interface ActivityAgencyRepository extends CrudRepository<ActivityAgency, Long> {

	@Query("select a from ActivityAgency a where a.state = 1 and a.companyId = ?1")
	ActivityAgency getActivityAgency(String companyId);
	
}
