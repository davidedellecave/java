package ddc.util;

import java.util.ArrayList;
import java.util.List;

public class LRange {
    private long lower=0;
    private long upper=0;
    public static final LRange EMPTY = new LRange(0,0);

    public LRange(long lbound, long ubound) {
        this.lower = lbound;
        this.upper = ubound;
        normalizeBound();
    }

    public static LRange create(long lbound, long ubound) {
        return new LRange(lbound, ubound);
    }
    
    public long getLower() {
		return lower;
	}

	public long getUpper() {
		return upper;
	}

    public LRange clone() {
        return new LRange(lower, upper);
    }
    
    public void add(long quantity) {
    	this.lower+=quantity;
    	this.upper+=quantity;
    }
    
    public void power(long quantity) {
    	this.lower*=quantity;
    	this.upper*=quantity;
    }

    public static List<LRange> emptyList() {
        return new ArrayList<LRange>();
    }
    
	public static List<LRange> list(long[] list) throws Exception {
        if (list.length == 0) return LRange.emptyList();
        if (list.length % 2 != 0)
            throw new Exception("The number of arguments must be even count:[" + list.length + "]");
        List<LRange> rangelist = new ArrayList<LRange>();
        for (int i = 0; i < list.length; i+=2) {
            rangelist.add(LRange.create(list[i], list[i+1]));
        }
        return rangelist;
    }

    public static List<LRange> list(LRange[] list) {
        if (list.length == 0)
            return new ArrayList<LRange>();
        List<LRange> rangelist = new ArrayList<LRange>();
        for (int i = 0; i < list.length; i += 2) {
            rangelist.add(list[i].clone());
        }
        return rangelist;
    }

    public static List<LRange> clone(List<LRange> list) {
        List<LRange> l = new ArrayList<LRange>();
        for (LRange r : list) {
            if (r.isNotEmpty()) {
                l.add(r.clone());
            }
        }
        return l;
    }


    
    public static List<LRange> cloneList(List<LRange> list) {
    	List<LRange> cloneList = new ArrayList<LRange>(list.size()); 
    	for (LRange r : list) {
    		cloneList.add(r.clone());
    	}
        return cloneList;
    }

//    / <summary>
    /// Two ranges are equal if they are empty or have equal lower and upper bound
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public boolean equals(LRange range) {
    	return (lower == range.lower && upper == range.upper);
    }

//    public override int GetHashCode() {
//        return HashBuilder.build(lower, upper);
//    }

    public long dimension() {
        return upper - lower;
    }

    public boolean isEmpty() {
        return (lower==upper);
    }

    public boolean isNotEmpty() {
        return (lower!=upper);
    }

    public boolean isValidBound(long lower, long upper) {
        return (lower <= upper);
    }

    public boolean isValidBoundAndNotEmpty(long lower, long upper) {
        return (lower <= upper && lower != upper);
    }

    public boolean contains(long p) {
        return lower <= p && p <= upper;
    }

    public boolean notContains(long p) {
        return !contains(p);
    }

    public boolean containsWithoutBounds(long p) {
        return lower < p && p < upper;
    }

    public boolean contains(LRange range) {
        return (lower <= range.lower && range.upper <= upper);
    }

    public boolean isOverlapped(LRange range) {
        return intersection(range).isNotEmpty();
    }

    /// <summary>
    /// Return connected if the distance is less or equals 1
    /// </summary>
    /// <param name="range"></param>
    /// <param name="thresholdDistance"></param>
    /// <returns></returns>
    public boolean isConnected(LRange range) {
        return isConnected(range, 1);
    }

    /// <summary>
    /// Return connected if the distance is less or equals thresholdDistance
    /// </summary>
    /// <param name="range"></param>
    /// <param name="thresholdDistance"></param>
    /// <returns></returns>
    public boolean isConnected(LRange range, long thresholdDistance) {
        LRange r = this.between(range);
        return (r.dimension() <= thresholdDistance);
    }

