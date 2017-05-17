package ddc.core.http;

import org.junit.Test;

import ddc.commons.http.HttpLiteClient;
import ddc.commons.http.HttpLiteClientException;
import ddc.commons.http.HttpLiteClientResponse;

public class HttpLiteClientTest {

	@Test
	public void test() throws HttpLiteClientException {
		HttpLiteClient client = new HttpLiteClient();
		
		client.setProxyConfig("webproxy.to", 80, "http", "username", "password");
		HttpLiteClientResponse res = client.executeGet("www.repubblica.it");
		System.out.println(res);
		System.out.println(res.getBody());
		
	}

}
