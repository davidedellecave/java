package ddc.bm.app;

import java.security.Principal;
import java.util.UUID;

import ddc.task.TaskExitCode;
import ddc.task.TaskStatus;
import ddc.util.Chronometer;

public abstract class AppTask<INPUT,OUTPUT> {
	private UUID id = UUID.randomUUID();
	private Principal principal = null;
	private TaskExitCode exitCode = TaskExitCode.Unknown;
	private TaskStatus status = TaskStatus.Ready;
	private Chronometer chron = new Chronometer();
	private Throwable exception = null;
	private OUTPUT result = null;
	
	protected abstract OUTPUT run(INPUT input) throws Throwable;
	
	public void execute(INPUT input) {		
		try {			
			chron.start();
			status=TaskStatus.Running;			
			result = run(input);
			exitCode=TaskExitCode.Succeeded;
		} catch(Throwable t) {
			exitCode=TaskExitCode.Failed;
			exception=t;
		} finally {
			status=TaskStatus.Terminated;
			chron.stop();
		}		
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TaskExitCode getExitCode() {
		return exitCode;
	}

	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Chronometer getChron() {
		return chron;
	}

	public void setChron(Chronometer chron) {
		this.chron = chron;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public OUTPUT getResult() {
		return result;
	}

	public void setResult(OUTPUT result) {
		this.result = result;
	}

	
}
