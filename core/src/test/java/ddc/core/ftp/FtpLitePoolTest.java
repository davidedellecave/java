package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;

public class FtpLitePoolTest {
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
	
	@Test
	public void testFtpLitePool() throws NoSuchElementException, IllegalStateException, Exception {
//		FtpLitePool pool = new FtpLitePool(getConfig());
		
		FtpLiteClientPool pool = new FtpLiteClientPool(getConfig(), 4);
		
		FtpLiteClient c = pool.hold();
		
		c.listing();
		
		pool.release(c);		
		
		c = pool.hold();
		
		c.listing();
		
		pool.release(c);
		
		pool.close();
		
		
		
	}

}
