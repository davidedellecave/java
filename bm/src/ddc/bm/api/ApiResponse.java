package ddc.bm.api;

import ddc.task.TaskExitCode;

public class ApiResponse<T> {
	private String task = "";
    private TaskExitCode exitCode = TaskExitCode.Unknown;
    private long elapsedMillis = 0;
    private T result = null;
    
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public TaskExitCode getExitCode() {
		return exitCode;
	}
	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public long getElapsedMillis() {
		return elapsedMillis;
	}
	public void setElapsedMillis(long elapsedMillis) {
		this.elapsedMillis = elapsedMillis;
	}
    
}
