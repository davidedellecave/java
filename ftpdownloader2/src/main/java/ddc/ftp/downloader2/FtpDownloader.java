package ddc.ftp.downloader2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import ddc.core.ftp.FtpFileMatcher;
import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLiteException;
import ddc.core.ftp.FtpLiteFile;
import ddc.core.ftp.ParallelFtpClient;
import ddc.ftp.downloader2.console.DownloadConfig;
import ddc.ftp.downloader2.console.RemotePath;
import ddc.util.FilePair;
import ddc.util.FileUtils;

public class FtpDownloader implements Runnable {
	private final static Logger logger = Logger.getLogger(FtpDownloader.class);
	private DownloadConfig config = null;

	public FtpDownloader(DownloadConfig config) {
		super();
		this.config = config;
	}

	public void run() {
		try {
			download();
		} catch (FtpLiteException e) {
			logger.error(e.getMessage());
		}
	}

	public void download() throws FtpLiteException {
		List<RemotePath> list = config.getRemotePath();
		for (RemotePath remote : list) {
			doDownload(remote);
			if (config.isDeleteRemoteEmptyFolder()) {
				doDeleteEmptyFolder(remote);
			}
		}
	}

	public void doDownload(RemotePath remote) throws FtpLiteException {
		FtpFileMatcher matcher = remote.getFileSelector();
		FtpLiteClient client = new FtpLiteClient(config.getFtpConfig());
		List<FtpLiteFile> files = new ArrayList<>();
		try {
			client.connect();
			Path workingPath = Paths.get(remote.getPath());
			files = client.listFiles(workingPath, matcher, true);
		} finally {
			client.disconnect();
		}
		List<FilePair> pairs = new LinkedList<>();
		for (FtpLiteFile f : files) {
			Path source = f.getPath();
			Path target = getLocalFile(Paths.get(config.getLocalPath().getPath()),config.getFtpConfig().getFtpServer().getHost(),f.getPath());
//			System.out.println(target);
			pairs.add(new FilePair(source, target));
		}
		ParallelFtpClient p = new ParallelFtpClient(config.getFtpConfig(), config.getMaxConnection());
		p.download(pairs);
	}
	
	public void doDeleteEmptyFolder(RemotePath remote) throws FtpLiteException {
		FtpLiteClient client = new FtpLiteClient(config.getFtpConfig());
		List<FtpLiteFile> files = new ArrayList<>();
		try {
			client.connect();
			Path workingPath = Paths.get(remote.getPath());
			files = client.listFiles(workingPath, true, false, true);		
			for (FtpLiteFile f : files) {
				if (f.isDirectory() && client.countFiles(f.getPath())==0) {
					client.deleteDir(f.getPath());
				}
			}
		} finally {
			client.disconnect();
		}

	}

	private Path getLocalFile(Path localPath, String host, Path remoteFile) {
		String path = FileUtils.appendSeparator(localPath.toString(), "/");
		if (host != null && host.length() > 0) {
			path += FileUtils.appendSeparator(host, "/");
		}
		path += remoteFile.getParent().toString();
		FileUtils.createWritebleFolder(path);
		return Paths.get(path, remoteFile.getFileName().toString());
	}

}
