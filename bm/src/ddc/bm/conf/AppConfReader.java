package ddc.bm.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ddc.util.ExceptionUtil;
import ddc.util.StringOutputStream;
import ddc.xml.LiteXml;
import ddc.xml.LiteXmlDocument;
import ddc.xml.LiteXmlException;

public class AppConfReader {
	private Tenants tenants = new Tenants();
	private static final String RESOURCE = "app.conf.xml";
	private static final String LOG_INFO = "App configuration parser - ";

	public PropsConf getAppConf(String tenant) {
		if (tenants == null)
			reload();
		return tenants.get(tenant);
	}

	private class Tenants extends TreeMap<String, PropsConf> {
		private static final long serialVersionUID = 4439886502218904233L;
	}


	public void reload() {
		try {
			InputStream is = this.getClass().getResourceAsStream(RESOURCE);
			LiteXmlDocument xml = getXml(is);
			tenants = parseTenants(xml);
		} catch (Throwable t) {
			throw new ConfigurationException(LOG_INFO + ExceptionUtil.getMsg(t), t);
		}
	}

	private Tenants parseTenants(LiteXmlDocument xml) {
		Tenants tenants = new Tenants();
		List<Element> elems = xml.getElements("tenant");
		for (Element e : elems) {
			PropsConf props = new PropsConf();
			String tenant = getAttribute(e, "name");
			NodeList list = e.getChildNodes();			
			for (int i = 0; i < list.getLength(); i++) {
				Node attr = list.item(i);
				if (attr instanceof Element) {
					Map<String, String> attrs = xml.getAttributes((Element)attr);
					PropConf prop = new PropConf(attrs);
					String propName = prop.getName();
					if (StringUtils.isBlank(propName) )
						throw new ConfigurationException("Attribute 'name' not found - tanant:[" + tenant + "#:" + i + "]");
					props.put(propName, prop);					
				}
			}
			tenants.put(tenant, props);
		}
		return tenants;
	}

	private String getAttribute(Element e, String name) {
		String v = e.getAttribute(name);
		if (StringUtils.isBlank(v))
			throw new ConfigurationException("Attribute is blank - name:[" + name + "]");
		return v.trim();
	}

	private LiteXmlDocument getXml(InputStream is) throws IOException, LiteXmlException {
		StringOutputStream os = new StringOutputStream();
		IOUtils.copy(is, os);
		LiteXml xml = new LiteXml();
		xml.createDocument(os.toString(), "UTF-8");
		return xml;
	}
}
