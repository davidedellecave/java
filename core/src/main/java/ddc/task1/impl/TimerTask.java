package ddc.task1.impl;

import org.apache.log4j.Logger;

import ddc.util.Timespan;

public class TimerTask extends EncloseTask {
	private Logger logger = Logger.getLogger(TimerTask.class);
	public enum TimerSequece {waitFirstAndRunAfter, runFirstAndWaitAfter};
	private long runEveryMillis; 
	private SleepTask sleepTask;
	private TimerSequece sequence = TimerSequece.waitFirstAndRunAfter;

	public TimerTask(Task task, long runEveryMillis) {
		super(task);
		this.runEveryMillis=runEveryMillis;
	}
	
	public TimerTask(Task task, long runEveryMillis, TimerSequece sequence) {
		super(task);
		this.runEveryMillis=runEveryMillis;
		this.sequence=sequence;
	}
	
	public void stopTask() {
		if (sleepTask!=null) sleepTask.stop();
		if (task!=null) task.stop();
	}
	
	@Override
	public void runTask() {
		sleepTask = new SleepTask(runEveryMillis);		
		while (!isStopRequested()) {
			if (sequence==TimerSequece.waitFirstAndRunAfter) {
				logger.debug("TimerTask. Wait and Run, every:[" + Timespan.getHumanReadable(runEveryMillis) + "]...");
				sleepTask.run();
				task.run();
			} else {
				logger.debug("TimerTask. Run and Wait, every:[" + Timespan.getHumanReadable(runEveryMillis) + "]...");
				task.run();
				sleepTask.run();
			}
		}
	}
}
