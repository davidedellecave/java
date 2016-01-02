package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteFile;

public class NotMatcher implements FtpFileMatcher {
	private FtpFileMatcher matcher;
	

	public NotMatcher(FtpFileMatcher matcher) {
		super();
		this.matcher = matcher;
	}

	@Override
	public boolean accept(FtpLiteFile file) {		
		return !matcher.accept(file);
	}

	@Override
	public String toString() {
		return " NOT (" + matcher.toString() + ")"; 
	}
}
