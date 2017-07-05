package com.s2e.gwcr.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
	// public static void decrypt(PrivateKey privateKey, byte[] encryptedData,
	// File decryptedDestination)
	// throws IOException, CMSException {
	//
	// CMSEnvelopedDataParser parser = new
	// CMSEnvelopedDataParser(encryptedData);
	//
	// RecipientInformation recInfo = getSingleRecipient(parser);
	// Recipient recipient = new JceKeyTransEnvelopedRecipient(privateKey);
	//
	// try (InputStream decryptedStream =
	// recInfo.getContentStream(recipient).getContentStream()) {
	// Files.copy(decryptedStream, decryptedDestination.toPath());
	// }
	//
	// }

	private static RecipientInformation getSingleRecipient(CMSEnvelopedDataParser parser) {
		Collection<RecipientInformation> recInfos = parser.getRecipientInfos().getRecipients();
		Iterator<RecipientInformation> recipientIterator = recInfos.iterator();
		if (!recipientIterator.hasNext()) {
			throw new RuntimeException("Could not find recipient");
		}
		return (RecipientInformation) recipientIterator.next();
	}

//	public static byte[] decompressByteArray(byte[] bytes) throws DataFormatException {
//
//		ByteArrayOutputStream baos = null;
//		Inflater iflr = new Inflater();
//		iflr.setInput(bytes);
//		baos = new ByteArrayOutputStream();
//		byte[] tmp = new byte[4 * 1024];
//		try {
//			while (!iflr.finished()) {
//				int size = iflr.inflate(tmp);
//				baos.write(tmp, 0, size);
//			}
//
//		} finally {
//			try {
//				if (baos != null)
//					baos.close();
//			} catch (Exception ex) {
//			}
//		}
//		return baos.toByteArray();
//	}

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

//	public class Main {
//		public static void main(String[] args) throws Exception {
//			FileInputStream fis = new FileInputStream("c:/inas400.zip");
//
//			// this is where you start, with an InputStream containing the bytes
//			// from the zip file
//			ZipInputStream zis = new ZipInputStream(fis);
//			ZipEntry entry;
//			// while there are entries I process them
//			while ((entry = zis.getNextEntry()) != null) {
//				System.out.println("entry: " + entry.getName() + ", " + entry.getSize());
//				// consume all the data from this entry
//				while (zis.available() > 0)
//					zis.read();
//				// I could close the entry, but getNextEntry does it
//				// automatically
//				// zis.closeEntry()
//			}
//		}
//	}

	// def unzipByteArray(input: Array[Byte]): String= {
	// val zipInputStream = new ZipInputStream(new ByteArrayInputStream(input))
	// val entry = zipInputStream.getNextEntry
	// IOUtils.toString(zipInputStream)
	// }

//	public static void doUnzip(Path source, Path dest) throws IOException {
//		FileOutputStream out = null;
//		ZipFile zf = null;
//		try {
//
//			zf = new ZipFile(source.toString());
//			Enumeration<? extends ZipEntry> enum1 = zf.entries();
//			while (enum1.hasMoreElements()) {
//				ZipEntry entry = (ZipEntry) enum1.nextElement();
//				if (!entry.isDirectory()) {
//					// save entry
//					File unzippedFile = new File(dest.toString(), entry.getName());
//					out = new FileOutputStream(unzippedFile);
//					IOUtils.copy(zf.getInputStream(entry), out);
//				} else {
//
//				}
//			}
//		} finally {
//			if (out != null)
//				out.close();
//			if (zf != null)
//				zf.close();
//		}
//	}
}
