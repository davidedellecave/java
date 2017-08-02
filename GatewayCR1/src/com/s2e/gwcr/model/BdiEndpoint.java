package com.s2e.gwcr.model;

import java.security.cert.X509Certificate;

public class BdiEndpoint {
	private String endpoint;
	private X509Certificate httpsCert;
	
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public X509Certificate getHttpsCert() {
		return httpsCert;
	}
	public void setHttpsCert(X509Certificate httpsCert) {
		this.httpsCert = httpsCert;
	}
	
	
}
