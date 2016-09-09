package ddc.core.tfile.filter;

import org.apache.log4j.Logger;

import ddc.core.tfile.TFileException;

public class StopAtLineFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(StopAtLineFilter.class);
	private long stopAtLine = -1;

	public StopAtLineFilter(long stopAtLine) {
		this.stopAtLine = stopAtLine;
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		if (lineNumber>=stopAtLine) {
			logger.info("onEndLine - #:[" + lineNumber + "] line:[" + sourceLine + "]");
			throw new TFileException("Reached the end line:[" + stopAtLine + "]");
		}
	}
}
