package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by arison on 2015/9/24.
 */
public interface ContractSnapshotRepository extends CrudRepository<ContractSnapshot, Long> {

	@Query("select a from ContractSnapshot a where a.userId=?1 and a.state=1 and (a.contractStatus="
			+ ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.contractStatus=" + ContractSnapshot.STATUS_SUBLET
			+ " or a.contractStatus=" + ContractSnapshot.STATUS_TERMINATED + ")order by a.updateTime desc")
	List<ContractSnapshot> findValidContractByUserId(Long userId);

	@Query("select a from ContractSnapshot a " + "where a.userMobile=?1 " + "and a.state=1 " + "and (a.contractStatus="
			+ ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.contractStatus=" + ContractSnapshot.STATUS_SUBLET
			+ " or " + "a.contractStatus=" + ContractSnapshot.STATUS_TERMINATED + ")" + "order by a.updateTime desc")
	List<ContractSnapshot> findValidContractByPhone(String phone);

    ContractSnapshot findContractByContractNo(String contractNo);

    @Query("select a from ContractSnapshot a where a.userId=?1 and a.state=1 and (a.contractStatus="
            + ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.contractStatus=" + ContractSnapshot.STATUS_SUBLET
            + " or a.contractStatus=" + ContractSnapshot.STATUS_TERMINATED + " or a.contractStatus="
            + ContractSnapshot.STATUS_RENEGE + ")" + "order by a.updateTime desc")
    List<ContractSnapshot> findValidContractForListByUserId(Long userId);

    @Query("select a from ContractSnapshot a where a.userMobile=?1 and a.state=1 and (a.contractStatus="
            + ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.contractStatus=" + ContractSnapshot.STATUS_SUBLET
            + " or a.contractStatus=" + ContractSnapshot.STATUS_TERMINATED + " or a.contractStatus="
            + ContractSnapshot.STATUS_RENEGE + ")" + "order by a.updateTime desc")
    List<ContractSnapshot> findValidContractForListByPhone(String phone);


	@Query("select a from ContractSnapshot a " + "where a.userId=?1 " + "and a.state=1 " + "and a.contractStatus="
			+ ContractSnapshot.STATUS_TAKING_EFFECT + " order by a.updateTime desc")
	List<ContractSnapshot> findEffectContractByUserId(Long userId);

    @Query("select a from ContractSnapshot a " + "where a.userMobile=?1 " + "and a.state=1 " + " and a.contractStatus="
            + ContractSnapshot.STATUS_TAKING_EFFECT + " order by a.updateTime desc")
    List<ContractSnapshot> findEffectContractByPhone(String phone);

	@Query("select a from ContractSnapshot a " + "where a.userId=?1 " + "and a.state=1 " +" and(a.contractStatus not in("+ContractSnapshot.STATUS_TERMINATED +","+ContractSnapshot.STATUS_RENEGE+")) "
			 + " order by a.updateTime desc")
	List<ContractSnapshot> findNoLogoffContractContractByUserId(Long userId);

	@Query("select a from ContractSnapshot a " + "where a.userMobile=?1 " + "and a.state=1 " +" and (a.contractStatus not in("+ContractSnapshot.STATUS_TERMINATED +","+ContractSnapshot.STATUS_RENEGE+")) "
			+ " order by a.updateTime desc")
	List<ContractSnapshot> findNoLogoffContractContractByPhone(String phone);
}
