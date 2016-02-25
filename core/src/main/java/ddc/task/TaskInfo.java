package ddc.task;

import java.util.UUID;

import ddc.util.Chronometer;

/**
 * @author davidedc 2013
 */
public class TaskInfo {
	private UUID uuid = UUID.randomUUID();
	private UUID parentUuid = null;
	private TaskExitCode exitCode = TaskExitCode.Unknown;
	private TaskStatus status=TaskStatus.Ready;
	private Throwable exception = null;
	private Long size= null;
	private Chronometer chron = new Chronometer(){{reset();}};
	//
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public UUID getParentUuid() {
		return parentUuid;
	}
	public void setParentUuid(UUID parentUuid) {
		this.parentUuid = parentUuid;
	}
	public TaskExitCode getExitCode() {
		return exitCode;
	}
	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Chronometer getChron() {
		return chron;
	}
	public void setChron(Chronometer chron) {
		this.chron = chron;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("status:[" + status.toString() + "] ");
		b.append("exitCode:[" + exitCode.toString() + "] ");
		b.append((chron != null && chron.getElapsed() > 0) ? "elapsedMillis:[" + chron.getElapsed() + "] " : "");
		b.append(exception!=null ? "exception:[" + exception.getMessage() + "] " : "");
		b.append(uuid != null ? "uuid:[" + uuid.toString() + "] " : "");
		b.append(parentUuid != null ? "parentUuid:[" + parentUuid.toString() + "] " : "");
		b.append(size != null ? "size:[" + size + "] " : "");
		return b.toString();
	}
	
	public String toTinyString() {
		StringBuffer b = new StringBuffer();
		b.append("status:[" + status.toString() + "] ");
		b.append("exitCode:[" + exitCode.toString() + "] ");
		b.append((chron != null && chron.getElapsed() > 0) ? "elapsed:[" + chron.toString() + "] " : "");
		b.append(exception!=null ? "exception:[" + exception.getMessage() + "] " : "");
		b.append(size != null ? "size:[" + size + "] " : "");
		return b.toString();
	}
}
