package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.AgencyRefund;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by HFQ-Arison on 2017/5/11.
 */
public interface AgencyRefundRepository  extends CrudRepository<AgencyRefund, Long> {
    public List<AgencyRefund> findByContractNoAndStateOrderByCreateTimeDesc(String contractNo, long state);
}
