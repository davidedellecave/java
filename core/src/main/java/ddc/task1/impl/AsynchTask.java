package ddc.task1.impl;

import org.apache.log4j.Logger;

import ddc.task1.TaskExitCode;
import ddc.task1.TaskStatus;

public class AsynchTask extends EncloseTask {
	private Logger logger = Logger.getLogger(AsynchTask.class);

	public AsynchTask(Task task) {
		super(task);
	}

	@Override
	public void run() {
		if (isRunning()) {
			logger.info("Task is already running");
			return;
		}		
		Runnable asynchRun = new Runnable() {
			public void run() {
				runTask();
			}
		};
		setStopRequested(false);
		Thread a = new Thread(asynchRun);
		a.start();
	}
		
	@Override
	public void runTask() {
		try {
			setStopRequested(false);
			logger.debug("run() task:[" + this.getClass().getName() + "] running...");
			notifyListener(this, TaskStatus.running, TaskExitCode.Unknown);
			if (getTaskListener()!=null) task.setTaskListener(getTaskListener());
			task.run();
			logger.debug("run() task:[" + this.getClass().getName() + "] terminated");
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Succeeded);
		} catch (RuntimeException e) {
			logger.error("run() Exception:" + e.getMessage());
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Failed);			
		}		
	}

	@Override
	public void stopTask() {
		task.stop();
	}
}
