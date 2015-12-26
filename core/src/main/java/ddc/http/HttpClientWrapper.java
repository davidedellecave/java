package ddc.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientWrapper {
	public static final String POST_CONTENTTYPE = "application/x-www-form-urlencoded"; // "text/html;
																						// charset=ISO-8859-1";
	public static final String JSON_CONTENTTYPE = "application/json"; // "text/html;
																		// charset=ISO-8859-1";

	private int maxTotalConnection = 5;
	private int maxPerRoute = 5;
	private PoolingHttpClientConnectionManager connectionManager = null;

	/**
	 * Http client wrapper based on pool of http connection
	 */
	public HttpClientWrapper() {
	}

	/**
	 * Http client wrapper based on pool of http connection
	 * 
	 * @param maxTotalConnection
	 * @param maxPerRoute
	 */
	public HttpClientWrapper(int maxTotalConnection, int maxPerRoute) {
		this.maxTotalConnection = maxTotalConnection;
		this.maxPerRoute = maxPerRoute;
	}

	private CloseableHttpClient getClient() {
		if (connectionManager == null) {
			connectionManager = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			connectionManager.setMaxTotal(maxTotalConnection);
			// Increase default max connection per route to 20
			connectionManager.setDefaultMaxPerRoute(maxPerRoute);
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
		return httpClient;
	}

	/**
	 * Execute Http POST verb
	 * 
	 * @param url:
	 *            Plain (not url encoded) url
	 * @param data
	 * @return
	 * @throws HttpClientExceptionWrapper
	 */
	public HttpClientResponseWrapper executePostForm(String url, Map<String, String> data)
			throws HttpClientExceptionWrapper {
		try {
			return doExecutePostForm(url, data);
		} catch (URISyntaxException | IOException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executePostJson(URI uri, String json) throws HttpClientExceptionWrapper {
		try {
			return doExecutePostJson(uri, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executePutJson(URI uri, String json) throws HttpClientExceptionWrapper {
		try {
			return doExecutePutJson(uri, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}
	
	public HttpClientResponseWrapper executePostJson(String url, String json) throws HttpClientExceptionWrapper {
		try {
			return doExecutePostJson(url, json);
		} catch (URISyntaxException | IOException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executeGet(URI uri) throws HttpClientExceptionWrapper {
		try {
			return doExecuteGet(uri);
		} catch (IOException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executeGet(String url) throws HttpClientExceptionWrapper {
		try {
			URI uri = doParseURI(url, null);
			return doExecuteGet(uri);
		} catch (IOException | URISyntaxException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executeGet(String url, Map<String, String> params)
			throws HttpClientExceptionWrapper {
		try {
			URI uri = doParseURI(url, params);
			return doExecuteGet(uri);
		} catch (IOException | URISyntaxException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	public HttpClientResponseWrapper executeGet(String schema, String hostname, int port, String path,
			Map<String, String> params) throws HttpClientExceptionWrapper {
		try {
			URI uri = new URIBuilder().setScheme(schema).setHost(hostname).setPort(port).setPath(path)
					.setParameters(mapToNvp(params)).build();
			return doExecuteGet(uri);
		} catch (Exception e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	private HttpClientResponseWrapper doExecutePostForm(String url, Map<String, String> data)
			throws HttpClientExceptionWrapper, URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		URI uri = doParseURI(url, null);
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type", POST_CONTENTTYPE);
		List<NameValuePair> params = mapToNvp(data);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		HttpClientContext context = HttpClientContext.create();
		client.execute(httpPost, context);
		return new HttpClientResponseWrapper(context);
	}

	private HttpClientResponseWrapper doExecutePostJson(String url, String json)
			throws URISyntaxException, ClientProtocolException, IOException, HttpClientExceptionWrapper {
		URI uri = doParseURI(url, null);
		return doExecutePostJson(uri, json);
	}

	private HttpClientResponseWrapper doExecutePutJson(URI uri, String json)
			throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpPut http = new HttpPut(uri);
		http.setHeader("Content-type", JSON_CONTENTTYPE);
		http.setEntity(new StringEntity(json));
		HttpClientContext context = HttpClientContext.create();
		client.execute(http, context);
		return new HttpClientResponseWrapper(context);
	}
	
	private HttpClientResponseWrapper doExecutePostJson(URI uri, String json)
			throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-type", JSON_CONTENTTYPE);
		httpPost.setEntity(new StringEntity(json));
		HttpClientContext context = HttpClientContext.create();
		client.execute(httpPost, context);
		return new HttpClientResponseWrapper(context);
	}

	private HttpClientResponseWrapper doExecuteGet(URI uri) throws ClientProtocolException, IOException {
		CloseableHttpClient client = getClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpClientContext context = HttpClientContext.create();
		client.execute(httpGet, context);
		return new HttpClientResponseWrapper(context);
	}

	/**
	 * Parse the url to get the URI
	 * @param url: the uri without the query string (parameter after ?)
	 * @param params: the parameters to build the query string (parameter after ?)
	 * @throws HttpClientExceptionWrapper
	 */
	public static URI parseURI(String url, Map<String, String> params) throws HttpClientExceptionWrapper {
		try {
			return doParseURI(url, params);
		} catch (URISyntaxException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}

	/**
	 * Parse the url to get the URI
	 * @param url: the url to parse. The query string might be url-encoded or not-url-enconded 
	 * @throws HttpClientExceptionWrapper
	 */
	public static URI parseURI(String url) throws HttpClientExceptionWrapper {
		try {
			return doParseURI(url, null);
		} catch (URISyntaxException e) {
			throw new HttpClientExceptionWrapper(e);
		}
	}
	
	private static URI doParseURI(String url, Map<String, String> params) throws URISyntaxException, HttpClientExceptionWrapper {
		if (url.indexOf('?') > 0 && params!=null && params.size()>0) {
			throw new HttpClientExceptionWrapper("URI already contains parameters as query string");
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

	// NOT TESTED !!!!!!!!
	private static String executePostFile(String postUrl, Map<String, String> params, Map<String, String> files)
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = null;
		InputStream is = null;
		String results = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			HttpPost httppost = new HttpPost(postUrl);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			if (params != null) {
				for (String key : params.keySet()) {
					StringBody value = new StringBody(params.get(key), ContentType.TEXT_PLAIN);
					builder.addPart(key, value);
				}
			}

			if (files != null && files.size() > 0) {
				for (String key : files.keySet()) {
					String value = files.get(key);
					FileBody body = new FileBody(new File(value));
					builder.addPart(key, body);
				}
			}

			HttpEntity reqEntity = builder.build();
			httppost.setEntity(reqEntity);

			response = httpclient.execute(httppost);
			// assertEquals(200, response.getStatusLine().getStatusCode());

			// HttpEntity entity = response.getEntity();
			// if (entity != null) {
			// is = entity.getContent();
			// StringWriter writer = new StringWriter();
			// IOUtils.copy(is, writer, "UTF-8");
			// results = writer.toString();
			// }

		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Throwable t) {
				// No-op
			}

			try {
				if (response != null) {
					response.close();
				}
			} catch (Throwable t) {
				// No-op
			}

			httpclient.close();
		}

		return results;
	}
}
