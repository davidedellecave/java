package ddc.core.tfile.filter;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ddc.core.http.HttpLiteClient;
import ddc.core.http.HttpLiteClientException;
import ddc.core.http.HttpLiteClientResponse;
import ddc.core.tfile.TFileException;

public class HttpBulkClientFilter {
	private int bulksize=1;
	private long itemCounter=0;
	private List<String> items = new LinkedList<>();
	private HttpLiteClient client = null;
	private String url = null; 
	private HttpBulkCallback callback;
	
	public HttpBulkClientFilter(HttpLiteClient client, String url, int bulkSize, HttpBulkCallback callback) {
		this.client=client;
		this.url=url;
		this.bulksize=bulkSize;
		this.callback=callback;
	}
	
	public void bulkSend(String item) throws TFileException {
		items.add(item);
		itemCounter++;
		if (itemCounter>=bulksize) {
			bulkFlush();
		}
	}
	
	public void bulkFlush() throws TFileException {
		doSend();
		items.clear();
		itemCounter=0;
	}
	
	private void doSend() throws TFileException {
		callback.notifyStart(items.size());
		String data = StringUtils.join(items, "\n");
		HttpLiteClientResponse r = null;
		try {
			r = client.executePostJson(url, data);
		} catch (HttpLiteClientException e) {
			throw new TFileException(e);
		}
		if (r.getStatusCode()==200) {
			String[] lines = StringUtils.split(r.getBody(),'\n'); 
			callback.notifyBulkProcessed(lines);		
		} else {
			throw new TFileException(r.getStatusCode() + " - " + r.getStatusPhrase() + " - " + r.getBody());
		}
	}
}
