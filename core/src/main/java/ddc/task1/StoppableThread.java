package ddc.task1;

/**
 * It is a runnable object with stop method
 * @author davide
 *
 */
public interface StoppableThread extends Runnable {
	public void stop();
	public boolean isStopRequested();
}
