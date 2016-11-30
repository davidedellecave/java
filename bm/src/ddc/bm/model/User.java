package ddc.bm.model;

import java.security.Principal;

import ddc.bm.conf.Feature;
import ddc.bm.conf.UserConf;
import ddc.bm.servlet.Tenant;

public class User implements Principal{
	private Tenant tenant;
	private UserConf conf;
	private String username;
	
	public User(Tenant tenant, String username, UserConf conf) {
		this.tenant=tenant;
		this.username=username;
		this.conf=conf;
	}
	
	public boolean isFeatureEnabled(String feature) {
		Feature f = conf.features.get(feature);
		if (f == null)
			return false;
		long now = System.currentTimeMillis();
		return f.getPeriod().contains(now);
	}
	
	@Override
	public String getName() {
		return username;
	}
	
	public String getTenantName() {
		return tenant.getName().toString();
	}

	public Tenant getTenant() {
		return tenant;
	}
	
	public UserConf getConf() {
		return conf;
	}
}
