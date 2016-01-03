package ddc.core.ftp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ddc.util.Chronometer;
import ddc.util.FilePair;

public class ParallelFtpClient {
	private Logger logger = Logger.getLogger(ParallelFtpClient.class);
	
	private int maxConnection = 1;
	private FtpLiteClientPool pool = null;

	public ParallelFtpClient(FtpLiteConfig config, int maxConnection) {
		this.maxConnection = maxConnection;
		pool = new FtpLiteClientPool(config, maxConnection);
	}

	public void download(List<FilePair> list) throws FtpLiteException {
		try {
			Chronometer chron = new Chronometer();
			doDownload(list);
			logger.info("Download elapsed:[" + chron + "]");
		} catch (InterruptedException | FtpLiteException e) {
			throw new FtpLiteException(e);
		}
	}

	public void upload(List<FilePair> list) throws FtpLiteException {
		try {
			Chronometer chron = new Chronometer();
			doUpload(list);
			logger.info("Download elapsed:[" + chron + "]");
		} catch (InterruptedException | FtpLiteException e) {
			throw new FtpLiteException(e);
		}
	}

	private void doDownload(List<FilePair> list) throws InterruptedException, FtpLiteException {
		final ExecutorService executor = Executors.newFixedThreadPool(maxConnection);
		for (FilePair p : list) {
			Runnable r = new Runnable() {
				public void run() {
					FtpLiteClient client = null;
					try {
						client = pool.hold();
						client.download(p.source, p.target);
					} catch (FtpLiteException e) {
					} finally {
						try {
							pool.release(client);
						} catch (FtpLiteException e) {}
					}
				}
			};
			executor.execute(r);
		}
		;
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		pool.close();
	}

	private void doUpload(List<FilePair> list) throws InterruptedException, FtpLiteException {
		final ExecutorService executor = Executors.newFixedThreadPool(maxConnection);
		for (FilePair p : list) {
			Runnable r = new Runnable() {
				public void run() {
					FtpLiteClient client = null;
					try {
						client = pool.hold();
						client.upload(p.source, p.target);
					} catch (FtpLiteException e) {
					} finally {
						try {
							pool.release(client);
						} catch (FtpLiteException e) {}
					}
				}
			};
			executor.execute(r);
		}
		;
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		pool.close();
	}

}
