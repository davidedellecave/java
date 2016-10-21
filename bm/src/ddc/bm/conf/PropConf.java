package ddc.bm.conf;

import java.util.Map;
import java.util.TreeMap;

public class PropConf  extends TreeMap<String, String> {
	private static final long serialVersionUID = 4439886502218904233L;

	public PropConf(Map<String, String> map) {
		super(map);
	}
	public String getName() {
		return this.get("name");
	}
	public String getString(String propName) {
		return this.get("propName");
	}
	
	public Integer getInt(String propName) {
		if (!this.containsKey(propName)) {
			return null;
		}
		return Integer.valueOf(this.get("propName"));
	}
	public Boolean getBool(String propName) {
		if (!this.containsKey(propName)) {
			return null;
		}
		return Boolean.valueOf(this.get("propName"));
	}
}