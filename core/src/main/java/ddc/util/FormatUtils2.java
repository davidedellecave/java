package ddc.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class FormatUtils2 {
	
	private static SimpleDateFormat dateHumanReadableFormatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");	
	private static SimpleDateFormat dateFormatterForFile = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	public static String formatDate(Date date, String pattern) {
		if (date==null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
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
		
	public static String format(Object name, Object value) {
		String s = " " + name!=null ? name + ":" : "";
		s += format(value);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public static String format(Object value) {
		if (value==null) return "[null]";
		if (value instanceof String[]) {
			String[] values = (String[])value;
			String ss="";
			for (String s : values)
				ss+=format(s) + " ";
			return "{" + ss + "}";
		}else if (value instanceof Collection) {
			Collection<Object> values = (Collection<Object>)value;
			String ss="";
			for (Object s : values)
				ss+=format(s) + " ";			
			return " {" + ss + "}"; 
		}	
		return "[" + value + "]";
	}
	
	public static String formatFile(File file) {
		return " file:[" + file==null ? "null" : file.getAbsolutePath() + "]"; 
	}
	
	public static String formatException(Exception e) {
		return " exception:[" + e==null ? "null" : e.getMessage() + "]"; 
	}
	
}
