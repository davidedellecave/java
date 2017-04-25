package ddc.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationLocator {
	public static String DEFAULT_ENV_VARNAME = "INFOHUB_HOME";
	public static String DEFAULT_RELATIVE_PATH = "conf";
	
	static {
		String envPath = System.getenv().get(DEFAULT_ENV_VARNAME);
		if (envPath == null || envPath.length() == 0) {
			envPath = System.getProperty(DEFAULT_ENV_VARNAME);
		} else {
			System.setProperty(DEFAULT_ENV_VARNAME, envPath);
		}
//		System.out.println("Environment path:" + envPath );
		
	}

	public static Path getConfigurationFile(Class<?> clazz, String extension) throws ConfigurationException {		
		return getConfigurationFile(clazz, clazz.getSimpleName(), extension);
	}
	
	public static Path getConfigurationFile(Class<?> clazz, String filename, String extension) throws ConfigurationException {
		return getConfigurationFile(clazz.getPackage().getName(), filename, extension);
	}
	
	private static Path getConfigurationFile(String baseFolder, String filename, String extension) throws ConfigurationException {
		if (extension!=null && !extension.startsWith(".") && extension.length()>0) 
			extension="."+extension;
		if (extension==null)
			extension="";
		
		Path relative = Paths.get(DEFAULT_RELATIVE_PATH, baseFolder, filename + extension);
		return getConfigurationFile(DEFAULT_ENV_VARNAME, relative);
	}
		
	private static Path getConfigurationFile(String envPathName, Path relative) throws ConfigurationException {
		String envPath = System.getenv().get(envPathName);
		if (envPath == null || envPath.length() == 0) {
			envPath = System.getProperty(envPathName);
			if (envPath == null || envPath.length() == 0) {
				throw new ConfigurationException("Environment variable not found:[" + envPathName + "]");
			}
		}
		return Paths.get(envPath, relative.toString());
	} 
	
	
	public static Path getDefaultConfigFolder() throws ConfigurationException {
		return getConfigurationFile(DEFAULT_ENV_VARNAME, Paths.get(DEFAULT_RELATIVE_PATH));
	}
	
	
//	public static Path detectXmlConfFile(Class<?> clazz, String confPath) throws ConfigurationException {
//		if (StringUtils.isBlank(confPath)) {
//			return getConfigurationFile(clazz, ".conf.xml");
//		}		
//		Path path = Paths.get(confPath);		
//		if (Files.isDirectory(path)) {
//			return Paths.get(path.toString(), clazz.getSimpleName() + ".conf.xml");			
//		}		
//		return path;
//	}
}
