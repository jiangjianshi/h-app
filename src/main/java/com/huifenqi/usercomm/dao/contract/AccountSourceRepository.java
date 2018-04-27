package com.huifenqi.usercomm.dao.contract;

import com.huifenqi.usercomm.domain.contract.AccountSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by arison on 2017/04/20.
 */
public interface AccountSourceRepository extends CrudRepository<AccountSource, Long> {
//subpay.getSourcesCapital()

	@Query("select a from AccountSource a where a.state=1")
	List<AccountSource> findValidAccountSource();
	@Query("select a from AccountSource a where a.state=1 and sourceCapital= ?1 ")
	AccountSource findBySourceCapital(String sourceCapital);
}
