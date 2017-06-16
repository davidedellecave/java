package ddc.commons.ftp.matcher;

import ddc.commons.ftp.FtpFileMatcher;
import ddc.commons.ftp.FtpLiteFile;

public class TrueMatcher implements FtpFileMatcher {

	@Override
	public boolean accept(FtpLiteFile file) {
		return true;
	}

	@Override
	public String toString() {
		return " IS TRUE "; 
	}
}
