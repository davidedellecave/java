package ddc.ftp.downloader2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void download() throws FtpLiteException {
		List<RemotePath> list = config.getRemotePath();
		for (RemotePath remote : list) {
			List<FilePair> pairs = getFilePairs(remote);
			if (config.isTestmode()) {
				testMode(pairs);
			} else {
				doDownload(pairs);
				if (config.isDeleteRemoteEmptyFolder()) {
					doDeleteEmptyFolder(remote);
				}
			}
		}
	}

	private List<FilePair> getFilePairs(RemotePath remote) throws FtpLiteException {
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
			Path target = getLocalFile(Paths.get(config.getLocalPath().getPath()),
					config.getFtpConfig().getFtpServer().getHost(), f.getPath());
			pairs.add(new FilePair(source, target));
		}
		return pairs;
	}

	private void doDownload(List<FilePair> pairs) throws FtpLiteException {
		ParallelFtpClient p = new ParallelFtpClient(config.getFtpConfig(), config.getMaxConnection());
		p.download(pairs);
	}

	private void testMode(List<FilePair> pairs) throws FtpLiteException {
		if (config.getTestmodeOutpath() != null && config.getTestmodeOutpath().length() > 0) {
			Path path = Paths.get(config.getTestmodeOutpath());
			for (FilePair p : pairs) {
				// System.out.println(p.toString());
				try {
					Files.write(path, p.toString().getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					throw new FtpLiteException(e);
				}
			}
		} else {
			for (FilePair p : pairs) {
				System.out.println("Test Mode:" + p.toString());
			}
		}
	}
	// private void doDownload(RemotePath remote) throws FtpLiteException {
	// FtpFileMatcher matcher = remote.getFileSelector();
	// FtpLiteClient client = new FtpLiteClient(config.getFtpConfig());
	// List<FtpLiteFile> files = new ArrayList<>();
	// try {
	// client.connect();
	// Path workingPath = Paths.get(remote.getPath());
	// files = client.listFiles(workingPath, matcher, true);
	// } finally {
	// client.disconnect();
	// }
	// List<FilePair> pairs = new LinkedList<>();
	// for (FtpLiteFile f : files) {
	// Path source = f.getPath();
	// Path target = getLocalFile(Paths.get(config.getLocalPath().getPath()),
	// config.getFtpConfig().getFtpServer().getHost(), f.getPath());
	// pairs.add(new FilePair(source, target));
	// }
	// if (config.isTestmode()) {
	// if (config.getTestmodeOutpath() != null &&
	// config.getTestmodeOutpath().length()>0) {
	// Path path = Paths.get(config.getTestmodeOutpath());
	// for (FilePair p : pairs) {
	//// System.out.println(p.toString());
	// try {
	// Files.write(path, p.toString().getBytes(), StandardOpenOption.APPEND);
	// } catch (IOException e) {
	// throw new FtpLiteException(e);
	// }
	// }
	// } else {
	// for (FilePair p : pairs) {
	// System.out.println("Test Mode:" + p.toString());
	// }
	// }
	// } else {
	// ParallelFtpClient p = new ParallelFtpClient(config.getFtpConfig(),
	// config.getMaxConnection());
	// p.download(pairs);
	// }
	// }

	private void doDeleteEmptyFolder(RemotePath remote) throws FtpLiteException {
		FtpLiteClient client = new FtpLiteClient(config.getFtpConfig());
		List<FtpLiteFile> files = new ArrayList<>();
		try {
			client.connect();
			Path workingPath = Paths.get(remote.getPath());
			files = client.listFiles(workingPath, true, false, true);
			for (FtpLiteFile f : files) {
				if (f.isDirectory() && client.countFiles(f.getPath()) == 0) {
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
