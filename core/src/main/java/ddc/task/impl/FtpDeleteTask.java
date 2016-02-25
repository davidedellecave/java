package ddc.task.impl;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import ddc.ftp.FtpClientWrapper;
import ddc.ftp.FtpConfigWrapper;
import ddc.ftp.FtpExceptionWrapper;
import ddc.task.Task;
import ddc.task.TaskException;

public class FtpDeleteTask extends Task {
	public final static String PARAMNAME_SERVERCONFIG="ftp.config.delete";
	private final static Logger logger = Logger.getLogger(FtpDeleteTask.class);

	private void doRun() throws FtpExceptionWrapper {
		FtpClientWrapper ftp = null;
		try {
			FtpConfigWrapper ftpConf = (FtpConfigWrapper)get(PARAMNAME_SERVERCONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpClientWrapper(ftpConf);
			ftp.login();
			//
			String info = "Ftp delete - file #:[" + fileBag.remoteFileToDelete.size() + "]";
			if (fileBag.remoteFileToDelete.size() == 0) {
				logger.error(info);
				throw new FtpExceptionWrapper(info);
			} else {
				logger.info(info);
			}
			//
			for (Path fp : fileBag.remoteFileToDelete) {
				ftp.delete(fp, ftpConf.isRemoteLogicalDelete());
			}
		} finally {
			if (ftp!=null) ftp.logout();
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
