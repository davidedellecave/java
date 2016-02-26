package ddc.core.tfile.filter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;

public class FileOutFilter extends BaseTFileFilter {
	private File outFile = null;
	private OutputStream out = null;

	public FileOutFilter(File outFile) {
		this.outFile = outFile;
	}
	
	public FileOutFilter(Path path) {
		this.outFile = path.toFile();
	}

	@Override
	public void onOpen(TFileContext context) throws TFileException {
		super.onOpen(context);
		try {
			out = new BufferedOutputStream(new FileOutputStream(outFile));
		} catch (FileNotFoundException e) {
			throw new TFileException(e);
		}
	}

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		try {
			if (lineBuffer != null && lineBuffer.length() > 0) {
				for (int i = 0; i < lineBuffer.length(); i++) {
					out.write((int) lineBuffer.charAt(i));
				}
				if (getContext().isAppendNewLine())
					out.write((int) getContext().getSourceEOL());
			}
			return lineBuffer;
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
