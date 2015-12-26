package ddc.task1.impl;

import java.util.ArrayList;
import java.util.List;

import ddc.task1.StoppableThread;


public abstract class MultipleTask extends Task {
	protected List<StoppableThread> taskList;

	@Override
	public void stopTask() {
		if (taskList!=null) {
			for (StoppableThread s :taskList) {
				s.stop();
			}
		}
	}
	
	public void setTasks(List<StoppableThread> taskList) {
		this.taskList=taskList;
		
	}
	public void addTask(StoppableThread t) {
		if (taskList==null) taskList = new ArrayList<StoppableThread>();
		taskList.add(t);
	}
	
	protected void joinThread(Thread t) {
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
		
	protected void joinThread(List<Thread> list) {
		try {
			for (Thread t : list) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
