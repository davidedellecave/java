package ddc.task1;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import ddc.util.Chronometer;

/**
 * @author davidedc 2014
 * 
 */
public class TaskInfo {
	private String name = "";
	private UUID uuid = UUID.randomUUID();
	private UUID parentUuid = null;
	private TaskExitCode exitCode = TaskExitCode.Unknown;
	private String exitMessage;
	private Long size= null;
	private Chronometer chron = new Chronometer();

	public String getName() {
		if (StringUtils.isBlank(name)) return this.getClass().getSimpleName();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(Class<?> clazz) {
		this.name = clazz.getSimpleName();
	}

	public TaskExitCode getExitCode() {
		return exitCode;
	}
	
	/**
	 * @return true if the status is exactly Failed
	 */
	public boolean isFailed() {
		return (exitCode.equals(TaskExitCode.Failed));
	}

	/**
	 * @return true if the status is not Failed, could be one of the other state 
	 */
	public boolean isNotFailed() {
		return (!exitCode.equals(TaskExitCode.Failed));
	}
	
	/**
	 * @return true if the status is not Succeeded, could be one of the other state 
	 */
	public boolean isNotSucceeded() {
		return (!exitCode.equals(TaskExitCode.Succeeded));
	}
	
	/**
	 * @return true if the status is exactly Succeeded 
	 */
	public boolean isSucceeded() {
		return (exitCode.equals(TaskExitCode.Succeeded));
	}

	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}

	public String getExitMessage() {
		return exitMessage;
	}

	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	public Chronometer getChron() {
		return chron;
	}
	
	public void startTask() {
		this.exitCode = TaskExitCode.Unknown;
		this.exitMessage = "";
		chron.start();
	}
	
	/**
	 * Stop chronometer and set exitCode 
	 */
	public void terminateAsFailed(Exception e) {
		this.exitCode = TaskExitCode.Failed;
		this.exitMessage = e.getMessage();
		chron.stop();
	}

	/**
	 * Stop chronometer and set exitCode 
	 */
	public void terminateAsFailed(String exitMessage) {
		this.exitCode = TaskExitCode.Failed;
		this.exitMessage = exitMessage;
		chron.stop();
	}

	/**
	 * Stop chronometer and set exitCode 
	 */
	public void terminateAsStopped(String exitMessage) {
		this.exitCode = TaskExitCode.Stopped;
		this.exitMessage = exitMessage;
		chron.stop();
	}

	/**
	 * Stop chronometer and set exitCode 
	 */
	public void terminateAsSucceeded() {
		this.exitCode = TaskExitCode.Succeeded;
		this.exitMessage = "";
		chron.stop();
	}
	
	/**
	 * Stop chronometer
	 */
	public void terminate() {
		chron.stop();
	}
	
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}

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

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(name != null ? "name:[" + name + "] " : "");
		b.append(uuid != null ? "uuid:[" + uuid.toString() + "] " : "");
		b.append(parentUuid != null ? "parentUuid:[" + parentUuid.toString() + "] " : "");
		b.append("exitCode:[" + exitCode.toString() + "] ");
		b.append(StringUtils.isNotBlank(exitMessage) ? "exitMessage:[" + exitMessage + "] " : "");
		b.append((chron != null && chron.getElapsed() > 0) ? "elapsedMillis:[" + chron.getElapsed() + "] " : "");
		b.append(size != null ? "size:[" + size + "] " : "");
		return b.toString();
	}
}
