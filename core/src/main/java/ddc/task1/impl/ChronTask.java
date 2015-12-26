package ddc.task1.impl;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class ChronTask extends EncloseTask {
	private Logger logger = Logger.getLogger(ChronTask.class);

	private String  chronExpression ="0 0/1 * * * ?";
	
	public ChronTask(Task task, String chronExpression) {
		super(task);
		this.chronExpression=chronExpression;
		logger.debug("ChronExpression:[" + chronExpression + "]");
	}
	
	@Override
	public void runTask() {
		try {
			SchedulerFactory sf=new StdSchedulerFactory();
			Scheduler sched=sf.getScheduler();
			JobDetail jd=new JobDetail("jobChronTask","groupChronTask", ChronTaskQuartzWrapper.class);
			JobDataMap data = new JobDataMap();
			data.put("task", task);
			jd.setJobDataMap(data);
			
			CronTrigger ct=new CronTrigger("cronTrigger","group2ChronTask", chronExpression);
			
			sched.scheduleJob(jd,ct);
			sched.start();
		} catch (Exception e) {
			logger.error("Exception:[" + e + "]");
		}
		
	}

	@Override
	public void stopTask() {
		try {
			SchedulerFactory sf=new StdSchedulerFactory();
			Scheduler sched=sf.getScheduler();
			if (sched!=null) sched.shutdown();
		} catch (Exception e) {
			logger.error("Exception:[" + e + "]");
		}
		
	}
	
}
