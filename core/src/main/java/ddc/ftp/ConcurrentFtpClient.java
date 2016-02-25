package ddc.ftp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ddc.util.FilePair;

public class ConcurrentFtpClient {
	
	private int maxConnection=1;
	private FtpServer server = null;
	
	public void download(List<FilePair> list) {
		try {
			doDownload(list);
		} catch (InterruptedException e) {
			new FtpExceptionWrapper(e);
		}
	}
	
	public void upload(List<FilePair> list) {
		try {
			doUpload(list);
		} catch (InterruptedException e) {
			new FtpExceptionWrapper(e);
		}
	}

	private void doDownload(List<FilePair> list) throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(maxConnection);
		for (FilePair p : list) {
			Runnable r = new Runnable() {
				public void run() {
					FtpConfigWrapper c = new FtpConfigWrapper();
					c.setBinaryTransfer(true);
					c.setPassiveMode(false);
					c.setPreserveRemoteTimestamp(true);
					c.setOverwriteLocal(true);
					c.setOverwriteRemote(true);
					c.setFtpServer(server);
					FtpClientWrapper client = new FtpClientWrapper(c);
					try {
						client.login();
						client.download(p.source, p.target);
					} catch (FtpExceptionWrapper e) {
					} finally {
						if (client!=null)
							try {
								client.logout();
							} catch (FtpExceptionWrapper e) {}
					}
				}
			};
			executor.execute(r);
		};
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}
	
	private void doUpload(List<FilePair> list) throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(maxConnection);
		for (FilePair p : list) {
			Runnable r = new Runnable() {
				public void run() {
					FtpConfigWrapper c = new FtpConfigWrapper();
					c.setBinaryTransfer(true);
					c.setPassiveMode(false);
					c.setPreserveRemoteTimestamp(true);
					c.setOverwriteLocal(true);
					c.setOverwriteRemote(true);
					c.setFtpServer(server);
					FtpClientWrapper client = new FtpClientWrapper(c);
					try {
						client.login();
						client.upload(p.source, p.target);
					} catch (FtpExceptionWrapper e) {
					} finally {
						if (client!=null)
							try {
								client.logout();
							} catch (FtpExceptionWrapper e) {}
					}
				}
			};
			executor.execute(r);
		};
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}


}
