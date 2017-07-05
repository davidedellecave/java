package com.s2e.gwcr.test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

public class HttpClient {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet("https://google.com/");
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			System.out.println(response);
		} finally {
			response.close();
		}
	}

	public static void t() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			CertificateException {
		HttpClientContext clientContext = HttpClientContext.create();
		PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();
		Socket socket = sf.createSocket(clientContext);
		int timeout = 1000; // ms
		HttpHost target = new HttpHost("localhost");
		InetSocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }),
				80);
		sf.connectSocket(timeout, socket, target, remoteAddress, null, clientContext);

		KeyStore myTrustStore = null;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new File("")).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
	}

	public static CloseableHttpClient createApacheHttp4ClientWithClientCertAuth() {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext,
					new DefaultHostnameVerifier());

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory())
					.build();

			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setSSLSocketFactory(sslConnectionFactory);
			builder.setConnectionManager(new PoolingHttpClientConnectionManager(registry));

			return builder.build();
		} catch (Exception ex) {

			return null;
		}
	}

	// You need to create a keystore for the trusted CAs trust.jks (maybe a copy
	// of the cacerts from Java -default password changeit btw-) and another
	// identity.jks for the identity of the client.

	static CloseableHttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException,
			CertificateException, KeyStoreException, IOException, UnrecoverableKeyException {
		// SSLContext sslContext = SSLContexts.custom().build();
		URL jks = HttpClient.class.getClassLoader().getResource("trust.jks");
		KeyStore keyStore = KeyStore.getInstance("jks");
		keyStore.load(HttpClient.class.getClassLoader().getResourceAsStream("identity.jks"), "identity-password".toCharArray());
		SSLContext sslContext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, "identity-privatekey-password".toCharArray()) // identity
				.loadTrustMaterial(jks, "trust-password".toCharArray()) // trust
				.build();
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
				new String[] { "TLSv1.2", "TLSv1.1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
				// .setRedirectStrategy(new LaxRedirectStrategy())
				// .disableRedirectHandling() // do *not* follow redirections!
				.build();
	}

}
