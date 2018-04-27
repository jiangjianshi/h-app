/** 
 * Project Name: usercomm_project 
 * File Name: ContractService.java 
 * Package Name: com.huifenqi.usercomm.service 
 * Date: 2016年3月3日下午5:13:29 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */  
package com.huifenqi.hzf_platform.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.ServiceInvokeFailException;
import com.huifenqi.hzf_platform.utils.*;
import com.huifenqi.usercomm.dao.contract.ContractSnapshotRepository;
import com.huifenqi.usercomm.dao.contract.InstallmentContractRepository;
import com.huifenqi.usercomm.domain.User;
import com.huifenqi.usercomm.domain.contract.ContractSnapshot;
import com.huifenqi.usercomm.domain.contract.InstallmentContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/** 
 * ClassName: ContractService
 * date: 2016年3月3日 下午5:13:29
 * Description: 合同服务
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
@Component
public class ContractService {
	private static Logger logger = LoggerFactory.getLogger(ContractService.class);
	
	private static final String SERVICE_API = "http://%s/%s";
	
	private static Gson gson = GsonUtils.buildGson();

	@Autowired
	private ContractSnapshotRepository csRepository;

	@Autowired
	private InstallmentContractRepository icRepository;
	
	@Autowired
	private Configuration configuration;

	@Value("${hfq.service.contract-query.depend-on-userid}")
	private Boolean contractQueryDependOnUserId = false;

	@PostConstruct
	private void init() {
	    logger.info("is contracts queried by userId: " + contractQueryDependOnUserId);
    }


    // ----- ContractSnapshot 与 InstallmentContract的查询做了兼容处理，并且加了切换开关
    // 这么做的原因是：
    // 1. 我们要从依赖手机号查询合同变更为依赖用户id查询
    // 2. 由于userid填充不久，担心会出什么问题，增加切换开关，如果遇到问题再切回来
    // 服务上线1个月后，这部分兼容代码可直接简化为只依赖userid对合同进行检索
	public List<ContractSnapshot> findContractSnapshots(User user) {
        if (contractQueryDependOnUserId) {
            return csRepository.findValidContractByUserId(user.getUserid());
        } else {
            return csRepository.findValidContractByPhone(user.getPhone());
        }
    }

    public List<ContractSnapshot> findTakingEffectContractSnapshots(User user) {
        if (contractQueryDependOnUserId) {
            return csRepository.findEffectContractByUserId(user.getUserid());
        } else {
            return csRepository.findEffectContractByPhone(user.getPhone());
        }
    }
	/*
	 查找不可以注销的合同
	*/
	public List<ContractSnapshot> findNoLogoffContractSnapshots(User user) {
		if (contractQueryDependOnUserId) {
			return csRepository.findNoLogoffContractContractByUserId(user.getUserid());
		} else {
			return csRepository.findNoLogoffContractContractByPhone(user.getPhone());
		}
	}

	/*
	 在ic表中查找满足注销条件的合同
	*/
	public List<InstallmentContract> findNoLogoffInstallmentContract(User user) {

		//hfq.logoff.daylimit=2592000000
		//hfq.logoff.hourlimit=259200000
		Date dateLimit30= DateUtil.addSeconds(new Date(),(int)(configuration.invalidDayLimit*(-1)/1000));
		Date dateLimit72= DateUtil.addSeconds(new Date(),(int)(configuration.invalidHourLimit*(-1)/1000));
		if (contractQueryDependOnUserId) {
			return icRepository.findNoLogoffContractContractByUserId(user.getUserid(),dateLimit30,dateLimit72);
		} else {
			return icRepository.findNoLogoffContractContractByPhone(user.getPhone(),dateLimit30,dateLimit72);
		}
	}



	public List<ContractSnapshot> findContractsSnapshotsForList(User user) {
        if (contractQueryDependOnUserId) {
            return csRepository.findValidContractForListByUserId(user.getUserid());
        } else {
            return csRepository.findValidContractForListByPhone(user.getPhone());
        }
    }

    /**
     * 根据用户id或者手机号查询有效的合同提交记录
     *
     * @param user
     * @return
     */
    public List<InstallmentContract> findInstallmentContracts(User user) {
        if (contractQueryDependOnUserId) {
            return icRepository.findValidContractsByUserId(user.getUserid());
        } else {
            return icRepository.findValidContractsByPhone(user.getPhone());
        }
    }

    public List<InstallmentContract> findInstallmentContractsByFundType(User user, Integer fundType) {
        if (contractQueryDependOnUserId) {
            return icRepository.findAllContractByUserIdAndFundType(user.getUserid(), fundType);
        } else {
            return icRepository.findAllContractByPhoneAndFundType(user.getPhone(), fundType);
        }
    }

    public List<InstallmentContract> findAllInstallmentContracts(User user) {
        if (contractQueryDependOnUserId) {
            return icRepository.findAllContractByUserId(user.getUserid());
        } else {
            return icRepository.findAllContractByPhone(user.getPhone());
        }
    }
    //查找用户申请过的合同
	public List<InstallmentContract> findAppliedContracts(User user) {
		if (contractQueryDependOnUserId) {
			return icRepository.findAppliedContractByUserId(user.getUserid());
		} else {
			return icRepository.findAppliedContractByPhone(user.getPhone());
		}
	}


    /**
	 * 获取合同的URL
	 * @param contractNo
	 * @return
	 */
	public JsonObject getContractUrl(String contractNo, String platform, String userName, String userIdNo, String phone) {
		String url = String.format(SERVICE_API, configuration.contractApiHost, "contract/get_contract_url/");
		
		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);
		params.put("platform", platform);
		params.put("user_name", userName);
		params.put("user_id_no", userIdNo);
		params.put("phone", phone);
		params.put("data_source", "1");
		
		return request(url, params);
	}
	
	/**
	 * 获取合同的URL
	 * @param contractNo
	 * @param platform
	 * @return
	 */
	public JsonObject getContractUrl(String contractNo, String platform) {
		String url = String.format(SERVICE_API, configuration.contractApiHost, "contract/get_contract_url/");
		
		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);
		params.put("platform", platform);
		
		return request(url, params);
	}
	
	/**
	 * 发起请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private JsonObject request(String url, Map<String, String> params) {
		String response = null;
		try {
			response = HttpUtil.get(url, params);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new ServiceInvokeFailException("服务暂时不可用");
		}

		JsonObject serviceRespJo = gson.fromJson(response, JsonObject.class);

		JsonObject statusJo = serviceRespJo.getAsJsonObject("status");
		int code = statusJo.getAsJsonPrimitive("code").getAsInt();
		if (0 != code) {
			String errMsg = statusJo.getAsJsonPrimitive("description").getAsString();
			throw new ServiceInvokeFailException(statusJo.getAsJsonPrimitive("code").getAsInt(), errMsg);
		}

		return serviceRespJo.getAsJsonObject("result");
	}
	
	public JsonObject getContracts(String contractNo) {
		String url = String.format(SERVICE_API, configuration.contractApiHost, "contract/get_url_all/");

		Map<String, String> params = new HashMap<>();
		params.put("contract_no", contractNo);		

		JsonObject result =  request(url, params);
		if (result.isJsonNull() || result.entrySet().size() == 0) {
			return new JsonObject();
		}
		String mainEleContractName = result.get("name").getAsString();
		String mainEleContractUrl = result.get("contract_electronic").getAsString();
		JsonObject retObj = new JsonObject();
		retObj.addProperty("mainEleContractName", mainEleContractName);
		retObj.addProperty("mainEleContractUrl", mainEleContractUrl);
		
		JsonArray newArray = new JsonArray();		
		JsonArray eleContracts = result.getAsJsonArray("electronic");
		Iterator<JsonElement> iterator = eleContracts.iterator();
		while (iterator.hasNext()) {
			JsonElement next = iterator.next();
			if (next instanceof JsonObject) {
				JsonObject eleContract = (JsonObject) next;
				JsonObject subObj = new JsonObject();
				replaceOrCreateKey(eleContract, subObj, "f_sources_capital_name", "eleContractName");
				replaceOrCreateKey(eleContract, subObj, "f_electronic_url", "eleContractUrl");
				
				newArray.add(subObj);
			}
		}
		retObj.add("subContracts", newArray);
		return retObj;
	}
	
	/**
	 * 替换JsonObject字段名
	 * 
	 * @param srcObj
	 * @param dstObj
	 * @param oldKey
	 * @param newKey
	 */
	private void replaceOrCreateKey(JsonObject srcObj, JsonObject dstObj, String oldKey, String newKey) {
		if (srcObj == null || dstObj == null) {
			return;
		}

		if (StringUtil.isEmpty(oldKey) || StringUtil.isEmpty(newKey)) {
			return;
		}

		JsonElement jsonElement = srcObj.get(oldKey);
		if (jsonElement != null) {
			dstObj.add(newKey, jsonElement);
		}
	}
}
