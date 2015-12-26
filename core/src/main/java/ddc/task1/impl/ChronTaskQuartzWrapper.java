package ddc.task1.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ChronTaskQuartzWrapper implements Job {

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		JobDataMap data = jobContext.getJobDetail().getJobDataMap();
		Task task = (Task)data.get("task");
		task.run();		
	}

}
