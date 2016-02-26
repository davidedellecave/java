package ddc.util;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

public class CsvUtil {
	public static String getHeader(LinkedHashMap<String, String> map, char separator) {
		return StringUtils.join(map.keySet(), separator);
	}
	
	public static String getLine(LinkedHashMap<String, String> map, char separator) {
		return StringUtils.join(map.values(), separator);		
	}

	public static String[] getHeader(String line, char separator) {
		String[] toks = StringUtils.split(line, separator);
		for (int i=0; i<toks.length; i++) {
			toks[i]=toks[i].trim();
		}
		return toks;
	}
}
