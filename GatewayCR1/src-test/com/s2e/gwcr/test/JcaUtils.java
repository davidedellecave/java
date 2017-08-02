package com.s2e.gwcr.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Date;

import javax.security.auth.x500.X500Principal;
import javax.security.auth.x500.X500PrivateCredential;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;

import ddc.util.FileUtils;
import ddc.util.StringInputStream;
import ddc.util.StringUtil;

/**
 * Utils
 */
public class JcaUtils {
//	public static final String ROOT_ALIAS = "root";
//	public static final String INTERMEDIATE_ALIAS = "intermediate";
//	public static final String END_ENTITY_ALIAS = "end";

	private static final int VALIDITY_PERIOD = 7 * 24 * 60 * 60 * 1000; // one week

	// public static char[] KEY_PASSWD = "keyPassword".toCharArray();

	public static KeyStore createKeyStore(char[] storePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, storePassword);
		return keyStore;
	}
	
	public static KeyStore loadKeyStore(File file) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		return loadKeyStore(file, null);
	}
	
	public static KeyStore loadKeyStore(InputStream stream) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		return loadKeyStore(stream, null);
	}
	
	public static KeyStore loadKeyStore(File file, char[] storePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final FileInputStream stream = new FileInputStream(file);
		return loadKeyStore(stream, null);
	}
	
	public static KeyStore loadKeyStore(InputStream stream, char[] storePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
		try {
			store.load(stream, storePassword);
		} finally {
			stream.close();
		}
		return store;
	}
	
	public static void addX509CertificateToKeyStore(KeyStore keyStore, String alias, File file) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate certificate = cf.generateCertificate(new FileInputStream(file));		
		addX509CertificateToKeyStore(keyStore, alias, certificate);
	}

	public static void addX509CertificateToKeyStore(KeyStore keyStore, String alias, Certificate certificate) throws KeyStoreException {
		keyStore.setCertificateEntry(alias, certificate);
	}

	/**
	 * Return a boolean array representing passed in keyUsage mask.
	 *
	 * @param mask
	 *            keyUsage mask.
	 */
	public static boolean[] getKeyUsage(int mask) {
		byte[] bytes = new byte[] { (byte) (mask & 0xff), (byte) ((mask & 0xff00) >> 8) };
		boolean[] keyUsage = new boolean[9];

		for (int i = 0; i != 9; i++) {
			keyUsage[i] = (bytes[i / 8] & (0x80 >>> (i % 8))) != 0;
		}

		return keyUsage;
	}

	/**
	 * Build a path using the given root as the trust anchor, and the passed in end
	 * constraints and certificate store.
	 * <p>
	 * Note: the path is built with revocation checking turned off.
	 */
	public static PKIXCertPathBuilderResult buildPath(X509Certificate rootCert, X509CertSelector endConstraints, CertStore certsAndCRLs) throws Exception {
		CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");
		PKIXBuilderParameters buildParams = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(rootCert, null)), endConstraints);
		buildParams.addCertStore(certsAndCRLs);
		buildParams.setRevocationEnabled(false);
		return (PKIXCertPathBuilderResult) builder.build(buildParams);
	}



//	private static Certificate buildCERTFromPEM(File file) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, CertificateException {
//		String openTag= "-----BEGIN CERTIFICATE-----";
//		String closeTag= "-----END CERTIFICATE-----";
//		byte[] keyBytes = FileUtils.readFileToByteArray(file);
//		
//		String base64 = new String(keyBytes);
//		String[] toks = StringUtils.substringsBetween(base64, openTag, closeTag);
//		if (toks.length!=1) throw new IOException("String found ["  + toks.length + "] times");
//		byte[] plain = Base64.decode(toks[0]);
//		ByteArrayInputStream byteArray = new ByteArrayInputStream(plain);
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		return (X509Certificate) cf.generateCertificate(byteArray);
//	}

	// ========================================================================================
	// private static
	// ========================================================================================

	private static X509Certificate getX509Certificate(Path certificate) throws IOException, CertificateException {
		try (InputStream inStream = new FileInputStream(certificate.toFile())) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(inStream);
			
		}
	}
	
	private static PublicKey toPublicKey(X509Certificate cert) throws IOException, CertificateException {
		return cert.getPublicKey();
		
	}

	private static X509Certificate toCertificate(PublicKey pubKey) throws IOException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pubKey.getEncoded()));		
	}
	
