package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

public class DosWildcardsMatcher implements FtpFileMatcher {
	private String matcher;

	public DosWildcardsMatcher(String matcher) {
		super();
		this.matcher = matcher;
	}

	private boolean isMatched(String source) {
		String rx =  ddc.util.StringMatcher.wildcardToRegex(matcher);
		return source.matches(rx);
	}

	@Override
	public boolean isMatched(FtpFileWrapper file) {
		return isMatched(file.getFilename());
	}
	
	@Override
	public String toString() {
		return " x.Name DosWildcardsMatch (" + matcher.toString() + ")"; 
	}
}
