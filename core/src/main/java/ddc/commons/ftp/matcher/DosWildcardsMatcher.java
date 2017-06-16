package ddc.commons.ftp.matcher;

import ddc.commons.ftp.FtpFileMatcher;
import ddc.commons.ftp.FtpLiteFile;

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
	public boolean accept(FtpLiteFile file) {
		return isMatched(file.getFilename());
	}
	
	@Override
	public String toString() {
		return " x.Name DosWildcardsMatch (" + matcher.toString() + ")"; 
	}
}
