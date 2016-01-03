package ddc.core.ftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.log4j.Logger;

import ddc.core.ftp.matcher.TrueMatcher;
import ddc.util.Chronometer;
import ddc.util.FileUtils;

public class FtpLiteClient {
	private final static Logger logger = Logger.getLogger(FtpLiteClient.class);
	private final static String WORKING_PREFIX_UPLOAD = "ftpuploading__";
	private final static String WORKING_PREFIX_DOWNLOAD = "ftpdownloading__";
	private final static String DELETE_PREFIX = "deleted__";
	private String INFO = "FtpClient - ";

	private FtpLiteConfig config = null;
	private FTPClient ftp;
	private boolean CONF_useEpsvWithIPv4 = false;

	public FtpLiteClient(FtpLiteConfig config) {
		super();
		this.config = config;
		INFO += config.getFtpServer().getHost() + " - ";
	}

	synchronized public boolean isConnected() {
		if (ftp==null) return false;
		return ftp.isConnected();
	}

	synchronized public void connect() throws FtpLiteException {
		try {
			logger.info(INFO + "Connecting...");
			Chronometer chron = new Chronometer();
			doConnect();
			logger.info(INFO + "Connecting ok - elapsed:[" + chron.toString() + "]");
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public void disconnect() throws FtpLiteException {
		try {
			logger.info(INFO + "Disconnecting...");
			Chronometer chron = new Chronometer();
			doDisconnect();
			logger.info(INFO + "Disconnecting ok - elapsed:[" + chron.toString() + "]");
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	/**
	 * Return true if exists exactly one file on remote system
	 * 
	 * @param remotePath
	 * @return
	 * @throws FtpLiteException
	 */
	synchronized public boolean exists(Path remotePath) throws FtpLiteException {
		try {
			logger.info(INFO + "Existing - file:[" + remotePath + "]...");
			Chronometer chron = new Chronometer();
			boolean exists = doExists(remotePath);
			logger.info(INFO + "Existing - file:[" + remotePath + "] exists:[" + exists + "] elapsed:[" + chron.toString() + "]");
			return exists;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	/**
	 * Return list of file from working path
	 */
	synchronized public List<FtpLiteFile> listing() throws FtpLiteException {
		return listing(Paths.get(config.getWorkingPath()));
	}

	synchronized public List<FtpLiteFile> listing(Path remotePath) throws FtpLiteException {
		try {
			logger.info(INFO + "Listing :[" + remotePath + "]...");
			Chronometer chron = new Chronometer();
			List<FtpLiteFile> list = doListing(remotePath);
			logListing(list);
			long bytes = list.stream().mapToLong(x -> x.getSize()).sum();
			logger.info(INFO + "Listing ok:[" + remotePath + "] #:[" + list.size() + "] bytes:[" + FileUtils.byteCountToDisplaySize(bytes) + "] elapsed:[" + chron.toString() + "]");
			return list;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public List<FtpLiteFile> listing(Path remotePath, boolean includeDir, boolean includeFile, boolean isRecursive) throws FtpLiteException {
		try {
			logger.info(INFO + "Listing :[" + remotePath + "] recursive:[" + isRecursive + "]...");
			Chronometer chron = new Chronometer();
			List<FtpLiteFile> list = doListing(remotePath, includeDir, includeFile, new TrueMatcher(), isRecursive);
			logListing(list);
			long bytes = list.stream().mapToLong(x -> x.getSize()).sum();
			logger.info(INFO + "Listing ok:[" + remotePath + "] #:[" + list.size() + "] bytes:[" + FileUtils.byteCountToDisplaySize(bytes) + "] elapsed:[" + chron.toString() + "]");
			return list;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}
	
	synchronized public List<FtpLiteFile> listing(Path remotePath, FtpFileMatcher matcher, boolean isRecursive) throws FtpLiteException {		
		try {
			logger.info(INFO + "Listing :[" + remotePath + "] matcher:[" + matcher.toString() + "] recursive:[" + isRecursive + "]...");
			Chronometer chron = new Chronometer();
			List<FtpLiteFile> list = doListing(remotePath, false, true, matcher, isRecursive);
			logListing(list);
			long bytes = list.stream().mapToLong(x -> x.getSize()).sum();
			logger.info(INFO + "Listing ok:[" + remotePath + "] #:[" + list.size() + "] bytes:[" + FileUtils.byteCountToDisplaySize(bytes) + "] elapsed:[" + chron.toString() + "]");
			return list;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}
	
	private void logListing(List<FtpLiteFile> list) {
		if (logger.isDebugEnabled()) {
			for (FtpLiteFile f : list) {
				logger.debug(INFO + "listing - "+ f.toString() + "]");
			}
		}
	}
	
	private List<FtpLiteFile> doListing(Path remotePath, boolean includeDir, boolean includeFile,  FtpFileMatcher matcher, boolean isRecursive) throws IOException  {
		final List<FtpLiteFile> list = new LinkedList<>();
		if (isRecursive) {
			recursiveListing(remotePath, new FtpListener() {				
				@Override
				public FtpListenerAction visitFile(FtpLiteFile file) throws IOException {
					if (includeFile && matcher.accept(file)) list.add(file);
					return FtpListenerAction.CONTINUE;
				}
				@Override
				public FtpListenerAction preVisitDirectory(FtpLiteFile dir) throws IOException {
					if (includeDir) list.add(dir);
					return FtpListenerAction.CONTINUE;
				}
				@Override
				public FtpListenerAction postVisitDirectory(FtpLiteFile dir) throws IOException {
					return FtpListenerAction.CONTINUE;
				}
			});
			return list;
		} else {
			return doListing(remotePath, includeDir, includeFile, new TrueMatcher());
		}	
	}

	synchronized public void listing(Path remotePath, FtpListener listener) throws FtpLiteException {
		try {
			logger.info(INFO + "Recursive listing...");
			Chronometer chron = new Chronometer();
			recursiveListing(remotePath, listener);
			logger.info(INFO + "Recursive listing ok - elapsed:[" + chron.toString() + "]");
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}
	
	synchronized public FtpLiteFile upload(Path localPath, Path remotePath) throws FtpLiteException {
		try {
			logger.info(INFO + "Uploading...:[" + localPath + "] > [" + remotePath + "]");
			Chronometer chron = new Chronometer();
			FtpLiteFile remote = doUpload(localPath, remotePath);
			String info = remote != null ? remote.toString() : "";
			logger.info(INFO + "Uploading ok - local:[" + localPath + "] > remote:[" + info + "] elapsed:["
					+ chron.toString() + "]");
			return remote;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public void rename(Path remotePath, Path remotePath2) throws FtpLiteException {
		try {
			logger.info(INFO + "Renaming...:[" + remotePath + "] > [" + remotePath2 + "]");
			Chronometer chron = new Chronometer();
			doRename(remotePath, remotePath2);
			logger.info(INFO + "Renaming ok:[" + remotePath + "] > [" + remotePath2 + "] elapsed:[" + chron.toString()
					+ "]");
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public FtpLiteFile download(Path remotePath, Path localPath) throws FtpLiteException {
		return download(remotePath, localPath, false);
	}

	synchronized public FtpLiteFile download(Path remotePath, Path localPath, boolean deleteRemote)
			throws FtpLiteException {
		try {
			logger.info(INFO + "Downloading ...:[" + remotePath + "] > [" + localPath + "]");
			Chronometer chron = new Chronometer();
			FtpLiteFile remote = doDownload(remotePath, localPath, deleteRemote);
			String info = remote != null ? remote.toString() : "";
			logger.info(INFO + "Downloading ok - remote:[" + info + "] > local:[" + localPath + "] elapsed:["
					+ chron.toString() + "]");
			return remote;
		} catch (Exception e) {
			throw new FtpLiteException(e);
		}
	}

	private boolean terminateRecursion = false;
	private void recursiveListing(Path remotePath, FtpListener listener) throws IOException {
		if (terminateRecursion) return;
		boolean skipSubtree = false;
		FtpListenerAction next = FtpListenerAction.CONTINUE;
		List<FtpLiteFile> list = doListing(remotePath, true, true, new TrueMatcher());
		for (FtpLiteFile f : list) {
			if (terminateRecursion)
				return;
			if (f.isDirectory() && !skipSubtree) {
				next = listener.preVisitDirectory(f);
				if (next.equals(FtpListenerAction.TERMINATE)) {
					logger.info(INFO + "Recursive listing - TERMINATE is requested");
					terminateRecursion = true;
					return;
				}
				if (next.equals(FtpListenerAction.SKIP_SIBLINGS)) {
					logger.info(INFO + "Recursive listing - SKIP_SIBLINGS is requested");
					return;
				}
				if (next.equals(FtpListenerAction.SKIP_SUBTREE)) {
					skipSubtree = true;
					logger.info(INFO + "Recursive listing - SKIP_SUBTREE is requested");
					listener.postVisitDirectory(f);
				}
				if (next.equals(FtpListenerAction.CONTINUE)) {
					recursiveListing(f.getPath(), listener);
					next = listener.postVisitDirectory(f);
					if (next.equals(FtpListenerAction.TERMINATE)) {
						logger.info(INFO + "Recursive listing - TERMINATE is requested");
						terminateRecursion = true;
						return;
					}
					if (next.equals(FtpListenerAction.SKIP_SIBLINGS)) {
						logger.info(INFO + "Recursive listing - SKIP_SIBLINGS is requested");
						return;
					}
					if (next.equals(FtpListenerAction.SKIP_SUBTREE)) {
						skipSubtree = true;
						logger.info(INFO + "Recursive listing - SKIP_SUBTREE is requested");
						listener.postVisitDirectory(f);
					}
				}
			}
			if (f.isFile()) {
				next = listener.visitFile(f);
				if (next.equals(FtpListenerAction.TERMINATE)) {
					logger.info(INFO + "Recursive listing - TERMINATE is requested");
					terminateRecursion = true;
					return;
				}
				if (next.equals(FtpListenerAction.SKIP_SIBLINGS)) {
					logger.info(INFO + "Recursive listing - SKIP_SIBLINGS is requested");
					return;
				}
				if (next.equals(FtpListenerAction.SKIP_SUBTREE)) {
					skipSubtree = true;
					logger.info(INFO + "Recursive listing - SKIP_SUBTREE is requested");
				}
			}
		}
	}



	// synchronized InputStream openInputStream(Path remotePath, Path localPath)
	// throws FtpExceptionWrapper {
	// try {
	// logger.info(INFO + "Downloading ...:[" + remotePath + "] > [" + localPath
	// + "]");
	// InputStream is = doOpenInputStream(remotePath, localPath);
	// logger.info(INFO + "Downloading ok:[" + remotePath + "] > [" + localPath
	// + "]");
	// return is;
	// } catch (Exception e) {
	// throw new FtpExceptionWrapper(e);
	// }
	// }
	//
	// synchronized public InputStream doOpenInputStream(Path remotePath, Path
	// localPath) throws FtpExceptionWrapper {
	// try {
	// logger.info(INFO + "Downloading ...:[" + remotePath + "] > [" + localPath
	// + "]");
	// doDownload(remotePath, localPath);
	// logger.info(INFO + "Downloading ok:[" + remotePath + "] > [" + localPath
	// + "]");
	// } catch (Exception e) {
	// throw new FtpExceptionWrapper(e);
	// }
	// }

	// System.out.println(ftp.printWorkingDirectory());
	// boolean status = ftp.changeWorkingDirectory("mydirectory");
	// System.out.println("Status of Change Directory:" + status);
	// System.out.println(ftp.printWorkingDirectory());
	// InputStream is = null;
	//
	// System.out.println(ftp.printWorkingDirectory());
	// System.out.println(ftp.isConnected());
	//
	// FTPFile[] list2 = ftp.listFiles();
	// System.out.println("Number of files in this directory:"
	// + list2.length);
	// for (int i = 0; i < list2.length; i++) {
	// System.out.println("-------[" + list2[i].getName()
	// + "]---------------------");
	// is = ftp.retrieveFileStream(list2[i].getName());
	// int bytesRead = 0;
	// byte[] buffer = new byte[1024];
	// while ((bytesRead = is.read(buffer, 0, buffer.length)) > 0) {
	// System.out.println(new String(buffer, 0, bytesRead));
	// }
	// is.close();
	// while(!ftpClient.completePendingCommand());
	// System.out.println("-------[END:" + list2[i].getName()
	// + "]---------------------");
	// }

	synchronized public boolean delete(Path remotePath) throws FtpLiteException {
		return delete(remotePath, config.isRemoteLogicalDelete());
	}

	synchronized public boolean delete(Path remotePath, boolean isLogical) throws FtpLiteException {
		try {
			logger.info(INFO + "Deleting ...:[" + remotePath + "] isLogical:[" + isLogical + "]");
			Chronometer chron = new Chronometer();
			boolean affected = doDelete(remotePath, isLogical);
			logger.info(INFO + "Deleting ok:[" + remotePath + "] isLogical:[" + isLogical + "] elapsed:["
					+ chron.toString() + "]");
			return affected;
		} catch (IOException e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public int deleteAll(Path remotePath, boolean isLogical) throws FtpLiteException {
		try {
			logger.info(INFO + "Deleting ...:[" + remotePath + "] isLogical:[" + isLogical + "]");
			Chronometer chron = new Chronometer();
			int affected = doDeleteAll(remotePath, isLogical);
			logger.info(INFO + "Deleting ok:[" + remotePath + "] isLogical:[" + isLogical + "] elapsed:["
					+ chron.toString() + "]");
			return affected;
		} catch (IOException e) {
			throw new FtpLiteException(e);
		}
	}

	synchronized public boolean equalByTimeAndSize(Path remotePath, Path localPath) throws IOException {
		FtpLiteFile remote = doGetOneRemote(remotePath);
		return equalInTimeAndSize(remote, localPath);
	}

	synchronized public boolean equalInTimeAndSize(FtpLiteFile remote, Path localPath) throws IOException {
		if (remote == null)
			return false;
		if (!Files.exists(localPath))
			return false;
		long localMillis = Files.getLastModifiedTime(localPath).toMillis();
		long localSize = Files.size(localPath);
		long remoteMillis = remote.getTimestamp();
		long remoteSize = remote.getSize();
		return (localMillis == remoteMillis && localSize == remoteSize);
	}

	private boolean doExists(Path remotePath) throws IOException {
		List<FtpLiteFile> list = doListing(remotePath);
		return (list.size() == 1);
	}

	private FtpLiteFile doGetOneRemote(Path remotePath) throws IOException {
		List<FtpLiteFile> list = doListing(remotePath);
		if (list.size() == 1)
			return list.get(0);
		return null;
	}

	private void setTransferListener(final String filename, final long streamBytes) {
		CopyStreamAdapter streamListener = new CopyStreamAdapter() {
			private final Chronometer chron = new Chronometer(30000);

			@Override
			public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
				if (chron.isCountdownCycle()) {
					int percent = (int) (totalBytesTransferred * 100 / streamBytes);
					System.out.println("Downloading:[" + filename + "] ..." + percent + "%");
				}
			}
		};
		ftp.setCopyStreamListener(streamListener);
	}

	private int doDeleteAll(Path remotePath, boolean isLogical) throws IOException {
		int success = 0;
		List<FtpLiteFile> list = doListing(remotePath);
		for (FtpLiteFile f : list) {
			if (doDelete(f.getPath(), isLogical))
				success++;
		}
		logger.info(INFO + "Deleting - expected#:[" + list.size() + "] affected#:[" + success + "]");
		return success;
	}

	private boolean doDelete(Path remotePath, boolean isLogical) throws IOException {
		boolean done = false;
		if (isLogical) {
			Path target = FileUtils.prefixFileName(remotePath, DELETE_PREFIX);
			done = ftp.rename(remotePath.toString(), target.toString());
			logger.warn(INFO + "Deleting logically (renamed) - file:[" + target.toString() + "]");
		} else {
			done = ftp.deleteFile(remotePath.toString());
			logger.warn(INFO + "Deleting - file:[" + remotePath + "]");
		}
		return done;
	}

	private FtpLiteFile doDownload(Path remotePath, Path localPath, boolean deleteRemote)
			throws IOException, FtpLiteException {
		OutputStream output = null;
		try {
			FtpLiteFile remoteFile = doGetOneRemote(remotePath);
			if (remoteFile == null) {
				throw new FtpLiteException(INFO + "Downloading - remote file is not found :[" + remotePath + "]");
			}
			// check if local file exists
			if (Files.exists(localPath)) {
				if (equalInTimeAndSize(remoteFile, localPath)) {
					logger.info(
							INFO + "Downloading - skipped because local and remote files are equal in time and size");
					return remoteFile;
				}
				if (!config.isOverwriteLocal()) {
					throw new FtpLiteException(INFO
							+ "Downloading - Local file already exists but cannot overwrite by configuration settings (see overwriteLocal parameter)");
				} else {
					logger.info(INFO + "Downloading - Local file already exists than deleting it - file:[" + localPath
							+ "]");
					Files.delete(localPath);
				}
			}
			Path tmpPath = FileUtils.prefixFileName(localPath, WORKING_PREFIX_DOWNLOAD);
			if (Files.exists(tmpPath)) {
				Files.delete(tmpPath);
			}
			long remoteBytes = remoteFile.getSize();
			long remoteTimestamp = remoteFile.getTimestamp();
			setTransferListener(remoteFile.getPath().getFileName().toString(), remoteBytes);
			output = new FileOutputStream(tmpPath.toString());
			ftp.retrieveFile(remotePath.toString(), output);
			ftp.setCopyStreamListener(null);
			output.close();
			if (config.isPreserveRemoteTimestamp()) {
				Files.setLastModifiedTime(tmpPath, FileTime.fromMillis(remoteTimestamp));
			}
			Files.move(tmpPath, localPath);
			if (deleteRemote) {
				doDelete(remotePath, config.isRemoteLogicalDelete());
			}
			return remoteFile;
		} finally {
			if (output != null)
				output.close();
		}
	}

	private FtpLiteFile doUpload(Path localPath, Path remotePath) throws IOException, FtpLiteException {
		InputStream input = null;
		try {
			if (!Files.exists(localPath)) {
				throw new FtpLiteException(INFO + "Uploading - local file is not found :[" + localPath + "]");
			}
			// check if remote file exists
			FtpLiteFile remoteFile = doGetOneRemote(remotePath);
			if (remoteFile != null) {
				if (equalInTimeAndSize(remoteFile, localPath)) {
					logger.info(INFO + "Uploading - skipped because local and remote files are equal in time and size");
					return remoteFile;
				}
				if (!config.isOverwriteRemote()) {
					throw new FtpLiteException(INFO
							+ "Uploading - Remote file already exists but cannot overwrite by configuration settings (see overwriteRemote parameter)");
				} else {
					logger.info(
							INFO + "Uploading - Remote file already exists than delete it - file:[" + localPath + "]");
					doDelete(remotePath, false);
				}
			}
			Path tmpPath = FileUtils.prefixFileName(remotePath, WORKING_PREFIX_UPLOAD);
			if (doExists(tmpPath)) {
				doDelete(tmpPath, false);
			}
			input = new FileInputStream(localPath.toString());
			ftp.storeFile(tmpPath.toString(), input);
			input.close();
			doRename(tmpPath, remotePath);
			return remoteFile;
		} finally {
			if (input != null)
				input.close();
		}
	}

	private void doRename(Path remotePath, Path remotePath2) throws IOException {
		ftp.rename(remotePath.toString(), remotePath2.toString());
	}

	private List<FtpLiteFile> doListing(Path remotePath) throws IOException {
		return doListing(remotePath, true, true, new TrueMatcher());
	}

	private List<FtpLiteFile> doListing(Path remotePath, boolean includeDir, boolean includeFile, FtpFileMatcher matcher)
			throws IOException {
		FTPFile[] files = ftp.listFiles(remotePath.toString());
		List<FtpLiteFile> list = new LinkedList<>();
		for (int i = 0; i < files.length; i++) {
			if ((files[i].getType() == FTPFile.DIRECTORY_TYPE && includeDir) || (files[i].getType() == FTPFile.FILE_TYPE && includeFile)) {
				FtpLiteFile f = buildFileWrapper(remotePath, files[i]);
				if (matcher.accept(f)) list.add(f);
			}
		}
		return list;
	}

	private FtpLiteFile buildFileWrapper(Path remotePath, FTPFile file) {
		FtpLiteFile f = new FtpLiteFile();
		f.setPath(remotePath.resolve(file.getName()));
		f.setSize(file.getSize());
		f.setTimestamp(file.getTimestamp().getTimeInMillis());
		f.setType(file.getType() == FTPFile.DIRECTORY_TYPE ? FtpLiteFile.TYPE_DIRECTORY
				: FtpLiteFile.TYPE_FILE);
		return f;
	}
	
	private void doConnect() throws IOException, FtpLiteException {
		if (ftp!=null && ftp.isConnected()) return;
		createConnector();
		connectSocket();
		if (!ftp.login(config.getFtpServer().getUsername(), config.getFtpServer().getPassword())) {
			ftp.logout();
			throw new FtpLiteException(INFO + "Connecting - username or password is wrong");
		}
		logger.info(INFO + "Connecting - Remote system is " + ftp.getSystemType());
		if (config.isBinaryTransfer()) {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
		} else {
			// in theory this should not be necessary as servers should default to ASCII
			// but they don't all do so - see NET-500
			ftp.setFileType(FTP.ASCII_FILE_TYPE);
		}
		if (config.isPassiveMode()) {
			ftp.enterLocalPassiveMode();
			logger.info(INFO + "Connecting - Interaction mode is passive");
		} else {
			ftp.enterLocalActiveMode();
			logger.info(INFO + "Connecting - Interaction mode is active");
		}
		ftp.setUseEPSVwithIPv4(CONF_useEpsvWithIPv4);
	}

	private void doDisconnect() throws IOException {
		ftp.logout();
		disconnectSocket();
	}

	private void createConnector() {
		ftp = new FTPClient();

	}

	private void connectSocket() throws IOException {
		int port = config.getFtpServer().getPort() > 0 ? config.getFtpServer().getPort() : ftp.getDefaultPort();
		ftp.connect(config.getFtpServer().getHost(), port);
		logger.info(INFO + "Connected to " + config.getFtpServer().getHost() + " on " + port);
		// After connection attempt, you should check the reply code to verify
		// success.
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			disconnectSocket();
		}
	}

	private void disconnectSocket() throws IOException {
		if (ftp != null && ftp.isConnected()) {
			ftp.disconnect();
		}
	}

}
