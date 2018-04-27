
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
// 这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BdUpdateHouseTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	/***
	 * 测试发布房源接口
	 * @throws Exception
	 */
	@Test
	public void testBdFeedHouse() throws Exception
	{	 
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appId", "110004");// 平台分配给每个客户端的唯一客户标示字符串 是
		params.add("outHouseId", "123456789");
		params.add("picUrlList", "[{\"detailNum\":\"1\",\"picDesc\":\"客厅\",\"picUrl\":\"http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/11//0e5544a0dee18c2ea5ae33dd4394e68bcd4d98ff121.JPG\"},{\"detailNum\":\"2\",\"picDesc\":\"卧室\",\"picUrl\":\"http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/11//0e5544a0dee18c2ea5ae33dd4394e68bcd4d98ff121.JPG\"}]");
		params.add("rentType", "1");
		params.add("bedRoomNum", "1");
		params.add("livingRoom", "1");
		params.add("toiletNum", "1");
		params.add("rentRoomArea", "88");
		params.add("bedRoomType", "31");
		params.add("roomName", "蓝天房间");
		params.add("roomCode", "00001");
		params.add("faceToType", "60");
		params.add("totalFloor", "7");
		params.add("houseFloor", "6");
		params.add("featureTag", "10");
		params.add("detailPoint", "71");
		params.add("servicePoint", "91");
		params.add("monthRent", "2200");
		params.add("rentStartDate", "2017-04-14");
		params.add("shortRent", "0");
		params.add("provice", "北京");
		params.add("cityName", "北京");
		params.add("countyName", "沙河");
		params.add("areaName", "沙河");
		params.add("districtName", "蓝天家园");
		params.add("street", "北京市沙河蓝天家园小区");
		params.add("address", "1号楼3单元614");
		params.add("subwayLine", "昌平线");
		params.add("subwayStation", "沙河");
		params.add("houseDesc", "很好11111");
		params.add("xCode", "39.759754470331");
		params.add("yCode", "116.550481733");
		params.add("agentPhone", "18910573200");
		params.add("orderPhone", "18910573200");
		params.add("agentName", "常明炜111");
		params.add("videoUrl", "http://api.hzfapi.com/url");
		params.add("buildYear", "2016");
		params.add("supplyHeating", "1");
		params.add("greenRatio", "30");
		params.add("buildType", "71");
		params.add("ak", "efdbc3ff16782536bce5a05884c3bc6652f33b4dac0a467301585fa3b0eeb548");
		//调用接口，传入添加的用户参数
		String responseString =  mockMvc.perform(post("/task/roomSingleModify")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.params(params))
				.andExpect(status().isOk())    //返回的状态是200
               .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
		System.out.println("---------------------返回的json------------------------ " + responseString);     
	}

}