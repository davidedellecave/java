package ddc.console;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import ddc.util.FormatUtils;
import ddc.util.XmlUtils;


public class Configuration {
	private final static Logger logger = Logger.getLogger(Configuration.class);
	
	public static void writeConfiguration(File file, Configuration configuration) throws IOException {
		try {			
//			XmlUtils.write(configuration.getFile(), configuration);
			logger.debug(FormatUtils.format("Configuration Writing - file", file));
			XmlUtils.XStreamWrite(file, configuration);
		} catch (IOException e) {
			logger.error("writeConfiguration() exception:[" + e.getMessage() + "]");
			throw e;
		}		
	}
		
	public static Configuration writeConfiguration(File file, Class<? extends Configuration> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		Configuration c = clazz.newInstance();
		writeConfiguration(file, c);
		return c;
	}
	/**
	 * Read the configuration file
	 * @param file
	 * @return the configuration file, null if fails
	 * @throws IOException 
	 */
	public static Configuration readConfiguration(File file) throws IOException {
		try {
			logger.debug(FormatUtils.format("readConfiguration() file", file.getCanonicalPath()));
			Configuration c = (Configuration)XmlUtils.XStreamRead(file);
			return c;
		} catch (IOException e) {
			logger.error("Configuration Reading - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		} catch (RuntimeException e) {
			logger.error("Configuration Reading - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		}
	}
	
	public static void deleteConfiguration(File file) throws IOException {
		try {		
			logger.debug(FormatUtils.format("Configuration Deleting - file", file));
			file.delete();
		} catch (SecurityException e) {
			logger.error("Configuration Deleting - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		}
	}
}
