/** 
 * Project Name: hzf_smart
 * File Name: SmartMeterRequestHandler.java
 * Package Name: com.huifenqi.hzf_platform.handler
 * Date:  2018年1月18日下午3:07:30
 * Copyright (c) 2017, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huifenqi.hzf_platform.comm.BaseRequestHandler;
import com.huifenqi.hzf_platform.comm.LockManager;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.comm.TradeOrderConstants;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.context.exception.TradeOrderException;
import com.huifenqi.hzf_platform.context.response.Responses;
import com.huifenqi.hzf_platform.service.PaymentNewService;
import com.huifenqi.hzf_platform.service.SmartService;
import com.huifenqi.hzf_platform.service.TradeServcie;
import com.huifenqi.hzf_platform.utils.DateUtil;
import com.huifenqi.hzf_platform.utils.GsonUtil;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.SessionManager;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.vo.ApiResult;
import com.huifenqi.hzf_platform.vo.PayChannelVo;
import com.huifenqi.hzf_platform.vo.PublicFeeVo;
import com.huifenqi.hzf_platform.vo.SmartServiceMeterVo;
import com.huifenqi.usercomm.dao.TradeOrderRepository;
import com.huifenqi.usercomm.domain.TradeOrder;

/**
 * ClassName: SmartMeterRequestHandler
 * date: 2018年1月18日 下午3:07:30
 * Description:
 * @author arison
 * @version
 * @since JDK 1.8
 */

@Component
public class SmartMeterRequestHandler extends BaseRequestHandler {
	@Autowired
	private TradeOrderRepository tradeOrderRepository;

	@Autowired
	private TradeOrderUtils tradeOrderUtils;

	@Autowired
	private LockManager lockManager;

	@Autowired
	private PaymentNewService paymentService;
	
	@Autowired
	private TradeServcie tradeServcie;

    @Autowired
	private RedisClient redisClient;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private SmartService smartService;
	
	private static String NAME_PREFIX = "门牌号：";

