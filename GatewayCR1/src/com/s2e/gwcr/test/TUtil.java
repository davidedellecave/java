package com.s2e.gwcr.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class TUtil {
	
	public byte[] getFileContent(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
	
	
	public static PrivateKey getPemPrivateKey(String filename, String algorithm)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		String temp = new String(keyBytes);
		String privateKeyPEM_base64 = temp.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
		privateKeyPEM_base64 = privateKeyPEM_base64.replace("-----END RSA PRIVATE KEY-----\n", "");
		// System.out.println("Private key\n"+privKeyPEM);

		byte[] privateKeyPlain = Base64.decode(privateKeyPEM_base64);
		// BASE64Decoder b64 = new BASE64Decoder();
		// byte[] decoded = b64.decodeBuffer(privKeyPEM);

		PKCS8EncodedKeySpec privateKeyPKCS8 = new PKCS8EncodedKeySpec(privateKeyPlain);
		KeyFactory kf = KeyFactory.getInstance(algorithm);
		return kf.generatePrivate(privateKeyPKCS8);
	}
	
	public static X509Certificate getX509Certificate(File certificate) throws IOException, CertificateException {
		try (InputStream inStream = new FileInputStream(certificate)) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(inStream);
		}
	}
}
