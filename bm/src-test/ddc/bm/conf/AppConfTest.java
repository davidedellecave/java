package ddc.bm.conf;

import org.junit.Test;

public class AppConfTest {

	@Test
	public void testReload() {
		AppConfReader conf = new AppConfReader();
		conf.reload();
		
		PropsConf props = conf.getAppConf("dev");
		System.out.println(props);
		
		props = conf.getAppConf("test");
		System.out.println(props);

		props = conf.getAppConf("gottardo");
		System.out.println(props);

		props = conf.getAppConf("spartaco");
		System.out.println(props);

	}
	
//	@Test
//	public void testProperties() throws FileNotFoundException, IOException {
//		Properties props = new Properties();
//		props.setProperty("name1", "value1");
//		props.setProperty("name2", "value2");
//		props.setProperty("name3", "value3");
//		props.storeToXML(new FileOutputStream(new File("/users/dellecave/temp/test.properties")), "comment");
//		
//	}

}
