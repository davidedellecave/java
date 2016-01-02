package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

public class FilenameMatcher implements FtpFileMatcher {
	private String matcher;

	public FilenameMatcher(String matcher) {
		super();
		this.matcher = matcher;
	}

	@Override
	public boolean isMatched(FtpFileWrapper file) {
		String source = file.getFilename();
		return source.contains(matcher);
	}
	
	@Override
	public String toString() {
		return " x.Filename contains ('" + matcher.toString() + "')"; 
	}
}
