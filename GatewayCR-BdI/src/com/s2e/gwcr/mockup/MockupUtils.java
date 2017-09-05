package com.s2e.gwcr.mockup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class MockupUtils {
	public static byte[] getData(String name) throws IOException {
		InputStream in = MockupUtils.class.getResourceAsStream(name);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.copy(in, out);
		return out.toByteArray();
	}

	public static String getString(String name) throws IOException {
		String s = new String(getData(name));
		return s;
	}

	public static void checkEnvVar() throws IOException {
		String envPath = getEnvPath();
		if (envPath == null || envPath.length() == 0) {
			throw new IOException("Environment var not set. name:[" + DEFAULT_ENV_VARNAME + "] value:[" + envPath+ "]");
		}
	}

	
	public static Path getReposPath() {
		Path path = Paths.get(MockupUtils.getEnvPath() + "/repos");
		return path;
	}
	
	
	public static String DEFAULT_ENV_VARNAME = "S2EP_HOME";

	public static String getEnvPath() {
		String envPath = System.getenv().get(DEFAULT_ENV_VARNAME);
		if (envPath == null || envPath.length() == 0) {
			envPath = System.getProperty(DEFAULT_ENV_VARNAME);
		} else {
			System.setProperty(DEFAULT_ENV_VARNAME, envPath);
		}
		return envPath;
	}

}
