/** 
 * Project Name: hzf_platform_project 
 * File Name: CityInfoVersionDaoAop.java 
 * Package Name: com.huifenqi.hzf_platform.dao.aop 
 * Date: 2016年5月21日下午5:42:53 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao.aop;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.context.entity.house.Agency;
import com.huifenqi.hzf_platform.context.entity.house.CompanyOffConfig;
import com.huifenqi.hzf_platform.context.entity.house.CompanyPayMonth;
import com.huifenqi.hzf_platform.context.entity.house.CompanyWhiteConfig;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RedisUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

/**
 * ClassName: CompanyDaoAop date: 2018年8月31日 下午5:42:53 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Aspect
@Component
public class CompanyDaoAop {

	@Autowired
	private RedisCacheManager redisCacheManager;

	private static Log logger = LogFactory.getLog(CompanyDaoAop.class);

	private static final String COMPANY_PAY_MONTH_KEY = "company.list.info";
	private static final String COMPANY_WHITE_CONFIG_KEY = "company.white.config.info";
	private static final String CITY_COMPANY_OFF_CONFIG_KEY = "company.off.config.info";
	private static final String SERVICE_APARTMENT_KEY = "city.house.serviceapartment";

	//查询所有品牌公寓列表by城市ID
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository.findAgenciesByCityId(..)) && args(cityId)")
	public List<Agency> findAgenciesByCityId(ProceedingJoinPoint pjp,long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(SERVICE_APARTMENT_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Agency>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存品牌公司列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<Agency> companys = (List<Agency>) pjp.proceed();

		if (companys != null && !companys.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(companys));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存品牌公司列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(companys)))));
			}
		}
		return companys;
	}
		
	//查询所有品牌公寓列表
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository.findAllAgency(..))")
	public List<Agency> findAllAgency(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(SERVICE_APARTMENT_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<Agency>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存品牌公司列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<Agency> companys = (List<Agency>) pjp.proceed();

		if (companys != null && !companys.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(companys));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存品牌公司列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(companys)))));
			}
		}
		return companys;
	}
	
	private String getAgencyHashKey(String companyId, long cityId) {
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(companyId);
		hashKey.append(StringUtil.HYPHEN);
		hashKey.append(cityId);
		return hashKey.toString();
	}
	
	//查询所有品牌公寓列表-by城市ID&&公司ID
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.house.AgencyManageRepository.getAgencyByCompanyIdAndCityId(..)) && args(companyId,cityId)")
	public Agency getAgencyByCompanyIdAndCityId(ProceedingJoinPoint pjp, String companyId, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(SERVICE_APARTMENT_KEY);
		String hashKey = getAgencyHashKey(companyId, cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, Agency.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获品牌公司信息%s失败", hashKey.toString())));
		}

		Agency company = (Agency) pjp.proceed();

		if (company != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(company));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存品牌公司信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(company)))));
			}
		}
		return company;
	}

	//查询全部支持月付公司列表
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository.findCompanys(..))")
	public List<CompanyPayMonth> findAllCompanys(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(COMPANY_PAY_MONTH_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CompanyPayMonth>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存月付公司列表信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<CompanyPayMonth> companys = (List<CompanyPayMonth>) pjp.proceed();

		if (companys != null && !companys.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(companys));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存月付公司列表信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(companys)))));
			}
		}
		return companys;
	}

	//查询支持月付公司列表-by公司ID
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.company.CompanyPayMonthRepository.findCompanyPayMonthBycompanyIdHzf(..)) && args(companyIdHzf)")
	public CompanyPayMonth findCompanyPayMonthBycompanyIdHzf(ProceedingJoinPoint pjp, String companyIdHzf)
			throws Throwable {
		String key = RedisUtils.getInstance().getKey(COMPANY_PAY_MONTH_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append(companyIdHzf);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, CompanyPayMonth.class);
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获月付公司信息%s失败", hashKey.toString())));
		}

		CompanyPayMonth company = (CompanyPayMonth) pjp.proceed();

		if (company != null) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(company));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存月付公司信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(company)))));
			}
		}
		return company;
	}
	
	//查询白名单
    @Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.company.CompanyWhiteConfigRepository.findBySourceAndCompanyId(..)) && args(source,companyId)")
    public CompanyWhiteConfig findBySourceAndCompanyId(ProceedingJoinPoint pjp, String source,String companyId)
            throws Throwable {
        String key = RedisUtils.getInstance().getKey(COMPANY_WHITE_CONFIG_KEY);
        StringBuilder hashKey = new StringBuilder();
        hashKey.append(source);
        hashKey.append(companyId);

        try {
            String result = redisCacheManager.getHash(key, hashKey.toString());
            if (result != null) {
                return GsonUtils.getInstace().fromJson(result, CompanyWhiteConfig.class);
            }
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog(String.format("从缓存获白名单公司信息%s失败", hashKey.toString())));
        }

        CompanyWhiteConfig company = (CompanyWhiteConfig) pjp.proceed();

        if (company != null) {
            try {
                redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(company));
            } catch (Exception e) {
                logger.error(LogUtils.getCommLog(String.format("保存白名单公司信息(%s=%s)到缓存失败", hashKey.toString(),
                        GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(company)))));
            }
        }
        return company;
    }
    
	//查看全部下架公司列表-by城市ID
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository.findCompanyOffConfigByCityId(..)) && args(cityId)")
	public List<CompanyOffConfig>  findCompanyOffConfigByCityId(ProceedingJoinPoint pjp, long cityId) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_COMPANY_OFF_CONFIG_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");
		hashKey.append(cityId);

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CompanyOffConfig>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取下架公司配置信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<CompanyOffConfig> configList = (List<CompanyOffConfig>) pjp.proceed();

		if (configList != null && !configList.isEmpty() ) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(configList));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存下架公司配置信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(configList)))));
			}
		}

		return configList;
	}
	
	//查看全部下架公司列表
	@Around(value = "execution(* com.huifenqi.hzf_platform.dao.repository.company.CompanyOffConfigRepository.findAllCompanyOffConfig(..))")
	public List<CompanyOffConfig> findAllCompanyOffConfig(ProceedingJoinPoint pjp) throws Throwable {
		String key = RedisUtils.getInstance().getKey(CITY_COMPANY_OFF_CONFIG_KEY);
		StringBuilder hashKey = new StringBuilder();
		hashKey.append("all-");

		try {
			String result = redisCacheManager.getHash(key, hashKey.toString());
			if (result != null) {
				return GsonUtils.getInstace().fromJson(result, new TypeToken<List<CompanyOffConfig>>() {
				}.getType());
			}
		} catch (Exception e) {
			logger.error(LogUtils.getCommLog(String.format("从缓存获取下架公司配置信息%s失败", hashKey.toString())));
		}

		@SuppressWarnings("unchecked")
		List<CompanyOffConfig> configList = (List<CompanyOffConfig>) pjp.proceed();

		if (configList != null && !configList.isEmpty()) {
			try {
				redisCacheManager.putHash(key, hashKey.toString(), GsonUtils.getInstace().toJson(configList));
			} catch (Exception e) {
				logger.error(LogUtils.getCommLog(String.format("保存下架公司配置信息(%s=%s)到缓存失败", hashKey.toString(),
						GsonUtils.getInstace().toJson(GsonUtils.getInstace().toJson(configList)))));
			}
		}

		return configList;
	}
}
