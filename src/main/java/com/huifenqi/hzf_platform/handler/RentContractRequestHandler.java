package com.huifenqi.hzf_platform.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.service.PaymentNewService;
import com.huifenqi.hzf_platform.service.RentPayService;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.HouseItems;
import com.huifenqi.hzf_platform.vo.RentContractBugetVo;
import com.huifenqi.hzf_platform.vo.RentContractDetailVo;
import com.huifenqi.hzf_platform.vo.RentContractVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Arison  on 2017/11/21.
 */
@Service
public class RentContractRequestHandler extends BaseRequestHandler {

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private RentPayService rentPayService;

	@Autowired
	private PaymentNewService paymentNewService;


	public static Map<Integer,String> periodMap;
	static{
		periodMap=new HashMap<>();
		periodMap.put(-1,"全额付");
		periodMap.put(1,"月付");
		periodMap.put(2,"二月付");
		periodMap.put(3,"季付");
		periodMap.put(4,"四月付");
		periodMap.put(5,"五月付");
		periodMap.put(6,"半年付");
		periodMap.put(12,"年付");
		periodMap.put(0,"");
	}

	public static Map<Integer,String> carMap;
	static{
		carMap=new HashMap<>();
		carMap.put(1,"身份证");
		carMap.put(4,"军官证");
		carMap.put(2,"护照");
		carMap.put(3,"往来港澳通行证");
		carMap.put(0,"");
	}
	/**
	 * @Title: getRentContractList
	 * @Description: 查询租房合同列表
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月22日 上午11:20:48
	 */
	public Responses getRentContractList(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String phone = getStringParam("phone");
		// 判断该用户是否绑过卡，如果绑过卡 表示用户已实名，入参增加用户姓名和身份证号，用来过滤合同数据
        String name = "";
        String idNo = "";
        JsonObject paramRetJo = rentPayService.getIdNoAndUserName(userId);
        if (!paramRetJo.isJsonNull() && paramRetJo.has("userName") && paramRetJo.has("idNo")) {
            name = paramRetJo.get("userName").getAsString();
            idNo = paramRetJo.get("idNo").getAsString();
        }
		JsonObject serviceRetJo = rentPayService.getRentContractList(userId+"", phone, name, idNo);
		List<RentContractVo> rentContractVoList=getRentContractVo(serviceRetJo,userId,phone);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		JsonObject ret=new JsonObject();
		ret.add( "list",gson.toJsonTree(rentContractVoList));
		responses.setBody(ret);
		return responses;
	}
    private String getPeriod(int period){
		return periodMap.get(period);
	}

