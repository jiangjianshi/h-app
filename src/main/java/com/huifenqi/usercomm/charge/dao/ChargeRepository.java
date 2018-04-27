package com.huifenqi.usercomm.charge.dao;


import com.huifenqi.usercomm.charge.domain.Charge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChargeRepository extends CrudRepository<Charge, Long> {
	public List<Charge> findChargeBySubpayIdAndStatusOrderByUpdateTimeDesc(long subpayId, Integer status);

	@Query("select a from Charge a where a.state=1 and a.orderNo=?1")
	Charge findByOrderNo(String orderNo);
}
