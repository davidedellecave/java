package ddc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringMatcher {
	public static boolean Match(String source, String matcher, boolean isRegexUsed) {
		return isRegexUsed ? source.matches(matcher) : source.contains(matcher); 
	}
	
	public static boolean match(String source, String matcher, boolean isRegexUsed, boolean ignoreCase) {
		if (ignoreCase && isRegexUsed) throw new UnsupportedOperationException("Ignore case for regext is not supported");			
		if (source==null || matcher==null) return false;
		if (ignoreCase) {
			return source.toLowerCase().contains(matcher.toLowerCase());
		} else {
			if (isRegexUsed) {
				Pattern pattern = Pattern.compile(matcher);
				Matcher match = pattern.matcher(source);
				return match.find();
			}			
			return source.contains(matcher);
		}
	}
	
	public static int indexOf(String line, String matcher, boolean ignoreCase) {		
		if (ignoreCase) {
			return line.toLowerCase().indexOf(matcher.toLowerCase());
		} else {
			return line.indexOf(matcher);
		}		
	}
	
	/**
	* Converts a windows wildcard pattern to a regex pattern
	*
	* @param wildcard - Wildcard pattern containing * and ?
	*
	* @return - a regex pattern that is equivalent to the windows wildcard pattern
	*/
	public static String wildcardToRegex(String wildcard) {
		if (wildcard == null) return null;	
		StringBuffer buffer = new StringBuffer();	
		char [] chars = wildcard.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
		  if (chars[i] == '*')
		    buffer.append(".*");
		  else if (chars[i] == '?')
		    buffer.append(".");
		  else if ("+()^$.{}[]|\\".indexOf(chars[i]) != -1)
		    buffer.append('\\').append(chars[i]); // prefix all metacharacters with backslash
		  else
		    buffer.append(chars[i]);
		}	
		return buffer.toString().toLowerCase();
	} 
	
	

}
