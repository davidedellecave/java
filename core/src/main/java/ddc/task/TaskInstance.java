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
		taskInfo.setExitCode(TaskExitCode.Unknown);
		taskInfo.setStatus(TaskStatus.Running);
		taskInfo.setException(null);
		taskInfo.getChron().start();
	}
	
	public void terminatedAsFailed(Throwable e) {
		taskInfo.setExitCode(TaskExitCode.Failed);
		taskInfo.setStatus(TaskStatus.Terminated);
		taskInfo.setException(e);
		taskInfo.getChron().stop();
	}
	
	public boolean isTerminated() {
		return  taskInfo.getStatus().equals(TaskStatus.Terminated);
	}
	
	public boolean isTerminatedAsSucceeded() {
		return 
		taskInfo.getExitCode().equals(TaskExitCode.Succeeded) &&
		taskInfo.getStatus().equals(TaskStatus.Terminated);
	}
	
	public boolean isNotTerminatedAsSucceeded() {
		return !isTerminatedAsSucceeded();
	}
	
	public void terminateAsSucceeded() {
		taskInfo.setExitCode(TaskExitCode.Succeeded);
		taskInfo.setStatus(TaskStatus.Terminated);
		taskInfo.setException(null);
		taskInfo.getChron().stop();
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
