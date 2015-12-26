package ddc.task1;

import org.apache.log4j.Logger;

import ddc.util.Chronometer;

public class GcTask implements Runnable {
	private static Logger logger = Logger.getLogger(GcTask.class);
	private static int PERF_GC_TIMER_MILLIS = 1000*30;
	private static int PERF_GC_TIMESLEEP = 100;
	private static Chronometer perfChron = new Chronometer(PERF_GC_TIMER_MILLIS);
	
	@Override
	public void run() {
		if (perfChron.isCountdownCycle()) {
			logger.debug("GcTask() running period:[" + PERF_GC_TIMER_MILLIS + "] pauseMillis:[" + PERF_GC_TIMESLEEP + "]");
			System.gc();
			Chronometer.sleep(PERF_GC_TIMESLEEP);
			perfChron.start();
		}		
	}

	
}
