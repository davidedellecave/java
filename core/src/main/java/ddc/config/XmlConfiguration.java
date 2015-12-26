package ddc.config;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ddc.util.XmlUtils;

public class XmlConfiguration {
	private static final Logger logger = LogManager.getLogger(XmlConfiguration.class);	
	
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
		logger.error("Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
		throw new ConfigurationException(e);
	}		
}
	
	public static Object write(File file, Class<?> clazz) throws ConfigurationException {
		try {
			Object o = clazz.newInstance();
			write(file, o);
			return o;
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Configuration Writing - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
			throw new ConfigurationException(e);
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
			logger.error("Configuration Reading - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
			throw new ConfigurationException(e);
		}
	}
	
	public static void delete(File file) throws ConfigurationException {
		try {		
			file.delete();
		} catch (SecurityException e) {
			logger.error("Configuration Deleting - file:[" + file.toString() + "] exception:[" + e.getMessage() + "]");
			throw new ConfigurationException(e);
		}
	}
}
