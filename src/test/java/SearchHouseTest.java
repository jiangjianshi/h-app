
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
public class SearchHouseTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	/***
	 * 测试查询房源接口
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception
	{	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();	
		params.add("appId", "110001");
		params.add("cityId", "1");
		params.add("sellId", "HF201703310010ak0");
		//调用接口，传入添加的用户参数
		String responseString =  mockMvc.perform(post("/search")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.params(params))
				.andExpect(status().isOk())    //返回的状态是200
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
		System.out.println("---------------------返回的json1------------------------ " + responseString);     
	}
	
	/***
	 * 测试查询房源详情接口
	 * @throws Exception
	 */
	@Test
	public void testGetHouseDetail() throws Exception
	{	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();	
		params.add("appId", "110001");
		params.add("cityId", "1");
		params.add("sellId", "HF201703310010ak0");
		//调用接口，传入添加的用户参数
		String responseString =  mockMvc.perform(post("/search/getHouseDetail")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.params(params))
				.andExpect(status().isOk())    //返回的状态是200
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串	
		System.out.println("---------------------返回的json2------------------------ " + responseString);     
	}
}