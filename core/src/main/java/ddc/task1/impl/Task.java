package ddc.task1.impl;

import static ddc.task1.TaskStatus.running;

import org.apache.log4j.Logger;

import ddc.task1.TaskEntity;
import ddc.task1.TaskExitCode;
import ddc.task1.TaskListener;
import ddc.task1.TaskStatus;
import ddc.util.Chronometer;
import ddc.util.StoppableThread;


public abstract class Task implements TaskEntity, StoppableThread {
	private Logger logger = Logger.getLogger(Task.class);
	private TaskExitCode exitCode = TaskExitCode.Unknown;
	private String errorMessage = null;
	private TaskStatus status = TaskStatus.ready;
	private boolean stopRequested = false;
	private TaskListener listener = null;	
	private Chronometer chron = new Chronometer();
	public abstract void runTask();
	public abstract void stopTask();

	@Override
	public boolean isStopRequested() {
		return stopRequested;
	}
	
	@Override
	public void stop() {
		logger.info("stop() task:[" + this.getClass().getName() + "] stopping...");
		try {
			setStopRequested(true);
			stopTask();
			chron.stop();
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Stopped);	
		} catch (RuntimeException e) {
			logger.error("stop() Exception:" + e.getMessage());
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Failed, e.getMessage());
		}
	}
	
	@Override
	public void run() {
		if (isRunning()) {
			logger.debug("run() task:[" + this.getClass().getName() + "] is already running");
			return;
		}
		try {
			chron.start();
			setStopRequested(false);
			logger.debug("run() task:[" + this.getClass().getName() + "] running...");
			notifyListener(this, running, TaskExitCode.Unknown);
			runTask();
			logger.debug("run() task:[" + this.getClass().getName() + "] terminated elapsed:[" + chron.getElapsed() + "] [" + chron.toString() + "]");
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Succeeded);
		} catch (RuntimeException e) {
			logger.error("run() Exception:" + e.getMessage());
			notifyListener(this, TaskStatus.terminated, TaskExitCode.Failed, e.getMessage());			
		}
	}
	@Override
	public TaskExitCode getExitCode() {
		return exitCode;
	}	

	@Override
	public TaskStatus getStatus() {
		return status;
	}
	
	private void setStatus(TaskStatus status) {
		this.status = status;
	}	
	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
	protected void setErrorMessage(String errorMessage) {
		this.errorMessage=errorMessage;
	}
	
	public boolean isRunning() {
		return getStatus().equals(TaskStatus.running);
	}

	public boolean isTerminated() {
		return getStatus().equals(TaskStatus.terminated);
	}

	public boolean isReady() {
		return getStatus().equals(TaskStatus.ready);
	}

	protected void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}
	
	protected TaskListener getTaskListener() {
		return listener;
	}
	public void setTaskListener(TaskListener listener) {
		this.listener = listener;
	}
	
	protected void setStopRequested(boolean stopRequested) {
		this.stopRequested = stopRequested;
	}
	
	protected void notifyListener(TaskEntity task, TaskStatus status, TaskExitCode exitCode) {
		notifyListener(task, status, exitCode, null);
	}
	protected void notifyListener(TaskEntity task, TaskStatus status, TaskExitCode exitCode, String errorMessage) {
		logger.info("Class:[" + task.getClass().getSimpleName() + "] Status:[" + status + "] exitCode:[" + exitCode +"] elapsed:[" + chron.getElapsed() + "] [" + chron.toString() + "]");
		setStatus(status);
		setExitCode(exitCode);
		setErrorMessage(errorMessage);
		if (getTaskListener()!=null) getTaskListener().notify(this);
	}
	
	protected void catchedSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			logger.error("catchedSleep:" + e.getMessage());
		}
	}
}
