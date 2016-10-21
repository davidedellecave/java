package ddc.core.http;

import static org.junit.Assert.*;

import org.junit.Test;

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
