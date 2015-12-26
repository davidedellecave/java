package ddc.task1.impl;

import ddc.task1.TaskEntity;
import ddc.task1.TaskExitCode;
import ddc.task1.TaskStatus;

public class TaskResult implements TaskEntity {
	private TaskStatus status;
	private TaskExitCode exitCode;
	private String errorMessage;	
	
	public static TaskResult createSucceeded() {
		TaskResult t = new TaskResult();
		t.exitCode=TaskExitCode.Succeeded;
		t.errorMessage="";
		return t;
	}
	
	public static TaskResult createFailed(String errorMessage) {
		TaskResult t = new TaskResult();
		t.exitCode=TaskExitCode.Failed;
		t.errorMessage=errorMessage;
		return t;
	}
	
	public static TaskResult createStopped() {
		TaskResult t = new TaskResult();
		t.exitCode=TaskExitCode.Stopped;
		t.errorMessage="";
		return t;
	}
	
	public boolean isSucceeded() {
		return (exitCode.equals(TaskExitCode.Succeeded));
	}
	
	public boolean isFailed() {
		return (exitCode.equals(TaskExitCode.Failed));
	}

	@Override
	public TaskExitCode getExitCode() {
		return exitCode;
	}

	@Override
	public TaskStatus getStatus() {
		return status;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
