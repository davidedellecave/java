package ddc.service;

import org.apache.log4j.Logger;


/**
 * @author davidedc
 */
public class ServiceManager {
	private static final Logger logger = Logger.getLogger(ServiceManager.class);
	private ServiceFactory serviceFactory;
	private Service service;
	private boolean stopRequested=false;
	private boolean stopped=true;
	
	public ServiceManager(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	public ServiceManager(Service service) {
		this.service = service;
	}
	
	public void start() {
		try {
			doStart();
		} catch(RuntimeException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void stop() {
		try {
			doStop();
		} catch(RuntimeException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void doStart() {
		if (stopped) {		
			if (service==null) service = serviceFactory.createService();
			logger.info("Start service...");
			Thread t = new Thread(service);
			t.start();
			stopped=false;
			stopRequested=false;			
			logger.info("Started");
		} else {
			logger.info("Start requested but service is already started");
		}
	}
	
	private void doStop() {
		if (service==null || stopped || stopRequested) {
			logger.info("Stop requested but service is already stopped");
			return;
		}
		logger.info("Stop service...");
		stopRequested=true;
		service.stop();
		stopped=true;
		logger.info("Stopped");
	}
}
