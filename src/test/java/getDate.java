import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class getDate {

	public static void main(String[] args) {
		System.out.println(getNextDay(new Date()));

	}
	public static String getNextDay(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime(); 
        SimpleDateFormat riqi= new SimpleDateFormat("YYYY-MM-dd");
        String biaodan=riqi.format(date);
        return biaodan;  
    } 
}
