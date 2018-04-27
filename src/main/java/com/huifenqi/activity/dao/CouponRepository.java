package com.huifenqi.activity.dao;


import com.huifenqi.activity.domain.Coupon;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by arison on 2017/9/9.
 */
public interface CouponRepository extends CrudRepository<Coupon, Long> {

	/**
	 * 列出该用户的所有优惠券
	 *
	 * @param userId
	 * @return
	 */
	List<Coupon> findByUserId(long userId);

	/**
	 * 列出date之后的优惠券
	 * @param date
	 * @return
	 */
	List<Coupon> findByUserIdAndExpiredDateAfterOrderByExpiredDateAsc(long userId, Date date);

	/**
	 * 列出date之前的优惠券
	 * @param date
	 * @return
	 */
	List<Coupon> findByUserIdAndExpiredDateBeforeOrderByExpiredDateAsc(long userId, Date date);
	/**
	 * 列出date之前的优惠券
	 * @param date
	 * @return
	 */
	Long countByUserIdAndExpiredDateBefore(long userId, Date date);
	
	/**
	 * 查询当前用户下是否存在优惠券
	 * @param couponType
	 * @return
	 */
	@Query("select c from Coupon c where c.phone = ?1")
	List<Coupon> getCouponListByPhone(String phone);
	
	/**
	 * 查询当前用户下有效的优惠券数据
	 * @param couponType
	 * @return
	 */
	@Query("select c from Coupon c where c.phone = ?1 and c.state = 1 and c.expiredDate >= CURRENT_DATE() order by c.expiredDate")
	List<Coupon> getValidCouponListByPhone(String phone);
	
	/**
	 * 查询当前用户下失效的优惠券数据：过期的和使用过的
	 * @param couponType
	 * @return
	 */
	@Query("select c from Coupon c where c.phone = ?1 and (c.state = 0 or c.expiredDate < CURRENT_DATE()) order by c.expiredDate desc")
	List<Coupon> getExpiredCouponListByPhone(String phone);
	
}