	/**
	 * 查询电表列表
	 *  update by arison 20170509
	 * @return
	 */
	public Responses reqMeterList() {
		long userId = sessionManager.getUserIdFromSession();
		int pageNum = getDefaultIntParam("pageNum",1);
		int pageSize = getDefaultIntParam("pageSize",10);
		JsonObject reqJo = smartService.reqMeterList(userId, pageNum, pageSize);
		JsonObject retJo = new JsonObject();
		JsonArray mArray = new JsonArray();
		int totalNum = reqJo.get("totalNum").getAsInt();
		if (reqJo.has("ammeters") && reqJo.get("ammeters") != null) {
			JsonElement ammeters = reqJo.get("ammeters");
			List<SmartServiceMeterVo> meterDtoList = GsonUtils.buildCommGson().fromJson(ammeters, new TypeToken<ArrayList<SmartServiceMeterVo>>(){}.getType());
			if (!CollectionUtils.isEmpty(meterDtoList)) {
				for (SmartServiceMeterVo meterDto : meterDtoList) {
					JsonObject meter = new JsonObject();
					
					String roomName = NAME_PREFIX + meterDto.getRoomName();
					if(meterDto.getHouseType()==2 && meterDto.getRentType()==2){
						roomName = NAME_PREFIX + meterDto.getPlateNo() + meterDto.getRoomName();
                        meter.addProperty("rentType",2);
                    }else if(meterDto.getHouseType()==2 && meterDto.getRentType()==1){
                    	roomName = NAME_PREFIX + meterDto.getPlateNo();
                        meter.addProperty("rentType", 1);
                    }
					meter.addProperty("roomName", roomName);
					meter.addProperty("meterStatus", meterDto.getOnlineState());
					meter.addProperty("meterId", meterDto.getId());
					meter.addProperty("unitPrice", meterDto.getPrice());
					meter.addProperty("leaveMoney", meterDto.getRemainingMoney().doubleValue());//一位两位
					meter.addProperty("meterSn", meterDto.getAmmeterSn());
					meter.addProperty("roomUuid", meterDto.getRoomUuid());
					meter.addProperty("leaveKwh", meterDto.getRemainingKwh().doubleValue()); //一位小数
					meter.addProperty("roomUuid", meterDto.getRoomUuid());
					meter.addProperty("userPhone", meterDto.getUserPhone());
					meter.addProperty("agencyId", meterDto.getAgencyId());
					meter.addProperty("userName", meterDto.getUserName());
					mArray.add(meter);
				}
			}
		}

		retJo.addProperty("total", totalNum);
		retJo.add("list", mArray);
		retJo.addProperty("currentPageNum", pageNum);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 获取购电金额
	 *
	 * @return
	 */
	public Responses getPayList() {
		int unitPrice = getIntParam("unitPrice"); //单价：单位分
		JsonObject retJo = new JsonObject();
		JsonArray mArray = new JsonArray();
		//int [] totalPrice=new int[]{50,100,200,300};
		int [] totalPrice=new int[]{50,100,200,300};
		for(int price:totalPrice){
			JsonObject meter = new JsonObject();
			meter.addProperty("price",price);
			meter.addProperty("chargeKwh", String.format("%.1f", (price*100.0)/unitPrice));
			mArray.add(meter);
		}
		retJo.add("list", mArray);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 电量明细
	 *
	 * @return
	 */
	public Responses reqPowerDetails() {
        long userId = sessionManager.getUserIdFromSession();
		String roomUuid=getStringParam("roomUuid");
		String meterSn=getStringParam("meterSn");
		String time=getDefaultStringParam("time","");
		String currentDate=DateUtil.formatCurrentDate();
        if(StringUtils.isBlank(time)){
			time=currentDate.substring(0,7);
        }else{
			String sixMonths=DateUtil.formatDateMonth(DateUtil.addMonths(new Date(),-6));
        	if(sixMonths.compareTo(time)>0){//时间不在半年内
                JsonObject retJo = new JsonObject();
                retJo.addProperty("totalChargeKwh",0.0);
                retJo.addProperty("totalAllKwh",0.0);
                retJo.addProperty("time",0.0);
                retJo.add("powerDetails",new JsonArray());
                Responses responses = new Responses();
                responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
                responses.getMeta().setErrorMessage("success");
                responses.setBody(retJo);
                return responses;
			}
		}

		JsonObject reqJo = smartService.reqChargeDetail(userId+"",roomUuid, meterSn, time); //获取电量明细
		JsonObject retJo = new JsonObject();
		retJo.addProperty("totalChargeKwh",reqJo.get("totalChargeKwh").getAsString());
		retJo.addProperty("totalAllKwh",reqJo.get("totalAllKwh").getAsString());
		retJo.addProperty("time",convertDate(reqJo.get("time").getAsString()));
		JsonArray mArray = new JsonArray();
		JsonElement powerDetails = reqJo.get("powerDetails");
		if (powerDetails != null && !powerDetails.isJsonNull()) {
			//List<SmartServiceMeterVo> meterDtoList = GsonUtils.buildCommGson().fromJson(powerDetails, new TypeToken<ArrayList<SmartServiceMeterVo>>(){}.getType());
			JsonArray pd=powerDetails.getAsJsonArray();
			for (JsonElement je : pd) {
				JsonObject jo =je.getAsJsonObject();
				JsonObject newJo =new JsonObject();
				String pbDate=jo.get("date").getAsString();
				//String tmpPbDate=pbDate.substring(0,7);
				newJo.addProperty("date",convertDate(pbDate));
				newJo.addProperty("chargeKwh",addOper(jo.get("chargeKwh").getAsString(),"+"));
				newJo.addProperty("totalKwh","-"+jo.get("totalKwh").getAsString());
				newJo.addProperty("pubKwh",addOper(jo.get("pubKwh").getAsString(),"-"));
				newJo.addProperty("leaveKwh",jo.get("enableKwh").getAsString());
				JsonArray pubDetails=jo.get("pubDetails").getAsJsonArray();
				int showToken=0;
				if(pubDetails != null && !pubDetails.isJsonNull()){
					JsonArray pdJa=new JsonArray();
					for (JsonElement pde : pubDetails) {
						JsonObject tmpJo =new JsonObject();
						JsonObject pdeJo =pde.getAsJsonObject();
						String pubDate=pdeJo.get("date").getAsString();
						//if(!pubDate.startsWith(tmpPbDate)){
						if(!pubDate.equals(pbDate)){
							showToken=1;
							tmpJo.addProperty("type","补扣");
						}else{
							tmpJo.addProperty("type","当天");
						}
						tmpJo.addProperty("date",pubDate.replace("-","."));
						tmpJo.addProperty("pubKwh",pdeJo.get("pubKwh").getAsString());
						pdJa.add(tmpJo);
					}
					newJo.add("pubDetails",pdJa);
				}else{
					newJo.add("pubDetails",new JsonArray());
				}
				newJo.addProperty("showToken",showToken);
				mArray.add(newJo);
			}
		}

		retJo.add("powerDetails",mArray);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	private String convertDate(String oldDate){
		String[] ary=oldDate.split("-");
		StringBuilder sb=new StringBuilder();
		if(ary.length==2){
			sb.append(ary[0]).append("年").append(ary[1]).append("月");
		}
		if(ary.length==3){
			sb.append(ary[0]).append("年").append(ary[1]).append("月").append(ary[2]).append("日");
		}
		return sb.toString();
	}

    private String addOper(String oldNum,String operator){
      if("0.0".equals(oldNum)) {
          return oldNum;
      }
          return operator+oldNum;
    }
	/**
	 * 获取渠道费用
	 *
	 * @return
	 */
	public Responses reqChannelFee() {
		double payPrice = Double.parseDouble(getStringParam("payPrice")); //单位元
		String paymentChannel = this.getStringParam("paymentChannel");//商家编号
		/*JsonObject serviceRetJo= paymentService.calcChannelFee(paymentChannel,payPrice*100);
		*/
		JsonObject retJo = new JsonObject();
		if(paymentChannel.equals("alipayApp")){
		   retJo.addProperty("payChannel","alipay");
		}else if(paymentChannel.equals("wechatApp")){
			retJo.addProperty("payChannel","wx");
		}else if(paymentChannel.equals("bankCard")){
			retJo.addProperty("payChannel","yeepay");
		}

		retJo.addProperty("payPrice", String.format("%.2f", (payPrice*1.0)));
		retJo.addProperty("paymentChannelIncome", String.format("%.2f", 0.0));
		retJo.addProperty("amount", String.format("%.2f", (payPrice*1.0)));
	    //retJo.addProperty("channelRate", String.format("%.1f", (payPrice*100.0)/payPrice));
		/*
		retJo.addProperty("payChannel", serviceRetJo.getAsJsonPrimitive("channel").getAsString());
		retJo.addProperty("payPrice", serviceRetJo.getAsJsonPrimitive("pay_price").getAsLong());
		retJo.addProperty("paymentChannelIncome",
				serviceRetJo.getAsJsonPrimitive("payment_channel_income").getAsLong());
		retJo.addProperty("amount", serviceRetJo.getAsJsonPrimitive("amount").getAsLong());
         */

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 租客公摊记录列表
	 * update by arison 201705016
	 * @return
    */
	public Responses reqPublicFeeList() {
		long userId = sessionManager.getUserIdFromSession();
		int pageNum = getIntParam("page_num");
		int pageSize = getIntParam("page_size");

		if(pageNum==0){
			pageNum=1;
		}

		if(pageSize==0){
			pageSize=10;
		}
		JsonObject reqJo = smartService.reqPublicFeeList(userId, pageNum, pageSize);
		JsonObject retJo = new JsonObject();
		JsonArray mArray = new JsonArray();

		int totalNum = reqJo.get("totalNum").getAsInt();

		if (reqJo.has("publicRooms") && reqJo.get("publicRooms") != null) {
			JsonElement fees = reqJo.get("publicRooms");
			List<PublicFeeVo> publicFeeDtoList = GsonUtils.buildCommGson().fromJson(fees, new TypeToken<ArrayList<PublicFeeVo>>(){}.getType());
			if (!CollectionUtils.isEmpty(publicFeeDtoList)) {
				for (PublicFeeVo publicFeeDto : publicFeeDtoList) {
					JsonObject meter = new JsonObject();
					int publicFeeState=publicFeeDto.getPublicState();
					meter.addProperty("user_id", publicFeeDto.getUserId());
					meter.addProperty("meter_kwh", publicFeeDto.getTotalKwh());
					meter.addProperty("fee_time", publicFeeDto.getPublicTime());
					meter.addProperty("fee_state", publicFeeState);
					if(publicFeeState==1){
						meter.addProperty("fee_state_desc", "扣电中");
					}else if(publicFeeState==2){
						meter.addProperty("fee_state_desc", "扣电成功");
					}else if(publicFeeState==3){
						meter.addProperty("fee_state_desc", "扣电失败");
					}
					meter.addProperty("meter_sn", publicFeeDto.getAmmeterSn());
					mArray.add(meter);
				}
			}
		}

		retJo.addProperty("total", totalNum);
		retJo.add("list", mArray);
		retJo.addProperty("current_page_num", pageNum);

		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	//电表订单相关接口--------------------------------------------------------------------
	/**
	 * 生成用户订单
	 * 
	 * @return
	 */
	public Responses reqCreateTradeOrder() {
		long userId = sessionManager.getUserIdFromSession();
		String productType = this.getDefaultStringParam("productType","10000001"); // 商品类型：购电:10000001
		String businessNo = this.getStringParam("agencyId");//商家编号
		String productNo = this.getStringParam("meterSn"); //商品序号: 购电时就是电表序列号
		String payAmountStr = this.getStringParam("payAmount");
		int payAmount=(int)(Double.parseDouble(payAmountStr)*100);  //add by arison
		int unitPrice = this.getIntParam("unitPrice");
		int quantity=0;
		String chargeKwhStr = this.getStringParam("chargeKwh");
		try {
			quantity= (int)(Double.parseDouble(chargeKwhStr)*10);
		} catch (NumberFormatException e) {
				throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM,"chargeKwh参数异常");
		}

		String userName=this.getDefaultStringParam("userName","");
		String userPhone=this.getDefaultStringParam("userPhone","");

		String productTypeId = TradeOrderConstants.getProductOrderIdByProductNo(productType);
		String productName = TradeOrderConstants.getProductNameByProductNo(productType);
		long productHfqId = this.getLongParam("meterId");
		if(payAmount <= 0) {
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM,"支付总金额必须大于0");
		}

		TradeOrder tradeOrder = new TradeOrder();
		tradeOrder.setUserId(userId);
		tradeOrder.setProductTypeNo(productType);
		if (TradeOrderConstants.PRODUCT_METER_NO.equals(productType)) {
			String meterName =this.getDefaultStringParam("productName",""); //?????
			String productDesc = userName+ "给" + meterName + "电表充值";
			tradeOrder.setProductDesc(productDesc);
		}
		// 产品描述
		tradeOrder.setProductName(productName);
		tradeOrder.setProductNo(productNo);
		tradeOrder.setProductHfqId(productHfqId);
		tradeOrder.setProductTypeNo(productType);
		tradeOrder.setBusinessNo(businessNo);
		tradeOrder.setPayAmount(payAmount);
		tradeOrder.setUnitPrice(unitPrice);
		tradeOrder.setQuantity(quantity);
		tradeOrder.setCreateTime(new Date());
		// tradeOrder.setRefundTime(new Date());
		tradeOrder.setUpdateTime(new Date());
		//tradeOrder.setUserName(userName);
		//tradeOrder.setUserPhone(userPhone);
		// TODO setOrderNo 11
		tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_UNPAY);
		tradeOrder.setOrderDesc("等待支付");
		String orderNo = tradeOrderUtils.getTradeOrderNo(productTypeId);
		tradeOrder.setOrderNo(orderNo);
		tradeOrderRepository.save(tradeOrder);
		JsonObject retJo = new JsonObject();
		retJo.addProperty("orderNo", orderNo);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 获取用户订单列表
	 * 
	 * @return
	 */
	public Responses reqUserTraderOrders() {
		long userId = sessionManager.getUserIdFromSession();
		int pageNumber = this.getIntParam("pageNum");
		int pageSize = this.getIntParam("pageSize");

		if (pageNumber <= 0) {
			pageNumber = 1;
		}
		if (pageSize <= 0) {
			pageSize = 4;
		}
		PageRequest pageRequest = this.buildPageRequest(pageNumber, pageSize);
		Page<TradeOrder> pagedTradeOrderList = tradeOrderRepository.findByUserIdOrderByCreateTimeDesc(userId, pageRequest);
		List<TradeOrder> tradeOrderList = pagedTradeOrderList.getContent();
		List<OrderVo> orderVoList = new ArrayList<OrderVo>();
		JsonObject retJo = new JsonObject();
		if (CollectionUtils.isNotEmpty(tradeOrderList)) {
			for (TradeOrder tradeOrder : tradeOrderList) {
				OrderVo orderVo = new OrderVo();
				orderVo.orderNo = tradeOrder.getOrderNo();
				// TODO ?
				int orderStatus = TradeOrderConstants.TradeOrderStatus
						.getUserViewStatus(tradeOrder.getOrderStatus());
				if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_UNPAY){
					continue;  //如果未支付则不进入列表
				}
						
				if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_CHARGE_CLOSED) {
					orderVo.orderStatus=1;
					orderVo.orderStatusDesc = "交易关闭";
				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_START_PAY)
				{
					long diff=DateUtil.getDiffMinuteFromCurrent(tradeOrder.getUpdateTime());
					if(diff>10){
						orderVo.orderStatus=1;
						orderVo.orderStatusDesc = "交易关闭";
					}else {
						orderVo.orderStatus = 2;
						orderVo.orderStatusDesc = "状态获取中";
					}
				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_SUCCESS  ||
						orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_SEND_GOODS 	) {
					orderVo.orderStatus=3;
					orderVo.orderStatusDesc = "充值中";
 				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_SEND_GOODS_SUCCESS)
				{
					orderVo.orderStatus=4;
					orderVo.orderStatusDesc = "充值成功";
				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_SEND_GOODS_FAILED) {
					orderVo.orderStatus=5;
					orderVo.orderStatusDesc = "充值失败";
				}
				else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUNDING)
				{
					orderVo.orderStatus=6;
					orderVo.orderStatusDesc = "退款中";
				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUNDED)
				{
					orderVo.orderStatus=7;
					orderVo.orderStatusDesc = "退款成功";
				}else if(orderStatus== TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUND_FAILED)
				{
					orderVo.orderStatus=8;
					orderVo.orderStatusDesc = "退款失败";
				}else{
					orderVo.orderStatusDesc = "";
				}

				orderVo.productType = tradeOrder.getProductTypeNo();
				orderVo.productName = tradeOrder.getProductName();
				orderVo.productNo = tradeOrder.getProductNo();
				orderVo.payAmount = String.format("%.2f",tradeOrder.getPayAmount()*1.0/100); //单位元
				orderVo.unitPrice = String.format("%.2f",tradeOrder.getUnitPrice()*1.0); //单位分
				orderVo.quantity=String.format("%.1f",(tradeOrder.getQuantity()*1.0)/10.0);  //要除以10
				orderVo.createTime = tradeOrder.getCreateTime();
				String payChannel=tradeOrder.getPayChannel();
				if(StringUtil.isNotBlank(payChannel)) {
					if (payChannel.equals("wx")) {
						orderVo.payChannel = "微信支付";
					} else {
						orderVo.payChannel = "支付宝支付";
					}
				}else{
					orderVo.payChannel ="";
				}
				orderVoList.add(orderVo);
			}
		}
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		retJo.addProperty("total", pagedTradeOrderList.getTotalElements());
		retJo.add("list", gson.toJsonTree(orderVoList));
		retJo.addProperty("currentPageNum", pageNumber);
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	// 查询订单列表
	private static class OrderVo {
		String orderNo;
		int orderStatus;
		String orderStatusDesc;
		String productType;
		String productName;
		String productNo;
		String payAmount;
		String unitPrice;
		String quantity;
		Date createTime;
		String payChannel;
	}


	/**
	 * 查询租客充值订单列表
	 * 
	 * @return
	 */
	public Responses reqChargeRecordsList() {
		int pageNumber = this.getIntParam("pageNum");
		int pageSize = this.getIntParam("pageSize");
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 4;
		}

		String businessNo = this.getStringParam("agencyId");//商家编号
		String productTypeNo ="10000001";// this.getStringParam("productTypeNo");
		String productNo = this.getStringParam("meterId");// 商品序号 购电时就是电表序列号

		PageRequest pageRequest = this.buildPageRequest(pageNumber, pageSize);
		Page<TradeOrder> pagedRendTradeOrderList = tradeOrderRepository
				.findByBusinessNoAndProductTypeNoAndProductNo(businessNo, productTypeNo, productNo, pageRequest);
		List<TradeOrder> rendTradeOrderList = pagedRendTradeOrderList.getContent();

		List<OrderVo> orderVoList = new ArrayList<OrderVo>();
		JsonObject retJo = new JsonObject();
		if (CollectionUtils.isNotEmpty(rendTradeOrderList)) {
			for (TradeOrder tradeOrder : rendTradeOrderList) {
				OrderVo orderVo = new OrderVo();
				orderVo.orderNo = tradeOrder.getOrderNo();
				// TODO ?
				/*orderVo.orderStatus = TradeOrderConstants.TradeOrderStatus
						.getUserViewStatus(tradeOrder.getOrderStatus());*/
				orderVo.productName = tradeOrder.getProductName();
				//orderVo.payAmount = tradeOrder.getPayAmount();
				//orderVo.quantity = ((tradeOrder.getQuantity()*1.0)/10.0);
				orderVo.createTime = tradeOrder.getCreateTime();
				String payChannel=tradeOrder.getPayChannel();
				if(payChannel.equals("wx")){
					orderVo.payChannel="微信支付";
				}else{
					orderVo.payChannel="支付宝支付";
				}
				orderVoList.add(orderVo);
			}
		}
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		retJo.addProperty("total", pagedRendTradeOrderList.getTotalElements());
		retJo.add("list", gson.toJsonTree(orderVoList));
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}


