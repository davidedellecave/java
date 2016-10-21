package ddc.bm.app;

import java.util.logging.Logger;

import org.junit.Test;

public class ApplicationTest {

	@Test
	public void test() {
		Logger log = Application.getLogger("mylog");
		
		log.info("hello log!");
	}

}
