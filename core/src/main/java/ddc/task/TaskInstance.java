package ddc.task;

import java.util.UUID;

public class TaskInstance {
	private Task instance = null;
	private TaskInfo taskInfo = new TaskInfo();
	private Class<? extends Task> clazz = null;
	
	private TaskInstance() {}
	
	public static TaskInstance create(Class<? extends Task> clazz, TaskContext context) throws InstantiationException, IllegalAccessException {
		TaskInstance i = new TaskInstance();
		i.clazz=clazz; 		
		i.instance = clazz.newInstance();
		i.instance.setContext(context);
		return i;
	}

	public UUID getId() {
		return getTaskInfo().getUuid();
	}
	
	public void start() {
		taskInfo.setAsStart();
	}
	
	public void terminatedAsFailed(Throwable e) {
		taskInfo.terminatedAsFailed(e);
	}
	
	public boolean isTerminated() {
		return  taskInfo.getStatus().equals(TaskStatus.Terminated);
	}
	
	public boolean isTerminatedAsSucceeded() {
		return taskInfo.isTerminatedAsSucceeded();
	}
	
	public boolean isNotTerminatedAsSucceeded() {
		return !isTerminatedAsSucceeded();
	}
	
	public void terminateAsSucceeded() {
		taskInfo.terminateAsSucceeded();
	}
	
	
	//======== properties ========
	
	public Class<? extends Task> getClazz() {
		return clazz;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public void setClazz(Class<? extends Task> clazz) {
		this.clazz = clazz;
	}

	public Task getInstance() {
		return instance;
	}

	public void setInstance(Task instance) {
		this.instance = instance;
	}
	
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}
	
	public boolean isFailed() {
		return taskInfo.getExitCode().equals(TaskExitCode.Failed);
	}

	public boolean isSucceeded() {
		return taskInfo.getExitCode().equals(TaskExitCode.Succeeded);
	}
	
	public boolean isNotFailed() {
		return !isFailed();
	}

	public boolean isNotSucceeded() {
		return !isSucceeded();
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();		
		b.append(clazz != null ? "task:[" + clazz.getSimpleName() + "] " : "");
		b.append(getTaskInfo().toTinyString());
		return b.toString();
	}
}
