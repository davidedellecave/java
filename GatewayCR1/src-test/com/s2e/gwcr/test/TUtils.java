package com.s2e.gwcr.test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;

public class TUtils {
	private static final String PRIVATE_KEY_ALGORITHM="RSA";
	
	public static X509Certificate getCert(String name) throws CertificateException {
		InputStream inStream = TUtils.class.getResourceAsStream(name);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(inStream);

	}
	
	public static byte[] getData(String name) throws IOException {
		InputStream in = TUtils.class.getResourceAsStream(name);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		return out.toByteArray();
	}
	
	public static PrivateKey getPrivateKey(String name) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		byte[] keyBytes = getData(name);
		
		String temp = new String(keyBytes);
		String privateKeyPEM_base64 = temp.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
		privateKeyPEM_base64 = privateKeyPEM_base64.replace("-----END RSA PRIVATE KEY-----\n", "");
		byte[] privateKeyPlain = Base64.decode(privateKeyPEM_base64);
		PKCS8EncodedKeySpec privateKeyPKCS8 = new PKCS8EncodedKeySpec(privateKeyPlain);
		KeyFactory kf = KeyFactory.getInstance(PRIVATE_KEY_ALGORITHM);
		return kf.generatePrivate(privateKeyPKCS8);
	}
}