	private String getCard(int cardType){
		return carMap.get(cardType);
	}
	private List<RentContractVo> getRentContractVo(JsonObject jsonObject,long userId,String phone){
		List<RentContractVo> rentContractVoList=new ArrayList<>();
		//int total=jsonObject.get("total").getAsInt();
		if(jsonObject.has("list")){
		    JsonElement jsonList = jsonObject.get("list");
			JsonArray jsonArray=jsonList.getAsJsonArray();
			for(JsonElement je:jsonArray){
				JsonObject jo=je.getAsJsonObject();
		        RentContractVo rentContractVo=new RentContractVo();
				rentContractVo.setApartmentName(jo.get("apartmentName").isJsonNull()?"":jo.get("apartmentName").getAsString());
				rentContractVo.setCommunityName(jo.get("communityName").isJsonNull()?"":jo.get("communityName").getAsString());
				rentContractVo.setHouseNo(jo.get("houseNo").isJsonNull()?"":jo.get("houseNo").getAsString());
				JsonElement rentBeg=jo.get("rentBeginDate");
				if(rentBeg.isJsonNull()){
					rentContractVo.setRentBeginDate("");
				}else{
					rentContractVo.setRentBeginDate(rentBeg.getAsString().replace("-","."));
				}
				JsonElement rentEnd=jo.get("rentEndDate");
				if(rentEnd.isJsonNull()){
					rentContractVo.setRentEndDate("");
				}else{
					rentContractVo.setRentEndDate(rentEnd.getAsString().replace("-","."));
				}

				rentContractVo.setRentContractCode(jo.get("rentContractCode").isJsonNull()?"":jo.get("rentContractCode").getAsString());
				rentContractVo.setRentPrice(jo.get("rentPriceMonth").isJsonNull()?0:jo.get("rentPriceMonth").getAsDouble());
				rentContractVo.setRoomName(jo.get("roomName").isJsonNull()?"":jo.get("roomName").getAsString());
				rentContractVo.setDistrict(jo.get("district").isJsonNull()?"":jo.get("district").getAsString());
				rentContractVo.setSignerPhone(jo.get("signerPhone").isJsonNull()?"":jo.get("signerPhone").getAsString());

				int unPayAmount =jo.get("needPayNum").isJsonNull()?0:jo.get("needPayNum").getAsInt();
				rentContractVo.setUnpayAmount(unPayAmount);

				int isBinded	=jo.get("isBinded").getAsInt();
				int isConfirmed	=jo.get("isConfirmed").getAsInt();

				int contractEffectStatus	=jo.get("contractEffectStatus").getAsInt();

				int isRetreat	=jo.get("isSurrender").getAsInt();

				if(isRetreat==1){
					contractEffectStatus=3;  //已退租时显示 已失败
				}

				rentContractVo.setConstractStatus(contractEffectStatus);
				rentContractVo.setOperationType(2); //默认是待支付状态
				if(0==rentContractVo.getUnpayAmount()){
					rentContractVo.setOperationType(0); //隐藏
				}

				if(isBinded==0&&isConfirmed==0){ // 待确认
					if(contractEffectStatus==3) { //已到期的无需确认
						rentContractVo.setUnpayAmount(0);
						rentContractVo.setOperationType(0); //隐藏
					}else{
						rentContractVo.setOperationType(1);
					}
				}else if(isBinded==1)
				{
					if(isConfirmed==0) {
						if(contractEffectStatus==3) { //已到期的无需确认
							rentContractVo.setOperationType(0); //隐藏
						}else{
						    rentContractVo.setOperationType(1); //二次确认
						}

					}
				}

				//ToDo
				rentContractVo.setRentPayPeriod(getPeriod(jo.get("payPeriod").getAsInt()));
				rentContractVoList.add(rentContractVo);
			}
		}

		List<RentContractVo> rentContractVoListOrderById=rentContractVoList.stream()
				.sorted(Comparator.comparing(RentContractVo::getRentContractCode).reversed())
				.collect(Collectors.toList());

		return rentContractVoListOrderById.stream().sorted(Comparator.comparingInt(RentContractVo::getConstractStatus))
				.collect(Collectors.toList());
	}

	/**
	 * @Title: getRentContractDetail
	 * @Description:查询租房合同详情
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月22日 上午12:00:32
	 */
	public Responses getRentContractDetail(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String rentContractCode= getStringParam("rentContractCode");
		int needHidden= getDefaultIntParam("needHidden",1);
		String phone= getStringParam("phone");
		JsonObject serviceRetJo = rentPayService.getRentContractDetail(userId+"",rentContractCode);
		RentContractDetailVo rentContractDetailVo=getRentContractDetailVo(serviceRetJo,needHidden,userId,phone,rentContractCode);
		Responses responses = new Responses();
		//JsonObject ret=new JsonObject();
		//ret.add( "contractdetail",gson.toJsonTree(rentContractDetailVo));
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(rentContractDetailVo);
		return responses;
	}

	private boolean isRealNamed(long userId){
        boolean isRealNamed=false;
		JsonObject serviceRetJo = paymentNewService.listBankcard(userId);
		JsonArray cardList = null;
		try {
			cardList = serviceRetJo.getAsJsonArray("cardList");
		} catch (Exception e) {
			logger.error("failed to parse card_list");
		}

		if (cardList != null) {
			JsonArray newArray = new JsonArray();
			Iterator<JsonElement> iterator = cardList.iterator();
			while (iterator.hasNext()) {
				JsonElement next = iterator.next();
				if (next instanceof JsonObject) {
					JsonObject bankcardObj = (JsonObject) next;
					if(bankcardObj!=null && !bankcardObj.isJsonNull()){
						isRealNamed=true;
						break;
					}
				}
			}
		}
         return isRealNamed;
	}

