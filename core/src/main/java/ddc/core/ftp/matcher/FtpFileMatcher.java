package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;


public interface FtpFileMatcher {
	public boolean isMatched(FtpFileWrapper file);
}
