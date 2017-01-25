package ddc.task;

import java.util.List;
import java.util.UUID;

import ddc.util.Chronometer;
import ddc.util.Statistics;

/**
 * @author davidedc 2013
 */
public class TaskInfo {
	private String name = null;
	private UUID uuid = UUID.randomUUID();
	private UUID parentUuid = null;
	private TaskExitCode exitCode = TaskExitCode.Unknown;
	private TaskStatus status = TaskStatus.Ready;
	private Throwable exception = null;
	private Statistics stats = new Statistics();
	private List<TaskInfo> subTasks = null;

	public TaskInfo() {
	}

	public TaskInfo(String name) {
		this.name = name;
	}

	public TaskInfo incItemProcessed() {
		stats.itemsProcessed++;
		return this;

	}

	public TaskInfo incItemProcessed(int items) {
		stats.itemsProcessed += items;
		return this;
	}

	public TaskInfo incItemFailed(int items) {
		stats.itemsFailed += items;
		return this;
	}

	public TaskInfo incItemFailed() {
		stats.itemsFailed++;
		return this;

	}

	public TaskInfo incItemAffected() {
		stats.itemsAffected++;
		return this;
	}

	public TaskInfo incItemAffected(int items) {
		stats.itemsAffected += items;
		return this;
	}

	public TaskInfo setAsStart() {
		this.setExitCode(TaskExitCode.Unknown);
		this.setStatus(TaskStatus.Running);
		this.setException(null);
		this.getStats().chron.start();
		return this;
	}

	public TaskInfo terminatedAsFailed(Throwable e) {
		this.setExitCode(TaskExitCode.Failed);
		this.setStatus(TaskStatus.Terminated);
		this.setException(e);
		this.getStats().chron.stop();
		return this;
	}

	public TaskInfo terminatedAsFailed() {
		return terminatedAsFailed(null);
	}

	public TaskInfo terminateAsSucceeded() {
		this.setExitCode(TaskExitCode.Succeeded);
		this.setStatus(TaskStatus.Terminated);
		this.setException(null);
		this.getStats().chron.stop();
		return this;
	}

	public boolean isTerminated() {
		return this.getStatus().equals(TaskStatus.Terminated);
	}

	public boolean isTerminatedAsSucceeded() {
		return this.getExitCode().equals(TaskExitCode.Succeeded) && this.getStatus().equals(TaskStatus.Terminated);
	}

	public boolean isFailed() {
		return this.getExitCode().equals(TaskExitCode.Failed);
	}

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

	public Statistics getStats() {
		return stats;
	}

	public void setStats(Statistics stats) {
		this.stats = stats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public long getSubTasksSucceeded() {
		return subTasks.stream().filter(x -> x.isTerminatedAsSucceeded()).count();
	}

	public long getSubTasksFailed() {
		return subTasks.stream().filter(x -> x.isFailed()).count();
	}

	public List<TaskInfo> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<TaskInfo> subTasks) {
		this.subTasks = subTasks;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(name != null ? "name:[" + name + "] " : "");
		b.append("status:[" + status.toString() + "] ");
		b.append("exitCode:[" + exitCode.toString() + "] ");
		b.append(stats != null ? "stats:[" + stats.toString() + "] " : "");
		b.append(exception != null ? "exception:[" + exception.getMessage() + "] " : "");
		b.append(uuid != null ? "uuid:[" + uuid.toString() + "] " : "");
		b.append(parentUuid != null ? "parentUuid:[" + parentUuid.toString() + "] " : "");
		return b.toString();
	}

	public String toTinyString() {
		StringBuffer b = new StringBuffer();
		b.append("status:[" + status.toString() + "] ");
		b.append("exitCode:[" + exitCode.toString() + "] ");
		b.append((stats != null && stats.chron != null) ? "elapsed:[" + stats.chron.toString() + "] " : "");
		b.append((stats != null && stats.bytesProcessed != 0) ? "bytesProcessed:[" + stats.getProcessedHumanReadable() + "] " : "");
		b.append(exception != null ? "exception:[" + exception.getMessage() + "] " : "");
		return b.toString();
	}

	public String toPrettyString() {
		StringBuffer b = new StringBuffer();
		b.append(name != null ? "\n name:[" + name + "] " : "");
		b.append("\n\t status:[" + status.toString() + "] ");
		b.append("\n\t exitCode:[" + exitCode.toString() + "] ");
		b.append(stats != null ? "\n\t stats:[" + stats.toString() + "] " : "");
		b.append(exception != null ? "\n\t exception:[" + exception.getMessage() + "] " : "");
		b.append(uuid != null ? "\n\t uuid:[" + uuid.toString() + "] " : "");
		b.append(parentUuid != null ? "\n\t parentUuid:[" + parentUuid.toString() + "] " : "");
		return b.toString();
	}

	public static TaskInfo aggregate(String name, List<TaskInfo> taskList) {
		long minStartTime = Chronometer.getNowMillis();
		long maxEndTime = Chronometer.get1January1970Millis();
		
		TaskInfo res = new TaskInfo(name + " #" + taskList.size());
		res.setExitCode(TaskExitCode.Succeeded);
		for (TaskInfo ti : taskList) {
			//Set only first exception
			if (!res.isFailed() && ti.isFailed()) {
				res.setExitCode(TaskExitCode.Failed);
				res.setException(ti.getException());
			}
			minStartTime = ti.getStats().chron.getStartTime()<minStartTime ? minStartTime=ti.getStats().chron.getStartTime() : minStartTime;
			maxEndTime = ti.getStats().chron.getEndTime()>maxEndTime ? maxEndTime=ti.getStats().chron.getEndTime() : maxEndTime;
			res.getStats().bytesProcessed += ti.getStats().bytesProcessed;
			res.getStats().itemsProcessed += ti.getStats().itemsProcessed;
			res.getStats().itemsFailed += ti.getStats().itemsFailed;
			res.getStats().itemsAffected += ti.getStats().itemsAffected;
		}
		Chronometer c = new Chronometer(minStartTime, maxEndTime);
		res.stats.chron=c;
		res.setStatus(TaskStatus.Terminated);		
		return res;
	}
}
