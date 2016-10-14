package ddc.util;

/**
 * @author davidedc
 */
public class LogUtils {
	
	public static void setLogManager(String className) {
		System.setProperty("java.util.logging.manager", className);
	}

	public static void setLog4JManager(String className) {
		setLog4JManager("org.apache.logging.log4j.jul.LogManager");
	}
}
