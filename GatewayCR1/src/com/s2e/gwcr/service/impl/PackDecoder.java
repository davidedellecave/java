package com.s2e.gwcr.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;

public class PackDecoder {

	public static byte[] getSignedContent(final byte[] p7mBytes) throws IOException, CMSException {
		CMSSignedData cms = new CMSSignedData(p7mBytes);
		if (cms.getSignedContent() == null) {
			// Error!!!
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		cms.getSignedContent().write(out);
		return out.toByteArray();
	}

	public static byte[] decrypt(PrivateKey privateKey, byte[] encryptedData) throws IOException, CMSException {

		CMSEnvelopedDataParser parser = new CMSEnvelopedDataParser(encryptedData);

		RecipientInformation recInfo = getSingleRecipient(parser);
		Recipient recipient = new JceKeyTransEnvelopedRecipient(privateKey);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (InputStream decryptedStream = recInfo.getContentStream(recipient).getContentStream()) {
			IOUtils.copy(decryptedStream, out);
		}
		return out.toByteArray();
	}

	private static RecipientInformation getSingleRecipient(CMSEnvelopedDataParser parser) {
		Collection<RecipientInformation> recInfos = parser.getRecipientInfos().getRecipients();
		Iterator<RecipientInformation> recipientIterator = recInfos.iterator();
		if (!recipientIterator.hasNext()) {
			throw new RuntimeException("Could not find recipient");
		}
		return (RecipientInformation) recipientIterator.next();
	}

	public static byte[] unzip(byte[] zipped) throws IOException  {
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipped));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				IOUtils.copy(zis, out);
			}
		} finally {
			if (zis != null)
				zis.close();
			if (out !=null) {
				out.close();
			}
		}
		return out.toByteArray();
	}
}
