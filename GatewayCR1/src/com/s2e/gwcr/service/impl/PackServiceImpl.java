package com.s2e.gwcr.service.impl;

import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackItem;
import com.s2e.gwcr.service.PackService;

public class PackServiceImpl implements PackService {
	private static String LOG_ENCODE_HEADER = PackServiceImpl.class.getSimpleName() + ".encode.";
	private static String LOG_DECODE_HEADER = PackServiceImpl.class.getSimpleName() + ".decode.";
	private static String EXT_ZIP = ".zip";
	private static String EXT_P7E = ".p7e";
	private static String EXT_P7M = ".p7m";

	private EncodeDiagnostic diagnostic = null;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Override
	public void encode(Pack pack) throws GwCrException {
		PackItem item = encode(pack.getName(), pack.getRemoteCert(), pack.getLocalCert(), pack.getLocalPrivateKey(), pack.getData());
		pack.setName(item.getName());
		pack.setData(item.getData());
	}

	@Override
	public void decode(Pack pack) throws GwCrException {
		PackItem item = decode(pack.getName(), pack.getLocalPrivateKey(), pack.getData());
		pack.setName(item.getName());
		pack.setData(item.getData());
	}

	public EncodeDiagnostic getDiagnostic() {
		return diagnostic;
	}

	public void setDiagnostic(EncodeDiagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	private PackItem decode(String name, PrivateKey localPrivateKey, byte[] p7mBytes) throws GwCrException {
		if (diagnostic != null) {
			diagnostic.clear();
		}
		PackItem p7mItem = new PackItem();
		p7mItem.setData(p7mBytes);
		p7mItem.setName(name);
		if (diagnostic != null) {
			diagnostic.add(p7mItem);
		}
		//
		PackItem p7eItem = new PackItem();
		try {
			p7eItem.setData(PackDecoder.getSignedContent(p7mBytes));
			p7eItem.setName(delExt(p7mItem.getName(), EXT_P7M));
			if (diagnostic != null)
				diagnostic.add(p7eItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "SignVerifing", e);
		}
		//
		PackItem zipItem = new PackItem();
		try {
			zipItem.setData(PackDecoder.decrypt(localPrivateKey, p7eItem.getData()));
			zipItem.setName(delExt(p7eItem.getName(), EXT_P7E));
			if (diagnostic != null)
				diagnostic.add(zipItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "Decrypting", e);
		}
		//
		PackItem plainItem = new PackItem();
		try {
			plainItem.setData(PackDecoder.unzip(zipItem.getData()));
			plainItem.setName(delExt(zipItem.getName(), EXT_ZIP));
			if (diagnostic != null)
				diagnostic.add(plainItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "Unzipping", e);
		}
		return plainItem;
	}

	private PackItem encode(String name, X509Certificate remoteCert, X509Certificate localCert, PrivateKey localPrivateKey, byte[] plain) throws GwCrException {
		if (diagnostic != null) {
			diagnostic.clear();
		}
		PackItem plainItem = new PackItem();
		plainItem.setData(plain);
		plainItem.setName(name);
		if (diagnostic != null)
			diagnostic.add(plainItem);
		//
		PackItem zipItem = new PackItem();
		try {
			zipItem.setData(PackEncoder.zip(name, plainItem.getData()));
			zipItem.setName(addExt(plainItem.getName(), EXT_ZIP));
			if (diagnostic != null)
				diagnostic.add(zipItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Zipping", e);
		}
		//
		PackItem p7eItem = new PackItem();
		try {
			p7eItem.setData(PackEncoder.encrypt(remoteCert, zipItem.getData()));
			p7eItem.setName(addExt(zipItem.getName(), EXT_P7E));
			if (diagnostic != null)
				diagnostic.add(p7eItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Encrypting", e);
		}
		//
		PackItem p7mItem = new PackItem();
		try {
			p7mItem.setData(PackEncoder.sign(localCert, localPrivateKey, p7eItem.getData()));
			p7mItem.setName(addExt(p7eItem.getName(), EXT_P7M));
			if (diagnostic != null)
				diagnostic.add(p7mItem);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Signing", e);
		}
		return p7mItem;
	}

	private String addExt(String filename, String ext) {
		if (filename.endsWith(ext))
			return filename;
		return filename + ext;
	}

	private String delExt(String filename, String ext) {
		if (!filename.endsWith(ext))
			return filename;
		return StringUtils.removeEnd(filename, ext);
	}
}
