package ddc.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class HttpLiteClient {
	public static final String POST_CONTENTTYPE = "application/x-www-form-urlencoded"; // "text/html;
																						// charset=ISO-8859-1";
	public static final String JSON_CONTENTTYPE = "application/json"; // "text/html;
																		// charset=ISO-8859-1";

	private int maxTotalConnection = 5;
	private int maxPerRoute = 5;
	private PoolingHttpClientConnectionManager connectionManager = null;	//
	private ProxyConfig proxyConfig = null;
//	private String username = null;
//	private String password = null;

	/**
	 * Http client wrapper based on pool of http connection
	 */
	public HttpLiteClient() {
	}

	/**
	 * Http client wrapper based on pool of http connection
	 * 
	 * @param maxTotalConnection
	 * @param maxPerRoute
	 */
	public HttpLiteClient(int maxTotalConnection, int maxPerRoute) {
		this.maxTotalConnection = maxTotalConnection;
		this.maxPerRoute = maxPerRoute;
	}

	private CloseableHttpClient getClient() {
		HttpClientBuilder httpBuilder = HttpClients.custom();
		if (connectionManager == null) {
			connectionManager = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			connectionManager.setMaxTotal(maxTotalConnection);
			// Increase default max connection per route to 20
			connectionManager.setDefaultMaxPerRoute(maxPerRoute);
			httpBuilder.setConnectionManager(connectionManager);
		}
//		if (username!=null && password!=null) {
//			CredentialsProvider credsProvider = new BasicCredentialsProvider();
//			
//			credsProvider.setCredentials( (new AuthScope(, authProxy.getPort()), new UsernamePasswordCredentials(authProxy.getUsername(), authProxy.getPassword()));
//			httpBuilder.setDefaultCredentialsProvider(credsProvider);
//			httpBuilder.set
//		}
		if (proxyConfig != null) {
			if (proxyConfig instanceof AuthProxyConfig) {
				AuthProxyConfig authProxy = (AuthProxyConfig) proxyConfig;
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(authProxy.getHost(), authProxy.getPort()), new UsernamePasswordCredentials(authProxy.getUsername(), authProxy.getPassword()));
				httpBuilder.setDefaultCredentialsProvider(credsProvider);
			} 		
		}		    
		return httpBuilder.build();
	}

	/**
	 * Execute Http POST verb
	 * 
	 * @param url:
	 *            Plain (not url encoded) url
	 * @param data
	 * @return
	 * @throws HttpLiteClientException
	 */
	public HttpLiteClientResponse executePostForm(String url, Map<String, String> data) throws HttpLiteClientException {
		try {
			return doExecutePostForm(url, data);
		} catch (URISyntaxException | IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executePostJson(URI uri, String json) throws HttpLiteClientException {
		try {
			return doExecutePostJson(uri, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executePutJson(URI uri, String json) throws HttpLiteClientException {
		try {
			return doExecutePutJson(uri, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executePostJson(String url, String json) throws HttpLiteClientException {
		try {
			return doExecutePostJson(url, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executeGet(URI uri) throws HttpLiteClientException {
		try {
			return doExecuteGet(uri);
		} catch (IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executeGet(String url) throws HttpLiteClientException {
		try {
			URI uri = doParseURI(url, null);
			return doExecuteGet(uri);
		} catch (IOException | URISyntaxException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executeGet(String url, Map<String, String> params) throws HttpLiteClientException {
		try {
			URI uri = doParseURI(url, params);
			return doExecuteGet(uri);
		} catch (IOException | URISyntaxException e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executeGet(String schema, String hostname, int port, String path, Map<String, String> params) throws HttpLiteClientException {
		try {
			URI uri = new URIBuilder().setScheme(schema).setHost(hostname).setPort(port).setPath(path).setParameters(mapToNvp(params)).build();
			return doExecuteGet(uri);
		} catch (Exception e) {
			throw new HttpLiteClientException(e);
		}
	}

	public HttpLiteClientResponse executePutStream(String url, InputStream is) throws HttpLiteClientException {
		try {
			return doExecutePutStream(url, is);
		} catch (URISyntaxException | IOException e) {
			throw new HttpLiteClientException(e);
		}
	}

//	public ProxyConfig getProxyConfig() {
//		return proxyConfig;
//	}
//
//	public void setProxyConfig(ProxyConfig proxyConfig) {
//		this.proxyConfig = proxyConfig;
//	}

	public void setProxyConfig(String host, int port, String protocol) {
		proxyConfig = new ProxyConfig();
		proxyConfig.setHost(host).setPort(port).setProtocol(protocol);
	}

	public void setProxyConfig(String host, int port, String protocol, String username, String password) {
		AuthProxyConfig c = new AuthProxyConfig();
		c.setHost(host).setPort(port).setProtocol(protocol);
		c.setUsername(username).setPassword(password);
		proxyConfig = c;
	}

	
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	private void setRequestConfig(final HttpRequestBase request) {
		if (proxyConfig != null) {
			HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort(), proxyConfig.getProtocol());
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			request.setConfig(config);
		}
	}	

	private HttpLiteClientResponse doExecutePutStream(String url, InputStream is) throws URISyntaxException, HttpLiteClientException, ClientProtocolException, IOException {

		URI uri = doParseURI(url, null);
		HttpPut request = new HttpPut(uri);
		setRequestConfig(request);

		// RequestConfig requestConfig = RequestConfig.custom()
		// .setConnectionRequestTimeout(1000 * 60 * 10)
		// .setConnectTimeout(1000 * 60 * 10)
		// .setSocketTimeout(1000 * 60 * 10)
		// .build();
		//
		// httpRequest.setConfig(requestConfig);

		HttpEntity entity = new InputStreamEntity(is, ContentType.APPLICATION_OCTET_STREAM);
		request.setEntity(entity);
		CloseableHttpClient client = getClient();
		HttpClientContext context = HttpClientContext.create();
		client.execute(request, context);
		return new HttpLiteClientResponse(context);
	}

	private HttpLiteClientResponse doExecutePostForm(String url, Map<String, String> data) throws HttpLiteClientException, URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		URI uri = doParseURI(url, null);
		HttpPost request = new HttpPost(uri);
		setRequestConfig(request);
		request.setHeader("Content-Type", POST_CONTENTTYPE);
		List<NameValuePair> params = mapToNvp(data);
		request.setEntity(new UrlEncodedFormEntity(params));
		HttpClientContext context = HttpClientContext.create();
		client.execute(request, context);
		return new HttpLiteClientResponse(context);
	}

	private HttpLiteClientResponse doExecutePostJson(String url, String json) throws URISyntaxException, ClientProtocolException, IOException, HttpLiteClientException {
		URI uri = doParseURI(url, null);
		return doExecutePostJson(uri, json);
	}

	private HttpLiteClientResponse doExecutePutJson(URI uri, String json) throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpPut request = new HttpPut(uri);
		setRequestConfig(request);
		request.setHeader("Content-type", JSON_CONTENTTYPE);
		request.setEntity(new StringEntity(json));
		HttpClientContext context = HttpClientContext.create();
		client.execute(request, context);
		return new HttpLiteClientResponse(context);
	}

	private HttpLiteClientResponse doExecutePostJson(URI uri, String json) throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpPost request = new HttpPost(uri);
		setRequestConfig(request);
		request.setHeader("Content-type", JSON_CONTENTTYPE);
		request.setEntity(new StringEntity(json));
		HttpClientContext context = HttpClientContext.create();
		client.execute(request, context);
		return new HttpLiteClientResponse(context);
	}

	private HttpLiteClientResponse doExecuteGet(URI uri) throws ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpGet request = new HttpGet(uri);
		setRequestConfig(request);
		HttpClientContext context = HttpClientContext.create();
		client.execute(request, context);
		return new HttpLiteClientResponse(context);
	}

	/**
	 * Parse the url to get the URI
	 * 
	 * @param url:
	 *            the uri without the query string (parameter after ?)
	 * @param params:
	 *            the parameters to build the query string (parameter after ?)
	 * @throws HttpLiteClientException
	 */
	public static URI parseURI(String url, Map<String, String> params) throws HttpLiteClientException {
		try {
			return doParseURI(url, params);
		} catch (URISyntaxException e) {
			throw new HttpLiteClientException(e);
		}
	}

	/**
	 * Parse the url to get the URI
	 * 
	 * @param url:
	 *            the url to parse. The query string might be url-encoded or
	 *            not-url-enconded
	 * @throws HttpLiteClientException
	 */
	public static URI parseURI(String url) throws HttpLiteClientException {
		try {
			return doParseURI(url, null);
		} catch (URISyntaxException e) {
			throw new HttpLiteClientException(e);
		}
	}

	private static URI doParseURI(String url, Map<String, String> params) throws URISyntaxException, HttpLiteClientException {
		if (url.indexOf('?') > 0 && params != null && params.size() > 0) {
			throw new HttpLiteClientException("URI already contains parameters as query string");
		}
		if (!url.toLowerCase().startsWith("http")) {
			url = "http://" + url;
		}
		URI uri = URI.create(url);
		if (params != null) {
			URIBuilder b = new URIBuilder();
			b.setScheme(uri.getScheme());
			b.setHost(uri.getHost());
			b.setPort(uri.getPort());
			b.setPath(uri.getPath());
			b.setParameters(mapToNvp(params));
			uri = b.build();
		}
		return uri;
	}

	private static ArrayList<NameValuePair> mapToNvp(Map<String, String> data) {
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		if (data == null)
			return nvp;
		for (Iterator<?> it = data.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Object> el = (Map.Entry<Object, Object>) it.next();
			NameValuePair nvp1 = new BasicNameValuePair(el.getKey() + "", el.getValue() + "");
			nvp.add(nvp1);
		}
		return nvp;
	}

	// // NOT TESTED !!!!!!!!
	// private static String executePostFile(String postUrl, Map<String, String>
	// params, Map<String, String> files)
	// throws ClientProtocolException, IOException {
	// CloseableHttpResponse response = null;
	// InputStream is = null;
	// String results = null;
	// CloseableHttpClient httpclient = HttpClients.createDefault();
	// try {
	//
	// HttpPost httppost = new HttpPost(postUrl);
	//
	// MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	//
	// if (params != null) {
	// for (String key : params.keySet()) {
	// StringBody value = new StringBody(params.get(key),
	// ContentType.TEXT_PLAIN);
	// builder.addPart(key, value);
	// }
	// }
	//
	// if (files != null && files.size() > 0) {
	// for (String key : files.keySet()) {
	// String value = files.get(key);
	// FileBody body = new FileBody(new File(value));
	// builder.addPart(key, body);
	// }
	// }
	//
	// HttpEntity reqEntity = builder.build();
	// httppost.setEntity(reqEntity);
	//
	// response = httpclient.execute(httppost);
	// // assertEquals(200, response.getStatusLine().getStatusCode());
	//
	// // HttpEntity entity = response.getEntity();
	// // if (entity != null) {
	// // is = entity.getContent();
	// // StringWriter writer = new StringWriter();
	// // IOUtils.copy(is, writer, "UTF-8");
	// // results = writer.toString();
	// // }
	//
	// } finally {
	// try {
	// if (is != null) {
	// is.close();
	// }
	// } catch (Throwable t) {
	// // No-op
	// }
	//
	// try {
	// if (response != null) {
	// response.close();
	// }
	// } catch (Throwable t) {
	// // No-op
	// }
	//
	// httpclient.close();
	// }
	//
	// return results;
	// }

	// public class FileRequestEntity implements RequestEntity {
	//
	// private File file = null;
	//
	// public FileRequestEntity(File file) {
	// super();
	// this.file = file;
	// }
	//
	// public boolean isRepeatable() {
	// return true;
	// }
	//
	// public String getContentType() {
	// return "text/plain; charset=UTF-8";
	// }
	//
	// public void writeRequest(OutputStream out) throws IOException {
	// InputStream in = new FileInputStream(this.file);
	// try {
	// int l;
	// byte[] buffer = new byte[1024];
	// while ((l = in.read(buffer)) != -1) {
	// out.write(buffer, 0, l);
	// }
	// } finally {
	// in.close();
	// }
	// }
	//
	// public long getContentLength() {
	// return file.length();
	// }
	// }

	// File myfile = new File("myfile.txt");
	// PostMethod httppost = new PostMethod("/stuff");
	// httppost.setRequestEntity(new FileRequestEntity(myfile));
}
