/** 
 * Project Name: hzf_platform 
 * File Name: HouseUtil.java 
 * Package Name: com.huifenqi.hzf_platform.utils 
 * Date: 2016年4月27日下午12:13:52 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.huifenqi.hzf_platform.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.Constants;
import com.huifenqi.hzf_platform.context.ThridSysConstants;
import com.huifenqi.hzf_platform.context.dto.request.house.SaasApartmentInfoDto;
import com.huifenqi.hzf_platform.context.entity.house.HouseDetail;
import com.huifenqi.hzf_platform.context.entity.house.HousePicture;
import com.huifenqi.hzf_platform.context.entity.house.HouseSetting;
import com.huifenqi.hzf_platform.context.entity.house.RoomBase;
import com.huifenqi.hzf_platform.context.entity.third.SaasApartmentInfo;
import com.huifenqi.hzf_platform.context.enums.ImgTypeEnums;
import com.huifenqi.hzf_platform.context.enums.ali.AliSettingsEnum;
import com.huifenqi.hzf_platform.context.enums.ali.FaceEnum;
import com.huifenqi.hzf_platform.context.enums.bd.SettingsEnum;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author changmingwei
 * @version
 * @since JDK 1.8
 */
public class ThirdSysUtil {

	private static final Log logger = LogFactory.getLog(ThirdSysUtil.class);
	//private static final String SAVE_PATH = "e:/tmp/house_image/";
	private static final String SAVE_PATH = "/data/www/hzf-platform-project/tmp/house_image/";

	/**
	 * 获取百度sass图片上传url
	 * 
	 * @return String url
	 */
	public static String getUrl(String imgUrl,String appId,String ak){
		if(imgUrl == null){
			return null;
		}
		if(appId == null){
			return null;
		}
		if(ak == null){
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(imgUrl);
		stringBuilder.append("?appId=");
		stringBuilder.append(appId);
		stringBuilder.append("&");
		stringBuilder.append("ak=");
		stringBuilder.append(ak);
		return stringBuilder.toString();	
	}

	/**
	 * 获取百度sass图片上传接口参数
	 * 
	 * @return String url
	 */
	public static Map<String, Object>  getImgParam(String picPath){
		if(picPath == null){
			return null;
		}
		File file = new File(picPath);
		Map<String, Object> params = new HashMap<>();
		params.put("image", file);
		return params;
	}
	
	/**
	 * 获取百度sass公寓信息上传参数
	 * 
	 * @return String url
	 */
	public static Map<String, String>  getApartmentParam(SaasApartmentInfo saasApartmentInfo,String backUrl){
		Map<String, String> params = new HashMap<>();
		SaasApartmentInfoDto dto = new SaasApartmentInfoDto();
		
		dto.setApart_id(saasApartmentInfo.getApartId());//saas的系统对公寓的id 
		dto.setCompany_name(saasApartmentInfo.getCompanyName());//公司全称    
		dto.setApart_name(saasApartmentInfo.getApartName());//公寓名称
		dto.setApart_intro(saasApartmentInfo.getApartIntro());//公寓介绍  
		dto.setApart_service(saasApartmentInfo.getApartService());//公寓服务  N
		dto.setCorporate_name(saasApartmentInfo.getCorporateName());//法人姓名
		dto.setId_card_hiphoto_key(saasApartmentInfo.getIdCardHiphotoKey());//法人身份证照片的key  N

		List<String> listPics = new ArrayList<>();
		listPics.add(saasApartmentInfo.getTempletRoomPicKey());
		dto.setTemplet_room_pic_key(listPics);//样板间的房源照片
		
		dto.setContact_name(saasApartmentInfo.getContactName());//联系人姓名
		dto.setContact_phone(saasApartmentInfo.getContactPhone());//联系人电话     
		dto.setLicence_number(saasApartmentInfo.getLicenceNumber());//营业执照号码
		dto.setLicence_hiphoto_key(saasApartmentInfo.getLicenceHiphotoKey());//营业执照照片key 
		dto.setOrganization_code(saasApartmentInfo.getOrganizationCode());//组织机构代码
		dto.setTax_code(saasApartmentInfo.getTaxCode());//税务登记代码 
		dto.setRegistered_address(saasApartmentInfo.getRegisteredAddress());// 企业注册地址 
		dto.setCachet_hiphoto_key(saasApartmentInfo.getCachetHiphotoKey());//公章照片key  N
		dto.setCovered_city(saasApartmentInfo.getCoveredCity());//公寓覆盖的城市
		dto.setOperate_duration_in_saas(saasApartmentInfo.getOperateDurationInSaas());//公寓在saas运营时间 
		dto.setRoom_total_count(saasApartmentInfo.getRoomTotalCount());//公寓房源总数量
		dto.setRoom_free_count(saasApartmentInfo.getRoomFreeCount());//公寓空闲房源总数量
		dto.setSaas_recommend_reason(saasApartmentInfo.getSaasRecommendReason());//公寓推荐理由  N
		dto.setOrder_notify_url(backUrl);//通知公寓预约信息的接口  N
		String requestData = GsonUtils.getInstace().toJson(dto);
		params.put("data", requestData);
		return params;
	}
	
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final byte[] IV = new byte[16];

    public static String encrypt(String content, String secretKey) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }
    
