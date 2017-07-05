package com.s2e.gwcr.service;

import java.security.cert.X509Certificate;

import com.s2e.gwcr.model.ABI;

public interface CertProvider {
	public X509Certificate getCert(ABI abi);
}
