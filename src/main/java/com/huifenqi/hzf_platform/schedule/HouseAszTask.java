package com.huifenqi.hzf_platform.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.TaskConfiguration;
import com.huifenqi.hzf_platform.context.AszConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDtoAsz;
import com.huifenqi.hzf_platform.dao.HouseAszDao;
import com.huifenqi.hzf_platform.utils.AszHttpClientUtil;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.RSAUtil;
import com.huifenqi.hzf_platform.utils.RedisUtils;


@Service
public class HouseAszTask {

	@Resource
	private RedisCacheManager redisCacheManager;

	@Resource
	private HouseAszDao houseAszDao;

	@Resource
	private TaskConfiguration taskConfiguration;
	private static Logger logger = LoggerFactory.getLogger(HouseAszTask.class);

	@Scheduled(cron = "${hfq.asz.house.task}") // 每五分钟执行一次
	
	@Transactional
	public void getHouseDataAsz() throws Exception {	
		logger.info("定时任务获取爱上租中介公司房源数据开始");
		 if (!taskConfiguration.isScheduleStatus()) {
			 logger.info("本台机器定时任务未开启，不能执行获取爱上自房源定时任务");
			 return;
		 }
		//设置标志位
		boolean flag = false;
		
		// 获取上次获取数据时间
		String key = RedisUtils.getInstance().getKey(AszConstants.ConfigDetail.ASZ_TASK_LAST_TIME);
		String startTime = redisCacheManager.getValue(key);

		for (int i = 1; i > 0; i++) {
			// 封装请求数据
			String jsonStr = getRequestData(i, startTime);

			// 发送请求
			String reponseStr = AszHttpClientUtil.postJson(AszConstants.ConfigDetail.HTTP_ASZ_URL,jsonStr);

			// 封装返回结果
			JsonObject reJto = GsonUtils.getInstace().fromJson(reponseStr, JsonObject.class);
			String code = reJto.get("code").getAsString();
			if (!code.equals("200")) {
				throw new Exception(reJto.get("message").toString());
			}
			int total = reJto.get("total").getAsInt();
			int pageNo = reJto.get("page_no").getAsInt();
			int pageSize = reJto.get("page_size").getAsInt();
			if(reJto.getAsJsonArray("data") == null){
				logger.info("获取爱上租房源data数据未空，");
				return;
			}
			JsonArray aszJsonArray = reJto.getAsJsonArray("data");;

			// 房源入库
			if (total > 0) {
				//成功获取数据后，设置请求时间标记
				flag = true;
				
				for (int j = 0; j < aszJsonArray.size(); j++) {
					HousePublishDtoAsz houseDtoAsz = getHousePublishDtoAsz(aszJsonArray.get(j).getAsJsonObject());
					if(houseDtoAsz != null){
						houseAszDao.addBdHousePublishDtoQft(houseDtoAsz);
					}
				}
			}
			
			logger.info("爱上租发布/更新房源进行中   当前页码:{}, 每页条数：{}, 总条数：{}", pageNo, pageSize,total);
			if (pageNo * pageSize >= total) {
				break;
			}
		}
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		if(flag){
			redisCacheManager.putValue(key, dateFormater.format(date));
		}
		logger.info("定时任务获取爱上租中介公司房源数据结束");
	}

