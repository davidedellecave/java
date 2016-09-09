package ddc.core.tfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;

import ddc.util.Chronometer;

public class FileTransformer {
	// private static final Logger logger =
	// LogManager.getLogger(FileTransformer.class);
	private static final int BUFFERSIZE = 64 * 1024;// * 1024;
	private TFileContext context;
	private ProxyFilter proxyFilter = new ProxyFilter();

	private long lineCounter = 0;
	private long charCounter = 0;

	public FileTransformer(TFileContext context) {
		super();
		this.context = context;
	}

	public void execute() throws TFileException {
		try {
			context.setChron(new Chronometer());
			context.getChron().start();
			Path source = context.getSource();
			if (source.getFileName().toString().endsWith("zip")) {
				doExecuteZip(source);
			} else if (source.getFileName().toString().endsWith("gz")){
				doExecuteGZip(source);
			} else {
				doExecuteFile(source);
			}
		} catch (IOException e) {
			throw new TFileException(e);
		} finally {
			context.getChron().stop();
		}
	}

	private void doExecuteGZip(Path path) throws IOException, TFileException {
		InputStream is = null;
		GZIPInputStream in = null;
		try {			
			is = new FileInputStream(path.toString());
			in = new GZIPInputStream(is);
			doExecuteFile(in);
		} finally {
			if (is != null)
				is.close();
			if (in != null)
				in.close();
		}
	}
	
	private void doExecuteZip(Path path) throws IOException, TFileException {
		try (FileSystem fs = FileSystems.newFileSystem(path, null)) {
			Path rootZip = fs.getPath("/");
			Files.walkFileTree(rootZip, new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						doExecuteFile(file);
					} catch (TFileException e) {
						throw new IOException(e);
					}
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}
	
	private void doExecuteFile(InputStream source) throws IOException, TFileException {
		try {
			onOpen();
			onRead(source);
		} finally {
			onClose();
		}
	}
	
	private void doExecuteFile(Path source) throws IOException, TFileException {
		try {
			onOpen();
			try (ReadableByteChannel channel = Files.newByteChannel(source, StandardOpenOption.READ)) {
				onRead(channel);
			}
		} finally {
			onClose();
		}
	}
	
	private void onOpen() throws IOException, TFileException {
		proxyFilter.onOpen(context);
	}
	
	private void onClose() throws IOException, TFileException {
		proxyFilter.onClose(context);
	}

	private void onRead(InputStream source) throws IOException, TFileException {
		try (ReadableByteChannel channel = Channels.newChannel(source)) {
			onRead(channel);
		}
	}

	private void onRead(ReadableByteChannel inChannel) throws IOException, TFileException {
		StringBuilder lineBuffer = new StringBuilder(BUFFERSIZE);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFERSIZE);
		int bytesRead = inChannel.read(buffer);

		boolean isEndLineFound = true;
		lineCounter = 0;
		charCounter = -1;
		while (bytesRead != -1) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				char ch = (char) buffer.get();
				charCounter++;
				if (isEndLineFound) {
					lineCounter++;
					isEndLineFound = false;
				}
				ch = proxyFilter.onChar(lineCounter, lineBuffer.length(), ch);
				if (ch == '\0') {
					continue;
				}
				if (ch != context.getSourceEOL()) {
					lineBuffer.append(ch);
				} else {
					// if new line
					isEndLineFound = true;
					proxyFilter.onTransformLine(lineCounter, lineBuffer);
					lineBuffer.delete(0, lineBuffer.length());
				}
			}
			buffer.clear();
			bytesRead = inChannel.read(buffer);
		}
	}

	private class ProxyFilter implements TFileFilter, TFileContextListener {

		@Override
		public void onTransformLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
			context.setLineTotal(lineNumber);
			context.setCharTotal(charCounter);
			context.getFilter().onTransformLine(lineNumber, lineBuffer);
		}

		@Override
		public char onChar(long lineNumber, long position, char ch) throws TFileException {
			context.setLineTotal(lineNumber);
			context.setCharTotal(charCounter);
			return context.getFilter().onChar(lineNumber, position, ch);
		}

		@Override
		public void onError(TFileLineError error) throws TFileException {
			context.getFilter().onError(error);

		}

		@Override
		public void onOpen(TFileContext context) throws TFileException {
			context.setLineTotal(0);
			context.setCharTotal(0);
			if (context.getFilter() instanceof TFileContextListener) {
				((TFileContextListener) context.getFilter()).onOpen(context);
			}
		}

		@Override
		public void onClose(TFileContext context) throws TFileException {
			if (context.getFilter() instanceof TFileContextListener) {
				((TFileContextListener) context.getFilter()).onClose(context);
			}
		}

		@Override
		public void onTransformLine(long lineNumber, StringBuilder lineBuffer, String[] fields)
				throws TFileException {
			// TODO Auto-generated method stub
			
		}
	}
}
