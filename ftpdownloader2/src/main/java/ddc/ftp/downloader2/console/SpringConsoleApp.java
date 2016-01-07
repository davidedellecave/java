package ddc.ftp.downloader2.console;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import ddc.console.AbstractConsoleApp;


/**
 * @author davide 2013
 *
 */
public abstract class SpringConsoleApp extends AbstractConsoleApp {
	private Logger logger = Logger.getLogger(SpringConsoleApp.class);
	private static GenericApplicationContext context;

	public GenericApplicationContext getContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see org.davidedc.console.AbstractConsoleApp#validateArgs()
	 */
	@Override
	public boolean validateArgs() {		
		String appContext = null;
		if (getArgs().length>0) {
			appContext = getArgs()[0];
		}
		if (appContext==null) return false;
		logger.info("AppContext file:[" + appContext + "]");		
		File ctxFile = new File(appContext);
		if (!ctxFile.exists()) {
			logger.error("AppContext file:[" + ctxFile.getAbsolutePath() + "] does not exist");
			return false;
		}
		context = getApplicationContext(ctxFile, false);
		return true;
	}
	
	private GenericApplicationContext getApplicationContext(File path, boolean validating) {		
		GenericXmlApplicationContext gac = new GenericXmlApplicationContext();
		gac.setValidating(validating);
		Resource res = new FileSystemResource(path);
		gac.load(res);
		gac.refresh();
		return gac;
	}

}
