package ddc.task.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import ddc.task.Task;
import ddc.task.TaskException;

public class DeleteFileOnSuccessTask extends Task {
	private final static Logger logger = Logger.getLogger(DeleteFileOnSuccessTask.class);

	private void doRun() throws IOException {
		FileBag fileBag = (FileBag) get(FileBag.class);

		for (Path p : fileBag.fileToDeleteOnSuccess) {
			if (Files.exists(p)) {
				logger.info("Deleting ... - file:[" + p + "]");				
				Files.delete(p);
				logger.info("Deleting ok - file:[" + p + "]");
			}
		}
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