package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.model.PackMetadata;
import com.s2e.gwcr.model.PackType;
import com.s2e.gwcr.model.entity.AbiContext;
import com.s2e.gwcr.repos.DbMock;

import ddc.commons.http.HttpLiteClientResponse;

public class BdiHttpClientTest {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static Pack getPackToSend() throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {

		Pack pack = new Pack();
		pack.setAbi(new AbiContext());
		pack.setRemoteCert(DbMock.getCert("bdi.crt"));
		pack.setLocalCert(DbMock.getCert("s2e.crt"));
		pack.setLocalPrivateKey(DbMock.getPrivateKey("s2e.key"));

		pack.setMedadata(new PackMetadata());
		pack.setMessageType(PackType.Message);
		pack.setName("packtosend");

		return pack;
	}
	
	private static BdiEndpoint getEndpoint() {
		BdiEndpoint endpoint = new BdiEndpoint();
		String url = "https://tomcat-apache/GatewayCR-BdI/List";
		endpoint.setEndpoint(url);
		return endpoint;
	}

	@Test
	public void test() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException {
		Pack pack = getPackToSend();
		BdiHttpClient ps = new BdiHttpClient();
		HttpLiteClientResponse res = ps.upload(getEndpoint(), getPackToSend());
		System.out.println(res);
		System.out.println(res.getBody());
	}

}
