package ddc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author davidedc, 30/ott/2010
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
	public enum Month {January (0), 
						February(1),
						March(2),
						April(3),
						May(4),
						June(5),
						July(6),
						August(7),
						September(8),
						October(9),
						November(10),
						December(11);
	
		private final int month;
		Month(int month) {
			this.month=month;
		}
		
		int getMonthValue() {
			return month;
		}
	}
	
	public static String DATE_PATTERN_ISO = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat dateISOFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	private static SimpleDateFormat dateHumanReadableFormatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");	
	private static SimpleDateFormat dateFormatterForFile = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private static SimpleDateFormat dateFormatterForTimestamp = new SimpleDateFormat("yyyyMMddHHmmss");

	
	//---------------- Parse
	
	public static String parseDate(Date date, String pattern) {
		if (date==null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String parseDate(String formattedDate, String fromPattern, String toPattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(fromPattern);
		Date fromDate;
		try {
			fromDate = formatter.parse(formattedDate);
		} catch (ParseException e) {
			System.err.print(e.getMessage());
			return "";
		}
		formatter = new SimpleDateFormat(toPattern);
		return formatter.format(fromDate);
	}
	
	public static Date parseDate(String formattedDate, String patter) {
		SimpleDateFormat formatter = new SimpleDateFormat(patter);		
		try {
			Date fromDate = formatter.parse(formattedDate);
			return fromDate;
		} catch (ParseException e) {
			System.err.println("parseDate() Exception:[" + e.getMessage() + "]");
			return null;
		}
	}
	
	
	//---------------- format
	
	public static String format(long timestamp, String pattern) {
		Date d = new Date(timestamp);
		return format(d, pattern);
	}
	
	public static String format(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);			
		return formatter.format(date);
	}
	
	public static String formatDate(Date date, String pattern) {
		if (date==null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);	
		return formatter.format(date);
	}
	
	public static String formatForISO(Date date) {
		if (date==null) return "";
		return dateISOFormatter.format(date);
	}
	
	public static String formatISO(long timestamp) {
		Date d = new Date(timestamp);
		return formatForISO(d);
	}
	
	public static String formatNowToISO() {
		return formatForISO(new Date());
	}
	
	public static String formatForHuman(Date date) {
		if (date==null) return "";
		return dateHumanReadableFormatter.format(date);
	}
	public static String formatForHuman(Object date) {	
		if (date==null) return "";
		return dateHumanReadableFormatter.format(date);
	}
	
	public static String formatForFilename(Object date) {	
		if (date==null) return "";
		return dateFormatterForFile.format(date);
	}
	public static String formatForFilename(Date date) {
		if (date==null) return "";
		return dateFormatterForFile.format(date);
	}
	
	public static String formatForTimestamp(Date date) {
		if (date==null) return "";
		return dateFormatterForTimestamp.format(date);
	}
	
	public static boolean isDateIntoTimeRange(Date d, String lowerBoundTime, String upperboudTime) {
		long timeMillis = getHSMFromDateMillis(d);
		
		Date lowerDate = parseDate(lowerBoundTime,"HH:mm:ss");
		long lowerBoundMillis = getHSMFromDateMillis(lowerDate);

		Date upperDate = parseDate(upperboudTime,"HH:mm:ss");				
		long upperBoundMillis = getHSMFromDateMillis(upperDate);			
		
		return (lowerBoundMillis<=timeMillis && timeMillis<=upperBoundMillis);
	}

	//---------------- Create
	
	public static Date create(int year, Month month, int day) {
		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, 0, 0, 0);
		return c.getTime();
	}
	
	public static Date create(int year, Month month, int day, int hour, int minute, int second) {
		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, hour, minute, second);
		return c.getTime();
	}
	
	public static Date createStartDay(int year, Month month, int day) {
		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, 0, 0, 0);
		return c.getTime();
	}

	public static Date createEndDay(int year, Month month, int day) {
		GregorianCalendar c = new GregorianCalendar(year, month.getMonthValue(), day, 23, 59, 59);
		return c.getTime();
	}
	
	private static long getHSMFromDateMillis(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return (cal.get(Calendar.HOUR_OF_DAY)*60*60+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.SECOND))*1000;
	}
	

}
