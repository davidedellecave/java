package ddc.bm.servlet;

import java.util.Map;
import java.util.TreeMap;

import ddc.bm.conf.ConfigurationException;
import ddc.core.crypto.AESCryptoConfig;

public class Tenant {
	public enum TenantName {
		dev, test, gottardo, spartaco, unknown
	}
	private static Map<String, TenantName> domains = new TreeMap<>();
	static {
		domains.put("localhost", TenantName.dev);
		domains.put("app.delle-cave.it", TenantName.test);
		domains.put("admin.medisportgottardo.it", TenantName.gottardo);
		domains.put("admin.spartacoms.it", TenantName.spartaco);
	}
	
	private static Map<TenantName, String[]> crypto = new TreeMap<>();
	static {
		crypto.put(TenantName.dev, new String[] {"KeTQvRIFBlDuJUhslStQAw==", "bRK+VCKvZ+D0qSzSJ9pbEg=="});
		crypto.put(TenantName.test, new String[] {"KeTQvRIFBlDuJUhslStQAw==", "bRK+VCKvZ+D0qSzSJ9pbEg=="});
		crypto.put(TenantName.gottardo, new String[] {"KeTQvRIFBlDuJUhslStQAw==", "bRK+VCKvZ+D0qSzSJ9pbEg=="});
		crypto.put(TenantName.spartaco, new String[] {"KeTQvRIFBlDuJUhslStQAw==", "bRK+VCKvZ+D0qSzSJ9pbEg=="});
	}
	
	//
	private String domain;
	public Tenant(String domain) {
		this.domain=domain;
	}
	
	public TenantName getName() {
		if (domains.containsKey(domain)) {
			return domains.get(domain);
		}
		return TenantName.unknown;
	}
	
	public AESCryptoConfig getCryptoConfig() {
		if (crypto.containsKey(getName())) {
			String[] keys = crypto.get(getName());
			return new AESCryptoConfig(keys[0], 128, keys[1]);
		}
		throw new ConfigurationException("Security items not found - " + domain);
	}
	
}
