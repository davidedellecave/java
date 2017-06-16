package ddc.commons.ftp.matcher;

import ddc.commons.ftp.FtpFileMatcher;
import ddc.commons.ftp.FtpLiteFile;

public class RegexMatcher implements FtpFileMatcher {
	private String matcher;

	public RegexMatcher(String matcher) {
		super();
		this.matcher = matcher;
	}

	private boolean isMatched(String source) {
		return source.matches(matcher);
	}

	@Override
	public boolean accept(FtpLiteFile file) {
		return isMatched(file.getFilename());
	}

	@Override
	public String toString() {
		return " x.Name regexMatch (" + matcher.toString() + ")";
	}
}
