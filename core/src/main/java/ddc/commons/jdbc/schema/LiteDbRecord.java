package ddc.commons.jdbc.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiteDbRecord<T> extends ArrayList<LiteDbField<T>> {
	private static final long serialVersionUID = -4667104921748808614L;

	public Map<String, T> toMap() {
		Map<String, T> map = new HashMap<>();
		for (LiteDbField<T> f : this) {
			map.put(f.getColumn().getName(), f.getValue());
		}
		return map;
	}

	public LiteDbField<T> get(String name) {
		for (LiteDbField<T> f : this) {
			if (f.getColumn().getName().equals(name))
				return f;
		}
		return null;
	}

	public T getValue(String name) {
		for (LiteDbField<T> f : this) {
			if (f.getColumn().getName().equals(name))
				return f.getValue();
		}
		return null;
	}
	
	public void setValue(String name, T value) {
		for (LiteDbField<T> f : this) {
			if (f.getColumn().getName().equals(name))
				f.setValue(value);
		}
	}	

	public boolean hasField(String name) {
		for (LiteDbField<T> f : this) {
			if (f.getColumn().getName().equals(name)) {
				return true;
			}				
		}
		return false;
	}

	
	public boolean isNotNullValue(String name) {
		return !isNullValue(name);
	}
	
	public boolean isNullValue(String name) {
		for (LiteDbField<T> f : this) {
			if (f.getColumn().getName().equals(name)) {
				return f.getValue()==null;
			}				
		}
		return true;
	}

}
