package ddc.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TaskExecutor implements Runnable {
	private final static Logger logger = Logger.getLogger(TaskExecutor.class);
	private TaskContext ctx = new TaskContext();
	private TaskSchema schema = null;
	private List<TaskInstance> executedTask = new ArrayList<>();
	
	public TaskExecutor(TaskSchema schema) {
		super();
		this.schema = schema;
	}
	
	public TaskExecutor(TaskSchema schema, Object contextValue) {
		super();
		this.schema = schema;
		this.ctx.set(contextValue);
	}
	
	public TaskExecutor(TaskSchema schema, Object[] contextValueList) {
		super();
		this.schema = schema;
		for (Object obj : contextValueList)
			this.ctx.set(obj);
	}
	
	public TaskContext getContext() {
		return ctx;
	}

	public Object getContextValue(Class<?> clazz) {
		return ctx.get(clazz);
	}

	public Object getContextParam(String name) {
		return ctx.getParam(name);
	}
	
	private final static String INFO = "executing - ";
	
	@Override
	public void run() {
		logger.info(INFO + "\n" + schema.toSchemaString());
		doRun(schema);
		logger.info(INFO + "\n" + toExecutedTaskString());
		if (hasFailedTask()) {
			logger.error(INFO + "Task failed - " + getFailedTask().toString());
		} else {
			logger.info(INFO + "All tasks are completed successfully");
		}
	}
	
	private void doRun(TaskSchema node) {
		if (node == null) {
			return;
		}
		String info = INFO + "task:["+ node.getName() + "]";
		logger.info(info + " - starting...");
		TaskInstance t = null;
		try {
			t = TaskInstance.create(node.getTaskClass(), ctx);
			executedTask.add(t);
			t.start();
			t.getInstance().run();
			t.terminateAsSucceeded();
			logger.info(info + " - terminated");			
			doRun(node.getOnSuccess());
		} catch (Throwable e) {			
			info = INFO + " - task:["+ node.getName() + "] error:[" + e.getMessage() + "]"; 
			logger.error(info);
			getContext().setException(e);
			if (t!=null) t.terminatedAsFailed(e);
			doRun(node.getOnFail());
		}
	}

	public String toExecutedTaskString() {
		StringBuilder b = new StringBuilder();
		for (TaskInstance t : executedTask) {
			b.append(" > " + t.getInstance().getClass().getSimpleName() + "[" + t.getTaskInfo().getExitCode() + "]");
		}
		return b.toString();
	}
	
	public List<TaskInstance> getExecutedTask() {
		return executedTask;
	}

	public boolean hasFailedTask() {
		for (TaskInstance t : executedTask) {
			if (t.isNotTerminatedAsSucceeded()) return true;
		}
		return false;
	}
	
	public TaskInstance getFailedTask() {
		for (TaskInstance t : executedTask) {
			if (t.isFailed()) return t;
		}
		return null;
	}

}
