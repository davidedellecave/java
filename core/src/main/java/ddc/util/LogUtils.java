package ddc.util;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;



/**
 * @author davidedc
 */
public class LogUtils {

	@SuppressWarnings("unchecked")
	public static Logger createLogInstance(Class clazz, Level level, String fileName) {
		Logger logger = Logger.getLogger(clazz);
		logger.removeAllAppenders();
		DailyRollingFileAppender appender = new DailyRollingFileAppender();
		appender.setName(clazz.toString());
		appender.setFile(fileName);
		appender.setThreshold(level);
		PatternLayout jobPattern = new PatternLayout();
		jobPattern.setConversionPattern("%d{dd-MM-yyyy HH:mm:ss} %p %C - %m%n");
//		jobPattern.setConversionPattern("%d{dd-MM-yyyy HH:mm:ss} %p %C.%M() - %m%n");
		appender.setLayout(jobPattern);
		//activate options before adding 
		appender.activateOptions();
		logger.addAppender(appender);
		return logger;
	}
}
