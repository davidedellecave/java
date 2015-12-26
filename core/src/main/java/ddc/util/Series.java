package ddc.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Series {	
	private String key = null;
	private List<Double> values = new ArrayList<Double>();
	
	
	public Series(String key) {
		this.key=key;
	}

	public String getKey() {
		return key;
	}
	
	public int getSize() {
		return values.size();
	}
	
	public List<Double> getValues() {
		return values;
	}
	
	public Series add(double value) {
		values.add(new Double(value));
		return this;
	}
	
	public SummaryStatistics getSummaryStatistics() {
		SummaryStatistics v = new SummaryStatistics();
		for (Double d : values) {
			v.addValue(d);			
		}
		return v;
	}
	
	public String toSummaryString() {
		StringBuffer buff = new StringBuffer();
		buff.append(key).append("={");
		SummaryStatistics s = getSummaryStatistics();
		buff.append(s).append(s.toString());
		buff.append("}");
		return buff.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(key).append("={");
		for (Double s : values) {
			buff.append(s).append(",");
		}
		if (values.size()>0) {
			buff.deleteCharAt(buff.length()-1);
		}
		buff.append("}");
		return buff.toString();
	}
}
