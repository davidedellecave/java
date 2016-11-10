package ddc.task.impl;

import org.apache.log4j.Logger;

import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLiteConfig;
import ddc.core.ftp.FtpLiteException;
import ddc.task.Task;
import ddc.task.TaskException;
import ddc.util.FilePair;

public class FtpRenameTask extends Task {
	private final static Logger logger = Logger.getLogger(FtpRenameTask.class);
	public final static String PARAMNAME_CONFIG="ftp.config.rename";

	private void doRun() throws FtpLiteException {
		FtpLiteClient ftp = null;
		try {
			FtpLiteConfig ftpConf = (FtpLiteConfig) get(PARAMNAME_CONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpLiteClient(ftpConf);
			ftp.connect();
			//
			String info = "Ftp renaming - file #:[" + fileBag.remoteFileToRename.size() + "]";
			if (fileBag.remoteFileToRename.size() == 0) {
				logger.error(info);
				throw new FtpLiteException(info);
			} else {
				logger.info(info);
			}
			//
			for (FilePair fp : fileBag.remoteFileToRename) {
				ftp.rename(fp.source, fp.target);
			}
		} finally {
			if (ftp != null)
				ftp.disconnect();
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
