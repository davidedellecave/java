package ddc.ftp;

import java.nio.file.Path;

import ddc.util.DateUtil;

public class FtpFileWrapper {
	private Path remotePath = null;
	private long timestamp = 0;
	private long size = 0;
	

	public Path getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(Path remotePath) {
		this.remotePath = remotePath;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "file:[" + remotePath.toString() + "] size:[" + size + "] date:[" + DateUtil.formatISO(timestamp) + "]"; 
	}

}
