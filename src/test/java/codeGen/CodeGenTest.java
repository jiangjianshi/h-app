package codeGen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

import org.junit.Test;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class CodeGenTest {

	public static final char UNDERLINE = '_';

	@Test
	public void contextLoads() {

		String driver = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://localhost:3306/db_huizhaofang_platform?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8";
//		String user = "root";
//		String password = "root";
		String url = "jdbc:mysql://rm-2ze41lui7lm025267o.mysql.rds.aliyuncs.com/db_huizhaofang_platform?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8";
		String user = "huizhaofang";
		String password = "zAbw4jiQZMbFFQraPBxXCrMD";
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed()) {
				Statement statement = conn.createStatement();
				String sql = "select COLUMN_NAME,COLUMN_COMMENT ,DATA_TYPE from information_schema.COLUMNS where table_name = 't_house_operate_record'";
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					String columnComment = rs.getString("COLUMN_COMMENT");
					String dataType = rs.getString("DATA_TYPE");

					String columnName = rs.getString("COLUMN_NAME");
					String tempColumn = columnName.substring(2, columnName.length());
					String camelColumn = underlineToCamel(tempColumn);
					
					/**
					 * 
					 */
					
					System.out.println();
					StringBuffer sb = new StringBuffer();
					sb.append("/**").append("\n *").append(columnComment).append("\n */");
					System.out.println(sb.toString());
					System.out.println("@Column(name = \"" + columnName + "\")");
					System.out.println(getEntity(camelColumn, columnComment, dataType));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEntity(String camelColumn, String columnComment, String dataType) {
		String type = "";

		switch (dataType) {
		case "int":
			type = "Integer";
			break;
		case "varchar":
			type = "String";
			break;
		case "float":
			type = "Double";
			break;
		case "tinyint":
			type = "Integer";
			break;
		case "datetime":
			type = "Date";
			break;
		case "timestamp":
			type = "Date";
			break;
		}
		return "private " + type + " " + camelColumn + ";";
//		return "private " + type + " " + camelColumn + "; //" + columnComment;
	}

	/**
	 * 将下划线转换为峰陀
	 * 
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将峰陀转换为下划线
	 * 
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
