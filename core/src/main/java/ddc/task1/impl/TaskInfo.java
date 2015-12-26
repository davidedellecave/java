package ddc.task1.impl;

import ddc.task1.TaskEntity;
import ddc.task1.TaskExitCode;
import ddc.task1.TaskStatus;

public class TaskInfo implements TaskEntity {
	private TaskStatus status;
	private TaskExitCode exitCode;
	private String errorMessage;	
	private long timestamp;
	private long elapsedMillis;
	private long size;
	
	@Override
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	@Override
	public TaskExitCode getExitCode() {
		return exitCode;
	}
	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getElapsedMillis() {
		return elapsedMillis;
	}
	public void setElapsedMillis(long elapsedMillis) {
		this.elapsedMillis = elapsedMillis;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
