package ddc.util;
/**
 * @author davidedc, 04/nov/2010
 */
public class DRange {
	public double lowerBound=0;
	public double upperBound=0;
	public boolean enableLowerBound=false;
	public boolean enableUpperBound=false;
	public boolean includeBound = false;
	
	public DRange() {
		this.enableLowerBound = false;
		this.enableUpperBound = false;
	}
	
	public DRange(double lowerBound, double upperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.enableLowerBound = true;
		this.enableUpperBound = true;
	}

	public DRange(double lowerBound, double upperBound, boolean includeBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.enableLowerBound = true;
		this.enableUpperBound = true;
		this.includeBound=includeBound;
	}

	public String toString() {
		StringBuilder info = new StringBuilder();
		String open ="(";
		String close =")";
		if (includeBound) {
			open = "[";
			close = "]";
		}
		if (enableLowerBound) 
			info.append(open + String.valueOf(lowerBound));
		else
			info.append(open + "-");		
		info.append(", ");
		if (enableUpperBound) 
			info.append(String.valueOf(upperBound) + close);
		else
			info.append("-" + close );
		return info.toString();
	}

	public boolean isInner(Double d) {		
		if (includeBound) {
			if (!enableLowerBound && enableUpperBound) return (d<=upperBound);
			if (enableLowerBound && !enableUpperBound) return (lowerBound<=d);
			return (lowerBound<=d && d<=upperBound);			
		}
		else {
			if (!enableLowerBound && enableUpperBound) return (d<upperBound);
			if (enableLowerBound && !enableUpperBound) return (lowerBound<d);
			return (lowerBound<d && d<upperBound);
		}
	}
	
//	public boolean isOuter(Double d) {		
//		if (includeBound) {
//			if (!enableLowerBound && enableUpperBound) return (d<=upperBound);
//			if (enableLowerBound && !enableUpperBound) return (lowerBound<=d);
//			return (lowerBound<=d && d<=upperBound);			
//		}
//		else {
//			if (!enableLowerBound && enableUpperBound) return (d<upperBound);
//			if (enableLowerBound && !enableUpperBound) return (lowerBound<d);
//			return (lowerBound<d && d<upperBound);
//		}
//	}
}
