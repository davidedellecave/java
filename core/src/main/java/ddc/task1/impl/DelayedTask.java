package ddc.task1.impl;

import org.apache.log4j.Logger;

import ddc.util.Timespan;

public class DelayedTask extends EncloseTask {
	private Logger logger = Logger.getLogger(DelayedTask.class);
	private SleepTask sleepTask;
	private long delayedMillis; 
	private boolean waitFirstAndRunAfter = true;
	
	public DelayedTask(Task task, long delayedMillis) {
		super(task);
		this.delayedMillis=delayedMillis;
	}

	public DelayedTask(Task task, long delayedMillis, boolean waitFirstAndRunAfter) {
		super(task);
		this.delayedMillis=delayedMillis;
		this.waitFirstAndRunAfter=waitFirstAndRunAfter;
	}

	public void stopTask() {
		if (sleepTask!=null) sleepTask.stop();
		if (task!=null) task.stop();		
	}
	
	public void runTask() {
		logger.debug("DelayedTask. Delaying:[" + Timespan.getHumanReadable(delayedMillis) + "]");
		if (isStopRequested()) return;
		if (waitFirstAndRunAfter) {
			sleepTask = new SleepTask(delayedMillis);
			sleepTask.run();
			logger.debug("DelayedTask. After delayed run task:[" + task.getClass().getName() + "]");
			task.run();
		} else {
			logger.debug("DelayedTask. Run task:[" + task.getClass().getName() + "] and delaying...");
			task.run();
			sleepTask = new SleepTask(delayedMillis);
			sleepTask.run();
		}
		logger.debug("DelayedTask. Terminated");
		
	}
}
