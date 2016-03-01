package ddc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Fields extends ArrayList<Field>{
	private static final long serialVersionUID = 1914197235303340201L;
	
	public Fields() {}
	
	public Fields(Map<String, Object> map) {
		for (Map.Entry<String, Object> e : map.entrySet()) {
			add(e.getKey(), e.getValue());
		}
	}
	
	public Fields add(String name) {
		return add(name, null);
	}
	
	public Fields add(String name, Object value) {
		this.add(new Field(name, value));
		return this;
	}
	
	/**
	 * Return the field named
	 * @param name
	 * @return if the name is not found return NULL
	 */
	public Field get(String name) {
		for (Field f : this) {
			if (f.getName().equals(name)) return f;
		}
		return null;
	}
		
	public Field set(String name, Object value) {
		for (Field f : this) {			
			if (f.getName().equals(name)) {
				f.setValue(value);
				return f;
			}
		}
		return null;
	}	
	
	public Field setSafe(String name, int index, String[] values) {
		return set(name, values.length>index ? values[index] : "");
	}	
	
	public Object getValue(String name) {
		for (Field f : this) {			
			if (f.getName().equals(name)) return f.getValue(); 
		}
		return null;		
	}
	
	public boolean contains(String name) {
		return (this.get(name)!=null);
	}
	
	public String toString() {
		StringBuilder m = new StringBuilder(this.size()*15);
		m.append("#:["+ this.size() +"]");
		for (Field f : this) {
			m.append(f.toString());
		}
		return m.toString();
	}
	
	public String[] getStringValues() {
		String[] values = new String[size()];
		int i=0;
		for (Field f : this) {
			values[i]=f.stringValue();
			i++;
		}
		return values;		
	}
	
	public String[] getNames() {
		String[] names = new String[size()];
		int i=0;
		for (Field f : this) {
			names[i]=f.getName();
			i++;
		}
		return names;		
	}
	
	public Object[] getValues() {
		Object[] values = new Object[size()];
		int i=0;
		for (Field f : this) {
			values[i]=f.getValue();
			i++;
		}
		return values;		
	}
	
	public void setAllValuesTo(Object value, boolean enabled) {
		for (Field f : this) {			
			f.setValue(value);
			f.setEnabled(enabled);
		}		
	}

	public void setAllValuesTo(Object value) {
		for (Field f : this) {			
			f.setValue(value);
		}		
	}

	public void setAllEnableTo(boolean enabled) {
		for (Field f : this) {			
			f.setEnabled(enabled);
		}		
	}

	public String join(String separator) {
		StringBuilder buff = new StringBuilder();
		for (Field f : this) {
			if (f.isEnabled()) buff.append(f.stringValue()).append(separator);
		}
		String j = buff.toString();
		j = StringUtils.removeEnd(j, separator);
		return j;
	}
		
	public int indexOf(String name) {
		int pos=0;
		for (Field f : this) {			
			if (f.getName().equals(name)) return pos;
			pos++;
		}
		return -1;
	}
	
	
	private static String SEP ="=";
	private void parseLine(String line) {
		String[] toks = line.split(SEP);
		if (toks.length>0) {
			this.add(new Field(toks[0], toks[1]));
		}
	}

	private String buildLine(Field f) {
		return f.getName()+SEP+f.stringValue();
	}

	public void read(File file) throws IOException {		
		BufferedReader br = null;
		try {
			this.clear();
			if (!file.exists()) return;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line=br.readLine())!=null) {
				parseLine(line);
			}
			br.close();
		} catch (IOException e) {
			if (br!=null) br.close();
			throw e;
		}
	}

	public void write(File file)  throws IOException {
		PrintWriter os  = null;
		try {
			os  = new PrintWriter(file);
			for (Field f : this) {
				os.println(buildLine(f));
			}
		} catch (IOException e) {
			throw e;
		} finally {			
			if (os!=null) os.close();
		}
	}
}
