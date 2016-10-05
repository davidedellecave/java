package ddc.core.tfile.filter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;
import ddc.util.CsvUtil;
import ddc.util.DateUtil;

public class FileCsvOutReportFilter extends BaseTFileFilter {
	private File outFile = null;
	
	private char separator = ',';
	private char eol = '\n';

	public FileCsvOutReportFilter(Path path, char separator, char eol) {
		this.outFile = path.toFile();
		this.separator = separator;
		this.eol = eol;
	}

	@Override
	public void onClose(final TFileContext context) throws TFileException {
		super.onClose(context);
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			LinkedHashMap<String, String> m = new LinkedHashMap<String, String>() {
				private static final long serialVersionUID = 1L;

				{
					put("FILENAME", context.getSource().getFileName().toString());
					put("DATE", DateUtil.formatNowToISO());
					put("ITEM_PROCESSED", String.valueOf(context.getProcessed()));
					put("ITEM_SUCCEDEED", String.valueOf(context.getProcessed() - context.getFailed()));
					put("ITEM_FAILED", String.valueOf(context.getFailed()));
				}
			};
			String header = CsvUtil.getHeader(m, separator) + eol;
			String line = CsvUtil.getLine(m, separator) + eol;
			out.write(header.getBytes());
			out.write(line.getBytes());
		} catch (IOException e) {
			throw new TFileException(e);
		};
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				throw new TFileException(e);
			}
	}
}