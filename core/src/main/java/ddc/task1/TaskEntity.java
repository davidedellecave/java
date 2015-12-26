package ddc.task1;

public interface TaskEntity {
	public TaskStatus getStatus();
	public TaskExitCode getExitCode();
	public String getErrorMessage();
}
