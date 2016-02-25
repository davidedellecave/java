package ddc.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class FtpClientWrapperTest {

	@Before
	public void setUp() throws Exception {
	}
	
	private FtpServer getServer() {
		FtpServer s = new FtpServer();
		s.setHost("ftp.mondadori.it");
		s.setUsername("cmt-mondadori");
		s.setPassword("HWKEjT2PZyGE");		
		return s;
	}

	private FtpConfigWrapper getConfig() {
		FtpConfigWrapper c = new FtpConfigWrapper();
		c.setBinaryTransfer(true);
		c.setPassiveMode(false);
		c.setPreserveRemoteTimestamp(true);
		c.setOverwriteLocal(true);
		c.setOverwriteRemote(true);
		c.setFtpServer(getServer());
		return c;
	}
	
	@Test
	public void testFtpClientWrapper_1() {
		FtpConfigWrapper c = getConfig();
		FtpClientWrapper ftp = new FtpClientWrapper(c);
		try {
			ftp.login();
//			System.out.println("---------");
//			List<FtpFileWrapper> listing = ftp.listing(Paths.get("/SPAM/INFOHUB/to-process/*tcustomer*"));
//			listing.forEach((FtpFileWrapper f) -> {System.out.println(f.getRemotePath().toString());});
//			System.out.println("---------");
			
			Path localPath = Paths.get("/usr/local/infohub_store/in/test.csv");
			Path remotePath = Paths.get("/SPAM/INFOHUB/to-process/test.csv");
			ftp.upload(localPath, remotePath);
			
			//
			ftp.delete(remotePath, true);		
			
//			Path downloadPath = Paths.get("/usr/local/infohub_store/in");
//			for (FtpFileWrapper f : listing) {
//				Path localPath = downloadPath.resolve(f.getRemotePath().getFileName());
//				ftp.download(f.getRemotePath(), localPath);
//			}
			
//			Path localDir = Paths.get("/usr/local/infohub_store/out");
//			String[] dirs = new File(localDir.toString()).list();
//			Path remoteDir = Paths.get("/SPAM/INFOHUB/processed/");
//			for (String f : dirs) {
//				Path localFile = localDir.resolve(f);
//				Path remoteFile = remoteDir.resolve(f);
//				ftp.upload(localFile, remoteFile);
//			}
			ftp.logout();
		} catch (FtpExceptionWrapper e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testFtpClientWrapper_2() {
//		FtpConfigWrapper c = getConfig();
//		FtpClientWrapper ftp = new FtpClientWrapper(c);
//		try {
//			ftp.login();
//			System.out.println("---------");
//			List<FtpFileWrapper> listing = ftp.listing(Paths.get("/SPAM/INFOHUB/to-process/tcustomer*"));
//			listing.forEach((FtpFileWrapper f) -> {System.out.println(f.getRemotePath().toString());});
//			System.out.println("---------");
//			
//			Path downloadPath = Paths.get("/usr/local/infohub_store/in");
//			for (FtpFileWrapper f : listing) {
//				Path localPath = downloadPath.resolve(f.getRemotePath().getFileName());
//				ftp.download(f.getRemotePath(), localPath);
//			}
//			
//			Path localDir = Paths.get("/usr/local/infohub_store/out");
//			String[] dirs = new File(localDir.toString()).list();
//			Path remoteDir = Paths.get("/SPAM/INFOHUB/processed/");
//			for (String f : dirs) {
//				Path localFile = localDir.resolve(f);
//				Path remoteFile = remoteDir.resolve(f);
//				ftp.upload(localFile, remoteFile);
//			}
//			ftp.logout();
//		} catch (FtpExceptionWrapper e) {
//			e.printStackTrace();
//		}
//	}


}
