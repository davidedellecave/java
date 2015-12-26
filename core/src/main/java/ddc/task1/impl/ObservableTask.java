package ddc.task1.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ddc.task1.TaskEntity;
import ddc.task1.TaskListener;

public class ObservableTask extends EncloseTask {	
	private Logger logger = Logger.getLogger(ObservableTask.class);
	private TaskListenerManager listenerManager = new TaskListenerManager();

	public ObservableTask(Task task) {
		super(task);
	}

	public void removeListener(TaskListener listener) {
		listenerManager.remove(listener);
	}
		
	public void addListener(TaskListener listener) {
		listenerManager.add(listener);
	}
	
	public void addListener(List<TaskListener> listenerList) {
		listenerManager.add(listenerList);	
	}
	
	@Override
	public void runTask() {
		task.setTaskListener(new Observer());
		task.run();
	}

	@Override
	public void stopTask() {
		task.stop();
	}
	
	class Observer implements TaskListener {
		@Override
		public void notify(TaskEntity task) {
			notifyListeners(task);		
		}
	}
	
	private void notifyListeners(TaskEntity task) {
		logger.debug("notifyListeners() Task:[" + task.getClass() + "] Status:[" + task.getStatus()+ "] exitCode:[" + task.getExitCode()+"] message:[" + task.getErrorMessage() + "]");
		if (listenerManager.hasValues()) {
			for (TaskListener l : listenerManager.getListeners()) {
				logger.debug("notifyListeners() firing to listener:[" + task.getClass() + "] Status:[" + task.getStatus()+ "] exitCode:[" + task.getExitCode()+"] message:[" + task.getErrorMessage() + "]");				
				l.notify(task);
			}
		}
	}
}
