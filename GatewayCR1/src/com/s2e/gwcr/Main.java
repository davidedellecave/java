package com.s2e.gwcr;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.s2e.gwcr.impl.SimpleMessageBuilder;
import com.s2e.gwcr.service.PackTransformer;

public class Main {

	private static String sourceFile ="/Users/davide/tmp/gatewaycr/test1.pdf";
	
	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.test1();

	}

	
	
	private void test1() throws Exception {
		Path source = Paths.get(sourceFile);
		Path dest = Paths.get(sourceFile + ".out");
		
		
//		PackTransformer builder = new SimpleMessageBuilder();
//		builder.build(source, dest);
	}
}
