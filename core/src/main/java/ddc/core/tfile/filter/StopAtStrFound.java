package ddc.core.tfile.filter;

import org.apache.log4j.Logger;

import ddc.core.tfile.TFileException;

public class StopAtStrFound extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(StopAtLineFilter.class);
	private String item = null;

	public StopAtStrFound(String item) {
		this.item = item;
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		if (item!=null && sourceLine.indexOf(item)>=0) {
			logger.info("onEndLine - #:[" + lineNumber + "] line:[" + sourceLine + "]");
			throw new TFileException("Found line that contains:[" + item + "]");
		}
	}
}
