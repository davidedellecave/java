package ddc.config;

import java.io.File;
import java.io.IOException;

import ddc.util.XmlUtils;

public class XmlConfiguration {
	
//	public static XmlConfiguration write(File file, XmlConfiguration configuration) throws ConfigurationException {
//		try {			
//			XmlUtils.XStreamWrite(file, configuration);
//			return configuration;
//		} catch (IOException e) {
//			logger.error("Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
//			throw new ConfigurationException(e);
//		}		
//	}
		
//	public static XmlConfiguration write(File file, Class<? extends XmlConfiguration> clazz) throws ConfigurationException {
//		XmlConfiguration c;
//		try {
//			c = clazz.newInstance();
//			write(file, c);
//			return c;
//		} catch (InstantiationException | IllegalAccessException e) {
//			logger.error("Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
//			throw new ConfigurationException(e);
//		}
//	}
	
	public static void write(File file, Object instance) throws ConfigurationException {
	try {			
		XmlUtils.XStreamWrite(file, instance);
	} catch (IOException e) {
		String info ="Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]"; 
		throw new ConfigurationException(info);
	}		
}
	
	public static Object write(File file, Class<?> clazz) throws ConfigurationException {
		try {
			Object o = clazz.newInstance();
			write(file, o);
			return o;
		} catch (InstantiationException | IllegalAccessException e) {
			String info ="Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]"; 
			throw new ConfigurationException(info);

		}
	}
	
	/**
	 * Read the configuration file
	 * @param file
	 * @return the configuration file, null if fails
	 * @throws IOException 
	 */
	public static Object read(File file) throws ConfigurationException {
		try {
			return XmlUtils.XStreamRead(file);
		} catch (IOException | RuntimeException e) {
			String info ="Configuration Reading - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]"; 
			throw new ConfigurationException(info);

		}
	}
	
	public static void delete(File file) throws ConfigurationException {
		try {		
			file.delete();
		} catch (SecurityException e) {
			String info ="Configuration Deleting - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]"; 
			throw new ConfigurationException(info);

		}
	}
}
