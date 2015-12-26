package ddc.dao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author davidedc 2014
 *
 */
public class LiteFields extends LinkedHashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public LiteFields trimAllValues() {
		for (Map.Entry<String, String> i : this.entrySet()) {
			i.setValue(i.getValue().trim());
		}	
		return this;
	}
	
	public LiteFields replaceAllValues(char oldChar, char newChar) {
		for (Map.Entry<String, String> i : this.entrySet()) {
			i.setValue(i.getValue().replace(oldChar, newChar));
		}	
		return this;
	}
	
	/**
	 * Trim all values and replace "\r\n\t" to '\0'
	 */
	public LiteFields sanitizeAllValues() {
		for (Map.Entry<String, String> i : this.entrySet()) {
			i.setValue(i.getValue().trim().replace("\t", "").replace("\n", "").replace("\r", ""));
		}
		return this;
	}
	
	/**
	 * Replace all values from "\r\n\t" to '\0'
	 */
	public LiteFields replaceControlValues() {
		for (Map.Entry<String, String> i : this.entrySet()) {
			i.setValue(i.getValue().replace("\t", "").replace("\n", "").replace("\r", ""));
		}
		return this;
	}
}
