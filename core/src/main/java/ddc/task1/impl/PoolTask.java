package ddc.task1.impl;

import java.util.ArrayList;
import java.util.List;

import ddc.util.StoppableThread;

public class PoolTask extends MultipleTask {
//	private int parallelExecution;
	
	public PoolTask() {}
	
	@SuppressWarnings("unchecked")
	public PoolTask(List<? extends StoppableThread> taskList) {
		super.taskList=(List<StoppableThread>)taskList;
	}	
	
	public PoolTask(final List<StoppableThread> taskList, final int parallelExecution) {
		this.taskList=taskList;
//		this.parallelExecution=parallelExecution;
	}
	
//	private List<Task> pool;
//	private List<Task> localCopy;
//	
	@Override
	public void runTask() {
		List<Thread> pool = new ArrayList<Thread>(taskList.size());
		for (StoppableThread s : taskList) {
			if (!isStopRequested()) {
				Thread t = new Thread(s);
				pool.add(t);
			}
		}		
		joinThread(pool);
	}

}
