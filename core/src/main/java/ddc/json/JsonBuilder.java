package ddc.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class JsonBuilder {
	private StringBuffer buff = new StringBuffer();
	private ArrayList<String> closeTag = new ArrayList<String>();
	private static SimpleDateFormat dateISOFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String[] escapingChars = new String[] { "'", "\"", "{", "}","[", "]", "*", "/", "\\", "\n", "\b", "\r", "\t" };
	
	public void openArray() {
		buff.append("[");
		closeTag.add("]");
	}

	private boolean endsWithCloseTags() {
		return (buff.toString().endsWith("]") || buff.toString().endsWith("}"));
	}

	JsonBuilder append(String value) {
		buff.append(value);
		return this;
	}

	private JsonBuilder appendName(String name) {
		buff.append("\"").append(name).append("\"").append(":");
		return this;
	}

	private JsonBuilder appendValue(String value) {
		buff.append("\"").append(escapeValue(value)).append("\"");
		return this;
	}

	private JsonBuilder appendValue(boolean value) {
		buff.append(String.valueOf(value));
		return this;
	}

	private JsonBuilder appendValue(long value) {
		buff.append(String.valueOf(value));
		return this;
	}

	private JsonBuilder appendValue(Date value) {
		String formattedDate = dateISOFormatter.format(value);
		return appendValue(formattedDate);
	}

	private String escapeValue(String value) {
		StringBuilder b = new StringBuilder(value);
		for (int i = 0; i < escapingChars.length; i++) {
			int pos = b.indexOf(escapingChars[i]); 
			while (pos > -1) {
				b.setCharAt(pos, ' ');				
				pos = b.indexOf(escapingChars[i]);
			}
		}
		return b.toString();
	}

	public JsonBuilder openObject() {
		if (endsWithCloseTags())
			buff.append(",");
		buff.append("{");
		closeTag.add("}");
		return this;
	}

	public JsonBuilder openObject(String name) {
		append("{").appendName(name);
		closeTag.add("}");
		return this;
	}

	public JsonBuilder addObject(String name) {
		appendName(name);
		return this;
	}

	public JsonBuilder addObjectValue(String name, String value) {
		appendName(name).appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addObjectValue(String name, boolean value) {
		appendName(name).appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addObjectValue(String name, long value) {
		appendName(name).appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addObjectValue(String name, Date value) {
		appendName(name).appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addObjectValue(String name, Map<String,String> nameStringPairs) {
		for (Map.Entry<String,String> pairs : nameStringPairs.entrySet()) {
			addObjectValue(String.valueOf(pairs.getKey()), String.valueOf(pairs.getValue()));
		}
		return this;
	}

	public JsonBuilder addArrayValue(String value) {
		appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addArrayValue(Date value) {
		appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addArrayValue(long value) {
		appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addArrayValue(boolean value) {
		appendValue(value).append(",");
		return this;
	}

	public JsonBuilder addArrayValue(String[] value) {
		for (int i = 0; i < value.length; i++) {
			addArrayValue(value[i]);
		}
		return this;
	}

	public JsonBuilder close() {
		if (closeTag.size() > 0) {
			String value = String.valueOf(closeTag.remove(closeTag.size() - 1));
			if (buff.toString().endsWith(",")) {
				buff = new StringBuffer(buff.substring(0, buff.length() - 1));
			}
			buff.append(value);
		}
		return this;
	}

	public void closeAll() {
		for (int i = 0; i <= closeTag.size(); i++) {
			close();
		}
	}

	public String toString() {
		closeAll();
		return buff.toString();
	}

}
