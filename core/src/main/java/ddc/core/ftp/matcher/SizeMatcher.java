package ddc.core.ftp.matcher;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteFile;
import ddc.util.LRange;

public class SizeMatcher implements FtpFileMatcher {
	private LRange matcher;

	public SizeMatcher(LRange matcher) {
		super();
		this.matcher = matcher;
	}
	
	public SizeMatcher(String absoluteRange) {
		String[] t = absoluteRange.split(",");
		if (t.length==2) { 
			long d1 = Long.parseLong(t[0].trim());
			long d2 = Long.parseLong(t[1].trim());			
			matcher = new LRange(d1, d2);
		}
	}
	
	private boolean isMatched(long size) {
		return matcher.contains(size);
	}

	@Override
	public boolean accept(FtpLiteFile file) {
		return isMatched(file.getSize());
	}
	
	@Override
	public String toString() {
		return " x.Size IN (" + matcher.toString() + ")"; 
	}
}
