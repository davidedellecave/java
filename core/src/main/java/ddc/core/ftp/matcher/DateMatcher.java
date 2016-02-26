package ddc.core.ftp.matcher;

import java.util.Date;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteFile;
import ddc.util.DateRange;
import ddc.util.DateUtil;

public class DateMatcher implements FtpFileMatcher {
	private DateRange matcher;

	public DateMatcher(DateRange matcher) {
		super();
		this.matcher = matcher;
	}
	
	public DateMatcher(String absoluteRange) {
		String[] t = absoluteRange.split(",");
		if (t.length==2) {
			Date d1 = DateUtil.parseDate(t[0].trim(), "yyyy/MM/dd HH:mm:ss");
			Date d2 = DateUtil.parseDate(t[1].trim(), "yyyy/MM/dd HH:mm:ss");
			matcher = new DateRange(d1, d2);		
		}
	}
		
	@Override
	public boolean accept(FtpLiteFile file) {		
		return matcher.contains(file.getTimestamp(), true);
	}
	
	@Override
	public String toString() {
		return " x.ModifiedDate IN (" + matcher.toString() + ")"; 
	}
}