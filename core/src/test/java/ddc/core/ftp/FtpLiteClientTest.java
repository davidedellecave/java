package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import ddc.core.ftp.matcher.AndMatcher;
import ddc.core.ftp.matcher.DosWildcardsMatcher;
import ddc.core.ftp.matcher.PathMatcher;

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
		s.setRemoteLogicalDelete(true);
		return s;
	}
	
	private FtpLiteClient getClient() {
		FtpLiteClient c = new FtpLiteClient(getConfig());
		return c;
	}
	
//	@Test
//	public void testListing_1() throws FtpExceptionWrapper {
//		FtpLiteClient c = getClient();
//		c.login();
//		List<FtpFileWrapper> list = c.listing();
//		c.logout();
//		list.forEach(System.out::println);
//	}
	
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
	
	@Test
	public void testListing_6() throws FtpLiteException {
		FtpLiteClient c = getClient();
		c.connect();
		FtpFileMatcher m = new AndMatcher(new PathMatcher("Studio"), new DosWildcardsMatcher("*.ascx")) ;
		List<FtpLiteFile> list = c.listing(WORKINGPATH, m, true);
		c.disconnect();
//		list.forEach(System.out::println);
	}
	
//	@Test
//	public void testIsConnected() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLogin() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLogout() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testExists() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testListing() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testListingPath() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testListingPathBooleanBooleanBoolean() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testListingPathFtpFileMatcherBoolean() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testListingPathFtpListener() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpload() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRename() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDownloadPathPath() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDownloadPathPathBoolean() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeletePath() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeletePathBoolean() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteAll() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEqualByTimeAndSize() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEqualInTimeAndSize() {
//		fail("Not yet implemented");
//	}

}
