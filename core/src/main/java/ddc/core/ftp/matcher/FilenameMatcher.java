package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteFile;

public class FilenameMatcher implements FtpFileMatcher {
	private String matcher;

	public FilenameMatcher(String matcher) {
		super();
		this.matcher = matcher;
	}

	@Override
	public boolean isMatched(FtpLiteFile file) {
		String source = file.getFilename();
		return source.contains(matcher);
	}
	
	@Override
	public String toString() {
		return " x.Filename contains ('" + matcher.toString() + "')"; 
	}
}
