package ddc.xml2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * @author davidedc, 01/Agosto/2013
 */
public class LiteXml implements LiteXmlDocument {
	private Document doc = null;

	// public LiteXml() {
	// }

	@Override
	public void createDocument() throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			doc = b.newDocument();
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument: " + e.getMessage());
		}
	}

	@Override
	public void createDocument(File file) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b;
			b = f.newDocumentBuilder();
			doc = b.parse(file);
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	@Override
	public void createDocument(String xml, String charset) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes(charset));
			doc = b.parse(is);
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	@Override
	public void createDocument(byte[] bytes) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			doc = b.parse(is);
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	@Override
	public Document getDocument() {
		return doc;
	}

	@Override
	public Node addRoot(String name) {
		Element e = doc.createElement(name);
		doc.appendChild(e);
		return e;
	}

	@Override
	public Node getRoot() {
		return doc.getDocumentElement();
	}

	@Override
	public Node addChild(Node parent, String name) {
		Element e = doc.createElement(name.trim());
		parent.appendChild(e);
		return e;
	}

	@Override
	public Node addChild(Node parent, String name, String value) {
		Node n = addChild(parent, name);
		Text text = doc.createTextNode(value);
		n.appendChild(text);
		return n;
	}

	private static DateFormat ISODateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Node addChild(Node element, String name, Date value) {
		return addChild(element, name, ISODateFormat.format(value));
	}

	@Override
	public Node addChild(Node parent, String name, long value) {
		return addChild(parent, name, String.valueOf(value));
	}

	@Override
	public Node addChildCData(Node parent, String name, String value) {
		Node n = addChild(parent, name);
		CDATASection c = doc.createCDATASection(value);
		n.appendChild(c);
		return n;
	}

	@Override
	public CDATASection setCData(Node parent, String name, String value) {
		CDATASection c = doc.createCDATASection(value);
		parent.appendChild(c);
		return c;
	}

	@Override
	public boolean hasElementByXPath(String xpath) {
		return getElementByXPath(xpath) != null;
	}

	@Override
	public boolean removeByXPath(String xpath) {
		Node e = getElementByXPath(xpath);
		if (e != null && e.getParentNode() != null) {
			e.getParentNode().removeChild(e);
			return true;
		}
		return false;
	}

	@Override
	public Attr addAttribute(Node element, String name, String value) {
		if (element instanceof Element) {
			Attr a = doc.createAttribute(name);
			a.setNodeValue(value);
			((Element) element).setAttributeNode(a);
			return a;
		}
		return null;
	}

	@Override
	public Node getElementByXPath(String xpath) {
		XPathExpression xp;
		try {
			xp = compileXPath(xpath);
			return getElement(xp);
		} catch (LiteXmlException e) {
			return null;
		}
	}

	@Override
	public Node getElement(String name) {
		NodeList list = doc.getElementsByTagName(name);
		if (list.getLength() > 0) {
			Node n = list.item(0);
			if (n instanceof Element) {
				return (Element) n;
			}
		}
		return null;
	}

	@Override
	public List<Element> getElements(String name) {
		NodeList list = doc.getElementsByTagName(name);
		return toListElement(list);
	}

	@Override
	public String getValueByXPath(String xpath) {
		Node e = getElementByXPath(xpath);
		return e == null ? null : e.getTextContent();
	}

	@Override
	public String getValue(String name) {
		Node e = getElement(name);
		return e == null ? null : e.getTextContent();
	}

	public String getValue(XPathExpression expr) {
		Element e = getElement(expr);
		return e == null ? null : e.getTextContent();
	}

	@Override
	public List<String> getValues(String name) {
		NodeList list = doc.getElementsByTagName(name);
		ArrayList<String> a = new ArrayList<String>();
		if (list.getLength() > 0) {
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n instanceof Element) {
					a.add(((Element) n).getTextContent());
				}
			}
		}
		return a;
	}

	private void serialize(Document doc, OutputStream out) throws LiteXmlException {
		try {
			TransformerFactory f = TransformerFactory.newInstance();
			Transformer serializer;
			serializer = f.newTransformer();
			serializer.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException e) {
			throw new LiteXmlException("serialize", e);
		}
	}

	@Override
	public String getXmlString(String charsetName) throws LiteXmlException {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			serialize(doc, bytes);
			return bytes.toString(charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new LiteXmlException(e);
		}
	}

	public void write(ByteArrayOutputStream bytes) throws LiteXmlException {
		serialize(doc, bytes);
	}

	@Override
	public void write(File file) throws LiteXmlException {
		try {
			FileOutputStream f = new FileOutputStream(file);
			serialize(doc, f);
		} catch (FileNotFoundException e) {
			throw new LiteXmlException("write", e);
		}
	}

	@Override
	public void setValue(Node element, String value) {
		element.setNodeValue(value);
	}

	@Override
	public void setValue(String name, String value) {
		Node e = this.getElement(name);
		if (e != null)
			e.setTextContent(value);
	}

	private List<Element> toListElement(NodeList nodes) {
		ArrayList<Element> a = new ArrayList<Element>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element) {
				a.add((Element) n);
			}
		}
		return a;
	}

	private List<Node> toNodeList(NodeList nodes) {
		ArrayList<Node> a = new ArrayList<Node>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			a.add(n);
		}
		return a;
	}

	@Override
	public void setValueByXPath(String xpath, String value) {
		Node e = getElementByXPath(xpath);
		if (e != null) {
			e.setTextContent(value);
		}
	}

	@Override
	public void setValue(XPathExpression expr, String value) {
		Element e;
		e = getElement(expr);
		if (e != null)
			setValue(e, value);
	}

	@Override
	public XPathExpression compileXPath(String xpath) throws LiteXmlException {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xp = factory.newXPath();
			return xp.compile(xpath);
		} catch (XPathExpressionException e) {
			throw new LiteXmlException("select", e);
		}
	}

	@Override
	public List<Element> getElements(XPathExpression expr) {
		try {
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			if (result instanceof NodeList) {
				NodeList nodes = (NodeList) result;
				return toListElement(nodes);
			}
		} catch (XPathExpressionException e) {
		}
		return new ArrayList<Element>();
	}

	@Override
	public List<Node> getNodeListByXPath(String xpath) throws LiteXmlException {
		try {
			XPathExpression expr = compileXPath(xpath);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			if (result instanceof NodeList) {
				NodeList nodes = (NodeList) result;
				return toNodeList(nodes);
			}		
			return new ArrayList<Node>();
		} catch (XPathExpressionException e) {
			throw new LiteXmlException(e);
		}
	}

	@Override
	public Element getElement(XPathExpression expr) {
		try {
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			if (result instanceof NodeList) {
				NodeList nodes = (NodeList) result;
				List<Element> list = toListElement(nodes);
				if (list.size() > 0)
					return list.get(0);
			}
		} catch (XPathExpressionException e) {
		}
		return null;
	}
}
