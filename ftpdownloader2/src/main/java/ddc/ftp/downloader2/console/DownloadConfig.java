package ddc.ftp.downloader2.console;

import java.util.List;

import ddc.core.ftp.FtpLiteConfig;
import ddc.util.FormatUtils;

public class DownloadConfig {
	private int maxConnection = 1;
	private FtpLiteConfig ftpConfig = null;
	private List<RemotePath> remotePath = null;
	private LocalPath localPath;
	private boolean deleteRemoteEmptyFolder=false;

	public String toString() {
		return FormatUtils.toString(this);
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public List<RemotePath> getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(List<RemotePath> remotePath) {
		this.remotePath = remotePath;
	}

	public LocalPath getLocalPath() {
		return localPath;
	}

	public void setLocalPath(LocalPath localPath) {
		this.localPath = localPath;
	}

	public FtpLiteConfig getFtpConfig() {
		return ftpConfig;
	}

	public void setFtpConfig(FtpLiteConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}

	public boolean isDeleteRemoteEmptyFolder() {
		return deleteRemoteEmptyFolder;
	}

	public void setDeleteRemoteEmptyFolder(boolean deleteRemoteEmptyFolder) {
		this.deleteRemoteEmptyFolder = deleteRemoteEmptyFolder;
	}

}