    /// <summary>
    /// Join two ranges collapsing the beetween interval
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public LRange joinConnected(LRange range) {
        if (isConnected(range))
            return join(range);
        return LRange.EMPTY;
    }

    /// <summary>
    /// Join two ranges collapsing the beetween interval
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public LRange joinConnected(LRange range, long thresholdDistance) {
        if (isConnected(range, thresholdDistance))
            return join(range);
        return LRange.EMPTY;
    }


    public static List<LRange> complement(List<LRange> list) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (LRange item : list) {
            min = item.lower < min ? item.lower : min;
            max = item.upper > max ? item.upper : max;
        }
        LRange mask = new LRange(min, max);
        return complement(mask, list);
    }

    public static List<LRange> complement(LRange mask, List<LRange> rangeList2) {
        List<LRange> list = new ArrayList<LRange>();
        if (rangeList2.size() == 0) {
            list.addAll(LRange.EMPTY.complement(mask));
        }
        List<LRange> currentMask = new ArrayList<LRange>();
        currentMask.add(mask);
        for (LRange r : rangeList2) {
            list.clear();
            for (LRange r2 : currentMask) {
                list.addAll(r.complement(r2));                 
            }
            currentMask.clear();
            currentMask = LRange.cloneList(list);
            //list.ForEach(a => currentMask.Add(a.clone()));   
        }
        return list;
    }

    public List<LRange> complement(LRange mask) {
        List<LRange> result = new ArrayList<LRange>();
        if (intersection(mask).isEmpty()) {
            result.add(mask.clone());
            return result;
        }
        if (mask.equals(this)) {
            result.add(LRange.EMPTY);
            return result;
        }
        LRange cutRange = cut(mask);
        LRange r1 = LRange.EMPTY;
        LRange r2 = LRange.EMPTY;
        if (mask.lower<cutRange.lower) {
            r1 = LRange.create(mask.lower, cutRange.lower-1);
        }
        if (mask.upper>cutRange.upper) {
            r2 = LRange.create(cutRange.upper + 1, mask.upper);
        }
        if (r1.isNotEmpty()) result.add(r1);
        if (r2.isNotEmpty()) result.add(r2);
        return result;
    }


    public static List<LRange> difference(List<LRange> rangeList1, List<LRange> rangeList2) {
        List<LRange> list = new ArrayList<LRange>();
        if (rangeList2.size() == 0) {
            return rangeList1;
        }
        List<LRange> currentMask = new ArrayList<LRange>();
        currentMask.addAll(rangeList1);
        for (LRange r2 : rangeList2) {
            list.clear();
            for (LRange r1 : currentMask) {
                list.addAll(r1.difference(r2));
            }
            currentMask.clear();
            currentMask = LRange.cloneList(list);
            //list.ForEach(a => currentMask.Add(a.clone()));
        }
        return list;
    }

    /// <summary>
    /// Return the range minus the intersection between two ranges
    /// If the intersection is empty return the cloned range
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public List<LRange> difference(LRange range) {            
        List<LRange> result = new ArrayList<LRange>();
        LRange intersection = this.intersection(range);            
        if (intersection.isEmpty()) {
            result.add(this.clone());
            return result;
        }
        if (this.equals(intersection)) {
            return result;
        }
        if (isValidBoundAndNotEmpty(this.lower, intersection.lower - 1)) {
            result.add(new LRange(this.lower, intersection.lower - 1));
        }
        if (isValidBoundAndNotEmpty(intersection.upper + 1, this.upper)) {
            result.add(new LRange(intersection.upper + 1, this.upper));
        }            
        return result;
    }

    public static List<LRange> intersection(List<LRange> rangeList1, List<LRange> rangeList2) {
        List<LRange> result = new ArrayList<LRange>();
        for (LRange r1 : rangeList1) {
        	List<LRange>  list = r1.intersection(rangeList2);
            if (list.size() > 0)
                result.addAll(list);
        }
        return result;
    }

    public List<LRange> intersection(List<LRange> rangeList) {
        List<LRange> result = new ArrayList<LRange>();
        for (LRange r : rangeList) {
            LRange i = this.intersection(r);
            if (i.isNotEmpty()) result.add(i);
        }
        return result;
    }

    /// <summary>
    /// Return the intersection between two ranges
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public LRange intersection(LRange range) {
        long maxLower = max(range.lower, lower);
        long minUpper = min(range.upper, upper);
        if (maxLower - minUpper <= 0) {
            return new LRange(maxLower, minUpper);
        }
        return LRange.EMPTY;
    }

    /// <summary>
    /// Join two ranges collapsing the interval betweem them
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public LRange join(LRange range) {
        long l = min(range.lower, lower);
        long u = max(range.upper, upper);
        return new LRange(l, u);
    }

    /// <summary>
    /// Return the union of the range
    /// if the ranges is overlapped return only one united range
    /// otherwise return the two source ranges
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public List<LRange> union(LRange range) {
        List<LRange> list = new ArrayList<LRange>();
        if (isOverlapped(range)) {
            list.add(join(range));
        } else {
            list.add(clone());
            list.add(range.clone());
        }
        return list;
    }

    //Range between two ranges, it's empty if there is an intersection
    public LRange between(LRange range) {
        long maxLower = max(range.lower, lower);
        long minLower = min(range.upper, upper);
        if (minLower <= maxLower) {
            return new LRange(maxLower, minLower);
        }
        //else intersection
        return LRange.EMPTY;
    }

    public static List<LRange> split(List<LRange> list, long slice) {
        List<LRange> result = new ArrayList<LRange>();
        for (LRange r : list) {
            result.addAll(r.split(slice));
        }
        return result;
    }

    public List<LRange> split(long slice) {
        long size = (long) (this.dimension()+1) / slice;
        List<LRange> result = new ArrayList<LRange>();
        for (int i=0; i<size; i++) {
            long l = lower + (i*slice);
            result.add(LRange.create(l, l + slice - 1));
        }
        return result;
    }

    public static List<LRange> union(List<LRange> list) {
        int i = 0;
        while (i < list.size()) {
            int j = i + 1;
            while (j < list.size()) {
                boolean isOverlapped = list.get(i).isOverlapped(list.get(j));
                if (isOverlapped) {
                    list.set(i, list.get(i).union(list.get(j)).get(0));
                    list.remove(list.get(j));
                    i--;
                    break;
                }
                j++;
            }
            i++;
        }
        return list;
    }
    
    /// <summary>
    /// Join connected if the distance is less or equals zero
    /// </summary>
    /// <param name="list"></param>
    /// <returns></returns>
    public static List<LRange> joinConnected(List<LRange> list) {
        return joinConnected(list, 1);
    }

    public static List<LRange> joinConnected(List<LRange> list, long threshold) {
        int i = 0;
        while (i < list.size()) {
            int j = i + 1;
            while (j < list.size()) {
                LRange u = list.get(i).joinConnected(list.get(j), threshold);
                if (u.isNotEmpty()) {
                    list.set(i, u);
                    list.remove(list.get(j));
                    i--;
                    break;
                }
                j++;
            }
            i++;
        }
        return list;
    }

    /// <summary>
    /// Cut the lower and upper bound  based on input range
    /// the new lower is the max between the two lower
    /// the new upper is the min between the two upper
    /// </summary>
    /// <param name="range"></param>
    /// <returns></returns>
    public LRange cut(LRange range) {
        return LRange.create(max(lower, range.lower), min(upper, range.upper));
    }


    private long max(long p1, long p2) {
        return p1 > p2 ? p1 : p2;
    }

    private long min(long p1, long p2) {
        return p1 < p2 ? p1 : p2;
    }

    private void normalizeBound() {
        if (lower > upper) {
            long t = lower;
            lower = upper;
            upper = t;
        }
    }

    public String toString() {
        return "[" + lower + "-" + upper + "]";
    }

}
