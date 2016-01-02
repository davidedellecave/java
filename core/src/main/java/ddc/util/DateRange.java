package ddc.util;

import java.util.Date;

public class DateRange {
	public Date start;
	public Date end;
	
	
	
	public DateRange() {
		
	}

	public DateRange(long start, long end) {
		this.start=new Date(start);
		this.end=new Date(end);		
	}

	public DateRange(Date start, Date end) {
		this.start=start;
		this.end=end;		
	}
	
	public DateRange(LRange range) {
		this.start=new Date(range.getLower());
		this.end=new Date(range.getUpper());		
	}

	public boolean contains(Date date, boolean includeBound) {
		if (includeBound) {
			return start.getTime()<=date.getTime() && date.getTime()<=end.getTime();
		} else {
			return start.getTime()<date.getTime() && date.getTime()<end.getTime();
			
		}
	}

	public boolean contains(long timestamp, boolean includeBound) {
		return contains(new Date(timestamp), includeBound);
	}

	@Override
	public String toString() {
		return " x IN (" +   DateUtils.formatForISO(start) + "," + DateUtils.formatForISO(end) + ")"; 
	}
}
