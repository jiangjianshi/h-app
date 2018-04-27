package com.huifenqi.hzf_platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.BdResponses;
import com.huifenqi.hzf_platform.handler.ThirdSysHouseRequestHandler;


@RestController
public class ThirdSysHouseCronller {
	
	@Autowired
	private ThirdSysHouseRequestHandler thirdSysHouseRequestHandler;
	
	/**
	 * 上传图片到百度saas
	 */
	@RequestMapping(value = "/thirdSys/task/saas/imageUpload", method = RequestMethod.POST)
	public BdResponses imageUpload(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = thirdSysHouseRequestHandler.imageUpload(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "上传图片到百度sass失败");
		}
		return bdResponses;
	}
	
	/**
	 * 上传公寓信息到百度saas平台
	 */
	@RequestMapping(value = "/thirdSys/task/saas/apartmentApply", method = RequestMethod.POST)
	public BdResponses apartmentApply(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = thirdSysHouseRequestHandler.apartmentApply(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "上传公寓信息到百度sass平台失败");		
		}
		return bdResponses;
	}
	
	/**
	 * 上传公寓信息到百度saas平台回调
	 */
	@RequestMapping(value = "/thirdSys/task/saas/apartmentApplyBack", method = RequestMethod.POST)
	public BdResponses apartmentApplyBack(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = thirdSysHouseRequestHandler.apartmentApplyBack(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "接收百度sass平台公寓回调失败");		
		}
		return bdResponses;
	}

	
	/**
	 * 同步房源到流量方
	 */
	@RequestMapping(value = "/third/thirdSysData", method = RequestMethod.POST)
	public BdResponses thirdSysData(HttpServletRequest request) throws Exception {
		BdResponses bdResponses = null;
		try{
			bdResponses = thirdSysHouseRequestHandler.thirdSysData(request);
		}catch(Exception e){
			return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "同步找房房源到流量方失败");
		}
		return bdResponses;
	}
    
    /**
     * 同步闲鱼账号
     */
    @RequestMapping(value = "/third/sysAliUser", method = RequestMethod.POST)
    public BdResponses addAliUser(HttpServletRequest request) throws Exception {
        BdResponses bdResponses = null;
        try{
            bdResponses = thirdSysHouseRequestHandler.sysAliUser(request);
        }catch(Exception e){
            return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "同步账号到闲鱼失败");
        }
        return bdResponses;
    }
    
    /**
     * 初始化发布账号到队列
     */
    @RequestMapping(value = "/third/initAliUser", method = RequestMethod.POST)
    public BdResponses initAliUser(HttpServletRequest request) throws Exception {
        BdResponses bdResponses = null;
        try{
            bdResponses = thirdSysHouseRequestHandler.initAliUser(request);
        }catch(Exception e){
            return new BdResponses(ErrorMsgCode.ERROR_MSG_ADD_PROG_EXCEPION, "初始化发布账号到队列失败");
        }
        return bdResponses;
    }
}
