package com.s2e.gwcr.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.service.PackTransformer;

public class PackTransformerImpl implements PackTransformer {
	private static String LOG_ENCODE_HEADER = PackTransformerImpl.class.getSimpleName() + ".encode.";
	private static String LOG_DECODE_HEADER = PackTransformerImpl.class.getSimpleName() + ".decode.";
	private Pack pack = null;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Override
	public void encode(Pack pack) throws GwCrException {
		byte[] encoded = encode(pack.getName(), pack.getRemoteCert(), pack.getLocalCert(), pack.getLocalPrivateKey(),
				pack.getData());
		pack.setData(encoded);
	}

	@Override
	public void decode(Pack pack) throws GwCrException {
		// TODO Auto-generated method stub

	}

	public Map<String, byte[]> encode_Diagnostic(Pack pack) throws GwCrException {
		Map<String, byte[]> map = encode_Diagnostic(pack.getName(), pack.getRemoteCert(), pack.getLocalCert(),
				pack.getLocalPrivateKey(), pack.getData());
		pack.setData(map.get("p7m"));
		return map;
	}

	private Map<String, byte[]> encode_Diagnostic(String name, X509Certificate remoteCert, X509Certificate localCert,
			PrivateKey localPrivateKey, byte[] data) throws GwCrException {
		Map<String, byte[]> map = new HashMap<>();

		byte[] zipBytes;
		byte[] p7eBytes;
		byte[] p7mBytes;
		try {
			zipBytes = zip(name, data);
			map.put("zip", zipBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Zipping", e);
		}
		try {
			p7eBytes = encrypt(remoteCert, zipBytes);
			map.put("p7e", p7eBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Encrypting", e);
		}
		try {
			p7mBytes = sign(localCert, localPrivateKey, p7eBytes);
			map.put("p7m", p7mBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Signing", e);
		}
		return map;
	}

	private byte[] encode(String name, X509Certificate remoteCert, X509Certificate localCert,
			PrivateKey localPrivateKey, byte[] data) throws GwCrException {
		byte[] zipBytes;
		byte[] p7eBytes;
		byte[] p7mBytes;
		try {
			zipBytes = zip(name, data);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Zipping", e);
		}
		try {
			p7eBytes = encrypt(remoteCert, zipBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Encrypting", e);
		}
		try {
			p7mBytes = sign(localCert, localPrivateKey, p7eBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Signing", e);
		}
		return p7mBytes;
	}

	private byte[] zip(String name, byte[] data) throws IOException {
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

	private byte[] encrypt(X509Certificate remoteCert, byte[] data)
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

	private byte[] sign(X509Certificate cert, PrivateKey privateKey, byte[] p7eBytes) throws NoSuchAlgorithmException,
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
