package ddc.core.tfile.filter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;

public class FileOutFilter extends BaseTFileFilter {
	private File outFile = null;
	private OutputStream out = null;
	private boolean enableCompression=false;

	public FileOutFilter(File outFile, boolean enableCompression) {
		this.outFile = outFile;
		this.enableCompression=enableCompression;
	}
	
	public FileOutFilter(Path path, boolean enableCompression) {
		this.outFile = path.toFile();
		this.enableCompression=enableCompression;
	}

	@Override
	public void onOpen(TFileContext context) throws TFileException {
		super.onOpen(context);
		try {
			if (enableCompression) {
				out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
			} else {
				out = new BufferedOutputStream(new FileOutputStream(outFile));					
			}
		} catch (IOException e) {
			throw new TFileException(e);
		}
	}

	@Override	
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		try {
			if (sourceLine.length() > 0) {
				for (int i = 0; i < sourceLine.length(); i++) {
					out.write((int) sourceLine.charAt(i));
				}
				if (getContext().isAppendNewLine())
					out.write((int) getContext().getSourceEOL());
			}
		} catch (IOException e) {
			throw new TFileException(e);
		}
	}

	@Override
	public void onClose(TFileContext context) throws TFileException {
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				throw new TFileException(e);
			}
	}
}
