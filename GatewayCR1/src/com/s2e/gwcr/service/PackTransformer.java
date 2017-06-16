package com.s2e.gwcr.service;

import com.s2e.gwcr.model.Pack;

public interface PackTransformer {
	public void encode(Pack pack) throws Exception;
	public void decode(Pack pack) throws Exception;
}
