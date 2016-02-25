package ddc.task.impl;

import ddc.ftp.FtpClientWrapper;
import ddc.ftp.FtpConfigWrapper;
import ddc.ftp.FtpExceptionWrapper;
import ddc.task.Task;
import ddc.task.TaskException;
import ddc.util.FilePair;

public class FtpUploadTask extends Task {
	public final static String PARAMNAME_SERVERCONFIG="ftp.config.upload";
	private void doRun() throws FtpExceptionWrapper {
		FtpClientWrapper ftp = null;
		try {
			FtpConfigWrapper ftpConf = (FtpConfigWrapper) get(PARAMNAME_SERVERCONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpClientWrapper(ftpConf);
			ftp.login();
			//
			for (FilePair fp : fileBag.fileToUpload) {
				ftp.upload(fp.source, fp.target);
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