//	private static void buildKeyStore() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
//
//		String certfile = "yourcert.cer"; /* your cert path */
//		FileInputStream is = new FileInputStream("yourKeyStore.keystore");
//
//		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//		keystore.load(is, "yourKeyStorePass".toCharArray());
//
//		String alias = "youralias";
//		char[] password = "yourKeyStorePass".toCharArray();
//
//		//////
//
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		InputStream certstream = fullStream(certfile);
//		Certificate certs = cf.generateCertificate(certstream);
//
//		///
//		File keystoreFile = new File("yourKeyStorePass.keystore");
//		// Load the keystore contents
//		FileInputStream in = new FileInputStream(keystoreFile);
//		keystore.load(in, password);
//		in.close();
//
//		// Add the certificate
//		keystore.setCertificateEntry(alias, certs);
//
//		// Save the new keystore contents
//		FileOutputStream out = new FileOutputStream(keystoreFile);
//		keystore.store(out, password);
//		out.close();
//	}

	private static KeyStore buildKeyStoreFromCertificate(File certFile) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		// CREATE A KEYSTORE OF TYPE "Java Key Store"
		KeyStore ks = KeyStore.getInstance("JKS");
		/*
		 * LOAD THE STORE The first time you're doing this (i.e. the keystore does not
		 * yet exist - you're creating it), you HAVE to load the keystore from a null
		 * source with null password. Before any methods can be called on your keystore
		 * you HAVE to load it first. Loading it from a null source and null password
		 * simply creates an empty keystore. At a later time, when you want to verify
		 * the keystore or get certificates (or whatever) you can load it from the file
		 * with your password.
		 */
		ks.load(null, null);
		// GET THE FILE CONTAINING YOUR CERTIFICATE
		FileInputStream fis = new FileInputStream(certFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		// I USE x.509 BECAUSE THAT'S WHAT keytool CREATES
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// NOTE: THIS IS java.security.cert.Certificate NOT java.security.Certificate
		Certificate cert = null;
		/*
		 * I ONLY HAVE ONE CERT, I JUST USED "while" BECAUSE I'M JUST DOING TESTING AND
		 * WAS TAKING WHATEVER CODE I FOUND IN THE API DOCUMENTATION. I COULD HAVE DONE
		 * AN "if", BUT I WANTED TO SHOW HOW YOU WOULD HANDLE IT IF YOU GOT A CERT FROM
		 * VERISIGN THAT CONTAINED MULTIPLE CERTS
		 */
		// GET THE CERTS CONTAINED IN THIS ROOT CERT FILE
		while (bis.available() > 0) {
			cert = cf.generateCertificate(bis);
			ks.setCertificateEntry("SGCert", cert);
		}
		// ADD TO THE KEYSTORE AND GIVE IT AN ALIAS NAME
		ks.setCertificateEntry("SGCert", cert);
		// SAVE THE KEYSTORE TO A FILE
		/*
		 * After this is saved, I believe you can just do setCertificateEntry to add
		 * entries and then not call store. I believe it will update the existing store
		 * you load it from and not just in memory.
		 */
		ks.store(new FileOutputStream("NewClientKeyStore"), "MyPass".toCharArray());
		return ks;
	}

	private static InputStream fullStream(String fname) throws IOException {
		FileInputStream fis = new FileInputStream(fname);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}

	// private static InputStream fullStream(String fname) throws IOException {
	// FileInputStream fis = new FileInputStream(fname);
	// DataInputStream dis = new DataInputStream(fis);
	// byte[] bytes = new byte[dis.available()];
	// dis.readFully(bytes);
	// ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	// return bais;
	// }

	/**
	 * Create a KeyStore containing the a private credential with certificate chain
	 * and a trust anchor.
	 */
	private static KeyStore createCredentials(String ROOT_ALIAS, String INTERMEDIATE_ALIAS, String END_ENTITY_ALIAS, char[] KEY_PASSWD) throws Exception {

		// public static final String ROOT_ALIAS = "root";
		// public static final String INTERMEDIATE_ALIAS = "intermediate";
		// public static final String END_ENTITY_ALIAS = "end";

		KeyStore store = KeyStore.getInstance("JKS");

		store.load(null, null);

		X500PrivateCredential rootCredential = JcaUtils.createRootCredential(ROOT_ALIAS);
		X500PrivateCredential interCredential = JcaUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate(), INTERMEDIATE_ALIAS);
		X500PrivateCredential endCredential = JcaUtils.createEndEntityCredential(END_ENTITY_ALIAS, interCredential.getPrivateKey(), interCredential.getCertificate());

		store.setCertificateEntry(rootCredential.getAlias(), rootCredential.getCertificate());
		store.setKeyEntry(endCredential.getAlias(), endCredential.getPrivateKey(), KEY_PASSWD, new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });

		return store;
	}

	/**
	 * Build a sample V1 certificate to use as a CA root certificate
	 */
	private static X509Certificate buildRootCert(KeyPair keyPair) throws Exception {
		X509v1CertificateBuilder certBldr = new JcaX509v1CertificateBuilder(new X500Name("CN=Test Root Certificate"), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
				new X500Name("CN=Test Root Certificate"), keyPair.getPublic());

		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keyPair.getPrivate());

		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}

	/**
	 * Build a sample V3 certificate to use as an intermediate CA certificate
	 */
	private static X509Certificate buildIntermediateCert(PublicKey intKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
				new X500Principal("CN=Test CA Certificate"), intKey);

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert)).addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(intKey))
				.addExtension(Extension.basicConstraints, true, new BasicConstraints(0)).addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caKey);

		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}

	/**
	 * Build a sample V3 certificate to use as an end entity certificate
	 */
	private static X509Certificate buildEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert) throws Exception {

		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
				new X500Principal("CN=Test End Entity Certificate"), entityKey);

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert)).addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(entityKey))
				.addExtension(Extension.basicConstraints, true, new BasicConstraints(false)).addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caKey);

		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}

	/**
	 * Create a random 2048 bit RSA key pair
	 */
	private static KeyPair generateRSA2048KeyPair() throws Exception {
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");

		kpGen.initialize(2048, new SecureRandom());

		return kpGen.generateKeyPair();
	}

	/**
	 * Create a X500PrivateCredential for the root entity.
	 */
	private static X500PrivateCredential createRootCredential(String rootAlias) throws Exception {
		KeyPair rootPair = generateRSA2048KeyPair();
		X509Certificate rootCert = buildRootCert(rootPair);

		return new X500PrivateCredential(rootCert, rootPair.getPrivate(), rootAlias);
	}

	/**
	 * Create a X500PrivateCredential for the intermediate entity.
	 */
	private static X500PrivateCredential createIntermediateCredential(PrivateKey caKey, X509Certificate caCert, String INTERMEDIATE_ALIAS) throws Exception {
		KeyPair interPair = generateRSA2048KeyPair();
		X509Certificate interCert = buildIntermediateCert(interPair.getPublic(), caKey, caCert);

		return new X500PrivateCredential(interCert, interPair.getPrivate(), INTERMEDIATE_ALIAS);
	}

	/**
	 * Create a X500PrivateCredential for the end entity.
	 */
	private static X500PrivateCredential createEndEntityCredential(String endEntityAlias, PrivateKey caKey, X509Certificate caCert) throws Exception {
		KeyPair endPair = generateRSA2048KeyPair();
		X509Certificate endCert = buildEndEntityCert(endPair.getPublic(), caKey, caCert);

		return new X500PrivateCredential(endCert, endPair.getPrivate(), endEntityAlias);
	}

}
