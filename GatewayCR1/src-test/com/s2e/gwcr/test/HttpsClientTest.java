package com.s2e.gwcr.test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.s2e.gwcr.repos.DbMock;

import ddc.commons.http.HttpLiteClientResponse;

public class HttpsClientTest {

	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		KeyStore keyStore =JcaUtils.createKeyStore("davidedc".toCharArray());
		Certificate cert = DbMock.getCert("cert.pem");
		JcaUtils.addX509CertificateToKeyStore(keyStore, "tomcat-apache", cert);
		
		CloseableHttpClient httpclient = buildHttpsClient(keyStore);
		String url = "https://tomcat-apache/GatewayCR-BdI/List";
		HttpGet httpget = new HttpGet(url);
		HttpClientContext context = HttpClientContext.create();
		CloseableHttpResponse response = httpclient.execute(httpget, context);
		try {
			System.out.println(response);
			HttpLiteClientResponse res = new HttpLiteClientResponse(context);
			System.out.println(res.getBody());
		} finally {
			response.close();
		}

	}

	private static CloseableHttpClient buildHttpsClient(KeyStore keyStore) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

		SSLConnectionSocketFactory sslConnectionFactory = buildSSLConnectionFactory(keyStore);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setSSLSocketFactory(sslConnectionFactory);
		builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

		return builder.build();
	}
	
	private static SSLConnectionSocketFactory buildSSLConnectionFactory(KeyStore keyStore) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		return sslConnectionFactory;
	}
}
