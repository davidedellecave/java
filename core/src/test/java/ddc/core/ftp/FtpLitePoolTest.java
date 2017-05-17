package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.junit.Test;

import ddc.commons.ftp.FtpLiteClient;
import ddc.commons.ftp.FtpLiteClientPool;
import ddc.commons.ftp.FtpLiteConfig;
import ddc.commons.ftp.FtpServer;

public class FtpLitePoolTest {
	private final Path WORKINGPATH=Paths.get("/");
	private FtpServer getServer() {
		FtpServer s = new FtpServer();
		s.setHost("5.134.124.246");
		s.setUsername("gottardo");
		s.setPassword("");
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
	
	@Test
	public void testFtpLitePool() throws NoSuchElementException, IllegalStateException, Exception {
//		FtpLitePool pool = new FtpLitePool(getConfig());
		
		FtpLiteClientPool pool = new FtpLiteClientPool(getConfig(), 4);
		
		FtpLiteClient c = pool.hold();
		
		c.listFiles();
		
		pool.release(c);		
		
		c = pool.hold();
		
		c.listFiles();
		
		pool.release(c);
		
		pool.close();
		
		
		
	}

}
