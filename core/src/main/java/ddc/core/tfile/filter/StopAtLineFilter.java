package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class StopAtLineFilter extends BaseTFileFilter {
	private long stopAtLine = -1;

	public StopAtLineFilter(long stopAtLine) {
		this.stopAtLine = stopAtLine;
	}

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		if (lineNumber>=stopAtLine) {
			System.out.println("onEndLine - #:[" + lineNumber + "] line:[" + lineBuffer + "]");
			throw new TFileException("Reached the end line:[" + stopAtLine + "]");
		}
		return lineBuffer;
	}
}
