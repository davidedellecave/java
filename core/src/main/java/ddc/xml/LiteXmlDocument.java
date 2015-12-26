package ddc.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

/**
 * @author davidedc, 01/Agosto/2013
 */
public interface LiteXmlDocument {
	public void createDocument() throws LiteXmlException;
	public void createDocument(File file) throws LiteXmlException;
	public void createDocument(String xml, String charset) throws LiteXmlException;	
	public void createDocument(byte[] bytes) throws LiteXmlException;
	public Document getDocument();
	public Element addRoot(String name);
	public Element getRoot();
	public Element addChild(Element element, String name);
	public Element addChild(Element element, String name, String value);
	public Element addChild(Element element, String name, long value);
	public Element addChild(Element element, String name, Date value);
	public Attr addAttribute(Element element, String name, String value);
	public Element getElement(String name);
	public Element getElement(XPathExpression expr);
	public Element getElementByXPath(String xpath);
	public List<Element> getElements(String name);
	public List<Element> getElements(XPathExpression expr);	
	public String getValue(String name);
	public String getValue(XPathExpression expr);
	public String getValueByXPath(String xpath);
	public List<String> getValues(String name);
	public void setValue(Element element, String value);
	public void setValue(String name, String value);
	public void setValue(XPathExpression expr, String value);
	public void setValueByXPath(String xpath, String value);
	public Element addChildCData(Element element, String name, String value);
	public CDATASection setCData(Element element, String name, String value);
	public boolean removeByXPath(String xpath);
	public boolean hasElementByXPath(String xpath);
	public void write(ByteArrayOutputStream bytes) throws LiteXmlException;
	public void write(File file) throws LiteXmlException;
	public String getXmlString(String charsetName) throws LiteXmlException;	
	public XPathExpression compileXPath(String xpath) throws LiteXmlException;

}
