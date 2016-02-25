package ddc.task.impl;

import org.apache.log4j.Logger;

import ddc.email.LiteMail;
import ddc.email.LiteMailConfig;
import ddc.email.LiteMailException;
import ddc.task.Task;
import ddc.task.TaskException;

public class MailTask extends Task{
	private final static Logger logger = Logger.getLogger(MailTask.class);

	private void doRun() throws LiteMailException {
		logger.info("Sending mail...");
		LiteMailConfig conf = (LiteMailConfig) get(LiteMailConfig.class);
		LiteMail e = new LiteMail();
		e.send(conf);
	}

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}
}
