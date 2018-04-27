
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
public class FeedRoomTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}


	/***
	 * 测试发布房间接口
	 * @throws Exception
	 */
	@Test
	public void testfeedRoom() throws Exception
	{	
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appId", "110001");// 平台分配给每个客户端的唯一客户标示字符串 是
		params.add("sellId", "HF004985162615");// 平台分配给每个客户端的唯一客户标示字符串 是
		params.add("token", "071TdDbnIlH9C4w3bU1T58894jwkW8E3");// 发布房源接口返回的token
		params.add("sign", "9609069bfe08b6987000b8387bef0f41f5362d18fdd7cf4f61794ce3912f13dc");// 发布房源接口返回的token
		params.add("price", "2100");// 租金月租金，单位为分 整型 是
		params.add("bonus", "0");//服务费 ，单位为分 整型 是
		params.add("ts", "1576453");//服务费 ，单位为分 整型 是
		params.add("deposit", "1600");//押金，单位为分 整型 是
		params.add("hasKey", "1");// 是否有钥匙 枚举 0:无 1:有 是
		params.add("companyId", "123456");//经纪公司Id 整型 是
		params.add("companyName", "HF201703310010ak0");//经纪公司名称 字符串 是
		params.add("agencyId", "123456");//经纪人Id 整型 是
		params.add("agencyPhone", "18910573100");//经纪人电话 字符串 是
		params.add("agencyName", "常明炜1");//经纪人姓名 字符串 是
		params.add("agencyIntroduce", "好房间经纪人自我介绍");//经纪人自我介绍 字符串 否
		params.add("agencyGender", "1");//经纪人性别枚举1:男2:女 是
		params.add("agencyAvatar", "http://img.huifenqi.com/D00/41/1a/1464858923246Na88ngxK.JPG");//经纪人头像 http://bar.com/avatar.jpg 字符串 否
		params.add("status", "2");// 房源状态枚举0:未上架2:待出租3:已出租 是
		params.add("area", "88.0");//面积最小值:8浮点型 是
		params.add("orientation", "10004");// 朝向枚举10001:东10002:西10003:南10004:北10023:西南10024:西北10014:东北10013:东南10034:南北是10012:东西
		params.add("fitmentType", "1");// 装修档次枚举1:精装2:简装3:毛坯4:老旧5.豪装6.中装7.普装 是
		params.add("toilet", "1");// 是否有独立卫生间枚举0:无1:有 否
		params.add("balcony", "1");// 是否有独立阳台枚举0:无1:有 否
		params.add("insurance", "1");// 是否有家财险枚举 否0:无1:有
		params.add("checkIn", "2017-11-12");// 入住时间(年月日：yyyy-MM-dd) 字符串 是
		params.add("depositMonth", "1");// 押几 整型 是
		params.add("periodMonth", "3");// 付几 整型 是
		params.add("settings", "bed:2,sofa:3");//主要室内设施，用“,”分割，例如：bed:2,sofa:3
		params.add("settingsAddon", "wifi:1");//次要设施用“,”分割，例如: wifi:1wifi:无线字符串 否
		params.add("desc", "优化间");// 房源描述 字符串 否
		params.add("imgs", "http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/12//81a8792fe099b88703cc87b8517c5f537918b9f1160.JPG,http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/12//81a8792fe099b88703cc87b8517c5f537918b9f1160.JPG,http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/12//81a8792fe099b88703cc87b8517c5f537918b9f1160.JPG");//房源照片（分租情况下，房间照片必须写到房间照片里面，不能写到房源里面）用“,”分割，最多传20 张图片字符串 否
		params.add("roomName", "房间名称");// 商圈名称 字符串 是
		params.add("roomType", "20");//房间类型  1主卧  10次卧  20 优化间

		//调用接口，传入添加的用户参数
		String responseString =  mockMvc.perform(post("/house/feedRoom")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.params(params))
				.andExpect(status().isOk())    //返回的状态是200
              .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
		System.out.println("---------------------返回的json3------------------------ " + responseString);     
	}
	
	
}