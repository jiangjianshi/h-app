package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huifenqi.hzf_platform.context.entity.house.HouseTypeMapping;

public interface HouseTypeMappingRepository extends JpaRepository<HouseTypeMapping, Long>{
	
	/**
	 * 根据描述获取户型
	 * @param houseTypeDesc
	 * @return
	 */
	@Query("select a from HouseTypeMapping a where a.liveBedTotile=?1")
	HouseTypeMapping findByHouseTypeDesc(String houseTypeDesc);
	
}
