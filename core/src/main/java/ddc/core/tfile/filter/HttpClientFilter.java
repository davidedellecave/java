package ddc.core.tfile.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ddc.core.http.HttpLiteClient;
import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;
import ddc.util.Chronometer;

public class HttpClientFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(HttpClientFilter.class);

	private String url = null;
	private int bulkSize = 1;
	private HttpBulkClientFilter bulkClient = null;
	private HttpCallback httpCallback = null;

	public HttpClientFilter(String url, int bulkSize) {
		this.url = url;
		this.bulkSize = bulkSize;
	}

	private HttpBulkClientFilter getBulkClient() {
		if (bulkClient == null) {
			httpCallback = new HttpCallback(this.getContext());
			bulkClient = new HttpBulkClientFilter(new HttpLiteClient(), url, bulkSize, httpCallback);
		}
		return bulkClient;
	}

	// @Override
	// public void onOpen(TFileContext context) throws TFileException {
	// super.onOpen(context);
	// }

	@Override
	public void onClose(TFileContext context) throws TFileException {
		super.onClose(context);
		if (bulkClient != null) {
			bulkClient.bulkFlush();
		}
	}

	@Override	
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		if (StringUtils.isNotBlank(sourceLine)) {
			getBulkClient().bulkSend(sourceLine.toString());
		}
	}

	private class HttpCallback implements HttpBulkCallback {
		private Chronometer chron = new Chronometer();
		private TFileContext context;
//		private long expectedProcessed = 0;

		public HttpCallback(TFileContext context) {
			this.context = context;
		}

		@Override
		public void notifyStart(long size) {
			chron.start();
//			expectedProcessed = size;
			// System.out.println("Bulk processing - start - size#:[" + size +
			// "]");
		}
		
		//{"elapsed":6,"processed":9,"api":"it.ame.ih.api.abilityart.DCustomer","failed":0,"succedeed":9,"timestamp":1454320815277}
		//{"line":1,"error_code":301,"error":"Expected a ':' after a key at 607 [character 608 line 1]"}
		@Override
		public void notifyBulkProcessed(String[] jsonl) throws TFileException {
			if (jsonl.length == 0) {
				throw new TFileException("Bulk processing - First line not found");
			}
			String firstLine = jsonl[0];
			logger.info("Bulk processing - result:[" + firstLine + "]");
			try {
				JSONObject jo = new JSONObject(firstLine);
				int processed = jo.getInt("processed");
				int succedeed = jo.getInt("succedeed");
				int failed = jo.getInt("failed");
				if (failed>0) {
					logger.warn("Bulk processing - Found failed items - failed#:[" + failed + "] succedeed#:[" + succedeed + "] processed#:[" + processed + "] ");
				}
			} catch (JSONException e) {
				throw new TFileException("Bulk processing - Error on first line:[" + e.getMessage() + "]");
			}
			for (int i = 1; i < jsonl.length; i++) {
				String json = jsonl[i];
				try {
					JSONObject jo = new JSONObject(json);
					int errorCode  = jo.getInt("error_code");					
					if (errorCode != 0) {
						String error = jo.getString("error");
						int line = jo.getInt("line");
						long currentLine = context.getProcessed() - jsonl.length + line;
						getContext().onError(currentLine, json, new TFileException(error));
					}
				} catch (JSONException e) {
					throw new TFileException("Bulk processing - JSON error :[" + e.getMessage() + "]");
				}
			}
		}
	}
}
