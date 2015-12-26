package ddc.console;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import ddc.util.Validator;


public abstract class ConfigurableConsoleApp extends AbstractConsoleApp {
	private final static Logger logger = Logger.getLogger(ConfigurableConsoleApp.class);
	private Configuration configuration;
	private File configurationFile;
	
	public abstract boolean validateConfiguration(Configuration configuration);
	public abstract Configuration createConfiguration();
		
	public boolean validateArgs() {
		String[] args = getArgs();
		if (args==null || args.length==0) return false;
		Configuration configuration=null;
		File file = new File(args[0]);
		if (Validator.isNotFile(file)) {
			System.err.println("Configuration file not found:[" + file.getAbsolutePath() + "]");
			configuration = createConfiguration();
			System.out.println("Writing default configuration file:[" +  file.getAbsolutePath() + "]");
			writeConfiguration(file, configuration);
		}
		System.out.println("Loading configuration file:[" + file.getAbsolutePath() + "]");
		configuration = readConfiguration(file);
		if (configuration==null) {
			System.err.println("Configuration file is null, file:[" + file.getAbsolutePath() + "]");
			return false;
		}
		if (!validateConfiguration(configuration)) return false;
		setConfigurationFile(file);
		setConfiguration(configuration);
		return true;
	}
	
	private void writeConfiguration(File file, Configuration conf) {
		try {
			Configuration.writeConfiguration(file, conf);
		} catch (IOException e) {
			logger.error("writeConfiguration() exception " + e.getMessage()); 
		}
	}
	
	private Configuration readConfiguration(File file) {
		try {
			return Configuration.readConfiguration(file);
		} catch (IOException e) {
			logger.error("readConfiguration() exception " + e.getMessage()); 
			return null;
		}
	}

	public void errorApplicationUsage() {
		System.out.println("Application needs configuration file as parameter, check the configuration file");
	}
	protected Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public File getConfigurationFile() {
		return configurationFile;
	}
	public void setConfigurationFile(File configurationFile) {
		this.configurationFile = configurationFile;
	}
	public Configuration readNewConfiguration() {
		return readConfiguration(configurationFile);
	}
}
