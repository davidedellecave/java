package ddc.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LiteXmlUtil {
	public static LiteXmlDocument createDocument() throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			LiteXmlDocument doc = new LiteXml(b.newDocument());
			return doc;
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument: " + e.getMessage());
		}
	}

	public static LiteXmlDocument createDocument(File file) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b;
			b = f.newDocumentBuilder();
			LiteXmlDocument doc = new LiteXml(b.parse(file));
			return doc;
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	public static LiteXmlDocument createDocument(String xml, String charset) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes(charset));
			LiteXmlDocument doc = new LiteXml(b.parse(is));
			return doc;
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	public static LiteXmlDocument createDocument(byte[] bytes) throws LiteXmlException {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(true); // never forget this!
			DocumentBuilder b = f.newDocumentBuilder();
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			LiteXmlDocument doc = new LiteXml(b.parse(is));
			return doc;
		} catch (ParserConfigurationException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (SAXException e) {
			throw new LiteXmlException("createDocument", e);
		} catch (IOException e) {
			throw new LiteXmlException("createDocument", e);
		}
	}

	
	public static List<Element> detectArray(LiteXmlDocument doc, List<Node> nodes) {
		List<Element> list = toElements(nodes);
		if (list.size() == 0) {
			return null;
		}
		if (list.size() == 1) {
			return null;
		}
		String tagName = list.get(0).getTagName();
		for (Element e : list) {
			if (e.getTagName() != tagName) {
				tagName = "";
				return null;
			}
		}
		return list;
	}

	public static boolean isTerminalElement(Element e) {
		List<Element> childElements = toElements(e.getChildNodes());
		for (Element childE : childElements) {
			if (countElement(childE.getChildNodes()) != 0) {
				return false;
			}
		}
		return true;
	}

	public static NodeList eval(Document doc, XPathExpression expr) throws XPathExpressionException {
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		if (result instanceof NodeList) {
			return (NodeList) result;
		}
		return null;
	}

	public static List<Node> toNodes(NodeList nodes) {
		ArrayList<Node> a = new ArrayList<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			a.add(nodes.item(i));
		}
		return a;
	}
	
	public static List<Node> toNodes(Node node) {
		ArrayList<Node> a = new ArrayList<>();
		a.add(node);
		return a;
	}

	public static List<Element> toElements(NodeList nodes) {
		ArrayList<Element> a = new ArrayList<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				a.add((Element) nodes.item(i));
			}
		}
		return a;
	}

	public static List<Element> toElements(Node node) {
		ArrayList<Element> a = new ArrayList<Element>();
		if (node instanceof Element) {
			a.add((Element) node);
		}
		return a;
	}

	public static int countElement(NodeList nodes) {
		int count = 0;
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				count++;
			}
		}
		return count;
	}

	public static List<Element> toElements(List<Node> nodes) {
		ArrayList<Element> a = new ArrayList<>();
		for (Node node : nodes) {
			if (node instanceof Element) {
				a.add((Element) node);
			}
		}
		return a;
	}
}
