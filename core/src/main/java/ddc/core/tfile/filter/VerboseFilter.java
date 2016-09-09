package ddc.core.tfile.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ddc.core.tfile.TFileException;
import ddc.core.tfile.TFileLineError;

public class VerboseFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(VerboseFilter.class);
	
	private boolean traceChar = false;
	private boolean traceLine = true;
	private String separator = "\t";
	private long traceLineFrom = -1;
	private long traceLineTo = -1;

	public VerboseFilter(boolean traceChar, boolean traceLine) {
		super();
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		logger.debug("Verbose enabled - char:[" + traceChar + "] line:[" + traceLine + "]");
	}

	public VerboseFilter(boolean traceChar, boolean traceLine, String separator) {
		super();
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		this.separator = separator;
		logger.debug("Verbose enabled - char:[" + traceChar + "] line:[" + traceLine + "] line:[" + traceLine + "]");
	}

	public VerboseFilter(boolean traceChar, boolean traceLine, String separator, long traceLineFrom, long traceLineTo) {
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		this.separator = separator;
		this.traceLineFrom = traceLineFrom;
		this.traceLineTo = traceLineTo;
	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		if (traceChar)
			logger.debug("OnChar - line#:[" + lineNumber + "] position:[" + position + "] char:[" + ch + "] int:" + (int) ch);
		return ch;
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		if (traceLine)
			if (traceLineFrom == -1 || traceLineTo == -1)
				trace(lineNumber, sourceLine);
			else if (traceLineFrom <= lineNumber && lineNumber <= traceLineTo)
				trace(lineNumber, sourceLine);
	}

	private void trace(long lineNumber, StringBuilder lineBuffer) {
		if (separator != null) {
			StringBuilder s = new StringBuilder(256);
			String[] toks = StringUtils.split(lineBuffer.toString(), separator);
			for (int i = 0; i < toks.length; i++) {
				s.append(i + ":[").append(toks[i]).append("] ");
			}
			logger.debug("onEndLine - #:[" + lineNumber + "] line:[" + s + "]");
		} else {
			logger.debug("onEndLine - #:[" + lineNumber + "] line:[" + lineBuffer + "]");
		}
	}

	@Override
	public void onError(TFileLineError error) throws TFileException {
		logger.debug("Error - line#:[" + error.getLine() + "] line:[" + error.getSource() + "] exception:[" + error.getException().getMessage() + "]");
	}

}
