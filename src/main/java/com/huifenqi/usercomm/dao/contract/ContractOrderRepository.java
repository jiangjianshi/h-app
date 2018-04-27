package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.ContractOrder;
import org.springframework.data.repository.CrudRepository;

public interface ContractOrderRepository extends CrudRepository<ContractOrder, String> {
	public ContractOrder findContractOrderByContractNo(String contractNo);
}
