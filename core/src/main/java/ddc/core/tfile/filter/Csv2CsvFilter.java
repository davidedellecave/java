package ddc.core.tfile.filter;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ddc.core.tfile.CsvParam;
import ddc.core.tfile.TFileException;
import ddc.csv.CsvWriter;

public class Csv2CsvFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(Csv2CsvFilter.class);
	private CsvParam targetParam = null;

	public Csv2CsvFilter(CsvParam targetParam) {
		this.targetParam = targetParam;
	}

	@Override
	public void onTransformLine(long lineNumber, StringBuilder lineBuffer, String[] fields) throws TFileException {
		StringWriter outBuffer = new StringWriter();
		try (CsvWriter w = new CsvWriter(outBuffer)) {
			w.setDelimiter(targetParam.getDelimiter());
			w.setTextQualifier(targetParam.getEnclosing());
			w.setEscapeMode(BACKSLASH);
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].contains("\n")) {
					fields[i] = StringUtils.remove(fields[i], "\n");
				}
				if (fields[i].contains("\r")) {
					fields[i] = StringUtils.remove(fields[i], "\r");
				}
				w.write(fields[i], true);
			}
			super.copyLine(outBuffer.toString(), lineBuffer);
		} catch (IOException e) {
			throw new TFileException(e);
		}

	}
}
