package com.s2e.gwcr.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.s2e.gwcr.repos.DbMock;

import ddc.commons.http.HttpLiteClientResponse;

public class HttpClient {

	public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
		
		KeyStore keyStore =JcaUtils.createKeyStore("davidedc".toCharArray());
		Certificate cert = DbMock.getCert("apache-tomcat.pem");
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

	public static void main_OLD_1(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
		// String certFile =
		// "/Users/davide/git/java/GatewayCR1/src-test/com/s2e/gwcr/test/bdi.crt";
		InputStream stream = DbMock.class.getResourceAsStream("gatewaycr.keystore");
		KeyStore keyStore = JcaUtils.loadKeyStore(stream, null);

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

	public static void main_OLD_2(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
		// String certFile =
		// "/Users/davide/git/java/GatewayCR1/src-test/com/s2e/gwcr/test/bdi.crt";
		String keyStorePath = "/Users/davide/git/java/GatewayCR1/src-test/com/s2e/gwcr/test/gatewaycr.keystore";
		File keyStoreFile = new File(keyStorePath);
		KeyStore keyStore = JcaUtils.loadKeyStore(keyStoreFile);

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


	public static CloseableHttpClient buildHttpsClient(KeyStore keyStore) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

		SSLConnectionSocketFactory sslConnectionFactory = buildSSLConnectionFactory(keyStore);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setSSLSocketFactory(sslConnectionFactory);
		builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

		return builder.build();
	}

	private static CloseableHttpClient buildHttpClient_OLD() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		// String certFile =
		// "/Users/davide/git/java/GatewayCR1/src-test/com/s2e/gwcr/test/bdi.crt";
		String keyStorePath = "/Users/davide/ddc/src/eclipse-workspace/S2EP/conf/gatewaycr";
		File keyStoreFile = new File(keyStorePath);
		// SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustMaterial,
		// new TrustSelfSignedStrategy()).build();
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStoreFile).build();
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setSSLSocketFactory(sslConnectionFactory);
		builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

		return builder.build();
	}

	public static SSLConnectionSocketFactory buildSSLConnectionFactory(KeyStore keyStore) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
		return sslConnectionFactory;
	}

	private static CloseableHttpClient buildHttpClient_old() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		String certFile = "/Users/davide/git/java/GatewayCR1/src-test/com/s2e/gwcr/test/bdi.crt";
		String keyStore = "/Users/davide/ddc/src/eclipse-workspace/S2EP/conf/gatewaycr";
		File trustMaterial = new File(keyStore);

		// SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustMaterial,
		// new TrustSelfSignedStrategy()).build();

		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustMaterial).build();

		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setSSLSocketFactory(sslConnectionFactory);
		builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

		return builder.build();

	}

	// public static CloseableHttpClient createApacheHttp4ClientWithClientCertAuth()
	// {
	// try {
	// SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new
	// TrustSelfSignedStrategy()).build();
	//
	// SSLConnectionSocketFactory sslConnectionFactory = new
	// SSLConnectionSocketFactory(sslContext,
	// new NoopHostnameVerifier());
	//
	//// SSLConnectionSocketFactory sslConnectionFactory = new
	// SSLConnectionSocketFactory(sslContext);
	//
	// Registry<ConnectionSocketFactory> registry =
	// RegistryBuilder.<ConnectionSocketFactory>create()
	// .register("https", sslConnectionFactory).register("http", new
	// PlainConnectionSocketFactory())
	// .build();
	//
	// HttpClientBuilder builder = HttpClientBuilder.create();
	// builder.setSSLSocketFactory(sslConnectionFactory);
	// builder.setConnectionManager(new
	// PoolingHttpClientConnectionManager(registry));
	//
	// return builder.build();
	// } catch (Exception ex) {
	//
	// return null;
	// }
	// }

	// public static void t() throws IOException, KeyManagementException,
	// NoSuchAlgorithmException, KeyStoreException,
	// CertificateException {
	// HttpClientContext clientContext = HttpClientContext.create();
	// PlainConnectionSocketFactory sf =
	// PlainConnectionSocketFactory.getSocketFactory();
	// Socket socket = sf.createSocket(clientContext);
	// int timeout = 1000; // ms
	// HttpHost target = new HttpHost("localhost");
	// InetSocketAddress remoteAddress = new
	// InetSocketAddress(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }),
	// 80);
	// sf.connectSocket(timeout, socket, target, remoteAddress, null,
	// clientContext);
	//
	// KeyStore myTrustStore = null;
	// SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new
	// File("")).build();
	// SSLConnectionSocketFactory sslsf = new
	// SSLConnectionSocketFactory(sslContext);
	// }

	// You need to create a keystore for the trusted CAs trust.jks (maybe a copy
	// of the cacerts from Java -default password changeit btw-) and another
	// identity.jks for the identity of the client.

	// static CloseableHttpClient getHttpClient_1() throws NoSuchAlgorithmException,
	// KeyManagementException,
	// CertificateException, KeyStoreException, IOException,
	// UnrecoverableKeyException {
	// // SSLContext sslContext = SSLContexts.custom().build();
	// URL jks = HttpClient.class.getClassLoader().getResource("trust.jks");
	// KeyStore keyStore = KeyStore.getInstance("jks");
	// keyStore.load(HttpClient.class.getClassLoader().getResourceAsStream("identity.jks"),
	// "identity-password".toCharArray());
	// SSLContext sslContext = SSLContexts.custom()
	// .loadKeyMaterial(keyStore, "identity-privatekey-password".toCharArray()) //
	// identity
	// .loadTrustMaterial(jks, "trust-password".toCharArray()) // trust
	// .build();
	// SSLConnectionSocketFactory sslConnectionSocketFactory = new
	// SSLConnectionSocketFactory(sslContext,
	// new String[] { "TLSv1.2", "TLSv1.1" }, null,
	// SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	// return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
	// // .setRedirectStrategy(new LaxRedirectStrategy())
	// // .disableRedirectHandling() // do *not* follow redirections!
	// .build();
	// }

}
