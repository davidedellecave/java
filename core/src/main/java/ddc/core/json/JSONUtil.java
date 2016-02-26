package ddc.core.json;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ddc.util.CsvUtil;
import ddc.util.StringUtil;

public class JSONUtil {
	
	public static JSONObject buildJSONSchemaFromCSV(String csvHeader, char separator, String title, Map<String, String[]> fieldType, String[] requiredFields) {
		String[] defaultType = new String[] {"string"};
		return buildJSONSchemaFromCSV(csvHeader, separator, title, fieldType, requiredFields, defaultType);
	}
	
	public static JSONObject buildJSONSchemaFromCSV(String csvHeader, char separator, String title, Map<String, String[]> fieldType, String[] requiredFields, String[] defaultType) {
		String[] headerToks = CsvUtil.getHeader(csvHeader, separator);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i=0; i<headerToks.length; i++) {
			String key = headerToks[i];
			JSONObject value =  null;
			if (fieldType.containsKey(key)) {
				String[] types = fieldType.get(key);
				if (types.length==1) {
					value =  (new JSONObject()).put("type", types[0]);
				} else {
					value =  (new JSONObject()).put("type", new JSONArray(types));
				}
			} else {
				if (defaultType.length==1) {
					value =  (new JSONObject()).put("type", defaultType[0]);
				} else {
					value =  (new JSONObject()).put("type", new JSONArray(defaultType));
				}				
			}	
			map.put(key, value);
		}
		JSONObject schemaProps = new JSONObject(map);
		
		JSONObject s = new JSONObject();
		s.put("$schema", "http://json-schema.org/draft-04/schema#");
		s.put("title", title);
		s.put("description", "");
		s.put("type", "object");
		s.put("properties", schemaProps);
		JSONArray a = new JSONArray(requiredFields);
		s.put("required", a);
		
		return s;
	}
	
	public static Map<String, String> buildMap(JSONObject json) {
		String names[] = JSONObject.getNames(json);
		Map<String, String> map = new HashMap<String, String>();
		for (String name : names) {
			Object v = json.get(name);
			if (v==null) {
				map.put(name, "");
			} else {
				map.put(name, String.valueOf(v));
			}
		}
		return map;
	}

	public static String buildCsvLine(String jsonData, String[] csvHeader, char sep) {
		JSONObject j = new JSONObject(jsonData);
		return buildCsvLine(j, csvHeader, sep);
	}

	public static String buildCsvLine(JSONObject json, String[] csvHeader, char sep) throws RuntimeException {
		StringBuilder s = new StringBuilder(csvHeader.length*15);
		for (int i = 0; i<csvHeader.length; i++) {
			Object o = json.get(csvHeader[i]);				
			if (o==null) {
				s.append("");
			} else {
				s.append(String.valueOf(o));
			}
			if (i<csvHeader.length-1) s.append(sep);
		}
		return s.toString();
	}
	
	public static Map<String, String> buildMap(String json) {
		JSONObject j = new JSONObject(json);
		return buildMap(j);
	}
	
	public static Map<String, String> getMapFromJSONSchema(JSONObject jsonSchema) {
		JSONObject schema = jsonSchema.getJSONObject("properties");
		
		Map<String, String> fields = new HashMap<String, String>();
		String names[] = JSONObject.getNames(schema);
		for (String name : names) {
			JSONObject f = schema.getJSONObject(name);
			Object o = f.get("type");
			String fieldType = "";
			if (o instanceof JSONArray) {
				fieldType = ((JSONArray)o).getString(0);
			} else {
				fieldType = (String)o;
			}
			fields.put(name, fieldType);
		}
		return fields;
	}
	
	public static String buildClouderaSchema(String tableName, Map<String, String> fields, String[] fieldOrder) {
		StringBuilder sb = new StringBuilder();
		sb.append("create EXTERNAL TABLE ").append(tableName).append(" (");
		for (String name: fieldOrder) {
			String v = fields.get(name);
			sb.append("\n    ").append(name).append(" ").append(v).append(",");
		}		
		sb = StringUtil.removeEnd(sb, ',');
		sb.append("\n)");		
		return sb.toString();		
	}
	
	public static String escape(char c) {
		StringBuilder sb = new StringBuilder(4);
		switch (c) {
		case '\\':
		case '"':
			sb.append('\\');
			sb.append(c);
			break;
		case '/':
			// if (b == '<') {
			sb.append('\\');
			// }
			sb.append(c);
			break;
		case '\b':
			sb.append("\\b");
			break;
		case '\t':
			sb.append("\\t");
			break;
		case '\n':
			sb.append("\\n");
			break;
		case '\f':
			sb.append("\\f");
			break;
		case '\r':
			sb.append("\\r");
			break;
		default:
			if (c < ' ') {
				String t = "000" + Integer.toHexString(c);
				sb.append("\\u" + t.substring(t.length() - 4));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static String quote(String string) {
		if (string == null || string.length() == 0) {
			return "\"\"";
		}
		char c = 0;
		int i;
		int len = string.length();
		StringBuilder sb = new StringBuilder(len + 4);
		sb.append('"');
		for (i = 0; i < len; i += 1) {
			c = string.charAt(i);
			sb.append(escape(c));			
		}
		sb.append('"');
		return sb.toString();
	}
	
	
}
