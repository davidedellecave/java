package ddc.bm.app;

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
import ddc.util.LRange;
import ddc.util.StringOutputStream;
import ddc.xml.LiteXml;
import ddc.xml.LiteXmlDocument;
import ddc.xml.LiteXmlException;

///ToDo transform to singleton
public class Auth {
	private static final String PARSING_EX = "Configuration parser - ";
	private static Tenants tenants = (new Auth()).new Tenants();

	public Auth() {
		try {
			InputStream is =  Auth.class.getResourceAsStream("conf.xml");
			load(is);
		} catch (Throwable t) {
			throw new AuthException(PARSING_EX + t.getMessage());
		}
	}

	public boolean isUserAuthenticated(String tenant, String username, String password) {
		User u = getUser(tenant, username);
		if (u == null)
			return false;
		return password.equals(u.password);
	}

	public boolean isFeatureEnabled(String tenant, String username, String feature) {
		User u = getUser(tenant, username);
		if (u == null)
			return false;
		Feature f = u.features.get(feature);
		if (f == null)
			return false;
		long now = System.currentTimeMillis();
		return f.period.contains(now);
	}

	public void print(PrintStream w) {
		String space ="";
		for ( Map.Entry<String, Users> t: tenants.entrySet()) {
			w.println(t.getKey());
			space += '\t';
			for (Map.Entry<String, User> u : t.getValue().entrySet()) {
				w.println(space + u.getKey());
				space += '\t';
				for (Map.Entry<String, Feature> f : u.getValue().features.entrySet()) {
					w.println(space + f.getKey() + " - " + f.getValue().period.toString());	
				}
				space = space.substring(0, space.length()-1);
			}
			space = space.substring(0, space.length()-1);
		}
	}
	
	private User getUser(String tenant, String username) {
		Map<String, User> users = tenants.get(tenant);
		if (users == null)
			return null;
		return users.get(username);
	}

	private void load(InputStream is) throws IOException, LiteXmlException {
		LiteXmlDocument xml = getXml(is);
		Features features = parseFeatures(xml);
		Profiles profiles = parseProfiles(xml, features);
		tenants = parseTenants(xml, profiles);
	}


	private class Feature {
		LRange period = LRange.EMPTY;

		@Override
		public String toString() {
			return period.toString();
		}
	}

	private class Features extends TreeMap<String, Feature> {
		private static final long serialVersionUID = 6076066219301760876L;
	}

	private class Profiles extends TreeMap<String, Features> {
		private static final long serialVersionUID = 4439886502218904233L;
	}

	public class User {
		public String password = null;
		public Features features = (new Auth()).new Features();
	}

	private class Users extends TreeMap<String, User> {
		private static final long serialVersionUID = 4439886502218904233L;
	}

	private class Tenants extends TreeMap<String, Users> {
		private static final long serialVersionUID = 4439886502218904233L;
	}

	private Tenants parseTenants(LiteXmlDocument xml, final Profiles profiles) {
		Tenants tenants = (new Auth()).new Tenants();
		List<Element> elems = xml.getElements("tenant");
		for (Element e : elems) {
			String tenant = getAttribute(e, "name");
			Users users = (new Auth()).new Users();
			NodeList list = e.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node instanceof Element) {
					String username = getAttribute((Element) node, "name");
					String password = getAttribute((Element) node, "password");
					String profileList = getAttribute((Element) node, "profiles");
					Features features = getFeaturesFromProfiles(tenant, username, profiles, profileList);
					User user = (new Auth()).new User();
					user.password = password;
					user.features = features;
					if (users.containsKey(username)) {
						throw new AuthException("Parsing tenants - user already exists - tenant:[" + tenant + "] user:[" + username + "]");
					}					
					users.put(username, user);
				}
			}
			if (tenants.containsKey(tenant)) {
				throw new AuthException("Parsing tenants - tenant already exists - tenant:[" + tenant + "]");
			}
			tenants.put(tenant, users);
		}
		return tenants;
	}

	private Features getFeaturesFromProfiles(String tenant, String username, final Profiles profiles, String profileList) {
		Features newFeatures = (new Auth()).new Features();
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
				throw new AuthException("Parsing tenants - profile not found - tenant:[" + tenant + "] username:[" + username +"] profile:[" + profile + "]");
			}
			newFeatures.putAll(profiles.get(profile));
		}
		return newFeatures;
	}

	private Profiles parseProfiles(LiteXmlDocument xml, final Features features) {
		Profiles profiles = (new Auth()).new Profiles();
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
							throw new AuthException(
									"Parsing profiles - feature not found - profile:[" + profile + "] feature:[" + feature + "]");
						} else {
							currentFeatures.put(feature, features.get(feature));
						}
					}
				}
			}
			if (profiles.containsKey(profile)) {
				throw new AuthException("Parsing profiles - profile already exists - profile:[" + profile + "]");
			}
			profiles.put(profile, currentFeatures);
		}
		return profiles;
	}

	private Features extendsProfile(String name, String extensionList, Profiles profiles) {
		Features newFeatures = (new Auth()).new Features();
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
				throw new AuthException("Parsing profiles - Cannot extend from profile not declared - profile:[" + name + "] extends:[" + profile +"]");
			} else {
				newFeatures.putAll(profiles.get(profile));
			}
		}
		return newFeatures;
	}

	private Features parseFeatures(LiteXmlDocument xml) {
		Features features = (new Auth()).new Features();
		List<Element> elems = xml.getElements("feature");
		for (Element e : elems) {
			Feature f = (new Auth()).new Feature();
			String feature = getAttribute(e, "name");
			String p = getAttribute(e, "period");
			f.period = parseRange(p);
			if (features.containsKey(feature))
				throw new AuthException("Parsing features - feature already exists - feature:[" + feature + "]");
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
				throw new AuthException("Period wrong format - value:[" + p + "]");
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
			throw new AuthException("Attribute is blank - name:[" + name + "]");
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
