import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.LogUtils;
import com.huifenqi.hzf_platform.utils.StringUtil;

public class HttpGaodeZone {
	
	//地球平均半径  
    private static final double EARTH_RADIUS = 6378137;
    
	/**
	 * 根据城市获取商圈信息 
	 * @param address
	 * @param city
	 * @throws Exception 
	 */
	public JsonObject getCoordinateByCity(String city) throws Exception {
		//http://restapi.amap.com/v3/config/district?key=9ab0896118026d7e3c348243cb33294f&keywords=重庆&subdistrict=3&showbiz=true&extensions=base

		Map<String, String> params = new LinkedHashMap<>();
		params.put("showbiz", "true");
		params.put("extensions", "base");	
		params.put("key", "9ab0896118026d7e3c348243cb33294f");
		params.put("subdistrict", "3");
		params.put("city", city);
		try {
			String url = "http://restapi.amap.com/v3/config/district";
			String result =  HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 1) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			JsonArray reJtoCountry = reJto.getAsJsonArray("districts");
			JsonArray reJtoProvince = reJtoCountry.get(0).getAsJsonObject().getAsJsonArray("districts");
			JsonArray reJtoCity = reJtoProvince.get(0).getAsJsonObject().getAsJsonArray("districts");
			JsonArray reJtoDistrict = reJtoCity.get(0).getAsJsonObject().getAsJsonArray("districts");
			for(int i = 0;i<reJtoDistrict.size();i++){
				JsonObject district = reJtoDistrict.get(i).getAsJsonObject();
				String districtName = district.getAsJsonPrimitive("name").getAsString();
				//1根据区名称
			}
			return reJto.getAsJsonObject("result");
		} catch (Exception e) {
			throw new Exception("根据城市获取商圈信息 ",e);
		}	
	}
	
	//把经纬度转为度（°）  
    private static double rad(double d){  
       return d * Math.PI / 180.0;  
    }  
      
    /**  
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米  
     * @param lng1  
     * @param lat1  
     * @param lng2  
     * @param lat2  
     * @return  
     */  
    public static double getDistance(double lng1, double lat1, double lng2, double lat2){  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lng1) - rad(lng2);  
       double s = 2 * Math.asin(  
            Math.sqrt(  
                Math.pow(Math.sin(a/2),2)   
                + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)  
            )  
        );  
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    } 
    
    
	public static void main(String args[]){		
//		HttpGaodeZone zone = new HttpGaodeZone();
//		try {
//			zone.getCoordinateByCity("北京");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.print("baidu沙河站台距离A出口"+getDistance(116.31997,40.110029,116.31310,40.104906));
		
		System.out.print("gaode沙河站台距离A出口"+getDistance(116.28873289,40.14829909,116.28833592,40.14814327));;
		
	}
	
	
}