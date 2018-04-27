package com.huifenqi.usercomm.dao.contract;


import com.huifenqi.usercomm.domain.contract.Subpay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by stargazer on 2015/9/25.
 */
public interface SubpayRepository extends CrudRepository<Subpay, Long> {

	@Query("select a from Subpay a " + "where a.contractSnapshotId=?1 " + "and a.state=?2 " + "and a.payState in(0,1) "
			+ "order by index")
	List<Subpay> findFirstUncompleteSubpay(long contractSnapshotId, int state);

	long countByContractSnapshotId(long contractSnapshotId);

	List<Subpay> findByContractSnapshotId(long contractSnapshotId);

	@Query("select a from Subpay a " + "where a.contractSnapshotId=?1 " + "order by index desc")
	List<Subpay> findSubpayOrderByIndex(long contractSnapshotId);

	List<Subpay> findByInstallmentContractId(long installmentContractId);

	@Query("select count(*) from Subpay a " + "where a.userId=?1 " + "and a.state=1 " + "and a.payState in(1,2,3,5) ")
	long countByUserId(long userId);

	@Query("select a from Subpay a where a.state=1 and a.subpayId=?1")
	Subpay findValidSubpay(Long subpayId);

	/**
	 * @deprecated 使用 {@link com.huifenqi.usercomm.dao.huizhaofang.SubpayRepository#findValidSubpay(Long)}
	 * @param subpayId
	 * @return
	 */
	Subpay findBySubpayId(long subpayId);

	List<Subpay> findByContractNo(String contractNo);
}
