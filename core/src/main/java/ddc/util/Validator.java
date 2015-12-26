package ddc.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class Validator {
	
	
	public static String getDefaultIfBlank(String value, String defaultValue) {
		return StringUtils.isBlank(value) ? value : defaultValue; 
	}
	
	public static boolean isFolder(String folder) {
		if (StringUtils.isBlank(folder)) return false;
		File f = new File(folder);			
		return f.isDirectory();
	}
	
	public static boolean isNotFolder(String folder) {
		return !isFolder(folder);
	}
	
	public static boolean isNotFolder(File folder) {
		return !isFolder(folder);
	}
	
	public static boolean isFolder(File folder) {
		if (folder==null) return false;
		return folder.isDirectory();
	}

	public static boolean isFile(String filename) {
		if (StringUtils.isBlank(filename)) return false;
		File f = new File(filename);		
		return f.exists() && f.isFile();
	}

	public static boolean isFile(File file) {
		if (file==null) return false;
		return file.exists() && file.isFile();
	}

	public static boolean isNotFile(File file) {		
		return !isFile(file);
	}
	
	public static boolean existsPathToFile(String filename) {
		if (StringUtils.isBlank(filename)) return false;
		File f = new File(filename);			
		if (f.getParentFile()!=null) {
			return f.getParentFile().isDirectory();	
		}
		return false;
		
	}

	/*
	public static boolean parseArgBoolean(String value, String trueValue, String falseValue) {
		if (value.equalsIgnoreCase(trueValue)) {
			return new Boolean(true);
		}		
		if (value.equalsIgnoreCase(falseValue)) {
			return new Boolean(false);
		}
		return null; 
	}
	*/	

}