	/**
	 * 封装请求参数
	 * 
	 * @return HousePublishDtoAsz
	 */
	public static HousePublishDtoAsz getHousePublishDtoAsz(JsonObject aszJsonObject) {
		HousePublishDtoAsz asz = new HousePublishDtoAsz();
		try{
			if(!aszJsonObject.get("apartment_id").isJsonNull()){
				asz.setApartmentId(aszJsonObject.get("apartment_id").getAsString());
			}
			if(!aszJsonObject.get("apartment_code").isJsonNull()){
				asz.setApartmentCode(aszJsonObject.get("apartment_code").getAsString());
			}
			if(!aszJsonObject.get("house_id").isJsonNull()){
				asz.setHouseId(aszJsonObject.get("house_id").getAsString());
			}
			
			if(!aszJsonObject.get("rent_type").isJsonNull()){
				asz.setRentType(aszJsonObject.get("rent_type").getAsString());
			}
			if(!aszJsonObject.get("room_no").isJsonNull()){
				asz.setRoomNo(aszJsonObject.get("room_no").getAsString());
			}
			if(!aszJsonObject.get("rent_status").isJsonNull()){
				asz.setRentStatus(aszJsonObject.get("rent_status").getAsString());
			}
			if(!aszJsonObject.get("rent_price").isJsonNull()){
				asz.setRentPrice(aszJsonObject.get("rent_price").getAsString());
			}
			if(!aszJsonObject.get("customer_person").isJsonNull()){
				asz.setCustomerPerson(aszJsonObject.getAsJsonArray("customer_person").toString());
			}
			
			if(!aszJsonObject.get("residential_name").isJsonNull()){
				asz.setResidentialName(aszJsonObject.get("residential_name").getAsString());
			}
			if(!aszJsonObject.get("city_code").isJsonNull()){
				asz.setCityCode(aszJsonObject.get("city_code").getAsString());
			}
			if(!aszJsonObject.get("city_name").isJsonNull()){
				asz.setCityName(aszJsonObject.get("city_name").getAsString());
			}
			
			if(!aszJsonObject.get("area_code").isJsonNull()){
				asz.setAreaCode(aszJsonObject.get("area_code").getAsString());
			}
			if(!aszJsonObject.get("area_name").isJsonNull()){
				asz.setAreaName(aszJsonObject.get("area_name").getAsString());
			}
			if(!aszJsonObject.get("address").isJsonNull()){
				asz.setAddress(aszJsonObject.get("address").getAsString());
			}
			if(!aszJsonObject.get("business_circle_multi").isJsonNull()){
				asz.setBusinessCircleMulti(aszJsonObject.get("business_circle_multi").getAsString());
			}
			if(!aszJsonObject.get("lng").isJsonNull()){
				asz.setLng(aszJsonObject.get("lng").getAsString());
			}
			if(!aszJsonObject.get("lat").isJsonNull()){
				asz.setLat(aszJsonObject.get("lat").getAsString());
			}
			if(!aszJsonObject.get("property_use").isJsonNull()){
				asz.setPropertyUse(aszJsonObject.get("property_use").getAsString());
			}
			if(!aszJsonObject.get("floor").isJsonNull()){
				asz.setFloor(Integer.valueOf(aszJsonObject.get("floor").getAsString().replaceAll(" ", "")));
			}
			if(!aszJsonObject.get("ground_floors").isJsonNull()){
				asz.setGroudFloors(aszJsonObject.get("ground_floors").getAsInt());
			}
			if(!aszJsonObject.get("rooms").isJsonNull()){
				asz.setRooms(aszJsonObject.get("rooms").getAsInt());
			}
			if(!aszJsonObject.get("livings").isJsonNull()){
				asz.setLivings(aszJsonObject.get("livings").getAsInt());
			}
			if(!aszJsonObject.get("bathrooms").isJsonNull()){
				asz.setBathRooms(aszJsonObject.get("bathrooms").getAsInt());
			}
			if(!aszJsonObject.get("build_area").isJsonNull()){
				asz.setBuildArea(aszJsonObject.get("build_area").getAsFloat());
			}
			if(!aszJsonObject.get("total_area").isJsonNull()){
				asz.setTotalArea(aszJsonObject.get("total_area").getAsFloat());
			}
			if(!aszJsonObject.get("orientation").isJsonNull()){
				asz.setOrientation(aszJsonObject.get("orientation").getAsString());
			}
			if(!aszJsonObject.get("fitment_type").isJsonNull()){
				asz.setFitmentType(aszJsonObject.get("fitment_type").getAsString());
			}
			if(!aszJsonObject.get("house_room_feature").isJsonNull()){
				asz.setHouseRoomFeature(aszJsonObject.get("house_room_feature").getAsString());
			}
			if(!aszJsonObject.get("house_configu_ration").isJsonNull()){
				asz.setHouseConfiuTation(aszJsonObject.get("house_configu_ration").getAsString());
			}
			if(!aszJsonObject.get("remark").isJsonNull()){
//				StringBuilder stringBuilder= new StringBuilder();
//				stringBuilder.append(aszJsonObject.get("remark").getAsString());
//				stringBuilder.append("-测试");
//				asz.setRemark(stringBuilder.toString());	
				asz.setRemark(aszJsonObject.get("remark").getAsString());
			}
			if(!aszJsonObject.get("img_list").isJsonNull()){
				asz.setImgList(aszJsonObject.getAsJsonObject().getAsJsonArray("img_list").toString());
			}
			if(!aszJsonObject.get("agent_uname").isJsonNull()){
				asz.setAgentUname(aszJsonObject.get("agent_uname").getAsString());
			}
			if(!aszJsonObject.get("agent_uphone").isJsonNull()){
				asz.setAgentUphone(aszJsonObject.get("agent_uphone").getAsString());
			}
			if(!aszJsonObject.get("agent_post").isJsonNull()){
				asz.setAgentPost(aszJsonObject.get("agent_post").getAsString());
			}
			if(!aszJsonObject.get("agent_department").isJsonNull()){
				asz.setAgentDepartment(aszJsonObject.get("agent_department").getAsString());
			}
			
			//日期格式化
	//		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		try {
	//			asz.setCreateTime(sdf.parse(aszJsonObject.get("create_time").getAsString()));
	//			asz.setUpdateTime(sdf.parse(aszJsonObject.get("update_time").getAsString()));
	//		} catch (ParseException e) {
	//			e.printStackTrace();
	//		}
			
			asz.setCreateTime(new Date());
			if(!aszJsonObject.get("online_status").isJsonNull()){
				asz.setOnlineStatus(aszJsonObject.get("online_status").getAsString());
			}
			
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("爱上租房源JSON转换失败:" +aszJsonObject+ e.getMessage()));
			logger.error(LogUtils.getCommLog("爱上租房源JSON转换失败,apartment_id是:" +aszJsonObject.get("apartment_id").getAsString()+ e.getMessage()));
			return null;
			//throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "爱上租房源JSON转换失败");
		}
		return asz;
	}

	/**
	 * 获取房源dto
	 * 
	 * @return Map<String, Object>
	 */
	public static String getRequestData(int pageNo, String startTime) {
		// 参数设置
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client_id", AszConstants.ConfigDetail.HTTP_CLIENT_ID_TEST);
		params.put("version", "1.0.0");
		params.put("page_no", String.valueOf(pageNo));
		params.put("page_size", String.valueOf(AszConstants.ConfigDetail.REQUEST_PAGE_SIZE));
		params.put("online_status", null);
		//params.put("apartment_id","8AB398CA584E250D015860CD7FC44FAD");
		params.put("start_time", startTime);
		// 参数加密
		String data = JSON.toJSONString(params);
		String datajson = RSAUtil.encrypt(data, AszConstants.ConfigDetail.SIGN_RSA_PRIVATE_TEST);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("client_id", AszConstants.ConfigDetail.HTTP_CLIENT_ID_TEST);
		map.put("data", datajson);
		String jsonStr = JSON.toJSONString(map);
		return jsonStr;
	}

}
