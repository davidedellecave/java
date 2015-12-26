package ddc.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;

public class HttpClientResponseWrapper {
	private String url="";
	private int statusCode=0;
	private String statusPhrase=null;
	private String body=null;
	private long bodyLength=0;
	private Map<String, String> header;
	
	public HttpClientResponseWrapper(HttpClientContext context) {
		super();
		this.url = context.getRequest().getRequestLine().getUri().toString();
		this.statusPhrase=context.getResponse().getStatusLine().getReasonPhrase();
		this.statusCode=context.getResponse().getStatusLine().getStatusCode();
		
		header =  new HashMap<String, String>();
		Header[] headers = context.getResponse().getAllHeaders();
		for (Header h : headers) {
			header.put(h.getName(), h.getValue());
		}
		try {
			body = EntityUtils.toString(context.getResponse().getEntity());
			bodyLength = context.getResponse().getEntity().getContentLength();
		} catch (ParseException | IOException e) { 
			body=null;
			bodyLength=0;
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusPhrase() {
		return statusPhrase;
	}

	public String getBody() {
		return body;
	}
	
	public long getBodyLength() {
		return bodyLength;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	
}
