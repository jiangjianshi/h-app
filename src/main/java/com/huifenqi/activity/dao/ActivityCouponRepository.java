package com.huifenqi.activity.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.huifenqi.activity.domain.ActivityCoupon;

/**
 * Created by YDM on 2017/10/12.
 */
public interface ActivityCouponRepository extends CrudRepository<ActivityCoupon, Long> {

	/**
	 * 获取全部有效的优惠券数据
	 * @param 
	 * @return
	 */
	@Query("select ac from ActivityCoupon ac where ac.state = 1")
	List<ActivityCoupon> getAllActivityCouponList();
	
	/**
	 * 获取带兑换码的优惠券数据
	 * @param couponType
	 * @return
	 */
	@Query("select ac from ActivityCoupon ac where ac.state = 1 and ac.couponType = ?1")
	List<ActivityCoupon> getActivityCouponList(int couponType);
	
	/**
	 * 把优惠券置为无效
	 * @param activityCouponId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("update ActivityCoupon ac set ac.state = 0 where ac.id = ?1")
	public int updateActivityCoupon(long activityCouponId);

}
