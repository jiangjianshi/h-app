package com.huifenqi.usercomm.dao.contract;


import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import com.huifenqi.usercomm.domain.contract.InstallmentContract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by arison on 2015/9/24.
 */
public interface InstallmentContractRepository extends CrudRepository<InstallmentContract, Long> {

    @Query("select a from InstallmentContract a where a.userId=?1 and state=1 order by a.updateTime desc")
    List<InstallmentContract> findValidContractsByUserId(Long userId);

    @Query("select a from InstallmentContract a where a.userMobile=?1 and state=1 order by a.updateTime desc")
    List<InstallmentContract> findValidContractsByPhone(String phone);


	/**
	 * 根据合同号获取合同信息
	 * 
	 * @param contractNo
	 * @return
	 */
	InstallmentContract findByContractNo(String contractNo);

    /**
     * 根据合同号、手机号、审核状态列表和获取合同信息
     *
     * @param contractNo
     * @param userMobile
     * @param contractStatusList
     * @param fundType
     * @return
     */
    @Query("select a from InstallmentContract a" + " where a.contractNo=?1" + " and a.userMobile=?2"
            + " and a.contractStatus in ?3" + " and a.state=1 " + " and a.fundType =?4 "
            + " order by a.updateTime desc")
    InstallmentContract findByConNoAndUPhoneAndConStatusListAndFundType(String contractNo, String userMobile,
                                                                        Collection<Integer> contractStatusList, int fundType);


    @Query("select a from InstallmentContract a " + "where a.userId=?1 " + "and a.state=1 "
            + "and (a.snapshotStatus=" + ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.snapshotStatus="
            + ContractSnapshot.STATUS_TERMINATED + ") and contractSnapshotId=0 " + "and a.contractStatus!=7 "
            + "and a.fundType =?2 " + "order by a.updateTime desc")
    List<InstallmentContract> findAllContractByUserIdAndFundType(Long userId, int fundType);

    @Query("select a from InstallmentContract a " + "where a.userMobile=?1 " + "and a.state=1 "
            + "and (a.snapshotStatus=" + ContractSnapshot.STATUS_TAKING_EFFECT + " or " + "a.snapshotStatus="
            + ContractSnapshot.STATUS_TERMINATED + ") and contractSnapshotId=0 " + "and a.contractStatus!=7 "
            + "and a.fundType =?2 " + "order by a.updateTime desc")
    List<InstallmentContract> findAllContractByPhoneAndFundType(String phone, int fundType);

    //@Query("select a from InstallmentContract a" + " where a.userId=?1 order by a.updateTime desc")
    @Query("select a from InstallmentContract a" + " where a.userId=?1 order by a.installmentContractId desc")
    List<InstallmentContract> findAllContractByUserId(Long userId);

  //  @Query("select a from InstallmentContract a" + " where a.userMobile=?1 order by a.updateTime desc")
    @Query("select a from InstallmentContract a" + " where a.userMobile=?1 order by a.installmentContractId desc")
    List<InstallmentContract> findAllContractByPhone(String phone);


    @Query("select a from InstallmentContract a " + " where a.userMobile=?1 " + " and a.state=1 "
             + " and a.fundType =0 " + " order by a.updateTime desc")
    List<InstallmentContract> findAppliedContractByPhone(String phone);

    @Query("select a from InstallmentContract a " + " where a.userId=?1 " + " and a.state=1 "
            + " and a.fundType =0 " + " order by a.updateTime desc")
    List<InstallmentContract> findAppliedContractByUserId(Long userId);

    @Query("select a from InstallmentContract a " + " where a.userMobile=?1 " + " and a.state=1 "
            + " and ("  //查找不满足以下5个条件的合同
            +"((a.contractStatus =5 and a.updateTime>?2)"//待审核   超过30天后，可以注销
            +"or (a.contractStatus =1 and a.updateTime>?2) "//审核未通过   超过30天后，可以注销
            +"or (a.contractStatus =8 and a.updateTime>?2) "//待补全   超过30天后，可以注销
            +"or (a.contractStatus =7 and a.updateTime>?3 ) "//待确认（租客待扫码）   超过72小时后，可以注销
            +"or (a.contractStatus =9 and a.updateTime>?3 ) "//修改待确认   超过72小时后，可以注销
            +"or (a.contractStatus in (0,2,3,4,10)) ) "
            + ") and a.fundType =0 and (a.snapshotStatus not in (2,3)) " + " order by a.updateTime desc")
    List<InstallmentContract>  findNoLogoffContractContractByPhone(String phone, Date limit30, Date limit72);

    @Query("select a from InstallmentContract a " + " where a.userId=?1 " + " and a.state=1 "
            + " and ("  //查找不满足以下5个条件的合同
            +"((a.contractStatus =5 and a.updateTime>?2)"//待审核   超过30天后，可以注销
            +"or (a.contractStatus =1 and a.updateTime>?2) "//审核未通过   超过30天后，可以注销
            +"or (a.contractStatus =8 and a.updateTime>?2) "//待补全   超过30天后，可以注销
            +"or (a.contractStatus =7 and a.updateTime>?3 ) "//待确认（租客待扫码）   超过72天后，可以注销
            +"or (a.contractStatus =9 and a.updateTime>?3 ) "//修改待确认   超过72天后，可以注销
            +"or (a.contractStatus in (0,2,3,4,10)) ) "
            + ") and a.fundType =0 and (a.snapshotStatus not in (2,3)) " + " order by a.updateTime desc")
    List<InstallmentContract> findNoLogoffContractContractByUserId(Long userId, Date limit30, Date limit72);

    /**
	 * 根据手机号获取用户所以合同 @Title: findAllByUserPhone @return List
     * @deprecated TODO 检查如果没什么用的话，就删除掉
     * <InstallmentContract> @throws
	 */
//	@Query("select a from InstallmentContract a " + "where a.userMobile=?1 " + "and a.state=1 ")
//	List<InstallmentContract> findAllByUserPhone(String phone);

}
