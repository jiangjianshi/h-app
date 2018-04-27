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
import com.huifenqi.hzf_platform.context.dto.request.house.BdHousePublishDtoQft;
import com.huifenqi.hzf_platform.context.dto.request.house.CoordinateDto;
import com.huifenqi.hzf_platform.utils.GsonUtils;
import com.huifenqi.hzf_platform.utils.HttpUtil;
import com.huifenqi.hzf_platform.utils.StringUtil;

public class HttpBdMapTest {

	public static void main(String args[]){		
		List<String> keywords = new ArrayList<>();
		keywords.add("地铁站");
		List<String> tags = new ArrayList<>();
		// tags.add("交通设施/地铁站");
		Map<String, String> sort = new HashMap<>();
		sort.put("industry_type", "life");
		sort.put("sort_name", "distance");
		sort.put("sort_rule", "1");
		 try {	
		 JsonArray result = radiusPlaceSearch(keywords, tags, "3000",
		 "40.059541", "116.315377", "1",
		 sort);
		
		 // 116.238409,40.218811 昌平1 {"lat":40.226351,"lng":116.240088}116.240088,40.226351
		 // 116.245667,40.213081昌平2  {"lat":40.226351,"lng":116.240088}
		 // 116.23115,40.21859 昌平3    116.239477,40.22659
		 
		 
		 //116.275316,40.146495  沙河1
		 //116.317896,40.158683 沙河2
		 
		 //116.314806,40.21248  南邵1
		 //116.327885,40.209504 南邵2
		 
		 //104.130788,30.676025 二仙桥
		 
		 //116.315377,40.059541 西二旗
		 System.out.println(result);
		 // 没有查到数据
		 if (result == null || result.size() == 0) {
		 return;
		 }
		 
		 JsonObject subwayDetail = result.get(0).getAsJsonObject();
		 int distance =
		 subwayDetail.getAsJsonObject("detail_info").getAsJsonPrimitive("distance").getAsInt();
		 // 用距离除70m/分钟
		 int time = distance / 45;
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
//		try {
//			BdHousePublishDtoQft qft = new BdHousePublishDtoQft();
//			qft.setCityName("");
//			qft.setCountyName("");
//			qft.setDistrictName("昌平区狮子坪");
//			// {"lng":120.04560488383497,"lat":30.290820074216904}
//			// {"lng":108.3015537765541,"lat":22.842329417918195}
//			CoordinateDto dto = getCoordinateByAddressQft(qft);
//			if (dto == null) {
//				System.out.println("返回结果是空");
//			} else {
//				System.out.println("返回结果是" + dto.getLat());
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// try {
		// getAddressByLocation("39.86283", "116.338656");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
	
	public static JsonArray radiusPlaceSearch(List<String> keywords, List<String> tags, String radius, String lat,
			String lng, String pageSize, Map<String, String> sort) throws Exception {
		try {
			
			//关键字
			if (keywords == null || keywords.isEmpty()) {
				throw new Exception("关键词不能为空.");
			}
			Map<String, String> params = new LinkedHashMap<>();
			StringBuilder keyword = new StringBuilder();
			for (String kw : keywords) {
				if (keyword.length() > 0) {
					keyword.append("$");
				}
				keyword.append(kw);
			}
			params.put("query", keyword.toString());
			//标签
			StringBuilder tag = new StringBuilder();
			if (tags != null && !tags.isEmpty()) {
				for (String tg : tags) {
					if (tag.length() > 0) {
						tag.append(",");
					}
					tag.append(tg);
				}
				params.put("tag", tag.toString());
			}
			
			//是否返回POI信息
			params.put("scope", "2");
			
			//每页的条数
			if (pageSize != null) {
				params.put("page_size", pageSize);
			}
			
			//过滤排序
			if (sort != null && !sort.isEmpty()) {
				StringBuilder filter = new StringBuilder();
				for (String sk : sort.keySet()) {
					if (filter.length() > 0) {
						filter.append("|");
					}
					filter.append(sk).append(":").append(sort.get(sk));
				}
				params.put("filter", filter.toString());
			}
			
			params.put("ak", "eima7RUwpM8D5XdjrnyTNfCLGndYDDYC");
			
			StringBuilder location = new StringBuilder();
			location.append(lat).append(",").append(lng);
			params.put("location", location.toString());
			if (!StringUtil.isBlank(radius)) {
				params.put("radius", radius);
			}
			params.put("output", "json");
			params.put("timestamp", String.valueOf(System.currentTimeMillis()));
			params.put("sn", createSignature("/place/v2/search", params));
			String url = String.format("%s%s", "http://api.map.baidu.com", "/place/v2/search");
			String result =  HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonArray("results");
		} catch (Exception e) {
			throw new Exception("地址搜索失败");
		}
	}
		/**
		 * 创建百度接口签名
		 * @param url
		 * @param params
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		private static String createSignature(String url, Map<?,?> params) throws UnsupportedEncodingException {
			String queryStr = HttpUtil.toQueryString(params);
			String wholeStr = URLEncoder.encode(String.format("%s?%s%s", url, queryStr, "mmodwwfueqNRiPIy709cPzp97mMmIGNM"));
			return DigestUtils.md5Hex(wholeStr);
		}
		
		/**
		 * 根据地址获取经纬度信息
		 * @param address
		 * @param city
		 * @throws Exception 
		 */
		public static JsonObject getCoordinateByAddress(String address, String city) throws Exception {
			Map<String, String> params = new LinkedHashMap<>();
			params.put("output", "json");
			params.put("ak", "eima7RUwpM8D5XdjrnyTNfCLGndYDDYC");
			params.put("address", address);
			params.put("city", city);
			try {
				params.put("sn", createSignature("/geocoder/v2/", params));
				String url = String.format("%s%s", "http://api.map.baidu.com", "/geocoder/v2/");
				String result =  HttpUtil.get(url, params, "UTF-8");
				JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			// int status = reJto.getAsJsonPrimitive("status").getAsInt();
			// if (status != 0) {
			// throw new
			// Exception(reJto.getAsJsonPrimitive("message").getAsString());
			// }
				return reJto.getAsJsonObject("result");
			} catch (Exception e) {
				throw new Exception("根据经纬度获取地址失败");
			}
			
		}

	/**
	 * 根据地址获取经纬度信息-数据校验全房通
	 * 
	 * @param address
	 * @param city
	 * @throws Exception
	 */
	public static CoordinateDto getCoordinateByAddressQft(BdHousePublishDtoQft qft) throws Exception {
		CoordinateDto coordinateDto = new CoordinateDto();
		StringBuilder stringBuilder = new StringBuilder();
		String cityName = qft.getCityName();// 城市
		String countyName = qft.getCountyName();// 行政区
		String districtName = qft.getDistrictName();// 小区名

		if (cityName == null || cityName == null) {
			return null;
		}

		stringBuilder.append(countyName);
		stringBuilder.append(districtName);

		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", "eima7RUwpM8D5XdjrnyTNfCLGndYDDYC");
		params.put("address", stringBuilder.toString());
		params.put("city", cityName);
		try {
			params.put("sn", createSignature("/geocoder/v2/", params));
			String url = String.format("%s%s", "http://api.map.baidu.com", "/geocoder/v2/");
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status == 0) {
				JsonObject res = reJto.getAsJsonObject("result");
				String lng = res.getAsJsonObject("location").getAsJsonPrimitive("lng").getAsString();
				String lat = res.getAsJsonObject("location").getAsJsonPrimitive("lat").getAsString();
				// int precise = res.getAsJsonObject("precise").getAsInt();
				// int confidence =
				// res.getAsJsonObject("confidence").getAsInt();
				// String level = res.getAsJsonObject("level").getAsString();
				coordinateDto.setLng(lng);
				coordinateDto.setLat(lat);
			} else {
				return null;
			}
			// return reJto.getAsJsonObject("result");

		} catch (Exception e) {
			throw new Exception("根据经纬度获取地址失败");
		}
		return coordinateDto;

	}

	/**
	 * 根据经纬度获取地址的相关信息
	 * 
	 * @param lat
	 * @param lng
	 * @throws Exception
	 */
	public static JsonObject getAddressByLocation(String lat, String lng) throws Exception {
		StringBuilder location = new StringBuilder();
		location.append(lat).append(",").append(lng);
		Map<String, String> params = new LinkedHashMap<>();
		params.put("output", "json");
		params.put("ak", "eima7RUwpM8D5XdjrnyTNfCLGndYDDYC");
		params.put("location", location.toString());
		try {
			params.put("sn", createSignature("/geocoder/v2/", params));
			String url = String.format("%s%s", "http://api.map.baidu.com", "/geocoder/v2/");
			String result = HttpUtil.get(url, params, "UTF-8");
			JsonObject reJto = GsonUtils.getInstace().fromJson(result, JsonObject.class);
			int status = reJto.getAsJsonPrimitive("status").getAsInt();
			if (status != 0) {
				throw new Exception(reJto.getAsJsonPrimitive("message").getAsString());
			}
			return reJto.getAsJsonObject("result");
		} catch (Exception e) {
			throw new Exception("根据经纬度获取地址失败");
		}

	}
}
