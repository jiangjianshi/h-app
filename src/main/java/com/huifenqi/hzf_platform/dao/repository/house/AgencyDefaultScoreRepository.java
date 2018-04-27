package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.huifenqi.hzf_platform.context.entity.house.AgencyDefaultScore;

public interface AgencyDefaultScoreRepository extends CrudRepository<AgencyDefaultScore, Long>{

	@Query("select a from AgencyDefaultScore a where a.status = 1 and a.source = ?1")
	AgencyDefaultScore findBySourceName(String source);
}
