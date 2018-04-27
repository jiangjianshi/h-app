import com.huifenqi.hzf_platform.Application;
import com.huifenqi.hzf_platform.cache.RedisCacheManager;
import com.huifenqi.hzf_platform.comm.RedisClient;
import com.huifenqi.hzf_platform.service.FileService;
import com.huifenqi.hzf_platform.utils.OSSClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RedisTest {
	
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private RedisCacheManager redisCacheManager;

	@Test
	public void testFile()throws Exception{
		//System.out.println(redisCacheManager.get("usercomm-1ffbf714b39ea47091c86b4aa82abe4a"));
		System.out.println("---------------------------------------------------");
		Object dd1=redisClient.get("1ffbf714b39ea47091c86b4aa82abe4a");
		if(dd1!=null) {
			System.out.println(dd1);
		}
		System.out.println("---------------------------------------------------");
	}

	@Test
	public void testFile2()throws Exception{
		System.out.println("---------------------------------------------------");
		redisCacheManager.getValue("");
		String dd1=redisCacheManager.getValue("hzf_platform-city.house.serviceapartment");
		if(dd1!=null) {
			System.out.println(dd1);
		}
		System.out.println("---------------------------------------------------");
	}
}
