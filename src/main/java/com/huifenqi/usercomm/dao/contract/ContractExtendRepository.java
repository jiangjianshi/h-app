package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.ContractExtend;
import org.springframework.data.repository.CrudRepository;

public interface ContractExtendRepository extends CrudRepository<ContractExtend, String> {
	public ContractExtend findContractExtendByContractNo(String contractNo);
}
