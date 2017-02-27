package ddc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.tz.ZoneInfoCompiler;

/**
 * @author davidedc, 30/ott/2010
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
	public enum Month {
		January(0), February(1), March(2), April(3), May(4), June(5), July(6), August(7), September(8), October(9), November(10), December(11);

		private final int month;

		Month(int month) {
			this.month = month;
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

	// Date and Time Pattern Result
	// "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
	// "EEE, MMM d, ''yy" Wed, Jul 4, '01
	// "h:mm a" 12:08 PM
	// "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific Daylight Time
	// "K:mm a, z" 0:08 PM, PDT
	// "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
	// "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
	// "yyMMddHHmmssZ" 010704120856-0700
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
	// "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" 2001-07-04T12:08:56.235-07:00
	// "YYYY-'W'ww-u" 2001-W27-3
	// ---------------- Parse

	public static Instant parseToInstant(String formattedDate, String pattern) throws ParseException {
		return parseToDate(formattedDate, pattern).toInstant();
	}
	
	public static Instant parseToInstant(String formattedDate, String[] pattern) throws ParseException {
		ParseException exception = null;
		for (String p : pattern) {
			try {
				return parseToInstant(formattedDate, p);
			} catch (ParseException e) {
				exception = e;
			}
		}
		throw exception;
		
	}
	
	public static Date parseToDate(String formattedDate, String[] pattern) throws ParseException {
		ParseException exception = null;
		for (String p : pattern) {
			try {
				return parseToDate(formattedDate, p);
			} catch (ParseException e) {
				exception = e;
			}
		}
		throw exception;
		
	}

	public static Date parseToDate(String formattedDate, String pattern) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date fromDate = formatter.parse(formattedDate);
		return fromDate;
	}

	public static String parseToString(String date, String[] fromPattern, String toPattern) throws ParseException {
		ParseException exception = null;
		for (String pattern : fromPattern) {
			try {
				return parseToString(date, pattern, toPattern);
			} catch (ParseException e) {
				exception = e;
			}
		}
		throw exception;
	}

//	public static LocalDate parseToLocalDate(String date, String[] fromPattern) throws DateTimeParseException {
//		DateTimeParseException exception = null;
//		for (String pattern : fromPattern) {
//			try {
//				return parseToLocalDate(date, pattern);
//			} catch (DateTimeParseException e) {
//				exception = e;
//			}
//		}
//		throw exception;
//	}

//	public static ZonedDateTime parseToZonedDateTime(String date, String[] fromPattern, Locale locale) throws DateTimeParseException {
//		DateTimeParseException exception = null;
//		for (String pattern : fromPattern) {
//			try {
//				return parseToZonedDateTime(date, pattern, locale);
//			} catch (DateTimeParseException e) {
//				exception = e;
//			}
//		}
//		throw exception;
//	}

	private static String parseToString(String formattedDate, String fromPattern, String toPattern) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(fromPattern);
		java.util.Date fromDate = formatter.parse(formattedDate);
		formatter = new SimpleDateFormat(toPattern);
		return formatter.format(fromDate);
	}

//	private static LocalDate parseToLocalDate(String formattedDate, String fromPattern) {
//		return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern(fromPattern));
//	}

//	private static ZonedDateTime parseToZonedDateTime(String formattedDate, String fromPattern, Locale locale) {
//		return ZonedDateTime.parse(formattedDate, DateTimeFormatter.ofPattern(fromPattern, locale));
//	}

	// ---------------- format

	public static String format(long timestamp, String pattern) {
		Date d = new Date(timestamp);
		return format(d, pattern);
	}

	public static String format(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String formatForISO(Date date) {
		if (date == null)
			return "";
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
		if (date == null)
			return "";
		return dateHumanReadableFormatter.format(date);
	}

	public static String formatForHuman(Object date) {
		if (date == null)
			return "";
		return dateHumanReadableFormatter.format(date);
	}

	public static String formatForFilename(Object date) {
		if (date == null)
			return "";
		return dateFormatterForFile.format(date);
	}

	public static String formatForFilename(Date date) {
		if (date == null)
			return "";
		return dateFormatterForFile.format(date);
	}

	public static String formatForTimestamp(Date date) {
		if (date == null)
			return "";
		return dateFormatterForTimestamp.format(date);
	}

	public static boolean isDateIntoTimeRange(Date d, String lowerBoundTime, String upperboudTime) throws ParseException {
		long timeMillis = getHSMFromDateMillis(d);

		Date lowerDate = parseToDate(lowerBoundTime, "HH:mm:ss");
		long lowerBoundMillis = getHSMFromDateMillis(lowerDate);

		Date upperDate = parseToDate(upperboudTime, "HH:mm:ss");
		long upperBoundMillis = getHSMFromDateMillis(upperDate);

		return (lowerBoundMillis <= timeMillis && timeMillis <= upperBoundMillis);
	}

	// ---------------- Create

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
		return (cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)) * 1000;
	}

	public static Instant toInstant(Date date) {
		return date.toInstant();
	}

	public static Date toDate(Instant instant) {
		return Date.from(instant);
	}

	public static long toMillis(Instant instant) {
		return instant.toEpochMilli();
	}

	public static Instant toInstant(long millis) {
		return Instant.ofEpochMilli(millis);
	}

	public static ZonedDateTime toZonedDateTime(long millis) {
		Instant i = Instant.ofEpochMilli(millis);
		return i.atZone(ZoneId.systemDefault());
	}

	public static long toMillis(LocalDateTime ldt) {
		Instant instant = ldt.toInstant(ZoneOffset.UTC);
		return instant.toEpochMilli();
	}

}
