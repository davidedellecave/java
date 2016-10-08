package ddc.bm.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ddc.util.DateUtil;
import ddc.util.ExceptionUtil;
import ddc.util.LRange;
import ddc.util.StringOutputStream;
import ddc.xml.LiteXml;
import ddc.xml.LiteXmlDocument;
import ddc.xml.LiteXmlException;

public class UserConfiguration {
	private static final String LOG_INFO = "Configuration parser - ";
	private static Tenants tenants = null;
	
	public void reload() {
		try {
			InputStream is =  this.getClass().getResourceAsStream("conf.xml");
			LiteXmlDocument xml = getXml(is);
			Features features = parseFeatures(xml);
			Profiles profiles = parseProfiles(xml, features);
			tenants = parseTenants(xml, profiles);
		} catch (Throwable t) {
			throw new ConfigurationException(LOG_INFO + ExceptionUtil.getMsg(t), t);
		}		
	}

	public Users getUsers(String tenant) {
		if (tenants==null) reload();
		return tenants.get(tenant);
	}
	
	public void print(PrintStream w) {
		if (tenants==null) reload();
		String space ="";
		for ( Map.Entry<String, Users> t: tenants.entrySet()) {
			w.println(t.getKey());
			space += '\t';
			for (Map.Entry<String, User> u : t.getValue().entrySet()) {
				w.println(space + u.getKey());
				space += '\t';
				for (Map.Entry<String, Feature> f : u.getValue().features.entrySet()) {
					w.println(space + f.getKey() + " - " + f.getValue().getPeriod().toString());	
				}
				space = space.substring(0, space.length()-1);
			}
			space = space.substring(0, space.length()-1);
		}
	}
	
	private class Profiles extends TreeMap<String, Features> {
		private static final long serialVersionUID = 4439886502218904233L;
	}

	private class Tenants extends TreeMap<String, Users> {
		private static final long serialVersionUID = 4439886502218904233L;
	}

	private Tenants parseTenants(LiteXmlDocument xml, final Profiles profiles) {
		Tenants tenants = new Tenants();
		List<Element> elems = xml.getElements("tenant");
		for (Element e : elems) {
			String tenant = getAttribute(e, "name");
			Users users = new Users();
			NodeList list = e.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node instanceof Element) {
					String username = getAttribute((Element) node, "name");
					String password = getAttribute((Element) node, "password");
					String profileList = getAttribute((Element) node, "profiles");
					Features features = getFeaturesFromProfiles(tenant, username, profiles, profileList);
					User user = new User();
					user.password = password;
					user.features = features;
					if (users.containsKey(username)) {
						throw new ConfigurationException("Parsing tenants - user already exists - tenant:[" + tenant + "] user:[" + username + "]");
					}					
					users.put(username, user);
				}
			}
			if (tenants.containsKey(tenant)) {
				throw new ConfigurationException("Parsing tenants - tenant already exists - tenant:[" + tenant + "]");
			}
			tenants.put(tenant, users);
		}
		return tenants;
	}

	private Features getFeaturesFromProfiles(String tenant, String username, final Profiles profiles, String profileList) {
		Features newFeatures = new Features();
		profileList = profileList.trim();
		if (StringUtils.isBlank(profileList)) {
			return newFeatures;
		}
		if ("*".equals(profileList)) {
			for (Features f : profiles.values()) {
				newFeatures.putAll(f);
			}
			return newFeatures;
		}
		String[] toks = profileList.split(",");
		for (String profile : toks) {
			profile = profile.trim();
			if (!profiles.containsKey(profile)) {
				throw new ConfigurationException("Parsing tenants - profile not found - tenant:[" + tenant + "] username:[" + username +"] profile:[" + profile + "]");
			}
			newFeatures.putAll(profiles.get(profile));
		}
		return newFeatures;
	}

	private Profiles parseProfiles(LiteXmlDocument xml, final Features features) {
		Profiles profiles = new Profiles();
		List<Element> elems = xml.getElements("profile");
		for (Element e : elems) {
			String profile = getAttribute(e, "name");
			String extensionList = getAttributeOptional(e, "extends");
			String featureList = getAttributeOptional(e, "features");

			Features currentFeatures = extendsProfile(profile,extensionList, profiles);
			if (StringUtils.isNotBlank(featureList)) {
				if ("*".equals(featureList)) {
					currentFeatures.putAll(features);
				} else {
					String[] toks = featureList.split(",");
					for (String feature : toks) {
						feature = feature.trim();
						if (!features.containsKey(feature)) {
							throw new ConfigurationException(
									"Parsing profiles - feature not found - profile:[" + profile + "] feature:[" + feature + "]");
						} else {
							currentFeatures.put(feature, features.get(feature));
						}
					}
				}
			}
			if (profiles.containsKey(profile)) {
				throw new ConfigurationException("Parsing profiles - profile already exists - profile:[" + profile + "]");
			}
			profiles.put(profile, currentFeatures);
		}
		return profiles;
	}

	private Features extendsProfile(String name, String extensionList, Profiles profiles) {
		Features newFeatures = new Features();
		if (StringUtils.isBlank(extensionList)) {
			return newFeatures;
		}
		if ("*".equals(extensionList)) {
			for (Features list : profiles.values()) {
				newFeatures.putAll(list);
			}
			return newFeatures;
		}
		String[] toks = extensionList.split(",");
		for (String profile : toks) {
			profile = profile.trim();
			if (!profiles.containsKey(profile)) {
				throw new ConfigurationException("Parsing profiles - Cannot extend from profile not declared - profile:[" + name + "] extends:[" + profile +"]");
			} else {
				newFeatures.putAll(profiles.get(profile));
			}
		}
		return newFeatures;
	}

	private Features parseFeatures(LiteXmlDocument xml) {
		Features features = new Features();
		List<Element> elems = xml.getElements("feature");
		for (Element e : elems) {
			Feature f = new Feature();
			String feature = getAttribute(e, "name");
			String p = getAttribute(e, "period");
			f.setPeriod(parseRange(p));
			if (features.containsKey(feature))
				throw new ConfigurationException("Parsing features - feature already exists - feature:[" + feature + "]");
			features.put(feature, f);
		}
		return features;
	}

	private LRange parseRange(String p) {
		long lower = 0;
		long upper = 0;
		p = p.trim();
		if (",".equals(p)) {
			lower = 0;
			upper = 0;
		} else {
			String[] toks = p.split(",");
			if (toks.length != 2)
				throw new ConfigurationException("Period wrong format - value:[" + p + "]");
			String sLower = toks[0].trim();
			if (sLower.length() == 0) {
				lower = 0;
			} else if (sLower.equals("*")) {
				lower = 0;
			} else {
				Date d = DateUtil.parseDate(sLower, DateUtil.DATE_PATTERN_ISO);
				lower = d.getTime();
			}

			String sUpper = toks[1].trim();
			if (sUpper.length() == 0) {
				upper = 0;
			} else if (sUpper.equals("*")) {
				upper = Long.MAX_VALUE;
			} else {
				Date d = DateUtil.parseDate(sUpper, DateUtil.DATE_PATTERN_ISO);
				upper = d.getTime();
			}
		}
		return new LRange(lower, upper);
	}

	private String getAttribute(Element e, String name) {
		String v = e.getAttribute(name);
		if (StringUtils.isBlank(v))
			throw new ConfigurationException("Attribute is blank - name:[" + name + "]");
		return v.trim();
	}

	private String getAttributeOptional(Element e, String name) {
		String v = e.getAttribute(name);
		if (StringUtils.isBlank(v)) {
			return "";
		}
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
