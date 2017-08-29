package com.s2e.gwcr.service;

import com.s2e.gwcr.model.BdiEndpoint;
import com.s2e.gwcr.model.BdiFileList;
import com.s2e.gwcr.model.GwCrException;
import com.s2e.gwcr.model.Pack;

public interface TransmitterService {
	public void upload(BdiEndpoint endpoint, Pack pack) throws GwCrException;
	public void download(BdiEndpoint endpoint, Pack pack) throws GwCrException;
	public BdiFileList listFile(BdiEndpoint endpoint) throws GwCrException;
}
