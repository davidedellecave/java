package ddc.util;

import java.nio.charset.Charset;

public class StringUtil {
//	private static final int INDEX_NOT_FOUND=-1;
	
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset ASCII = Charset.forName("ASCII");
    
    public static String fromUTF8toISO88591(String source) {
    	return toCharset(source, UTF_8, ISO_8859_1);
    }
    
    public static String toCharset(String source, Charset fromCharset, Charset toCharset) {
    	return new String(source.getBytes(fromCharset), toCharset);
    }
	
	public static StringBuilder removeEnd(final StringBuilder str, final char remove) {
		if (str==null || str.length()==0) return str;
		int l = str.length()-1;
		int size = 0;
		while (l>=0 && str.charAt(l)==remove) {
			size++;
			l--;
		}
		if (size>0) return str.delete(str.length()-size, str.length());
		return str;
	} 
	
	public static StringBuilder removeChar(final StringBuilder str, final char remove) {
		if (str==null || str.length()==0) return str;
		
		StringBuilder out = new StringBuilder(str.length());
		for (int i=0; i<str.length(); i++) {
			char ch = str.charAt(i);
			if (ch!=remove) out.append(ch);
		}
		return out;
	} 
	
//	public static String removeNonPrintable(final String str) {
//		if (str==null || str.length()==0) return str;
//		
//		StringBuilder out = new StringBuilder(str.length());
//		for (int i=0; i<str.length(); i++) {
//			char ch = str.charAt(i);
//			if (ch>31) out.append(ch);
//		}
//		return out.toString();
//	} 
	
	//Remove all chars minus or equal than ASCII (decimal) 31
    public static String removeNonPrintable(final CharSequence cs) {
        if (cs == null || cs.length() == 0) return "";
        StringBuilder out = new StringBuilder(cs.length());
        for (int i = 0; i < cs.length(); i++) {
			char ch = cs.charAt(i);
			if (ch>31) out.append(ch);
        }
        return out.toString();
    }

	public static String[] splitByChunk(String s, int chunkSize){
	    int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
	    String[] returnVal = new String[chunkCount];
	    for(int i=0;i<chunkCount;i++){
	        returnVal[i] = s.substring(i*chunkSize, Math.min((i+1)*(chunkSize), s.length()));
	    }
	    return returnVal;
	}

}
