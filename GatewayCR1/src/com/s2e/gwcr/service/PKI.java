package com.s2e.gwcr.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class PKI {

	public static X509Certificate getX509Certificate(Path certificate) throws IOException, CertificateException {
		try (InputStream inStream = new FileInputStream(certificate.toFile())) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(inStream);
			
		}
	}
	
	public static PublicKey toPublicKey(X509Certificate cert) throws IOException, CertificateException {
		return cert.getPublicKey();
		
	}

	public static X509Certificate toCertificate(PublicKey pubKey) throws IOException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pubKey.getEncoded()));		
	}

}
