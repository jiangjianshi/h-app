package com.huifenqi.usercomm.charge.dao;

import com.huifenqi.usercomm.charge.domain.BailFlow;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BailFlowRepository extends CrudRepository<BailFlow, Long> {
	public List<BailFlow> findBailFlowByDepositIdAndStatusOrderByUpdateTimeDesc(Long depositId, Integer status);
}
