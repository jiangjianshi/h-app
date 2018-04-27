package com.huifenqi.usercomm.dao.contract;


import com.huifenqi.usercomm.domain.contract.InstallmentApply;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by arison on 2015/9/12.
 */
public interface InstallmentApplyRepository extends CrudRepository<InstallmentApply, Long> {

    /**
     * 正常情况下，一个用户仅能够提交一次，但因为历史接口的bug，会有一些重复的脏数据
     *
     * @param phone
     * @return
     */
    List<InstallmentApply> findByPhoneAndStateOrderByCreateTimeDesc(String phone, int state);
    List<InstallmentApply> findByUserIdAndStateOrderByCreateTimeDesc(long userId, int state);

}
