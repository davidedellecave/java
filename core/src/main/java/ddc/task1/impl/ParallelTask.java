package ddc.task1.impl;

import java.util.ArrayList;
import java.util.List;

import ddc.task1.StoppableThread;
import ddc.util.Chronometer;

public class ParallelTask extends MultipleTask {
	public ParallelTask() {}
	
	@SuppressWarnings("unchecked")
	public ParallelTask(List<? extends Runnable> taskList) {
		super.taskList=(List<StoppableThread>)taskList;
	}	
	
	@Override
	public void runTask() {
		List<Thread> waterfall = new ArrayList<Thread>(taskList.size());
		for (StoppableThread s : taskList) {
			if (!isStopRequested()) {
				Thread t = new Thread(s);
				waterfall.add(t);
				t.start();
				Chronometer.sleep(100);
			}
		}
		joinThread(waterfall);
	}
}
