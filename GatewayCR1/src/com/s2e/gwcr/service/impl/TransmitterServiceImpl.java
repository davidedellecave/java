package com.s2e.gwcr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.BdiFile;
import com.s2e.gwcr.model.BdiFileList;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;
import com.s2e.gwcr.service.TransmitterService;

import ddc.commons.http.HttpLiteClientResponse;

public class TransmitterServiceImpl implements TransmitterService {

	@Override
	public void upload(BdiEndpoint endpoint, Pack pack) {
		// TODO Auto-generated method stub
	}

	@Override
	public void download(BdiEndpoint endpoint, Pack pack) {
		// TODO Auto-generated method stub

	}

	@Override
	public BdiFileList filelist(BdiEndpoint endpoint) throws GwCrException {
		try {
			return doFilelist(endpoint);
		} catch (Throwable e) {
			throw new GwCrException(e);
		}
	}

	private BdiFileList doFilelist(BdiEndpoint endpoint) throws Exception {
		BdiHttpClient client = new BdiHttpClient();
		HttpLiteClientResponse res = client.httpGet(endpoint);
		if (res.getStatusCode() != 200) {
			throw new GwCrException("filelist - error - Http Status Code - " + res.getStatusCode());
		}
		String json = res.getBody();
		System.out.println(json);

		// json = "['filename':'pippo', 'filename' : 'pluto']";

		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		BdiFileList list = (BdiFileList) mapper.readValue(json, BdiFileList.class);
		for (BdiFile f : list.getFiles()) {
			f.setPath(endpoint.getEndpoint());
		}
		return list;
	}

}
