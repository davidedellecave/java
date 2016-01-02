package ddc.core.ftp;

import java.io.IOException;

public interface FtpListener {
	public FtpListenerAction preVisitDirectory(FtpFileWrapper dir) throws IOException;
	public FtpListenerAction postVisitDirectory(FtpFileWrapper dir) throws IOException;
	public FtpListenerAction visitFile(FtpFileWrapper file) throws IOException;
//	public FtpListenerAction visitFileFailed(Path file, IOException exc) throws IOException;
}