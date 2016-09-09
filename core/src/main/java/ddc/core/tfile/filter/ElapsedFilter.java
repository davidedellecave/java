package ddc.core.tfile.filter;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ddc.core.tfile.TFileException;
import ddc.util.Chronometer;

public class ElapsedFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(ElapsedFilter.class);
	private Chronometer chron;

	public ElapsedFilter() {
		chron = new Chronometer(5, TimeUnit.SECONDS);
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {	
		if (chron.isCountdownCycle()) {
			String info = getContext().toString();
			logger.debug("Processing..." + info);
		}
	}

}
