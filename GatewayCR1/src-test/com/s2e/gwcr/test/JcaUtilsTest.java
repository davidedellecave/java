package com.s2e.gwcr.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.junit.Test;

import com.s2e.gwcr.repos.DbMock;

public class JcaUtilsTest {

	@Test
	public void testAddX509CertificateToKeyStore() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//		String certPath = "/Users/davide/Downloads/cert.pem";
		
		X509Certificate cert =  DbMock.readCert("apache-tomcat.pem");
		
		KeyStore keyStore = KeyStore.getInstance("JKS");
		//initialize keystore
		keyStore.load(null, null);
		
		JcaUtils.addX509CertificateToKeyStore(keyStore, "alias1", cert);
		
		// Save the new keystore contents
		FileOutputStream out = new FileOutputStream("/Users/davide/Downloads/ks.dat");
		keyStore.store(out, "davidedc".toCharArray());
		out.close();
	}

	
}
