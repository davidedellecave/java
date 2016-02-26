package ddc.core.tfile.filter;

import org.apache.commons.lang3.StringUtils;

import ddc.core.tfile.TFileException;
import ddc.core.tfile.TFileFilter;
import ddc.core.tfile.TFileLineError;

public class VerboseFilter implements TFileFilter {
	private boolean traceChar = false;
	private boolean traceLine = true;
	private String separator = "\t";
	private long traceLineFrom = -1;
	private long traceLineTo = -1;

	public VerboseFilter(boolean traceChar, boolean traceLine) {
		super();
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		System.out.println("Verbose enabled - char:[" + traceChar + "] line:[" + traceLine + "]");
	}

	public VerboseFilter(boolean traceChar, boolean traceLine, String separator) {
		super();
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		this.separator = separator;
		System.out.println(
				"Verbose enabled - char:[" + traceChar + "] line:[" + traceLine + "] line:[" + traceLine + "]");
	}

	public VerboseFilter(boolean traceChar, boolean traceLine, String separator, long traceLineFrom, long traceLineTo) {
		this.traceChar = traceChar;
		this.traceLine = traceLine;
		this.separator = separator;
		this.traceLineFrom = traceLineFrom;
		this.traceLineTo = traceLineTo;
	}

	@Override
	public StringBuilder onStartLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		return lineBuffer;
	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		if (traceChar)
			System.out.println("OnChar - line#:[" + lineNumber + "] position:[" + position + "] char:[" + ch + "] int:"
					+ (int) ch);
		return ch;
	}

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		if (traceLine)
			if (traceLineFrom == -1 || traceLineTo == -1)
				trace(lineNumber, lineBuffer);
			else if (traceLineFrom <= lineNumber && lineNumber <= traceLineTo)
				trace(lineNumber, lineBuffer);
		return lineBuffer;
	}

	private void trace(long lineNumber, StringBuilder lineBuffer) {
		if (separator != null) {
			StringBuilder s = new StringBuilder(256);
			String[] toks = StringUtils.split(lineBuffer.toString(), separator);
			for (int i = 0; i < toks.length; i++) {
				s.append(i + ":[").append(toks[i]).append("] ");
			}
			System.out.println("onEndLine - #:[" + lineNumber + "] line:[" + s + "]");
		} else {
			System.out.println("onEndLine - #:[" + lineNumber + "] line:[" + lineBuffer + "]");
		}
	}

	@Override
	public void onError(TFileLineError error) throws TFileException {
		System.out.println("Error - line#:[" + error.getLine() + "] line:[" + error.getSource() + "] exception:[" + error.getException().getMessage() + "]");
	}

}
