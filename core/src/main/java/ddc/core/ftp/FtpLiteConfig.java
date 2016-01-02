package ddc.core.ftp;


public class FtpLiteConfig {
	//Remote conf
	private FtpServer ftpServer = new FtpServer();
//	private int retryLogin=3;
	private boolean preserveRemoteTimestamp=true;
	private boolean binaryTransfer=true;
	private boolean passiveMode=true;
	private boolean overwriteLocal = false;
	private boolean overwriteRemote = false;
	private boolean remoteLogicalDelete= true;
	private String workingPath="/remote/path";
	//
	public FtpServer getFtpServer() {
		return ftpServer;
	}
	public void setFtpServer(FtpServer ftpServer) {
		this.ftpServer = ftpServer;
	}
//	public int getRetryLogin() {
//		return retryLogin;
//	}
//	public void setRetryLogin(int retryLogin) {
//		this.retryLogin = retryLogin;
//	}
	public boolean isPreserveRemoteTimestamp() {
		return preserveRemoteTimestamp;
	}
	public void setPreserveRemoteTimestamp(boolean preserveRemoteTimestamp) {
		this.preserveRemoteTimestamp = preserveRemoteTimestamp;
	}
	public boolean isBinaryTransfer() {
		return binaryTransfer;
	}
	public void setBinaryTransfer(boolean binaryTransfer) {
		this.binaryTransfer = binaryTransfer;
	}
	public boolean isPassiveMode() {
		return passiveMode;
	}
	public void setPassiveMode(boolean passiveMode) {
		this.passiveMode = passiveMode;
	}
	public boolean isOverwriteLocal() {
		return overwriteLocal;
	}
	public void setOverwriteLocal(boolean overwriteLocal) {
		this.overwriteLocal = overwriteLocal;
	}
	public boolean isOverwriteRemote() {
		return overwriteRemote;
	}
	public void setOverwriteRemote(boolean overwriteRemote) {
		this.overwriteRemote = overwriteRemote;
	}
	public boolean isRemoteLogicalDelete() {
		return remoteLogicalDelete;
	}
	public void setRemoteLogicalDelete(boolean remoteLogicalDelete) {
		this.remoteLogicalDelete = remoteLogicalDelete;
	}
	public String getWorkingPath() {
		return workingPath;
	}
	public void setWorkingPath(String workingPath) {
		this.workingPath = workingPath;
	}
}