	/**
	 * 获取支付渠道
	 *
	 * @return
	 */
	public Responses getPayChannels() {
		long userId = sessionManager.getUserIdFromSession();
		String platform = this.getPlatform();
		List<PayChannelVo> payChannelVoList = getchannellist(platform);
		JsonObject retJo = new JsonObject();
		retJo.add("list", GsonUtil.buildGson().toJsonTree(payChannelVoList));
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.setBody(retJo);
		return responses;
	}

	private List<PayChannelVo> getchannellist(String platform) {
		List<PayChannelVo> payChannelVoList = new ArrayList<>();
		if (Constants.platform.ANDROID.equals(platform) || Constants.platform.IOS.equals(platform)) {
			PayChannelVo alipayPayChannelVo = new PayChannelVo();
			alipayPayChannelVo.setType("alipayApp");
			alipayPayChannelVo.setDesc("");
			alipayPayChannelVo.setName("支付宝支付");
			alipayPayChannelVo.setScheme("wx8c50e3bf6eb30b60");
			payChannelVoList.add(alipayPayChannelVo);

			PayChannelVo wxPayChannelVo = new PayChannelVo();
			wxPayChannelVo.setName("微信支付");
			wxPayChannelVo.setType("wechatApp");
			wxPayChannelVo.setDesc("");
			wxPayChannelVo.setScheme("HuizhaofangAPP");
			//payChannelVoList.add(wxPayChannelVo);
		} else {
			PayChannelVo wxPayChannelVo = new PayChannelVo();
			wxPayChannelVo.setName("微信支付");
			wxPayChannelVo.setType("wechatPublic");
			wxPayChannelVo.setDesc("");
			//payChannelVoList.add(wxPayChannelVo);

			PayChannelVo alipayPayChannelVo = new PayChannelVo();
			alipayPayChannelVo.setType("alipayWap");
			alipayPayChannelVo.setDesc("");
			alipayPayChannelVo.setName("支付宝支付");
			payChannelVoList.add(alipayPayChannelVo);
		}
		return payChannelVoList;
	}

