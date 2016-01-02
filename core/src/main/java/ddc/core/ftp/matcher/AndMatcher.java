package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

public class AndMatcher implements FtpFileMatcher {
	private FtpFileMatcher matcher1;
	private FtpFileMatcher matcher2;
	
	public AndMatcher(FtpFileMatcher matcher1, FtpFileMatcher matcher2) {
		super();
		this.matcher1 = matcher1;
		this.matcher2 = matcher2;
	}
	
	@Override
	public boolean isMatched(FtpFileWrapper file) {		
		return matcher1.isMatched(file) && matcher2.isMatched(file);
	}
	
	@Override
	public String toString() {
		return "((" + matcher1.toString() + ") AND (" + matcher2.toString() + "))";
	}
}
