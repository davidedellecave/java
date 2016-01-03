package ddc.core.ftp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ddc.core.ftp.matcher.AndMatcher;
import ddc.core.ftp.matcher.OrMatcher;
import ddc.core.ftp.matcher.PathMatcher;
import ddc.core.ftp.matcher.RelativeDateMatcher;
import ddc.util.FilePair;
import ddc.util.FileUtils;

public class ParallelFtpClientTest {
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
	
	private FtpLiteClient getClient() {
		FtpLiteClient c = new FtpLiteClient(getConfig());
		return c;
	}
	
	@Test
	public void testDownload() throws FtpLiteException {
		FtpLiteClient c = getClient();
		c.connect();
		FtpFileMatcher m = new AndMatcher(new OrMatcher(new PathMatcher("/log/"), new PathMatcher("/App_Data/")), new RelativeDateMatcher("-7 DAYS, -20 MINUTES")) ;
		List<FtpLiteFile> list = c.listing(WORKINGPATH, m, true);
		c.disconnect();
		
		
		List<FilePair> pairs = new LinkedList<>();
		for (FtpLiteFile f : list) {
			Path source = f.getPath();
			Path target = getLocalFile(Paths.get("/Users/dellecave/data/test"), getConfig().getFtpServer().getHost(), f.getPath());
			System.out.println(target);
			pairs.add(new FilePair(source, target));
		}
		ParallelFtpClient p = new ParallelFtpClient(getConfig(), 4);
		p.download(pairs);
		
	}
	
	private Path getLocalFile(Path localPath, String host, Path remoteFile) {
		
		Path remotePath = remoteFile.getParent();
		String remoteFilename = remoteFile.getFileName().toString();
		
		String path = FileUtils.appendSeparator(localPath.toString(), "/");
		
		if (host!=null && host.length()>0) {
			path += FileUtils.appendSeparator(host, "/"); 
		}		
		
		if (remotePath!=null) {
			path +=  FileUtils.appendSeparator(remotePath.toString(), "/");
		} else {
			String removedRemoteBasePath = StringUtils.removeStart(localPath.toString(), remotePath.toString());
			path +=  FileUtils.appendSeparator(removedRemoteBasePath, "/");
		}		
		//path +=  FileUtils.appendSeparator(task.currentPath, "/");
		path = StringUtils.replace(path, "\\", "/");
		path = StringUtils.replace(path, "//", "/");
		//System.out.println("Local path:" + path);
		FileUtils.createWritebleFolder(path);
		return Paths.get(path, remoteFilename);
	}

}