    public static String decrypt(String encrypted, String secretKey) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encrypted);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }
    
    
	/**
	 * 获取房源图片封装成数组
	 * 
	 * @return List<Object>
	 */
	public static List<Object>  getListPics(List<HousePicture> hpList){
		
		List<Object> listPics = new ArrayList<>();
		for(HousePicture hp : hpList){
			JsonObject json = new JsonObject();
			if(hp.getIsDefault() == 1){
				json.addProperty("detailNum", "1");
			}else{
				json.addProperty("detailNum", "2");
			}
			json.addProperty("picDesc", "");
			json.addProperty("picUrl", hp.getPicRootPath());
			listPics.add(json);
		}
		return listPics;
	}
	
	/**
	 * 获取房源配置
	 * 
	 * @return List<Object>
	 */
	public static String  getListSettings(List<HouseSetting> hsList){
		StringBuffer  stringBuffer = new StringBuffer();
			for(HouseSetting hs : hsList){			
				String bdCode = SettingsEnum.getBdSettingKey(hs.getCategoryType(),hs.getSettingCode());
				if(bdCode != null  && !bdCode.equals("")){
					stringBuffer.append(bdCode);
					stringBuffer.append(ThridSysConstants.ThirdSysRecordUtil.IS_EMPTY_SPLIT);
				}
			}
			return stringBuffer.toString().substring(0,stringBuffer.length()-1);
	}
	
	/**
     * 获取闲鱼房源配置
     * 
     * @return List<Long>
     */
    public static List<Long>  getAliListSettings(List<HouseSetting> hsList){
        List<Long>  roomConfig = new ArrayList<>();
        for(HouseSetting hs : hsList){          
            Long aliCode = AliSettingsEnum.getAliSettingKey(hs.getCategoryType(),hs.getSettingCode());
            if(aliCode != null  && !aliCode.equals("")){
                roomConfig.add(aliCode);
            }
        }
        return roomConfig;
    }
    
