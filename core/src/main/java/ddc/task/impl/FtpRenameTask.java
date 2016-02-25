package ddc.task.impl;

import org.apache.log4j.Logger;

import ddc.ftp.FtpClientWrapper;
import ddc.ftp.FtpConfigWrapper;
import ddc.ftp.FtpExceptionWrapper;
import ddc.task.Task;
import ddc.task.TaskException;
import ddc.util.FilePair;

public class FtpRenameTask extends Task {
	private final static Logger logger = Logger.getLogger(FtpRenameTask.class);
	public final static String PARAMNAME_CONFIG="ftp.config.rename";

	private void doRun() throws FtpExceptionWrapper {
		FtpClientWrapper ftp = null;
		try {
			FtpConfigWrapper ftpConf = (FtpConfigWrapper) get(PARAMNAME_CONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpClientWrapper(ftpConf);
			ftp.login();
			//
			String info = "Ftp renaming - file #:[" + fileBag.remoteFileToRename.size() + "]";
			if (fileBag.remoteFileToRename.size() == 0) {
				logger.error(info);
				throw new FtpExceptionWrapper(info);
			} else {
				logger.info(info);
			}
			//
			for (FilePair fp : fileBag.remoteFileToRename) {
				ftp.rename(fp.source, fp.target);
			}
		} finally {
			if (ftp != null)
				ftp.logout();
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
