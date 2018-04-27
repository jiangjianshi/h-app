package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.ContractDeposit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ContractDepositRepository extends CrudRepository<ContractDeposit, Long> {
	
	@Query("select cd from ContractDeposit as cd where cd.contractNo=?1 and cd.state = 1")
	public ContractDeposit findContractDepositByContractNo(String contractNo);
}
