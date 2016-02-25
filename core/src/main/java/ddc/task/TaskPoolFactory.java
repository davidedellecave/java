package ddc.task;

import ddc.task.impl.ArgsValue;

public interface TaskPoolFactory {
	public TaskPool create(ArgsValue args) throws TaskException;
	public TaskPool onNext(TaskPool prevJobs) throws TaskException;
}
