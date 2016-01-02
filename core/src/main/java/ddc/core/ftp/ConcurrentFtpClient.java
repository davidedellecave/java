package ddc.core.ftp;

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
			new FtpLiteException(e);
		}
	}
	
	public void upload(List<FilePair> list) {
		try {
			doUpload(list);
		} catch (InterruptedException e) {
			new FtpLiteException(e);
		}
	}

	private void doDownload(List<FilePair> list) throws InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(maxConnection);
		for (FilePair p : list) {
			Runnable r = new Runnable() {
				public void run() {
					FtpLiteConfig c = new FtpLiteConfig();
					c.setBinaryTransfer(true);
					c.setPassiveMode(false);
					c.setPreserveRemoteTimestamp(true);
					c.setOverwriteLocal(true);
					c.setOverwriteRemote(true);
					c.setFtpServer(server);
					FtpLiteClient client = new FtpLiteClient(c);
					try {
						client.connect();
						client.download(p.source, p.target);
					} catch (FtpLiteException e) {
					} finally {
						if (client!=null)
							try {
								client.disconnect();
							} catch (FtpLiteException e) {}
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
					FtpLiteConfig c = new FtpLiteConfig();
					c.setBinaryTransfer(true);
					c.setPassiveMode(false);
					c.setPreserveRemoteTimestamp(true);
					c.setOverwriteLocal(true);
					c.setOverwriteRemote(true);
					c.setFtpServer(server);
					FtpLiteClient client = new FtpLiteClient(c);
					try {
						client.connect();
						client.upload(p.source, p.target);
					} catch (FtpLiteException e) {
					} finally {
						if (client!=null)
							try {
								client.disconnect();
							} catch (FtpLiteException e) {}
					}
				}
			};
			executor.execute(r);
		};
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}


}
