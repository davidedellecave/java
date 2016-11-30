package ddc.files.scan;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import ddc.util.ParseUtil;

public class ScanUtilTest {

	@Test
	public void test() throws Exception {
		Path path = Paths.get("/Users/dellecave/temp/in/");
		String d = "3 DAYS";
		long millis = ParseUtil.parseTimeAndUnit(d);
		System.out.println(millis);
		List<Path> list = ScanUtil.getFiles(path, true, millis, new String[] {}, new String[] {});
		System.out.println(list);
	}

}
