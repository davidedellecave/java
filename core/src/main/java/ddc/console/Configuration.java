package ddc.console;

import java.io.File;
import java.io.IOException;

import ddc.util.FormatUtils;
import ddc.util.XmlUtils;


public class Configuration {
	
	public static void writeConfiguration(File file, Configuration configuration) throws IOException {
		try {			
//			XmlUtils.write(configuration.getFile(), configuration);
			System.out.println(FormatUtils.format("Configuration Writing - file", file));
			XmlUtils.XStreamWrite(file, configuration);
		} catch (IOException e) {
			System.err.println("writeConfiguration() exception:[" + e.getMessage() + "]");
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
			System.out.println(FormatUtils.format("readConfiguration() file", file.getCanonicalPath()));
			Configuration c = (Configuration)XmlUtils.XStreamRead(file);
			return c;
		} catch (IOException e) {
			System.err.println("Configuration Reading - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		} catch (RuntimeException e) {
			System.err.println("Configuration Reading - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		}
	}
	
	public static void deleteConfiguration(File file) throws IOException {
		try {		
			System.out.println(FormatUtils.format("Configuration Deleting - file", file));
			file.delete();
		} catch (SecurityException e) {
			System.err.println("Configuration Deleting - file:[" + file.getCanonicalPath() + "] " + e.getMessage());
			throw e;
		}
	}
}
