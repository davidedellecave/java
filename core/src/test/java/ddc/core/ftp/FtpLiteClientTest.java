package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class FtpLiteClientTest {
	private final Path WORKINGPATH=Paths.get("/");
	private FtpServer getServer() {
		FtpServer s = new FtpServer();
		s.setHost("5.134.124.246");
		s.setUsername("gottardo");
		s.setPassword("quasar-2");
		return s;
	}

	private FtpLiteConfig getConfig() {
		FtpLiteConfig s = new FtpLiteConfig();
		s.setFtpServer(getServer());
		s.setOverwriteLocal(true);
		s.setOverwriteRemote(true);
		s.setPreserveRemoteTimestamp(true);
		s.setWorkingPath(WORKINGPATH.toString());
		s.setBinaryTransfer(true);
		s.setPassiveMode(true);
		s.setDeleteRemoteLogically(true);
		return s;
	}
	
	private FtpLiteClient getClient() {
		FtpLiteClient c = new FtpLiteClient(getConfig());
		return c;
	}
	
	@Test
	public void testListing_1() throws FtpLiteException {
		FtpLiteClient c = getClient();
		c.connect();
		List<FtpLiteFile> list = c.listFiles(Paths.get("/"), true, true, true);
		c.disconnect();
		list.forEach(System.out::println);
	}
	
//	@Test
//	public void testListing_2() throws FtpExceptionWrapper {
//		FtpLiteClient c = getClient();
//		c.login();
//		List<FtpFileWrapper> list = c.listing(WORKINGPATH, false, true, false);
//		c.logout();
//		list.forEach(System.out::println);
//	}

//	@Test
//	public void testListing_3() throws FtpExceptionWrapper {
//		FtpLiteClient c = getClient();
//		c.login();
//		List<FtpFileWrapper> list = c.listing(WORKINGPATH, true, false, false);
//		c.logout();
//		list.forEach(System.out::println);
//	}
	
//	@Test
//	public void testListing_4() throws FtpExceptionWrapper {
//		FtpLiteClient c = getClient();
//		c.login();
//		List<FtpFileWrapper> list = c.listing(WORKINGPATH, true, false, true);
//		c.logout();
//		list.forEach(System.out::println);
//	}

//	@Test
//	public void testListing_5() throws FtpExceptionWrapper {
//		FtpLiteClient c = getClient();
//		c.login();
//		List<FtpFileWrapper> list = c.listing(WORKINGPATH, true, true, true);
//		c.logout();
//		list.forEach(System.out::println);
//	}
	
//	@Test
//	public void testListing_6() throws FtpLiteException {
//		FtpLiteClient c = getClient();
//		c.connect();
//		FtpFileMatcher m = new AndMatcher(new PathMatcher("Studio"), new DosWildcardsMatcher("*.ascx")) ;
//		List<FtpLiteFile> list = c.listFiles(WORKINGPATH, m, true);
//		c.disconnect();
////		list.forEach(System.out::println);
//	}
	
	@Test
	public void testGetFiles_1() throws FtpLiteException {
		FtpLiteClient c = getClient();
		c.connect();
		
//		String s ="/admin.medisportgottardo.it/log/admin.medisportgottardo.it/2016-01-06/admin.medisportgottardo.it_2016-01-06_rgqc5wz13ovudvabcyjllnji.txt";
//		String s ="/admin.medisportgottardo.it/log/admin.medisportgottardo.it/2016-01-06";
		String s ="/admin.medisportgottardo.it/log/admin.medisportgottardo.it/2015-12-01";
		
		Path p = Paths.get(s);
		FtpLiteFile f =c.getFile(p);
			
		System.out.println(f);
		
		c.disconnect();
//		list.forEach(System.out::println);
	}

}
