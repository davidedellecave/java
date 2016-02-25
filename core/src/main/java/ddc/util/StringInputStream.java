package ddc.util;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class StringInputStream extends ByteArrayInputStream {

	public StringInputStream(byte[] buf) {
		super(buf);
	}
	
	public StringInputStream(String buf) {
		super(buf.getBytes(StandardCharsets.UTF_8));

	}
}
