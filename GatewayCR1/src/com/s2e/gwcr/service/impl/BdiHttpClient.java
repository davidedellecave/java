package com.s2e.gwcr.service.impl;

import java.io.ByteArrayInputStream;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.test.JcaUtils;

import ddc.commons.http.HttpLiteClientResponse;

public class BdiHttpClient {

	public HttpLiteClientResponse putData(BdiEndpoint endpoint, String filepath, byte[] data) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		return putStream(endpoint, filepath, data);
	}
	
	public HttpLiteClientResponse postJson(BdiEndpoint endpoint, String filepath, String json) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		return doPostJson(endpoint, filepath, json);
	}
	
	private HttpLiteClientResponse putStream(BdiEndpoint endpoint, String filepath, byte[] data) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient client = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint() + "/" + filepath;
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPut request = new HttpPut(url);
			entity = new InputStreamEntity(new ByteArrayInputStream(data), ContentType.APPLICATION_OCTET_STREAM);
			request.setEntity(entity);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(request, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			if (response!=null) response.close();
			if (entity!=null && entity.getContent()!=null) entity.getContent().close();
		}
		return res;
	}
	
	private HttpLiteClientResponse doPostJson(BdiEndpoint endpoint, String filepath, String json) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient client = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint() + "/" + filepath;
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPost request = new HttpPost(url);
			entity = new StringEntity(json,  ContentType.APPLICATION_JSON);
			request.setEntity(entity);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(request, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			if (response!=null) response.close();
			if (entity!=null && entity.getContent()!=null) entity.getContent().close();
		}
		return res;
	}
	
	public HttpLiteClientResponse getData(BdiEndpoint endpoint, String filepath) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient client = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint() + "/" + filepath;
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet request = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(request, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			response.close();
		}
		return res;
	}
	
	public HttpLiteClientResponse getString(BdiEndpoint endpoint) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient client = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint();
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet request = new HttpGet(url);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(request, context);
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
	
	//obsolete
	private HttpLiteClientResponse upload_Multipart(BdiEndpoint endpoint, byte[] data) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
		String tempPassword = RandomStringUtils.random(36, true, true);
		KeyStore keyStore = JcaUtils.createKeyStore(tempPassword.toCharArray());
		Certificate cert = endpoint.getHttpsCert();
		String alias = UUID.randomUUID().toString();
		JcaUtils.addX509CertificateToKeyStore(keyStore, alias, cert);

		CloseableHttpClient client = buildHttpsClient(keyStore);
		String url = endpoint.getEndpoint();
		HttpLiteClientResponse res = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPut request = new HttpPut(url);

			MultipartEntityBuilder mpeBuilder = MultipartEntityBuilder.create();
			mpeBuilder.addBinaryBody("file", data);
			entity = mpeBuilder.build();
			
			request.setEntity(entity);
			HttpClientContext context = HttpClientContext.create();
			response = client.execute(request, context);
			res = new HttpLiteClientResponse(context);
		} finally {
			if (response!=null) response.close();
			if (entity!=null && entity.getContent()!=null) entity.getContent().close();
		}
		return res;
	}
}