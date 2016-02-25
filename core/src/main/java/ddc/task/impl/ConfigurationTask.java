package ddc.task.impl;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import ddc.config.ConfigurationException;
import ddc.config.ConfigurationLocator;
import ddc.config.XmlConfiguration;
import ddc.task.Task;
import ddc.task.TaskException;

public class ConfigurationTask extends Task {
	private final static Logger logger = Logger.getLogger(ConfigurationTask.class);

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}

	private void doRun() throws ConfigurationException {
		ArgsValue args = ((ArgsValue) getContext().get(ArgsValue.class));
//		String confFilename = args.getConfFilename();
		Class<?> confClass = args.getConfClass();	
		if (confClass==null)
			throw new ConfigurationException("Configuration class is expected");
//		if (confFilename==null)
//			throw new ConfigurationException("Configuration filename is expected");
		Path path = ConfigurationLocator.getConfigurationFile(confClass, "conf.xml");
		logger.info("Configuration file:[" + path.toString() + "]");
		if (!Files.exists(path)) {
			XmlConfiguration.write(path.toFile(), confClass);
			String error = "Configuration file not found - default file has been created:[" + path.toString() + "]";
			logger.error(error);
			throw new ConfigurationException(error);
		}
		Object v = XmlConfiguration.read(path.toFile());
		getContext().set(v);
	}

}
