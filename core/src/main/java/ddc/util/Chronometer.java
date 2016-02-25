package ddc.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Chronometer {
	private long startFirstTime=getNowMillis();
    private long startTime=-1;
    private long endTime=-1;        
    private long countdown=0;
    private long countdownCounter=0;
    
    public Chronometer() {
    	start();
    }
 
    public void reset() {
    	startTime=-1;
    	endTime=-1;
    }

    public Chronometer(long countdown) {
    	this.countdown=countdown;
    	start();
    }
    
    public Chronometer(long countdown, TimeUnit unit) {    	
    	this.countdown=(new Timespan(countdown, unit)).getMillis();
    	start();
    }

    public long start() {
        startTime=getNowMillis();
        endTime=-1;
        return startTime;
    }
    
    public void setCountdownMillis(long countdown) {
		this.countdown = countdown;
	}
    
    /**
     * Return true if countdown is passed one time (from start)
     * The chronometer is not stopped
     * @return
     * @see isCountdownCycle to ask more than one time 
     */
    public boolean isCountdownOver() {
    	if (getElapsed()>=countdown) {
    		return true;
    	}
    	return false;
	}
    
    /**
     * Return true if countdown period is passed from last call
     * @param millis
     * @return
     */
    public boolean isCountdownCycle() {    
        long times = getCountdownCycle();
        if (times>countdownCounter) {
            countdownCounter=times;
            return true;
        }
        return false;
    }
    
    /**
     * Return how many times the countdown period is passed
     * @return
     */
    public long getCountdownCycle() {    
    	return (long)(getElapsed() / countdown);
    }
    
    /**
     * Return the millis elapsed
     * @return
     */
    public long getElapsed() {
    	if (startTime==-1) return 0; 
    	//end time is -1 when the chron is not stopped
    	if (endTime==-1) return getNowMillis()-startTime;
        return endTime-startTime;
    }
    
    public long getElapsed(int index) {
    	if (0<=index && index<=partialCounter) {
    		return partial[index];
    	} else {
    		return getElapsed();
    	}
    }
      
    private final static int CAPACITY = 10;
    long[] partial = new long[CAPACITY];
    int partialCounter = 0;
    public long stop() {
    	endTime = getNowMillis();
    	long elapsed = endTime-startTime;
    	if (partialCounter<CAPACITY) {
	    	partial[partialCounter]=elapsed;
	    	partialCounter++;
    	}
    	return elapsed;
    }
    
    
    /**
     * Get elapsed time from chronometer is created, without consider start/stop
     * @return
     */
    public long getTotalElapsed() {
    	return getNowMillis()-startFirstTime;
    }
	
    public String toString() {
    	return Timespan.getHumanReadable(getElapsed());
    }

    
    public static long getNowMillis() {
        return System.currentTimeMillis();
    }

	public long getStartTime() {
		return startTime;
	}

	public Date getStartDate() {
		return new Date(startTime);
	}
	
	public Date getEndDate() {
		return new Date(endTime);
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public static void sleep(long millis) {
		if (millis<=0) return;
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println("sleep() Exception:[" + e.getMessage() + "]");
		}
	}
	
	public static void sleep(Timespan duration) {
		sleep(duration.getMillis());
	}
}