package ddc.task1.impl;

import org.apache.log4j.Logger;

import ddc.util.Timespan;

public class SleepTask extends Task {
	private Logger logger = Logger.getLogger(SleepTask.class);
	private long sleepMillis=5*1000;
	private long microSleepMillis=1000;
	
	public SleepTask(long sleepMillis) {
		this.sleepMillis=sleepMillis;
	}
	
	public SleepTask(long sleepMillis, long microSleepMillis) {
		this.sleepMillis=sleepMillis;
		this.microSleepMillis=microSleepMillis;
	}
	
	public void runTask() {
		logger.debug("runTask() Sleep for :[" + Timespan.getHumanReadable(sleepMillis) + "] microSleep:[" + Timespan.getHumanReadable(microSleepMillis) + "]...");
		if (sleepMillis<=0) return;
		if (sleepMillis<=microSleepMillis) {
			if (!isStopRequested()) catchedSleep(sleepMillis);
			return;
		}
		long elapsed = 0;
		while (elapsed<sleepMillis && !isStopRequested()) {
			catchedSleep(microSleepMillis);
			elapsed += microSleepMillis;
		}
		logger.debug("runTask() Terminated after:[" + Timespan.getHumanReadable(elapsed) + "]");
	}

	@Override
	public void stopTask() {
		// TODO Auto-generated method stub		
	}
}
