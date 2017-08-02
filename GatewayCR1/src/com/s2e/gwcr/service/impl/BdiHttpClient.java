package com.s2e.gwcr.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.test.JcaUtils;

import ddc.commons.http.HttpLiteClientResponse;

public class BdiHttpClient {

	public HttpLiteClientResponse httpGet(BdiEndpoint endpoint) throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

		CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
//		HttpGet httpGet = new HttpGet(endpoint.getEndpoint());
////		httpGet.setHeader("Accept", "application/xml");
//
//		HttpResponse response = client.execute(httpGet);
		
		String url = endpoint.getEndpoint();
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(httpget, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			response.close();
		}
		return res;
		
	}

	public HttpLiteClientResponse httpGet_OLD(BdiEndpoint endpoint) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient httpclient = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint();
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = httpclient.execute(httpget, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			response.close();
		}
		return res;
	}

	public HttpLiteClientResponse upload(BdiEndpoint endpoint, Pack pack) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient httpclient = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint();
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = httpclient.execute(httpget, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			response.close();
		}
		return res;
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
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
		return sslConnectionFactory;
	}
}