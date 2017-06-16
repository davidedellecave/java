package com.s2e.gwcr.test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaCertStoreBuilder;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JcaX509CertSelectorConverter;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public class EncDec {

	private static final String WORK_DIR = "/Users/davide/tmp/gtw/test";

	private static final File SOURCE_JFILE = new File(WORK_DIR, "jmessage.txt");

	private static final File P7E_JFILE = new File(WORK_DIR, "jmessage.txt.p7e");
	private static final File P7M_JFILE = new File(WORK_DIR, "jmessage.txt.p7e.p7m");

	private static final File P7E_DECRYPTED_JFILE = new File(WORK_DIR, "jmessage.p7e.txt");
	private static final File P7M_DECRYPTED_JFILE = new File(WORK_DIR, "jmessage.p7m.p7e.txt");

	private static final File BDI_CERT = new File(WORK_DIR, "bdi.crt");
	private static final File BDI_KEY = new File(WORK_DIR, "bdi.key");

	private static final File S2E_CERT = new File(WORK_DIR, "s2e.crt");
	private static final File S2E_KEY = new File(WORK_DIR, "s2e.key");

	private static final boolean ENCRYPT_ENABLE = true;
	private static final boolean VERIFY = false;
	private static final boolean DECRYPT_ENABLE = false;
//	private static final boolean EXTRACT_CONTENT_FROM_P7M = true;
	

	public static void main(final String[] args) throws Exception {
		if (!new File(WORK_DIR).exists()) {
			throw new RuntimeException("Update WORK_DIR to point to the directory the project is cloned into.");
		}

		Security.addProvider(new BouncyCastleProvider());

		if (ENCRYPT_ENABLE) {
			System.out.println("Encoding... ");
			Files.deleteIfExists(P7E_JFILE.toPath());
			X509Certificate certificate = getX509Certificate(BDI_CERT);
			encrypt(certificate, SOURCE_JFILE, P7E_JFILE);

			System.out.println("Signing... ");
			Files.deleteIfExists(P7M_JFILE.toPath());
			X509Certificate S2ECertificate = getX509Certificate(S2E_CERT);
			PrivateKey S2EPrivateKey = getPemPrivateKey(S2E_KEY.toString(), "RSA");
			sign(S2ECertificate, S2EPrivateKey, P7E_JFILE, P7M_JFILE);
			// sign(S2ECertificate, S2EPrivateKey, SOURCE_JFILE, P7M_JFILE);
		}
		
		if (VERIFY) {
			System.out.println("Validation... ");
			X509Certificate S2ECertificate = getX509Certificate(S2E_CERT);
			byte[] p7mBytes = Files.readAllBytes(P7M_JFILE.toPath());
			System.out.println("Valid:" + isValid(p7mBytes, S2ECertificate));

		}

		if (DECRYPT_ENABLE) {
			System.out.println("Decoding... ");
			
			Files.deleteIfExists(P7M_DECRYPTED_JFILE.toPath());
			byte[] p7mBytes = Files.readAllBytes(P7M_JFILE.toPath());
			byte[] p7eOut = getSignedContent(p7mBytes);		
			FileOutputStream fOut = new FileOutputStream(P7M_DECRYPTED_JFILE);
			fOut.write(p7eOut);
			fOut.close();
			
			
//			Files.deleteIfExists(P7E_DECRYPTED_JFILE.toPath());
			PrivateKey privateKey = getPemPrivateKey(BDI_KEY.toString(), "RSA");
			decrypt(privateKey, p7eOut, P7E_DECRYPTED_JFILE);
			
		}

//		if (EXTRACT_CONTENT_FROM_P7M) {
//			System.out.println("Extraction P7M... ");
//
//			byte[] p7mBytes = Files.readAllBytes(P7M_JFILE.toPath());
//			byte[] p7eOut = getSignedContent(p7mBytes);		
//			FileOutputStream fOut = new FileOutputStream(P7M_DECRYPTED_JFILE);
//			fOut.write(p7eOut);
//			fOut.close();
//			
//			
//			System.out.println("Extraction P7E... ");
//			byte[] p7eBytes = Files.readAllBytes(P7E_JFILE.toPath());
//			byte[] out = getSignedContent(p7eBytes);
//			System.out.println(out);
//			
////			byte[] out = getSignedContent(p7mBytes);
////			System.out.println(out);
//
//		}
		

	}

	public static byte[] getSignedContent(final byte[] p7eBytes) throws IOException, CMSException {
		CMSSignedData cms = new CMSSignedData(p7eBytes);
		if (cms.getSignedContent() == null) {
			// Error!!!
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		cms.getSignedContent().write(out);
		return out.toByteArray();
	}

	
	public static boolean isValid(final byte[] p7bytes, X509Certificate rootCert) throws Exception {
		CMSSignedData signedData = new CMSSignedData(p7bytes);
		
//		byte[] data = Base64.decodeBase64(p7bytes);
//		CMSSignedData signedData = new CMSSignedData(p7bytes);
//		System.out.println(signedData.getSignedContent());
		
		CertStore certsAndCRLs = new JcaCertStoreBuilder().setProvider("BC").addCertificates(signedData.getCertificates()).build();
		SignerInformationStore signers = signedData.getSignerInfos();
		Iterator it = signers.getSigners().iterator();

		if (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			X509CertSelector signerConstraints = new JcaX509CertSelectorConverter().getCertSelector(signer.getSID());

			signerConstraints.setKeyUsage(JcaUtils.getKeyUsage(KeyUsage.digitalSignature));

			PKIXCertPathBuilderResult result = JcaUtils.buildPath(rootCert, signerConstraints, certsAndCRLs);

			return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC")
					.build((X509Certificate) result.getCertPath().getCertificates().get(0)));
		}

		return false;
	}

	private static PrivateKey getPemPrivateKey(String filename, String algorithm)
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

	private static void getPk() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		byte[] encodedPrivateKey = Files.readAllBytes((new File(WORK_DIR, "s2e.key")).toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey pk = keyFactory.generatePrivate(privateKeySpec);

	}

	private static void decrypt(PrivateKey privateKey, File encrypted, File decryptedDestination)
			throws IOException, CMSException {

		byte[] encryptedData = Files.readAllBytes(encrypted.toPath());
		decrypt(privateKey, encryptedData, decryptedDestination);
		
		System.out.println(String.format("Decrypted '%s' to '%s'", encrypted.getAbsolutePath(),
				decryptedDestination.getAbsolutePath()));
	}
	
	private static void decrypt(PrivateKey privateKey, byte[] encryptedData, File decryptedDestination)
			throws IOException, CMSException {
		
		CMSEnvelopedDataParser parser = new CMSEnvelopedDataParser(encryptedData);

		RecipientInformation recInfo = getSingleRecipient(parser);
		Recipient recipient = new JceKeyTransEnvelopedRecipient(privateKey);

		try (InputStream decryptedStream = recInfo.getContentStream(recipient).getContentStream()) {
			Files.copy(decryptedStream, decryptedDestination.toPath());
		}

	}

	private static void encrypt(X509Certificate cert, File source, File destination)
			throws CertificateEncodingException, CMSException, IOException {

		OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES256_CBC)
				.setProvider(BouncyCastleProvider.PROVIDER_NAME).build();

		CMSEnvelopedDataStreamGenerator generator = new CMSEnvelopedDataStreamGenerator();
		generator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(cert));

		try (FileOutputStream fileOutStream = new FileOutputStream(destination);
				OutputStream encryptingStream = generator.open(fileOutStream, encryptor)) {

			byte[] unencryptedContent = Files.readAllBytes(source.toPath());
			encryptingStream.write(unencryptedContent);
		}

		System.out.println(
				String.format("Encrypted '%s' to '%s'", source.getAbsolutePath(), destination.getAbsolutePath()));
	}

	private static void sign(X509Certificate cert, PrivateKey privateKey, File source, File destination)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, CertificateException,
			OperatorCreationException, CMSException {

		// PrivateKey privateKey = getPemPrivateKey(S2E_KEY.toString(), "RSA");
		// X509Certificate cert = getX509Certificate(S2E_CERT);
		Store certs = new JcaCertStore(Arrays.asList(new X509Certificate[] { cert }));

		// set up the generator
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

		gen.addSignerInfoGenerator(
				new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC").build("SHA256withRSA", privateKey, cert));
		gen.addCertificates(certs);

		// create the signed-data object
		byte[] p7eBytes = Files.readAllBytes(source.toPath());
		CMSTypedData data = new CMSProcessableByteArray(p7eBytes);
		CMSSignedData signed = gen.generate(data, true);

		// recreate
		signed = new CMSSignedData(data, signed.getEncoded());

		FileOutputStream fileStream = new FileOutputStream(destination);
		fileStream.write(signed.getEncoded());
		fileStream.close();
		System.out.println(
				String.format("Encrypted '%s' to '%s'", source.getAbsolutePath(), destination.getAbsolutePath()));

	}

	private static X509Certificate getX509Certificate(File certificate) throws IOException, CertificateException {
		try (InputStream inStream = new FileInputStream(certificate)) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(inStream);
		}
	}

	private static PrivateKey getPrivateKey(File file, String password) throws Exception {
		// Provider pList[] = Security.getProviders();
		// for (Provider p : pList)
		// System.out.println(p.getName());

		KeyStore ks = KeyStore.getInstance("PKCS12");
		try (FileInputStream fis = new FileInputStream(file)) {
			ks.load(fis, password.toCharArray());
		}

		Enumeration<String> aliases = ks.aliases();
		String alias = aliases.nextElement();
		return (PrivateKey) ks.getKey(alias, password.toCharArray());

	}

	private static RecipientInformation getSingleRecipient(CMSEnvelopedDataParser parser) {
		Collection recInfos = parser.getRecipientInfos().getRecipients();
		Iterator recipientIterator = recInfos.iterator();
		if (!recipientIterator.hasNext()) {
			throw new RuntimeException("Could not find recipient");
		}
		return (RecipientInformation) recipientIterator.next();
	}
}
