package com.huifenqi.activity.dao;


import com.huifenqi.activity.domain.VoucherConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by arison on 2015/9/10.
 */
public interface VoucherConfigRepository extends CrudRepository<VoucherConfig, Long> {

    /**
     * 根据活动id查询代金券配置
     *
     * @param activityId
     * @return
     */
    List<VoucherConfig> findByActivityId(long activityId);
}
