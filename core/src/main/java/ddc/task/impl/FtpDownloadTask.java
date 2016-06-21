package ddc.task.impl;

import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLiteConfig;
import ddc.core.ftp.FtpLiteException;
import ddc.task.Task;
import ddc.task.TaskException;
import ddc.util.FilePair;

public class FtpDownloadTask extends Task {
	public final static String PARAMNAME_SERVERCONFIG="ftp.config.download";
	
	private void doRun() throws FtpLiteException {
		FtpLiteClient ftp = null;
		try {
			FtpLiteConfig ftpConf = (FtpLiteConfig)get(PARAMNAME_SERVERCONFIG);
			FileBag fileBag = (FileBag) get(FileBag.class);
			//
			ftp = new FtpLiteClient(ftpConf);
			ftp.connect();
			//
			for (FilePair fp : fileBag.fileToDownload) {
				ftp.download(fp.source, fp.target);
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
		} catch (Exception e) {
			throw new TaskException(e);
		}
	}
}