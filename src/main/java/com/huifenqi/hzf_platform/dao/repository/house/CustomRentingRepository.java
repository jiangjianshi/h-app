package com.huifenqi.hzf_platform.dao.repository.house;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huifenqi.hzf_platform.context.entity.house.CustomRenting;

public interface CustomRentingRepository extends JpaRepository<CustomRenting, Long> {

	/**
	 * 
	 * @return
	 */
	@Query("select a from CustomRenting a where a.status = 1 and a.id not in :ids")
	List<CustomRenting> findAllValid(@Param(value = "ids") long[] ids);


	@Query("select a from CustomRenting a where a.status = 0")
	List<CustomRenting> findAllInvalid();

}
