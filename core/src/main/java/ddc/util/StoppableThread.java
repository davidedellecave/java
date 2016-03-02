package ddc.util;

public interface StoppableThread extends Runnable {
	public void stop();
	public boolean isStopRequested();
}
