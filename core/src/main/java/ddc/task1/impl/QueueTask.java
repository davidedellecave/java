package ddc.task1.impl;

import java.util.List;

import ddc.task1.StoppableThread;

public class QueueTask extends MultipleTask {	
	public QueueTask() {}
	
	@SuppressWarnings("unchecked")
	public QueueTask(List<? extends StoppableThread> taskList) {
		super.taskList=(List<StoppableThread>)taskList;
	}	
	
	
	@Override
	public void runTask() {
		for (StoppableThread s : taskList) {
			if (!isStopRequested()) {
				Thread t = new Thread(s);
				t.start();
				joinThread(t);				
			}
		}
	}

}