	private RentContractDetailVo getRentContractDetailVo(JsonObject jo,int needHidden,long userId,String phone,String rentContractCode){
		RentContractDetailVo rentContractDetailVo=new RentContractDetailVo();
		rentContractDetailVo.setApartmentName(jo.get("apartmentName").isJsonNull()?"":jo.get("apartmentName").getAsString());
		rentContractDetailVo.setCommunityName(jo.get("communityName").isJsonNull()?"":jo.get("communityName").getAsString());
		JsonElement rentBeg=jo.get("rentBeginDate");
		if(rentBeg.isJsonNull()){
			rentContractDetailVo.setRentBeginDate("");
		}else{
			rentContractDetailVo.setRentBeginDate(rentBeg.getAsString().replace("-","."));
		}

		JsonElement rentEnd=jo.get("rentEndDate");
		if(rentEnd.isJsonNull()){
			rentContractDetailVo.setRentEndDate("");
		}else{
			rentContractDetailVo.setRentEndDate(rentEnd.getAsString().replace("-","."));
		}


		rentContractDetailVo.setRentContractCode(jo.get("rentContractCode").isJsonNull()?"":jo.get("rentContractCode").getAsString());
		rentContractDetailVo.setRentPrice(jo.get("rentPriceMonth").isJsonNull()?0:jo.get("rentPriceMonth").getAsInt());
		rentContractDetailVo.setRentDepositFee(jo.get("rentDepositFee").isJsonNull()?0:jo.get("rentDepositFee").getAsInt());
		rentContractDetailVo.setRoomName(jo.get("roomName").isJsonNull()?"":jo.get("roomName").getAsString());
		rentContractDetailVo.setDistrict(jo.get("district").isJsonNull()?"":jo.get("district").getAsString());
		//rentContractDetailVo.setContractPics(jo.get("contractPics").isJsonNull()?"":jo.get("contractPics").getAsString());

		rentContractDetailVo.setHouseNo(jo.get("houseNo").isJsonNull()?"":jo.get("houseNo").getAsString());
		rentContractDetailVo.setComment(jo.get("agencyComment").isJsonNull()?"":jo.get("agencyComment").getAsString());
		rentContractDetailVo.setSignerPhone(jo.get("signerPhone").isJsonNull()?"":jo.get("signerPhone").getAsString());
		rentContractDetailVo.setSignerName(jo.get("signerName").isJsonNull()?"":jo.get("signerName").getAsString());
		rentContractDetailVo.setAgencyRentContractCode(jo.get("agencyRentContractCode").isJsonNull()?"":jo.get("agencyRentContractCode").getAsString());

		rentContractDetailVo.setTenantName(jo.get("tenantName").isJsonNull()?"":jo.get("tenantName").getAsString());
		rentContractDetailVo.setTenantPhone(jo.get("tenantPhone").isJsonNull()?"":jo.get("tenantPhone").getAsString());
		//rentContractDetailVo.setSignDate(jo.get("signDate").isJsonNull()?"":jo.get("signDate").getAsString());
		String signDate=jo.get("signDate").isJsonNull()?"":jo.get("signDate").getAsString();
		if(StringUtil.isBlank(signDate)){
			rentContractDetailVo.setSignDate("");
		}else{
			rentContractDetailVo.setSignDate(signDate.replace("-","."));
		}


		rentContractDetailVo.setRentPayPeriod(getPeriod(jo.get("payPeriod").isJsonNull()?0:jo.get("payPeriod").getAsInt()));

		JsonElement jsonBasicNumbers = jo.get("basicNumbers");
		List<HouseItems> basicNumbers=rentContractDetailVo.getBasicNumbers();
		if(jsonBasicNumbers!=null && !jsonBasicNumbers.isJsonNull()) {
			JsonArray jsonArray = jsonBasicNumbers.getAsJsonArray();
			if(jsonArray!=null && jsonArray.size()>0)
			{
				for(JsonElement je:jsonArray){
					HouseItems houseItems=new HouseItems();
					JsonObject j=je.getAsJsonObject();
					houseItems.setName(j.get("basicNumberDictValue").isJsonNull()?"":j.get("basicNumberDictValue").getAsString());
					houseItems.setInfo(j.get("beginBasicNumber").isJsonNull()?"":j.get("beginBasicNumber").getAsString());
					basicNumbers.add(houseItems);
				}
			}
		}


		JsonElement jsonGoodsItems= jo.get("goodsItems");
		List<HouseItems> goodsItems=rentContractDetailVo.getGoodsItems();
		if(jsonGoodsItems!=null && !jsonGoodsItems.isJsonNull()) {
			JsonArray jsonArray = jsonGoodsItems.getAsJsonArray();
			if(jsonArray!=null && jsonArray.size()>0)
			{
				for(JsonElement je:jsonArray){
					HouseItems houseItems=new HouseItems();
					JsonObject j=je.getAsJsonObject();
					houseItems.setName(j.get("goodsName").isJsonNull()?"":j.get("goodsName").getAsString());
					houseItems.setInfo(j.get("goodsNumber").isJsonNull()?"":j.get("goodsNumber").getAsString());
					goodsItems.add(houseItems);
				}
			}
		}

		JsonElement jsonOtherFees= jo.get("otherFees");
		List<HouseItems> otherFees=rentContractDetailVo.getOtherFees();
		if(jsonOtherFees!=null && !jsonOtherFees.isJsonNull()) {
			JsonArray jsonArray = jsonOtherFees.getAsJsonArray();
			if(jsonArray!=null && jsonArray.size()>0)
			{
				for(JsonElement je:jsonArray){
					HouseItems houseItems=new HouseItems();
					JsonObject j=je.getAsJsonObject();
					houseItems.setName(j.get("feeDictValue").isJsonNull()?"":j.get("feeDictValue").getAsString());
					houseItems.setInfo(j.get("price").isJsonNull()?"":j.get("price").getAsString());
					houseItems.setPayType(j.get("payType").isJsonNull()?1:j.get("payType").getAsInt());
					otherFees.add(houseItems);
				}
			}
		}

		int isUseWithhold	=jo.get("isUseWithhold").getAsInt(); //用户
		int isAgencyUseWithhold	=jo.get("isAgencyUseWithhold").getAsInt();
		rentContractDetailVo.setIsAgencyUseWithhold(isAgencyUseWithhold);//中介公司合同是否扶持
		//rentContractDetailVo.setIsSupportWithhold(isUseWithhold);
		rentContractDetailVo.setWithHoldStatus(isUseWithhold);
		//TODO
		int isBinded	=jo.get("isBinded").getAsInt();
		int isConfirmed	=jo.get("isConfirmed").getAsInt();
		int contractEffectStatus	=jo.get("contractEffectStatus").getAsInt();
		rentContractDetailVo.setContractConfirmStatus(Constants.ContractConfirmStatus.DEFAULT); //默认是查看合同
		if(isBinded==0 && isConfirmed==0){ // 待确认 ,需要确认
			rentContractDetailVo.setContractConfirmStatus(Constants.ContractConfirmStatus.RN_NO_WD);
			rentContractDetailVo.setIsAgencyUseWithhold(Constants.SupportWithDraw.NO_SUPPORT);//合同确认时，统一状态为中介公司合同不支持代扣
		}else if(isBinded==1)
		{
			if(isConfirmed==0){
				rentContractDetailVo.setContractConfirmStatus(Constants.ContractConfirmStatus.RE_CONFIRM) ;//二次确认
			}
		}

		rentContractDetailVo.setConstractStatus(contractEffectStatus);  //履约中
		String urls=jo.get("contractPics").isJsonNull()?"":jo.get("contractPics").getAsString();
		if(StringUtil.isNotBlank(urls))
		{
			String [] usrAry=urls.split(",");
			for(String str:usrAry)
			{
				rentContractDetailVo.getContractPics().add(str);
			}
		}

		int tenantDocType =jo.get("tenantDocType").isJsonNull()?0:jo.get("tenantDocType").getAsInt();
		// 除身份证以外的证件，不展示证件名称和证件号 by YDM 2017/12/23
		if (tenantDocType == 1) {
		    rentContractDetailVo.setTenantDocType(getCard(tenantDocType));
		} else {
		    rentContractDetailVo.setTenantDocType("");
		}
		if(needHidden==1){
		    if (tenantDocType == 1) {
		        String  tenantIdNo=jo.get("tenantIdNo").isJsonNull()?"0":jo.get("tenantIdNo").getAsString();
		        tenantIdNo=encrypt(tenantDocType,tenantIdNo);
		        rentContractDetailVo.setTenantIdNo(tenantIdNo);
		    } else {
		        rentContractDetailVo.setTenantIdNo("");
		    }
			String tenrentPhone=rentContractDetailVo.getTenantPhone();
			if(StringUtil.isNotBlank(tenrentPhone)){
				tenrentPhone=tenrentPhone.substring(0,3)+" **** "+tenrentPhone.substring(tenrentPhone.length()-4,tenrentPhone.length());
			}
			rentContractDetailVo.setTenantPhone(tenrentPhone);
		}else{
		    if (tenantDocType == 1) {
		        String  tenantIdNo=jo.get("tenantIdNo").isJsonNull()?"0":jo.get("tenantIdNo").getAsString();
		        rentContractDetailVo.setTenantIdNo(tenantIdNo);
		    } else {
                rentContractDetailVo.setTenantIdNo("");
            }
		}

		//查询合同的其它待支付的费用 begin
		int unPayAmount =jo.get("needPayNum").isJsonNull()?0:jo.get("needPayNum").getAsInt();
		rentContractDetailVo.setUnpayAmount(unPayAmount);
		rentContractDetailVo.setBankToken(jo.get("bankToken").isJsonNull()?"":jo.get("bankToken").getAsString());
		rentContractDetailVo.setIsConfirmed(isConfirmed);
		return rentContractDetailVo;
	}
	private  String encrypt(int tenantDocType,String tenantIdNo ){
		/*身份证：15位或18位，展示前三位和后四位
		护照：8位，*掉后4位
		军官证：8位，*掉后4位
		港澳通行证：9位，*掉后4位*/
        if(StringUtil.isBlank(tenantIdNo)){
        	return "";
		}
		String retStr="";
		if(tenantDocType==1){
			retStr=tenantIdNo.substring(0,3)+"***********"+tenantIdNo.substring(tenantIdNo.length()-4,tenantIdNo.length());
		}else if(tenantDocType==2){
			retStr=tenantIdNo.substring(0,tenantIdNo.length()-4)+"****";
		}else if(tenantDocType==3){
			retStr=tenantIdNo.substring(0,tenantIdNo.length()-4)+"****";
		}else if(tenantDocType==4){
			retStr=tenantIdNo.substring(0,tenantIdNo.length()-4)+"****";
		}
		return retStr;
	}
	/**
	 * @Title: rentWithhold
	 * @Description: 设置自动代扣
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月21日 上午12:08:23
	 */
	public Responses rentWithhold(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		int isWithhold=getIntParam("isUseWithhold");
		String bankToken=getDefaultStringParam("bankToken","");
		String rentContractCode= getStringParam("rentContractCode");
		JsonObject serviceRetJo = rentPayService.rentWithhold(userId+"",rentContractCode,isWithhold,bankToken);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(serviceRetJo);
		return responses;
	}

	/**
	 * @Title: updateWithholdStatus
	 * @Description: 更新是否开启自动代扣
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月24日 上午16:07:23
	 */
	public Responses updateWithholdStatus(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		int isWithhold=getIntParam("isUseWithhold");
		String bankToken=getDefaultStringParam("bankToken","");
		if(isWithhold==1){
			if(StringUtil.isBlank(bankToken)){
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM,"请设置一张交租银行卡");
			}
		}
		String rentContractCode= getStringParam("rentContractCode");
		JsonObject serviceRetJo = rentPayService.updateWithholdStatus(userId+"",rentContractCode,isWithhold,bankToken);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(serviceRetJo);
		return responses;
	}


	/**
	 * @Title: rentWithhold
	 * @Description: 确认修改出租合同
	 * @return Responses
	 * @author Arison
	 * @dateTime 2017年11月22日 上午11:42:15
	 */
	public Responses rentReContractConfirm(HttpServletRequest request) throws Exception {
		long userId = sessionManager.getUserIdFromSession();
		String rentContractCode= getStringParam("rentContractCode");
		JsonObject serviceRetJo = rentPayService.rentReContractConfirm(userId+"",rentContractCode);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(serviceRetJo);
		return responses;
	}
}
