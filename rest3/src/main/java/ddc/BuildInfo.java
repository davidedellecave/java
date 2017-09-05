package ddc;

import java.io.IOException;
import java.util.Properties;

public class BuildInfo {
	public static Properties getInfo() throws IOException {
		Properties props = new Properties();
		props.load(BuildInfo.class.getResourceAsStream("../build.properties"));
		return props;
	}
}
