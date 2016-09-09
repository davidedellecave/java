package ddc.core.tfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;


import ddc.csv.CsvReader;
import ddc.util.Chronometer;

public class FileTransformer2 {
	private static final int BUFFERSIZE = 64 * 1024;// * 1024;
	private TFileContext context;
	private ProxyFilter proxyFilter = new ProxyFilter();
	//stats
	private long lineCounter = 0;
	private long charCounter = 0;
	//csv
	private boolean csvEnabled=false;
	private char csvDelimiter = ',';
	private char csvEnclose = '"';
	private CsvEscapeMode csvEscapeMode = CsvEscapeMode.ESCAPE_MODE_DOUBLED;
	public enum CsvEscapeMode {ESCAPE_MODE_DOUBLED, ESCAPE_MODE_BACKSLASH}; 
	//
	
	public FileTransformer2(TFileContext context, char csvDelimiter, char csvEnclose, CsvEscapeMode csvEscapeMode) {
		super();
		this.csvEnabled=true;
		this.context = context;
		this.csvDelimiter = csvDelimiter;
		this.csvEnclose = csvEnclose;
		this.csvEscapeMode = csvEscapeMode;
	}

	public FileTransformer2(TFileContext context) {
		super();
		this.csvEnabled=false;
		this.context = context;
	}
	
	public void execute() throws TFileException {
		try {
			context.setChron(new Chronometer());
			context.getChron().start();
			Path source = context.getSource();
			if (source.getFileName().toString().endsWith("zip")) {
				doExecuteZip(source);
			} else if (source.getFileName().toString().endsWith("gz")) {
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
			doExecuteStream(in);
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

	private void doExecuteFile(Path source) throws IOException, TFileException {
		doExecuteStream(new FileInputStream(source.toFile()));
	}

	private void doExecuteStream(InputStream source) throws IOException, TFileException {
		try {
			onOpen();
			if (csvEnabled) {
				parseCsv(source);
			} else {
				parseLine(source);
			}
		} finally {
			onClose();
		}
	}

	private void parseCsv(InputStream source) throws IOException, TFileException {
		try (CsvReader r = new CsvReader(new InputStreamReader(source))) {
			r.setDelimiter(this.csvDelimiter);
			r.setTextQualifier(this.csvEnclose);
			r.setEscapeMode(this.csvEscapeMode.equals(CsvEscapeMode.ESCAPE_MODE_DOUBLED) ? CsvReader.ESCAPE_MODE_DOUBLED : CsvReader.ESCAPE_MODE_BACKSLASH);
			StringBuilder lineBuffer = new StringBuilder(BUFFERSIZE);
			lineCounter = 0;
			charCounter = -1;
			while (r.readRecord()) {
				lineCounter++;
				String record = r.getRawRecord();
				for (int i=0; i<record.length(); i++) {
					charCounter++;
					char ch = record.charAt(i);
					ch = proxyFilter.onChar(lineCounter, lineBuffer.length(), ch);
					if (ch == '\0') {
						continue;
					}
					lineBuffer.append(ch);
				}
				proxyFilter.onTransformLine(lineCounter, lineBuffer, r.getValues());
				proxyFilter.onTransformLine(lineCounter, lineBuffer);
				lineBuffer.delete(0, lineBuffer.length());
			}
		}
	}

	private void parseLine(InputStream source) throws IOException, TFileException {
		try (ReadableByteChannel channel = Channels.newChannel(source)) {
			StringBuilder lineBuffer = new StringBuilder(BUFFERSIZE);
			ByteBuffer buffer = ByteBuffer.allocate(BUFFERSIZE);
			int bytesRead = channel.read(buffer);

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
					if (ch != '\n' && ch != '\r') {
						lineBuffer.append(ch);
					} else {
						// new line
						isEndLineFound = true;
						proxyFilter.onTransformLine(lineCounter, lineBuffer);
						lineBuffer.delete(0, lineBuffer.length());
					}
				}
				buffer.clear();
				bytesRead = channel.read(buffer);
			}
		}
	}

	private void onOpen() throws IOException, TFileException {
		proxyFilter.onOpen(context);
	}

	private void onClose() throws IOException, TFileException {
		proxyFilter.onClose(context);
	}

	public static final char EMPTY_CHAR = '\0';

	private class ProxyFilter implements TFileFilter, TFileContextListener {

		@Override
		public void onTransformLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
			context.setLineTotal(lineNumber);
			context.setCharTotal(charCounter);
			context.getFilter().onTransformLine(lineNumber, lineBuffer);
		}

		@Override
		public void onTransformLine(long lineNumber, StringBuilder lineBuffer, String[] fields) throws TFileException {
			context.setLineTotal(lineNumber);
			context.setCharTotal(charCounter);
			context.getFilter().onTransformLine(lineNumber, lineBuffer, fields);
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

	}
}
