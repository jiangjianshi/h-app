package com.huifenqi.hzf_platform.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.CompanyConstants;
import com.huifenqi.hzf_platform.context.entity.house.CompanyPayMonth;
import com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;

@Service
public class HouseTagMonthPayTask {

	@Resource
	private HouseDetailRepository houseDetailRepository;

	@Resource
	private HouseBaseRepository houseBaseRepository;

	@Resource
	private CompanyPayMonthRepository companyPayMonthRepository;

	@Resource
	private RedisCacheManager redisCacheManager;

	@Resource
	private TaskConfiguration taskConfiguration;

	private static Logger logger = LoggerFactory.getLogger(HouseTagMonthPayTask.class);

	private static final String COMPANY_PAY_MONTH_KEY = "hzf_platform-company.list.info";

	@Scheduled(cron = "${hfq.houseTag.monthPay.house.task}") // 每五分钟执行一次
	@Transactional
	public void houseTagCollect() throws Exception {
		if (!taskConfiguration.isScheduleStatus()) {
			logger.info("本台机器定时任务未开启，不能执行收集房源月付标签定时任务");
			return;
		}
		// 查询状态变更的公司列表
		List<CompanyPayMonth> listcpms = companyPayMonthRepository.findCompanys();
		if(listcpms != null && !listcpms.isEmpty()){
			for (CompanyPayMonth cpm : listcpms) {
				// 根据公司ID查询该公司房源信息
				List<String> sellIds = houseBaseRepository.findByCompanyId(cpm.getCompanyIdHzf());
				if (sellIds != null && !sellIds.isEmpty()) {
					// 更新该公司房源状态为待收集
					houseDetailRepository.setIsRunByIds(sellIds);
					logger.info("房源更新为待收集，公司名称：" + cpm.getCompanyNameHzf());
					
					//更新公司为已经收集
					companyPayMonthRepository.setIsRunByCompanyIdHzf(cpm.getCompanyIdHzf(),
							CompanyConstants.CompanyHouseDetail.IS_RUN_YES);
					logger.info("支付公司收集状态更新为已收集，公司名称：" + cpm.getCompanyNameHzf());

					// 清除缓存
					redisCacheManager.delete(COMPANY_PAY_MONTH_KEY);
				}
			}
		} else {
			logger.info("收集房源月付标签定时任务结束，支付公司状态无变化");
		}
		
		logger.info("收集房源月付标签定时任务结束");
	}

}
