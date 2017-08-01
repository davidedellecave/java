package com.s2e.gwcr.service.impl;

import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.service.PackService;

public class PackServiceImpl implements PackService {
	private static String LOG_ENCODE_HEADER = PackServiceImpl.class.getSimpleName() + ".encode.";
	private static String LOG_DECODE_HEADER = PackServiceImpl.class.getSimpleName() + ".decode.";
	private EncodeDiagnostic diagnostic = null;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Override
	public void encode(Pack pack) throws GwCrException {
		byte[] data = encode(pack.getName(), pack.getRemoteCert(), pack.getLocalCert(), pack.getLocalPrivateKey(), pack.getData());
		pack.setData(data);
	}

	@Override
	public void decode(Pack pack) throws GwCrException {
		byte[] data = decode(pack.getName(), pack.getLocalPrivateKey(), pack.getData());
		pack.setData(data);
	}
	
	public EncodeDiagnostic getDiagnostic() {
		return diagnostic;
	}

	public void setDiagnostic(EncodeDiagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	private byte[] decode(String name, PrivateKey localPrivateKey, byte[] p7mBytes) throws GwCrException {
		if (diagnostic!=null) {
			diagnostic.setZipBytes(null);
			diagnostic.setP7eBytes(null);
			diagnostic.setP7mBytes(null);
		}
		byte[] p7eBytes;
		byte[] zipBytes;		
		byte[] plain;
		
		if (diagnostic!=null) diagnostic.setP7mBytes(p7mBytes);

		try {
			p7eBytes = PackDecoder.getSignedContent(p7mBytes);
			if (diagnostic!=null) diagnostic.setP7eBytes(p7eBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "SignVerifing", e);
		}
		
		try {
			zipBytes = PackDecoder.decrypt(localPrivateKey, p7eBytes);
			if (diagnostic!=null) diagnostic.setZipBytes(zipBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "Decrypting", e);
		}

		try {
			plain = PackDecoder.unzip(zipBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_DECODE_HEADER + "Unzipping", e);
		}
		return plain;
	}
	

	private byte[] encode(String name, X509Certificate remoteCert, X509Certificate localCert, PrivateKey localPrivateKey, byte[] plain) throws GwCrException {
		if (diagnostic!=null) {
			diagnostic.setZipBytes(null);
			diagnostic.setP7eBytes(null);
			diagnostic.setP7mBytes(null);
		} 
		byte[] zipBytes;
		byte[] p7eBytes;
		byte[] p7mBytes;
		try {
			zipBytes = PackEncoder.zip(name, plain);
			if (diagnostic!=null) diagnostic.setZipBytes(zipBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Zipping", e);
		}
		try {
			p7eBytes = PackEncoder.encrypt(remoteCert, zipBytes);
			if (diagnostic!=null) diagnostic.setP7eBytes(p7eBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Encrypting", e);
		}
		try {
			p7mBytes = PackEncoder.sign(localCert, localPrivateKey, p7eBytes);
			if (diagnostic!=null) diagnostic.setP7mBytes(p7mBytes);
		} catch (Throwable e) {
			throw new GwCrException(LOG_ENCODE_HEADER + "Signing", e);
		}
		return p7mBytes;
	}
}
