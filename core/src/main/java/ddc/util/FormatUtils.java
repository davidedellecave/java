package ddc.util;

import java.io.File;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FormatUtils {
	

		
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
	
	
	public static String toString(Object o) {
		return ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public static String formatFile(File file) {
		return " file:[" + file==null ? "null" : file.getAbsolutePath() + "]"; 
	}
	
	public static String formatException(Exception e) {
		return " exception:[" + e==null ? "null" : e.getMessage() + "]"; 
	}
	
}
