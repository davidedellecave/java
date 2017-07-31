package com.s2e.gwcr.service;

import java.security.cert.X509Certificate;

import com.s2e.gwcr.model.ABI;

public interface _CertProvider {
	public X509Certificate getCert(ABI abi);
}
