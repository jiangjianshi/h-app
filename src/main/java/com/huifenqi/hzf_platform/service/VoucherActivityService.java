package com.huifenqi.hzf_platform.service;

import com.huifenqi.activity.dao.VoucherConfigRepository;
import com.huifenqi.activity.dao.VoucherRepository;
import com.huifenqi.activity.domain.Voucher;
import com.huifenqi.activity.domain.VoucherActivity;
import com.huifenqi.activity.domain.VoucherConfig;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.NoSuchUserException;
import com.huifenqi.hzf_platform.context.exception.NoSuchVoucherException;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.LogUtil;
import com.huifenqi.usercomm.dao.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HFQ-Arison on 2017/9/11.
 */
@Service
public class VoucherActivityService {
    private static Logger logger = LoggerFactory.getLogger(VoucherActivity.class);

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherConfigRepository voucherConfigRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 存放配置id用于随机生成代金券
     */
    private static long[] ids = new long[10000];

    /**
     * 代金券活动开关 一旦关闭，则所有活动不会再获取代金券
     */
    private static boolean VOUCHER_ACTIVITY_ENABLED = false;

    /**
     * 将代金券设置为未使用
     *
     * @param voucherId
     */
    public void setVoucherNotUsed(long voucherId) throws NoSuchVoucherException {
        Voucher voucher = voucherRepository.findOne(voucherId);

        if (null == voucher) {
            throw new NoSuchVoucherException(voucherId);
        }

        voucher.setVoucherUseState(Voucher.USE_STATE_NOTUSED);
        voucher.setChargeId(0);
        voucher.setPayId(0);
        voucherRepository.save(voucher);

    }

    /**
     * 代金券活动 - 注册
     *
     * 注册的用户将会获得一张代金券 该方法的逻辑是：在每次登录的时候调用该方法，如果用户从未获取过代金券，则获取一张
     *
     * @param userId
     */
    public void activityRegister(long userId) {
        if (!isActivityEnabled()) {
            return;
        }

        if (!ifUserExist(userId)) {
            throw new NoSuchUserException(userId);
        }

        logger.debug("register activity, user " + userId + " will get one voucher");

        List<Voucher> voucherList = voucherRepository.findByUserId(userId);

        if (CollectionUtils.isNotEmpty(voucherList)) {
            logger.info("user " + userId + " has already got voucher");
            return;
        }

        Voucher voucher = genVoucher(Constants.voucher_activity.REGISTER);
        if (null == voucher) {
            logger.error("failed to generate new voucher");
            return;
        }

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, voucher.getValidity());

        voucher.setUserId(userId);
        voucher.setExpireDate(now.getTime());
        voucherRepository.save(voucher);

        logger.info("a new voucher generated, id=" + voucher.getId());
    }

    /**
     * 代金券活动 - 还款
     *
     * 用户每月第一次还款将获得代金券一张，金额随机
     *
     * @param userId
     */
    public Voucher activityRepay(long userId) {
        if (!isActivityEnabled()) {
            return null;
        }

        if (!ifUserExist(userId)) {
            throw new NoSuchUserException(userId);
        }

        // 查询该用户是否在本月内，通过还款获取过代金券，如果有，则不能再次获取
        Pair<Date, Date> monthRange = DateUtil.getMonthRange(new Date());
        Date startDate = monthRange.getLeft();
        Date endDadte = monthRange.getRight();

        List<Voucher> voucherList = voucherRepository.findByCreateTimeRange(startDate, endDadte,
                Constants.voucher_activity.REPAY, userId);
        logger.info(LogUtil.formatLog(String.format("startDate=%s, endDate=%s, voucher_activity_id=%d, userId=%d",
                DateUtil.formatDateTime(startDate), DateUtil.formatDateTime(endDadte), Constants.voucher_activity.REPAY,
                userId)));

        // 用户本月还款已经获取过代金券，不能在抽取
        if (CollectionUtils.isNotEmpty(voucherList)) {
            logger.info(LogUtil.formatLog("user " + userId + " has already got voucher through activity repay"));
            return null;
        }

        // 生成一张新的代金券，然后关联用户，修改到期时间
        Voucher voucher = genVoucher(Constants.voucher_activity.REPAY);
        if (null != voucher) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, voucher.getValidity());

            voucher.setUserId(userId);
            voucher.setExpireDate(cal.getTime());
            voucherRepository.save(voucher);

            logger.info(LogUtil.formatLog("a voucher generated successfully, id=" + voucher.getId()));
        } else {
            logger.error(LogUtil.formatLog("failed to generate voucher, userId=" + userId));
        }

        return voucher;
    }

    public boolean isActivityEnabled() {
        return VOUCHER_ACTIVITY_ENABLED;
    }

    /**
     * 用户是否存在
     *
     * @param userId
     * @return
     */
    public boolean ifUserExist(long userId) {
        return null !=userRepository.findByUseridAndState(userId,1);
    }

    /**
     * 生成一张代金券
     *
     * @param activityId
     *            活动id
     * @return
     */
    private Voucher genVoucher(long activityId) {
        List<VoucherConfig> voucherConfigList = voucherConfigRepository.findByActivityId(activityId);

        logger.debug("voucher config list=" + voucherConfigList);

        if (CollectionUtils.isEmpty(voucherConfigList)) {
            logger.error("can't find voucher configs by activity id=" + activityId);
            return null;
        }

        List<Pair<Integer, Long>> lotteryProbArr = new ArrayList<>();
        for (VoucherConfig conf : voucherConfigList) {
            lotteryProbArr.add(Pair.of(conf.getLotteryProb(), conf.getId()));
        }

        logger.debug("lottery prob array=" + lotteryProbArr);

        Voucher voucher = null;
        long targetConfId = pickLotteryProb(lotteryProbArr);
        for (VoucherConfig conf : voucherConfigList) {
            if (conf.getId() == targetConfId) {
                voucher = new Voucher();
                voucher.setVoucherActivityId(conf.getActivityId());
                voucher.setVoucherUseState(Voucher.USE_STATE_NOTUSED);
                voucher.setVoucherConfId(conf.getId());
                voucher.setVoucherShareState(conf.getVoucherShareState());
                voucher.setValidity(conf.getValidity());
                voucher.setPrice(conf.getPrice());

                // TODO 需要优化
                voucher.setUpdateTime(new Date());
                voucher.setCreateTime(new Date());

                voucherRepository.save(voucher);
                break;
            }
        }

        if (null == voucher) {
            logger.error(
                    "failed to generate voucher by target conf id=" + targetConfId + " and activity id=" + activityId);
        } else {
            logger.info("new voucher generated, id=" + voucher.getId());
        }

        return voucher;
    }

    /**
     * 根据代金券配置中的概率随机生成一张代金券，支持到万分之一的概率
     *
     * @param lotteryProbArr
     * @return
     */
    private long pickLotteryProb(List<Pair<Integer, Long>> lotteryProbArr) {
        int idIndex = 0;
        for (Pair<Integer, Long> pair : lotteryProbArr) {
            int count = pair.getLeft();
            for (int i = 0; i < count; ++i) {
                ids[idIndex++] = pair.getRight();
            }
        }

        return ids[(int) (Math.random() * 10000)];
    }

}
