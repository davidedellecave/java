package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

public class NotMatcher implements FtpFileMatcher {
	private FtpFileMatcher matcher;
	

	public NotMatcher(FtpFileMatcher matcher) {
		super();
		this.matcher = matcher;
	}

	@Override
	public boolean isMatched(FtpFileWrapper file) {		
		return !matcher.isMatched(file);
	}

	@Override
	public String toString() {
		return " NOT (" + matcher.toString() + ")"; 
	}
}
