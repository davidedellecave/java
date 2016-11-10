package ddc.task.impl;

import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLiteConfig;
import ddc.core.ftp.FtpLiteException;
import ddc.task.Task;
import ddc.task.TaskException;
import ddc.util.FilePair;

public class FtpUploadTask extends Task {
	public final static String PARAMNAME_SERVERCONFIG="ftp.config.upload";
	private void doRun() throws FtpLiteException {
		FtpLiteClient ftp = null;
		try {
			FtpLiteConfig ftpConf = (FtpLiteConfig) get(PARAMNAME_SERVERCONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpLiteClient(ftpConf);
			ftp.connect();
			//
			for (FilePair fp : fileBag.fileToUpload) {
				ftp.upload(fp.source, fp.target);
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