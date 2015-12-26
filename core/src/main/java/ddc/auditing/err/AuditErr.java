package ddc.auditing.err;

import org.apache.log4j.Logger;

import ddc.auditing.perf.AuditPerf;

public class AuditErr {
	private static final Logger logger = Logger.getLogger(AuditPerf.class);
	public static String AUDIT_TAG_START="<audit metric='Error'>";
	public static String AUDIT_TAG_END="</audit>";
	
	public static char SEP_FIELD=';';
	public static char SEP_VALUE='=';
	public static char REPLACING_CHAR='_';
	
	public static void log(@SuppressWarnings("rawtypes") Class clazz, String method, Throwable exception) {		
		doLog(clazz, method, getString(exception));
	}

	public static void log(@SuppressWarnings("rawtypes") Class clazz, String method, String message) {
		doLog(clazz, method, getString(message));
	}
	
	private static void doLog(@SuppressWarnings("rawtypes") Class clazz, String method, String message) {		
		StringBuilder b = new StringBuilder();
		b.append(AUDIT_TAG_START)
		.append("timestamp")		
		.append(SEP_VALUE)
		.append(System.currentTimeMillis())
		.append(SEP_FIELD)		
		.append("class")		
		.append(SEP_VALUE)
		.append(clazz.getCanonicalName()).append(".").append(method)
		.append(SEP_FIELD)
		.append("exception")
		.append(SEP_VALUE)
		.append(replaceReservedChars(message))		
		.append(AUDIT_TAG_END);
		logger.info(b.toString());
	}
	
	private static String replaceReservedChars(String source) {
		return source.replace(SEP_FIELD, REPLACING_CHAR).replace(SEP_VALUE, REPLACING_CHAR);
	}
	
	private static String getString(Throwable source) {
		return source==null ? "null" : source.getMessage();
	}
	private static String getString(String source) {
		return source==null ? "null" : source;
	}

}
