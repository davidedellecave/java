package ddc.util;

import org.apache.commons.lang3.StringUtils;

public class FieldType<T> {
	private String name;
	private T value;
	private boolean enabled=true;
	
	public FieldType() {}
	
	public FieldType(String name, T value) {
		this.name=name;
		this.value=value;
	}	
	public boolean isBlank() {
		return StringUtils.isBlank(String.valueOf(value));
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
