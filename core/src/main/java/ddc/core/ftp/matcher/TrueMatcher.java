package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteFile;

public class TrueMatcher implements FtpFileMatcher {

	@Override
	public boolean isMatched(FtpLiteFile file) {
		return true;
	}

	@Override
	public String toString() {
		return " IS TRUE "; 
	}
}
