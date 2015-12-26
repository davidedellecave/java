package ddc.service.impl;

import org.apache.log4j.Logger;

import ddc.service.Service;

/**
 * @author davidedc
 */
public class DummyService implements Service {
	private static final Logger logger = Logger.getLogger(DummyService.class);
	private boolean stopRequested = false;
	private final int WAIT_SECS=30;
			
	@Override
	public void run() {
		stopRequested=false;
		int loop = 0;
		while(!stopRequested) {	         
	         synchronized(this) {
	            try {
	               this.wait(WAIT_SECS*1000);
	            }
	            catch(InterruptedException ie){}
	         }
	         loop++;
	         logger.debug("Looping #:" + loop);
	      }
	}
	
	@Override
	public void stop() {
		stopRequested=true;
		synchronized(this) {
			this.notify();
		}
	}
}
