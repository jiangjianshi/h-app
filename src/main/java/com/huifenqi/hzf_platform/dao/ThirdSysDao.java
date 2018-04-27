/** 
 * Project Name: hzf_platform 
 * File Name: HouseDao.java 
 * Package Name: com.huifenqi.hzf_platform.dao 
 * Date: 2016年4月26日下午2:20:01 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.dao;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.configuration.AlibabaSysHouseConfiguration;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.AliSysHouseDetailToLocalDto;
import com.huifenqi.hzf_platform.context.dto.request.house.BdSysHouseDetailToLocalDto;
import com.huifenqi.hzf_platform.context.entity.house.HouseBase;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.house.solr.HouseSolrResult;
import com.huifenqi.hzf_platform.context.entity.third.SaasApartmentInfo;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysCompany;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysFile;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysRecord;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUser;
import com.huifenqi.hzf_platform.context.entity.third.ThirdSysUserRecord;
import com.huifenqi.hzf_platform.context.exception.BaseException;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;
import com.huifenqi.hzf_platform.dao.repository.house.HouseBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseDetailRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HousePictureRepository;
import com.huifenqi.hzf_platform.dao.repository.house.HouseSettingRepository;
import com.huifenqi.hzf_platform.dao.repository.house.RoomBaseRepository;
import com.huifenqi.hzf_platform.dao.repository.third.SaasApartMnetInfoRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysCompanyRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysFileRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysUserRecordRepository;
import com.huifenqi.hzf_platform.dao.repository.third.ThirdSysUserRepository;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HouseUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;
import com.huifenqi.hzf_platform.utils.ThirdSysUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest.Extrainfo;
import com.taobao.api.request.AlibabaIdleHouseItemAddRequest.RentItemApiDto;
import com.taobao.api.request.AlibabaIdleHouseUserAddRequest;
import com.taobao.api.request.AlibabaIdleHouseUserAddRequest.RentUserApiDto;
import com.taobao.api.request.AlibabaIdleHouseUserDelRequest;
import com.taobao.api.response.AlibabaIdleHouseItemAddResponse;
import com.taobao.api.response.AlibabaIdleHouseUserAddResponse;
import com.taobao.api.response.AlibabaIdleHouseUserDelResponse;

/**
 * ClassName: HouseDao date: 2016年4月26日 下午2:20:01 Description:
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
@Repository
public class ThirdSysDao {

	private static final Log logger = LogFactory.getLog(ThirdSysDao.class);
	
	@Autowired
	private HouseBaseRepository houseBaseRepository;

	@Autowired
	private HouseDetailRepository houseDetailRepository;

	@Autowired
	private HouseSettingRepository houseSettingRepository;

	@Autowired
	private HousePictureRepository housePictureRepository;

	@Autowired
	private RoomBaseRepository roomBaseRepository;

	@Autowired
	private SaasApartMnetInfoRepository saasApartMnetInfoRepository;
	
	@Autowired
	private ThirdSysRecordRepository thirdSysRecordRepository;
	
	@Autowired
    private ThirdSysFileRepository thirdSysFileRepository;
	
    @Autowired
    private ThirdSysUserRepository thirdSysUserRepository;
    
    @Autowired
    private ThirdSysUserRecordRepository thirdSysUserRecordRepository;
    
    @Autowired
    private ThirdSysCompanyRepository thirdSysCompanyRepository; 
    
    @Autowired
    private static RedisCacheManager redisCacheManager;
    
    @Autowired
    private AlibabaSysHouseConfiguration alibabaSysHouseConfiguration;
	
	/**
	 * 查询需要上传到百度sass平台公寓信息
	 *
	 * @param apartId
	 * @return SaasApartmentInfo
	 */
	public SaasApartmentInfo findByApartId(String apartId) {
		SaasApartmentInfo saasApartmentInfo = null;
		try{
			saasApartmentInfo = saasApartMnetInfoRepository.findByApartId(apartId);	
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("查询需要上传到百度sass平台公寓信息失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "查询需要上传到百度sass平台公寓信息失败");
		}
		return saasApartmentInfo;	
	}
	
	/**
	 * 保存公寓信息
	 *
	 * @param saasApartmentInfo
	 * @return 
	 */
	public void updateSaasApartmentInfo(SaasApartmentInfo saasApartmentInfo) {
		try{
			saasApartMnetInfoRepository.save(saasApartmentInfo);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("保存公寓信息失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "保存公寓信息失败");
		}	
	}
	
	/**
	 * 获取推向第三方发布房源
	 * 
	 * @param sellId roomId
	 * @return BdHouseDetailToLocalDto
	 */
	public Map<String,String> getHouseSubmit(String sellId,int roomId,int rentType,String ak,String saasAppId){	
	    
		Map<String,String> params = new HashMap<>();
		BdSysHouseDetailToLocalDto dto = new BdSysHouseDetailToLocalDto();
		//获取房源基础信息
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		//获取房源详情rentStartDate
		HouseDetail houseDetail = houseDetailRepository.findBySellId(sellId);
			
		List<HouseSetting> hsList = null;
		if(rentType == Constants.HouseDetail.RENT_TYPE_SHARE){
			//获取分租配置
			 hsList = houseSettingRepository.findAllBySellIdAndRoomId(sellId,roomId);
		}else{
			//获取整租配置
			 hsList = houseSettingRepository.findAllBySellId(sellId);
		}
		
		
		//获取图片
		List<HousePicture> hpList = housePictureRepository.findAllBySellId(sellId);

		//获取房间
		RoomBase roomBase= roomBaseRepository.findBySellIdAndRoomId(sellId, roomId);

		
		if(houseBase == null){
			return null;
		}
		
		dto.setAppId(saasAppId);
		
		//房源ID 
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setOutHouseId(houseBase.getSellId()+roomBase.getId());//房源ID
		}else{//不区分主次
			dto.setOutHouseId(houseBase.getSellId());//房源ID
		}
		
		dto.setRentType(houseDetail.getEntireRent());//出租方式
		dto.setBedRoomNum(houseDetail.getBedroomNum());//房屋户型-室
		dto.setLivingRoomNum(houseDetail.getLivingroomNum());//房屋户型-厅
		dto.setToiletNum(houseDetail.getToiletNum());//房屋户型-卫
		dto.setRentRoomArea(houseDetail.getArea());//面积
		
		//出租类型，枚举值31:主卧 32:次卧 33:不区分主次
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setBedRoomType(roomBase.getRoomType());
		}else{//不区分主次
			dto.setBedRoomType(ThridSysConstants.BaiduUtil.ROOM_TYPE_BD);
		}
		
		//房间标签
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setFeatureTag(roomBase.getRoomTag());
		}else{
			dto.setFeatureTag(houseDetail.getHouseTag());
		}
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
			dto.setRoomName(roomBase.getRoomName());//房间名称  N
			dto.setRoomCode(roomBase.getRoomCode());//房间编码  N
		}
		
		//房间朝向
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setFaceToType(roomBase.getOrientations());//房间朝向
		}else{
			if(houseDetail.getOrientations() > 0){
				dto.setFaceToType(houseDetail.getOrientations());//房间朝向
			}else{
				dto.setFaceToType(ThridSysConstants.BaiduUtil.FACE_TO_TYPE_NO);
			}
		}
		dto.setTotalFloor(Integer.parseInt(houseDetail.getFlowTotal()));//楼层总数
		dto.setHouseFloor(Integer.parseInt(houseDetail.getFlowNo()));//所在楼层数

		if(!hsList.isEmpty()){
			String detailPoint = ThirdSysUtil.getListSettings(hsList);
			dto.setDetailPoint(detailPoint);//房间配置	
		}else{
			dto.setDetailPoint("");//房间配置	
		}
		
		//dto.setServicePoint();//配套服务 N
		dto.setMonthRent(houseBase.getMonthRent()/100);//月租金
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		dto.setRentStartDate(sdf.format(houseBase.getCheckinTime()));//可入住时间
		
		dto.setShortRent(ThridSysConstants.BaiduUtil.SHORT_RENT_NO);//是否支持短租 统一为不支持
		dto.setProvice(houseDetail.getProvince());//省份
		dto.setCityName(houseDetail.getCity());//城市
		dto.setCountyName(houseDetail.getDistrict());//行政区
		dto.setAreaName(houseDetail.getBizName());//商圈 N
		dto.setDistrictName(houseDetail.getCommunityName());//小区名称
		dto.setStreet(houseDetail.getAddress());//想起地址
		dto.setAddress("");//门牌地址
		//dto.setSubwayLine();//地铁线路名 N
		//dto.setSubwayStation();//地铁站名称 N
		dto.setHouseDesc(houseBase.getComment());//房源描述
		dto.setxCoord(houseDetail.getBaiduLongitude());//经度
		dto.setyCoord(houseDetail.getBaiduLatitude());//纬度
		dto.setAgentPhone(houseBase.getAgencyPhone());//电话
		//dto.setOrderPhone();//接受短信手机号 N
		dto.setAgentName(houseBase.getAgencyName());//房管员姓名
		//dto.setVideoUrl();//房屋视频 N
		dto.setBuildYear(houseDetail.getBuildingYear());//建筑年代 N
		dto.setSupplyHeating(ThridSysConstants.BaiduUtil.SUPPLY_HEATING);//小区供暖方式，1集中供暖 2自供暖 3无供暖
		//dto.setGreenRatio();//绿化率 N
		//dto.setBuildType();//建筑类型 N
		
		String jsonString = JSON.toJSONString(dto);
		dto = JSON.parseObject(jsonString,BdSysHouseDetailToLocalDto.class);
		
		if(CollectionUtils.isNotEmpty(hpList)){
			List<Object> listPics = ThirdSysUtil.getListPics(hpList);
			dto.setPicUrlList(listPics);//图片数组
		}
		
		String requestData = GsonUtils.getInstace().toJson(dto);
		logger.info(LogUtils.getCommLog("发布房源请求参数为：" + requestData));
		params.put("data", requestData);
		params.put("ak", ak);
		
		return params;
	}
	
	/**
     * 获取添加闲鱼账户DTO
     * 
     * @param thirdSysUser
     * @return BdHouseDetailToLocalDto
     */
    public String getAliUserAddDto(ThirdSysUser thirdSysUser){ 
        //封装添加闲鱼请求对象
        AlibabaIdleHouseUserAddResponse rsp =  null;
        String url = alibabaSysHouseConfiguration.getUrl();
        String appkey =alibabaSysHouseConfiguration.getAliAppkey();
        String secret = alibabaSysHouseConfiguration.getAliAppSecret();
        String ownerUser = alibabaSysHouseConfiguration.getOwnerUser();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaIdleHouseUserAddRequest req = new AlibabaIdleHouseUserAddRequest();
        RentUserApiDto obj1 = new RentUserApiDto();
        List<String> userNicks = new ArrayList<String>();
        userNicks.add(thirdSysUser.getUserId());
        obj1.setUserNicks(userNicks);
        obj1.setOwnerUserNick("");
        req.setRentUserParam(obj1);
        try {
            logger.info(LogUtils.getCommLog("闲鱼api添加账号，请求信息" + req.getRentUserParam())); 
             rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error(LogUtils.getCommLog("闲鱼api添加账号失败" + e.getMessage())); 
            e.printStackTrace();
        }
        logger.info(LogUtils.getCommLog("闲鱼api添加账号，返回信息" + rsp.getBody())); 
       return rsp.getBody();
    }
    
    /**
     * 获取删除闲鱼账户DTO
     * 
     * @param thirdSysUser
     * @return BdHouseDetailToLocalDto
     */
    public String getAliUserDelDto(ThirdSysUser thirdSysUser){ 
        //封装删除闲鱼请求对象
        AlibabaIdleHouseUserDelResponse rsp =  null;
        String url = alibabaSysHouseConfiguration.getUrl();
        String appkey =alibabaSysHouseConfiguration.getAliAppkey();
        String secret = alibabaSysHouseConfiguration.getAliAppSecret();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaIdleHouseUserDelRequest req = new AlibabaIdleHouseUserDelRequest();
        req.setUserNick(thirdSysUser.getUserId());
        try {
            logger.info(LogUtils.getCommLog("闲鱼api删除账号，请求信息" + req.getUserNick())); 
             rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error(LogUtils.getCommLog("闲鱼api删除账号失败" + e.getMessage())); 
            e.printStackTrace();
        }
        logger.info(LogUtils.getCommLog("闲鱼api删除账号，请求信息" + rsp.getBody())); 
       return rsp.getBody();
    }
    
	/**
     * 获取闲鱼发布房源DTO
     * 
     * @param sellId roomId
     * @return BdHouseDetailToLocalDto
     */
    public AliSysHouseDetailToLocalDto getAliHouseSubmit(String sellId,int roomId,int rentType){ 
        //封装请求对象
        AliSysHouseDetailToLocalDto dto = new AliSysHouseDetailToLocalDto();
        
        //获取房源基础信息
        HouseBase houseBase = houseBaseRepository.findAllDeleteBySellId(sellId);
       
        if(houseBase == null){
            return null;
        }
        
        
        //获取房源详情
        HouseDetail houseDetail = houseDetailRepository.findAllDeleteBySellId(sellId);
        
        //获取配置信息
        List<HouseSetting> hsList = null;
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            //获取分租配置
             hsList = houseSettingRepository.findAllBySellIdAndRoomId(sellId,roomId);
        }else{
            //获取整租配置
             hsList = houseSettingRepository.findAllBySellId(sellId);
        }
        
        
        //获取图片
        List<HousePicture> hpList = housePictureRepository.findAllDeleteBySellId(sellId);

        //获取房间
        RoomBase roomBase= roomBaseRepository.findALLDeleteBySellIdAndRoomId(sellId, roomId);

        //周边配套 1餐饮方便 2公交 3学校 4公园广场 5超市 6健身会馆 7地铁房 8菜场 9医院 10便利店
        //List<Long> aroundConfig = new ArrayList<Long>(Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L));
        dto.setAroundConfig(null);//周边配置
        
        dto.setTitle(houseDetail.getBizName()+houseDetail.getCommunityName()+houseDetail.getBedroomNum()+"居");//+"测试,请不要拍"
        dto.setDesc(ThirdSysUtil.getHouseDesc(houseDetail,roomBase));//房屋描述
        //dto.setImageIds("");//图片
        //dto.setVideoCoverId();//视频首图
        //dto.setVideoId();//视频ID
        dto.setDetailAddress(houseDetail.getAddress());
        dto.setCommunityName(houseDetail.getCommunityName());//小区名称
        dto.setRentType(1L);//出租类型 默认长租
        dto.setRentMode((long)houseDetail.getEntireRent());//租赁模式 
        dto.setHouseType((long)houseDetail.getHouseType());//房源类型
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            dto.setRent((long)roomBase.getMonthRent());//房间租金
        }else{
            dto.setRent((long)houseBase.getMonthRent());//房屋租金
        }

        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            dto.setDeposit((long)roomBase.getDepositMonth());//房间押金
        }else{
            dto.setDeposit((long)houseBase.getDepositMonth());//房屋租金
        }
        
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            dto.setRoomArea((long)roomBase.getArea());//房间面面积
            dto.setTotalAreas((long)houseDetail.getArea());//房源总面积
            dto.setHouseClass(ThridSysConstants.ThirdSysRecordUtil.ALI_HOUSE_CLASS_PT);//房源分类 1- 别墅 2-普通住宅
        }else{
            dto.setRoomArea((long)houseDetail.getArea());//房源面积
        }
        
        dto.setBedRoomCnt((long)houseDetail.getBedroomNum());//房屋户型-室
        dto.setLivingRoomCnt((long)houseDetail.getLivingroomNum());//房屋户型-厅
        dto.setBathRoomCnt((long)houseDetail.getToiletNum());//房屋户型-卫
        
        dto.setDecorateLevel((long)houseDetail.getDecoration());//装修程度
        
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            dto.setHouseStatus((long)roomBase.getStatus());//房间房态
            if(roomBase.getIsDelete() != Constants.Common.STATE_IS_DELETE_NO){//房源删除设置已租
                dto.setHouseStatus(ThridSysConstants.ThirdSysRecordUtil.DELETE_HOUSE_STATUS); 
            }
        }else{
            dto.setHouseStatus((long)houseBase.getStatus());//房源房态
            if(houseBase.getIsDelete() != Constants.Common.STATE_IS_DELETE_NO ){//房间删除设置已租
                dto.setHouseStatus(ThridSysConstants.ThirdSysRecordUtil.DELETE_HOUSE_STATUS);  
            }
        }
        
        
        dto.setPayModePaid((long)houseBase.getPeriodMonth());//付几
        dto.setPayModePre((long)houseBase.getDepositMonth());//押几
        
        dto.setRoomConfig(null);//房间配置   
        List<Long> roomConfig = new ArrayList<Long>();
        if(!hsList.isEmpty()){
            roomConfig = ThirdSysUtil.getAliListSettings(hsList);
            if(!roomConfig.isEmpty()){
                dto.setRoomConfig(roomConfig);//房间配置    
            }
        }
        
        
        //房间朝向
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
            dto.setFace((long)roomBase.getOrientations());//房间朝向
        }else{
            if(houseDetail.getOrientations() > 0){
                dto.setFace((long)houseDetail.getOrientations());//房间朝向
            }else{
                dto.setFace((long)ThridSysConstants.BaiduUtil.FACE_TO_TYPE_NO);
            }
        }
        
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
            dto.setRoomClass((long)roomBase.getRoomType());//房间类型
        }
        
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
            if(roomBase.getBalcony() == 1){
                dto.setBathRoomType(ThridSysConstants.ThirdSysRecordUtil.ALI_BATH_TOOM_TYPE_YES);//是否独卫
            }else{
                dto.setBathRoomType(ThridSysConstants.ThirdSysRecordUtil.ALI_BATH_TOOM_TYPE_NO);//是否独卫
            }
        }
        
        dto.setBedRoomArea((long)houseDetail.getArea());
        dto.setSettlingTime(houseBase.getCheckinTime());//可入住时间

        if(houseDetail.getFlowTotal().equals("0") || houseDetail.getFlowTotal().isEmpty()){//设置随机值6-20
            //Math.round(Math.random()*(Max-Min)+Min) 
            dto.setTotalFloor(Math.round(Math.random()*(20-6)+6));//楼层总数
        }else{
            dto.setTotalFloor((long)Integer.parseInt(houseDetail.getFlowTotal()));//楼层总数 
        }
        
        if(houseDetail.getFlowNo().equals("0") ||houseDetail.getFlowNo().isEmpty()){
            dto.setFloor(Math.round(Math.random()*(dto.getTotalFloor()-1)+1));//随机数1-20 小于总楼层
        }else{
            dto.setFloor((long)Integer.parseInt(houseDetail.getFlowNo()));//所在楼层数
        }
        
        if(houseDetail.getBuildingNo().isEmpty()){//随机生成 X-Y-Z门牌号   
          dto.setBuildingAddr(ThirdSysUtil.getHouseNo(dto.getFloor()));//门牌号
        }else{
            dto.setBuildingAddr(houseDetail.getBuildingNo());
        }


        dto.setCompanyId(houseBase.getCompanyId());//公司ID
        dto.setSource(houseDetail.getSource());//渠道
        List<String> imgHzfList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(hpList)){
            for(HousePicture pic :hpList){
                if(imgHzfList.size() == ThridSysConstants.ThirdSysFileUtil.ThIRd_SYS_FILE_MAX){//闲鱼图片最大限制
                    break;
                }
                if(!pic.getPicRootPath().isEmpty()){
                    imgHzfList.add(pic.getPicRootPath());  
                }
            }
        }
        
        
        dto.setImageHzfList(imgHzfList);
        dto.setSellId(sellId);
        dto.setRoomId(roomId);
        dto.setLat(houseDetail.getBaiduLatitude());
        dto.setLng(houseDetail.getBaiduLongitude());
        return dto;
    }
    
    /**
     * 闲鱼更新房源
     * 
     * @param sellId roomId rentType
     * @return BdHouseDetailToLocalDto
     */
    public String getAliRoomSingleModify(String sellId,int roomId,int rentType){
        //封装更新房源请求对象
        return null;
    }
    
    /**
     * 闲鱼上下架房源
     * 
     * @param sellId roomId rentType status
     * @return BdHouseDetailToLocalDto
     */
    public Map<String,String> getAliRoomSingleModify(String sellId,int roomId,int rentType,String status){
        //1.获取需要更新房源对应的闲鱼账号
        //2.更新房源
        //3.校验房态（上架减少账号发布次数、下架增加账号发布次数）

        return null;
    }
    
	/**
	 * 获取推向第三方更新房源
	 * 
	 * @param sellId roomId
	 * @return BdHouseDetailToLocalDto
	 */
	public String getRoomSingleModify(String sellId,int roomId,int rentType,String ak,String saasAppId){
		BdSysHouseDetailToLocalDto dto = new BdSysHouseDetailToLocalDto();
		//获取房源基础信息
		HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
		//获取房源详情rentStartDate
		HouseDetail houseDetail = houseDetailRepository.findBySellId(sellId);
		
		List<HouseSetting> hsList = null;
		if(rentType == Constants.HouseDetail.RENT_TYPE_SHARE){
			//获取分租配置
			 hsList = houseSettingRepository.findAllBySellIdAndRoomId(sellId,roomId);
		}else{
			//获取整租配置
			 hsList = houseSettingRepository.findAllBySellId(sellId);
		}
		
		//获取房间
		RoomBase roomBase= roomBaseRepository.findBySellIdAndRoomId(sellId, roomId);	
		
		dto.setAk(ak);
		dto.setAppId(saasAppId);
		//房源ID 
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setOutHouseId(houseBase.getSellId()+roomBase.getId());//房源ID
		}else{//不区分主次
			dto.setOutHouseId(houseBase.getSellId());//房源ID
		}
		
		dto.setRentType(houseDetail.getEntireRent());//出租方式
		dto.setBedRoomNum(houseDetail.getBedroomNum());//房屋户型-室
		dto.setLivingRoomNum(houseDetail.getLivingroomNum());//房屋户型-厅
		dto.setToiletNum(houseDetail.getToiletNum());//房屋户型-卫
		dto.setRentRoomArea(houseDetail.getArea());//面积
		
		//出租类型，枚举值31:主卧 32:次卧 33:不区分主次
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setBedRoomType(roomBase.getRoomType());
		}else{//不区分主次
			dto.setBedRoomType(ThridSysConstants.BaiduUtil.ROOM_TYPE_BD);
		}
		
		//房间名称 房间编码
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
			dto.setRoomName(roomBase.getRoomName());//房间名称  N
			dto.setRoomCode(roomBase.getRoomCode());//房间编码  N
		}
		
		//房间朝向
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setFaceToType(roomBase.getOrientations());//房间朝向
		}else{
			if(houseDetail.getOrientations() > 0){
				dto.setFaceToType(houseDetail.getOrientations());//房间朝向
			}else{
				dto.setFaceToType(ThridSysConstants.BaiduUtil.FACE_TO_TYPE_NO);
			}
		}
		
		dto.setTotalFloor(Integer.parseInt(houseDetail.getFlowTotal()));//楼层总数
		dto.setHouseFloor(Integer.parseInt(houseDetail.getFlowNo()));//所在楼层数
	
		//房间标签
		if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
			dto.setFeatureTag(roomBase.getRoomTag());
		}else{
			dto.setFeatureTag(houseDetail.getHouseTag());
		}
		
		if(!hsList.isEmpty()){
			String detailPoint = ThirdSysUtil.getListSettings(hsList);
			dto.setDetailPoint(detailPoint);//房间配置	
		}else{
			dto.setDetailPoint("");//房间配置	
		}
		//dto.setServicePoint();//配套服务 N
		dto.setMonthRent(houseBase.getMonthRent()/100);//月租金
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		dto.setRentStartDate(sdf.format(houseBase.getCheckinTime()));//可入住时间
		
		dto.setShortRent(ThridSysConstants.BaiduUtil.SHORT_RENT_NO);//是否支持短租 统一为不支持
		dto.setAgentPhone(houseBase.getAgencyPhone());//电话
		//dto.setOrderPhone();//接受短信手机号 N
		dto.setAgentName(houseBase.getAgencyName());//房管员姓名
		dto.setBuildYear(houseDetail.getBuildingYear());//建筑年代 N
		dto.setSupplyHeating(ThridSysConstants.BaiduUtil.SUPPLY_HEATING);//小区供暖方式，1集中供暖 2自供暖 3无供暖
		//dto.setGreenRatio();//绿化率 N
		//dto.setBuildType();//建筑类型 N
		
		String jsonString = JSON.toJSONString(dto).replaceAll(",", "&").replaceAll(":", "=").replaceAll("\"", "");
		String rsmUrl = "?"+jsonString.substring(1, jsonString.length()-1)+"&";	
		logger.info(LogUtils.getCommLog("更新房源请求参数为：" + rsmUrl));

		return rsmUrl;
		//params.put("data", jsonString);
	}
	
	/**
	 * 获取上下架房源
	 * 
	 * @param sellId roomId
	 * @return BdHouseDetailToLocalDto
	 */
	public Map<String,String> getHouseModify(String sellId,int roomId,String status,String memo,String ak,String saasAppId){
		Map<String,String> params = new HashMap<>();
		JsonObject json = new JsonObject();
		String outHouseId = null;
		if(roomId > 0){//分租
			HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
			RoomBase roomBase= roomBaseRepository.findBySellIdAndRoomId(sellId, roomId);
			outHouseId = houseBase.getSellId()+roomBase.getId();
		}else{
			HouseBase houseBase = houseBaseRepository.findBySellId(sellId);
			outHouseId = houseBase.getSellId();
		}
		json.addProperty("appId", saasAppId);
		json.addProperty("houseId", outHouseId);//房源ID
		json.addProperty("memo", memo);//房源状态
		json.addProperty("status", status);//改动原因
		logger.info(LogUtils.getCommLog("房源上下架请求参数为：" + json.toString()));
		params.put("data", json.toString());
		params.put("ak",ak);
		return params;
	}
	
	
	/**
	 * 获取需要同步房源列表
	 *
	 * @param optStatus
	 * @return List<ThirdSysRecord> tsrList
	 */
	public List<ThirdSysRecord>  findByOptStatus(String optStatus) {
		List<ThirdSysRecord> tsrList = null;
		try{
			tsrList = thirdSysRecordRepository.findAllByOptStatus(optStatus);	
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("获取需要同步房源列表失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "获取需要同步房源列表失败");
		}
		return tsrList;	
	}

	/**
	 * 更新同步房源列表
	 *
	 * @param thirdSysRecord
	 * @return 
	 */
	public void updateThirdSysRecord(ThirdSysRecord thirdSysRecord) {
		try{
			thirdSysRecordRepository.save(thirdSysRecord);
		}catch (Exception e) {
			logger.error(LogUtils.getCommLog("更新同步房源列表失败" + e.getMessage()));
			throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "更新同步房源列表失败");
		}	
	}
	
	/**
     * 更新同步房源列表
     *
     * @param thirdSysRecord
     * @return 
     */
    public void updateThirdSysRecord(List<ThirdSysRecord> sysList) {
        try{
            thirdSysRecordRepository.save(sysList);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新同步房源列表失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "更新同步房源列表失败");
        }   
    }
	
	/**
     * 获取同步到闲鱼的文件
     *
     * @param userId imgUrl
     * @return 
     */
    public ThirdSysFile findAllByUserIdandImgUrl(String userId,String imgUrl) {
        ThirdSysFile thirdSysFile = null;
        try{
            thirdSysFile = thirdSysFileRepository.findAllByUserIdandImgUrl(userId, imgUrl);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新同步房源列表失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "更新同步房源列表失败");
        }
        return thirdSysFile;
    }

    /**
     * 添加文件信息
     *
     * @param thirdSysFile
     * @return 
     */
    public void addThirdSysFile(ThirdSysFile thirdSysFile) {
        try{
            thirdSysFileRepository.save(thirdSysFile);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新同步文件失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "更新同步文件失败");
        }
    }
    
    /**
     * 添加用户房源信息
     *
     * @param thirdSysUserRecord
     * @return 
     */
    public void addThirdSysUserRecord(ThirdSysUserRecord thirdSysUserRecord) {
        try{
            thirdSysUserRecordRepository.save(thirdSysUserRecord);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("更新同步用户房源失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "更新同步用户房源失败");
        }
    }
    
    /**
     * 获取用户同步房源明细
     *
     * @param sellId roomId
     * @return 
     */
    public ThirdSysUserRecord findThirdSysUserRecordUsedId(String sellId, int roomId) {
        try{
            return thirdSysUserRecordRepository.findUserId(sellId,roomId);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("获取用户同步房源明细失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "获取用户同步房源明细失败");
        }
    }
    
    /**
     * 获取用户同步房源记录
     *
     * @param sellId roomId
     * @return 
     */
    public ThirdSysRecord findBySellIdRoomId(String sellId, int roomId) {
        try{
            return thirdSysRecordRepository.findBySellIdRoomId(sellId,roomId);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("获取用户同步房源失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "获取用户同步房源失败");
        }
    }
    
    /**
     * 获取用户同步房源记录列表
     *
     * @param sellId roomId
     * @return 
     */
    public List<ThirdSysRecord> findBySellId(String sellId) {
        try{
            return thirdSysRecordRepository.findBySellId(sellId);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("获取用户同步房源失败" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "获取用户同步房源失败");
        }
    }
    /**
     * 获取用户同步房源记录
     *
     * @param sellId roomId
     * @return 
     */
    public ThirdSysCompany findBySourceAndCompanyId(String source,String companyId) {
        try{
            return thirdSysCompanyRepository.findBySourceAndCompanyId(source,companyId);
        }catch (Exception e) {
            logger.error(LogUtils.getCommLog("获取闲鱼限制公司失败哪个是" + e.getMessage()));
            throw new BaseException(ErrorMsgCode.ERROR_MSG_ADD_HOUSE_FAIL, "获取闲鱼限制公司失败");
        }
    }
    
}
