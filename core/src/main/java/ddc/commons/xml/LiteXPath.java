package ddc.commons.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LiteXPath {
	
	public static List<Element> eval(LiteXmlDocument doc, String xpath) throws LiteXmlException {
		return evalToElements(doc.getDocument(), xpath);	
	}
	
	public static List<Element> evalToElements(Node node, String xpath) throws LiteXmlException {		 
		try {
			XPathExpression c = compileXPath(xpath);
			Object result = c.evaluate(node, XPathConstants.NODESET);
			if (result instanceof NodeList) {
				NodeList nodes = (NodeList) result;
				return LiteXmlUtil.toElements(nodes);
			} else if (result instanceof Node) {
				Node n = (Node) result;
				return LiteXmlUtil.toElements(n);
			}
		} catch (XPathExpressionException e) {
			throw new LiteXmlException("evalToElements()", e);
		}
		return new ArrayList<Element>();
	}
	
	public static List<Node> eval(Node node, String xpath) throws LiteXmlException {		 
		try {
			XPathExpression c = compileXPath(xpath);
			Object result = c.evaluate(node, XPathConstants.NODESET);
			
			if (result instanceof NodeList) {
				NodeList nodes = (NodeList) result;
				return LiteXmlUtil.toNodes(nodes);
			} else if (result instanceof Node) {
				Node n = (Node) result;
				return LiteXmlUtil.toNodes(n);
			}
		} catch (XPathExpressionException e) {
			throw new LiteXmlException("eval()", e);
		}
		return new ArrayList<Node>();
	}

	
	public static XPathExpression compileXPath(String xpath) throws LiteXmlException {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xp = factory.newXPath();
			return xp.compile(xpath);
		} catch (XPathExpressionException e) {
			throw new LiteXmlException("compileXPath()", e);
		}
	}
}
