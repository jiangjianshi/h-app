package com.huifenqi.usercomm.dao;

import com.huifenqi.usercomm.domain.contract.AgencyConf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tengguodong on 2016/8/25.
 */
public interface AgencyConfRepository extends JpaRepository<AgencyConf,Integer> {
    //是否开通分期 0：否 1：是
    public List<AgencyConf> findByIsSupportHfq(int isSupportHfq);
    public AgencyConf findByAgencyid(int agencyid);
}
