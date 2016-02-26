package ddc.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class Field {
	private String name;
	private Object value;
	private int type;
	private boolean enabled=true;
	
	public Field() {}
	
	public Field(String name, Object value) {
		this.name=name;
		this.value=value;
	}

	public Field(String name, Object value, boolean enabled) {
		this.name=name;
		this.value=value;
		this.enabled=enabled;
	}
	
	public boolean isBlank() {
		return StringUtils.isBlank(String.valueOf(value));
	}
		
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
		

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}	
	public String stringValue() {
		if (value==null) return "";
		return String.valueOf(value);
	}
	
	public boolean booleanValue() {
		if (value==null) return false;
		return Boolean.parseBoolean(value.toString());
	}
	
//	public Date dateValue() {
//		if (value==null) return null;
//		return parse(value);
//	}
		
	public long longValue() {
		if (value==null) return 0;
		try {
			return Long.parseLong(String.valueOf(value));
		} catch (NumberFormatException e) {
			System.err.println("longValue() Exception:[" + e.getMessage() + "]");
			return 0;
		}
	}
	
	public float floatValue() {
		if (value==null) return 0;
		try {
			return Float.parseFloat(String.valueOf(value));
		} catch (NumberFormatException e) {
			System.err.println("floatValue() Exception:[" + e.getMessage() + "]");
			return 0;
		}
	}	
	
	public File fileValue() {
		if (value==null) return null;
		try {
			return new File(String.valueOf(value));
		} catch (NumberFormatException e) {
			System.err.println("fileValue() Exception:[" + e.getMessage() + "]");
			return null;
		}
	}	
	
	public StringBuffer stringBufferValue() {
		if (value==null) return new StringBuffer("");
		return new StringBuffer(String.valueOf(value));
	}
	
	public Field setValue(Object value) {
		this.value = value;
		return this;
	}
	
	public Field setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public Field appendString(String appendValue, String separator) {
		if (value==null) value="";
		this.setValue(value.toString() + separator + appendValue);
		return this;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	

	
	public String toString() {
		return " " + getName() + ":" + FormatUtils.format(getValue()) + " ";
	}
}
