package ddc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author davidedc 2013
 *
 */
public class ArrayField {
	private int index = -1;
	private Field[] values;
	private int contentSize=0;
	
	public ArrayField(int size) {
		values=new Field[size];
		clear();
	}
	
	public ArrayField(Map<String, Object> map) {
		values=new Field[map.size()];
		clear();
		for (Map.Entry<String, Object> e : map.entrySet()) {
			add(e.getKey(), e.getValue());
		}
	}
	
	public int getContentSize() {
		return contentSize;
	}
	
	public ArrayField add(Field f) {
		index++;
		values[index]=f;
		contentSize += f.stringValue().length();
		return this;
	}
	
	public ArrayField add(String name) {
		Field f = new Field(name, null);
		return add(f);
	}
	
	public ArrayField add(String name, Object value) {
		Field f = new Field(name, value);
		return add(f);
	}
	
	public ArrayField resetIndex() {
		index=-1;
		contentSize=0;
		return this;
	}
	
	public ArrayField clear() {
		resetIndex();
		for (int i=0; i<values.length; i++) {
			values[i]=null;
		}
		return this;
	}
	
	public Field get(String name) {		
		for (int i=0; i<=index; i++) {
			if (values[i].getName().equals(name)) return values[i];
		}
		return null;
	}
		
	public Field set(String name, Object value) {
		Field f = get(name);
		if (f!=null) {
			contentSize -= f.stringValue().length();
			f.setValue(value);
			contentSize += f.stringValue().length();
			return f;
		}
		return null;
	}	
	
//	public Field setSafe(String name, String[] sourceValues, int sourceIndex) {
//		return set(name, sourceValues.length>sourceIndex ? values[index] : "");
//	}	
	
	public Object getValue(String name) {
		Field f = get(name);
		if (f!=null) return f.getValue();
		return null;
	}
	
	public boolean hasName(String name) {
		return (this.get(name)!=null);
	}
	
	public String toString() {
		if (index<0) return "";
		StringBuilder out = new StringBuilder(index*10);
		for (int i=0; i<=index; i++) {
			out.append(values[i].toString());
		}
		return out.toString();
	}
	
	public String[] getStringValues() {
		if (index<0) return new String[0];
		String[] out = new String[index];
		for (int i=0; i<=index; i++) {
			out[i]=values[i].stringValue();
		}
		return out;		
	}
	
	public String[] getNames() {
		if (index<0) return new String[0];
		String[] out = new String[index];
		for (int i=0; i<=index; i++) {
			out[i]=values[i].getName();
		}
		return out;		
	}
	
	public Object[] getValues() {
		if (index<0) return new Object[0];
		Object[] out = new String[index];		
		for (int i=0; i<=index; i++) {
			out[i]=values[i].getValue();
		}
		return out;		
	}
	
	public Field[] getArray() {
		return values;
	}
	
//	public void setAllValuesTo(Object value, boolean enabled) {
//		for (int i=0; i<values.length; i++) {
//			values[i].setValue(value);
//			values[i].setEnabled(enabled);
//		}		
//		contentSize=value.toString().length()*values.length;
//	}

//	public void setAllValuesTo(Object value) {
//		for (int i=0; i<values.length; i++) {
//			values[i].setValue(value);
//		}
//		contentSize=value.toString().length()*values.length;
//	}

//	public void setAllEnableTo(boolean enabled) {
//		for (int i=0; i<values.length; i++) {
//			values[i].setEnabled(enabled);
//		}		
//	}

	/**
	 * Join fields using separator
	 * Replace from field value 'separator' to blank ' ' 
	 * @param separator
	 * @return
	 */
	public String join(char separator) {
		StringBuilder buff = new StringBuilder(contentSize+index-1);
		for (int i=0; i<=index; i++) {
			Field f = values[i];
			if (f.isEnabled()) {
				if (f.stringValue().indexOf(separator)>0) {
					f.setValue(f.stringValue().replace(separator, ' '));
				}
				buff.append(f.stringValue());
			}
			if (i<index) buff.append(separator);
		}
		return buff.toString();
	}
		
	public int indexOf(String name) {
		for (int i=0; i<=index; i++) {
			if (values[i].getName().equals(name)) return i;
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
			for (int i=0; i<=index; i++) {
				os.println(buildLine(values[i]));
			}
		} catch (IOException e) {
			throw e;
		} finally {			
			if (os!=null) os.close();
		}
	}
}
