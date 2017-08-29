package com.s2e.gwcr.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.BdiFile;
import com.s2e.gwcr.model.BdiFileList;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.service.PackService;
import com.s2e.gwcr.service.TransmitterService;

import ddc.commons.http.HttpLiteClientResponse;
import ddc.commons.jack.JackUtil;

public class TransmitterServiceImpl implements TransmitterService {
	private static final Logger logger = LogManager.getLogger(TransmitterServiceImpl.class);

	@Override
	public void upload(BdiEndpoint endpoint, Pack pack) throws GwCrException {
		try {
			doUpload(endpoint, pack);
		} catch (Throwable e) {
			logger.error("upload exception - {}", e.getMessage());
			throw new GwCrException(e);
		}
	}

	@Override
	public void download(BdiEndpoint endpoint, Pack pack) throws GwCrException {
		try {
			doDownload(endpoint, pack);
		} catch (Throwable e) {
			logger.error("download exception - {}", e.getMessage());
			throw new GwCrException(e);
		}
	}
	
	@Override
	public BdiFileList listFile(BdiEndpoint endpoint) throws GwCrException {
		try {
			return doFilelist(endpoint);
		} catch (Throwable e) {
			logger.error("listFile exception - {}", e.getMessage());
			throw new GwCrException(e);
		}
	}
	
	private void doUpload(BdiEndpoint endpoint, Pack pack) throws Exception  {
		PackService ps = new PackServiceImpl();
		ps.encode(pack);		
		//
		String filepath = pack.getPath() + "/" + pack.getName();
		BdiHttpClient httpClient = new BdiHttpClient();
		HttpLiteClientResponse res1 = httpClient.putData(endpoint, filepath, pack.getData());
		if (!res1.isStatusCodeOk()) {
			throw new GwCrException("Upload data error - " + res1.getStatusCode() + " - " + res1.getStatusPhrase());
		}		
		String jsonMetadata = JackUtil.convAsString(pack.getMedadata());				
		HttpLiteClientResponse res2 = httpClient.postJson(endpoint, filepath, jsonMetadata);
		if (!res2.isStatusCodeOk()) {
			throw new GwCrException("Upload json error - " + res2.getStatusCode() + " - " + res2.getStatusPhrase());
		}	
	}
	
	private void doDownload(BdiEndpoint endpoint, Pack pack) throws Exception  {
		String filepath = pack.getPath() + "/" + pack.getName();
		BdiHttpClient httpClient = new BdiHttpClient();
		HttpLiteClientResponse res = httpClient.getData(endpoint, filepath);
		pack.setData(res.getData());
		//
		PackService ps = new PackServiceImpl();
		ps.decode(pack);		
	}



	private BdiFileList doFilelist(BdiEndpoint endpoint) throws Exception {
		BdiHttpClient client = new BdiHttpClient();
		HttpLiteClientResponse res = client.getString(endpoint);
		if (res.getStatusCode() != 200) {
			throw new GwCrException("filelist - error - Http Status Code - " + res.getStatusCode());
		}
		String json = res.getBody();
		logger.debug("listFile - json:[" + json + "]");
		ObjectMapper mapper = new ObjectMapper();
		BdiFileList list = (BdiFileList) mapper.readValue(json, BdiFileList.class);
		logger.info("listFile - file#:[" + list.getFiles().size() + "]");
		for (BdiFile f : list.getFiles()) {			
			f.setPath(endpoint.getEndpoint());			
			if (f.isDirectory()) {
				logger.warn("listFile - deep scan not implemented - directory:{}",  f.toString());	
			} else {
				logger.info("listFile - file:{}", f.toString());	
			}				
		}
		
		return list;
	}

}
