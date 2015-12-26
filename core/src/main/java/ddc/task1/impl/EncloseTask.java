package ddc.task1.impl;


public abstract class EncloseTask extends Task {

	protected Task task;

	public EncloseTask(Task task) {
		this.task=task;
	}

	@Override
	public abstract void runTask();

	@Override
	public abstract void stopTask();

}
