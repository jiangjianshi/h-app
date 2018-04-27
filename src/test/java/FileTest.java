import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.huifenqi.hzf_platform.Application;
import com.huifenqi.hzf_platform.service.FileService;
import com.huifenqi.hzf_platform.utils.OSSClientUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FileTest {
	
	@Resource
	private OSSClientUtils ossClient;
	
	@Resource
	private FileService fileService;
	
	@Test
	public void testFile(){
		
//		try {
//			System.out.println("start....");
			
//			File img = new File("/Users/jjs/Downloads/abc.jpg");
//			InputStream in = new BufferInputStream(img);
//			ossClient.upload(in, in.available(), "HFest/image1.jpg");
//			ossClient.upload(new File("/Users/jjs/Downloads/abc.jpg"), "HFest/image.jpg");
			
//			ossClient.download("test/image.jpg", "/Users/jjs/Downloads/abc_oss.jpg");
			
//			List list = Arrays.asList("test/image.jpg");
//			ossClient.delete(list);
//			
//			
//			System.out.println("end......");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			String savePath = fileService.downImage("http://pic1.58cdn.com.cn/gongyu/n_v1bkuyfvj3uyhftgwyeqqq_4efbf870af0a0e2d.jpg", "='HF201706024154L65");
//			String savePath = fileService.downFile("http://imgcdn.eallcn.com/eagle/12/house/2015/08/22/11//0db5e1492cb86a3123ba29e1e5827848045a66ac225.JPG", "HF20160602009694g");
			System.out.println(savePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
