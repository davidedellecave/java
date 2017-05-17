package ddc.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Element;

import ddc.commons.xml.LiteXml;
import ddc.commons.xml.LiteXmlDocument;
import ddc.commons.xml.LiteXmlException;
import ddc.commons.xml.LiteXmlUtil;
import ddc.util.StringOutputStream;

public class LiteXmlTest {
	private static final String RESOURCE="test.xml";


	@Test
	public void testCreateDocument() throws IOException, LiteXmlException {
		LiteXml xml = (LiteXml) getXml();
	}

	@Test
	public void testGetAttrsAsMap() throws Exception {
		LiteXml xml = (LiteXml) getXml();
		
		Element elem = xml.getElementByTagName("prop");
		Map<String, String> map = xml.getAttributes(elem);
		System.out.println(map.toString());
	}
	

	
	private LiteXmlDocument getXml() throws IOException, LiteXmlException {
		InputStream is =  this.getClass().getResourceAsStream(RESOURCE);
		StringOutputStream os = new StringOutputStream();
		IOUtils.copy(is, os);
		LiteXmlDocument xml = LiteXmlUtil.createDocument(os.toString(), "UTF-8");		
		return xml;
	}
}
