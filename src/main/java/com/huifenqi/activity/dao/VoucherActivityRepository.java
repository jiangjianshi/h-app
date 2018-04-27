package com.huifenqi.activity.dao;


import com.huifenqi.activity.domain.VoucherActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoucherActivityRepository  extends CrudRepository<VoucherActivity, Long> {

	@Query("select v from VoucherActivity v " +
            "where v.state =1" +
			"order by id")
	List<VoucherActivity> findStartActivity();
}