	// 构建PageRequest
	private PageRequest buildPageRequest(int pageNumber, int pagzSize) {
		Sort sort = new Sort(Direction.DESC, "createTime");
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 支付账单
	 * 
	 * @return
	 */
	public Responses meterCharge() {
		String orderNo = getStringParam("orderNo");
		String payChannel = getStringParam("payChannel");
		TradeOrder order = tradeOrderRepository.findByOrderNo(orderNo);

		if (order == null) {
			logger.info(String.format("订单%s不存在!", orderNo));
			throw new TradeOrderException("该订单不存在");
		}

		// 判断订单是否处于可支付的状态
		// 只有等待支付和支付失败才可以再次支付
		if (order.getOrderStatus() != TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_START_PAY
				&&order.getOrderStatus() != TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_UNPAY
				&& order.getOrderStatus() != TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_FAILED) {
			throw new TradeOrderException("订单不能重复支付");
		}

		String lockResource = TradeOrderConstants.getTradeOrderLockKey(orderNo);
		// 获取锁
		boolean lock = lockManager.lock(lockResource);
		if (!lock) {
			logger.info(String.format("获取订单%s锁失败!", orderNo));
			throw new TradeOrderException("一笔订单不能同时发起多次支付");
		}

		JsonObject retJo = new JsonObject();
		try {
			// 向第三方发起支付
			Map<String, String> params = new HashMap<String, String>();
			params.put("amount", order.getPayAmount() + "");
			params.put("currency", "cny");
			params.put("subject", order.getProductDesc() + "");
			params.put("body", order.getProductDesc() + "");
			params.put("order_no", orderNo);
			params.put("channel", payChannel);
			params.put("req_type", "1");
			params.put("notify_url", "http://www.huifenqi.com/c/order/api/payasyncallback/");

			JsonObject payResult = paymentService.pingppCharge(params);

			String thirdOrderNo = payResult.get("flow_no").getAsString();

			// 更新订单状态为发起支付
			order.setPayNo(thirdOrderNo);
			order.setUpdateTime(new Date());
			order.setPayChannel(payChannel);
			order.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_START_PAY);
			order.setOrderDesc("发起支付");
			tradeOrderRepository.save(order);


			retJo.addProperty("orderNo", orderNo);
			retJo.addProperty("orderStatus", order.getOrderStatus());
			retJo.add("charge", payResult.get("charge"));

		} catch (Exception e) {
			logger.error(String.format("订单%s发起支付失败:%s", orderNo, ExceptionUtils.getStackTrace(e)));
			throw new TradeOrderException(String.format("订单发起支付失败:%s", e.getMessage()));
		} finally {
			if (lock) {
				// 释放锁
				lockManager.unLock(lockResource);
			}
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}

	/**
	 * 查询订单支付状态
	 * @return
	 */
	public Responses reqMeterChargeStatus() {
		String orderNo = this.getStringParam("orderNo");
		TradeOrder tradeOrder = tradeOrderRepository.findByOrderNo(orderNo);
		if (tradeOrder == null) {
			logger.info(String.format("订单%s不存在!", orderNo));
			throw new TradeOrderException("该订单不存在");
		}
		
		JsonObject retJo = new JsonObject();
		retJo.addProperty("orderNo", orderNo);
		//如果订单状态处于发起支付的状态,去支付平台查询最新的结果
		if (tradeOrder.getOrderStatus() == TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_START_PAY) {
			Map<String, String> params = new HashMap<>();
			params.put("order_no", tradeOrder.getOrderNo());
			JsonObject reqJo = paymentService.reqOrderPayStatus(params);
			int status = reqJo.get("order_status").getAsInt();
			//在支付结果没有返回的情况下不做处理
			if (PaymentNewService.ORDER_PAY_STATUS_PAYING != status) {
				String lockResource = TradeOrderConstants.getTradeOrderLockKey(orderNo);
				// 获取锁
				boolean lock = lockManager.lock(lockResource);
				try {
					if (lock) {
						if (PaymentNewService.ORDER_PAY_STATUS_FAILED == status) {
							tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_FAILED);
							tradeOrder.setOrderDesc("支付失败");
							tradeOrder.setUpdateTime(new Date());
							tradeOrderRepository.save(tradeOrder);
						} else if(PaymentNewService.ORDER_PAY_STATUS_SUCCESS == status) {
							tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_SUCCESS);
							tradeOrder.setOrderDesc("支付成功");
							tradeOrder.setUpdateTime(new Date());
							tradeOrderRepository.save(tradeOrder);
							//状态变为支付成功时，进行提交给商家
							tradeServcie.processTrade(tradeOrder);
						}
					}
				} finally {
					if (lock) {
						// 释放锁
						lockManager.unLock(lockResource);
					}
				}
			}
			
		}
		retJo.addProperty("orderStatus", tradeOrder.getOrderStatus());
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody(retJo);
		return responses;
	}



	/**
	 * 电表充值的异步回调
	 * by arison 20170509
	 * @return
	 */
	public Responses prePayAsynCallBack() {
		String orderNo = getStringParam("order_no");
		int status = getIntParam("status");
		logger.debug(String.format(" in prePayAsynCallBack  asyn status :%s , orderNo : %s " ,status,orderNo));
		TradeOrder tradeOrder = tradeOrderRepository.findByOrderNo(orderNo);
		if (StringUtil.isBlank(orderNo) || (status!=2 && status!=3)) {
			logger.info(String.format("订单%s不存在!", orderNo));
			logger.info(String.format("订单状态%s错误!", status));
			throw new BaseException(ErrorMsgCode.ERRCODE_INVALID_PARAM,"非法参数");
		}
		Gson gson = new GsonBuilder().serializeNulls()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
		if (status == 2) {
			tradeOrder.setOrderDesc("发货成功");
			tradeOrder.setUpdateTime(new Date());
			tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_SEND_GOODS_SUCCESS);
			tradeOrderRepository.save(tradeOrder);
			//加入到结算队列
			redisClient.lefltPushQueue(TradeOrderConstants.TRADE_ORDER_SETTLE_QUEUE_METER, gson.toJson(tradeOrder));
		} else {
			logger.error(String.format("订单(%s)发货失败", tradeOrder.getOrderNo()));
			tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_SEND_GOODS_FAILED);
			tradeOrder.setOrderDesc("发货失败");
			tradeOrder.setUpdateTime(new Date());
			tradeOrderRepository.save(tradeOrder);
			//对失败订单进行退款处理
			redisClient.lefltPushQueue(TradeOrderConstants.TRADE_ORDER_REFUND_QUEUE_METER, gson.toJson(tradeOrder));
		}
		Responses responses = new Responses();
		responses.getMeta().setErrorCode(ErrorMsgCode.ERROR_MSG_OK);
		responses.getMeta().setErrorMessage("success");
		responses.setBody("");
		return responses;
	}

	/**
	 * 支付异步回调
	 * 
	 * @return
	 */
	public ApiResult payAsynCallBack() {
		String orderNo = getStringParam("order_no");
		int status = getIntParam("status");
		TradeOrder tradeOrder = tradeOrderRepository.findByOrderNo(orderNo);
		if (tradeOrder == null) {
			logger.info(String.format("订单%s不存在!", orderNo));
			return new ApiResult();
		}
		
		// 如果订单状态处于发起支付的状态,则进行处理，否则认为已经处理过了
		if (tradeOrder.getOrderStatus() == TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_START_PAY) {
			// 在支付结果没有返回的情况下不做处理
			String lockResource = TradeOrderConstants.getTradeOrderLockKey(orderNo);
			// 获取锁
			boolean lock = lockManager.lock(lockResource);
			if (!lock) {
				logger.info(String.format("获取订单%s锁失败!", orderNo));
				throw new TradeOrderException("一笔订单不能同时发起多次支付");
			}
			try {
				if (lock) {
					if (PaymentNewService.ORDER_PAY_STATUS_FAILED == status) {
						tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_FAILED);
						tradeOrder.setOrderDesc("支付失败");
						tradeOrder.setUpdateTime(new Date());
						tradeOrderRepository.save(tradeOrder);
					} else if(PaymentNewService.ORDER_PAY_STATUS_SUCCESS == status) {
						tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_PAY_SUCCESS);
						tradeOrder.setOrderDesc("支付成功");
						tradeOrder.setUpdateTime(new Date());
						tradeOrderRepository.save(tradeOrder);
						//状态变为支付成功时，进行提交给商家
						tradeServcie.processTrade(tradeOrder);
					}
				}
			} finally {
				if (lock) {
					// 释放锁
					lockManager.unLock(lockResource);
				}
			}
		}
		return new ApiResult();
	}

	/**
	 * 退款异步回调
	 * 
	 * @return
	 */
	public ApiResult refundAsynCallBack() {
		String orderNo = getStringParam("order_no");
		int status = getIntParam("status");
		TradeOrder tradeOrder = tradeOrderRepository.findByOrderNo(orderNo);
		if (tradeOrder == null) {
			logger.info(String.format("订单%s不存在!", orderNo));
			return new ApiResult();
		}
		
		// 如果订单状态处于发起支付的状态,则进行处理，否则认为已经处理过了
		if (tradeOrder.getOrderStatus() == TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUNDING) {
			// 在支付结果没有返回的情况下不做处理
			String lockResource = TradeOrderConstants.getTradeOrderLockKey(orderNo);
			// 获取锁
			boolean lock = lockManager.lock(lockResource);
			if (!lock) {
				logger.info(String.format("获取订单%s锁失败!", orderNo));
				throw new TradeOrderException("一笔订单不能同时发起多次支付");
			}
			try {
				if (lock) {
					if (PaymentNewService.ORDER_PAY_STATUS_FAILED == status) {
						tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUND_FAILED);
						tradeOrder.setOrderDesc("退款失败");
					} else if(PaymentNewService.ORDER_PAY_STATUS_SUCCESS == status) {
						tradeOrder.setOrderStatus(TradeOrderConstants.TradeOrderStatus.TRADE_ORDER_STATUS_REFUNDED);
						tradeOrder.setOrderDesc("退款成功");
					}
					tradeOrder.setUpdateTime(new Date());
					tradeOrderRepository.save(tradeOrder);
				}
			} finally {
				if (lock) {
					// 释放锁
					lockManager.unLock(lockResource);
				}
			}
		}
		return new ApiResult();
	}
}
