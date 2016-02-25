package ddc.task;

import java.util.ArrayList;

public class TaskPool extends ArrayList<TaskExecutor> {
	private static final long serialVersionUID = 1L;
	private String id = "";
	private ExecutionType poolType=ExecutionType.Sequence;
	private int maxThreads = 1;
	//

	public enum ExecutionType {
		Concurrent, Sequence 
	}
	
	public TaskPool(String id, ExecutionType poolType) {
		this.id=id;
		this.poolType=poolType;
	}	
	
	public TaskPool(String id, ExecutionType poolType, int maxThreads) {
		this.id=id;
		this.poolType=poolType;
		this.maxThreads=maxThreads;
	}	
	
	//
	public ExecutionType getPoolType() {
		return poolType;
	}
	public void setPoolType(ExecutionType poolType) {
		this.poolType = poolType;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}