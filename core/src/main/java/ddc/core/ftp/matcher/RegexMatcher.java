package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

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
	public boolean isMatched(FtpFileWrapper file) {
		return isMatched(file.getFilename());
	}

	@Override
	public String toString() {
		return " x.Name regexMatch (" + matcher.toString() + ")";
	}
}
