package ddc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4jUtils {

	public static Logger createLogInstance(Class<?> clazz, Level level, String fileName) {
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
	
	public static void setAppenderFilename(Logger logger, String fileName) {
		Enumeration<?> enumAppenders = logger.getAllAppenders();
		if (!enumAppenders.hasMoreElements()) {
			FileAppender fa = new FileAppender();
			fa.setFile(fileName);
		} else {
			while (enumAppenders.hasMoreElements()) {
				Appender appender =(Appender )enumAppenders.nextElement();
				if (appender instanceof FileAppender) {
					((FileAppender)appender).setFile(fileName);				
				}
			}	
		}
	}
	
	public static void setAppenderFilename(String fileName) {
		Enumeration<?> enumAppenders = Logger.getRootLogger().getAllAppenders();
		while (enumAppenders.hasMoreElements()) {
			Appender appender =(Appender )enumAppenders.nextElement();
			if (appender instanceof FileAppender) {
				((FileAppender)appender).setFile(fileName);				
			}
		}
	}
	
	public static void writeExceptionStackTrace(Logger logger, Throwable exception, String message) {
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    exception.printStackTrace(pw);
	    logger.error(message + " " + pw.toString());
	}
	
}
