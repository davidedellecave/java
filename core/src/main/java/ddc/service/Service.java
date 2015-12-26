package ddc.service;

/**
 * @author davidedc
 * Service is stoppable thread
 */
public interface Service extends Runnable {
	public void stop();
}
