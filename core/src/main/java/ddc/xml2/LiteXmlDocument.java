package ddc.xml2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * @author davidedc, 01/Agosto/2013
 */
public interface LiteXmlDocument {
	public void createDocument() throws LiteXmlException;
	public void createDocument(File file) throws LiteXmlException;
	public void createDocument(String xml, String charset) throws LiteXmlException;	
	public void createDocument(byte[] bytes) throws LiteXmlException;
	public Document getDocument();
	public Node addRoot(String name);
	public Node getRoot();
	public Node addChild(Node parent, String name);
	public Node addChild(Node parent, String name, String value);
	public Node addChild(Node parent, String name, long value);
	public Node addChild(Node parent, String name, Date value);
	public Node addChildCData(Node node, String name, String value);
	public Attr addAttribute(Node node, String name, String value);	
	public Node getElement(String name);
	public Node getElement(XPathExpression expr);
	public Node getElementByXPath(String xpath);
	//
	public List<Element> getElements(String name);
	public List<Element> getElements(XPathExpression expr);
	public List<Node> getNodeListByXPath(String xpath) throws LiteXmlException;
	//	
	public String getValue(String name);
	public String getValue(XPathExpression expr);
	public String getValueByXPath(String xpath);
	public List<String> getValues(String name);
	public void setValue(Node node, String value);
	public void setValue(String name, String value);
	public void setValue(XPathExpression expr, String value);
	public void setValueByXPath(String xpath, String value);
	public CDATASection setCData(Node node, String name, String value);
	public boolean removeByXPath(String xpath);
	public boolean hasElementByXPath(String xpath);
	public void write(ByteArrayOutputStream bytes) throws LiteXmlException;
	public void write(File file) throws LiteXmlException;
	public String getXmlString(String charsetName) throws LiteXmlException;	
	public XPathExpression compileXPath(String xpath) throws LiteXmlException;

}
