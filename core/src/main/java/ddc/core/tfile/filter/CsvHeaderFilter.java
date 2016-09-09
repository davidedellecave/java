package ddc.core.tfile.filter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;
import ddc.util.CsvUtil;

/**
 * Split the first csv line (header) and set TFileContext.header property Add
 * this filter before the csv processing filter
 * 
 * @author dellecave
 *
 */
public class CsvHeaderFilter extends BaseTFileFilter {
	private char separator = BaseTFileFilter.TAB;
	private String csvHeaderFile = null;
	private boolean removeHeader = false;

	public CsvHeaderFilter(String csvHeaderFile, char separator, boolean removeHeader) {
		super();
		this.csvHeaderFile=csvHeaderFile;
		this.separator = separator;
		this.removeHeader = removeHeader;
	}

	@Override
	public void onOpen(TFileContext context) throws TFileException {
		super.onOpen(context);
		String header = null;
		// Get header from external file
		header = getHeaderFromFile(Paths.get(csvHeaderFile));
		String[] toks = CsvUtil.getHeader(header, separator);
		getContext().setHeader(toks);
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {
		// Optionally remove header
		if (lineNumber==1 && removeHeader) {
			super.emptyLine(sourceLine);
		}
	}

	private String getHeaderFromFile(Path path) throws TFileException {
		if (!Files.isReadable(path))
			throw new TFileException(
					"CsvHeaderFilter - Header file is not found or not readable - file:[" + csvHeaderFile + "]");
		try {
			return new String(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new TFileException(e);
		}
	}
}
