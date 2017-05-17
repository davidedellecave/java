package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ddc.commons.ftp.FtpFileMatcher;
import ddc.commons.ftp.FtpLiteClient;
import ddc.commons.ftp.FtpLiteConfig;
import ddc.commons.ftp.FtpLiteException;
import ddc.commons.ftp.FtpLiteFile;
import ddc.commons.ftp.FtpServer;
import ddc.commons.ftp.ParallelFtpClient;
import ddc.commons.ftp.matcher.AndMatcher;
import ddc.commons.ftp.matcher.OrMatcher;
import ddc.commons.ftp.matcher.PathMatcher;
import ddc.commons.ftp.matcher.RelativeDateMatcher;
import ddc.util.FilePair;
import ddc.util.FileUtils;

public class ParallelFtpClientTest {
	private final Path WORKINGPATH = Paths.get("/");
	private final int MAX_CONNECTION = 4;

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

	private FtpLiteClient getClient() {
		FtpLiteClient c = new FtpLiteClient(getConfig());
		return c;
	}

	@Test
	public void testDownload() throws FtpLiteException {
		FtpLiteClient c = getClient();
		c.connect();
		FtpFileMatcher m = new AndMatcher(new OrMatcher(new PathMatcher("/log/"), new PathMatcher("/App_Data/")),
				new RelativeDateMatcher("-7 DAYS, -2 HOURS"));
		List<FtpLiteFile> list = c.listFiles(WORKINGPATH, m, true);
		c.disconnect();

		List<FilePair> pairs = new LinkedList<>();
		for (FtpLiteFile f : list) {
			Path source = f.getPath();
			Path target = getLocalFile(Paths.get("/Users/dellecave/data/test"), getConfig().getFtpServer().getHost(),
					f.getPath());
			System.out.println(target);
			pairs.add(new FilePair(source, target));
		}
		ParallelFtpClient p = new ParallelFtpClient(getConfig(), MAX_CONNECTION);
		p.download(pairs);

	}

	private Path getLocalFile(Path localPath, String host, Path remoteFile) {

		Path remotePath = remoteFile.getParent();
		String remoteFilename = remoteFile.getFileName().toString();

		String path = FileUtils.appendSeparator(localPath.toString(), "/");

		if (host != null && host.length() > 0) {
			path += FileUtils.appendSeparator(host, "/");
		}

		path += FileUtils.appendSeparator(remotePath.toString(), "/");
		// path += FileUtils.appendSeparator(task.currentPath, "/");
		path = StringUtils.replace(path, "\\", "/");
		path = StringUtils.replace(path, "//", "/");
		// System.out.println("Local path:" + path);
		FileUtils.createWritebleFolder(path);
		return Paths.get(path, remoteFilename);
	}

}
