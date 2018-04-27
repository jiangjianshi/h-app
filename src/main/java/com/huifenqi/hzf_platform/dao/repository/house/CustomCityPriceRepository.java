package com.huifenqi.hzf_platform.dao.repository.house;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huifenqi.hzf_platform.context.entity.house.CustomCityPrice;

public interface CustomCityPriceRepository extends JpaRepository<CustomCityPrice, Long>{
	
	/**
	 * 
	 * @return
	 */
	CustomCityPrice findByCityIdAndStatus(long cityId, Integer status);

}
