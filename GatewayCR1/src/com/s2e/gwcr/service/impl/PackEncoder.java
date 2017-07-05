package com.s2e.gwcr.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.Store;

public class PackEncoder {
	public static byte[] zip(String name, byte[] data) throws IOException {
		ByteArrayInputStream inputStream = null;
		ZipOutputStream outZip = null;
		ByteArrayOutputStream outStream = null;
		try {
			inputStream = new ByteArrayInputStream(data);
			outStream = new ByteArrayOutputStream();
			outZip = new ZipOutputStream(outStream);
			ZipEntry entry = new ZipEntry(name);
			outZip.putNextEntry(entry);
			IOUtils.copy(inputStream, outZip);
			outZip.finish();
			byte[] out = outStream.toByteArray();
			return out;
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outZip != null)				
				outZip.close();			
			if (outStream != null)
				outStream.close();
		}
	}

	public static byte[] encrypt(X509Certificate remoteCert, byte[] data)
			throws CertificateEncodingException, CMSException, IOException {

		ByteArrayOutputStream out = null;
		OutputStream encryptingStream = null;
		try {
			CMSEnvelopedDataStreamGenerator generator = new CMSEnvelopedDataStreamGenerator();
			generator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(remoteCert));
			OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES256_CBC)
					.setProvider(BouncyCastleProvider.PROVIDER_NAME).build();
			out = new ByteArrayOutputStream();
			encryptingStream = generator.open(out, encryptor);
			encryptingStream.write(data);
			out.close();
			encryptingStream.close();
			return out.toByteArray();
		} finally {
			if (out != null)
				out.close();
			if (encryptingStream != null)
				encryptingStream.close();
		}
	}

	public static byte[] sign(X509Certificate cert, PrivateKey privateKey, byte[] p7eBytes) throws NoSuchAlgorithmException,
			InvalidKeySpecException, IOException, CertificateException, OperatorCreationException, CMSException {

		@SuppressWarnings("rawtypes")
		Store store = new JcaCertStore(Arrays.asList(new X509Certificate[] { cert }));
		// set up the generator
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		gen.addSignerInfoGenerator(
				new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC").build("SHA256withRSA", privateKey, cert));
		gen.addCertificates(store);
		// create the signed-data object
		CMSTypedData data = new CMSProcessableByteArray(p7eBytes);
		CMSSignedData signed = gen.generate(data, true);
		// recreate
		signed = new CMSSignedData(data, signed.getEncoded());
		return signed.getEncoded();
	}
}
