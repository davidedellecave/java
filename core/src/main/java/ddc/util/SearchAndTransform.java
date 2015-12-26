/*
 * Created on 26-gen-2005 
 * By davidedc
 */
package ddc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchAndTransform {
	public static String replace(String source, String regexp, StringTransformer transformer) throws Exception {
		CharSequence inputStr = source;
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(inputStr);

		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {		
			String replaceStr = matcher.group();
			replaceStr = transformer.tranform(replaceStr);
			if(replaceStr == null) {
				replaceStr = "";
			}
			matcher.appendReplacement(buf, replaceStr);
		}
		matcher.appendTail(buf);

		return buf.toString();
	}

	public static String find(String source, String regexp) throws Exception {
		CharSequence inputStr = source;
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.find())
			return matcher.group();

		return null;
	}
}