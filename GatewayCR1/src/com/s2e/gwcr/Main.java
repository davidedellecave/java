package com.s2e.gwcr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.s2e.gwcr.service.TransmitterService;
import com.s2e.gwcr.service.impl.TransmitterServiceImpl;

public class Main {

	private static String sourceFile ="/Users/davide/tmp/gatewaycr/test1.pdf";
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.test1();

	}

	
	
	private void test1() throws Exception {
		logger.debug("start");
		TransmitterService t = new TransmitterServiceImpl();
		logger.debug("end");
//		t.filelist(endpoint)
	}
}
