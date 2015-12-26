package ddc.util;

import java.util.ArrayList;

public class SeriesList extends ArrayList<Series> {
	private static final long serialVersionUID = 1L;
	
	public Series get(String key) {
		for (Series s : this) {
			if (s.getKey().equals(key)) return s;
		}
		return null;
	}
	
	public boolean union(SeriesList seriesList) {
		for (Series s : seriesList) {
			this.union(s);
		}
		return true;
	}
	
	/**
	 * Add new series, if series already exist add the values
	 * @param list
	 */
	private boolean union(Series series) {
		Series s = get(series.getKey());
		if (s==null) {
			super.add(series);
		} else {
			s.getValues().addAll(series.getValues());
		}
		return true;
	}
	
	
	public String toSummaryString() {
		StringBuffer buff = new StringBuffer();
		for (Series s : this) {
			buff.append(" ").append(s.toSummaryString());
		}
		return buff.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (Series s : this) {
			buff.append(" ").append(s.toString());
		}
		return buff.toString();
		
	}
}
