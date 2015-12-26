package ddc.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

//	byte[] xmlStringArray = Base64.encodeBase64(xmlString.getBytes(CHARSET_DEFAULT));
//	String xmlBase64 = new String(xmlStringArray, CHARSET_DEFAULT);

	public static String encodeToString(String source, String charset) throws UnsupportedEncodingException {
		byte[] sourceArray = Base64.encodeBase64(source.getBytes(charset));
		return new String(sourceArray, charset);		
	}
	
	public static String encodeToString(byte[] source, String charset) throws UnsupportedEncodingException {
		byte[] sourceArray = Base64.encodeBase64(source);
		return new String(sourceArray, charset);		
	}
	
	public static String decodeToString(String source, String charset) throws UnsupportedEncodingException {
		byte[] sourceArray = Base64.decodeBase64(source.getBytes(charset));
		return new String(sourceArray, charset);		
	}
	
	public static String decodeToString(byte[] source, String charset) throws UnsupportedEncodingException {
		byte[] sourceArray = Base64.decodeBase64(source);
		return new String(sourceArray, charset);		
	}
	
	public static byte[] encode(String source, String charset) throws UnsupportedEncodingException {
		return Base64.encodeBase64(source.getBytes(charset));		
	}
	
	public static byte[] decode(String source, String charset) throws UnsupportedEncodingException {
		return Base64.decodeBase64(source.getBytes(charset));		
	}
	
	public static byte[] encode(byte[] source) throws UnsupportedEncodingException {
		return Base64.encodeBase64(source);		
	}
	
	public static byte[] decode(byte[] source) throws UnsupportedEncodingException {
		return Base64.decodeBase64(source);		
	}

}
