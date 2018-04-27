
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.huifenqi.hzf_platform.Application;




//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
// 这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FeedHouseTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

//	/***
//	 * 测试查询房源接口
//	 * @throws Exception
//	 */
//	@Test
//	public void testSearch() throws Exception
//	{	
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();	
//		params.add("appId", "110001");
//		params.add("cityId", "1");
//		params.add("sellId", "HF201703310010ak0");
//		//调用接口，传入添加的用户参数
//		String responseString =  mockMvc.perform(post("/search")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//				.params(params))
//				.andExpect(status().isOk())    //返回的状态是200
//                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
//		System.out.println("---------------------返回的json1------------------------ " + responseString);     
//	}
//	
//	/***
//	 * 测试查询房源详情接口
//	 * @throws Exception
//	 */
//	@Test
//	public void testGetHouseDetail() throws Exception
//	{	
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();	
//		params.add("appId", "110001");
//		params.add("cityId", "1");
//		params.add("sellId", "HF201703310010ak0");
//		//调用接口，传入添加的用户参数
//		String responseString =  mockMvc.perform(post("/search/getHouseDetail")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//				.params(params))
//				.andExpect(status().isOk())    //返回的状态是200
//                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
//		System.out.println("---------------------返回的json2------------------------ " + responseString);     
//	}
	
	/***
	 * 测试发布房源接口
	 * @throws Exception
	 */
	@Test
	public void testfeedHouse() throws Exception
	{	 
		
//				"detailPoint":"71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86",
//				"servicePoint":"91,92,93,94,95,96,97",
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appId", "110001");// 平台分配给每个客户端的唯一客户标示字符串 是
		params.add("secretKey", "ba0b3d86458a69b67df51a18d5e3c9bc");// 平台用户用于创建签名和加密数据的秘钥字符串 是
		params.add("ts", "110001");// ts 是当前的unix 时间戳，单位到毫秒 字符串 是
		params.add("sign", "87610999a32020df592502743991394f18c96d89fc063a88fc79f048ff1e71bc");// 请求签名签名规则字符串 是
		params.add("positionX", "39.915457");// x 坐标 纬度 字符串 是
		params.add("positionY", "116.404692");// y 坐标 经度 字符串 是
		params.add("communityName", "渝中");//小区名 字符串 是
		params.add("price", "120000");// 租金月租金，单位为分 整型 是
		params.add("bonus", "0");//服务费 ，单位为分 整型 是
		params.add("deposit", "120000");//押金，单位为分 整型 是
		params.add("hasKey", "1");// 是否有钥匙 枚举 0:无 1:有 是
		params.add("companyId", "");//经纪公司Id 整型 是
		params.add("companyName", "常明炜");// 经纪公司名称 字符串 是
		params.add("agencyId", "");//经纪人Id 整型 是
		params.add("agencyPhone", "18910573200");//经纪人电话 字符串 是
		params.add("agencyName", "邱伟");//经纪人姓名 字符串 是
		params.add("agencyIntroduce", "");//经纪人自我介绍 字符串 否
		// params.add("agencyGender", "1");//经纪人性别枚举1:男2:女 是
		params.add("agencyAvatar", "http://imgcdn.eallcn.com/beaver/image/im/2015/11/03/13/63fd1105b59a2113ee181078c2d30c47c36ecc12.jpg,http://imgcdn.eallcn.com/beaver/image/im/2015/11/03/13/63fd1105b59a2113ee181078c2d30c47c36ecc12.jpg");//经纪人头像 http://bar.com/avatar.jpg 字符串 否
		params.add("status", "2");// 房源状态枚举0:未上架2:待出租3:已出租 是
		params.add("bedroomNum", "1");//卧室数量 整型 是
		params.add("livingroomNum", "1");// 客厅数量 整型 是
		params.add("kitchenNum", "0");// 厨房数量 整型 是
		params.add("toiletNum", "0");// 卫生间数量 整型 是
		params.add("balconyNum", "0");// 阳台数量 整型 是
		params.add("buildingNo", "1");// 楼栋编号 字符串 是
		params.add("unitNo", "1");// 单元号 字符串 否
		params.add("houseNo", "");// 门牌号 字符串 否
		params.add("buildingName", "渝中");// 楼栋名称 字符串 是
		params.add("area", "38.0");//面积最小值:8浮点型 是
		params.add("floorNo", "11");// 所在楼层，物理楼层 字符串 是
		params.add("floorTotal", "22");// 总楼层数量 字符串 是
		params.add("orientation", "10001");// 朝向枚举10001:东10002:西10003:南10004:北10023:西南10024:西北10014:东北10013:东南10034:南北是10012:东西
		params.add("buildingType", "0");// 建筑类型枚举1:板楼2:塔楼3:板塔结合4:独栋5:联排6:叠拼 是
		params.add("fitmentType", "0");// 装修档次枚举1:精装2:简装3:毛坯4:老旧5.豪装6.中装7.普装 是
		params.add("buildingYear", "2016");// 建筑时间(单位:年) 字符串 否
		params.add("toilet", "0");// 是否有独立卫生间枚举0:无1:有 否
		params.add("balcony", "0");// 是否有独立阳台枚举0:无1:有 否
		params.add("insurance", "1");// 是否有家财险枚举 否0:无1:有
		params.add("checkIn", "2017-11-15");// 入住时间(年月日：yyyy-MM-dd) 字符串 是
		params.add("depositMonth", "1");// 押几 整型 是
		params.add("periodMonth", "1");// 付几 整型 是
		params.add("entireRent", "0");// 租赁方式枚举0:分租1:整租2:整分皆可 是
		params.add("settings", "bed:1,sofa:2");//主要室内设施，用“,”分割，例n如：bed:2,sofa:3
		
/*		主要室内设施，用“,”分割， 字符串 否
		例如：bed:2,sofa:3
		bed:床,
		sofa:沙发
		table:电脑桌
		wardrobe:衣柜
		chair:椅子
		tv:电视
		fridge:冰箱
		ac:空调
		washer:洗衣机
		microwaveoven:微波炉
		kettle:电水壶
		curtain:窗帘
		mattress:被褥
		vase:花瓶
		lamp:台灯
		decoration:装饰画*/
//		params.add("bed", "HF201703310010ak0");//:床,
//		params.add("sofa", "HF201703310010ak0");//:沙发
//		params.add("table", "HF201703310010ak0");//:电脑桌
//		params.add("wardrobe", "HF201703310010ak0");//:衣柜
//		params.add("chair", "HF201703310010ak0");//:椅子
//		params.add("tv", "HF201703310010ak0");//:电视
//		params.add("fridge", "HF201703310010ak0");//:冰箱
//		params.add("ac", "HF201703310010ak0");//:空调
//		params.add("washer", "HF201703310010ak0");//:洗衣机
		params.add("microwaveoven", "HF201703310010ak0");//:微波炉
		params.add("kettle", "HF201703310010ak0");//:电水壶
		params.add("curtain", "HF201703310010ak0");//:窗帘
		params.add("mattress", "HF201703310010ak0");//:被褥 字符串 否
		params.add("vase", "HF201703310010ak0");//:花瓶
		params.add("lamp", "HF201703310010ak0");//:台灯
		params.add("houseType", "1");//:台灯
		params.add("focusCode", "123abc");//:集中式房源编号
		params.add("agencyIntroduce", "中介介绍");//:集中式房源编号
		params.add("decoration", "HF201703310010ak0");//:装饰画
		params.add("settingsAddon", "wifi:1");//次要设施用“,”分割，例如: wifi:1wifi:无线字符串 否
		params.add("desc", "房源描述");// 房源描述 字符串 否
		params.add("imgs",
				"http://imgcdn.eallcn.com/eagle/12/house_image/2015/11/14/10//2239f1fc9f145677609e99071bf98208d436e938296.jpg,http://image.quanfangtong.net/Company_201507160003/housing1/kfoc7d9e2.jpg,http://image.quanfangtong.net/Company_201507160003/housing1/kfoc7d9e2.jpg,http://image.quanfangtong.net/Company_201507160003/housing1/kfoc7d9e2.jpg");// 房源照片（分租情况下，房间照片必须写到房间照片里面，不能写到房源里面）用“,”分割，最多传20
																																// 张图片字符串
																																// 否
		// http://imgcdn.eallcn.com/eagle/12/house_image/2015/11/14/10//2239f1fc9f145677609e99071bf98208d436e938296.jpg
		// http://image.quanfangtong.net/Company_201507160003/housing1/kfoc7d9e2.jpg
		params.add("bizName", "渝中");// 商圈名称 字符串 是

		//调用接口，传入添加的用户参数
		String responseString =  mockMvc.perform(post("/house/feedHouse")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.params(params))
				.andExpect(status().isOk())    //返回的状态是200
               .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
		System.out.println("---------------------返回的json3------------------------ " + responseString);     
	}

}