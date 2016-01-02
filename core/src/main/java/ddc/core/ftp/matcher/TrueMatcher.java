package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileWrapper;

public class TrueMatcher implements FtpFileMatcher {

	@Override
	public boolean isMatched(FtpFileWrapper file) {
		return true;
	}

	@Override
	public String toString() {
		return " IS TRUE "; 
	}
}
