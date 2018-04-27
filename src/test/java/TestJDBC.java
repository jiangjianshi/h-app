import java.util.Date;

////这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
//@RunWith(SpringJUnit4ClassRunner.class)
//// 这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class TestJDBC {
	
	
//	@Test
//	public void washData(){
//		
//		System.out.println("fdfs");
//	}
	
	public static void main(String[] args) {

//		String driver = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://rm-2ze41lui7lm025267o.mysql.rds.aliyuncs.com/db_huizhaofang_platform?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8";
//		String user = "huizhaofang";
//		String password = "zAbw4jiQZMbFFQraPBxXCrMD";
//		try {
//			Class.forName(driver);
//			Connection conn = DriverManager.getConnection(url, user, password);
//			if (!conn.isClosed()) {
//				Statement statement = conn.createStatement();
//				String sql = "select f_house_sell_id, f_agency_phone from t_house_base where f_delete = 0";
//				ResultSet rs = statement.executeQuery(sql);
//				while (rs.next()) {
//					String sellId = rs.getString("f_house_sell_id");
//					String agencyPhone = rs.getString("f_agency_phone");
//					System.out.println("update t_room_base rb set rb.f_agency_phone = '" + agencyPhone+ "' where rb.f_house_sell_id = '"+ sellId+"';" );
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println(new Date().getTime());
	}
}
