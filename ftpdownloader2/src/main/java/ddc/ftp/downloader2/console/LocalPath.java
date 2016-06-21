package ddc.ftp.downloader2.console;

import ddc.util.FormatUtils;

public class LocalPath {
	//Local Path
	private String path="";
	private boolean includeHost=true;
	private boolean includeRemotePath=true;
	private boolean preserveRemoteTimestamp=true;
	private boolean skipFilIfEqSizeAndDate=false;

	public String getPath() {
		return path;
	}

	public void setPath(String localPathBase) {
		this.path = localPathBase;
	}

	public boolean isIncludeHost() {
		return includeHost;
	}

	public void setIncludeHost(boolean includeHost) {
		this.includeHost = includeHost;
	}

	public boolean isPreserveRemoteTimestamp() {
		return preserveRemoteTimestamp;
	}

	public void setPreserveRemoteTimestamp(
			boolean downloadFilePreserveTimestamp) {
		this.preserveRemoteTimestamp = downloadFilePreserveTimestamp;
	}

	public boolean isSkipFileIfEqSizeAndDate() {
		return skipFilIfEqSizeAndDate;
	}

	public void setSkipFilIfEqSizeAndDate(
			boolean skipFilIfEqSizeAndDate) {
		this.skipFilIfEqSizeAndDate = skipFilIfEqSizeAndDate;
	}

	public boolean isIncludeRemotePath() {
		return includeRemotePath;
	}

	public void setIncludeRemotePath(boolean includeRemotePath) {
		this.includeRemotePath = includeRemotePath;
	}
	
	public String toString() {
		return FormatUtils.format(this);
	}
	
}
