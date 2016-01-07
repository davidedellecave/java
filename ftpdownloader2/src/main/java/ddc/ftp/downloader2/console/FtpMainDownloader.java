package ddc.ftp.downloader2.console;

import static org.apache.log4j.Logger.getLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ddc.ftp.downloader2.FtpDownloader;
import ddc.ftp.downloader2.Version;


public class FtpMainDownloader extends SpringConsoleApp {
	private Logger logger = getLogger(FtpMainDownloader.class);

	public static void main(String[] args) {
		FtpMainDownloader app = new FtpMainDownloader();
		if (args!=null && args.length>0) {
			app.setArgs(new String[] {args[0]});
		} else {
			app.setArgs(new String[] {"applicationContext.xml"});
		}
		app.run();
	}
	
	@Override
	public void execute() throws InterruptedException {
		DownloadConfigList config = (DownloadConfigList)super.getContext().getBean("FtpDownloaderConfiguration");
		doExecute(config);
	}
	
	private void doExecute(DownloadConfigList config) throws InterruptedException {
		int parallelThread = 1;
		if (config.isEnableParallelExecution()) {
			parallelThread = config.getConfList().size();
		}
		final ExecutorService executor = Executors.newFixedThreadPool(parallelThread);
		for (DownloadConfig p : config.getConfList()) {
			FtpDownloader d = new FtpDownloader(p);
			executor.execute(d);
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
	}

	@Override
	public void infoApplicationHeader() {
		StringBuilder b = new StringBuilder();		
		b.append("\n=======================================\n");		
		b.append(Version.FULL_APP_NAME + "\n");
		b.append("=======================================\n");
		System.out.println(b.toString());
		logger.info(b.toString());
	}

	@Override
	public void infoApplicationFooter() {
		String b = ("=======================================");
		System.out.println(b);
		logger.info(b);
	}

	@Override
	public void errorApplicationUsage() {
		// TODO Auto-generated method stub
		
	}

}
