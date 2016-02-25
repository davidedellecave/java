package ddc.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtils {
	
	public static void XmlDecoderWrite(File file, Object valueObject) throws IOException {
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
		e.writeObject(valueObject);
		e.close();		
	}
	
	public static Object XmlDecoderRead(File file) throws IOException {
		XMLDecoder e = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		Object o = e.readObject();
		e.close();		
		return o;
	}
	
	public static void XStreamWrite(File file, Object valueObject) throws IOException {
		XStream xstream = new XStream();
		String xml = xstream.toXML(valueObject);
		FileUtils.writeStringToFile(file, xml);
	}
	
	public static Object XStreamRead(File file) throws IOException {
		String xml = FileUtils.readFileToString(file);
		XStream xstream = new XStream(new DomDriver());
		return xstream.fromXML(xml);
	}
	
	public static String xmlElement(String tag, String value) {
		return "<" + tag + ">" + value + "</" + tag + ">";
	}
	
	public static String xmlElement(String tag, Map<String, String> attributes, String value) {
		return "<" + tag + xmlAttributes(attributes) + ">" + value + "</" + tag + ">";
	}
	
	public static String xmlAttributes(Map<String, String> attributes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ");
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			buffer.append(xmlAttribute(entry.getKey(), entry.getValue()));
		}
		return buffer.toString();
	}
	
	public static String xmlAttribute(String name, String value) {
		return " " + name + "=\"" + value + "\""; 
	}
}