//	public static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
//	    // 第一步：检查参数是否已经排序
//	    String[] keys = params.keySet().toArray(new String[0]);
//	    Arrays.sort(keys);
//	 
//	    // 第二步：把所有参数名和参数值串在一起
//	    StringBuilder query = new StringBuilder();
//	    if (ThridSysConstants.SignUtil.SIGN_METHOD_MD5.equals(signMethod)) {
//	        query.append(secret);
//	    }
//	    for (String key : keys) {
//	        String value = params.get(key);
//	        if (StringUtils.areNotEmpty(key, value)) {
//	            query.append(key).append(value);
//	        }
//	    }
//	 
//	    // 第三步：使用MD5/HMAC加密
//	    byte[] bytes;
//	    if (ThridSysConstants.SignUtil.SIGN_METHOD_HMAC.equals(signMethod)) {
//	        bytes = encryptHMAC(query.toString(), secret);
//	    } else {
//	        query.append(secret);
//	        bytes = encryptMD5(query.toString());
//	    }
//	 
//	    // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
//	    //return byte2hex(bytes);
//	}
//	 
//	public static byte[] encryptHMAC(String data, String secret) throws IOException {
//	    byte[] bytes = null;
//	    try {
//	        SecretKey secretKey = new SecretKeySpec(secret.getBytes(ThridSysConstants.SignUtil.CHARSET_UTF8), "HmacMD5");
//	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
//	        mac.init(secretKey);
//	        bytes = mac.doFinal(data.getBytes(ThridSysConstants.SignUtil.CHARSET_UTF8));
//	    } catch (GeneralSecurityException gse) {
//	        throw new IOException(gse.toString());
//	    }
//	    return bytes;
//	}
//	
//	public static byte[] encryptMD5(String data) throws IOException {
//	    return encryptMD5(data.getBytes(ThridSysConstants.SignUtil.CHARSET_UTF8));
//	}
//	 
//	public static String byte2hex(byte[] bytes) {
//	    StringBuilder sign = new StringBuilder();
//	    for (int i = 0; i < bytes.length; i++) {
//	        String hex = Integer.toHexString(bytes[i] & 0xFF);
//	        if (hex.length() == 1) {
//	            sign.append("0");
//	        }
//	        sign.append(hex.toUpperCase());
//	    }
//	    return sign.toString();
//	}
	/**
     * 下载文件
     * 
     * @param url
     *            下载的URL
     * @param savePath
     *            保存路径
     * @param isCompress
     *            是否压缩
     * @return 文件保存的路径
     */
    public static File downImageNew(String url) throws Exception {
        InputStream instream = null;
            try {
                //根据地址获得数据的字节流 
                instream = HttpImageByteUtil.getImageFromNetByUrl(url);
                
                //随机生成文件
                String imgName = createFileName(FileUtils.getFileFexName(url));
                
                //返回本地文件
                File localImg =saveAndUploadImage(instream, 800,600, imgName);
                
                return localImg;
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("图片下载失败! url=" + url + " " + e));
            throw new Exception("图片下载失败!");
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    /**
     * 下载文件
     * 
     * @param url
     *            下载的URL
     * @param savePath
     *            保存路径
     * @param isCompress
     *            是否压缩
     * @return 文件保存的路径
     */
    public static File downImage(String url) throws Exception {
        // 调置http请求连接保持时间，防止大图片下载不成功。
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).setStaleConnectionCheckEnabled(true).build();

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

        try {
            RequestBuilder get = RequestBuilder.get();
            get.setUri(url);
            logger.info("图片地址：" + url);
            CloseableHttpResponse response = httpclient.execute(get.build());
            InputStream instream = null;
            try {
                HttpEntity entity = response.getEntity();
                logger.info(LogUtils.getCommLog(entity.toString()));
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != Constants.Common.HTTP_STATUS_CODE_OK) {
                    String result = EntityUtils.toString(entity);
                    logger.info("http状态码：" + statusCode);
                    logger.info(LogUtils.getCommLog(String.format("The result of downloading file is %s", result)));
                    throw new Exception(String.format("下载图片http请求出错%s", result));
                }
                instream = entity.getContent();

                String imgName = createFileName(FileUtils.getFileFexName(url));
                File localImg =saveAndUploadImage(instream, 800,600, imgName);
                return localImg;
            } finally {
                if (instream != null) {
                    instream.close();
                }
                response.close();
            }
        } catch (Exception e) {
            logger.error(LogUtils.getCommLog("图片下载失败! url=" + url + " " + e));
            throw new Exception("图片下载失败!");
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error(LogUtils.getCommLog(e.toString()));
            }
        }
    }
    /**
     * 生产文件名 时间+8位的随机串
     * 
     * @param suffix
     *            后缀名
     * @return
     */
    public static String createFileName(String suffix) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(System.currentTimeMillis());
        fileName.append(MsgUtils.generateNoncestr(8));
        fileName.append(ImgTypeEnums.DEFAULT.getCode());
        fileName.append(".").append(suffix);
        return fileName.toString();
    }
    
    /**
     * 压缩文件
     *
     * @param cWidth
     *            压缩宽度
     * @param cHeight
     *            压缩高度
     * @return
     */
    public static File saveAndUploadImage(InputStream inputStream, int cWidth, int cHeight, String imgName) throws Exception {
        try {
            //将输入流转换图片字符流
            BufferedImage oldImage = ImageIO.read(inputStream);
            int width = oldImage.getWidth();//图片宽
            int height = oldImage.getHeight();//图片高
            
            //创建文件/tmp/house_image/
            File logcalImg = FileUtils.createFile(new File(SAVE_PATH + imgName));
            
            //图片存储本地
            Thumbnails.of(oldImage).size(width, height).toFile(logcalImg);
//            if(height > width){
//                logger.info("图片为竖向图片，需要进行裁剪");
//                String erect = "_("+height+","+width+")";
//                imgName = imgName.replace(ImgTypeEnums.DEFAULT.getCode(), erect);       
//            }
            
            //删除本地图片
            //logcalImg.delete();
            
            return logcalImg;
        } catch (IOException e) {
            logger.error("保存图片出错");
            logger.error(LogUtils.getCommLog(e.getMessage()));
            throw e;
        }

    }
    
    /**
     * 获取房源描述
     * 
     * @param houseDetail
     * @return
     */
    public static String getHouseDesc(HouseDetail houseDetail,RoomBase roomBase) {
        StringBuilder stringBuilder = new StringBuilder();

        if (houseDetail == null) {
            return StringUtil.EMPTY;
        }
        //位置
        String address = "该房源位于"+houseDetail.getCity()+houseDetail.getDistrict()+houseDetail.getBizName()+houseDetail.getCommunityName();
        stringBuilder.append(address);
        stringBuilder.append(StringUtil.COMMA);
        
        //地铁信息
        if(houseDetail.getSubway() != null){
            stringBuilder.append(houseDetail.getSubway());
            stringBuilder.append(StringUtil.DOT3);
        }else{
            stringBuilder.append(StringUtil.DOT3);
        } 
        
        //基础信息
        stringBuilder.append("房屋");
        //朝向
        if(houseDetail.getOrientations() > 0 && houseDetail.getOrientations() != 11111){
            String orientationsStr = getOrientationsStr(houseDetail.getOrientations()); 
            stringBuilder.append(orientationsStr);
            stringBuilder.append(StringUtil.COMMA);
        }
        
        //面积
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){//分租
           if(roomBase.getArea()>0){
               stringBuilder.append("面积为"+roomBase.getArea()+"平方米");
           }
        }else{
            if(houseDetail.getArea()>0){
                stringBuilder.append("面积为"+houseDetail.getArea()+"平方米");
            }
        }
        
        //独卫 独立阳台
        if(houseDetail.getEntireRent() == Constants.HouseDetail.RENT_TYPE_SHARE){
            stringBuilder.append(StringUtil.COMMA);
            if(roomBase.getBalcony() >0){
                stringBuilder.append("带独立卫生间");
            }
            if(roomBase.getInsurance()>0){
                stringBuilder.append("带独立阳台");
            }
        }

        //月付
        if(houseDetail.getIsPayMonth() >0){
            stringBuilder.append(StringUtil.COMMA);
            stringBuilder.append("房租支持月付");
        }
        //最后拼接
        stringBuilder.append(StringUtil.DOT3);
        stringBuilder.append("好房不等人,赶紧来看房。房源编号："+houseDetail.getSellId());
        return stringBuilder.toString();
        
    }
    
    
    /**
     * 获取门牌号
     * 
     * @param flowNo
     * @return
     */
    public static String getHouseNo(long flowNo) {
        StringBuilder stringBuilder = new StringBuilder();
        int x = (int)Math.round(Math.random()*(6-1)+1); 
        int y = (int)Math.round(Math.random()*(6-1)+1);
        int z = (int)Math.round(Math.random()*(6-1)+1);
        
        //门牌号
        stringBuilder.append(x);
        stringBuilder.append(StringUtil.QFT_HYPHEN);
        stringBuilder.append(y);
        stringBuilder.append(StringUtil.QFT_HYPHEN);
        stringBuilder.append(flowNo);
        stringBuilder.append(z);
        
        return stringBuilder.toString();
        
    }
    
    /**
     * 获取朝向字符串
     * 
     * @param orientations
     * @return
     */
    public static String getOrientationsStr(int orientations) {
        String orientationsStr = FaceEnum.getTitleDesc(Long.valueOf(orientations));
        return orientationsStr;
    }
    
//    public static void main(String args[]){
//        try {
//           System.out.println(downImage("http://hzf-image.oss-cn-beijing.aliyuncs.com/hr_image/HF619512892060/1502358754311b64xMRCh_defalut.jpg"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
