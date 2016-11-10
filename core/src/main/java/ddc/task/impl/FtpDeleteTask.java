package ddc.task.impl;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLiteConfig;
import ddc.core.ftp.FtpLiteException;
import ddc.task.Task;
import ddc.task.TaskException;

public class FtpDeleteTask extends Task {
	public final static String PARAMNAME_SERVERCONFIG="ftp.config.delete";
	private final static Logger logger = Logger.getLogger(FtpDeleteTask.class);

	private void doRun() throws FtpLiteException {
		FtpLiteClient ftp = null;
		try {
			FtpLiteConfig ftpConf = (FtpLiteConfig)get(PARAMNAME_SERVERCONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpLiteClient(ftpConf);
			ftp.connect();
			//
			String info = "Ftp delete - file #:[" + fileBag.remoteFileToDelete.size() + "]";
			if (fileBag.remoteFileToDelete.size() == 0) {
				logger.error(info);
				throw new FtpLiteException(info);
			} else {
				logger.info(info);
			}
			//
			for (Path fp : fileBag.remoteFileToDelete) {
				ftp.delete(fp, ftpConf.isDeleteRemoteLogically());
			}
		} finally {
			if (ftp!=null) ftp.disconnect();
		}
	}

	@Override
	public void run() {
		try {
			doRun();
		} catch (Throwable e) {
			throw new TaskException(e);
		}
	}

}
