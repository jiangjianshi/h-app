package com.house.asz;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.dto.request.house.HousePublishDtoAsz;
import com.huifenqi.hzf_platform.utils.GsonUtils;

public class TestC {
	public static void main(String args[]) throws UnsupportedEncodingException {
		String url ="http://isz.ishangzu.com/isz_thirdparty/openapi/apartment/getApartmentList.action";
		//String urlTest = "http://122.225.206.74:9981/isz_thirdparty/openapi/apartment/getApartmentList.action";
		String privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL6qibrpNeri8oZ5apGtJ2Xvr68aMH3Y6bNGi2+U+MFXY3Q6ZzoYvR+9CyvlpVlBcx4vsSsxcIz1BV5yjamKphfEqnyRrOvVdI7U+JztI2nyVOF/nK3WY5fcYU51NgNvmzQqsbNLPhrhxE9V4Z+2cmI5i3bprT/q2cPQVbhuKe0FAgMBAAECgYABGAliQSRGDLdHfjrWSyAGvbFMV+IfVrdAiA8UvM4QjefMKumcs7eiDvuZbN/d+zol2jAyBz6WEHHPcOjPKDR7u+Edf8pNgB7N11I2W2YKa13ZHGFDgEryk42ipcyZIKMHt8Jlz1PwqORKGc1Y5KkLiOJ8cVetf0Geg6Ojc7rAoQJBAPNLKxUQ1WENliHCRrq9LdnckV+A/eA+oI8kmfWWJ3ziKzRJUkVvE/RlS1qq3HCVF/kdOK/a55y6+rVDi3tKzLcCQQDIn73OcludC8OCJ5O9Ba2FOVmAiLRZPj8hhL0NwUVR70L6agiy6kT/yeoL7h6Adar6+DuegW7RcgFwwReXf5AjAkAT8jy0/G1SCKgfWmssEih5LREqEExAH0JQmgKZVNcl8PDz13MMSEANkGRuKYXrIP4XKWMlX8APZHD7fW8pC4ffAkEAgMhx2dT01BHaFXF8V6kOYueWeXjHdEYN1mFTzkGTUu4oa4CnRto1IpElaTUYZVOjRukTtELXtSDepdd9YmWjSwJAbfhK9taqhPOtVvswb6Ruh5tGUjBaCv8S08rmmaRc9lwJekCd5KftVLSxrN62T0g0OODbc/T8Qm7PtOVaFB0kPg==";
		//String data = "{\"client_id\":\"1234567890\",\"version\":\"1.0.0\",\"page_no\":1,\"page_size\":200,\"start_time\":\"2016-08-24 12:01:01\"}";
		
		//参数设置
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("client_id", "ISZ17091501");
		params.put("version", "1.0.0");
		params.put("page_no", "1");
		params.put("page_size", "2000");
		params.put("online_status", "ONLINE");
		params.put("start_time", "2017-10-01 12:01:01");
		
		//参数加密
		String data=JSON.toJSONString(params);
		System.out.println("data： "+ data);
		String datajson = RSAUtil.encrypt(data, privatekey);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("client_id", "ISZ17091501");
		map.put("data", datajson);
		String jsonStr=JSON.toJSONString(map);
		System.out.println(jsonStr);
		//发送请求
		String reponseStr = HttpClientUtil.postJson(url, jsonStr);
	
		// 封装返回结果
		JsonObject reJto = GsonUtils.getInstace().fromJson(reponseStr, JsonObject.class);
		int total = reJto.get("total").getAsInt();
		int pageNo = reJto.get("page_no").getAsInt();
		int pageSize = reJto.get("page_size").getAsInt();
		JsonArray aszJsonArray = reJto.getAsJsonArray("data");
		// 房源入库
		if (total > 0) {

			for (int j = 0; j < aszJsonArray.size(); j++) {
				String date = aszJsonArray.get(j).getAsJsonObject().get("update_time").getAsString();
				System.out.println(date);

			}
		}
	}
	
	
}
