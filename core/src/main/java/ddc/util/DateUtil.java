package ddc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String DATE_PATTERN_ISO = "yyyy-MM-dd HH:mm:ss";
		
	public static String format(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);			
		return formatter.format(date);
	}

	public static String format(long timestamp, String pattern) {
		Date d = new Date(timestamp);
		return format(d, pattern);
	}
	
	public static String formatISO(long timestamp) {
		Date d = new Date(timestamp);
		return format(d, DATE_PATTERN_ISO);
	}

	public static String formatISO(Date date) {
		return format(date, DATE_PATTERN_ISO);
	}
	
	public static String formatNowToISO() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_ISO);			
		return formatter.format(new Date());
	}

}
