package com.huifenqi.activity.dao;


import com.huifenqi.activity.domain.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by arison on 2015/9/9.
 */
public interface VoucherRepository extends CrudRepository<Voucher, Long> {

	@Query("select v from Voucher v " + "where v.userId = ?1 " + "and v.contractSnapshotId = ?2 "
			+ "and v.useTime > ?3 " + "and v.useTime < ?4 " + "and v.voucherUseState = 1")
	List<Voucher> getUsableVoucherListInRegion(long userId, long contractSnapshotId, Date startTime, Date endTime);

	@Query("select v from Voucher v " + "where v.createTime > ?1 " + "and v.createTime < ?2 "
			+ "and v.voucherActivityId = ?3 " + "and v.userId = ?4")
	List<Voucher> findByCreateTimeRange(Date startTime, Date endTime, long voucherActivityId, long userId);

	/**
	 * 列出该用户的所有代金券
	 *
	 * @param userId
	 * @return
	 */
	List<Voucher> findByUserId(long userId);

	/**
	 * 根据voucher_use_state字段来查询指定用户的代金券
	 *
	 * @param userId
	 * @param voucherUseState
	 * @return
	 */
	List<Voucher> findByUserIdAndVoucherUseState(long userId, int voucherUseState);

	/**
	 * 列出date之后的代金券
	 * @param date
	 * @return
	 */
	List<Voucher> findByUserIdAndCreateTimeAfter(long userId, Date date);
}
